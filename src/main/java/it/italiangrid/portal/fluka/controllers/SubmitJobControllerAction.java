package it.italiangrid.portal.fluka.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;

import it.italiangrid.portal.fluka.admin.DiracAdminUtil;
import it.italiangrid.portal.fluka.model.Jdl;
import it.italiangrid.portal.fluka.model.Notify;
import it.italiangrid.portal.fluka.server.Checker;
import it.italiangrid.portal.fluka.util.DiracConfig;
import it.italiangrid.portal.fluka.util.DiracUtil;
import it.italiangrid.portal.fluka.util.GuseNotify;
import it.italiangrid.portal.fluka.util.GuseNotifyUtil;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

@Controller("diracSubmitJobControllerAction")
@RequestMapping(value = "VIEW")
public class SubmitJobControllerAction {
	/**
	 * Logger
	 */
	private static final Logger log = Logger.getLogger(SubmitJobControllerAction.class);
	
	@ActionMapping(params="myaction=submitJob")
	public void submitJob(@ModelAttribute Jdl jdl, ActionRequest request, ActionResponse response){
		log.info("Submitting job");
		
		
		try {
			User user = PortalUtil.getUser(request);
			
			UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(request);

			if (user != null) {
				
				/*
				 * Prepare temp folder for submission
				 */
				
				
				
				String path;
				String diracWrapper = DiracConfig.getProperties("Dirac.properties", "dirac.wrapper.script");
				String diracHome = DiracConfig.getProperties("Dirac.properties", "dirac.admin.homedir");
				String templateHome = DiracConfig.getProperties("Dirac.properties", "dirac.template.home");
				
				log.info(uploadRequest.getParameter("settedPath")!=null?uploadRequest.getParameter("settedPath"):"is null");
				
				if(uploadRequest.getParameter("settedPath")!=null && !uploadRequest.getParameter("settedPath").isEmpty()){
				
					path = uploadRequest.getParameter("settedPath");
					log.info("existing temp folder:" + path);
				
				}else{
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
					Calendar cal = new GregorianCalendar();
					Date now = cal.getTime();
					
					
					String tmpDir = "JDL_"+sdf.format(now);
					String userPath = System.getProperty("java.io.tmpdir") + "/users/"+user.getUserId();
					path = userPath + "/DIRAC/jdls/"+tmpDir;
					log.info("create temp folder");
					
				}
				
				
				
				File jdlFolder = new File(path);
				
				if(!jdlFolder.exists())
					jdlFolder.mkdirs();
				
				jdl.setPath(path);
				
				log.info("temp folder: " + path);
				
				/*
				 * Get inputSandbox and jdl parameters
				 */
				
				List<String> inputSandbox = new ArrayList<String>();
				
		        File tempFile;
		        
		        boolean needsWrapper = false;
		        
		        @SuppressWarnings("unchecked")
				Enumeration<String> paramEnum = uploadRequest.getParameterNames();
		        while (paramEnum.hasMoreElements())
		        {
		        	
		            String parameter = paramEnum.nextElement();
		            
		           
		            
		            if (parameter.startsWith("uploadFile"))
		            {
		            	
		                
		                
		                String fileName = uploadRequest.getFileName(parameter);
		                log.info(parameter +" = "+fileName);
		                
		                if(!fileName.isEmpty()){
		                	fileName= fileName.replaceAll(" ", "_");
		                	log.info("Uploading " + fileName);
		                
		                	tempFile = uploadRequest.getFile(parameter, true);
		                	
		                	log.info("temp file " + tempFile.getAbsolutePath());
		                	
			                File destination = new File(path + "/" + fileName);
			                
			                log.info("destination file " + destination.getAbsolutePath());
			                
			                FileUtil.copyFile(tempFile, destination);
			                
			                tempFile.delete();
		                	
			                inputSandbox.add(path+"/"+fileName);
			                
			             }
		            }else{
		            	
		            	
		            	if(parameter.contains("executableFile")){
		            		 String fileName = uploadRequest.getFileName(parameter);
				                log.info(parameter +" = "+fileName);
				                
				                if(!fileName.isEmpty()){
				                	fileName= fileName.replaceAll(" ", "_");
				                	needsWrapper = true;
				                	
				                	log.info("Uploading exe file: " + fileName);
				                
				                	tempFile = uploadRequest.getFile(parameter, true);
				                	
				                	log.info("temp file " + tempFile.getAbsolutePath());
				                	
					                File destination = new File(path + "/" + fileName);
					                
					                log.info("destination file " + destination.getAbsolutePath());
					                
					                FileUtil.copyFile(tempFile, destination);
					                
					                tempFile.delete();
				                	
					                inputSandbox.add(path+"/"+fileName);
					                jdl.setExecutable(fileName);
					                
					             }
		            	} else{
			            	String value = uploadRequest.getParameter(parameter);
			            	log.info(parameter +" = "+value);
			            	if(parameter.contains("executable")||parameter.contains("uploadedFile_")){
			            		File check = new File(path + "/" + value);
			            		
			            		if(check.exists()){
			            			log.info("Fonunded file: " + path+"/"+value);
			            			inputSandbox.add(path+"/"+value);
			            			log.info("File " + value + " inserted.");
			            		}
			            		if(parameter.contains("executable")){
			            			
							        if(!value.startsWith("/"))
							        	needsWrapper=true;
							        
							        jdl.setParameter(parameter, value);
			            		}
			            	}else{
						        jdl.setParameter(parameter, value);
			            	}
		            	}
		            }
		        }
		        
		        List<String> outputSandbox = new ArrayList<String>();
				outputSandbox.add(jdl.getStdOutput());
				outputSandbox.add(jdl.getStdError());
				if(!jdl.getOutputSandboxRequest().isEmpty()){
					for(String s: jdl.getOutputSandboxRequest().split(";")){
						outputSandbox.add(s.replaceAll(" ", ""));
					}
				}
				jdl.setOutputSandbox(outputSandbox);
				
				log.info("OutputSandbox: " + jdl.getOutputSandbox());
		        
				if(needsWrapper){
					
					String wrapperPath = System.getProperty("java.io.tmpdir") + "/" + diracHome + "/" + diracWrapper;
					List<String> newIS = new ArrayList<String>();
					newIS.add(wrapperPath);
					if(!inputSandbox.isEmpty()){
			        	newIS.addAll(inputSandbox);
			        }
					inputSandbox = newIS;
					
					
					
//					File wrapperFile = new File(wrapperPath);
					
					String destPath = path + "/" + diracWrapper;
					
					FileUtil.copyFile(wrapperPath, destPath);
					
					jdl.setExecutable(diracWrapper);
					
					String arguments = newIS.get(1).substring(newIS.get(1).lastIndexOf("/")+1, newIS.get(1).length()) + " " + jdl.getArguments();
					
					jdl.setArguments(arguments);
				}
				
		        if(!inputSandbox.isEmpty()){
		        	jdl.setInputSandbox(inputSandbox);
		        }
		 
		        log.info("Jdl:\n"+jdl);
		        
		        /*
				 * Save jdl on file
				 */
				
				String jdlFilename = jdl.getJobName()+".jdl";
				
				FileOutputStream jdlFile = new FileOutputStream(path + "/" + jdlFilename);
				jdlFile.write(jdl.toString().getBytes());
				jdlFile.close();
				
				String saveOnly = uploadRequest.getParameter("saveOnly");
				if(saveOnly==null){
				
					/*
					 * Download proxy
					 */
					
					DiracAdminUtil util = new DiracAdminUtil();
					util.dowloadUserProxy(path, user.getScreenName(), jdl.getVo()+"_user");
					
					/*
					 * Submit job
					 */
					
					List<Long> ids = util.submitJob(path, path, jdlFilename);
					
					/*
					 * Adding notify task
					 */
					boolean isNotify = isNotificationSetted(user);
					
					log.info("notify = " + isNotify);
					if(isNotify){
						Notify notify = new Notify(user.getEmailAddress(), user.getFirstName(), ids);
						Checker.addNotify(notify);
					}
				}
				
				/*
				 * Manage Template
				 */
				String saveAsTemplate = uploadRequest.getParameter("saveAsTemplate");
				String shareTemplate = uploadRequest.getParameter("shareTemplate");
				
				log.info("saveAsTempalte: " + saveAsTemplate);				
				log.info("shareTemplate: " + shareTemplate);
				
				if(saveAsTemplate!=null){
					String copyPath;
					if(shareTemplate==null){
						/*
						 * user template
						 */
						copyPath = System.getProperty("java.io.tmpdir") + "/users/"+user.getUserId()+"/DIRAC/"+ templateHome + "/" + jdl.getJobName().replaceAll(" ", "_")+"@"+user.getUserId();
						copyPath = DiracUtil.checkIfExsist(copyPath);
						
					} else {
						/*
						 * shared template
						 */
						copyPath = System.getProperty("java.io.tmpdir") + "/"+diracHome+"/"+ templateHome + "/" + jdl.getJobName().replaceAll(" ", "_")+"@"+user.getUserId();
						copyPath = DiracUtil.checkIfExsist(copyPath);
					}
					File destination = new File(copyPath);
					FileUtil.copyDirectory(jdlFolder, destination);
				}
				
				
				/*
				 * Delete temp folder
				 */
				DiracUtil.delete(jdlFolder);
				
				
			}
			String saveAsTemplate = uploadRequest.getParameter("saveAsTemplate");
			String saveOnly = uploadRequest.getParameter("saveOnly");
			String shareTemplate = uploadRequest.getParameter("shareTemplate");
			
			if(saveOnly!=null){
				response.setRenderParameter("myaction", "showSubmitJob");
				response.setRenderParameter("viewTemplate", "true");
			}else{
				SessionMessages.add(request, "submit-successufully");
			}
			if(saveAsTemplate!=null){
				SessionMessages.add(request, "save-successufully");
			}
			if(shareTemplate!=null){
				SessionMessages.add(request, "shared-successufully");
			}
			return;

		} catch (Exception e) {
			
			if(e.getMessage().contains("no-proxy-uploaded")){
				
				SessionErrors.add(request, e.getMessage());
				PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
				SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
				
				response.setRenderParameter("showUploadCert", "true");
				
			}else{
				if (e.getMessage().equals("submit-error")){
					SessionErrors.add(request, "check-jdl");
				}else{
					e.printStackTrace();
				}
			}
		}
		
		SessionErrors.add(request, "submit-error");
		PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		
		response.setRenderParameter("myaction", "showSubmitJob");
		request.setAttribute("jdl", jdl);
		
	}

	

	private boolean isNotificationSetted(User user) {
		GuseNotifyUtil gnu = new GuseNotifyUtil();
		GuseNotify gn = gnu.readNotifyXML(user);
		if(gn!=null){
			
			log.info("The notification configuration is: " + gn.getWfchgEnab());
			
			if(gn.getWfchgEnab().equals("true"))
				return true;
		}
		return false;
	}
}
