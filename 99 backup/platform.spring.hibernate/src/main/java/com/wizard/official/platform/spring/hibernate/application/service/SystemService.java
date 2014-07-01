package com.wizard.official.platform.spring.hibernate.application.service;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wizard.official.platform.spring.hibernate.application.dao.SystemDao;
import com.wizard.official.platform.spring.hibernate.entity.MenuEntity;
import com.wizard.official.platform.spring.hibernate.entity.PermissionEntity;
import com.wizard.official.platform.spring.hibernate.entity.RoleInfoEntity;
import com.wizard.official.platform.spring.hibernate.entity.UserInfoEntity;
import com.wizard.official.platform.spring.hibernate.io.PageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageRequest;

@Service
@Transactional
public class SystemService {

	@Autowired
	private SystemDao systemDao = null;

	public UserInfoEntity login(String user, String pwd) {
		UserInfoEntity loginInfo = new UserInfoEntity();

		Subject currentUser = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(user, pwd);
		token.setRememberMe(true);

		try {
			currentUser.login(token);
		} catch (UnknownAccountException uae) {
			return null;
		} catch (IncorrectCredentialsException ice) {
			return null;
		} catch (LockedAccountException lae) {
			return null;
		} catch (AuthenticationException ae) {
			return null;
		}

		return loginInfo;
	}

	public PageResponse<UserInfoEntity> searchUserInfo(UserInfoEntity userInfo,
			ExtPageRequest pageRequest) {
		return systemDao.searchUserInfo(userInfo, pageRequest);
	}

	public List<RoleInfoEntity> getRoleInfoCombox() {
		return systemDao.getRoleInfoCombox();
	}

	public void saveUserInfo(UserInfoEntity userInfo) {
		String id = userInfo.getId();
		if (null == id || "".equals(id)) {
			systemDao.save(userInfo);
		} else {
			UserInfoEntity user = systemDao.get(UserInfoEntity.class, id);
			user.setRolename(userInfo.getRolename());
			systemDao.update(user);
		}
	}

	public PageResponse<RoleInfoEntity> searchRoleInfo(RoleInfoEntity roleInfo,
			ExtPageRequest pageRequest) {
		return systemDao.searchRoleInfo(roleInfo, pageRequest);
	}

	public List<MenuEntity> getMenunameMultiselect(String rolename) {
		return systemDao.getMenunameMultiselect(rolename);
	}

	public List<PermissionEntity> getPermissionMultiselect(String rolename) {
		return systemDao.getPermissionMultiselect(rolename);
	}

	public void addPermission(String rolename, String[] menu) {
		PermissionEntity permission = null;
		for (String m : menu) {
			permission = new PermissionEntity();
			permission.setMenuname(m);
			permission.setRolename(rolename);
			systemDao.save(permission);
		}
	}

	public void removePermission(String rolename, String[] menu) {
		systemDao.removePermission(rolename, menu);
	}

}
