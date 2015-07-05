/*
 * Created on 23/03/2005
 *
 */
package common;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author MGracia
 *
 */
public class FilenameFilterAcceptAll 
implements FilenameFilter,LicenseString {
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}
	/* (non-Javadoc)
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public boolean accept(File dir, String name) {
		return true;
	}


}
