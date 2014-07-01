package cn.lonwin.fax.application.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lonwin.fax.domain.entity.EastFaxStat;
import cn.lonwin.fax.domain.entity.FaxEntity;
import cn.lonwin.fax.domain.vo.FaxVo;

import com.wizard.official.platform.spring.hibernate.database.BaseDao;
import com.wizard.official.platform.spring.hibernate.io.PageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageRequest;

@Repository
@SuppressWarnings("unchecked")
public class FaxBoxDao extends BaseDao {

	public PageResponse<FaxVo> searchInFax(String user, String key,
			ExtPageRequest pageRequest) {
		Criteria criteria = super.createCriteria(FaxEntity.class, "fax");

		Criteria personCriteria = criteria.createCriteria("sendPerson",
				"person", JoinType.LEFT_OUTER_JOIN);

		addKey(personCriteria, key);
		personCriteria.add(Restrictions.eq("fax.type", 1));
		personCriteria.add(Restrictions.eq("fax.receiveUser", user));
		personCriteria.addOrder(Order.desc("fax.saveTime"));

		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("fax.id").as("id"));
		pList.add(Projections.property("fax.title").as("title"));
		pList.add(Projections.property("fax.content").as("content"));
		pList.add(Projections.property("person.name").as("name"));
		pList.add(Projections.property("fax.type").as("type"));
		pList.add(Projections.property("fax.status").as("status"));
		pList.add(Projections.property("fax.saveTime").as("saveTime"));
		personCriteria.setProjection(pList);
		personCriteria.setResultTransformer(Transformers
				.aliasToBean(FaxVo.class));
		return super.pageQuery(personCriteria, pageRequest);
	}

	public PageResponse<FaxVo> searchOutFax(String user, String key,
			ExtPageRequest pageRequest) {
		Criteria criteria = super.createCriteria(FaxEntity.class, "fax");

		Criteria personCriteria = criteria.createCriteria("receivePerson",
				"person", JoinType.LEFT_OUTER_JOIN);

		addKey(personCriteria, key);
		personCriteria.add(Restrictions.eq("fax.type", 2));
		personCriteria.add(Restrictions.eq("fax.sendUser", user));
		personCriteria.addOrder(Order.desc("fax.saveTime"));

		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("fax.id").as("id"));
		pList.add(Projections.property("fax.title").as("title"));
		pList.add(Projections.property("fax.content").as("content"));
		pList.add(Projections.property("person.name").as("name"));
		pList.add(Projections.property("fax.type").as("type"));
		pList.add(Projections.property("fax.status").as("status"));
		pList.add(Projections.property("fax.saveTime").as("saveTime"));
		personCriteria.setProjection(pList);
		personCriteria.setResultTransformer(Transformers
				.aliasToBean(FaxVo.class));
		return super.pageQuery(personCriteria, pageRequest);
	}

	public PageResponse<FaxVo> searchDraftFax(String user, String key,
			ExtPageRequest pageRequest) {
		Criteria criteria = super.createCriteria(FaxEntity.class, "fax");

		Criteria personCriteria = criteria.createCriteria("receivePerson",
				"person", JoinType.LEFT_OUTER_JOIN);

		addKey(personCriteria, key);
		personCriteria.add(Restrictions.eq("fax.type", 3));
		personCriteria.add(Restrictions.eq("fax.sendUser", user));
		personCriteria.addOrder(Order.desc("fax.saveTime"));

		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("fax.id").as("id"));
		pList.add(Projections.property("fax.title").as("title"));
		pList.add(Projections.property("fax.content").as("content"));
		pList.add(Projections.property("person.name").as("name"));
		pList.add(Projections.property("fax.type").as("type"));
		pList.add(Projections.property("fax.status").as("status"));
		pList.add(Projections.property("fax.saveTime").as("saveTime"));
		personCriteria.setProjection(pList);
		personCriteria.setResultTransformer(Transformers
				.aliasToBean(FaxVo.class));
		return super.pageQuery(personCriteria, pageRequest);
	}

	public PageResponse<FaxVo> searchRecycleFax(String user, String key,
			ExtPageRequest pageRequest) {
		Criteria criteria = super.createCriteria(FaxEntity.class, "fax");
		addKey(criteria, key);

		Criterion expression1 = Restrictions.and(
				Restrictions.eq("fax.type", 6),
				Restrictions.eq("fax.receiveUser", user));

		Criterion expression2 = Restrictions.and(
				Restrictions.in("fax.type", new Integer[] { 7, 8 }),
				Restrictions.eq("fax.sendUser", user));

		criteria.add(Restrictions.or(expression1, expression2));

		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("fax.id").as("id"));
		pList.add(Projections.property("fax.title").as("title"));
		pList.add(Projections.property("fax.content").as("content"));
		pList.add(Projections.property("fax.type").as("type"));
		pList.add(Projections.property("fax.status").as("status"));
		pList.add(Projections.property("fax.saveTime").as("saveTime"));
		criteria.setProjection(pList);
		criteria.setResultTransformer(Transformers.aliasToBean(FaxVo.class));

		return super.pageQuery(criteria, pageRequest);
	}

	private void addKey(Criteria criteria, String key) {
		if (null != key && !"".equals(key))
			criteria.add(Restrictions.or(
					Restrictions.like("fax.title", "%" + key + "%"),
					Restrictions.like("fax.content", "%" + key + "%")));
	}

	public int moveToRecycleFaxBox(String[] ids) {
		String hql = "UPDATE FaxEntity SET type =";
		hql += "CASE type WHEN 1 THEN 6 WHEN 2 THEN 7 WHEN 3 THEN 8 END ";
		hql += "WHERE id IN :ids";

		Query query = super.createQuery(hql);
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}

	public int restoreFax(String[] ids) {
		String hql = "UPDATE FaxEntity SET type =";
		hql += "CASE type WHEN 6 THEN 1 WHEN 7 THEN 2 WHEN 8 THEN 3 END ";
		hql += "WHERE id IN :ids";

		Query query = super.createQuery(hql);
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}

	public int deleteReceiveUserByFaxId(String[] ids) {
		String hql = "DELETE FROM ReceiveUserEntity WHERE faxId IN :ids";
		Query query = super.createQuery(hql);
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}

	public int deleteFaxFileByFaxId(String[] ids) {
		String hql = "DELETE FROM FaxFileEntity WHERE faxId IN :ids";
		Query query = super.createQuery(hql);
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}

	public int deleteFaxById(String[] ids) {
		String hql = "DELETE FROM FaxEntity WHERE id IN :ids";
		Query query = super.createQuery(hql);
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}

	public List<EastFaxStat> getFaxStat(String faxId) {
		Criteria criteria = super.createCriteria(EastFaxStat.class);
		criteria.add(Restrictions.eq("id.ordNo", faxId));
		return criteria.list();
	}
}
