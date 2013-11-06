package it.italiangrid.portal.fluka.db.service;

import it.italiangrid.portal.fluka.db.dao.generic.JobsDAO;
import it.italiangrid.portal.fluka.db.domain.Jobs;

import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class JobsServiceImpl implements JobsService {

	private static final Logger log = Logger.getLogger(JobsServiceImpl.class);

	@Autowired
	private JobsDAO jobsDAO;

	@Transactional
	public void save(Jobs transientInstance) {
		log.debug("persisting Jobs instance");
		jobsDAO.makePersistent(transientInstance);
	}

	@Transactional
	public void delete(Jobs persistentInstance) {
		log.debug("removing Jobs instance");
		jobsDAO.makeTransient(persistentInstance);
	}

	@Transactional
	public Jobs findById(Long id) {
		log.debug("getting Jobs instance with id: " + id);
		return jobsDAO.findById(id,false);
	}

	@Transactional
	public List<Jobs> getAllJobs() {
		log.info("getting all Jobs instance");
		return jobsDAO.findAll();
	}

	@Transactional
	public List<Jobs> findByOwner(String owner) {
		// TODO Auto-generated method stub
		return jobsDAO.findByOwner(owner);
	}
	
	@Transactional
	public List<Jobs> findByOwnerDN(String dn) {
		// TODO Auto-generated method stub
		return jobsDAO.findByOwnerDN(dn);
	}
}
