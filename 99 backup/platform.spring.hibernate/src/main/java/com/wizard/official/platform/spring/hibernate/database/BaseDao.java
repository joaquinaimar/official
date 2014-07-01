package com.wizard.official.platform.spring.hibernate.database;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wizard.official.platform.spring.hibernate.io.PageRequest;
import com.wizard.official.platform.spring.hibernate.io.PageResponse;

@Repository
@SuppressWarnings({ "rawtypes", "unchecked" })
public class BaseDao {

	@Autowired
	private SessionFactory sessionFactory = null;

	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	public <X> X get(Class<X> clazz, Serializable id) {
		return (X) getSession().get(clazz, id);
	}

	public Serializable save(EntityTemplate entity) {
		return getSession().save(entity);
	}

	public void delete(EntityTemplate entity) {
		getSession().delete(entity);
	}

	public void update(EntityTemplate entity) {
		getSession().update(entity);
	}

	public int updateByHql(String queryString) {
		Query query = createQuery(queryString);
		return query.executeUpdate();
	}

	public int updateByHql(String queryString, Object obj) {
		Query query = createQuery(queryString);
		query.setProperties(obj);
		return query.executeUpdate();
	}

	public int updateByHql(String queryString, Map<String, Object> map) {
		Query query = createQuery(queryString);
		query.setProperties(map);
		return query.executeUpdate();
	}

	public Query createQuery(String queryString) {
		return getSession().createQuery(queryString);
	}

	public SQLQuery createSQLQuery(String queryString) {
		return getSession().createSQLQuery(queryString);
	}

	public Criteria createCriteria(String entityName) {
		return getSession().createCriteria(entityName);
	}

	public Criteria createCriteria(String entityName, String alias) {
		return getSession().createCriteria(entityName, alias);
	}

	public Criteria createCriteria(Class<?> persistentClass) {
		return getSession().createCriteria(persistentClass);
	}

	public Criteria createCriteria(Class<?> persistentClass, String alias) {
		return getSession().createCriteria(persistentClass, alias);
	}

	public PageResponse pageQuery(Criteria criteria, PageRequest request) {
		criteria.setFirstResult(request.getStart());
		criteria.setMaxResults(request.getLimit());
		return new PageResponse(criteria.list(), request, getSize(criteria));
	}

	private long getSize(Criteria count) {
		count.setFirstResult(0);
		count.setMaxResults(0);
		count.setProjection(Projections.rowCount());
		clearOrderBy(count);
		return (Long) count.uniqueResult();
	}

	private void clearOrderBy(Criteria criteria) {
		try {
			Field order = criteria.getClass().getDeclaredField("parent");
			order.setAccessible(true);
			clearOrderBy((Criteria) order.get(criteria));
		} catch (NoSuchFieldException e) {
			try {
				Field order = criteria.getClass().getDeclaredField(
						"orderEntries");
				order.setAccessible(true);
				order.set(criteria, new ArrayList());
			} catch (Exception ex) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public PageResponse pageQuery(Query query, PageRequest request) {
		return pageQuery(query, null, request);
	}

	public PageResponse pageQuery(Query query, Object obj, PageRequest request) {
		query.setProperties(obj);
		query.setFirstResult(request.getStart());
		query.setMaxResults(request.getLimit());
		return new PageResponse(query.list(), request, getSize(query, obj));
	}

	private long getSize(Query query, Object obj) {
		String queryString = query.getQueryString();
		int fromIndex = queryString.toLowerCase().indexOf("from");
		String countString = "SELECT COUNT(*) AS CNT "
				+ queryString.substring(fromIndex);
		int orderIndex = countString.toLowerCase().lastIndexOf("order");
		Query countQuery = createQuery(countString.substring(0, orderIndex));
		countQuery.setProperties(obj);
		return (Long) countQuery.uniqueResult();
	}

	public PageResponse pageQuery(Query query, Map<String, Object> bean,
			PageRequest request) {
		query.setProperties(bean);
		query.setFirstResult(request.getStart());
		query.setMaxResults(request.getLimit());
		return new PageResponse(query.list(), request, getSize(query, bean));
	}

	private long getSize(Query query, Map<String, Object> bean) {
		String queryString = query.getQueryString();
		int fromIndex = queryString.toLowerCase().indexOf("from");
		String countString = "SELECT COUNT(*) AS CNT "
				+ queryString.substring(fromIndex);
		int orderIndex = countString.toLowerCase().lastIndexOf("order");
		Query countQuery = createQuery(countString.substring(0, orderIndex));
		countQuery.setProperties(bean);
		return (Long) countQuery.uniqueResult();
	}

}
