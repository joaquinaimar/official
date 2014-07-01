package cn.lonwin.fax.application.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lonwin.fax.application.dao.FaxBoxDao;
import cn.lonwin.fax.domain.entity.EastFaxStat;
import cn.lonwin.fax.domain.vo.FaxVo;

import com.wizard.official.platform.spring.hibernate.io.PageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageRequest;

@Service
@Transactional
public class FaxBoxService {

	@Autowired
	private FaxBoxDao faxBoxDao = null;

	public PageResponse<FaxVo> searchInFax(String user, String key,
			ExtPageRequest pageRequest) {
		return faxBoxDao.searchInFax(user, key, pageRequest);
	}

	public PageResponse<FaxVo> searchOutFax(String user, String key,
			ExtPageRequest pageRequest) {
		return faxBoxDao.searchOutFax(user, key, pageRequest);
	}

	public PageResponse<FaxVo> searchDraftFax(String user, String key,
			ExtPageRequest pageRequest) {
		return faxBoxDao.searchDraftFax(user, key, pageRequest);
	}

	public PageResponse<FaxVo> searchRecycleFax(String user, String key,
			ExtPageRequest pageRequest) {
		return faxBoxDao.searchRecycleFax(user, key, pageRequest);
	}

	public void moveToRecycleFaxBox(String[] ids) {
		faxBoxDao.moveToRecycleFaxBox(ids);
	}

	public void restoreFax(String[] ids) {
		faxBoxDao.restoreFax(ids);
	}

	public void deleteFax(String[] ids) {
		faxBoxDao.deleteReceiveUserByFaxId(ids);
		faxBoxDao.deleteFaxFileByFaxId(ids);
		faxBoxDao.deleteFaxById(ids);
	}

	public List<EastFaxStat> getFaxStat(String faxId) {
		return faxBoxDao.getFaxStat(faxId);
	}
}
