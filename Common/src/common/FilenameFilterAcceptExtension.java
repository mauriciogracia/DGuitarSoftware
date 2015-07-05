/*
 * Created on 23/03/2005
 *
 */
package common;

import java.io.File;
import java.io.FilenameFilter;

/**
 * @author Mauricio Gracia Gutierrez
 *
 */
public class FilenameFilterAcceptExtension 
implements FilenameFilter,LicenseString {
    private String extension ;
    
    public String getLicenseString() {
    	return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
    }
    public FilenameFilterAcceptExtension(String aExtension) {
        if(aExtension != null) {
            this.extension = aExtension.toUpperCase() ;
            if(!aExtension.startsWith(".")) {
                this.extension = "." + this.extension ;
            }
        }else {
            throw new NullPointerException("extension is null") ;
        }
    }
    /* (non-Javadoc)
     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
     */
    public boolean accept(File dir, String name) {
        boolean resp ;
        
        resp = false ;
        if(name != null) {
        	//the file is accepted if the name matches the extension.
        	resp = (name.toUpperCase().endsWith(this.extension)) ;
        }
        return resp ;
    }

}
