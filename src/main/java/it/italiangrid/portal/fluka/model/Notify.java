package it.italiangrid.portal.fluka.model;

import java.io.Serializable;
import java.util.List;

/**
 * Class that contain the job and user information for the notification.
 * 
 * @author dmichelotto
 *
 */
public class Notify implements Serializable {
	
	/**
	 * Logger
	 */
	private static final long serialVersionUID = 4405547426428536516L;

	/**
	 * User's e-mail
	 */
	private String email;
	
	/**
	 * User's name.
	 */
	private String user;
	
	/**
	 * Jobs list
	 */
	private List<Long> jobs;

	/**
	 * @param email
	 * @param jobs
	 */
	public Notify(String email, String user, List<Long> jobs) {
		this.user = user;
		this.email = email;
		this.jobs = jobs;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the jobs
	 */
	public List<Long> getJobs() {
		return jobs;
	}

	/**
	 * @param jobs the jobs to set
	 */
	public void setJobs(List<Long> jobs) {
		this.jobs = jobs;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Notify [email=" + email + ", user=" + user + ", jobs=" + jobs
				+ "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((jobs == null) ? 0 : jobs.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Notify other = (Notify) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (jobs == null) {
			if (other.jobs != null)
				return false;
		} else if (!jobs.equals(other.jobs))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}
	
	
	
}
