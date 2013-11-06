package it.italiangrid.portal.fluka.db.dao.hibernate;


import it.italiangrid.portal.fluka.db.dao.generic.JobJdlsDAO;
import it.italiangrid.portal.fluka.db.domain.JobJdls;

import org.springframework.stereotype.Repository;

@Repository
public class JobJdlsDAOHibernate extends GenericHibernateDAO<JobJdls, Integer> implements JobJdlsDAO {

}
