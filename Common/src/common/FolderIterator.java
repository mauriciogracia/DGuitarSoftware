/*
 * Created on 12-mar-2005
 *
 */
package common;

import java.io.File;
import java.util.Iterator;
import java.util.Vector;

/**
 * @author Mauricio Gracia Gutiérrez
 *  
 */
public class FolderIterator 
implements Runnable,LicenseString {
    private FolderIteratorInfo FII;

    private FolderIteratorListener folderIteratorListener;

	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}
    public FolderIterator() {
        this.folderIteratorListener = null ;
        commonConstructor() ;
        FII.setStartigPath(new File(""));
    }
    public FolderIterator(FolderIteratorListener aFolderIteratorListener) {
        this.setFolderIteratorListener(aFolderIteratorListener) ;
        commonConstructor() ;
        FII.setStartigPath(new File(""));
    }

    public FolderIterator(String pathName) {
        this.folderIteratorListener = null ;
        this.setStartingPath(pathName);
        commonConstructor() ;
    }

    public FolderIterator(File path) {
        this.folderIteratorListener = null ;
        this.setStartingPath(path);
        commonConstructor() ;
    }
    private void commonConstructor() {
        this.FII = new FolderIteratorInfo();
        FII.setFileFolderFilter(new FileFolderFilter(
            new FilenameFilterAcceptAll()));
        FII.setCountFolders(0);
        FII.setCountFiles(0);
        FII.currentDeep = 0;
    	this.setHowDeep(0);
    }
    
    public void setFolderIteratorListener(FolderIteratorListener aFolderIteratorListener) {
        if (aFolderIteratorListener != null) {
            this.folderIteratorListener = aFolderIteratorListener ;
        } else {
            throw new NullPointerException("FolderIteratorListener is null");
        }
    }
    public void setStartingPath(String pathName) {
        File path;

        path = new File(pathName);
        this.setStartingPath(path);

    }

    public void setStartingPath(File path) {
        if ((path != null) && (path.isDirectory())) {
            FII.setStartigPath(path);
        }
    }

    /**
     * @param howDeep
     *            The howDeep to set.
     */
    public void setHowDeep(int howDeep) {
        if (howDeep > 0) {
            FII.setHowDeep(howDeep);
        }
    }

    /**
     * @return Returns the howDeep.
     */
    public int getHowDeep() {
        return FII.getHowDeep();
    }

    private Vector<File> stringsToFiles(File path, String[] list) {
        int i;
        Vector<File> files;
        File f;

        files = null;
        if (list != null) {
            files = new Vector<File>(0, 1);
            
            for (i = 0; i < list.length; i++) {
                f = new File(path + File.separator + list[i]);
                files.add(f);
            }
        }
        return files;
    }

    private void runImpl(File path) {
        String[] list;
        Vector<File> files;
        File f;
        Iterator<File> it;

        //obtain the list of the current files and folders
        if (FII.getFileFolderFilter() != null) {
            list = path.list(FII.fileFolderFilter);
        } else {
            //DEBUG
            //System.err.println("no file filter specified for path >" + path +
            // "<");
            list = path.list();
        }
        if (list == null) {
            System.err.println("path >" + path + "< is not a Directory !!");
        } else {
            //convert the String[] to a <File> Vector
            files = stringsToFiles(path, list);

            //it = Collections.
            if (files != null) {
                it = files.iterator();
                while (it.hasNext()) {
                    f = (File) it.next();
                    //TODO...apparently f.isFile() is not the same as
                    // !f.isDirectory() !!
                    if (!f.isDirectory()) {
                        //IS not a DIRECTORY
                        this.actionForFile(f);
                    } else {
                        //IS A DIRECTORY
                        this.actionForFolder(f, path);
                    }
                }
            }
        }
    }

    private void actionForFile(File file) {
        FII.countFiles++;
        if (this.folderIteratorListener != null) {
            this.folderIteratorListener.actionForFile(file, FII);
        }
    }

    private void actionForFolder(File folder, File path) {
        FII.countFolders++;
        if (this.folderIteratorListener != null) {
            this.folderIteratorListener.actionForFolder(folder, path, FII);
        }

        if (FII.currentDeep < getHowDeep()) {
            FII.currentDeep++;
            //RECURSIVE call
            runImpl(folder);
            FII.currentDeep--;
        } else {
            String aux;

            aux = "path >" + path;
            aux += "< had more folders but \"howDeep\" prevented from descending more";
            System.err.println(aux);
        }

    }

    private void actionRunInit() {
        if (this.folderIteratorListener != null) {
            this.folderIteratorListener.actionRunInit(FII);
        }

    }

    private void actionRunFinished() {
        if (this.folderIteratorListener != null) {
            this.folderIteratorListener.actionRunFinished(FII);
        }
    }

    /**
     * invoke this method to start the Iteration over the folders.
     */

    public final void run() {
        this.actionRunInit();

        this.runImpl(FII.getStartigPath());

        this.actionRunFinished();
    }

    /**
     * @return Returns the folderIteratorListener.
     */
    public FolderIteratorListener getFolderIteratorListener() {
        return folderIteratorListener;
    }

    /**
     * @param filter
     */
    public void setFileFolderFilter(FileFolderFilter filter) {
        if (filter != null) {
            FII.setFileFolderFilter(filter);
        }
    }
}