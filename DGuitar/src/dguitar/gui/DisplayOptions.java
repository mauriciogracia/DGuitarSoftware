/*
 * Created on 03-mar-2005, EVERY TIME A NEW FIELD IS ADDED...CLONE METHOD NEEDS TO BE MODIFIED
 *
 */
package dguitar.gui;

import java.awt.Color;

/**
 * This class stores the DisplayOptions related to a Song and the whole program too.
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public class DisplayOptions implements java.lang.Cloneable{
    /*when there is a unique color*/
    public static final int RELATED_TO_NOTHING = 0 ;
    
    /*are the color of the rhythm/notes related to the duration */
    public static final int RELATED_TO_DURATION = 1 ;
    
    /*are the color of the rhythm/notes related to the dyanmic */
    public static final int RELATED_TO_DYNAMIC = 2 ;
    
    /** The spacing between horizontal lines in pixels (LineSpacing)**/
    public int LS ;
    
    /**The horinzal spacing between notes in pixels */
    public int NS ;
    
    /**how far below are the lines draw*/
    public int TopOffset ;
    
    /** how much space below is preserved*/
    protected int BottomOffset ;
    
    /**are lines are extend to the top*/
    protected boolean extendTop  ;
    
    /**are the lines extend to the bottom*/
    protected boolean extendBottom ;
    
    /*
    protected Color background ;
     
     
    protected Color foreground ;
     */
    /**The colors that are used for vertical rhythm lines*/
    public ColorScheme rhythmColors = new ColorScheme();
    
    /**The colors that are used for vertical rhythm lines*/
    public ColorScheme fretColors = new ColorScheme();
    
    /**type of coloring for the Rhythm lines*/
    public int coloringForRhythm ;
    
    /**type of coloring for the Frets*/
    public int coloringForFrets ;
    
    /**display the music cursor ?*/
    public boolean displayMusicCursor ;
    
    /**display a question mark when the a effect is present but is not painted yet*/
    public boolean displayUnsupportedEffects ; 
    
    /**is the proportional spacing linked to the piece**/
    public boolean minPieceSpacing ;
    
    /**is the preferred view MultiTrack ?*/
    public boolean multiTrackView ;

    /**if true the musicCursor are all in the same color
     * otherwise each MusicCursor is painted with the Track color
     */
    public boolean singleMusicCursorColor ;
    
    public Color musicCursorColor ;
    
    public DisplayOptions() {
        this.LS = 10 ; //10 is GP default value
        this.NS = 20 ; //between 12 and 30 looks nice
        
        this.TopOffset = 0 ;
        this.BottomOffset = 0 ;
        this.extendBottom = false ;
        this.extendTop = false ;
        
        this.rhythmColors = new ColorScheme() ;
        this.fretColors = new ColorScheme() ;
        
        this.coloringForRhythm = DisplayOptions.RELATED_TO_DURATION ;
        this.coloringForFrets = DisplayOptions.RELATED_TO_DURATION ;
        this.displayMusicCursor = true ;
        this.displayUnsupportedEffects = true ;
        
        this.minPieceSpacing = false ;
        this.multiTrackView = true ;
        
        this.singleMusicCursorColor = false ;
        this.musicCursorColor = Color.DARK_GRAY ;
        
    }
    protected Object clone() {
        DisplayOptions retValue;

        retValue = new DisplayOptions();

        retValue.LS = this.LS ;
        retValue.NS = this.NS ;         
        retValue.TopOffset = this.TopOffset ;
        
        retValue.BottomOffset = this.BottomOffset = 0 ;
        retValue.extendBottom = this.extendBottom = false ;
        retValue.extendTop = this.extendTop = false ;
        
        retValue.rhythmColors = (ColorScheme) this.rhythmColors.clone()  ;
        retValue.fretColors = (ColorScheme) this.fretColors.clone()  ;
        
        retValue.coloringForRhythm = this.coloringForRhythm ;
        retValue.coloringForRhythm = this.coloringForFrets  ;
        retValue.displayMusicCursor = this.displayMusicCursor ;
        retValue.displayUnsupportedEffects = this.displayUnsupportedEffects ;
        
        retValue.minPieceSpacing = this.minPieceSpacing ;
        retValue.multiTrackView = this.multiTrackView  ;
        
        retValue.singleMusicCursorColor = this.singleMusicCursorColor  ;
        retValue.musicCursorColor = this.musicCursorColor ;
        
        return retValue;
    }
    public void toggleMinPieceSpacing() {
        this.minPieceSpacing = !this.minPieceSpacing ;
    }
    /**
    * remember to call validate when necesary.
    */
    public void toggleDisplayUnsupportedEffects() {
        this.displayUnsupportedEffects = !this.displayUnsupportedEffects;

    }
    public void toggleSingleMusicCursorColor() {
        this.singleMusicCursorColor = !this.singleMusicCursorColor;
    }
    public void toggleViewMusicCursors() {
        this.displayMusicCursor = !this.displayMusicCursor;
    }
    public void toggleMultiTrackView() {
        this.multiTrackView = !this.multiTrackView;
    }

}
