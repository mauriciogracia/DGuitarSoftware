package dguitar.codecs.guitarPro.version4;

import java.io.File;

/**
 * This is a Filter for the GP file extension
 */

public class GP4FileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {

    
    public boolean accept(File f) {
        boolean ok ;
       
        ok = false ;
        if(f != null) {
            ok = f.isDirectory() ;
            ok = ok || accept(f.getName()) ;
        }
        return ok ;
    }
    public static boolean accept(String name) {
        boolean resp ;
        
        resp = false ;
        if(name != null) {
          resp = (name.toUpperCase().endsWith(".GP4")) ;
        }
        return resp ;
    }
    public String getDescription() {
        return "*.GP4" ;
    }
    
}


