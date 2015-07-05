/*
 * SongPanel.java
 *
 * Created on 14 de febrero de 2005, 10:20 AM
 */

package dguitar.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
//NOT NEEDED import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JViewport;

import dguitar.codecs.guitarPro.GPDuration;
import dguitar.codecs.guitarPro.GPFormatException;
import dguitar.codecs.guitarPro.GPInputStream;
import dguitar.codecs.guitarPro.GPMeasure;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;
import dguitar.codecs.guitarPro.GPSong;
import dguitar.codecs.guitarPro.GPTrack;

/**
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public class SongPanel extends JPanel 
implements OptionsDisplay {
    /**
	 * 
	 */
	private static final long serialVersionUID = 196365926067710437L;

	private GridBagLayout gridBagLayout;

    private dguitar.codecs.guitarPro.GPSong piece;

    private List<GPMeasure> measures;

    private List<?> tracks;

    private int numMeasures;

    private int numTracks;

    private JTabbedPane TabPane;

    private JPanel Panel;

    /*
     * To iterate over the TrackPanel collection
     */
    private int count;

    //TODO...implement this
//  UNUSED    private boolean isHorizontalScroll; //if false then is vertical

    //have Tracks been added
    private boolean hasTracks;

    //has the View change
    private boolean viewChange;

    //has the spacing change
    private boolean spacingChange;

    private DisplayOptions displayOptions;

    private MusicCursorPosition musicCursorPosition;

    /**
     * How many lines are added to the top
     */
    private int linesOnTop = 2; 

    /**
     * How many lines are added to the bottom
     */
    private int linesOnBottom = 3;
    
    /**
     * Creates a new instance of SongPanel
     */
    public SongPanel() {
        super();
        this.displayOptions = new DisplayOptions();
        
        initComponents();

    }
    /**
     * To create a SongPanel with displayOptions already created.
     * @param aDisplayOptions
     */
    public SongPanel(DisplayOptions aDisplayOptions) {
        super();
        if(aDisplayOptions != null) {
            initComponents();
            this.setDisplayOptions(aDisplayOptions) ;
    	}
        else {
            throw new NullPointerException("aDisplayOptions is null") ;
        }
    }

    /**
     * @param dO The displayOptions to set.
     */
    public void setDisplayOptions(DisplayOptions dO) {
        
        this.displayOptions = dO;
        
        //The TopOffset is increased in linesOnTop lines
        this.displayOptions.TopOffset += linesOnTop * this.displayOptions.LS;

        //The BottomOffset is increased in linesOnBottom lines
        this.displayOptions.BottomOffset += linesOnBottom * this.displayOptions.LS;

        if (this.displayOptions.multiTrackView) {
            addScrollPane();
        } else {
            addTabPane();
        }

    }
    /**
     * @return Returns the dO.
     */
    public DisplayOptions getDisplayOptions() {
        return displayOptions;
    }
    private void initComponents() {
        this.setOpaque(true) ;
        this.setBackground(Color.WHITE) ;
        this.numMeasures = 0;
        this.numTracks = 0;
        this.setLayout(new BorderLayout());
        this.hasTracks = false;
        
//      if false then is vertical, not IMPLEMENTED yet
//      UNUSED        this.isHorizontalScroll = true;
        
        this.measures = null;
        this.viewChange = false;
        this.spacingChange = true;

        /*
        //POST INIT
        this.musicCursorPosition = new MusicCursorPosition();

        //The TopOffset is the BP offset plus the minimum, repeat, measure
        // numbers
        this.displayOptions.TopOffset += 3 * this.displayOptions.LS;

        //The BottomOffset is the BP offset plus the minimum, lyrics, rhytm
        // lines
        this.displayOptions.BottomOffset += 3 * this.displayOptions.LS;

        if (this.displayOptions.multiTrackView) {
            addScrollPane();
        } else {
            addTabPane();
        }
        */
    }

    /*
     * This is the usual iteration over the TrackPanel TrackPanel TP ;
     * 
     * TP = getFirstTrackPanel() ; while(TP != null) { TP = getNextTrackPanel() ; }
     */

    private void addScrollPane() {
        JScrollPane JSP;

        this.gridBagLayout = new GridBagLayout();
        this.Panel = new JPanel(this.gridBagLayout);
        
        //new..this is to make the panel white an opaque
        this.Panel.setOpaque(true);
        this.Panel.setBackground(Color.WHITE) ;
        
        JSP = new JScrollPane(Panel);
        this.add(JSP);
    }

    private void addTabPane() {
        this.TabPane = new JTabbedPane();
        this.add(this.TabPane);
    }

    private void removeScrollPane() {
        this.Panel.removeAll();
        this.remove(0);
    }

    private void removeTabPane() {
        this.TabPane.removeAll();
        this.remove(0);
    }

    public void setMinPieceSpacing(boolean b) {
        if (b != this.displayOptions.minPieceSpacing) {
            this.displayOptions.minPieceSpacing = b;
            this.spacingChange = true;
        }

    }
    /**
     * Call this to swith back and forward between the two types of spacing
     * 
     * remember to call validate when necesary.
     *
     */
    public void toggleMinPieceSpacing() {
        this.displayOptions.toggleMinPieceSpacing() ;
        this.spacingChange = true;
    }
    /**
    * remember to call validate when necesary.
    */
    public void toggleDisplayUnsupportedEffects() {
        this.displayOptions.toggleDisplayUnsupportedEffects() ;

    }
    public void toggleSingleMusicCursorColor() {
        this.displayOptions.toggleSingleMusicCursorColor() ;
        this.setMusicCursorSingleColor(this.displayOptions.singleMusicCursorColor);
    }
    public void toggleViewMusicCursors() {
        this.displayOptions.toggleViewMusicCursors();
        this.musicCursorsSetEnable(this.displayOptions.displayMusicCursor);
        this.musicCursorsSetVisible(this.displayOptions.displayMusicCursor);
    }
    public boolean getMinPieceSpacing() {
        return this.displayOptions.minPieceSpacing;
    }

    /**
     * Gets the Piece object that was read using readPieceFrom(GPInputStream
     * GIS)
     */
    public GPSong getPiece() {
        return this.piece;
    }

    public void changeView() {
        if (this.viewChange) {
            if (this.displayOptions.multiTrackView) {
                removeTabPane();
                addScrollPane();
            } else {
                removeScrollPane();
                addTabPane();
            }
            this.hasTracks = false;
            this.spacingChange = true;
        }
    }

    /**
     * this method sets the MeasuresTracksPairs, and the then calls refresh() ;
     */
    public void displayPiece() {

        if (this.hasTracks) {
            changeView();
        }
        if (!this.hasTracks) {
            this.addTrackMeasures();
            this.hasTracks = true;
        }
        this.setMeasuresTracksPairs();
        this.alignBars();

    }

    //TODO it seems like the cursors are being aligned..but ther are not
    // SHOWING
    public void musicCursorsAlign() {
        TrackPanel TP;

        TP = this.getFirstTrackPanel();
        while (TP != null) {
            //set the cursor without scrolling
            TP.setMusicCursor(1, 1, false);
            TP = this.getNextTrackPanel();
        }
    }

    public void musicCursorsSetVisible(boolean value) {
        TrackPanel TP;

        TP = this.getFirstTrackPanel();
        while (TP != null) {
            TP.setMusicCursorsVisible(value);
            TP = this.getNextTrackPanel();
        }
    }

    public void musicCursorsSetEnable(boolean value) {
        TrackPanel TP;

        TP = this.getFirstTrackPanel();
        while (TP != null) {
            TP.setMusicCursorsProcesingEvents(value);
            TP = this.getNextTrackPanel();
        }
    }
    /**
     * Call this method when you change variable this.DO.multiTrackView
     *
     */
    private void multiTrackViewChanged() {
        this.viewChange = true;
        changeView();
        
    }

    public void setMultiTrackView(boolean b) {
        if (b != this.displayOptions.multiTrackView) {
            this.displayOptions.multiTrackView = b;
            this.multiTrackViewChanged() ; 
        }
    }
    public void toggleMultiTrackView() {
        this.displayOptions.toggleMultiTrackView() ;
        this.multiTrackViewChanged() ;
    }

    public boolean isMultiTrackView() {
        return this.displayOptions.multiTrackView;
    }

    //measures already contains the measure information (how many measures,
    // what kind, etc
    /**
     * Adds a tracks...VERY IMPORTANT validate() method should be invoqued as
     * required
     */
    private void addTrackPanel(GPTrack track) {
        TrackPanel TP;
        GridBagConstraints gridBagConstraints;

        TP = new TrackPanel(this.displayOptions, this.musicCursorPosition);
        if (measures != null) {
		    //Set GPtrack information to the trackPanel
		    TP.setTrack(track);

		    //add as many bars as the numer of the measures
		    TP.addBar(measures.size());

		    //set information of every measure
		    TP.setMeasures(measures);

		} else {
		    //Add a default number of bars ??
		    
		}
		if (this.displayOptions.multiTrackView) {
		    //this.gridBagLayout.setRows(this.gridBagLayout.getRows() +1 )
		    // ;
		    gridBagConstraints = new java.awt.GridBagConstraints();
		    gridBagConstraints.gridx = 0;
		    gridBagConstraints.gridy = this.Panel.getComponentCount();
		    //gridBagConstraints.gridwidth = 8;
		    //gridBagConstraints.fill =
		    // java.awt.GridBagConstraints.HORIZONTAL;
		    //gridBagConstraints.weightx = 8.0;
		    //gridBagConstraints.weighty = 1.0;

		    this.Panel.add(TP, gridBagConstraints);
		} else {
		    JScrollPane JSP;

		    JSP = new JScrollPane(TP);
		    this.TabPane.add(track.getName(), JSP);
		}

    }

    /**
     * This method allows you to obtain the TrackPanel at pos position, starting
     * from 0
     * 
     * @param pos
     *            the trackPanel index starting from 0
     * @return a TrackPanel located at that position
     */
    public TrackPanel getTrackPanel(int pos) {
        TrackPanel TP;

        TP = null;
        if (pos < numTracks) {
            if (this.displayOptions.multiTrackView) {
                //We know that the components are TrackPanels...we put them ;
                TP = (TrackPanel) this.Panel.getComponent(pos);
            } else {
                JScrollPane JSP;
                JViewport JVP;

                JSP = (JScrollPane) this.TabPane.getComponent(pos);
                JVP = JSP.getViewport();
                TP = (TrackPanel) JVP.getView();
            }
        }
        return TP;
    }

    /**
     * a method to start to Iterate over the Track Panels is provided
     */

    protected TrackPanel getFirstTrackPanel() {
        TrackPanel TP;

        count = 0;
        TP = null;

        try {
            if (this.displayOptions.multiTrackView) {
                //We know that the components are TrackPanels...we added them ;
                TP = (TrackPanel) this.Panel.getComponent(count);
            } else {
                JScrollPane JSP;
                JViewport JVP;

                JSP = (JScrollPane) this.TabPane.getComponent(count);
                JVP = JSP.getViewport();
                TP = (TrackPanel) JVP.getView();
            }
        } catch (java.lang.IndexOutOfBoundsException IOOBE) {
            /*
             * When the getComponent fails..catch this exception and let TP stay
             * null
             */
        }

        return TP;
    }

    /*
     * a method to continue to Iteratating over the Track Panels is provided
     */

    protected TrackPanel getNextTrackPanel() {
        TrackPanel TP;

        count++;
        TP = null;
        if (count < numTracks) {
            if (this.displayOptions.multiTrackView) {
                //We know that the components are TrackPanels...we put them ;
                TP = (TrackPanel) this.Panel.getComponent(count);
            } else {
                JScrollPane JSP;
                JViewport JVP;

                JSP = (JScrollPane) this.TabPane.getComponent(count);
                JVP = JSP.getViewport();
                TP = (TrackPanel) JVP.getView();
            }
        }
        return TP;
    }

    /**
     * calculate the min duration of the beat of measure ´m´ it is assumed that
     * m < numMeasures
     */
    private GPDuration minDurationOfMeasure(int m) {
        TrackPanel TP;
        BarMTP BMTP;
        GPDuration minDuration;
        GPDuration duration;

        minDuration = GPDuration.WHOLE;
        TP = getFirstTrackPanel();
        while (TP != null) {
            BMTP = TP.getTablatureBar(m);
            duration = BMTP.minDurationOfBeats();

            //The higher the type of duration..the shortes the note is
            if (duration.compareTo(minDuration) < 0) {
                minDuration = duration;
            }
            TP = getNextTrackPanel();
        }
        return minDuration;
    }

    /**
     * this method propagates the minDuration vertically..across the tracks m is
     * the measure number
     */
    private void setMinDuration(GPDuration minDuration, int m) {
        TrackPanel TP;
        BarMTP BMTP;
        TP = getFirstTrackPanel();
        while (TP != null) {
            BMTP = TP.getTablatureBar(m);
            BMTP.setMinDuration(minDuration);
            TP = getNextTrackPanel();
        }
    }

    private void setMinWidth(int min, int m) {
        TrackPanel TP;
        BarMTP BMTP;
        TP = getFirstTrackPanel();
        
        while (TP != null) {
            BMTP = TP.getTablatureBar(m);
            BMTP.setMinWidth(min);
            BMTP.invalidate();
            TP = getNextTrackPanel();
        }
    }

    private int getMaxWidth(int m) {
        TrackPanel TP;
        Bar bar;
        int max;

        max = 0;
        TP = getFirstTrackPanel();
        while (TP != null) {
            bar = TP.getTablatureBar(m);
            max = Math.max(max, bar.getWidth());
            TP = getNextTrackPanel();
        }
        return max;
    }

    //this method asumes that displayNotes has been called
    protected void alignBars() {
        int m;
        GPDuration minDuration;
        GPDuration minPieceDuration;

        if (this.spacingChange) {
            minPieceDuration = GPDuration.WHOLE;
            /* Proportional spacing */
            for (m = 0; m < numMeasures; m++) {
                minDuration = minDurationOfMeasure(m);

                if (this.displayOptions.minPieceSpacing) {
                    if (minDuration.compareTo(minPieceDuration) < 0) {
                        minPieceDuration = minDuration;
                    }
                } else {
                    setMinDuration(minDuration, m);
                }
            }
            if (this.displayOptions.minPieceSpacing) {
                //                DGuitar.LS.log("minPieceDuration " + minPieceDuration) ;
                for (m = 0; m < numMeasures; m++) {
                    setMinDuration(minPieceDuration, m);
                }
            }
            for (m = 0; m < numMeasures; m++) {
                setMinWidth(getMaxWidth(m), m);
            }
            this.spacingChange = false;
        }

    }

    //Is asumed that Piece is not null
    private void setMeasuresTracksPairs() {
        int m;
        int i;
        List<GPMeasureTrackPair> MTPs;
        TrackPanel TP;

        MTPs = piece.getMeasuresTracksPairs();
        if (MTPs != null) {
            i = 0;

            for (m = 0; m < numMeasures; m++) {
                TP = getFirstTrackPanel();

                while (TP != null) {
                    TP.setMeasureTrackPair(m, (GPMeasureTrackPair) MTPs.get(i));
                    TP = getNextTrackPanel();
                    i++;
                }
            }
        }
    }

    //Is asumed that Piece is not null
    /**
     * add The different track and measures of a song, but they are empty.
     */
    private void addTrackMeasures() {
        int i;

        if (measures == null) {
            measures = this.piece.getMeasures();
        }
        if (measures != null) {
            numMeasures = measures.size();
            if (numMeasures > 0) {
                tracks = this.piece.getTracks();
                if (tracks != null) {
                    numTracks = tracks.size();
                    if (numTracks > 0) {
                        for (i = 0; i < numTracks; i++) {
                            this.addTrackPanel((GPTrack) tracks.get(i));
                        }
                    }
                }
            }
        }
    }

    public boolean hasChanged() {
        boolean changed;
        TrackPanel TP;

        //Check if any of the DGuitarPanels has changed
        changed = false;

        TP = getFirstTrackPanel();
        while ((!changed) && (TP != null)) {
            changed = TP.hasChanged();
            TP = getNextTrackPanel();
        }

        return changed;
    }

    /**
     * This method reads from a GPInputStream, and displays any exceptions
     */
    public boolean readPieceFrom(GPInputStream GPis) {
        boolean success;

        success = false;
        if (GPis != null) {
            try {
                //WORKDS this.piece = GPis.readPiece();
                this.piece = (GPSong) GPis.readObject();
                success = true;
            } catch (GPFormatException GFE) {
                common.Util.showDialogOk(this,
                        "File contains this format Exception ", GFE.toString());
            } catch (IOException IOE) {
                IOE.printStackTrace();
            }
        }
        return success;
    }
/*does not seem to be needed
    public Dimension getPreferredSize() {
        int w;
        int h;
        TrackPanel TP;
        Dimension dim;

        h = 0;
        w = 0;
        TP = getFirstTrackPanel();
        while (TP != null) {
            dim = TP.getMinimumSize();
            if (this.DO.multiTrackView) {
                h += dim.height;
            } else {
                h = Math.max(h, dim.height);
            }
            w = Math.max(w, dim.width);
            TP = getNextTrackPanel();
        }
        //if each track goes on a separate tab
        if (!this.DO.multiTrackView) {
            //the tab names have a height
            h += 20;
        }
        //to add some space to the right
        w += 10;
        return (new Dimension(w, h));
    }
*/
    /*
     * if the minimum size has been set to a non- <code>null</code> value just
     * returns it. if the UI delegate's <code>getMinimumSize</code> method
     * returns a non- <code>null</code> value then return that; otherwise
     * defer to the component's layout manager.
     * 
     * @return the value of the <code>minimumSize</code> property
     * @see #setMinimumSize
     *  
     */
    /*does not seem to be needed
    public Dimension getMinimumSize() {
        return new Dimension(100, 100);
    }
*/
    public int getNoteSpacing() {
        TrackPanel TP;
        int ns;

        ns = -1;

        TP = getFirstTrackPanel();
        if (TP != null) {
            ns = TP.getNoteSpacing();
        }
        return ns;
    }

    public void setNoteSpacing(int ns) {
        TrackPanel TP;

        if (ns > 0) {

            TP = getFirstTrackPanel();
            while (TP != null) {
                TP.setNoteSpacing(ns);
                TP = getNextTrackPanel();
            }
        }
    }

    public void setMusicCursorSingleColor(boolean single) {
        TrackPanel TP;

        TP = getFirstTrackPanel();
        while (TP != null) {
            TP.setMusicCursorSingleColor(single);
            TP = getNextTrackPanel();
        }
    }

    /**
     * 
     * Validates this container and all of its subcomponents.
     * <p>
     * The <code>validate</code> method is used to cause a container to lay
     * out its subcomponents again. It should be invoked when this container's
     * subcomponents are modified (added to or removed from the container, or
     * layout-related information changed) after the container has been
     * displayed.
     * 
     * @see #add(java.awt.Component)
     * @see Component#invalidate
     * @see javax.swing.JComponent#revalidate()
     */

    public void validate() {
        this.alignBars();
        TrackPanel TP;

        TP = getFirstTrackPanel();
        while (TP != null) {
            TP.validate();
            TP = getNextTrackPanel();
        }

        super.validate();
    }

    /**
     * NOT USED...and not working
     * 
     * Scrolls the other TrackPanel according to TP
     */
    protected void scrollOthers(TrackPanel tp) {
        TrackPanel TP;
        int i;
        JScrollPane JSP;
        JViewport JVPRef;
        JViewport JVPAux;
        JViewport JVP;

        //CURRENT WORK.
        //DGuitar.LS.log("scrollOthers: isMultiTrackView ? " +
        // this.isMultiTrackView) ;
        if (!this.displayOptions.multiTrackView) {
            i = 0;
            do {
                JSP = (JScrollPane) this.TabPane.getComponent(i);

                JVPRef = JSP.getViewport();
                TP = (TrackPanel) JVPRef.getView();
                i++;
            } while ((i < this.numTracks) && (!TP.equals(tp)));

            i = 0;
            do {
                JSP = (JScrollPane) this.TabPane.getComponent(i);
                JVP = JSP.getViewport();
                TP = (TrackPanel) JVP.getView();
                if (!TP.equals(tp)) {
                    JVPAux = new JViewport();
                    JVPAux.setView(TP);
                    JVPAux.setViewPosition(JVPRef.getViewPosition());
                    JSP.setViewport(JVPAux);

                    //DGuitar.LS.log("setting view position for track " + (i+1)
                    // ) ;
                    //JVP.setViewPosition(JVPRef.getViewPosition()) ;
                    //JSP.setViewport(JVPRef) ;
                    //JVP.validate() ;
                    /*
                     * JVPRef.setView(TP) ; JSP.setViewport(JVPRef) ;
                     */
                }
                i++;
            } while ((i < this.numTracks));
        }
    }

    /**
     * @param numTracks
     *            The numTracks to set.
     */
    public void setNumTracks(int numTracks) {
        this.numTracks = numTracks;
    }

    /**
     * @return Returns the numTracks.
     */
    public int getNumTracks() {
        return numTracks;
    }
}