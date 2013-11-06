package it.italiangrid.portal.fluka.db.dao.hibernate;


import java.lang.reflect.ParameterizedType;
import java.util.List;

import it.italiangrid.portal.fluka.db.dao.generic.ProxiesDAO;
import it.italiangrid.portal.fluka.db.domain.Proxies;
import it.italiangrid.portal.fluka.db.domain.ProxiesId;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class ProxiesDAOHibernate extends GenericHibernateDAO<Proxies, ProxiesId> implements ProxiesDAO {
	private Class<Proxies> persistentClass;
	
	@Autowired
	@Qualifier("sessionFactoryDirac2")
	private SessionFactory sessionFactoryDirac2;

	private static final Logger log = Logger
			.getLogger(GenericHibernateDAO.class);

	@SuppressWarnings("unchecked")
	public ProxiesDAOHibernate() {
		log.debug("Constuctor of GenericHibernateDAO");
		this.persistentClass = (Class<Proxies>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		log.debug("Persisten Class is: Proxies");
	}

	protected Session getSession() {
		return sessionFactoryDirac2.getCurrentSession();
	}

	public Class<Proxies> getPersistentClass() {
		return persistentClass;
	}

	public Proxies findById(ProxiesId id, boolean lock) {
		log.debug("Getting " + persistentClass.getCanonicalName()
				+ " by Identifier");
		Proxies entity;
		if (lock) {
			entity = (Proxies) getSession().get(getPersistentClass(), id,
					LockMode.UPGRADE);
		} else {
			entity = (Proxies) getSession().get(getPersistentClass(), id);
		}
		return entity;
	}

	public List<Proxies> findAll() {
		log.info("Getting all " + persistentClass.getCanonicalName()
				+ " istances");
		return findByCriteria();
	}

	@SuppressWarnings("unchecked")
	public List<Proxies> findByExample(Proxies exampleInstance, String[] excludeProperty) {
		log.debug("Find by exzample of " + persistentClass.getCanonicalName()
				+ " istance");
		Criteria crit = getSession().createCriteria(getPersistentClass());
		Example example = Example.create(exampleInstance);
		for (String exclude : excludeProperty) {
			example.excludeProperty(exclude);
		}
		crit.add(example);
		return crit.list();
	}

	public Proxies makePersistent(Proxies entity) {
		log.debug("Saving or updating " + persistentClass.getCanonicalName()
				+ " istance");
		getSession().saveOrUpdate(entity);
		return entity;
	}

	public void makeTransient(Proxies entity) {
		log.debug("Deleting " + persistentClass.getCanonicalName() + " istance");
		getSession().delete(entity);
	}

	public void flush() {
		getSession().flush();
	}

	public void clear() {
		getSession().clear();
	}

	/**
	 * Use this inside subclasses as a convenience method.
	 */
	@SuppressWarnings("unchecked")
	protected List<Proxies> findByCriteria(Criterion... criterion) {
		Criteria crit = getSession().createCriteria(getPersistentClass());
		for (Criterion c : criterion) {
			crit.add(c);
		}
		return crit.list();
	}
}