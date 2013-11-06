package it.italiangrid.portal.fluka.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.portlet.RenderRequest;

import it.italiangrid.portal.dbapi.domain.UserInfo;
import it.italiangrid.portal.dbapi.domain.Vo;
import it.italiangrid.portal.dbapi.services.UserInfoService;
import it.italiangrid.portal.dbapi.services.UserToVoService;
import it.italiangrid.portal.fluka.admin.DiracAdminUtil;
import it.italiangrid.portal.fluka.db.service.JobJdlsService;
import it.italiangrid.portal.fluka.exception.DiracException;
import it.italiangrid.portal.fluka.model.Jdl;
import it.italiangrid.portal.fluka.model.Template;
import it.italiangrid.portal.fluka.util.DiracConfig;
import it.italiangrid.portal.fluka.util.DiracUtil;
import it.italiangrid.portal.fluka.util.TemplateList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

@Controller("diracSubmitJobController")
@RequestMapping(value = "VIEW")
public class SubmitJobController {
	/**
	 * Logger
	 */
	private static final Logger log = Logger.getLogger(SubmitJobController.class);
	
	@Autowired
	private UserInfoService userInfoService;
	
	@Autowired
	private UserToVoService userToVoService;
	
	@Autowired
	private JobJdlsService jobJdlsService;
	
	/**
	 * Display the home page.
	 * 
	 * @return Return the portlet home page.
	 */
	@RenderMapping(params="myaction=showSubmitJob")
	public String showHomePage(){
		return "submit";
	}
	
	@ModelAttribute("jdl")
	public Jdl newJob(RenderRequest request){
		Jdl jdl = new Jdl();
		try {
			
			User user = PortalUtil.getUser(request);
			if(request.getParameter("jobId")!=null){
				
				int jobId = Integer.parseInt(request.getParameter("jobId"));
				log.info("jobId: " + jobId);
				
				jdl = DiracUtil.parseJdl(jobJdlsService.findById(jobId), user.getUserId());
				
				log.info("Duplicated jdl:");
				log.info(jdl);
				
			}
			String path = request.getParameter("path");
			if(path!=null){
				log.info("Use Template: "+ path);
				jdl = DiracUtil.getTemplate(user.getUserId(), path);
				log.info("Template jdl:");
				log.info(jdl);
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return jdl;
		
	}
	
	@ModelAttribute("sites")
	public List<String> showSites(){
		DiracAdminUtil util = new DiracAdminUtil();
		try {
			return util.getSite();
		} catch (DiracException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@ModelAttribute("showUploadCert")
	public boolean showUploadCert(RenderRequest request){
		
		if(request.getParameter("showUploadCert")!=null)
			if(request.getParameter("showUploadCert").equals("true"))
				return true;
		return false;
	}

	@ModelAttribute("vos")
	public List<Vo> getUserVos(RenderRequest request){
		try {
			User user = PortalUtil.getUser(request);
			if(user!=null){
				log.info(user.getEmailAddress()); 
				UserInfo userInfo = userInfoService.findByMail(user.getEmailAddress());
				
				List<Vo> vos = userToVoService.findVoByUserId(userInfo.getUserId());
				
				String[] excluded = DiracConfig.getProperties("Dirac.properties", "dirac.exclude.vos").split(";");
				
				List<Vo> result = new ArrayList<Vo>();
				for (Vo vo : vos) {
					if(notIn(vo.getVo(), excluded))
						result.add(vo);
				}
				return result;
			}
		}catch (DiracException e){
			log.error(e.getMessage());
			User user;
			try {
				user = PortalUtil.getUser(request);
				if(user!=null){
					log.info(user.getEmailAddress()); 
					UserInfo userInfo = userInfoService.findByMail(user.getEmailAddress());
					
					List<Vo> vos = userToVoService.findVoByUserId(userInfo.getUserId());
					
					return vos;
				}
			} catch (PortalException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (SystemException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Vo>();
	}
	
	private boolean notIn(String vo, String[] excluded) {
		
		for (String excludedVo : excluded) {
			if(vo.equals(excludedVo)){
				return false;
			}
		}
		
		return true;
	}

	@ModelAttribute("defaultVo")
	public String getUserDefaultVo(RenderRequest request){
		try {
			
			User user = PortalUtil.getUser(request);
			
			if(user!=null){
			
				UserInfo userInfo = userInfoService.findByMail(user.getEmailAddress());
				
				return userToVoService.findDefaultVo(userInfo.getUserId());
			}
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@ModelAttribute("templateList")
	public List<Template> getTemplates(RenderRequest request){
		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
				TemplateList tl = new TemplateList(user.getUserId());
				tl.load();
				return tl.getTemplates();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return new TemplateList().getTemplates();	
	}
	
	@ModelAttribute("viewTemplate")
	public boolean viewTempalte(RenderRequest request){
		
		String value = request.getParameter("viewTemplate");
		if(value!= null && Boolean.parseBoolean(value))
			return true;
		
		return false;
		
	}
	
}
