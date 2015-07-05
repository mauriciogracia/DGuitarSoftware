/*
 * Created on 3/04/2005
 *
 */
package dguitar.gui;

import java.awt.Component;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import dguitar.gui.midi.MidiInternalFrame;

/**
 * @author Mauricio Gracia Gutiérrez
 *
 */
public class SongAction { 
    /**
     * This method performs the given action over a JInternalFrame.
     * 
     * @param JIF		a JInternFrame (or a subclass) 
     * @param SAP	 	The song action you want to perform , refresh the language,close,update display options
     */
    public static void perform(JInternalFrame JIF,SongActionParameters SAP) {
        SongInternalFrame SIF ;
        MidiInternalFrame MIF ;
        Class<? extends JInternalFrame> c ;
        DisplayOptions DO ;
        
        switch(SAP.action) {

        //Applies ONLY to SongInternal frames
        case SongActionParameters.REFRESH_DISPLAY_OPTIONS :
            c = JIF.getClass() ;
            SIF = new SongInternalFrame() ;
            if(c.isInstance(SIF)) {
                SIF = (SongInternalFrame) JIF ;
                DO = SAP.doReference.getDisplayOptions() ;
                SIF.setDisplayOptions(DO) ;
                SIF.validate() ;
            }
            else  {
                //TODO do nothing or display an error ?
            }
            break ;
        //Applies to all internal frames
        case SongActionParameters.REFRESH_LANG :
            c = JIF.getClass() ;
            SIF = new SongInternalFrame() ;
            MIF = new MidiInternalFrame() ;
            if(c.isInstance(SIF)) {
                SIF = (SongInternalFrame) JIF ;
                SIF.setLangText() ;
            }
            else if(c.isInstance(MIF)) {
                MIF = (MidiInternalFrame) JIF ;
                MIF.setLangText() ;
            }
            break ;
        //Applies to all internal frames
        case SongActionParameters.CLOSE_DISPOSE :
            try {
                JIF.setClosed(true);
                JIF.dispose();
            } catch (java.beans.PropertyVetoException PVE) {
                PVE.printStackTrace() ;
            }
            break ;

            
        }

    }
    /**
     * This method ITERATES over the internal Frames of a desktopPane and performs 
     * the given action.
     * 
     * @param desktopPane	A desktop pane that contains InternFrame (and/or subclasses) 
     * @param SAP			The Song action you want to perform , refresh the language,close,update display options
     */

    public static void perform(JDesktopPane desktopPane,SongActionParameters SAP) {
        Component comp[];
        SongInternalFrame SIF;
        JInternalFrame JIF ;
        int i;
        Class<? extends Component> c;
        JInternalFrame.JDesktopIcon JDI ;
        
        comp = desktopPane.getComponents();
        i = 0;
        //	  Iterate over the Desktop Pane objects
        SIF = new SongInternalFrame();
        for (i = 0; i < comp.length; i++) {
            c = comp[i].getClass();
            JIF = new JInternalFrame() ;
            JDI = new JInternalFrame.JDesktopIcon(JIF) ;
            
            if (c.isInstance(JDI)) {
                JDI = (JInternalFrame.JDesktopIcon) comp[i];
                JIF = JDI.getInternalFrame();
            }
            else {
                //assuming all objects on Desktop are INTERNAL FRAMES
                JIF = (JInternalFrame) comp[i];
            }
            
            if(SAP.allWindows){
                //PERFORM THE GIVEN ACTION on allWindows (no cast check)
                perform(JIF,SAP) ;
            }
            else if( c.isInstance(SIF) ) {
                SIF = (SongInternalFrame) JIF;
                perform(SIF,SAP) ;
            }
        }
    }
}
