package it.italiangrid.portal.fluka.util;

import it.italiangrid.portal.fluka.db.domain.JobJdls;
import it.italiangrid.portal.fluka.exception.DiracException;
import it.italiangrid.portal.fluka.model.Jdl;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;

public class DiracUtil {
	
	private static final Logger log = Logger.getLogger(DiracUtil.class);
	
	public static Jdl parseJdl(JobJdls diracJdl, long userId) throws DiracException, IOException{
		
		Jdl myJdl = new Jdl();
		
		myJdl.copyJob(diracJdl, userId);
		
		myJdl.setJobName("FLUKA_Job_Copy_of_" + diracJdl.getJobId());
		
		return myJdl;
	}
	
	public static Jdl getTemplate(long userId, String path) throws DiracException, IOException{
		
		Jdl myJdl = new Jdl();
		
		myJdl.copyJob(null, userId, true, path);
		
		myJdl.setJobName("FLUKA_Template_" + myJdl.getJobName());
		
		return myJdl;
	}
	
	public static List<String> getTemplates(int userId){
		List<String> result = new ArrayList<String>();
		
		return result;
	}

	public static void delete(File file)
	    	throws IOException{
	 
    	if(file.isDirectory()){
 
    		//directory is empty, then delete it
    		if(file.list().length==0){
 
    		   file.delete();
    		   log.info("Directory is deleted : " + file.getAbsolutePath());
 
    		}else{
 
    		   //list all the directory contents
        	   String files[] = file.list();
 
        	   for (String temp : files) {
        	      //construct the file structure
        	      File fileDelete = new File(file, temp);
 
        	      //recursive delete
        	     delete(fileDelete);
        	   }
 
        	   //check the directory again, if empty then delete it
        	   if(file.list().length==0){
           	     file.delete();
        	     log.info("Directory is deleted : " + file.getAbsolutePath());
        	   }
    		}
 
    	}else{
    		//if file, then delete it
    		file.delete();
    		log.info("File is deleted : " + file.getAbsolutePath());
    	}
    }
	
	@SuppressWarnings("resource")
	public static void zip(File directory, File zipfile) throws Exception {
		URI base = directory.toURI();
		Deque<File> queue = new LinkedList<File>();
		queue.push(directory);
		OutputStream out = new FileOutputStream(zipfile);
		Closeable res = out;
		try {
			ZipOutputStream zout = new ZipOutputStream(out);
			res = zout;
			while (!queue.isEmpty()) {
				directory = queue.pop();
				for (File kid : directory.listFiles()) {
					String name = base.relativize(kid.toURI()).getPath();
					if (kid.isDirectory()) {
						queue.push(kid);
						name = name.endsWith("/") ? name : name + "/";
						zout.putNextEntry(new ZipEntry(name));
					} else {
						zout.putNextEntry(new ZipEntry(name));
						copy(kid, zout);
						zout.closeEntry();
					}
				}
			}
		}catch(Exception e){ 
			res.close();
		}finally {
			res.close();
		}
	}

	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] buffer = new byte[1024];
		while (true) {
			int readCount = in.read(buffer);
			if (readCount < 0) {
				break;
			}
			out.write(buffer, 0, readCount);
		}
	}

	private static void copy(File file, OutputStream out) throws IOException {
		InputStream in = new FileInputStream(file);
		try {
			copy(in, out);
		} finally {
			in.close();
		}
	}

	@SuppressWarnings("unused")
	private static void copy(InputStream in, File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		try {
			copy(in, out);
		} finally {
			out.close();
		}
	}

	public static String checkIfExsist(String copyPath) {
		File path = new File(copyPath);
		if(path.exists()){
			String owner = copyPath.split("@")[1];
			String oldPath = copyPath.split("@")[0];
			String last = oldPath.substring(oldPath.lastIndexOf("_")+1,oldPath.length());
			if(!last.contains(".")&&!last.contains(",")){
				try{
					int index = Integer.parseInt(last);
					String prefix = oldPath.substring(0,oldPath.lastIndexOf("_")+1);
					index++;
					oldPath=prefix+index;
				} catch (NumberFormatException e){
					oldPath += "_1"; 
				}
			}else{
				oldPath += "_1";
			}
			copyPath = checkIfExsist(oldPath + "@" + owner);
		}
			
		return copyPath;
	}

	public static void mv(File source, String destination) {
		
		File destFolder = new File(destination);
		if(!destFolder.exists())
			destFolder.mkdir();
		
		if(source.isDirectory()) {
		    File[] content = source.listFiles();
		    for(int i = 0; i < content.length; i++) {
		        content[i].renameTo(new File(destination+"/"+content[i].getName()));
		        log.info("File " + content[i].getName() + "moved:\nFrom: " + source.getAbsolutePath() + "\nTo:   " + destination+"/"+content[i].getName());
		    }
		}
		
		source.delete();
		
		
		
	}
	
}