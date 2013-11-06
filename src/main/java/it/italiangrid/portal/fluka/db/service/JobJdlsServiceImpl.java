package it.italiangrid.portal.fluka.db.service;

import it.italiangrid.portal.fluka.db.dao.generic.JobJdlsDAO;
import it.italiangrid.portal.fluka.db.domain.JobJdls;

import java.util.List;

import org.apache.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
public class JobJdlsServiceImpl implements JobJdlsService {

	private static final Logger log = Logger.getLogger(JobJdlsServiceImpl.class);

	@Autowired
	private JobJdlsDAO jobJdlsDAO;

	@Transactional
	public void save(JobJdls transientInstance) {
		log.debug("persisting JobJdls instance");
		jobJdlsDAO.makePersistent(transientInstance);
	}

	@Transactional
	public void delete(JobJdls persistentInstance) {
		log.debug("removing JobJdls instance");
		jobJdlsDAO.makeTransient(persistentInstance);
	}

	@Transactional
	public JobJdls findById(Integer id) {
		log.debug("getting JobJdls instance with id: " + id);
		return jobJdlsDAO.findById(id,false);
	}

	@Transactional
	public List<JobJdls> getAllJobJdls() {
		log.debug("getting all JobJdls instance");
		return jobJdlsDAO.findAll();
	}
}
