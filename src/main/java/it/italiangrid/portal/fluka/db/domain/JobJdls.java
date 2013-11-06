package it.italiangrid.portal.fluka.db.domain;

// Generated 26-mar-2013 11.36.50 by Hibernate Tools 3.4.0.CR1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * JobJdls generated by hbm2java
 */
@Entity
@Table(name = "JobJDLs", catalog = "JobDB")
public class JobJdls implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1686559239176911138L;
	private int jobId;
	private byte[] jdl;
	private byte[] jobRequirements;
	private byte[] originalJdl;

	public JobJdls() {
	}

	public JobJdls(byte[] jdl, byte[] jobRequirements, byte[] originalJdl) {
		this.jdl = jdl;
		this.jobRequirements = jobRequirements;
		this.originalJdl = originalJdl;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "JobID", unique = true, nullable = false)
	public int getJobId() {
		return this.jobId;
	}

	public void setJobId(int jobId) {
		this.jobId = jobId;
	}

	@Column(name = "JDL", nullable = false)
	public byte[] getJdl() {
		return this.jdl;
	}

	public void setJdl(byte[] jdl) {
		this.jdl = jdl;
	}

	@Column(name = "JobRequirements", nullable = false)
	public byte[] getJobRequirements() {
		return this.jobRequirements;
	}

	public void setJobRequirements(byte[] jobRequirements) {
		this.jobRequirements = jobRequirements;
	}

	@Column(name = "OriginalJDL", nullable = false)
	public byte[] getOriginalJdl() {
		return this.originalJdl;
	}

	public void setOriginalJdl(byte[] originalJdl) {
		this.originalJdl = originalJdl;
	}

}
