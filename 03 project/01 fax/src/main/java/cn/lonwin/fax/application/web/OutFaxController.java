package cn.lonwin.fax.application.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lonwin.fax.application.service.OutFaxService;
import cn.lonwin.fax.domain.entity.FaxEntity;
import cn.lonwin.fax.domain.entity.FaxFileEntity;
import cn.lonwin.fax.domain.entity.PersonEntity;
import cn.lonwin.fax.domain.entity.ReceiveUserEntity;

import com.wizard.official.platform.spring.hibernate.io.extjs.ExtResponse;
import com.wizard.official.platform.spring.hibernate.io.vo.ResponseVo;

@Controller
@RequestMapping("controller/outFax/")
public class OutFaxController {

	@Autowired
	private OutFaxService outFaxService = null;

	@RequestMapping(value = "getFax.do", method = RequestMethod.POST)
	@ResponseBody
	public ResponseVo<FaxEntity> getFax(String id) {
		FaxEntity result = outFaxService.getFax(id);
		return new ResponseVo<FaxEntity>(true, result);
	}

	@RequestMapping(value = "getReceiveUser.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<List<ReceiveUserEntity>> getReceiveUser(String faxId) {
		List<ReceiveUserEntity> list = outFaxService.getReceiveUser(faxId);
		return new ExtResponse<List<ReceiveUserEntity>>(true, list);
	}

	@RequestMapping(value = "getFaxNos.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Object> getFaxNos(String faxId) {
		String faxNos = outFaxService.getFaxNos(faxId);
		return new ExtResponse<Object>(true, faxNos);
	}

	@RequestMapping(value = "getFaxFiles.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<List<FaxFileEntity>> getFaxFiles(String faxId) {
		List<FaxFileEntity> list = outFaxService.getFaxFiles(faxId);
		return new ExtResponse<List<FaxFileEntity>>(true, list);
	}

	@RequestMapping(value = "saveFax.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> saveFax(HttpSession session, FaxEntity fax,
			String[] faxNames, String[] receiveCodes, Integer[] receiveTypes,
			String[] faxNos) {
		PersonEntity person = (PersonEntity) session.getAttribute("userInfo");
		fax.setSaveTime(new Date());
		fax.setSendUser(person.getCode());
		outFaxService
				.saveFax(fax, faxNames, receiveCodes, receiveTypes, faxNos);
		return new ExtResponse<Boolean>(true, true);
	}

}
