package dguitar.codecs.guitarPro.version2;

import java.io.File;

import javax.swing.filechooser.FileFilter;
/**
 * This is a Filter for the GTP file extension
 */

public class GP2FileFilter extends FileFilter implements java.io.FileFilter {

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
          resp = (name.toUpperCase().endsWith(".GTP")) ;
        }
        return resp ;
    }
    
    public String getDescription() {
        return "*.GTP" ;
    }
    
}


