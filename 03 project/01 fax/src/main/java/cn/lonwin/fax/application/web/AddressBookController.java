package cn.lonwin.fax.application.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.lonwin.fax.application.service.AddressBookService;
import cn.lonwin.fax.domain.entity.AddressListEntity;
import cn.lonwin.fax.domain.entity.OrgEntity;
import cn.lonwin.fax.domain.entity.PersonEntity;
import cn.lonwin.fax.domain.entity.PersonFaxNoEntity;
import cn.lonwin.fax.domain.entity.PersonPhoneNoEntity;
import cn.lonwin.fax.domain.vo.PersonVo;

import com.wizard.official.platform.spring.hibernate.io.PageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageRequest;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtResponse;

@Controller
@RequestMapping("controller/addressBook/")
public class AddressBookController {

	@Autowired
	private AddressBookService addressBookService = null;

	// =========================================================================
	// PERSON
	// =========================================================================

	@RequestMapping(value = "searchPerson.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtPageResponse<PersonEntity> searchPerson(PersonEntity persion,
			ExtPageRequest pageRequest) {
		PageResponse<PersonEntity> page = addressBookService.searchPerson(
				persion, pageRequest);
		return new ExtPageResponse<PersonEntity>(true, page);
	}

	@RequestMapping(value = "/savePerson.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> savePerson(PersonEntity person, Integer saveFlg) {
		addressBookService.savePerson(person, saveFlg);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "/deletePerson.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> deletePerson(@RequestParam String[] ids) {
		addressBookService.deletePerson(ids);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "/importPerson.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> importPerson(HttpServletRequest request) {
		addressBookService.importPerson(request);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "/exportPerson.do", method = RequestMethod.GET)
	@ResponseBody
	public void exportPerson(HttpServletRequest request,
			HttpServletResponse response) {
		addressBookService.exportPerson(request, response);
	}

	@RequestMapping(value = "/getOrgCombox.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtResponse<List<OrgEntity>> getOrgCombox() {
		List<OrgEntity> list = addressBookService.getOrgCombox();
		return new ExtResponse<List<OrgEntity>>(true, list);
	}

	@RequestMapping(value = "/getPersonPhoneNo.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtResponse<List<PersonPhoneNoEntity>> getPersonPhoneNo(
			String personCode) {
		List<PersonPhoneNoEntity> list = addressBookService
				.getPersonPhoneNo(personCode);
		return new ExtResponse<List<PersonPhoneNoEntity>>(true, list);
	}

	@RequestMapping(value = "/savePersonPhoneNo.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> savePersonPhoneNo(
			PersonPhoneNoEntity personPhoneNo) {
		addressBookService.savePersonPhoneNo(personPhoneNo);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "/deletePersonPhoneNo.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> deletePersonPhoneNo(@RequestParam String[] ids) {
		addressBookService.deletePersonPhoneNo(ids);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "/getPersonFaxNo.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtResponse<List<PersonFaxNoEntity>> getPersonFaxNo(String personCode) {
		List<PersonFaxNoEntity> list = addressBookService
				.getPersonFaxNo(personCode);
		return new ExtResponse<List<PersonFaxNoEntity>>(true, list);
	}

	@RequestMapping(value = "/savePersonFaxNo.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> savePersonFaxNo(PersonFaxNoEntity personFaxNo) {
		addressBookService.savePersonFaxNo(personFaxNo);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "/deletePersonFaxNo.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> deletePersonFaxNo(@RequestParam String[] ids) {
		addressBookService.deletePersonFaxNo(ids);
		return new ExtResponse<Boolean>(true, true);
	}

	// =========================================================================
	// ADDRESSLIST
	// =========================================================================

	@RequestMapping(value = "/saveAddressList.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> saveAddressList(AddressListEntity addressList,
			Integer saveFlg) {
		addressBookService.saveAddressList(addressList, saveFlg);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "/deleteAddressList.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> deleteAddressList(@RequestParam String[] ids) {
		addressBookService.deleteAddressList(ids);
		return new ExtResponse<Boolean>(true, true);
	}

	// =========================================================================
	// ADDRESSLISTDETAIL
	// =========================================================================

	@RequestMapping(value = "/getUnabsorbedMultiselect.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtResponse<List<PersonVo>> getUnabsorbedMultiselect(String code) {
		List<PersonVo> list = addressBookService.getUnabsorbedMultiselect(code);
		return new ExtResponse<List<PersonVo>>(true, list);
	}

	@RequestMapping(value = "/getAbsorbedMultiselect.do", method = RequestMethod.GET)
	@ResponseBody
	public ExtResponse<List<PersonVo>> getAbsorbedMultiselect(String code) {
		List<PersonVo> list = addressBookService.getAbsorbedMultiselect(code);
		return new ExtResponse<List<PersonVo>>(true, list);
	}

	@RequestMapping(value = "/addAddressListDetail.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> addAddressListDetail(String code,
			String[] person) {
		addressBookService.addAddressListDetail(code, person);
		return new ExtResponse<Boolean>(true, true);
	}

	@RequestMapping(value = "/removeAddressListDetail.do", method = RequestMethod.POST)
	@ResponseBody
	public ExtResponse<Boolean> removeAddressListDetail(String code,
			String[] person) {
		addressBookService.removeAddressListDetail(code, person);
		return new ExtResponse<Boolean>(true, true);
	}

}
