/*
 * FolderIteratorEvent.java
 *
 * Created on 16 de marzo de 2005, 12:46 PM
 */

package common;

import java.io.File;
import java.io.FilenameFilter;

/**
 *
 * @author Mauricio Gracia Gutiérrez
 */
public class FolderIteratorInfo 
implements LicenseString

{
	private File startigPath;

	protected FileFolderFilter fileFolderFilter;

	protected int countFolders;

	protected int countFiles;

	private int howDeep ;

	protected int currentDeep;
	
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}
    /** Creates a new instance of FolderIteratorEvent */
    public FolderIteratorInfo() {
    }
	/**
	 * @param startigPath
	 *            The startigPath to set.
	 */
    protected void setStartigPath(File startigPath) {
		this.startigPath = startigPath;
	}

	/**
	 * @return Returns the startigPath.
	 */
	public File getStartigPath() {
		return startigPath ;
	}

	/**
	 * @param countFolders
	 *            The countFolders to set.
	 */
	protected void setCountFolders(int countFolders) {
		this.countFolders = countFolders;
	}

	/**
	 * @return Returns the countFolders.
	 */
	public int getCountFolders() {
		return countFolders;
	}

	/**
	 * @param countFiles
	 *            The countFiles to set.
	 */
	protected void setCountFiles(int countFiles) {
		this.countFiles = countFiles;
	}

	/**
	 * @return Returns the countFiles.
	 */
	public int getCountFiles() {
		return countFiles;
	}

	/**
	 * 	The fileNameFiter should NEVER use the first argument of the accept(File f, String name)
	 *  
	 *  Since in this version the first parameter is always passed as null 

	 * @param aFileFolderFilter
	 *            The FileFolderFilter to set that implments the accept(File f, String name) but never uses the File argument
	 */
	protected void setFileFolderFilter(FileFolderFilter aFileFolderFilter) {
		this.fileFolderFilter = aFileFolderFilter ;
	}
	/**
	 * @return Returns the filenameFilter.
	 */
	public FilenameFilter getFileFolderFilter() {
		return fileFolderFilter;
	}
    /**
     * @return Returns the filenameFilter.
     */
    public FilenameFilter getFilenameFilter() {
        return fileFolderFilter.getFilenameFilter();
    }

	/**
	 * @return Returns the currentDeep.
	 */
	public int getCurrentDeep() {
		return currentDeep;
	}
	/**
	 * @param howDeep The howDeep to set.
	 */
	protected void setHowDeep(int howDeep) {
		this.howDeep = howDeep;
	}
	/**
	 * @return Returns the howDeep.
	 */
	public int getHowDeep() {
		return howDeep;
	}
    
}
