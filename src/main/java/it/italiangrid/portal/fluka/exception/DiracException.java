package it.italiangrid.portal.fluka.exception;

public class DiracException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1003250052737503412L;
	
	private String message;
	
	public DiracException(String message){
		super();
		this.message = message;
	}
	
	public String getMessage(){
		return this.message;
	}

}
