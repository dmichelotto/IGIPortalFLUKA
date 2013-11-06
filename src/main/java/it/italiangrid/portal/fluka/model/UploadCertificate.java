package it.italiangrid.portal.fluka.model;

public class UploadCertificate {

	private String certificate;
	private String password;
	/**
	 * @param certificate
	 * @param password
	 */
	public UploadCertificate(String certificate, String password) {
		super();
		this.certificate = certificate;
		this.password = password;
	}
	/**
	 * 
	 */
	public UploadCertificate() {
	}
	/**
	 * @return the certificate
	 */
	public String getCertificate() {
		return certificate;
	}
	/**
	 * @param certificate the certificate to set
	 */
	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UploadCertificate [certificate=" + certificate  + "]";
	}
	
	
	
}
