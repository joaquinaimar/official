package com.wizard.official.platform.spring.hibernate.application.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.springframework.stereotype.Repository;

import com.wizard.official.platform.spring.hibernate.database.BaseDao;
import com.wizard.official.platform.spring.hibernate.entity.MenuEntity;
import com.wizard.official.platform.spring.hibernate.entity.PermissionEntity;
import com.wizard.official.platform.spring.hibernate.entity.RoleInfoEntity;
import com.wizard.official.platform.spring.hibernate.entity.UserInfoEntity;
import com.wizard.official.platform.spring.hibernate.io.PageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageRequest;

@Repository
public class SystemDao extends BaseDao {

	@SuppressWarnings("unchecked")
	public PageResponse<UserInfoEntity> searchUserInfo(UserInfoEntity userInfo,
			ExtPageRequest pageRequest) {
		Criteria criteria = super.createCriteria(UserInfoEntity.class);

		String username = userInfo.getUsername();
		if (null != username && !"".equals(username)) {
			criteria.add(Restrictions.like("username", "%" + username + "%"));
		}

		String rolename = userInfo.getRolename();
		if (null != rolename && !"".equals(rolename)) {
			criteria.add(Restrictions.eq("rolename", rolename));
		}

		criteria.addOrder(Order.asc("username"));
		return super.pageQuery(criteria, pageRequest);
	}

	@SuppressWarnings("unchecked")
	public List<RoleInfoEntity> getRoleInfoCombox() {
		Criteria criteria = super.createCriteria(RoleInfoEntity.class);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public PageResponse<RoleInfoEntity> searchRoleInfo(RoleInfoEntity roleInfo,
			ExtPageRequest pageRequest) {
		Criteria criteria = super.createCriteria(RoleInfoEntity.class);

		String rolename = roleInfo.getRolename();
		if (null != rolename && !"".equals(rolename)) {
			criteria.add(Restrictions.like("rolename", "%" + rolename + "%"));
		}

		criteria.addOrder(Order.asc("rolename"));
		return super.pageQuery(criteria, pageRequest);
	}

	@SuppressWarnings("unchecked")
	public List<MenuEntity> getMenunameMultiselect(String rolename) {

		Criteria criteria = super.createCriteria(MenuEntity.class, "menu");

		DetachedCriteria permissionCriteria = DetachedCriteria.forClass(
				PermissionEntity.class, "permission");
		permissionCriteria
				.add(Restrictions.eq("permission.rolename", rolename));
		permissionCriteria.add(Restrictions.eqProperty("permission.menuname",
				"menu.menuname"));
		criteria.add(Restrictions.not(Subqueries.exists(permissionCriteria
				.setProjection(Projections.property("permission.menuname")))));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PermissionEntity> getPermissionMultiselect(String rolename) {
		Criteria criteria = super.createCriteria(PermissionEntity.class);
		criteria.add(Restrictions.eq("rolename", rolename));
		return criteria.list();
	}

	public int removePermission(String rolename, String[] menu) {
		Query query = super
				.createQuery("DELETE FROM PermissionEntity WHERE rolename = :rolename AND menuname IN :menu");
		query.setParameter("rolename", rolename);
		query.setParameterList("menu", menu);
		return query.executeUpdate();
	}
}
