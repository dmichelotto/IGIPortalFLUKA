package it.italiangrid.portal.fluka.util;

import it.italiangrid.portal.fluka.exception.DiracException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class DiracConfig {
	
	private static final Logger log = Logger.getLogger(DiracConfig.class);
	
	public static String getProperties(String file, String key) throws DiracException{
		String contextPath = DiracConfig.class.getClassLoader()
				.getResource("").getPath();
		File test = new File(contextPath + "/content/" + file);
		log.debug("File: " + test.getAbsolutePath());
		if (test.exists()) {
			log.debug("ESISTE!!");
			try {
				FileInputStream inStream = new FileInputStream(contextPath
						+ "/content/" + file);

				Properties prop = new Properties();

				prop.load(inStream);

				inStream.close();
				if (prop.getProperty(key) != null)
					return prop.getProperty(key);
				else
					throw new DiracException("properties-not-found");

			} catch (IOException e) {
				e.printStackTrace();
				throw new DiracException("properties-file-not-found");
			}
		}else{
			throw new DiracException("properties-file-not-found");
		}
	}
	
	public static String getUserProperties(File file, String key) throws DiracException{
		
		try {
			FileInputStream inStream = new FileInputStream(file);

			Properties prop = new Properties();

			prop.load(inStream);

			inStream.close();
			if (prop.getProperty(key) != null)
				return prop.getProperty(key);
			else
				throw new DiracException("properties-not-found");

		} catch (IOException e) {
			e.printStackTrace();
			throw new DiracException("properties-file-not-found");
		}
		
	}
	
	public static void saveProperties(File file, String key, String value) throws DiracException{
		try {
			
			if(file==null){
				String contextPath = DiracConfig.class.getClassLoader().getResource("").getPath();
				file = new File(contextPath + "/content/Fluka.properties");
			}
			
			
			Properties props = new Properties();
			
			if(file.exists()){
				FileInputStream inStream = new FileInputStream(file);
				props.load(inStream);
			}
			
			props.setProperty(key, value);
			
			FileOutputStream fos = new FileOutputStream(file);
			props.store(fos, null);
			fos.close();
			
			log.info("Properties saved in: " + file.getAbsolutePath());
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			throw new DiracException("saving-properties-problem");
		} 
	}

}
