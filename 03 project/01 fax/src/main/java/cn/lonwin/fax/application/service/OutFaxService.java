package cn.lonwin.fax.application.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lonwin.fax.application.dao.OutFaxDao;
import cn.lonwin.fax.domain.entity.FaxEntity;
import cn.lonwin.fax.domain.entity.FaxFileEntity;
import cn.lonwin.fax.domain.entity.ReceiveUserEntity;
import cn.lonwin.fax.domain.fax.EastFaxConfig;
import cn.lonwin.fax.domain.fax.EastFaxReceiver;
import cn.lonwin.fax.utils.EastFaxUtil;
import cn.lonwin.fax.utils.FaxUtil;

@Service
@Transactional
public class OutFaxService {

	@Autowired
	private OutFaxDao outFaxDao = null;

	public FaxEntity getFax(String id) {
		return outFaxDao.get(FaxEntity.class, id);
	}

	public List<FaxFileEntity> getFaxFiles(String faxId) {
		return outFaxDao.getFaxFiles(faxId);
	}

	public List<ReceiveUserEntity> getReceiveUser(String faxId) {
		return outFaxDao.getReceiveUser(faxId);
	}

	public String getFaxNos(String faxId) {
		List<ReceiveUserEntity> list = outFaxDao.getTempFaxNos(faxId);
		StringBuffer faxNos = new StringBuffer();
		for (ReceiveUserEntity el : list) {
			faxNos.append(",").append(el.getReceiveCode());
		}
		if (0 < faxNos.length())
			return faxNos.substring(1).toString();
		else
			return faxNos.toString();
	}

	public void saveFax(FaxEntity fax, String[] faxNames,
			String[] receiveCodes, Integer[] receiveTypes, String[] faxNos) {

		FaxEntity dbFax = outFaxDao.get(FaxEntity.class, fax.getId());

		if (null != dbFax && 3 == dbFax.getType()) {
			outFaxDao.deleteFaxFileByFaxId(fax.getId());
			outFaxDao.deleteReceiveByFaxId(fax.getId());
			outFaxDao.deleteFaxById(fax.getId());
		}

		outFaxDao.save(fax);

		FaxFileEntity file = null;
		if (null != faxNames)
			for (String faxName : faxNames) {
				file = new FaxFileEntity();
				file.setFaxId(fax.getId());
				file.setFaxFilePath(faxName);
				outFaxDao.save(file);
			}

		ReceiveUserEntity user = null;
		if (null != receiveCodes && null != receiveTypes)
			for (int i = 0; i < receiveCodes.length && i < receiveTypes.length; i++) {
				user = new ReceiveUserEntity();
				user.setFaxId(fax.getId());
				user.setReceiveCode(receiveCodes[i]);
				user.setType(receiveTypes[i]);
				outFaxDao.save(user);
			}

		if (null != faxNos)
			for (String faxNo : faxNos) {
				user = new ReceiveUserEntity();
				user.setFaxId(fax.getId());
				user.setReceiveCode(faxNo);
				user.setType(4);
				outFaxDao.save(user);
			}

		if (2 == fax.getType()) {
			sendFax(fax, faxNames);
		}

	}

	private void sendFax(FaxEntity fax, String[] faxNames) {
		Set<String> personList = getPersonList(fax);
		List<String> faxNos = new ArrayList<String>();

		if (null != personList && 0 != personList.size())
			faxNos.addAll(outFaxDao.getFaxNos(personList));

		faxNos.addAll(outFaxDao.getType4FaxNos(fax.getId()));

		EastFaxConfig config = new EastFaxConfig();

		config.setSubject(fax.getTitle());
		config.setComments(fax.getContent());
		config.setSerialNo(fax.getId());
		config.setPriority(1);

		List<EastFaxReceiver> receiverList = new ArrayList<EastFaxReceiver>();

		EastFaxReceiver receiver = null;
		for (String faxNo : faxNos) {
			receiver = new EastFaxReceiver();
			receiver.setNumber(faxNo);
			receiverList.add(receiver);
		}

		config.setReceiverList(receiverList);

		config.setEastFax(true);

		List<FaxFileEntity> faxFiles = outFaxDao.getFaxFiles(fax.getId());

		List<File> sendFiles = new ArrayList<File>(faxFiles.size());

		String faxPath = FaxUtil.getFilePath();

		for (FaxFileEntity faxFile : faxFiles) {
			File sendFile = new File(faxPath + "temp\\"
					+ faxFile.getFaxFilePath());
			if (sendFile.exists())
				sendFiles.add(sendFile);
		}
		config.setSendFiles(sendFiles);

		EastFaxUtil.creatSendConfigFile(config);

	}

	private Set<String> getPersonList(FaxEntity fax) {
		Set<String> personList = new HashSet<String>();
		List<ReceiveUserEntity> list = outFaxDao
				.getReceiveUserList(fax.getId());

		Set<String> personType = new HashSet<String>();
		Set<String> orgType = new HashSet<String>();
		Set<String> addressListType = new HashSet<String>();

		for (ReceiveUserEntity user : list) {
			switch (user.getType()) {
			case 1:
				personType.add(user.getReceiveCode());
				break;
			case 2:
				orgType.add(user.getReceiveCode());
				break;
			case 3:
				addressListType.add(user.getReceiveCode());
				break;
			}
		}

		if (0 != personType.size())
			personList.addAll(personType);
		if (0 != orgType.size())
			personList.addAll(getPersonWithOrg(orgType));
		if (0 != addressListType.size())
			personList.addAll(getPersonWithAddressList(addressListType));

		return personList;
	}

	private Set<String> getPersonWithOrg(Collection<String> orgType) {
		Set<String> result = new HashSet<String>();

		List<String> orgList = outFaxDao.getSubOrgWithOrg(orgType);
		if (0 != orgList.size()) {
			result.addAll(getPersonWithOrg(orgList));
		}

		List<String> list = outFaxDao.getPersonWithOrg(orgType);
		result.addAll(list);
		return result;
	}

	private Set<String> getPersonWithAddressList(
			Collection<String> addressListType) {
		Set<String> result = new HashSet<String>();
		List<String> list = outFaxDao.getPersonWithAddressList(addressListType);
		result.addAll(list);
		return result;
	}

}
