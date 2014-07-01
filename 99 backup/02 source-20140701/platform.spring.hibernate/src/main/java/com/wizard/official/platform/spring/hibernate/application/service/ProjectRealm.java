package com.wizard.official.platform.spring.hibernate.application.service;

import java.util.List;

import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wizard.official.platform.spring.hibernate.application.dao.ProjectDao;
import com.wizard.official.platform.spring.hibernate.security.PermissionInfo;
import com.wizard.official.platform.spring.hibernate.security.RoleInfo;
import com.wizard.official.platform.spring.hibernate.security.SpringHibernateRealm;
import com.wizard.official.platform.spring.hibernate.security.UserInfo;

@Service("projectRealm")
@Transactional
public class ProjectRealm extends SpringHibernateRealm {

	public ProjectRealm() {
		super();
		setAuthenticationTokenClass(UsernamePasswordToken.class);
	}

	@Autowired
	private ProjectDao projectDao = null;

	public UserInfo getUserInfo(String username, String password) {
		return projectDao.getUserInfo(username, password);
	}

	public List<RoleInfo> getRoleInfo(String username) {
		return projectDao.getRoleInfo(username);
	}

	public List<PermissionInfo> getPermissionInfo(String rolename) {
		return projectDao.getPermissionInfo(rolename);
	}

}
