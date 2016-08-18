/**
 * 
 */
package com.strateratech.dhs.peerrate.testingsupport;

import java.io.File;
import java.nio.file.FileAlreadyExistsException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author 2020
 * @date Jan 26, 2016 11:23:47 AM
 * @version 
 */
public class DirectoryUtils {
	private static final Logger log = LoggerFactory.getLogger(DirectoryUtils.class);
	public static void createTargetDir(String dirRelativeToTarget) throws FileAlreadyExistsException {
		File f = new File("target"+File.separator+dirRelativeToTarget);
		if (!f.exists()) {
			if (f.getParentFile().exists() && f.getParentFile().isDirectory()) {
				f.mkdirs();
			}
		} else {
			if (f.isDirectory()) {
				log.debug("It looks like {} already exists.  continuing", f.getAbsolutePath());
			}
			else {
				throw new FileAlreadyExistsException(f.getAbsolutePath()+" exists but is not a directory");
			}
		}
	
	}
	
	public static String cleanFileNameForJenkinsPath(String filename) {
		return filename.replace("%20", " ").replace("%2", " ");
	}

}
