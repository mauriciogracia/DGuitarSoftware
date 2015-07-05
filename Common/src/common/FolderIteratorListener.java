/*
 * Created on 16/03/2005
 *
 */
package common;

import java.io.File;

/**
 * This interface must be implemented bye any class that 
 * wants to interate over a folder content
 * 
 * @see common.FolderIterator 
 * @author Mauricio Gracia Gutiérrez
 *
 */
public interface FolderIteratorListener 
{
	/*
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}
	*/
    /**
     * This action will be performed when a File is found by the FolderIterator
     *  
     * @param file The file that was found
     * @param FII The complementary information about that file
     */
	public abstract void actionForFile(File file, FolderIteratorInfo FII);

	/**
     * This action will be performed when a Folder is found by the FolderIterator 
     * before opening that folder to search for more folders
     *  
	 * @param folder The File object that represents the physical folder 
	 * @param path   The File object that represents the path were was found
	 * @param FII	 The complementary FolderIteratorInfo
	 */
	public abstract void actionForFolder(File folder, File path,FolderIteratorInfo FII);

	/**
	 * This action is called right before running the FolderIterator
	 * 
	 * @param FII The complementary FolderIteratorInfo
	 */
	public abstract void actionRunInit(FolderIteratorInfo FII);

	/**
	 * This action is called right after the FolderIterator has finished.
	 * @param FII
	 */
	public abstract void actionRunFinished(FolderIteratorInfo FII);

}
