package it.italiangrid.portal.fluka.db.service;

import it.italiangrid.portal.fluka.db.dao.generic.JobParametersDAO;
import it.italiangrid.portal.fluka.db.domain.JobParameters;
import it.italiangrid.portal.fluka.db.domain.JobParametersId;

import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JobParametersServiceImpl implements JobParametersService {

	private static final Logger log = Logger.getLogger(JobParametersServiceImpl.class);

	@Autowired
	private JobParametersDAO jobParametersDAO;

	@Transactional
	public void save(JobParameters transientInstance) {
		log.debug("persisting JobParameters instance");
		jobParametersDAO.makePersistent(transientInstance);
	}

	@Transactional
	public void delete(JobParameters persistentInstance) {
		log.debug("removing JobParameters instance");
		jobParametersDAO.makeTransient(persistentInstance);
	}

	@Transactional
	public JobParameters findById(JobParametersId id) {
		log.debug("getting JobParameters instance with id: " + id);
		return jobParametersDAO.findById(id,false);
	}

	@Transactional
	public List<JobParameters> getAllJobParameters() {
		log.debug("getting all JobParameters instance");
		return jobParametersDAO.findAll();
	}
}
