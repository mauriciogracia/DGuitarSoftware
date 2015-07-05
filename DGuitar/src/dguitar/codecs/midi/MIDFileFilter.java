package dguitar.codecs.midi;

import java.util.Vector;

import dguitar.codecs.CodecFileFilter;
/**
 * This is a Filter for the *.MID (midi) file extension
 * 
 * @author Mauricio Gracia Gutierrez
 */

//OLDpublic class MIDFileFilter extends FileFilter implements java.io.FileFilter{
    
public class MIDFileFilter extends CodecFileFilter {
    public String getDescription() {
        return "*.MID files" ;
    }


    /* (non-Javadoc)
     * @see dguitar.codecs.CodecFileFilter#getExtensions()
     */
    public Vector<String> getExtensions() {
        String []res = { ".MID"} ;
        Vector<String> vector ;
        int i ;
        
        vector = new Vector<String>() ;
        
        for(i = 0; i < res.length ; i++) {
            vector.add(res[i]) ;
        }
        return (vector);
    }


    /* (non-Javadoc)
     * @see dguitar.codecs.CodecFileFilter#accept(java.lang.String)
     */
    public boolean accept(String s) {
        boolean resp ;
        
        resp = false ;
        if(s != null) {
            resp = s.toUpperCase().endsWith(".MID") ; 
        }
        return (resp) ;
    }
    
}


