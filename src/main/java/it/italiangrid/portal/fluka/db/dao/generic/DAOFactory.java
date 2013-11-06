package it.italiangrid.portal.fluka.db.dao.generic;

import it.italiangrid.portal.dbapi.dao.hibernate.HibernateDAOFactory;

public abstract class DAOFactory {

	/**
     * Creates a standalone DAOFactory that returns unmanaged DAO
     * beans for use in any environment Hibernate has been configured
     * for. Uses HibernateUtil/SessionFactory and Hibernate context
     * propagation (CurrentSessionContext), thread-bound or transaction-bound,
     * and transaction scoped.
     */
    @SuppressWarnings("rawtypes")
	public static final Class HIBERNATE = HibernateDAOFactory.class;
 
    /**
     * Factory method for instantiation of concrete factories.
     */
    @SuppressWarnings("rawtypes")
	public static DAOFactory instance(Class factory) {
        try {
            return (DAOFactory)factory.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("Couldn't create DAOFactory: " + factory);
        }
    }
    
    public static DAOFactory instance() {
		return instance(HIBERNATE);
	}
 
    // Add your DAO interfaces here
    public abstract JobsDAO getJobDAO();
    public abstract JobJdlsDAO getJobJdlsDAO();
    public abstract JobParametersDAO getJobParametersDAO();
    public abstract ProxiesDAO getProxiesDAO();
	
}
