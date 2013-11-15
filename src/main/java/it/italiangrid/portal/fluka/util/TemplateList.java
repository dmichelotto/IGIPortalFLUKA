package it.italiangrid.portal.fluka.util;

import it.italiangrid.portal.fluka.exception.DiracException;
import it.italiangrid.portal.fluka.model.Template;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

public class TemplateList {
	
	private static final Logger log = Logger.getLogger(TemplateList.class);

	private long user;
	private List<Template> templates;

	/**
	 * @param user
	 */
	public TemplateList(long user) {
		this.user = user;
		templates = new ArrayList<Template>();
	}

	/**
	 * 
	 */
	public TemplateList() {
		templates = new ArrayList<Template>();
	}

	/**
	 * @return the user
	 */
	public long getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(long user) {
		this.user = user;
	}
	
	/**
	 * @return the templates
	 */
	public List<Template> getTemplates() {
		return templates;
	}

	/**
	 * @param templates the templates to set
	 */
	public void setTemplates(List<Template> templates) {
		this.templates = templates;
	}
	
	public void load(){
		
		scanFolder(getPath(true), "Private");
		scanFolder(getPath(false), "Shared");
		
		Collections.sort(this.templates);
		
	}

	private void scanFolder(String path, String type) {
		File folder = new File(path);
		
		if(folder.list()==null)
			return;
		
		List<File> jobNameList = new ArrayList<File>(Arrays.asList(folder.listFiles()));
		for (File templateFile : jobNameList) {
			String templatePath = templateFile.toString();
			String name = templatePath.substring(templatePath.lastIndexOf("/")+1,templatePath.length()).split("@")[0];
			String owner = templatePath.substring(templatePath.lastIndexOf("/")+1,templatePath.length()).split("@")[1];
			this.templates.add(new Template(name, owner, type, templateFile.getAbsolutePath()));
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TemplateList [user=" + user + ", templates=" + templates + "]";
	}
	
	public boolean isShared(String path) {
		
		String sharePath = getPath(false) + path.substring(path.lastIndexOf("/"), path.length());
		
		return path.equals(sharePath);
		
	}

	public void share(String path) {
		
		if(isShared(path)){
			log.info("Template " + path + " already shared");
			return;
		}
		
		String sharePath = getPath(false) + path.substring(path.lastIndexOf("/"), path.length());
		sharePath = DiracUtil.checkIfExsist(sharePath);
		log.info("SHARING\nMove template:\nFrom: " + path + "\nTo: " + sharePath);
		
		DiracUtil.mv(new File(path), sharePath);
		
	}
	
	public boolean isPrivate(String path) {
		
		String localPath = getPath(true) + path.substring(path.lastIndexOf("/"), path.length());
		
		return path.equals(localPath);
	}

	public void unshare(String path) {
		
		if(isPrivate(path)){
			log.info("Template " + path + " already private.");
			return;
		}
		
		String localPath = getPath(true) + path.substring(path.lastIndexOf("/"), path.length());
		localPath = DiracUtil.checkIfExsist(localPath);
		log.info("UNSHARING\nMove template:\nFrom: " + path + "\nTo: " + localPath);
		
		DiracUtil.mv(new File(path), localPath);
	}
	
	private String getPath(boolean isPrivate){
		String diracHome;
		String templateHome;
		String tomcatTemp = System.getProperty("java.io.tmpdir");
		try {
			diracHome = DiracConfig.getProperties("Fluka.properties", "dirac.admin.homedir");
			templateHome = DiracConfig.getProperties("Fluka.properties", "dirac.template.home");
		} catch (DiracException e) {
			diracHome = "diracHome";
			templateHome = "Template";
			e.printStackTrace();
		}
		
		String userPath = tomcatTemp + "/users/" + this.user + "/DIRAC/" + templateHome;
		String sharedPath = tomcatTemp + "/" + diracHome + "/" + templateHome;
		
		File createUserPath = new File(userPath);
		File createSharedPath = new File(sharedPath);
		
		if(!createUserPath.exists())
			createUserPath.mkdirs();
		
		if(!createSharedPath.exists())
			createSharedPath.mkdirs();
		
		if(isPrivate)
			return userPath;
		return sharedPath;
	}
	
}
