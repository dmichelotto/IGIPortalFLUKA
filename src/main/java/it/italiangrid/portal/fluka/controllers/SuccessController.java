package it.italiangrid.portal.fluka.controllers;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

/**
 * Show the success page.
 * 
 * @author dmichelotto
 *
 */
@Controller("successController")
@RequestMapping(value = "VIEW")
public class SuccessController {
	/**
	 * Class logger.
	 */
	private static final Logger log = Logger.getLogger(SuccessController.class);
	
	/**
	 * Return the success.jsp page.
	 * 
	 * @param request - the request.
	 * @return the upload page.
	 */
	@RenderMapping(params = "myaction=showSuccessPage")
	public String showUploadCert(){
		log.info("Show Success Page");
		
		return "success";

	}
	
}
