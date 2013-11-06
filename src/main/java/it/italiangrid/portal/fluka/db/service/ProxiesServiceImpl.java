package it.italiangrid.portal.fluka.db.service;

import it.italiangrid.portal.fluka.db.dao.generic.ProxiesDAO;
import it.italiangrid.portal.fluka.db.domain.Proxies;
import it.italiangrid.portal.fluka.db.domain.ProxiesId;

import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProxiesServiceImpl implements ProxiesService {

	private static final Logger log = Logger.getLogger(ProxiesServiceImpl.class);

	@Autowired
	private ProxiesDAO ProxiesDAO;

	@Transactional
	public void save(Proxies transientInstance) {
		log.debug("persisting Proxies instance");
		ProxiesDAO.makePersistent(transientInstance);
	}

	@Transactional
	public void delete(Proxies persistentInstance) {
		log.debug("removing Proxies instance");
		ProxiesDAO.makeTransient(persistentInstance);
	}

	@Transactional
	public Proxies findById(ProxiesId id) {
		log.debug("getting Proxies instance with id: " + id);
		return ProxiesDAO.findById(id,false);
	}

	@Transactional
	public List<Proxies> getAllProxies() {
		log.debug("getting all Proxies instance");
		return ProxiesDAO.findAll();
	}
}
