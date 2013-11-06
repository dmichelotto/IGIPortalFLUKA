package it.italiangrid.portal.fluka.db.dao.hibernate;


import it.italiangrid.portal.fluka.db.dao.generic.JobParametersDAO;
import it.italiangrid.portal.fluka.db.domain.JobParameters;
import it.italiangrid.portal.fluka.db.domain.JobParametersId;

import org.springframework.stereotype.Repository;

@Repository
public class JobParametersDAOHibernate extends GenericHibernateDAO<JobParameters, JobParametersId> implements JobParametersDAO {

}
