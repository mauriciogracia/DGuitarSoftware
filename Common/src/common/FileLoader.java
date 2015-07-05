/*
 * FileLoader.java
 *
 * Created on 21 de junio de 2004, 12:10 PM
 */

package common;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
/**
 *
 * @author  Mauricio Gracia Gutiérrez
 */
public class FileLoader 
implements LicenseString
{
    public  int sizeLoader ;
    public  int BUFFER = 512 ;
    public  boolean success = false ;
    public  String errorCode = "NO ERROR" ;
    public  byte []resp = null ;
    private File file ;
    private int lengthOfTask ;
    private int current = 0 ;
    private boolean done = false ;
    private boolean canceled = false ;
    private String statMessage ;
    
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}
    /**
     * Creates a new instance of FileLoader opening <fileName>
     */
    public FileLoader(String fileName) {
        file = new File(fileName) ;
        if( (file != null) && (file.isFile()))
            sizeLoader = this.lengthOfTask = (int) file.length() ;
        else 
            errorCode = "FILE OBJECT COULD NOT BE CREATED" ;
    }
    /**
     * Creates a new instance of FileLoader based on al already open file
     */
    public FileLoader(File userFile) {
        file = userFile ;
        if( (file != null) && (file.isFile()))
            sizeLoader = this.lengthOfTask = (int) file.length() ;
        else 
            errorCode = "FILE OBJECT COULD NOT BE CREATED" ;

    }
    
    public void loadFile() {
        final SwingWorker worker = new SwingWorker() {
            public Object construct() {
                Object respB = null ;
                current = 0;
                done = false;
                canceled = false;
                statMessage = null;
                respB = new Loader(file) ;
                return(respB) ;
            }
        };
        worker.start();
    }
    /**
     * This is called to find out how much work needd to be done to complete the load.
     */
    public int getLengthOfTask() {
        return lengthOfTask;
    }
    
    /**
     * This is called to find out how much has been loaded.
     */
    public int getCurrent() {
        return current;
    }
    
    public void stop() {
        canceled = true;
        statMessage = null;
    }
    
    /**
     * This is called to find out if the task has completed.
     */
    public boolean isDone() {
        return done;
    }
    
    /**
     * Returns the most recent status message, or null
     * if there is no current status message.
     */
    public String getMessage() {
        return statMessage;
    }
    /*
                try {
                FR = new FileReader(str) ;
                BR = new BufferedReader(FR) ;
                do {
                    line = BR.readLine() ;
                    if(line != null) {
                        this.append(line + "\n") ;    
                        contLines++ ;
                    }
                } while (line != null) ;
            } catch (Exception e) {
                success = false ;
                e.printStackTrace() ;
            }
    */
    /** This class performs the actual loading of the file
     *
     */
    class Loader {
        String errorStr ;
        
        public Loader(File file) {
            FileInputStream fis ;
            int i ;
            int j ;
            byte []mem = null ;
            long size ;
            int leyo ;
            
            fis = null ;
            try {
                fis = new FileInputStream(file) ;
                size = file.length() ;
                if(size > 0) {
                    mem = new byte[BUFFER] ;
                    resp = new byte[(int) size] ;
                        j = 0 ;
                        do {
                            leyo = fis.read(mem) ;
                            if(leyo > 0) {
                                for(i = 0; (i < leyo) && (!canceled); i++) {
                                    //resp[j] = new Byte(mem[i]) ;
                                    resp[j] = mem[i] ;
                                    j++ ;
                                    current = j ;
                                    statMessage = "Loading byte " + j + "/" + size ;
                                }
                            }
                        }while(leyo > 0) ;
                        if(!canceled) {
                            done = true ;
                            current = lengthOfTask ;
                        }
                        
                    }
            } catch (IOException e) {
                this.errorStr = e.toString() ;
                success = false ;
            } finally {
                if(fis != null) {
                    try {
                        fis.close();
                        success = true ;
                    }
                    catch (IOException e) {
                        this.errorStr = e.toString() ;
                    }
                }
            }
        }
    }
}