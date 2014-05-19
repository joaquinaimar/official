package com.wizard.official.platform.spring.hibernate.application.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wizard.official.platform.spring.hibernate.database.BaseDao;
import com.wizard.official.platform.spring.hibernate.entity.PermissionEntity;
import com.wizard.official.platform.spring.hibernate.entity.UserInfoEntity;
import com.wizard.official.platform.spring.hibernate.security.PermissionInfo;
import com.wizard.official.platform.spring.hibernate.security.RoleInfo;
import com.wizard.official.platform.spring.hibernate.security.UserInfo;

@Repository("projectDao")
@Transactional
public class ProjectDao extends BaseDao {

	public UserInfo getUserInfo(String username, String password) {

		Criteria criteria = super.createCriteria(UserInfoEntity.class);
		criteria.add(Restrictions.eq("username", username));
		criteria.add(Restrictions.eq("password", password));
		UserInfoEntity entity = (UserInfoEntity) criteria.uniqueResult();

		if (null == entity)
			return null;
		UserInfo info = new UserInfo();
		info.setUsername(entity.getUsername());
		info.setPassword(entity.getPassword());
		return info;
	}

	public List<RoleInfo> getRoleInfo(String username) {
		Criteria criteria = super.createCriteria(UserInfoEntity.class);
		criteria.add(Restrictions.eq("username", username));
		UserInfoEntity entity = (UserInfoEntity) criteria.uniqueResult();
		List<RoleInfo> roleList = new ArrayList<RoleInfo>();
		RoleInfo role = new RoleInfo();
		role.setRolename(entity.getRolename());
		roleList.add(role);
		return roleList;
	}

	@SuppressWarnings("unchecked")
	public List<PermissionInfo> getPermissionInfo(String rolename) {
		Criteria criteria = super.createCriteria(PermissionEntity.class);
		criteria.setProjection(Projections.property("menuname").as(
				"permissionname"));
		criteria.setResultTransformer(Transformers
				.aliasToBean(PermissionInfo.class));
		criteria.add(Restrictions.eq("rolename", rolename));
		return criteria.list();
	}
}
