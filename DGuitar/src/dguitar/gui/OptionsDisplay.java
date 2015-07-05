/*
 * Created on 23/04/2005
 *
 */
package dguitar.gui;

/**
 * Any object that wants to allow its DisplayOptions to be changed/read 
 * should implement this interface
 * 
 * @author Mauricio Gracia G
 *
 */
public interface OptionsDisplay {
    /*Propagates the passed aDisplayOptions to this all its components*/
    public void setDisplayOptions(DisplayOptions aDisplayOptions) ;
    
    /*Gets the current DisplayOptions for this component*/
    public DisplayOptions getDisplayOptions() ;
}
