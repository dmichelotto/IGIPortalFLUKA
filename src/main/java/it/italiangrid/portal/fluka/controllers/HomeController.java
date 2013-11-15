package it.italiangrid.portal.fluka.controllers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import it.italiangrid.portal.dbapi.domain.Certificate;
import it.italiangrid.portal.dbapi.domain.UserInfo;
import it.italiangrid.portal.dbapi.services.CertificateService;
import it.italiangrid.portal.dbapi.services.UserInfoService;
import it.italiangrid.portal.fluka.db.domain.Jobs;
import it.italiangrid.portal.fluka.db.service.JobsService;
import it.italiangrid.portal.fluka.db.service.ProxiesService;
import it.italiangrid.portal.fluka.exception.DiracException;
import it.italiangrid.portal.fluka.util.DiracConfig;
import it.italiangrid.portal.fluka.util.LFCUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

/**
 * Home controller for present the portlet first page and prepare the user
 * session for dirac job submission.
 * 
 * @author dmichelotto
 * 
 */

@Controller("diracHomeController")
@RequestMapping(value = "VIEW")
public class HomeController {
	/**
	 * Logger
	 */
	private static final Logger log = Logger.getLogger(HomeController.class);
	
	/**
	 * Jobs Service
	 */
	@Autowired
	private JobsService jobService;
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private CertificateService certificateService;
	
	@Autowired
	private ProxiesService proxiesService;
	
	/**
	 * Display the home page.
	 * 
	 * @return Return the portlet home page.
	 */
	@RenderMapping
	public String showHomePage(RenderRequest request){
		log.info("Display home page");
		
		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				log.info("User logged in.....");
				
				File userProperties = new File(System.getProperty("java.io.tmpdir") + "/users/" + user.getUserId() + "/" + DiracConfig.getProperties("Fluka.properties", "fluka.userproperties.file")); 
				
				if(userProperties.exists()){
					try{
						DiracConfig.getUserProperties(userProperties, "lfc.fluka.home");
					} catch(DiracException e){
						
						return "preferenceFluka";
					}
					return "home";
				}
				
				return "preferenceFluka";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "error";
	}
	
	/**
	 * Display the home page.
	 * 
	 * @return Return the portlet home page.
	 */
	@RenderMapping(params="myaction=showHome")
	public String showHomePage2(RenderRequest request){
		return showHomePage(request);
	}
	
	/**
	 * Display the preferences page.
	 * 
	 * @return Return the portlet home page.
	 */
	@RenderMapping(params="myaction=showPreferences")
	public String showPreference(RenderRequest request){
		return "preferenceFluka";
	}
	
	
	@ModelAttribute("jobs")
	public List<Jobs> getJobs(RenderRequest request){
		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				log.info("User logged in.....");
				UserInfo userInfo = userInfoService.findByMail(user.getEmailAddress());
				log.info(userInfo.getFirstName() + " " +userInfo.getLastName());
				
				List<Certificate> certs = new ArrayList<Certificate>();
				
				certs = certificateService.findById(userInfo.getUserId());
				
				List<Jobs> jobs = new ArrayList<Jobs>();
				
				for (Certificate certificate : certs) {
					
					jobs.addAll(jobService.findByOwnerDN(certificate.getSubject()));
					
				}
				
				return jobs;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@ModelAttribute("isAllJobsTerminate")
	public boolean getAllJobsStatus(RenderRequest request){
		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				log.info("User logged in.....");
				UserInfo userInfo = userInfoService.findByMail(user.getEmailAddress());
				log.info(userInfo.getFirstName() + " " +userInfo.getLastName());
				
				List<Certificate> certs = new ArrayList<Certificate>();
				
				certs = certificateService.findById(userInfo.getUserId());
				
				List<Jobs> jobs = new ArrayList<Jobs>();
				
				for (Certificate certificate : certs) {
					
					jobs.addAll(jobService.findByOwnerDN(certificate.getSubject()));
					
				}
				
				for (Jobs job : jobs) {
					if(!job.getStatus().equals("Done")&&!job.getStatus().equals("Failed")&&!job.getStatus().equals("Deleted"))
						return false;
				}
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return true;
	}
	
	@ModelAttribute("reloadPage")
	public String getReloadPage(){
		try {
			return DiracConfig.getProperties("Fluka.properties", "dirac.reload.page");
		} catch (DiracException e) {
			e.printStackTrace();
		}
		return "https://portal.italiangrid.it/job";
	}
	
	@ModelAttribute("template")
	public String getTemplates(RenderRequest request){
		
		String result;
		try {
			result = DiracConfig.getProperties("Fluka.properties", "fluka.template.path");
		} catch (DiracException e) {
			e.printStackTrace();
			result=null;
		}
		
		return result;	
	}
	
	@ModelAttribute("homes")
	public String getFlukaHomes(RenderRequest request){
		log.info("Getting Fluka Homes");
		
		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				log.info("User logged in.....");
				
				File userProperties = new File(System.getProperty("java.io.tmpdir") + "/users/" + user.getUserId() + "/" + DiracConfig.getProperties("Fluka.properties", "fluka.userproperties.file")); 
				
				if(userProperties.exists()){
					try{
					DiracConfig.getUserProperties(userProperties, "lfc.fluka.home");
					}catch (DiracException e) {
						log.info("Selected Fluka Home not found.");
					}
				}
				List<String> result = LFCUtils.getHomes(System.getProperty("java.io.tmpdir") + "/users/" + user.getUserId() + "/");
				return LFCUtils.listToString(result);
				
			}

		} catch (DiracException e) {
			e.printStackTrace();
			
			SessionErrors.add(request, e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			SessionErrors.add(request, "portal-problem");
			
		}
		
		
		PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		
		return null;
	}
	
	@ModelAttribute("selectedHome")
	public String getSelectedFlukaHome(RenderRequest request){
		log.info("Getting Selected Fluka Home");
		
		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				log.info("User logged in.....");
				
				File userProperties = new File(System.getProperty("java.io.tmpdir") + "/users/" + user.getUserId() + "/" + DiracConfig.getProperties("Fluka.properties", "fluka.userproperties.file")); 
				
				if(userProperties.exists()){
					try{
						return DiracConfig.getUserProperties(userProperties, "lfc.fluka.home");
					}catch (DiracException e) {
						log.info("Selected Fluka Home not found.");
					}
				}else{
					log.info("Selected Fluka Home not found.");
				}
			}

		} catch (DiracException e) {
			e.printStackTrace();
			
			SessionErrors.add(request, e.getMessage());
			
		} catch (Exception e) {
			e.printStackTrace();
			
			SessionErrors.add(request, "portal-problem");
			
		}
		
		
		PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		
		return "empty";
	}
	
}
