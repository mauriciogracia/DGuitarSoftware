/*
 * Created on 17/03/2005
 *
 */
package dguitar.codecs;

import java.io.File;
import java.util.Vector;

/**
 * @author Mauricio Gracia Gutiérrez
 *  
 */
public abstract class CodecFileFilter extends
        javax.swing.filechooser.FileFilter implements java.io.FileFilter,
        java.io.FilenameFilter {

    /**
     * This method returns the extension the codec handles Example:
     * {".GP3",".GP4",".GTP"} if it is a Guitar Pro codec)
     * 
     * @return and Vector of String with the extensions the codec supports
     */
    public abstract Vector<String> getExtensions();

    /**
     * Override this method actually accept the Name of the file or not
     */

    public abstract boolean accept(String s);

    /**
     * this method is equivalent to <code>f.isDirectory() || accept(f.getName())</code>
     * 
     * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
     */
    public final boolean accept(File f) {
        boolean ok;

        ok = false;
        if (f != null) {
            ok = f.isDirectory() || accept(f.getName());
        }
        return ok;
    }

    /**
     * This methos returns <code>this.accept(name)</code>
     * 
     * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
     */
    public final boolean accept(File dir, String name) {
        return accept(name);
    }

    /**
     * Returns a description of this CodeFilter
     * 
     * @see javax.swing.filechooser.FileFilter#getDescription()
     */
    public abstract String getDescription();

}