/*
 * Created on 2/04/2005
 *
 */
package dguitar.gui;

import javax.swing.JFrame;
import javax.swing.JMenu;

import dguitar.gui.listeners.MenuIndexedListener;

/**
 * This class applies the selected options for a Single Piece or all the Pieces
 * 
 * @author Mauricio Gracia Gutierrez
 *  
 */
public class SongOptionsListener 
implements MenuIndexedListener {
    private DisplayOptions displayOptions;

    private JFrame jFrame ;
    
    private OptionsDisplay reference ;
    
    
    //public SongOptionsListener(JFrame aFrame, DisplayOptions aDisplayOptions) {
    public SongOptionsListener(JFrame aFrame, OptionsDisplay aReference) {
        this.jFrame = aFrame ;
        
        this.reference = aReference ;
        this.displayOptions = this.reference.getDisplayOptions() ;
        
    }


    /*
     * (non-Javadoc)
     * 
     * @see dguitar.gui.MenuIndexedListener#menuClicked(javax.swing.JMenu, int)
     */
    public void menuClicked(JMenu aMenu, int idMenu) {
        boolean refresh ;
        /*
         * SongOptionsMenu SOM ; SOM = new SongOptionsMenu() ;
         * if(this.menu.getClass().isInstance(SOM)) { SOM =
         * (SongOptionsMenu)this.menu ; } DO = SOM.getDisplayOptions() ;
         */

        //DEBUG
        System.err.println("MENU CLICKED" + idMenu) ;
        
        refresh = true ;
        switch (idMenu) {
        case SongOptionsMenu.COLOR_OPTIONS:
            refresh = this.viewDisplayColorDialog() ;
            break;
        case SongOptionsMenu.MIN_PIECE:
            this.displayOptions.toggleMinPieceSpacing() ;
            //TODO this.PP.validate();
            break;
        case SongOptionsMenu.MULTI_TRACK:
            this.displayOptions.toggleMultiTrackView();
        	//DEBUG
        	System.err.println("MULTI_TRACK toggled") ;
            //TODO this.PP.displayPiece();
            //TODO this.PP.validate();
            //TODO DGuitar.setOptSizeChildWindow(this);
            break;
        case SongOptionsMenu.MUSIC_CURSORS:
            this.displayOptions.toggleViewMusicCursors();
            break;
        case SongOptionsMenu.SINGLE_MUSIC_CURSORS_COLOR:
            this.displayOptions.toggleSingleMusicCursorColor();
            break;
        //this will eventually disapper
        case SongOptionsMenu.UNSUPPORTED_EFFECTS:
            this.displayOptions.toggleDisplayUnsupportedEffects();
        	break;
        }
        //the DisplayOptions need to be applied to the Reference object
        if(refresh) {
            this.reference.setDisplayOptions(this.displayOptions) ;
        }
    }

    private boolean viewDisplayColorDialog() {
        ColorDialog CP;
        boolean changed;

        CP = new ColorDialog(this.jFrame);
        CP.fretColors.setColors(displayOptions.fretColors,
                displayOptions.coloringForFrets);
        CP.rhythmColors.setColors(displayOptions.rhythmColors,
                displayOptions.coloringForRhythm);
        CP.setVisible(true);
        changed = false;
        if (CP.OKclicked()) {
            
            if (CP.fretColors.selectionChanged()) {
                displayOptions.fretColors = CP.fretColors.getColors();
                displayOptions.coloringForFrets = CP.fretColors
                        .getTypeOfColoring();
                changed = true;
            }
            if (CP.rhythmColors.selectionChanged()) {
                displayOptions.rhythmColors = CP.rhythmColors.getColors();
                displayOptions.coloringForRhythm = CP.rhythmColors
                        .getTypeOfColoring();
                changed = true;
            }
        }
        return changed ;
    }
    
}