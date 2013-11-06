package it.italiangrid.portal.fluka.controllers;

import it.italiangrid.portal.fluka.admin.DiracAdminUtil;
import it.italiangrid.portal.fluka.db.domain.Jobs;
import it.italiangrid.portal.fluka.db.service.JobsService;
import it.italiangrid.portal.fluka.util.DiracUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.liferay.portal.model.User;
import com.liferay.portal.util.PortalUtil;

@Controller("resourceController")
@RequestMapping(value = "VIEW")
public class ResourceController {
	
	private static final int BYTE_ARRAY_SIZE = 4096;
	
	/**
	 * Logger
	 */
	private static final Logger log = Logger.getLogger(JobActionController.class);
	
	@Autowired
	private JobsService jobsService;
	
	@ResourceMapping(value = "getOutputZipFile")
	public void getOutputJob(ResourceRequest request, ResourceResponse response){
		log.info("Get output for job: " + request.getParameter("jobId"));
		
		try {
			User user = PortalUtil.getUser(request);
			if (user != null) {

				long jobId = Long.parseLong(request.getParameter("jobId"));
				
				Jobs job = jobsService.findById(jobId);
				
				String userPath = System.getProperty("java.io.tmpdir") + "/users/"+user.getUserId();
				
				File storePath = new File(userPath + "/DIRAC/Outputs/" + job.getJobName() + "_" + job.getJobId());
				
				File zipFile = new File(userPath + "/DIRAC/Outputs/" + job.getJobName() + "_" + job.getJobId() + ".zip");
				
				storePath.mkdirs();
				
				DiracAdminUtil util = new DiracAdminUtil();
				
				util.dowloadUserProxy(storePath.getAbsolutePath(), job.getOwner(), job.getOwnerGroup());
				util.getOutputJob(storePath.getAbsolutePath(), jobId, storePath.getAbsolutePath());
				
				File sourcePath = new File(storePath.getAbsolutePath()+"/"+job.getJobId());
				
				DiracUtil.zip(sourcePath, zipFile);
				
				response.setContentType("application/zip");
				response.setProperty("Content-Disposition", "attachment; filename=\"" + job.getJobName() + "_" + job.getJobId() + ".zip\"");
				
				
				FileInputStream fileIn = new FileInputStream(zipFile);
				
				OutputStream out = response.getPortletOutputStream();
				
				byte[] outputByte = new byte[BYTE_ARRAY_SIZE];
				
				while (fileIn.read(outputByte, 0, BYTE_ARRAY_SIZE) != -1) {
					out.write(outputByte, 0, BYTE_ARRAY_SIZE);
				}
				fileIn.close();
				out.flush();
				out.close();
				
				DiracUtil.delete(storePath);
				DiracUtil.delete(zipFile);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@ResourceMapping(value = "getMultipleOutputZipFile")
	public void getOutputMultipleJob(@RequestParam long[] jobs, ResourceRequest request, ResourceResponse response){
		
		try {
			User user = PortalUtil.getUser(request);
			if (user != null) {
				
				String userPath = System.getProperty("java.io.tmpdir") + "/users/"+user.getUserId();
				
				String jobsName = "Jobs_" + jobs[jobs.length-1] + "_" + jobs[0];
				
				File sourcePath = new File(userPath + "/DIRAC/Outputs/"+ jobsName);
				
				if(!sourcePath.exists())
					sourcePath.mkdirs();
				
				DiracAdminUtil util = new DiracAdminUtil();
				Jobs job = jobsService.findById(jobs[0]);
				util.dowloadUserProxy(sourcePath.getAbsolutePath(), job.getOwner(), job.getOwnerGroup());
				
				for(long jobId : jobs){
					
					job = jobsService.findById(jobId);
					
					File storePath = new File(userPath + "/DIRAC/Outputs/" + jobsName + "/" + job.getJobName() + "_" + job.getJobId());
					
					storePath.mkdirs();
					
					util.getOutputJob(sourcePath.getAbsolutePath(), jobId, storePath.getAbsolutePath());
					
					File downloaded = new File(userPath + "/DIRAC/Outputs/" + jobsName + "/" + job.getJobName() + "_" + job.getJobId() + "/" + job.getJobId());
					
					DiracUtil.mv(downloaded, storePath.getAbsolutePath());
				}
				
				File proxy = new File(sourcePath.getAbsolutePath() + "/x509up");
				
				if(proxy.exists())
					proxy.delete();
				
				File zipFile = new File(userPath + "/DIRAC/Outputs/" + jobsName + ".zip");
				
				DiracUtil.zip(sourcePath, zipFile);
				
				response.setContentType("application/zip");
				response.setProperty("Content-Disposition", "attachment; filename=\"" + jobsName + ".zip\"");
				
				
				FileInputStream fileIn = new FileInputStream(zipFile);
				
				OutputStream out = response.getPortletOutputStream();
				
				byte[] outputByte = new byte[BYTE_ARRAY_SIZE];
				
				while (fileIn.read(outputByte, 0, BYTE_ARRAY_SIZE) != -1) {
					out.write(outputByte, 0, BYTE_ARRAY_SIZE);
				}
				fileIn.close();
				out.flush();
				out.close();
				
				DiracUtil.delete(sourcePath);
				DiracUtil.delete(zipFile);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
