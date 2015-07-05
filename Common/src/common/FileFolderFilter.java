/*
 * Created on 16/03/2005
 *
 */
package common;

import java.io.File;
import java.io.FilenameFilter;

/**
 * This class accepts all the folders and the Files specify by the FilenameFilter
 * @author Mauricio Gracia Gutiérrez
 * 
 */
public class FileFolderFilter
implements FilenameFilter,LicenseString {
	private FilenameFilter filenameFilter;

	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}

	public FileFolderFilter(FilenameFilter aFileNameFilter) {
	    if(aFileNameFilter != null) {
	        this.filenameFilter = aFileNameFilter;
	    } else {
	        throw new NullPointerException("aFilenameFilter is null") ;
	    }
	        
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
	 */
	public final boolean accept(File dir, String name) {
        boolean resp ;
        File f ;
        
        resp = false ;
        if( (dir != null) && (name != null) ) {
            //create a file object that represents a posible folder.
        	f = new File(dir.getAbsolutePath() + File.separator + name) ;
        	//check if IT is a folder
        	resp = f.isDirectory() ;
        	//the file is accepted if it is a folder or matches the extension.

        	//THIS is called like this...since the fileNameFiter that the user
			// specifies should NEVER use the first argument

        	resp = resp || (this.filenameFilter.accept(null, name)) ;
        }
        return resp ;

/*		boolean resp;

		resp = false;
		if (arg0 != null) {
			resp = arg0.isDirectory();
		}
		if (arg1 != null) {
			//THIS is called like this...since the fileNameFiter that the user
			// specifies...
			//should not use the first argument
			resp = resp || this.FNF.accept(null, arg1);
			//resp = resp || this.FNF.accept(arg0,arg1) ;
		}
		return resp;
		*/
	}

    /**
     * @param aFilenameFilter The filenameFilter to set.
     */
    public void setFilenameFilter(FilenameFilter aFilenameFilter) {
        if(aFilenameFilter != null) {
            this.filenameFilter = aFilenameFilter;
        }
    }

    /**
     * @return Returns the filenameFilter.
     */
    public FilenameFilter getFilenameFilter() {
        return filenameFilter;
    }


}

