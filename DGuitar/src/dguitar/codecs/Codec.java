/*
 * Created on March 13, 2005
 */
package dguitar.codecs;


/**
 * Codec stands for COder/DECoder that reads/writes a specific format or 
 * sequence of bytes from/to a InputStream/OutputStream
 * 
 * This class was created so the javadoc could process the package.html file
 * 
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public interface Codec
{

    /*when will this method be used ??
    protected CodecFileFilter codecFileFilter ;
    
    
    public final boolean setCodecFileFilter(CodecFileFilter aCodecFileFilter) {
        boolean success ;
        
        success = false ;
        if(aCodecFileFilter != null) {
            this.codecFileFilter = aCodecFileFilter ;
        }
        
        return success ;
    }
    */
    /**
     * Returns if the extension is supported by this codec
     * 
     * @param extension ".GP3" or ".TAB" or something else
     * @return true if this codec know how to hadle that extension
     */
    public boolean supportedExtension(String extension) ;
}
