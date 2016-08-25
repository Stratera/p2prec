package com.strateratech.dhs.peerrate.testingsupport;

import java.io.File;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.lang3.CharEncoding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This utility is shamelessly stolen from 
 * http://stackoverflow.com/questions/15519626/how-to-get-all-classes-names-in-a-package.
 * 
 * Do not add this class to non-test classpath without Extreme testing and code review
 * 
 * @author 2020
 * @date Jan 11, 2016 1:50:20 PM
 * @version 
 */
public class ClassFinder {
	private static final Logger log = LoggerFactory.getLogger(ClassFinder.class);

    private static final char DOT = '.';

    private static final char SLASH = '/';

    private static final String CLASS_SUFFIX = ".class";

    private static final String BAD_PACKAGE_ERROR = "Unable to get resources from path '%s'. Are you sure the package '%s' exists?";

    /**
     * list classes under package 
     * @param scannedPackage
     * @return
     */
    public static List<Class<?>> find(String scannedPackage) {
        String scannedPath = scannedPackage.replace(DOT, SLASH);

    	List<Class<?>> classes = new ArrayList<Class<?>>();
    	try {
	        Enumeration<URL> scannedUrlEnum = Thread.currentThread().getContextClassLoader().getResources(scannedPath);
	        ArrayList<URL> scannedUrls = Collections.list(scannedUrlEnum);
	        if (scannedUrls == null || scannedUrls.size() == 0) {
	            throw new IllegalArgumentException(String.format(BAD_PACKAGE_ERROR, scannedPath, scannedPackage));
	        }
	        for (URL scannedUrl : scannedUrls) {
	        	log.debug("found package url: {} for url {}", scannedUrl.getFile(), scannedUrl);
	        	File scannedDir = new File(URLDecoder.decode(scannedUrl.getFile(), CharEncoding.UTF_8));
	        	for (File file : scannedDir.listFiles()) {
	        		if (file != null) {
	        			classes.addAll(find(file, scannedPackage));
	        		}
	        	}
	        }
    	} catch (Exception e) {
    		log.error("Exception occurred trying to iterate over multiple classpath urls" , e);
    	}
        return classes;
    }

    /**
     * if file is directory, list contents and call this method againw with children.
     * if file is java class, add it to the list
     * @param file
     * @param scannedPackage
     * @return
     */
    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<Class<?>>();
        String resource = scannedPackage + DOT + file.getName();
        if (file.isDirectory()) {
//            for (File child : file.listFiles()) {
//                classes.addAll(find(child, resource));
//            }
        } else if (resource.endsWith(CLASS_SUFFIX)) {
            int endIndex = resource.length() - CLASS_SUFFIX.length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }

}