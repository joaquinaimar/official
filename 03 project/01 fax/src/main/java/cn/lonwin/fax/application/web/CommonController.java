package cn.lonwin.fax.application.web;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import cn.lonwin.fax.application.service.CommonService;
import cn.lonwin.fax.domain.entity.PersonEntity;
import cn.lonwin.fax.domain.vo.RegisterVo;
import cn.lonwin.fax.domain.vo.TreeNode;
import cn.lonwin.fax.utils.FaxUtil;

import com.wizard.official.platform.spring.hibernate.io.vo.ResponseVo;
import com.wizard.official.platform.spring.hibernate.utils.PlatformUtil;

@Controller
@RequestMapping("controller/common/")
public class CommonController {

	@Autowired
	private CommonService commonService = null;

	@RequestMapping(value = "register.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseVo<Object> register(RegisterVo register) {
		return commonService.register(register);
	}

	@RequestMapping(value = "main.do", method = RequestMethod.GET)
	public ModelAndView getUserInfo(HttpServletRequest request) {
		Subject currentUser = SecurityUtils.getSubject();
		PersonEntity person = commonService.getUserInfo((String) currentUser
				.getPrincipal());
		HttpSession session = request.getSession();
		if (null != person) {
			session.setAttribute("userInfo", person);
			return new ModelAndView(new RedirectView(request.getContextPath()
					+ "/main"));
		} else {
			session.setAttribute("loginInfo", "登录失败");
			return new ModelAndView(new RedirectView(request.getContextPath()
					+ "/"));
		}
	}

	@RequestMapping(value = "upload.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseVo<Object> upload(HttpServletRequest request) {

		File path = new File(FaxUtil.getFilePath() + "temp\\");
		if (!path.exists())
			path.mkdirs();
		FileItem fileItem = PlatformUtil.getFileItem(request).get(0);

		String upload = fileItem.getName();
		String type = upload.substring(upload.lastIndexOf('.'));
		String uuid = UUID.randomUUID().toString();
		String name = UUID.randomUUID().toString() + type;
		try {
			File file = new File(path.getPath() + "\\" + name);
			file.createNewFile();
			fileItem.write(file);

			switch (upload.substring(upload.lastIndexOf('.'))) {
			case ".doc":
			case ".xls":
			case ".ppt":
				name = uuid + ".pdf";
				FaxUtil.convertOfficeToPdf(file, new File(path.getPath() + "\\"
						+ name));
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseVo<Object>(true, name);
	}

	@RequestMapping(value = "getFile.do", method = RequestMethod.GET)
	public void getFile(String fileName, HttpServletResponse response) {
		File file = new File(FaxUtil.getFilePath() + "temp\\" + fileName);

		if (!file.exists())
			return;

		try {

			String type = fileName.substring(fileName.lastIndexOf('.') + 1);

			BufferedInputStream br = new BufferedInputStream(
					new FileInputStream(file));

			switch (type) {
			case "pdf":
				response.setContentType("application/pdf");
				break;
			default:
				response.setContentType("image/jpeg");
				break;
			}

			response.setHeader("Pragma", "no-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setIntHeader("Expires", -1);
			OutputStream out = response.getOutputStream();

			byte[] buf = new byte[1024];
			int len = 0;
			while ((len = br.read(buf)) > 0)
				out.write(buf, 0, len);
			br.close();
			out.flush();
			out.close();
		} catch (Exception e) {
		}
	}

	@RequestMapping(value = "getFaxCount.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseVo<Map<String, Long>> getFaxCount(HttpSession session) {
		PersonEntity person = (PersonEntity) session.getAttribute("userInfo");
		Map<String, Long> result = commonService.getFaxCount(person.getCode());
		return new ResponseVo<Map<String, Long>>(true, result);
	}

	@RequestMapping(value = "getAddressBookTreeGrid.do", method = RequestMethod.GET)
	@ResponseBody
	public TreeNode getAddressBookTreeGrid(String flg) {
		TreeNode root = commonService.getAddressBookTreeGrid(flg);
		return root;
	}

}
