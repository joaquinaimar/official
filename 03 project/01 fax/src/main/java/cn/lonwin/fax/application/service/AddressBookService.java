package cn.lonwin.fax.application.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lonwin.fax.application.dao.AddressBookDao;
import cn.lonwin.fax.domain.entity.AddressListDetailEntity;
import cn.lonwin.fax.domain.entity.AddressListEntity;
import cn.lonwin.fax.domain.entity.OrgEntity;
import cn.lonwin.fax.domain.entity.PersonEntity;
import cn.lonwin.fax.domain.entity.PersonFaxNoEntity;
import cn.lonwin.fax.domain.entity.PersonPhoneNoEntity;
import cn.lonwin.fax.domain.vo.PersonVo;
import cn.lonwin.fax.utils.FaxUtil;

import com.wizard.official.platform.spring.hibernate.io.PageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageRequest;
import com.wizard.official.platform.spring.hibernate.utils.PlatformUtil;

@Service
@Transactional
public class AddressBookService {

	@Autowired
	private AddressBookDao addressBookDao = null;

	// =========================================================================
	// PERSON
	// =========================================================================

	public PageResponse<PersonEntity> searchPerson(PersonEntity persion,
			ExtPageRequest pageRequest) {
		return addressBookDao.searchPerson(persion, pageRequest);
	}

	public void savePerson(PersonEntity person, Integer saveFlg) {
		if (0 == saveFlg) {
			addressBookDao.save(person);
		} else {
			addressBookDao.update(person);
		}
	}

	public void deletePerson(String[] ids) {
		addressBookDao.deletePerson(ids);
	}

	public void importPerson(HttpServletRequest request) {
		try {

			List<FileItem> fileItems = PlatformUtil.getFileItem(request);

			InputStream inputStream = null;

			for (FileItem fileitem : fileItems) {
				if (!fileitem.isFormField()) {
					inputStream = fileitem.getInputStream();
					break;
				}
			}
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			HSSFSheet sheet = workbook.getSheetAt(0);

			List<String> template = FaxUtil.getTemplate();
			int rowIndex = 0;

			HSSFRow row = sheet.getRow(rowIndex);

			addressBookDao.removeAllPersonEntity();

			while (null != row
					&& !"".equals(row.getCell(0).getStringCellValue())) {
				PersonEntity person = new PersonEntity();
				int colIndex = 0;
				String phoneNo = null, faxNo = null;
				for (String column : template) {

					HSSFCell cell = row.getCell(colIndex);
					String value = "";
					if (null != cell) {
						switch (cell.getCellType()) {
						case HSSFCell.CELL_TYPE_STRING:
							value = cell.getStringCellValue();
							break;
						case HSSFCell.CELL_TYPE_NUMERIC:
							value = String.valueOf((long) cell
									.getNumericCellValue());
							break;
						case HSSFCell.CELL_TYPE_BOOLEAN:
							value = String.valueOf(cell.getBooleanCellValue());
							break;
						}
					}

					if ("phoneNo".equals(column)) {
						phoneNo = value;
					} else if ("faxNo".equals(column)) {
						faxNo = value;
					} else {
						Field field = PersonEntity.class
								.getDeclaredField(column);
						field.setAccessible(true);
						field.set(person, value);
					}

					colIndex++;
				}
				addressBookDao.save(person);

				addressBookDao.removeAllPersonPhoneNoEntity(person.getCode());
				addressBookDao.removeAllPersonFaxNoEntity(person.getCode());

				if (null != phoneNo) {
					for (String p : phoneNo.split(",")) {
						PersonPhoneNoEntity el = new PersonPhoneNoEntity();
						el.setPersonCode(person.getCode());
						el.setNo(p.trim());
						addressBookDao.save(el);
					}
				}

				if (null != faxNo) {
					for (String p : faxNo.split(",")) {
						PersonFaxNoEntity el = new PersonFaxNoEntity();
						el.setPersonCode(person.getCode());
						el.setNo(p.trim());
						addressBookDao.save(el);
					}
				}

				row = sheet.getRow(++rowIndex);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void exportPerson(HttpServletRequest request,
			HttpServletResponse response) {
		try {
			List<PersonEntity> personList = addressBookDao.getPersonList();
			List<String> template = FaxUtil.getTemplate();

			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet();
			HSSFCell cell = null;

			int rowIndex = 0;
			for (PersonEntity person : personList) {
				HSSFRow row = sheet.createRow(rowIndex);
				int colIndex = 0;

				String code = person.getCode();

				for (String column : template) {
					cell = row.createCell(colIndex);
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					if ("phoneNo".equals(column)) {
						cell.setCellValue(joinPhoneNo(code));
					} else if ("faxNo".equals(column)) {
						cell.setCellValue(joinFaxNo(code));
					} else {
						Field field = PersonEntity.class
								.getDeclaredField(column);
						field.setAccessible(true);
						cell.setCellValue((String) field.get(person));
					}
					colIndex++;
				}
				rowIndex++;
			}

			OutputStream out = PlatformUtil.getExcelOutputStream(request,
					response, "通讯录.xls");
			workbook.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String joinPhoneNo(String personCode) {
		List<PersonPhoneNoEntity> list = addressBookDao
				.getPersonPhoneNo(personCode);
		StringBuffer str = new StringBuffer();
		for (PersonPhoneNoEntity e : list) {
			str.append(", ").append(e.getNo());
		}
		if (0 != str.length())
			return str.substring(2).toString();
		else
			return "";
	}

	private String joinFaxNo(String personCode) {
		List<PersonFaxNoEntity> list = addressBookDao
				.getPersonFaxNo(personCode);
		StringBuffer str = new StringBuffer();
		for (PersonFaxNoEntity e : list) {
			str.append(", ").append(e.getNo());
		}
		if (0 != str.length())
			return str.substring(2).toString();
		else
			return "";
	}

	public List<OrgEntity> getOrgCombox() {
		return addressBookDao.getOrgCombox();
	}

	public List<PersonPhoneNoEntity> getPersonPhoneNo(String personCode) {
		return addressBookDao.getPersonPhoneNo(personCode);
	}

	public void savePersonPhoneNo(PersonPhoneNoEntity personPhoneNo) {
		addressBookDao.save(personPhoneNo);
	}

	public void deletePersonPhoneNo(String[] ids) {
		addressBookDao.deletePersonPhoneNo(ids);
	}

	public List<PersonFaxNoEntity> getPersonFaxNo(String personCode) {
		return addressBookDao.getPersonFaxNo(personCode);
	}

	public void savePersonFaxNo(PersonFaxNoEntity personFaxNo) {
		addressBookDao.save(personFaxNo);
	}

	public void deletePersonFaxNo(String[] ids) {
		addressBookDao.deletePersonFaxNo(ids);
	}

	// =========================================================================
	// ADDRESSLIST
	// =========================================================================

	public void saveAddressList(AddressListEntity addressList, Integer saveFlg) {
		if (0 == saveFlg) {
			addressBookDao.save(addressList);
		} else {
			addressBookDao.update(addressList);
		}
	}

	public void deleteAddressList(String[] ids) {
		addressBookDao.deleteAddressListDetail(ids);
		addressBookDao.deleteAddressList(ids);
	}

	// =========================================================================
	// ADDRESSLISTDETAIL
	// =========================================================================

	public List<PersonVo> getUnabsorbedMultiselect(String code) {
		return addressBookDao.getUnabsorbedMultiselect(code);
	}

	public List<PersonVo> getAbsorbedMultiselect(String code) {
		return addressBookDao.getAbsorbedMultiselect(code);
	}

	public void addAddressListDetail(String code, String[] person) {
		AddressListDetailEntity addressListDetail = null;
		for (String p : person) {
			addressListDetail = new AddressListDetailEntity();
			addressListDetail.setCode(code);
			addressListDetail.setPersonCode(p);
			addressBookDao.save(addressListDetail);
		}
	}

	public void removeAddressListDetail(String code, String[] person) {
		addressBookDao.removeAddressListDetail(code, person);
	}

}
