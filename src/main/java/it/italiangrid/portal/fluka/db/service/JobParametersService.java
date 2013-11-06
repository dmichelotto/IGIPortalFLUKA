package it.italiangrid.portal.fluka.db.service;

import java.util.List;

import it.italiangrid.portal.fluka.db.domain.JobParameters;
import it.italiangrid.portal.fluka.db.domain.JobParametersId;

public interface JobParametersService {

	public void save(JobParameters transientInstance);

	public void delete(JobParameters persistentInstance);

	public JobParameters findById(JobParametersId id);

	public List<JobParameters> getAllJobParameters();

}
