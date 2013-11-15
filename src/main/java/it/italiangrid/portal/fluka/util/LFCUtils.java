package it.italiangrid.portal.fluka.util;

import it.italiangrid.portal.fluka.exception.DiracException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.liferay.portal.model.User;

public class LFCUtils {
	
	private static final Logger log = Logger.getLogger(LFCUtils.class);
	
	
	public static List<String> getHomes(String userHomeDir) throws DiracException{
		String homesPath = DiracConfig.getProperties("Fluka.properties", "fluka.lfc.homes.path");
		
		try{
			List<String> results = new ArrayList<String>();
			
	        String lfc = "lfc-ls " + homesPath;
	        
	        log.info("LFC Command: " + lfc);
	        log.info("X509_USER_PROXY="+userHomeDir + "x509up.gridit");
	        String[] envp = {"LFC_HOST=lfcserver.cnaf.infn.it","X509_USER_PROXY="+userHomeDir + "x509up.gridit"};
	        Process p = Runtime.getRuntime().exec(lfc, envp, new File(userHomeDir));
	        InputStream stdout = p.getInputStream();
	        InputStream stderr = p.getErrorStream();
	
	        BufferedReader output = new BufferedReader(
	                        new InputStreamReader(stdout));
	        String line = null;
	
	        while (((line = output.readLine()) != null)) {
	
	                log.info("[Stdout] " + line);
	                results.add(line);
	                if (line.contains(" Could not find or use a credential")) {
	                    throw new DiracException("no-proxy-found");
	                }
	        }
	        output.close();
	
	        BufferedReader brCleanUp = new BufferedReader(
	                        new InputStreamReader(stderr));
	        while ((line = brCleanUp.readLine()) != null) {
	                log.error("[Stderr] " + line);
	        }
	        brCleanUp.close();
	        
	        return results;
		} catch (IOException e) {
			throw new DiracException("lfc-execution-problem");
		}
	}
	
	public static List<String> getInputs(String userHomeDir, String userHome) throws DiracException{
		String homesPath = DiracConfig.getProperties("Fluka.properties", "fluka.lfc.homes.path");
		String searchDir = homesPath + "/" + userHome;
		
		try{
			List<String> results = new ArrayList<String>();
			
	        String lfc = "lfc-ls -R " + searchDir;
	        
	        log.info("LFC Command: " + lfc);
	        log.info("X509_USER_PROXY="+userHomeDir + "x509up.gridit");
	        String[] envp = {"LFC_HOST=lfcserver.cnaf.infn.it","X509_USER_PROXY="+userHomeDir + "x509up.gridit"};
	        Process p = Runtime.getRuntime().exec(lfc, envp, new File(userHomeDir));
	        InputStream stdout = p.getInputStream();
	        InputStream stderr = p.getErrorStream();
	
	        BufferedReader output = new BufferedReader(
	                        new InputStreamReader(stdout));
	        String line = null;
	        String currentDir = "";
	        while (((line = output.readLine()) != null)) {
	
	                log.info("[Stdout] " + line);
	                if(line.contains(searchDir)){
	                	currentDir= line.substring(0, line.length()-1);
	                }else{
	                	if(line.contains("input_")){
	                		results.add(currentDir+"/"+line);
	                	}
	                }
	                
	                if (line.contains(" Could not find or use a credential")) {
	                    throw new DiracException("no-proxy-found");
	                }
	        }
	        output.close();
	
	        BufferedReader brCleanUp = new BufferedReader(
	                        new InputStreamReader(stderr));
	        while ((line = brCleanUp.readLine()) != null) {
	                log.error("[Stderr] " + line);
	        }
	        brCleanUp.close();
	        
	        return results;
		} catch (IOException e) {
			throw new DiracException("lfc-execution-problem");
		}
	}
	
	public static List<String> getOutputs(String userHomeDir, String userHome) throws DiracException{
		String homesPath = DiracConfig.getProperties("Fluka.properties", "fluka.lfc.homes.path");
		String searchDir = homesPath + "/" + userHome;
		
		try{
			List<String> results = new ArrayList<String>();
			
	        String lfc = "lfc-ls -R " + searchDir;
	        
	        log.info("LFC Command: " + lfc);
	        log.info("X509_USER_PROXY="+userHomeDir + "x509up.gridit");
	        String[] envp = {"LFC_HOST=lfcserver.cnaf.infn.it","X509_USER_PROXY="+userHomeDir + "x509up.gridit"};
	        Process p = Runtime.getRuntime().exec(lfc, envp, new File(userHomeDir));
	        InputStream stdout = p.getInputStream();
	        InputStream stderr = p.getErrorStream();
	
	        BufferedReader output = new BufferedReader(
	                        new InputStreamReader(stdout));
	        String line = null;
	
	        while (((line = output.readLine()) != null)) {
	
	                log.info("[Stdout] " + line);
	                
	                if(line.contains(searchDir)){
	                	results.add(line.substring(0, line.length()-1));
	                }
	                
	                if (line.contains(" Could not find or use a credential")) {
	                    throw new DiracException("no-proxy-found");
	                }
	        }
	        output.close();
	
	        BufferedReader brCleanUp = new BufferedReader(
	                        new InputStreamReader(stderr));
	        while ((line = brCleanUp.readLine()) != null) {
	                log.error("[Stderr] " + line);
	        }
	        brCleanUp.close();
	        
	        return results;
		} catch (IOException e) {
			throw new DiracException("lfc-execution-problem");
		}
	}


	public static void createHome(User user, String userHome) throws DiracException {
		String homesPath = DiracConfig.getProperties("Fluka.properties", "fluka.lfc.homes.path");
		String userHomeDir = System.getProperty("java.io.tmpdir") + "/users/" + user.getUserId() + "/";
		
		try{
			List<String> results = new ArrayList<String>();
			
	        String lfc = "lfc-mkdir " + homesPath+"/"+userHome.replaceAll(" ", "");
	        
	        log.info("LFC Command: " + lfc);
	        log.info("X509_USER_PROXY="+userHomeDir + "x509up.gridit");
	        String[] envp = {"LFC_HOST=lfcserver.cnaf.infn.it","X509_USER_PROXY="+userHomeDir + "x509up.gridit"};
	        Process p = Runtime.getRuntime().exec(lfc, envp, new File(userHomeDir));
	        InputStream stdout = p.getInputStream();
	        InputStream stderr = p.getErrorStream();
	
	        BufferedReader output = new BufferedReader(
	                        new InputStreamReader(stdout));
	        String line = null;
	
	        while (((line = output.readLine()) != null)) {
	
	                log.info("[Stdout] " + line);
	                results.add(line);
	                if (line.contains(" Could not find or use a credential")) {
	                    throw new DiracException("no-proxy-found");
	                }         
	                
	        }
	        output.close();
	
	        BufferedReader brCleanUp = new BufferedReader(
	                        new InputStreamReader(stderr));
	        while ((line = brCleanUp.readLine()) != null) {
	                log.error("[Stderr] " + line);
	                if (line.contains(" File exists")) {
	                	log.info("Exception home-already-exists");
	                    throw new DiracException("home-already-exists");
	                }
	        }
	        brCleanUp.close();
	        
		} catch (IOException e) {
			throw new DiracException("lfc-execution-problem");
		}
		
	}
	
	public static String listToString(List<String> list){
		
		if(list == null)
			return null;
		
		String result = "";
		for (String string : list) {
			result += string + ";";
			
		}
		
		return result.substring(0, result.length()-1);
	}
	
}
