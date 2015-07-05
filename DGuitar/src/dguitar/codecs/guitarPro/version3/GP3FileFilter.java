package dguitar.codecs.guitarPro.version3;

import java.io.File;

import javax.swing.filechooser.FileFilter;
/**
 * This is a Filter for the GP3 file extension
 */

public class GP3FileFilter extends FileFilter  implements java.io.FileFilter {

    
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
          resp = (name.toUpperCase().endsWith(".GP3")) ;
        }
        return resp ;
    }
    
    public String getDescription() {
        return "*.GP3" ;
    }
    
}


