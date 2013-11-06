package it.italiangrid.portal.fluka.db.dao.hibernate;


import java.util.List;

import it.italiangrid.portal.fluka.db.dao.generic.JobsDAO;
import it.italiangrid.portal.fluka.db.domain.Jobs;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

@Repository
public class JobsDAOHibernate extends GenericHibernateDAO<Jobs, Long> implements JobsDAO {

	public List<Jobs> findByOwner(String owner) {
		
		return findByCriteria(Restrictions.eq("owner", owner),
				Restrictions.sqlRestriction("1=1 ORDER BY jobId DESC"));
	}

	public List<Jobs> findByOwnerDN(String dn) {
		
		return findByCriteria(Restrictions.eq("ownerDn", dn),
				Restrictions.sqlRestriction("1=1 ORDER BY jobId DESC"));
	}

}
