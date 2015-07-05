/*
 * Created on 13-mar-2005
 *
 * THIS FILES HAS NOT BEEN UPDATED IN A LONG TIME (note writeen on Nov 28/2005)
 */
package dguitar;
import java.io.File;

import common.FileFolderFilter;
import common.FilenameFilterAcceptExtension;
import common.FolderIterator;
import common.FolderIteratorInfo;
import common.FolderIteratorListener;

/**
 * This program prints to System.out the comands that can be used to compile the
 * DGuitar program
 * 
 * see COMPILING.TXT located under the "src" folder for more info
 * 
 * It uses the common.FolderIterator to check the project structure.
 * 
 * It has only been tested under windows XP
 * 
 * @author Mauricio Gracia Gutiérrez
 *  
 */
public class DGcompile implements FolderIteratorListener {
    /**
     * the path for the classes
     */
    private static String classPath ;

    /**
     * the path of the common package
     * 
     *  @see common
     */
    private static String commonPath ;
    
    /**
     * The separator in a path "/" or "\"
     */
    private static String sep  = File.separator ;

    /**
     * the command for the java compiler
     */
    private String compiler = "javac";

    /**
     * the options for the java compiler stored as string 
     */
    private String options = "-deprecation -g -d .." + sep + "bin";

    /**
     * The action for each file
     * @param file the name of the file
     * @param FII the folderIteratorinfo to be used
     * 
     * @see common.FolderIteratorListener#actionForFile(java.io.File,FolderIteratorInfo)
     */
    public void actionForFile(File file, FolderIteratorInfo FII) {
        /*
         * String binPath ; int i ;
         * 
         * binPath = "../bin" ; i = 1 ; while(i <= this.getCurrentDeep()) {
         * binPath = "../" + binPath ; i++ ; } //System.out.println(binPath ) ;
         * //System.out.println(compiler + " " + options + " " + "-d " + binPath + " " +
         * file) ;
         */
    }

    /**
     * The action to run initially
     * 
     * @see common.FolderIteratorListener#actionRunInit(FolderIteratorInfo)
     */
    public void actionRunInit(FolderIteratorInfo FII) {
        //at this moment not used
    }

    /*
     * (non-Javadoc)
     * 
     * @see common.FolderIterator#actionRunFinished()
     */
    public void actionRunFinished(FolderIteratorInfo FII) {
        //		at this moment not used
    }

    public void actionForFolder(File folder, File Path, FolderIteratorInfo FII) {
        String aux;
        
        //containt the package name
        String res ;
        
        String path;
        int pos;
        int numFiles;
        String cmd ;

        numFiles = folder.listFiles(FII.getFilenameFilter()).length;
        //If there is at least one JAVA FILE
        if (numFiles > 0) {
            aux = folder.toString();

            path = FII.getStartigPath().toString() + sep;

            pos = aux.indexOf(path);

            res = new String(aux.substring(pos + path.length()));

            //this is to remove the Test folders....called "test"
            if (!res.startsWith("test")) {
                //javac -deprecation -d ../bin -g -classpath <CLASSPATH> <PACKAGE>/*.java
                cmd = compiler + " " + options + " " ;
                cmd += "-classpath " + classPath + " " ;
                cmd += res + sep + "*.java" ;
                System.out.println(cmd);

            }
        }
    }

    public static void main(String[] args) {
        DGcompile DGC;
        String current;
        String parent;

        FolderIterator FFI;
        int index;
        String bin;

        parent = new File("").getAbsolutePath();

        bin = sep + "bin";
        index = parent.indexOf(bin);
        
        //OLD commonPath =  ".." + sep + "Common" + sep + "dist" + sep + "common.jar" ;

        //NEW since Ene 10/2006
        commonPath =  ".." + sep + "dist" + sep + "Common.jar" ;
        //if the path contains the BIN string is because it was invoked from a console.
        if (index != -1) {
            parent = parent.substring(0, index);
            commonPath = ".." + sep + commonPath ; 
        } else {
            //DEBUG
            //System.err.println(parent + " does not contain " + bin) ;
        }
        
        classPath = "." + ";" + commonPath ; 

        //DEBUG
        //System.err.println("classPath =" + classPath) ;

        
        //OLDcurrent = parent + sep + "src";
        //NEW since Ene 10/2006
        current = parent ;
        
        //System.out.println("cd" + " " + parent);


        DGC = new DGcompile();
        FFI = new FolderIterator(DGC);

        //DEBUG
        System.err.println("Setting the Startin path to " + current);
        FFI.setStartingPath(new File(current));

        //The current depth of pacakges is 4....DGC.setHowDeep(4) ;
        // to make sure...we call like this
        FFI.setHowDeep(7);
        FFI.setFileFolderFilter(new FileFolderFilter(
                new FilenameFilterAcceptExtension("java")));

        /*commented since this are for WINDOWS/DOS
        System.out.println("@echo off") ;
        System.out.println("REM To create the DGCompile.bat file run this") ;
        System.out.println("REM \t\"cd src\"") ;
        System.out.println("REM \t\"java DGcompile > .." + sep + "DGcompile.bat\"") ; ;
        */
        
//      Since the DGcompile file has been moved this is not needed
        //System.out.println("cd" + " " + "src");

        FFI.run();
        //Since the DGcompile file has been moved this is not needed
        //System.out.println("cd" + " " + "..");
        System.out.flush() ;
        
        //DEBUG
        //System.err.println("Finished");
    }
}