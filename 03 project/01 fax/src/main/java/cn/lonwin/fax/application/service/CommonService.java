package cn.lonwin.fax.application.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.lonwin.fax.application.dao.CommonDao;
import cn.lonwin.fax.domain.entity.PersonEntity;
import cn.lonwin.fax.domain.entity.ReceiveUserEntity;
import cn.lonwin.fax.domain.vo.RegisterVo;
import cn.lonwin.fax.domain.vo.TreeNode;

import com.wizard.official.platform.spring.hibernate.entity.UserInfoEntity;
import com.wizard.official.platform.spring.hibernate.io.vo.ResponseVo;

@Service
@Transactional
public class CommonService {

	@Autowired
	private CommonDao commonDao = null;

	public ResponseVo<Object> register(RegisterVo register) {

		if (null == register.getUsername()
				|| "".equals(register.getUsername().trim()))
			return new ResponseVo<Object>(false, "请输入用户名！");
		if (null == register.getPassword()
				|| "".equals(register.getPassword().trim()))
			return new ResponseVo<Object>(false, "请输入密码！");
		if (null == register.getRePassword()
				|| "".equals(register.getRePassword().trim()))
			return new ResponseVo<Object>(false, "请输入密码！");
		if (null == register.getCode() || "".equals(register.getCode().trim()))
			return new ResponseVo<Object>(false, "请输入编码！");
		if (null == register.getName() || "".equals(register.getName().trim()))
			return new ResponseVo<Object>(false, "请输入姓名！");
		if (!register.getPassword().trim()
				.equals(register.getRePassword().trim()))
			return new ResponseVo<Object>(false, "两次密码输入不一致！");

		PersonEntity person = commonDao.get(PersonEntity.class,
				register.getCode());

		if (null == person
				|| !register.getName().trim().equals(person.getName()))
			return new ResponseVo<Object>(false, "人员不存在！");

		if (!(null == person.getUsername() || "".equals(person.getUsername()
				.trim())))
			return new ResponseVo<Object>(false, "人员已注册！");
		person.setUsername(register.getUsername());
		commonDao.update(person);

		UserInfoEntity userInfo = new UserInfoEntity();
		userInfo.setUsername(register.getUsername());
		userInfo.setPassword(register.getPassword());
		commonDao.save(userInfo);

		return new ResponseVo<Object>(true, "");
	}

	public PersonEntity getUserInfo(String username) {
		return commonDao.getUserInfo(username);
	}

	public Map<String, Long> getFaxCount(String user) {
		Map<String, Long> result = new HashMap<String, Long>();
		result.put("inCnt", commonDao.getInFaxCount(user, new Integer[] { 1 }));
		result.put("outCnt",
				commonDao.getOutFaxCount(user, new Integer[] { 2 }));
		result.put("draftCnt",
				commonDao.getOutFaxCount(user, new Integer[] { 3 }));
		result.put(
				"recycleCnt",
				commonDao.getInFaxCount(user, new Integer[] { 6 })
						+ commonDao
								.getOutFaxCount(user, new Integer[] { 7, 8 }));
		return result;
	}

	public Set<String> getReceivePersonCode(List<ReceiveUserEntity> receiveUsers) {
		return null;
	}

	public TreeNode getAddressBookTreeGrid(String flg) {
		TreeNode root = new TreeNode();
		root.setCode("root");
		root.setText("root");

		TreeNode org = new TreeNode();
		org.setCode("org");
		org.setText("组织机构");
		org.setType("orgRoot");
		if ("1".equals(flg))
			org.setChecked(false);
		org.setChildren(getOrgTree("0", flg));
		root.addChildren(org);

		TreeNode temp = new TreeNode();
		temp.setCode("addressList");
		temp.setText("临时组");
		temp.setType("addressListRoot");
		if ("1".equals(flg))
			temp.setChecked(false);
		temp.setChildren(getAddressListTree(flg));
		root.addChildren(temp);

		return root;
	}

	private List<TreeNode> getOrgTree(String parentCode, String flg) {
		List<TreeNode> tree = commonDao.getOrgTree(parentCode, flg);

		for (TreeNode t : tree) {
			t.setChildren(getOrgTree(t.getCode(), flg));
		}
		tree.addAll(commonDao.getOrgPersonTree(parentCode, flg));
		return tree;
	}

	private List<TreeNode> getAddressListTree(String flg) {
		List<TreeNode> tree = commonDao.getAddressListTree(flg);
		for (TreeNode t : tree) {
			t.setChildren(commonDao.getAddressListPersonTree(t.getCode(), flg));
		}
		return tree;
	}

}
