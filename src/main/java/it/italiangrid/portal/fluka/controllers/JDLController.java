package it.italiangrid.portal.fluka.controllers;

import it.italiangrid.portal.fluka.db.service.JobJdlsService;

import javax.portlet.RenderRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

@Controller("jdlController")
@RequestMapping(value = "VIEW")
public class JDLController {
	
	private static final Logger log = Logger.getLogger(JDLController.class);

	@Autowired
	private JobJdlsService jobJdlsService;
	
	@RenderMapping(params = "myaction=getJDL")
	public String showGetJDL(RenderRequest request){
		log.info("Get JDL for job: " + request.getParameter("jobId"));
		
		return "jdlViewer";

	}
	
	@ModelAttribute("jdl")
	public String getJDL(@RequestParam int jobId){
		return new String(jobJdlsService.findById(jobId).getJdl());
	}
	
	
}
