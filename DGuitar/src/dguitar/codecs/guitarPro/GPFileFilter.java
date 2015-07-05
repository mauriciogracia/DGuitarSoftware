package dguitar.codecs.guitarPro;

import java.util.Vector;

import dguitar.codecs.CodecFileFilter;
import dguitar.codecs.guitarPro.version2.GP2FileFilter;
import dguitar.codecs.guitarPro.version3.GP3FileFilter;
import dguitar.codecs.guitarPro.version4.GP4FileFilter;

//OLD public class GPFileFilter extends javax.swing.filechooser.FileFilter
//implements java.io.FileFilter, FilenameFilter {
/**
 * This class is a Filter for the Guitar Pro file extensions.
 * 
 * @author Mauricio Gracia Gutierrez
 */
public class GPFileFilter extends CodecFileFilter {

    /**
     * Check if the file filters for GP3 and GP4 accept this
     * filename.
     * @param s the filename
     * @return true if the filename can be accepted.
     */
    public boolean accept(String s) {
        return (GP4FileFilter.accept(s) || GP3FileFilter.accept(s) || GP2FileFilter.accept(s));
    }

    /**
     * Returns the description for this filter.
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public String getDescription() {
        return "*.GP4, *.GP3, *.GTP";
    }

    /* (non-Javadoc)
     * @see dguitar.codecs.CodecFileFilter#getExtensions()
     */
    public Vector<String> getExtensions() {
        String []res = { ".GP4", ".GP3", ".GTP"} ;
        Vector<String> vector ;
        int i ;
        
        vector = new Vector<String>() ;
        
        for(i = 0; i < res.length ; i++) {
            vector.add(res[i]) ;
        }
        return (vector);
    }

}
