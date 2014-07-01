package cn.lonwin.fax.application.web;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lonwin.fax.application.service.FaxBoxService;
import cn.lonwin.fax.domain.entity.EastFaxStat;
import cn.lonwin.fax.domain.entity.PersonEntity;
import cn.lonwin.fax.domain.vo.FaxVo;

import com.wizard.official.platform.spring.hibernate.io.PageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageRequest;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtResponse;

@Controller
@RequestMapping("controller/faxBox/")
public class FaxBoxController {

	@Autowired
	private FaxBoxService faxBoxService = null;

	@RequestMapping(value = "searchInFax.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtPageResponse<FaxVo> searchInFax(HttpSession session, String key,
			ExtPageRequest pageRequest) {
		PersonEntity person = (PersonEntity) session.getAttribute("userInfo");
		PageResponse<FaxVo> page = faxBoxService.searchInFax(person.getCode(),
				key, pageRequest);
		return new ExtPageResponse<FaxVo>(true, page);
	}

	@RequestMapping(value = "searchOutFax.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtPageResponse<FaxVo> searchOutFax(HttpSession session, String key,
			ExtPageRequest pageRequest) {
		PersonEntity person = (PersonEntity) session.getAttribute("userInfo");
		PageResponse<FaxVo> page = faxBoxService.searchOutFax(person.getCode(),
				key, pageRequest);
		return new ExtPageResponse<FaxVo>(true, page);
	}

	@RequestMapping(value = "searchDraftFax.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtPageResponse<FaxVo> searchDraftFax(HttpSession session,
			String key, ExtPageRequest pageRequest) {
		PersonEntity person = (PersonEntity) session.getAttribute("userInfo");
		PageResponse<FaxVo> page = faxBoxService.searchDraftFax(
				person.getCode(), key, pageRequest);
		return new ExtPageResponse<FaxVo>(true, page);
	}

	@RequestMapping(value = "searchRecycleFax.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtPageResponse<FaxVo> searchRecycleFax(HttpSession session,
			String key, ExtPageRequest pageRequest) {
		PersonEntity person = (PersonEntity) session.getAttribute("userInfo");
		PageResponse<FaxVo> page = faxBoxService.searchRecycleFax(
				person.getCode(), key, pageRequest);
		return new ExtPageResponse<FaxVo>(true, page);
	}

	@RequestMapping(value = "moveToRecycleFaxBox.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> moveToRecycleFaxBox(String[] ids) {
		faxBoxService.moveToRecycleFaxBox(ids);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "restoreFax.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> restoreFax(String[] ids) {
		faxBoxService.restoreFax(ids);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "deleteFax.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> deleteFax(String[] ids) {
		faxBoxService.deleteFax(ids);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "getFaxStat.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtResponse<List<EastFaxStat>> getFaxStat(String faxId) {
		List<EastFaxStat> list = faxBoxService.getFaxStat(faxId);
		return new ExtResponse<List<EastFaxStat>>(true, list);
	}

}
