package cn.lonwin.fax.application.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import cn.lonwin.fax.domain.entity.AddressListDetailEntity;
import cn.lonwin.fax.domain.entity.OrgEntity;
import cn.lonwin.fax.domain.entity.PersonEntity;
import cn.lonwin.fax.domain.entity.PersonFaxNoEntity;
import cn.lonwin.fax.domain.entity.PersonPhoneNoEntity;
import cn.lonwin.fax.domain.vo.PersonVo;

import com.wizard.official.platform.spring.hibernate.database.BaseDao;
import com.wizard.official.platform.spring.hibernate.io.PageResponse;
import com.wizard.official.platform.spring.hibernate.io.extjs.ExtPageRequest;

@Repository
public class AddressBookDao extends BaseDao {

	// =========================================================================
	// PERSON
	// =========================================================================

	@SuppressWarnings("unchecked")
	public PageResponse<PersonEntity> searchPerson(PersonEntity persion,
			ExtPageRequest pageRequest) {
		Criteria criteria = super.createCriteria(PersonEntity.class, "person");
		Criteria orgCriteria = criteria.createCriteria("org", "org",
				JoinType.INNER_JOIN);

		String name = persion.getName();
		if (null != name && !"".equals(name)) {
			orgCriteria.add(Restrictions.like("person.name", "%" + name + "%"));
		}

		orgCriteria.addOrder(Order.asc("person.code"));

		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("person.code").as("code"));
		pList.add(Projections.property("person.name").as("name"));
		pList.add(Projections.property("person.email").as("email"));
		pList.add(Projections.property("person.orgCode").as("orgCode"));
		pList.add(Projections.property("org.name").as("org"));
		orgCriteria.setProjection(pList);

		orgCriteria.setResultTransformer(Transformers
				.aliasToBean(PersonVo.class));
		return super.pageQuery(orgCriteria, pageRequest);
	}

	@SuppressWarnings("unchecked")
	public List<PersonEntity> getPersonList() {
		Criteria criteria = super.createCriteria(PersonEntity.class);
		return criteria.list();
	}

	public int deletePerson(String[] ids) {
		Query query = super
				.createQuery("DELETE FROM PersonEntity WHERE code IN :ids");
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<OrgEntity> getOrgCombox() {
		Criteria criteria = super.createCriteria(OrgEntity.class);
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PersonPhoneNoEntity> getPersonPhoneNo(String personCode) {
		Criteria criteria = super.createCriteria(PersonPhoneNoEntity.class);
		criteria.add(Restrictions.eq("personCode", personCode));
		return criteria.list();
	}

	public int deletePersonPhoneNo(String[] ids) {
		Query query = super
				.createQuery("DELETE FROM PersonPhoneNoEntity WHERE id IN :ids");
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<PersonFaxNoEntity> getPersonFaxNo(String personCode) {
		Criteria criteria = super.createCriteria(PersonFaxNoEntity.class);
		criteria.add(Restrictions.eq("personCode", personCode));
		return criteria.list();
	}

	public int deletePersonFaxNo(String[] ids) {
		Query query = super
				.createQuery("DELETE FROM PersonFaxNoEntity WHERE id IN :ids");
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}

	// =========================================================================
	// ADDRESSLIST
	// =========================================================================

	public int deleteAddressListDetail(String[] ids) {
		Query query = super
				.createQuery("DELETE FROM AddressListDetailEntity WHERE code IN :ids");
		query.setParameterList("ids", ids);
		return query.executeUpdate();

	}

	public int deleteAddressList(String[] ids) {
		Query query = super
				.createQuery("DELETE FROM AddressListEntity WHERE code IN :ids");
		query.setParameterList("ids", ids);
		return query.executeUpdate();
	}

	// =========================================================================
	// ADDRESSLISTDETAIL
	// =========================================================================

	@SuppressWarnings("unchecked")
	public List<PersonVo> getUnabsorbedMultiselect(String code) {

		Criteria criteria = super.createCriteria(PersonEntity.class, "p");
		Criteria orgCriteria = criteria.createCriteria("org", "org",
				JoinType.INNER_JOIN);

		DetachedCriteria addressListDetailCriteria = DetachedCriteria.forClass(
				AddressListDetailEntity.class, "addressListDetail");
		addressListDetailCriteria.add(Restrictions.eq("addressListDetail.code",
				code));
		addressListDetailCriteria.add(Restrictions.eqProperty(
				"addressListDetail.personCode", "p.code"));

		orgCriteria.add(Restrictions.not(Subqueries
				.exists(addressListDetailCriteria.setProjection(Projections
						.property("addressListDetail.personCode")))));

		orgCriteria.addOrder(Order.asc("p.code"));

		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("p.code").as("code"));
		pList.add(Projections.property("p.name").as("name"));
		pList.add(Projections.property("p.email").as("email"));
		pList.add(Projections.property("p.orgCode").as("orgCode"));
		pList.add(Projections.property("org.name").as("org"));
		orgCriteria.setProjection(pList);

		orgCriteria.setResultTransformer(Transformers
				.aliasToBean(PersonVo.class));

		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<PersonVo> getAbsorbedMultiselect(String code) {
		Criteria criteria = super.createCriteria(AddressListDetailEntity.class,
				"addressListDetail");

		Criteria personCriteria = criteria.createCriteria("person", "person",
				JoinType.INNER_JOIN);
		Criteria orgCriteria = personCriteria.createCriteria("org", "org",
				JoinType.INNER_JOIN);

		orgCriteria.add(Restrictions.eq("addressListDetail.code", code));
		orgCriteria.addOrder(Order.asc("person.code"));

		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("person.code").as("code"));
		pList.add(Projections.property("person.name").as("name"));
		pList.add(Projections.property("person.email").as("email"));
		pList.add(Projections.property("person.orgCode").as("orgCode"));
		pList.add(Projections.property("org.name").as("org"));
		orgCriteria.setProjection(pList);

		orgCriteria.setResultTransformer(Transformers
				.aliasToBean(PersonVo.class));

		return criteria.list();
	}

	public int removeAddressListDetail(String code, String[] person) {
		Query query = super
				.createQuery("DELETE FROM AddressListDetailEntity WHERE code = :code AND personCode IN :person");
		query.setParameter("code", code);
		query.setParameterList("person", person);
		return query.executeUpdate();
	}

	public int removeAllPersonEntity() {
		Query query = super.createQuery("DELETE FROM PersonEntity");
		return query.executeUpdate();
	}

	public int removeAllPersonPhoneNoEntity(String code) {
		Query query = super
				.createQuery("DELETE FROM PersonPhoneNoEntity WHERE personCode = :code");
		query.setParameter("code", code);
		return query.executeUpdate();
	}

	public int removeAllPersonFaxNoEntity(String code) {
		Query query = super
				.createQuery("DELETE FROM PersonFaxNoEntity WHERE personCode = :code");
		query.setParameter("code", code);
		return query.executeUpdate();
	}

}
