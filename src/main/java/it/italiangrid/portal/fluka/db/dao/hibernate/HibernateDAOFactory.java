package it.italiangrid.portal.fluka.db.dao.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import it.italiangrid.portal.fluka.db.dao.generic.DAOFactory;
import it.italiangrid.portal.fluka.db.dao.generic.JobJdlsDAO;
import it.italiangrid.portal.fluka.db.dao.generic.JobParametersDAO;
import it.italiangrid.portal.fluka.db.dao.generic.JobsDAO;
import it.italiangrid.portal.fluka.db.dao.generic.ProxiesDAO;

public class HibernateDAOFactory extends DAOFactory {

	@Autowired
	@Qualifier("sessionFactoryDirac")
	private SessionFactory sessionFactoryDirac;

	@SuppressWarnings("rawtypes")
	private GenericHibernateDAO instantiateDAO(Class daoClass) {
		try {
			GenericHibernateDAO dao = (GenericHibernateDAO) daoClass
					.newInstance();
			// dao.setSession(getCurrentSession());
			return dao;
		} catch (Exception ex) {
			throw new RuntimeException("Can not instantiate DAO: " + daoClass,
					ex);
		}
	}

	// You could override this if you don't want HibernateUtil for lookup
	protected Session getCurrentSession() {
		return sessionFactoryDirac.getCurrentSession();
	}

	@Override
	public JobsDAO getJobDAO() {
		// TODO Auto-generated method stub
		return (JobsDAO) instantiateDAO(JobsDAO.class);
	}

	@Override
	public JobJdlsDAO getJobJdlsDAO() {
		// TODO Auto-generated method stub
		return (JobJdlsDAO) instantiateDAO(JobJdlsDAO.class);
	}

	@Override
	public JobParametersDAO getJobParametersDAO() {
		// TODO Auto-generated method stub
		return (JobParametersDAO) instantiateDAO(JobParametersDAO.class);
	}
	
	@Override
	public ProxiesDAO getProxiesDAO() {
		// TODO Auto-generated method stub
		return (ProxiesDAO) instantiateDAO(ProxiesDAO.class);
	}

	// Inline concrete DAO implementations with no business-related data access
	// methods.
	// If we use public static nested classes, we can centralize all of them in
	// one source file.

	/*
	 * public static class CommentDAOHibernate extends
	 * GenericHibernateDAO<Comment, Long> implements CommentDAO {}
	 * 
	 * public static class ShipmentDAOHibernate extends
	 * GenericHibernateDAO<Shipment, Long> implements ShipmentDAO {}
	 */

	

}
