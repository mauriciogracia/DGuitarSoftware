/*
 * Created on 31/03/2005
 *
 */
package dguitar.gui;

import i18n.Internationalized;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
//import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import dguitar.gui.listeners.MenuIndexedListener;

/**
 * @author Mauricio Gracia Gutierrez
 *  
 */
public class SongOptionsMenu extends JMenu 
implements ActionListener,
        Internationalized 
        {
//UNUSED	private JFrame jFrame;

    /**
	 * 
	 */
	private static final long serialVersionUID = 1607817653206686890L;

	private String keyName;

    private JMenuItem menuViewColorOptions;

    private JCheckBoxMenuItem menuViewMinPiece;

    private JCheckBoxMenuItem menuViewMultiTrack;

    private JCheckBoxMenuItem menuViewMusicCursors;

    private JCheckBoxMenuItem menuViewSingleMusicCursorsColor;

    private JCheckBoxMenuItem menuViewUnsupportedEffects;

    public static final int COLOR_OPTIONS = 1;

    public static final int MIN_PIECE = 2;

    public static final int MULTI_TRACK = 3;

    public static final int MUSIC_CURSORS = 4;

    public static final int SINGLE_MUSIC_CURSORS_COLOR = 5;

    //this will eventually disappear
    public static final int UNSUPPORTED_EFFECTS = 99;

    private int selectedOption;

    private MenuIndexedListener songOptionsListener;

    private DisplayOptions displayOptions;

    /**
     * This constructor is provided for isInstancee/casting purposes
     *  
     */
    public SongOptionsMenu() {
        super();
//UNUSED        this.jFrame = null;
    }

    public SongOptionsMenu(String aKeyName) {
        super();
        if (aKeyName != null) {
//      UNUSED        if ((aKeyName != null) && (aFrame != null)) {
//        	UNUSED            this.jFrame = aFrame;
            this.keyName = aKeyName;
            this.selectedOption = -99;
            this.songOptionsListener = null;
            initComponents();
        } else {
            throw new NullPointerException("aDisplayOptions or aFrame is null");
        }

    }

    public void setMenuIndexedListener(MenuIndexedListener aSongOptionsListener) {
        this.songOptionsListener = aSongOptionsListener;
    }

    public void setDisplayOptions(DisplayOptions aDisplayOptions) {
        this.displayOptions = aDisplayOptions;

        //is the MultiTrack option selected ?
        this.menuViewMultiTrack.setSelected(this.displayOptions.multiTrackView);

        //is the Minimum Piece duration spacing selected ?
        this.menuViewMinPiece.setSelected(this.displayOptions.minPieceSpacing);

        //are MusicCursors visible ?
        this.menuViewMusicCursors
                .setSelected(this.displayOptions.displayMusicCursor);

        //MusicCursors use a single color ?
        this.menuViewSingleMusicCursorsColor
                .setSelected(!this.displayOptions.singleMusicCursorColor);

        //display the unsupported effects as "?" ?
        this.menuViewUnsupportedEffects
                .setSelected(this.displayOptions.displayUnsupportedEffects);
    }

    private void initComponents() {
        menuViewMinPiece = new javax.swing.JCheckBoxMenuItem();
        menuViewMultiTrack = new javax.swing.JCheckBoxMenuItem();
        menuViewColorOptions = new javax.swing.JMenuItem();
        menuViewUnsupportedEffects = new JCheckBoxMenuItem();
        menuViewMusicCursors = new JCheckBoxMenuItem();
        menuViewSingleMusicCursorsColor = new JCheckBoxMenuItem();

        //MENU MIN PIECE
        menuViewMinPiece.addActionListener(this);
        this.add(menuViewMinPiece);

        menuViewMultiTrack.addActionListener(this);
        this.add(menuViewMultiTrack);

        //COLOR MENU
        menuViewColorOptions.addActionListener(this);
        this.add(menuViewColorOptions);

        //do you want to see the music cursors
        menuViewMusicCursors.addActionListener(this);
        this.add(menuViewMusicCursors);

        //display all musicCursors as a Single color
        //or
        //with the color of the tracks
        menuViewSingleMusicCursorsColor.addActionListener(this);
        this.add(menuViewSingleMusicCursorsColor);

        //display or not the question mark option for the effects
        menuViewUnsupportedEffects.addActionListener(this);

        this.add(menuViewUnsupportedEffects);

        //INTERNATIONALIZATION
        this.setLangText();

    }

    //INTERNATIONALIZATION
    public void setLangText() {
        this.setText(DGuitar.lang.getString(keyName));
        this.menuViewMinPiece.setText(DGuitar.lang
                .getString("menuPIFMinDurPiece"));
        this.menuViewMultiTrack.setText(DGuitar.lang
                .getString("menuPIFMultiTrack"));
        this.menuViewColorOptions.setText(DGuitar.lang
                .getString("menuPIFColors"));
        this.menuViewMusicCursors.setText(DGuitar.lang
                .getString("menuViewMusicCursors"));
        this.menuViewUnsupportedEffects.setText(DGuitar.lang
                .getString("menuViewUnsupportedEffects"));
        this.menuViewSingleMusicCursorsColor.setText(DGuitar.lang
                .getString("menuSingleMusicCursorsColor"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
        Object obj;
        Class<? extends Object> c;

        obj = e.getSource();
        c = obj.getClass();
        //TODO....THIS CODE DOES NOT WORK..BECAUS IT EVALUTES INSTANCES...
        //IT SHOULD EVALUATE actionCommands or something like that.
        if (c.isInstance(this.menuViewColorOptions)) {
            selectedOption = COLOR_OPTIONS;
        } else if (c.isInstance(this.menuViewMinPiece)) {
            selectedOption = MIN_PIECE;
        } else if (c.isInstance(this.menuViewMultiTrack)) {
            selectedOption = MULTI_TRACK;
        } else if (c.isInstance(this.menuViewMusicCursors)) {
            selectedOption = MUSIC_CURSORS;
        } else if (c.isInstance(this.menuViewSingleMusicCursorsColor)) {
            selectedOption = SINGLE_MUSIC_CURSORS_COLOR;
        }
        //this will eventually disapper
        else if (c.isInstance(this.menuViewUnsupportedEffects)) {
            selectedOption = UNSUPPORTED_EFFECTS;
        }

        //now that we now which option is selected, call the listener
        if (this.songOptionsListener != null) {
            this.songOptionsListener.menuClicked(this, selectedOption);
        }
    }

    public int getSelectedOption() {
        return this.selectedOption;
    }
}