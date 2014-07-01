package cn.lonwin.fax.application.dao;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import cn.lonwin.fax.domain.entity.AddressListDetailEntity;
import cn.lonwin.fax.domain.entity.FaxFileEntity;
import cn.lonwin.fax.domain.entity.OrgEntity;
import cn.lonwin.fax.domain.entity.PersonEntity;
import cn.lonwin.fax.domain.entity.PersonFaxNoEntity;
import cn.lonwin.fax.domain.entity.ReceiveUserEntity;

import com.wizard.official.platform.spring.hibernate.database.BaseDao;

@Repository
@SuppressWarnings("unchecked")
public class OutFaxDao extends BaseDao {

	public List<FaxFileEntity> getFaxFiles(String faxId) {
		Criteria criteria = super.createCriteria(FaxFileEntity.class);
		criteria.add(Restrictions.eq("faxId", faxId));
		return criteria.list();
	}

	public List<ReceiveUserEntity> getReceiveUser(String faxId) {
		Criteria criteria = super.createCriteria(ReceiveUserEntity.class);
		criteria.add(Restrictions.eq("faxId", faxId));
		criteria.add(Restrictions.in("type", new Integer[] { 1, 2, 3 }));
		return criteria.list();
	}

	public List<ReceiveUserEntity> getTempFaxNos(String faxId) {
		Criteria criteria = super.createCriteria(ReceiveUserEntity.class);
		criteria.add(Restrictions.eq("faxId", faxId));
		criteria.add(Restrictions.eq("type", 4));
		return criteria.list();
	}

	public List<String> getType4FaxNos(String faxId) {
		Criteria criteria = super.createCriteria(ReceiveUserEntity.class);
		criteria.add(Restrictions.eq("faxId", faxId));
		criteria.add(Restrictions.eq("type", 4));
		criteria.setProjection(Projections.distinct(Projections
				.property("receiveCode")));
		return criteria.list();
	}

	public int deleteFaxFileByFaxId(String id) {
		Query query = super
				.createQuery("DELETE FROM FaxFileEntity WHERE faxId = :id");
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public int deleteReceiveByFaxId(String id) {
		Query query = super
				.createQuery("DELETE FROM ReceiveUserEntity WHERE faxId = :id");
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public int deleteFaxById(String id) {
		Query query = super.createQuery("DELETE FROM FaxEntity WHERE id = :id");
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public List<ReceiveUserEntity> getReceiveUserList(String faxId) {
		Criteria criteria = super.createCriteria(ReceiveUserEntity.class);
		criteria.add(Restrictions.eq("faxId", faxId));
		criteria.addOrder(Order.asc("type"));
		return criteria.list();
	}

	public List<String> getSubOrgWithOrg(Collection<String> orgType) {
		Criteria criteria = super.createCriteria(OrgEntity.class);
		criteria.add(Restrictions.in("parentCode", orgType));
		criteria.setProjection(Projections.distinct(Projections
				.property("code")));
		return criteria.list();
	}

	public List<String> getPersonWithOrg(Collection<String> orgType) {
		Criteria criteria = super.createCriteria(PersonEntity.class);
		criteria.add(Restrictions.in("orgCode", orgType));
		criteria.setProjection(Projections.distinct(Projections
				.property("code")));
		return criteria.list();
	}

	public List<String> getPersonWithAddressList(
			Collection<String> addressListType) {
		Criteria criteria = super.createCriteria(AddressListDetailEntity.class);
		criteria.add(Restrictions.in("code", addressListType));
		criteria.setProjection(Projections.distinct(Projections
				.property("personCode")));
		return criteria.list();
	}

	public List<String> getFaxNos(Set<String> personList) {
		Criteria criteria = super.createCriteria(PersonFaxNoEntity.class);
		criteria.add(Restrictions.in("personCode", personList));
		criteria.setProjection(Projections.distinct(Projections.property("no")));
		return criteria.list();
	}

}
