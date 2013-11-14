package it.italiangrid.portal.fluka.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyStoreException;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

import it.italiangrid.portal.dbapi.domain.Certificate;
import it.italiangrid.portal.dbapi.domain.UserInfo;
import it.italiangrid.portal.dbapi.services.CertificateService;
import it.italiangrid.portal.dbapi.services.UserInfoService;
import it.italiangrid.portal.fluka.exception.DiracException;
import it.italiangrid.portal.fluka.model.DiracTask;
import it.italiangrid.portal.fluka.model.UploadCertificate;
import it.italiangrid.portal.fluka.util.DiracConfig;
import it.italiangrid.portal.fluka.util.DiracTaskJSON;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletConfig;
import javax.portlet.RenderRequest;
import org.apache.log4j.Logger;
import org.glite.security.util.DNHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;

import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.upload.UploadPortletRequest;
import com.liferay.portal.kernel.util.FileUtil;
import com.liferay.portal.kernel.util.JavaConstants;
import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

import eu.emi.security.authn.x509.X509Credential;
import eu.emi.security.authn.x509.impl.CertificateUtils;
import eu.emi.security.authn.x509.impl.CertificateUtils.Encoding;
import eu.emi.security.authn.x509.impl.KeystoreCredential;

/**
 * Controller that manage the user's certificate upload, and submit through RESTfull inteface a DiracTask to the Registration portlet.
 * 
 * @author dmichelotto
 *
 */
@Controller("uploadCertificateController")
@RequestMapping(value = "VIEW")
public class UploadCertificateController {
	
	/**
	 * Class logger.
	 */
	private static final Logger log = Logger.getLogger(UploadCertificateController.class);
	
	/**
	 * Certificate DB service.
	 */
	@Autowired
	private CertificateService certificateService;
	
	/**
	 * UserInfo DB service.
	 */
	@Autowired
	private UserInfoService userInfoService;

	/**
	 * Return the upload.jsp page.
	 * 
	 * @param request - the request.
	 * @return the upload page.
	 */
	@RenderMapping(params = "myaction=showUploadCert")
	public String showUploadCert(RenderRequest request){
		log.info("Show Certificate Uploader");
		
		return "upload";

	}
	
	/**
	 * Pass to the page a new upload instance.
	 * 
	 * @return the new instance.
	 */
	@ModelAttribute("uploadCert")
	public UploadCertificate newDiracTask(){
		return new UploadCertificate();
	}
	
	/**
	 * Receive the certificate, split the certificate in PEM format and store these file in the appropriate directory, check if it is the appropriate not expired user certificate and submit via REST a new DiracTask.
	 * 
	 * @param uploadCert - the instance that contain the certificate and the certificate password.
	 * @param request - the request.
	 * @param response - the response.
	 */
	@ActionMapping(params="myaction=uploadCert")
	public void uplaodCert(@ModelAttribute UploadCertificate uploadCert, ActionRequest request, ActionResponse response){
		log.info("Uploading Certificate...");
		
		try {
			User user = PortalUtil.getUser(request);

			if (user != null) {
			
				File tempFile;
				UploadPortletRequest uploadRequest = PortalUtil.getUploadPortletRequest(request);
		        
				@SuppressWarnings("unchecked")
				Enumeration<String> paramEnum = uploadRequest.getParameterNames();
				
				while (paramEnum.hasMoreElements()){
					String parameter = paramEnum.nextElement();
					if(parameter.contains("certificate")){
						String fileName = uploadRequest.getFileName(parameter);
		                log.info(parameter +" = "+fileName);
		                
		                if(!fileName.isEmpty()){
		                	
		                	log.info("Uploading " + fileName);
		                
		                	tempFile = uploadRequest.getFile(parameter, true);
		                	
		                	log.info("temp file " + tempFile.getAbsolutePath());
		                	
			                File destination = new File("/upload_files/" + fileName);
			                
			                log.info("destination file " + destination.getAbsolutePath());
			                
			                FileUtil.copyFile(tempFile, destination);
			                
			                tempFile.delete();
			                
			                uploadCert.setCertificate(destination.getAbsolutePath());
			                
			             }
					}
					if(parameter.contains("password")){
						String value = uploadRequest.getParameter(parameter);
				        
		            	uploadCert.setPassword(value);
				        
					}
				}
				
				log.info(uploadCert);
				
				
				
				X509Credential chain = new KeystoreCredential(uploadCert.getCertificate(), uploadCert.getPassword().toCharArray(), uploadCert.getPassword().toCharArray(), null, "PKCS12");
				
				if(chain.getCertificate().getNotAfter().before(new GregorianCalendar().getTime()))
					log.info("Certificate Expired");
				else
					log.info("Certificate NOT Expired");
				
				String uuid = UUID.randomUUID().toString();
				File userCert = new File("/upload_files/usercert_"+uuid+".pem");
				FileOutputStream usercertOS = new FileOutputStream(userCert);
				File userKey = new File("/upload_files/userkey_"+uuid+".pem");
				FileOutputStream userkeyOS = new FileOutputStream(userKey);
				
				CertificateUtils.saveCertificate(usercertOS, chain.getCertificate(), Encoding.PEM);
				CertificateUtils.savePrivateKey(userkeyOS, chain.getKey(), Encoding.PEM, "AES-256-CBC", uploadCert.getPassword().toCharArray(),true);
				
				DiracTask diracTask = new DiracTask(userCert.getAbsolutePath(), userKey.getAbsolutePath(), uploadCert.getPassword(), user.getEmailAddress(), DNHandler.getSubject(chain.getCertificate()).getX500(), user.getScreenName(), DiracTask.ADD_TASK);
				
				DiracTaskJSON diracTaskJSON = new DiracTaskJSON(diracTask);
				
				log.info(diracTaskJSON.getEncodedDiracTask()); 
				
				/*
				 * Check if DN is equal to previously uploaded certificate
				 */
				
				UserInfo userInfo = userInfoService.findByMail(user.getEmailAddress());
				List<Certificate> certs = certificateService.findById(userInfo.getUserId());
				boolean certificateNotFound = true;
				for (Certificate certificate : certs) {
					if(certificate.getSubject().equals(diracTask.getDn()))
						certificateNotFound = false;
				}
				
				if(certificateNotFound)
					throw new DiracException("certificate-not-corresponding");
				
				/*
				 * Contact restful service
				 */
				
				restClient(diracTaskJSON.getEncodedDiracTask());
				
				FileUtil.delete(new File(uploadCert.getCertificate()));
				
				SessionMessages.add(request, "upload-successufully");
				response.setRenderParameter("myaction", "showSuccessPage");
				return;
			}
		} catch(DiracException e) {
			SessionErrors.add(request, e.getMessage());
			log.info(e.getMessage());
		} catch(KeyStoreException e) {
			if(e.getMessage().equals("Keystore key password is invalid or the keystore is corrupted.")){
				SessionErrors.add(request, "check-certificate-password");
				log.info(e.getMessage());
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		SessionErrors.add(request, "upload-error");
		PortletConfig portletConfig = (PortletConfig)request.getAttribute(JavaConstants.JAVAX_PORTLET_CONFIG);
		SessionMessages.add(request, portletConfig.getPortletName() + SessionMessages.KEY_SUFFIX_HIDE_DEFAULT_ERROR_MESSAGE);
		
		response.setRenderParameter("myaction", "showUploadCert");
		request.setAttribute("uploadCert", uploadCert);
		
	}
	
	/**
	 * REST client for the JSON POST to Registration Portlet.
	 * @param input - the DiracTask encoded in JSON.
	 */
	private void restClient(String input) {
		try {
			
			URL url;
			try {
				url = new URL(DiracConfig.getProperties("Fluka.properties", "registration.rest.url"));
			} catch (DiracException e) {
				url = new URL("http://portal.italiangrid.it:8080/Registration-4/rest/addtask");
				e.printStackTrace();
			}
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");

			OutputStream os = conn.getOutputStream();
			os.write(input.getBytes());
			os.flush();

			if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String output;
			System.out.println("Output from Server .... \n");
			while ((output = br.readLine()) != null) {
				System.out.println(output);
			}

			conn.disconnect();

		} catch (MalformedURLException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

}
