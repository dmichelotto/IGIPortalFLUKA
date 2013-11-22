package it.italiangrid.portal.fluka.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import it.italiangrid.portal.fluka.exception.DiracException;
import it.italiangrid.portal.fluka.util.DiracConfig;
import it.italiangrid.portal.fluka.util.DiracUtil;
import it.italiangrid.portal.fluka.util.TemplateList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

@Controller("templateActionController")
@RequestMapping(value = "VIEW")
public class TemplateActionController {
	
	private static final Logger log = Logger.getLogger(TemplateActionController.class);

	private static List<String> operations = Arrays.asList(new String[]{"delete", "share", "unshare"});
	private static List<String> operationsMessages = Arrays.asList(new String[]{"deleting-template-successufully", "shared-successufully", "unshared-successufully"});
	
	@ActionMapping(params = "myaction=deleteTemplate")
	public void deleteTemplate(ActionRequest request, ActionResponse response) throws DiracException{
		String path = request.getParameter("path");
		log.info("Deleting: " + path);
		
		long owner = Long.parseLong(path.split("@")[1]);
		
		
		try {
			User user = PortalUtil.getUser(request);

			if (user != null && user.getUserId()==owner) {
				
				String templateUsed = DiracConfig.getProperties("Fluka.properties", "fluka.template.path");
				
				if(path.equals(templateUsed))
					throw new DiracException("dirac-in-production");
					
				DiracUtil.delete(new File(path));
				
				log.info("Template deleted");
			}
			
			SessionMessages.add(request, "deleting-template-successufully");

		} catch (IOException e) {
			e.printStackTrace();
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionErrors.add(request, "deleting-tempalte-error");
		} catch (PortalException e) {
			e.printStackTrace();
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionErrors.add(request, "deleting-tempalte-error");
		} catch (SystemException e) {
			e.printStackTrace();
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionErrors.add(request, "deleting-tempalte-error");
		}
		response.setRenderParameter("myaction", "showSubmitJob");
		response.setRenderParameter("viewTemplate", "true");
		
	}
	
	@ActionMapping(params = "myaction=shareTemplate")
	public void shareTemplate(ActionRequest request, ActionResponse response) throws DiracException{
		String path = request.getParameter("path");
		log.info("Sharing: " + path);
		
		long owner = Long.parseLong(path.split("@")[1]);
		
		try {
			User user = PortalUtil.getUser(request);

			if (user != null && user.getUserId()==owner) {
				
				String templateUsed = DiracConfig.getProperties("Fluka.properties", "fluka.template.path");
				
				if(path.equals(templateUsed))
					throw new DiracException("dirac-in-production");
				
				TemplateList tl = new TemplateList(user.getUserId());
				tl.share(path);
				
				log.info("Template shared");
				
			}
			
			SessionMessages.add(request, "shared-successufully");
			
		} catch (PortalException e) {
			e.printStackTrace();
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionErrors.add(request, "deleting-tempalte-error");
		} catch (SystemException e) {
			e.printStackTrace();
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionErrors.add(request, "deleting-tempalte-error");
		}
		
		response.setRenderParameter("myaction", "showSubmitJob");
		response.setRenderParameter("viewTemplate", "true");
	}
	
	@ActionMapping(params = "myaction=unshareTemplate")
	public void unshareTemplate(ActionRequest request, ActionResponse response) throws DiracException{
		String path = request.getParameter("path");
		log.info("Unsharing: " + path);
		
		long owner = Long.parseLong(path.split("@")[1]);
		
		try {
			User user = PortalUtil.getUser(request);

			if (user != null && user.getUserId()==owner) {
				
				String templateUsed = DiracConfig.getProperties("Fluka.properties", "fluka.template.path");
				
				if(path.equals(templateUsed))
					throw new DiracException("dirac-in-production");
				
				TemplateList tl = new TemplateList(user.getUserId());
				tl.unshare(path);
				
				log.info("Template unshared");
			}
			
			SessionMessages.add(request, "unshared-successufully");

		} catch (PortalException e) {
			e.printStackTrace();
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionErrors.add(request, "deleting-tempalte-error");
		} catch (SystemException e) {
			e.printStackTrace();
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionErrors.add(request, "deleting-tempalte-error");
		}
		
		response.setRenderParameter("myaction", "showSubmitJob");
		response.setRenderParameter("viewTemplate", "true");
	}
	
	@ActionMapping(params = "myaction=operationMultipleTemplate")
	public void operationMultipleTemplate(@RequestParam String[] templateList, ActionRequest request, ActionResponse response) throws DiracException{
		
		try {
			User user = PortalUtil.getUser(request);
			
			if (user != null) {
				
				String templateUsed = DiracConfig.getProperties("Fluka.properties", "fluka.template.path");
				
				String operation = request.getParameter("operation");
				log.info("Operation: " + operation);
				long owner = 0;
				for (String path : templateList) {
					if(!path.equals(templateUsed)){
						switch (operations.indexOf(operation)) {
						case 0: /* delete */
							log.info("Deleting: " + path);
							
							owner = Long.parseLong(path.split("@")[1]);
							if(user.getUserId() == owner){
								DiracUtil.delete(new File(path));
							}
							
							break;
						case 1: /* share */
							log.info("Share job: " + path);
							
							owner = Long.parseLong(path.split("@")[1]);
							if(user.getUserId() == owner){
								TemplateList tl = new TemplateList(user.getUserId());
								tl.share(path);
							}
							
							break;
						case 2: /* unshare */
							log.info("Privatize job: " + path);
							
							owner = Long.parseLong(path.split("@")[1]);
							if(user.getUserId() == owner){
								TemplateList tl = new TemplateList(user.getUserId());
								tl.unshare(path);
							}
							
							break;
						default:
							log.info("Operation Unrecognized");
						}
					}
				}
				
				SessionMessages.add(request, operationsMessages.get(operations.indexOf(operation)));
				
			}

		} catch (IOException e) {
			e.printStackTrace();
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionErrors.add(request, "deleting-tempalte-error");
		} catch (PortalException e) {
			e.printStackTrace();
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionErrors.add(request, "deleting-tempalte-error");
		} catch (SystemException e) {
			e.printStackTrace();
			PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
			SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
			SessionErrors.add(request, "deleting-tempalte-error");
		}
		
		response.setRenderParameter("myaction", "showSubmitJob");
		response.setRenderParameter("viewTemplate", "true");
	}
	
	@ActionMapping(params = "myaction=setInProduction")
	public void setInProduction(ActionRequest request, ActionResponse response){
		String path = request.getParameter("path");
		log.info("Setting template in production: " + path);
		
		
		
		try {
			User user = PortalUtil.getUser(request);

			
				
				TemplateList tl = new TemplateList(user.getUserId());
				
				
				if(tl.isShared(path)){
					
					DiracConfig.saveProperties(null, "fluka.template.path", path);
					
					log.info("Template in Production: " + path);
					SessionMessages.add(request, "template-setted-in-production-successufully");
				}else{
					SessionErrors.add(request, "template-not-shared");
					log.info("Template not shared.");
				}
				
			
			
		} catch (Exception e) {
			e.printStackTrace();
			
			SessionErrors.add(request, "setting-in-production-error");
		}
		
		PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		
		response.setRenderParameter("myaction", "showSubmitJob");
		response.setRenderParameter("viewTemplate", "true");
	}
}
