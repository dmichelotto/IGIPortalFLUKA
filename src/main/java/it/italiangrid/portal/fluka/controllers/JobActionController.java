package it.italiangrid.portal.fluka.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import it.italiangrid.portal.fluka.admin.DiracAdminUtil;
import it.italiangrid.portal.fluka.util.DiracUtil;

import javax.portlet.ActionRequest;
import javax.portlet.PortletConfig;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

@Controller("jobActionController")
@RequestMapping(value = "VIEW")
public class JobActionController {
	/**
	 * Logger
	 */
	private static final Logger log = Logger.getLogger(JobActionController.class);
	
	private static List<String> operations = Arrays.asList(new String[]{"delete", "reschedule"});
	private static List<String> operationsMessages = Arrays.asList(new String[]{"deleting-successufully", "resheduling-successufully"});
	
	@ActionMapping(params = "myaction=rescheduleJob")
	public void getRescheduleJob(ActionRequest request){
		log.info("Reschedule job: " + request.getParameter("jobId"));
		
		try {
			User user = PortalUtil.getUser(request);
			if (user != null) {
				
				int jobId = Integer.parseInt(request.getParameter("jobId"));
				
				String userPath = System.getProperty("java.io.tmpdir") + "/users/"+user.getUserId();
				
				DiracAdminUtil util = new DiracAdminUtil();
				util.getRescheduleJob(userPath, jobId);
				
				SessionMessages.add(request, "resheduling-successufully");
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SessionErrors.add(request, "rescheduling-error");
		PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);

	}
	
	@ActionMapping(params = "myaction=deleteJob")
	public void getDeleteJob(ActionRequest request){
		log.info("Delete job: " + request.getParameter("jobId"));
		
		try {
			User user = PortalUtil.getUser(request);
			if (user != null) {
				
				int jobId = Integer.parseInt(request.getParameter("jobId"));
				
				String userPath = System.getProperty("java.io.tmpdir") + "/users/"+user.getUserId();
				
				DiracAdminUtil util = new DiracAdminUtil();
				util.getDeleteJob(userPath, jobId);
				
				SessionMessages.add(request, "deleting-successufully");
				return;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SessionErrors.add(request, "deleting-error");
		PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);

	}
	
	@ActionMapping(params = "myaction=operationOnMultipleJob")
	public void operationOnMultipleJob(@RequestParam int[] jobs, ActionRequest request){
		
		try {
			User user = PortalUtil.getUser(request);
			if (user != null) {
				DiracAdminUtil util = new DiracAdminUtil();
				String userPath = System.getProperty("java.io.tmpdir") + "/users/"+user.getUserId();
				
				String operation = request.getParameter("operation");
				log.info("Operation: " + operation);
				
				for(int jobId : jobs){
					switch (operations.indexOf(operation)) {
					case 0: /* delete */
						log.info("Delete job: " + jobId);
						
						util.getDeleteJob(userPath, jobId);
						break;
					case 1: /* reschedule */
						log.info("Reschedule job: " + jobId);
						
						util.getRescheduleJob(userPath, jobId);
						
						break;
					default:
						log.info("Operation Unrecognized");
					}
					
				}
				
				SessionMessages.add(request, operationsMessages.get(operations.indexOf(operation)));
				return;
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SessionErrors.add(request, "deleting-error");
		PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);

	}
	
	@ActionMapping(params = "myaction=goHome")
	public void goHome(ActionRequest request){
		
		log.info(request.getParameter("settedPath"));
		
		if(!request.getParameter("settedPath").isEmpty()){
			File dir = new File(request.getParameter("settedPath"));
			
			if(dir.exists())
				try {
					DiracUtil.delete(dir);
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return;

	}
	
}
