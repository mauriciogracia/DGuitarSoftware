package dguitar.gui;

/*
 * SongInternalFrame.java
 *
 * Created on November 8, 2004, 11:28 PM
 */

import i18n.Internationalized;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.event.InternalFrameListener;

import common.ScrollText;
import common.SwingWorker;

import dguitar.adaptors.guitarPro.GPAdaptor;
import dguitar.adaptors.song.Song;
import dguitar.codecs.CodecFormatException;
import dguitar.codecs.guitarPro.GPFormatException;
import dguitar.codecs.guitarPro.GPInputStream;
import dguitar.codecs.guitarPro.GPSong;
import dguitar.codecs.guitarPro.statistics.GPStatsPiece;
import dguitar.codecs.midi.MIDFileFilter;
import dguitar.codecs.midi.MidiOutputStream;
import dguitar.players.gui.MusicCursorScroller;
import dguitar.players.sound.Arrangement;
import dguitar.players.sound.MasterPlayer;
import dguitar.players.sound.midi.MidiPlayer;

/**
 * this class represents a JInternalFrame for any new of Open file
 * 
 * @author Mauricio Gracia 
 */
public class SongInternalFrame extends JInternalFrame 
implements			InternalFrameListener,
			Internationalized, 
			OptionsDisplay ,
			Playable
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -5162064904973349576L;

	private boolean closeIt = true;

    private String songName;

    private DGuitar parent;

    private JSeparator jSeparator1;

    private JScrollPane JSP;

    private JMenuBar menuChild;

    private JMenu menuFile;

    private JMenuItem menuFileClose;

    private JMenuItem menuFileExportMidi;

    private JMenuItem menuFilePrint;

    private JMenuItem menuFileScoreInformation;

    private JMenuItem menuFileStats;

    private JMenuItem menuFileTextView;

    private JMenu menuView;

    private JMenuItem menuViewColorOptions;

    private JCheckBoxMenuItem menuViewMinPiece;

    private JCheckBoxMenuItem menuViewMultiTrack;

    private JCheckBoxMenuItem menuViewMusicCursors;

    private JCheckBoxMenuItem menuViewSingleMusicCursorsColor;

    private JCheckBoxMenuItem menuViewUnsupportedEffects;
    
    private JFileChooser fileChooser;

    private GPSong gpSong;

    MasterPlayer player;

    private short status;

    private SongPanel songPanel;

    private ScoreInformationDialog SID;

    private Song song;

    /**
     * Statistics are displayed using this object
     */
    private ScrollText STStats;

    /**
     * TextView of a song is displayed using this object
     */
    private ScrollText STTextView;

    private SwingWorker workerPlay;

    private DisplayOptions displayOptions;

    //variables or objects needed for playback

    private SwingWorker workerStats;

    /**
     * A variable used to evaluate performance when a process starts
     */
    private long start ;
    
    /**
     * A variable used to evaluate performance when a process finish
     */
    private long finish ;
    
    PlayToolBar playToolBar ;
    /**
     * This method is provided to allow the fast creation of this object
     * for casting purposes
     *
     */
    public SongInternalFrame() {
        super();
        parent = null;
        this.displayOptions = new DisplayOptions() ;
        //commented on Jan.06.2006 
        //commonConstructor(null, null);
    }
    /**
     * This method creates a SongInternalFrame linked to a DGuitar main window
     * @param songName      the song name to display as title of this internal frame
     * @param DG            the DGuitar object that contains this one    
     * @param DO            the DisplayOptions to use for this object
     * @param aPlayToolBar  the PlayToolBar to be used
     */
    public SongInternalFrame(String songName, DGuitar DG, DisplayOptions DO, PlayToolBar aPlayToolBar) {
        super();
        parent = DG;
        this.displayOptions = (DisplayOptions) DO.clone();
        this.playToolBar = aPlayToolBar ;
        
        commonConstructor(songName, DG.fileChooser);
    }
    private void commonConstructor(String aSongName,JFileChooser aFileChooser) {
        this.player = null;
        this.song = null;
        this.status = NOT_PLAYING ;
        this.workerStats = null;
        this.songName = aSongName;
        this.setTitle(aSongName);
        this.STStats = null;
        this.STTextView = null;
        this.fileChooser = aFileChooser ;

        initComponents();


        //OLD this.songPanel = new SongPanel(this.displayOptions);
        
        this.songPanel = new SongPanel();
        
        //this.DisplayOptions has been initialized before this method
        this.setDisplayOptions(this.displayOptions) ;

        this.getContentPane().add(this.songPanel, java.awt.BorderLayout.CENTER);

        //TODO, this method should be called eventually
        //TODO check if the WIDTH of the cursors is correct
        //this.PP.musicCursorsAlign() ;
    }
    /**
     * This methods apply the <code>aDisplayOptions</code> values to this Song
     * @param aDisplayOptions
     */
    public void setDisplayOptions(DisplayOptions aDisplayOptions) {
        //TODO this method should check if the COLOR DIALOG is open and refresh the colors
        
        //seting the menus according to the values of the DisplayOptions (DO)
        
        //pass the parameter to the songPanel
        this.songPanel.setDisplayOptions(this.displayOptions) ;
        
        //is the MultiTrack option selected ?
        this.menuViewMultiTrack.setSelected(this.displayOptions.multiTrackView);

        //is the Minimum Piece duration spacing selected ?
        this.menuViewMinPiece.setSelected(this.displayOptions.minPieceSpacing);

        //are MusicCursors visible ?
        this.menuViewMusicCursors.setSelected(this.displayOptions.displayMusicCursor);

        //MusicCursors use a single color ?
        this.menuViewSingleMusicCursorsColor
                .setSelected(!this.displayOptions.singleMusicCursorColor);

        //display the unsupported effects as ?  ?
        this.menuViewUnsupportedEffects
                .setSelected(this.displayOptions.displayUnsupportedEffects);

        
    }
    /* (non-Javadoc)
     * @see dguitar.gui.OptionsDisplay#getDisplayOptions()
     */
    public DisplayOptions getDisplayOptions() {
        return displayOptions ;
    }

    private void close() {
        //If the worker is doing something...stop it
        if (this.workerStats != null) {
            this.workerStats.interrupt();
        }
        //If the workerPlay is doing something...stop it
        if ((this.workerPlay != null) && (status == PLAYING)) {
        	setStatus(NOT_PLAYING) ;
        }

        //If theres is a Text View dispose it
        if (this.STTextView != null) {
            this.STTextView.dispose();
        }
        
        //if there is a Statistics Window open dispose it
        if (this.STStats != null) {
            this.STStats.dispose();
        }
        if ((this.SID != null) && (this.SID.isVisible())) {
            //if(this.SID.hasChanged()
            //ask the user if he wants to save the changes of the
            // ScoreInformation
            //this.SID.setVisible(false) ;
            this.SID.dispose();
        }
        /*
         * This will handle changes performed on this information
         * if(this.hasChanged()) { value = JOptionPane.showConfirmDialog(this,
         * "Save Changes to file \'" + this.file + "\' ?", "Saving NOT
         * implemented yet",JOptionPane.YES_NO_CANCEL_OPTION) ; switch (value) {
         * case 0: //SAVE THE FILE //save the file closeIt = true ; break ; case
         * 1: //USER CHOSE NO TO SAVE closeIt = true ; break ; case 2: // USER
         * CANCELED. closeIt = false ; break ; } } else
         */
        closeIt = true;
        if (closeIt) {
            this.dispose();
        }
    }


    private void componentsCreate() {
        //solving an unrerported bug..when exporting to MIDI and fileChooser is null
        if(this.fileChooser == null) {
            //when parent is null is being used in SongAction
            if(parent != null) {
                parent.createFileChooser() ;
                this.fileChooser = parent.fileChooser ;
            }
        }
        JSP = new javax.swing.JScrollPane();
        menuChild = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        menuFileScoreInformation = new javax.swing.JMenuItem();
        menuFileTextView = new javax.swing.JMenuItem();
        menuFilePrint = new javax.swing.JMenuItem();
        menuFileStats = new javax.swing.JMenuItem();
        menuFileExportMidi = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        menuFileClose = new javax.swing.JMenuItem();
        menuView = new javax.swing.JMenu();
        menuViewMinPiece = new javax.swing.JCheckBoxMenuItem();
        menuViewMultiTrack = new javax.swing.JCheckBoxMenuItem();
        menuViewColorOptions = new javax.swing.JMenuItem();
        menuViewUnsupportedEffects = new JCheckBoxMenuItem();
        menuViewMusicCursors = new JCheckBoxMenuItem();
        menuViewSingleMusicCursorsColor = new JCheckBoxMenuItem();

        setBorder(new javax.swing.border.SoftBevelBorder(
                javax.swing.border.BevelBorder.RAISED));
        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        addInternalFrameListener(this);

        getContentPane().add(JSP, java.awt.BorderLayout.CENTER);

        menuFileScoreInformation
                .addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        menuFileScoreInformationActionPerformed(evt);
                    }
                });

        menuFile.add(menuFileScoreInformation);
        menuFileTextView.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                menuFileTextViewActionPerformed(evt);
            }
        });

        menuFile.add(menuFileTextView);
        menuFilePrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                menuFilePrintActionPerformed(evt);
            }
        });

        menuFile.add(menuFilePrint);

        //STATISTICS MENU
        menuFileStats.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                menuFileStatsActionPerformed(evt);
            }
        });

        menuFile.add(menuFileStats);

        //EXPORT MIDI MENU
        menuFileExportMidi.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                menuFileExportMidiActionPerformed(evt);
            }
        });

        menuFile.add(menuFileExportMidi);

        menuFile.add(jSeparator1);

        menuFileClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                menuFileCloseActionPerformed(evt);
            }
        });

        menuFile.add(menuFileClose);

        menuChild.add(menuFile);
        // MENU for the PieceOptions
        // MENU MIN PIECE
        menuViewMinPiece.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                viewMinPiece(evt);
            }
        });

        menuView.add(menuViewMinPiece);

        menuViewMultiTrack.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                viewMultiTrack(evt);
            }
        });

        menuView.add(menuViewMultiTrack);

        //COLOR MENU
        menuViewColorOptions.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                viewColorOptions(evt);
            }
        });
        menuView.add(menuViewColorOptions);
        
        //		display or not the question mark option for the effects
        menuViewUnsupportedEffects.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                viewUnsupportedEffects(evt);
            }
        });
        menuView.add(menuViewUnsupportedEffects);


        //		do you want to see the music cursors
        menuViewMusicCursors.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                viewMusicCursors(evt);
            }
        });
        menuView.add(menuViewMusicCursors);


        //display all musicCursors as a Single color or with the color of the
        // tracks
        menuViewSingleMusicCursorsColor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                viewSingleMusicCursorsColor(evt);
            }
        });
        menuView.add(menuViewSingleMusicCursorsColor);

        menuChild.add(menuView);

        setJMenuBar(menuChild);

        setLocation(new java.awt.Point(0, 0));
        //INTERNATIONALIZATION
        setLangText();
    }

    private void viewDisplayColorDialog() {
        ColorDialog CP;
        boolean changed;

        CP = new ColorDialog(this.parent);
        CP.fretColors.setColors(displayOptions.fretColors, displayOptions.coloringForFrets);
        CP.rhythmColors.setColors(displayOptions.rhythmColors, displayOptions.coloringForRhythm);
        CP.setVisible(true);

        if (CP.OKclicked()) {
            changed = false;
            if (CP.fretColors.selectionChanged()) {
                displayOptions.fretColors = CP.fretColors.getColors();
                displayOptions.coloringForFrets = CP.fretColors.getTypeOfColoring();
                changed = true;
            }
            if (CP.rhythmColors.selectionChanged()) {
                displayOptions.rhythmColors = CP.rhythmColors.getColors();
                displayOptions.coloringForRhythm = CP.rhythmColors.getTypeOfColoring();
                changed = true;
            }
            if (changed) {
                this.songPanel.repaint();
            }
        }

    }

    /**
     * Indicates whether some other object is 'equal to'his one.
     * <p>
     * The <code>equals</code> method implements an equivalence relation on
     * non-null object references:
     * <ul>
     * <li>It is <i>reflexive </i>: for any non-null reference value
     * <code>x</code>,<code>x.equals(x)</code> should return
     * <code>true</code>.
     * <li>It is <i>symmetric </i>: for any non-null reference values
     * <code>x</code> and <code>y</code>,<code>x.equals(y)</code> should
     * return <code>true</code> if and only if <code>y.equals(x)</code>
     * returns <code>true</code>.
     * <li>It is <i>transitive </i>: for any non-null reference values
     * <code>x</code>,<code>y</code>, and <code>z</code>, if
     * <code>x.equals(y)</code> returns <code>true</code> and
     * <code>y.equals(z)</code> returns <code>true</code>, then
     * <code>x.equals(z)</code> should return <code>true</code>.
     * <li>It is <i>consistent </i>: for any non-null reference values
     * <code>x</code> and <code>y</code>, multiple invocations of
     * <tt>x.equals(y)</tt> consistently return <code>true</code> or
     * consistently return <code>false</code>, provided no information used
     * in <code>equals</code> comparisons on the objects is modified.
     * <li>for any non-null reference value <code>x</code>,
     * <code>x.equals(null)</code> should return <code>false</code>.
     * </ul>
     * <p>
     * The <tt>equals</tt> method for class <code>Object</code> implements
     * the most discriminating possible equivalence relation on objects; that
     * is, for any non-null reference values <code>x</code> and <code>y</code>,
     * this method returns <code>true</code> if and only if <code>x</code>
     * and <code>y</code> refer to the same object (<code>x == y</code> has
     * the value <code>true</code>).
     * <p>
     * Note that it is generally necessary to override the <tt>hashCode</tt>
     * method whenever this method is overridden, so as to maintain the general
     * contract for the <tt>hashCode</tt> method, which states that equal
     * objects must have equal hash codes.
     * 
     * @param obj
     *            the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     * @see #hashCode()
     * @see java.util.Hashtable
     */
    public boolean equals(Object obj) {
        boolean equal = false;
        SongInternalFrame cast;

        if (obj != null) {
            if (obj.getClass().isInstance(this)) {
                cast = (SongInternalFrame) obj;
                if (cast.title == null) {
                    equal = (this.title == null);
                } else {
                    equal = cast.title.equals(this.title);
                }
            }
        }

        return equal;
    }

    public boolean hasChanged() {
        return this.songPanel.hasChanged();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        componentsCreate();
    }//GEN-END:initComponents

    /*
     * Taken from Player demo and changed to be integrated
     */
    private void initSongAndPlayer() {
        int cont;
        boolean scrollTrack;
        boolean multiTrack;
        MusicCursorScroller musicCursorScroller;
        String aux ;

        if(parent.evaluate) {
            start = System.currentTimeMillis() ;
        }
        if (song == null) {
            try {
                //Attempting to load file as Guitar Pro
                //if (song == null) {
                DGuitar.LS.println(DGuitar.lang.getString("pleaseWait"));
                song = GPAdaptor.makeSong(gpSong);
                //}

                // Create the master player. The master player is a
                // 'framework' on
                // which we can hang all sorts of EventListeners and
                // SoundPlayers.
                // We issue all instructions through the MasterPlayer and it
                // sends
                // them to the appropriate SoundPlayer, or timer device.
                //       if (player == null) {
                player = new MasterPlayer();
                //     }
            } catch (GPFormatException e1) {
                e1.printStackTrace();
            }
        }

        // Specify a SoundPlayer. This one is the MIDI
        // implementation.
        // SoundPlayers have to be sligthly different than event
        // listeners.
        // The main difference is a SoundPlayer must create whatever
        // timer
        // resources it needs (while event listeners can react to
        // any type
        // of timer, the sound implementations need a more accurate
        // clock).
        player.setSoundPlayer(new MidiPlayer());

        // Specify the subdivision of timing frequency. Here we will
        // specify
        // we want the timer to click 4 times for every musical
        // notation
        // (for example, if the score is 4/4, then we will click 16
        // times
        // in each measure). Some timer implementations will not
        // allow you
        // to change the clock frequency like this.
        player.setTimerFrequency(4);

        // Specify we also want to receive other types of events
        // (these have not been implemented yet).
        player.enableNoteEvents(true); // deliver all note
        // events to all
        // Listeners

        // Add listeners for the timer and for the other events.
        // Listeners
        // just implement the PerformanceXXXListener interfaces. It
        // is up
        // to the SoundPlayer and the timer to decide how to
        // implement
        // these. I believe the MIDI module uses the standard Java
        // event
        // mechanism, so you should be able to do normal Swing
        // event handling.

        player.addTimerListener(this.playToolBar.getPlayPanel());

        //TODO adding a PerformanceTimerListener for EVERY TRACK...is
        // not efficient is it ??
        multiTrack = this.songPanel.isMultiTrackView();
        for (cont = 0; cont < this.songPanel.getNumTracks(); cont++) {
            //the scroll is done when is NOT multitrack..or
            //when is multitrack but only for the fist track.
            scrollTrack = ((!multiTrack) || (multiTrack && (cont == 0)));

            musicCursorScroller = new MusicCursorScroller(songPanel
                    .getTrackPanel(cont), scrollTrack);
            player.addTimerListener(musicCursorScroller);
            //DEBUG
            //System.err.println("MusicCursorScoller #"+ cont + " was
            // added" ) ;
        }
        /*
         * player.addTimerListener(new ExampleTimerListener());
         * player.addEventListener(new ExampleEventListener());
         */

        // specify what song to play, and how to arrange it
        // (arrangement not supported yet, so this will play the
        // entire song
        // at the default tempo set at the top of the song). Note
        // that the
        // Arrangement is done by the particular player, so for
        // instance
        // the MIDI player will make a MIDI file
        Arrangement arrangement = null;
        player.arrange(song, arrangement);

        finish = System.currentTimeMillis() ;
        
        if(parent.evaluate) {
            finish = System.currentTimeMillis();
            
            aux = DGuitar.lang.getString("evaluateAdaptingProcessTook") ; 
            aux += (finish - start) / 1000 + " " + DGuitar.lang.getString("seconds");
            System.err.println(aux);
        }
        
        // Configure the workerPlay to start the
        // listeners.
        workerPlay = new SwingWorker() {
            public Object construct() {
                player.start();
                player.waitForCompletion();
                setStatus(NOT_PLAYING);

                return (null);
            }
        };
    }

    public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
    }

    public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
    }

    public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
        this.close();
    }

    public void internalFrameDeactivated(
            javax.swing.event.InternalFrameEvent evt) {
    }

    public void internalFrameDeiconified(
            javax.swing.event.InternalFrameEvent evt) {
    }

    public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
    }

    public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
    }

    private void menuFileCloseActionPerformed(ActionEvent evt) {//GEN-FIRST:event_menuFileCloseActionPerformed
        this.close();
    }//GEN-LAST:event_menuFileCloseActionPerformed

    private void menuFileExportMidiActionPerformed(ActionEvent evt) {
        File MIDfile;
        FileOutputStream FOS;
        MidiOutputStream mos;
        int val;
        //the absolute name of the file.
        String absolute;
        //is the file to be exported ?
        boolean export;
        String aux ;
        
        this.fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        this.fileChooser.setApproveButtonText(DGuitar.lang.getString("menuExportMidi"));

        //set the suggested name to <name>.mid
        this.fileChooser.setSelectedFile(new File(trimExtension() + ".mid"));

        //set the file Filter to *.MID
        this.fileChooser.setFileFilter(new MIDFileFilter());

        val = this.fileChooser.showSaveDialog(this);
        if (val == JFileChooser.CANCEL_OPTION)
            return;
        //ELSE
        if (val == JFileChooser.ERROR_OPTION)
            return; //replace this by something more helpfull
        //ELSE
        MIDfile = this.fileChooser.getSelectedFile();
        absolute = MIDfile.getAbsolutePath();
        export = true;
        if (MIDfile.exists()) {            
            String aux2 ;
        
            //verify if user wants to replace the existing file
            aux = absolute  + " " + DGuitar.lang.getString("already_exists") + "." ;
            aux += DGuitar.lang.getString("doYouWantToReplaceIt") ;

            aux2 = DGuitar.lang.getString("File") + " " + DGuitar.lang.getString("already_exists") ;
            val = JOptionPane.showConfirmDialog(this,  aux, aux2, JOptionPane.YES_NO_OPTION);

            export = (val == JOptionPane.YES_OPTION);
        }
        //if still user wants to export
        if (export) {
            start = 99999 ;
            if(parent.evaluate) {
                start = System.currentTimeMillis();
            }
           try {
                FOS = new FileOutputStream(MIDfile);
                mos = new MidiOutputStream(FOS);
                mos.write(this.gpSong);
                /*
                 * mos.write(object);
                 * 
                 * The object you pass can be anything that the player supports
                 * (a GPSong, or a Song, or any of the Performance objects that
                 * are produced by the player).
                 */

                mos.close();
                FOS.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (CodecFormatException e) {
                e.printStackTrace();
            }
            if(parent.evaluate) {
                finish = System.currentTimeMillis();
                aux = DGuitar.lang.getString("evaluateExportProcessTook") + ":"; 
                aux += (finish - start) / 1000 + " " + DGuitar.lang.getString("seconds");
                System.err.println(aux);

            }

            System.err.println(MIDfile.getAbsolutePath() + " has been created");
        }
    }

    private void menuFilePrintActionPerformed(ActionEvent evt) {//GEN-FIRST:event_menuFilePrintActionPerformed
        this.print();
    }//GEN-LAST:event_menuFilePrintActionPerformed

    private void menuFileScoreInformationActionPerformed(ActionEvent evt) {//GEN-FIRST:event_menuFileScoreInformationActionPerformed
        if (this.SID == null) {
            Container c = this.getParent();
            if (c.getClass().isInstance(new JFrame())) {
                this.SID = new ScoreInformationDialog((JFrame) c, false);
            } else {
                this.SID = new ScoreInformationDialog(null, false);
            }
            this.SID.setTitle(this.songName + "-" + SID.getTitle());
            //if(this.piece != null) {
            //Fill the Score information dialog.
            this.SID.setPiece(this.gpSong);
            //}
        }
        this.SID.setVisible(true);
    }//GEN-LAST:event_menuFileScoreInformationActionPerformed

    private void menuFileStatsActionPerformed(ActionEvent evt) {//GEN-FIRST:event_menuFileStatsActionPerformed

        if (STStats == null) {
            STStats = new ScrollText();
            STStats.setClosable(true, false);
            STStats.setTitle(this.songName + "-"
                    + DGuitar.lang.getString("Statistics"));

            STStats.setEditable(false);

            workerStats = new SwingWorker() {
                public Object construct() {
                    String aux;
                    GPStatsPiece GSP;

                    aux = menuFileStats.getText();
                    menuFileStats.setEnabled(false);
                    menuFileStats.setText(DGuitar.lang.getString("pleaseWait"));
                    GSP = new GPStatsPiece();
                    GSP.setDetailed(true);
                    GSP.getStats(gpSong, songName);
                    STStats.setText(GSP.toString());

                    menuFileStats.setText(aux);
                    menuFileStats.setEnabled(true);
                    return (null);
                }
            };
            workerStats.start();
        }
        STStats.setVisible(true);
    }//GEN-LAST:event_menuFileStatsActionPerformed

    private void menuFileTextViewActionPerformed(ActionEvent evt) {//GEN-FIRST:event_menuFileTextViewActionPerformed
        if (STTextView == null) {
            STTextView = new ScrollText();
            STTextView.setClosable(true, false);
            STTextView.setTitle(this.songName + "- "
                    + DGuitar.lang.getString("Text_View"));
            //DEBUG
            //System.err.println("TEXT VIEW!!") ;
            this.gpSong.startDisplayString(STTextView.getScrollTextPanel());
            STTextView.addWindowListener(new WindowListener() {
                public void windowActivated(WindowEvent e) {
                }

                public void windowClosed(WindowEvent e) {
                }

                public void windowClosing(WindowEvent e) {
                    gpSong.stopDisplayString();
                    /*
                     * if(!piece.isDone()) { //DGuitar.showDialogOk("warning",
                     * "The process of converting to String has not finished, It
                     * will be stopped now") ; }
                     */
                }

                public void windowDeactivated(WindowEvent e) {
                }

                public void windowDeiconified(WindowEvent e) {
                }

                public void windowIconified(WindowEvent e) {
                }

                public void windowOpened(WindowEvent e) {
                }
            });
            STTextView.setEditable(false);
        }
        STTextView.setVisible(true);
    }//GEN-LAST:event_menuFileTextViewActionPerformed

    private void viewColorOptions(ActionEvent evt) {
        this.viewDisplayColorDialog();
    }

    private void viewMinPiece(ActionEvent evt) {
        this.songPanel.toggleMinPieceSpacing();
        this.songPanel.validate();
        //not necessary ?this.PP.repaint() ;
    }

    private void viewMultiTrack(ActionEvent evt) {//GEN-FIRST:event_menuMultiTrackActionPerformed
        this.songPanel.toggleMultiTrackView();
        this.songPanel.displayPiece();
        this.songPanel.validate();
        DGuitar.internalFrameOptSize(this);
    }//GEN-LAST:event_menuMultiTrackActionPerformed

    private void viewMusicCursors(ActionEvent evt) {
        this.songPanel.toggleViewMusicCursors();
    }

    private void viewSingleMusicCursorsColor(ActionEvent evt) {
        this.songPanel.toggleSingleMusicCursorColor();
    }

    private void viewUnsupportedEffects(ActionEvent evt) {
        this.songPanel.toggleDisplayUnsupportedEffects();
        this.songPanel.repaint();
    }

 

    /*
     * Using the PRINTING API A typical application using the Java Print Service
     * API performs these steps to process a print request: - Chooses a
     * DocFlavor. - Creates a set of attributes. - Locates a print service that
     * can handle the print request as specified by the DocFlavor and the
     * attribute set. - Creates a Doc object encapsulating the DocFlavor and the
     * actual print data, which can take many forms including: a Postscript
     * file, a JPEG image, a URL, or plain text. - Gets a print job, represented
     * by DocPrintJob, from the print service. - Calls the print method of the
     * print job.
     * 
     * The following code sample demonstrates a typical use of the Java Print
     * Service API: locating printers that can print five double-sided copies of
     * a Postscript document on size A4 paper, creating a print job from one of
     * the returned print services, and calling print.
     *  
     */
    public void print() {

        common.Util.showDialogOk(this.getParent(), DGuitar.lang
                .getString("Print_option_under_work"), DGuitar.lang
                .getString("This_is_not_finished"));
        //Chooses a DocFlavor.

        /*
         * DocFlavor InFormat = DocFlavor.INPUT_STREAM.JPEG ;
         * 
         * //Creates a set of attributes. Doc myDoc = new SimpleDoc(GIS,
         * InFormat, null); PrintRequestAttributeSet aset = new
         * HashPrintRequestAttributeSet(); aset.add(new Copies(1));
         * //aset.add(MediaSize.ISO.A4) ; aset.add(Sides.ONE_SIDED);
         * 
         * //Locates a print service that can handle the print request as
         * specified by the DocFlavor and the attribute set. PrintService[]
         * services = PrintServiceLookup.lookupPrintServices(InFormat, aset);
         * 
         * //Creates a Doc object encapsulating the DocFlavor and the actual
         * print data, which can take many forms including: a Postscript file, a
         * JPEG image, a URL, or plain text. if (services.length > 0) {
         * DocPrintJob job = services[0].createPrintJob(); try {
         * job.print(myDoc, aset); } catch (PrintException pe) {
         * DGuitar.showDialogOk("Print Exception",pe.toString()) ; } } else {
         * DGuitar.showDialogOk("No print services are
         * available","lookupPrintServices failed, printing not posible") ; }
         * 
         * //Gets a print job, represented by DocPrintJob, from the print
         * service.
         * 
         * //Calls the print method of the print job.
         * 
         * 
         * /*FileInputStream psStream; try { psStream = new
         * FileInputStream("file.ps"); } catch (FileNotFoundException ffne) { }
         * if (psStream == null) { return; } REMEMBER TO CLOSE THE STREAM !!!
         * 
         * DocFlavor psInFormat = DocFlavor.INPUT_STREAM.POSTSCRIPT; Doc myDoc =
         * new SimpleDoc(psStream, psInFormat, null); PrintRequestAttributeSet
         * aset = new HashPrintRequestAttributeSet(); aset.add(new Copies(5));
         * aset.add(MediaSize.A4); aset.add(Sides.DUPLEX); PrintService[]
         * services = PrintServiceLookup.lookupPrintServices(psInFormat, aset);
         * if (services.length > 0) { DocPrintJob job =
         * services[0].createPrintJob(); try { job.print(myDoc, aset); } catch
         * (PrintException pe) {} }
         */
    }

    /**
     * This method readsAndDisplay a piece from a GPInputStream GPis
     * 
     * @param GPis
     *            is a non-null GPinputStream
     * @return if the process was successfull
     */
    public boolean readAndDisplay(GPInputStream GPis) {
        boolean success;

        success = this.songPanel.readPieceFrom(GPis);
        if (success) {
            this.gpSong = this.songPanel.getPiece();
            this.songPanel.displayPiece();
            this.songPanel.validate();
            //Once the song has been displayed set the number of measure on the playPanel
            this.playToolBar.getPlayPanel().setNumMeasures(this.gpSong.getNumMeasures()) ;
        } else {
            DGuitar.msg = "PP.readPieceFrom(GPInputStream_gis)"
                    + DGuitar.lang.getString("FAILED");
        }
        return success;
    }

    /**
     * This methods disable the components that may interfere with the playing
     * of the song
     *  
     */
    private void setEnableComponents(boolean enable) {
        //disable the cursors on all the tracks (this does not hide them)
        this.songPanel.musicCursorsSetEnable(enable);
        
        //disable other options that might interfere with playing
        this.menuViewMinPiece.setEnabled(enable);
        this.menuViewMultiTrack.setEnabled(enable);
        this.menuFileStats.setEnabled(enable);
        this.menuFileTextView.setEnabled(enable);
        this.menuFileExportMidi.setEnabled(enable) ;
    }

    public void setLangText() {
        this.menuFile.setText(DGuitar.lang.getString("File"));
        this.menuFileScoreInformation.setText(DGuitar.lang
                .getString("menuPIFScoreInformation"));
        this.menuFileTextView
                .setText(DGuitar.lang.getString("menuPIFTextView"));
        this.menuFilePrint.setText(DGuitar.lang.getString("menuPIFPrint"));
        this.menuFileStats.setText(DGuitar.lang.getString("Statistics"));
        this.menuFileExportMidi.setText(DGuitar.lang
                .getString("menuExportMidi"));
        this.menuFileClose.setText(DGuitar.lang.getString("menuClose"));
        this.menuView.setText(DGuitar.lang.getString("menuView"));
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

        if (STStats != null) {
            STStats.setTitle(this.songName + "-"
                    + DGuitar.lang.getString("Statistics"));
        }
        if (this.SID != null) {
            SID.setLangText();
        }
    }

    private String trimExtension() {
        return this.songName.substring(0, this.songName.indexOf('.'));
    }
	/**
	 * @param st the status to be set
	 */
    @Override
	public void setStatus(short st) {
		this.status = st;
		
		if(st == PLAYING) {
	        //disable the conflicting components while playing
	        this.setEnableComponents(false);
	        
	        //initialize the player
	        this.initSongAndPlayer();

	        //start the SwingWorker that performs the actual playing
	        workerPlay.start();
		}
		else if (st == NOT_PLAYING) {
			player.stop();
			player.close();
			this.workerPlay.interrupt();
			
			//enable the components again.
			setEnableComponents(true);
			
			this.playToolBar.setStatus(Playable.NOT_PLAYING) ;
		}
	}
	@Override
	public short getStatus() {
		return this.status ;
	}


	@Override
	public String getSongTitle() {
		return this.title ;
	}


}