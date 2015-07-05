/*
 * Created on 23/04/2005
 *
 */
package dguitar.gui;

/**
 * This object stores the parameters used by the SongAction class
 * 
 * @author Mauricio Gracia G
 
 */
public class SongActionParameters {
    public int 			action ;
    public boolean 		allWindows ;
    public OptionsDisplay	doReference ;
    
    /**Refresh the language on the open windows*/
    public static final int REFRESH_LANG = 1 ;
    
    /**Close and Dispose all the open windows*/
    public static final int CLOSE_DISPOSE = 2 ;
    
    /**Refresh/Apply the current Display Options to all open windows*/ 
    public static final int REFRESH_DISPLAY_OPTIONS = 3 ;
    
    public SongActionParameters(int anAction) {
        this.action = anAction ;
        this.allWindows = true ;
        this.doReference = null ;
    }
    public SongActionParameters(int anAction, boolean onAllWindows) {
        this.action = anAction ;
        this.allWindows = onAllWindows ;
        this.doReference = null ;
    }
    public SongActionParameters(int anAction, boolean onAllWindows, OptionsDisplay reference) {
        this.action = anAction ;
        this.allWindows = onAllWindows ;
        this.doReference = reference ;
    }

}
