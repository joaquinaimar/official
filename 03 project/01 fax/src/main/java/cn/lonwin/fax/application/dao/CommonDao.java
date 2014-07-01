package cn.lonwin.fax.application.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BooleanType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import cn.lonwin.fax.domain.entity.AddressListDetailEntity;
import cn.lonwin.fax.domain.entity.AddressListEntity;
import cn.lonwin.fax.domain.entity.FaxEntity;
import cn.lonwin.fax.domain.entity.OrgEntity;
import cn.lonwin.fax.domain.entity.PersonEntity;
import cn.lonwin.fax.domain.vo.TreeNode;

import com.wizard.official.platform.spring.hibernate.database.BaseDao;

@Repository
public class CommonDao extends BaseDao {

	public PersonEntity getUserInfo(String username) {
		Criteria criteria = super.createCriteria(PersonEntity.class);
		criteria.add(Restrictions.eq("username", username));
		return (PersonEntity) criteria.uniqueResult();
	}

	public Long getInFaxCount(String user, Integer[] types) {
		Criteria criteria = super.createCriteria(FaxEntity.class);
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.in("type", types));
		criteria.add(Restrictions.eq("receiveUser", user));
		return (Long) criteria.uniqueResult();
	}

	public Long getOutFaxCount(String user, Integer[] types) {
		Criteria criteria = super.createCriteria(FaxEntity.class);
		criteria.setProjection(Projections.rowCount());
		criteria.add(Restrictions.in("type", types));
		criteria.add(Restrictions.eq("sendUser", user));
		return (Long) criteria.uniqueResult();
	}

	@SuppressWarnings("unchecked")
	public List<TreeNode> getOrgTree(String parentCode, String flg) {
		Criteria criteria = super.createCriteria(OrgEntity.class);
		criteria.add(Restrictions.eq("parentCode", parentCode));
		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("code").as("code"));
		pList.add(Projections.property("name").as("text"));
		pList.add(Projections.sqlProjection("'org' as type",
				new String[] { "type" }, new Type[] { StringType.INSTANCE }));
		if ("1".equals(flg))
			pList.add(Projections.sqlProjection("0 as checked",
					new String[] { "checked" },
					new Type[] { BooleanType.INSTANCE }));
		criteria.setProjection(pList);
		criteria.setResultTransformer(Transformers.aliasToBean(TreeNode.class));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public Collection<? extends TreeNode> getOrgPersonTree(String parentCode,
			String flg) {
		Criteria criteria = super.createCriteria(PersonEntity.class);
		criteria.add(Restrictions.eq("orgCode", parentCode));
		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("code").as("code"));
		pList.add(Projections.property("name").as("text"));
		pList.add(Projections.sqlProjection("'orgPerson' as type",
				new String[] { "type" }, new Type[] { StringType.INSTANCE }));
		if ("1".equals(flg))
			pList.add(Projections.sqlProjection("0 as checked",
					new String[] { "checked" },
					new Type[] { BooleanType.INSTANCE }));
		criteria.setProjection(pList);
		criteria.setResultTransformer(Transformers.aliasToBean(TreeNode.class));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<TreeNode> getAddressListTree(String flg) {
		Criteria criteria = super.createCriteria(AddressListEntity.class);
		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("code").as("code"));
		pList.add(Projections.property("name").as("text"));
		pList.add(Projections.sqlProjection("'addressList' as type",
				new String[] { "type" }, new Type[] { StringType.INSTANCE }));
		if ("1".equals(flg))
			pList.add(Projections.sqlProjection("0 as checked",
					new String[] { "checked" },
					new Type[] { BooleanType.INSTANCE }));
		criteria.setProjection(pList);

		criteria.setResultTransformer(Transformers.aliasToBean(TreeNode.class));
		return criteria.list();
	}

	@SuppressWarnings("unchecked")
	public List<TreeNode> getAddressListPersonTree(String code, String flg) {
		Criteria criteria = super.createCriteria(AddressListDetailEntity.class,
				"d");
		Criteria pCriteria = criteria.createCriteria("person", "p",
				JoinType.INNER_JOIN);
		pCriteria.add(Restrictions.eq("d.code", code));

		ProjectionList pList = Projections.projectionList();
		pList.add(Projections.property("p.code").as("code"));
		pList.add(Projections.property("p.name").as("text"));
		pList.add(Projections.sqlProjection("'addressListPerson' as type",
				new String[] { "type" }, new Type[] { StringType.INSTANCE }));
		if ("1".equals(flg))
			pList.add(Projections.sqlProjection("0 as checked",
					new String[] { "checked" },
					new Type[] { BooleanType.INSTANCE }));
		pCriteria.setProjection(pList);
		pCriteria
				.setResultTransformer(Transformers.aliasToBean(TreeNode.class));
		return pCriteria.list();
	}

}
