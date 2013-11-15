package it.italiangrid.portal.fluka.controllers;

import it.italiangrid.portal.fluka.exception.DiracException;
import it.italiangrid.portal.fluka.util.DiracConfig;
import it.italiangrid.portal.fluka.util.LFCUtils;

import java.io.File;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

@Controller("PreferenceController")
@RequestMapping(value = "VIEW")
public class PreferenceController {
	
	private static final Logger log = Logger.getLogger(PreferenceController.class);
	
	@ActionMapping(params="myaction=savePreference")
	public void savePreference(ActionRequest request, ActionResponse response){
		log.info("Saving preferences...");
		
		String userHome = request.getParameter("LFCHome");
		log.info("Set User Fluka Home: " + userHome);
		
		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				log.info("User logged in.....");
				
				savePreference(user, userHome);
				
				SessionMessages.add(request, "preferences-saved-successfully");
				return;
				
			}

		} catch (DiracException e) {
			SessionErrors.add(request, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		SessionErrors.add(request, "preferences-not-saved");
		
		response.setRenderParameter("myaction", "showPreferences");
		
	}

	@ActionMapping(params="myaction=createHome")
	public void createHome(ActionRequest request, ActionResponse response){
		log.info("Creating home and saving preferences...");
		
		String userHome = request.getParameter("home");
		log.info("Create User Fluka Home: " + userHome);
		
		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				log.info("User logged in.....");
				
				LFCUtils.createHome(user, userHome);
				
				savePreference(user, userHome);
				
				SessionMessages.add(request, "preferences-saved-successfully");
				return;
				
			}

		} catch (DiracException e) {
			SessionErrors.add(request, e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		SessionErrors.add(request, "preferences-not-saved");
		
		response.setRenderParameter("myaction", "showPreferences");
		
	}

	private void savePreference(User user, String userHome) throws DiracException {
		
		File portalUserHome = new File(System.getProperty("java.io.tmpdir") + "/users/" + user.getUserId() + "/"); 
		
		if(!portalUserHome.exists())
			portalUserHome.mkdirs();
		
		File userProperties = new File(System.getProperty("java.io.tmpdir") + "/users/" + user.getUserId() + "/" + DiracConfig.getProperties("Fluka.properties", "fluka.userproperties.file")); 
		
		DiracConfig.saveProperties(userProperties, "lfc.fluka.home", userHome);
		
		log.info("Preferences saved in: " + userProperties.getAbsolutePath());
	}
	
}
