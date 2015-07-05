package dguitar.gui;

/*
 * DGuitar.java
 *
 * Created on 21 de mayo de 2004, 01:52 AM
 */

import i18n.Internationalized;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.dnd.DropTarget;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.UIManager;


import common.FileFolderFilter;
import common.FilenameFilterAcceptExtension;
import common.FolderIterator;
import common.LogStream;
import common.LogStreamOptions;
import common.Midi;
import common.ScrollText;
import common.ScrollTextPanel;
import common.SwingWorker;
import common.Util;

import dguitar.codecs.CodecFileFilter;
import dguitar.codecs.guitarPro.GPFileFilter;
import dguitar.codecs.guitarPro.GPInputStream;
import dguitar.codecs.guitarPro.statistics.GPStats;
import dguitar.codecs.midi.MIDFileFilter;
import dguitar.gui.listeners.ADropTargetListener;
import dguitar.gui.listeners.DGInternalFrameListener;
import dguitar.gui.midi.MidiDevices;
import dguitar.gui.midi.MidiInternalFrame;
import dguitar.gui.midi.MidiTest;
import java.util.*;
/**
 * this is the DGuitar class which represents the principal window
 * 
 * @author Mauricio Gracia Guti�rrez
 */
public class DGuitar extends JFrame 
implements Internationalized,OptionsDisplay

{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5732964462630564585L;

	/**
	 * Set to true if you want Capture the Sytem.err to the LogStream
	 */
	private boolean captureErrors;
	
	/**
	 * Set to true if you want a confirmation dialog after file is loaded
	 */
	private boolean confirmLoad;
	
	/**
	 * The current skin index being used
	 */

    private static short currentSkinIndex ;
    
    /**
     * a buttonGroup that allows only one skin to be selected
     **/
    private static ButtonGroup buttonGroupSkin = null;
    
    private static String arraySkinNames[] = new String[2] ;
    /**
     * 
     */
	private static JCheckBoxMenuItem arraySkinCheckBoxes[] = new JCheckBoxMenuItem[2] ; 
	/**
	 * The Absolute or Full path were the properties of DGuitar are being stored
	 */
	private final String DGpropertiesAbsolute;
	
	/**
	 * set to true evaluate performance in time of certains parts of the program and log it
	 */
	public boolean evaluate;
	
	/**  
	 * this variable stores a preloaded JFileChooser
	 */
	protected DGFileChooser fileChooser;
	
	/**
	 * Indicates the current File Type
	 */
	private int currentFileType;
	
	/**
	 * The location of the GTP files
	 */
	private File filePath;
	
	/**  
	 * a Vector that contains a list of files show in the WINDOW MENU
	 */
	private Vector<String> files; 

	/**
	 * Indicates if the location of the files has changed in order to recalculate statistics
	 */
	private boolean hasPathChanged;
	
	/**
	 * if this variable is set to true..and user exits program..ask if he wants to save them
	 */
	private static boolean hasOptionsChanged ;
	
	/**
	 * this variable stores the current launch path
	 */
	private String lauchPath;
	
	/** 
	 * this contains the ICON for the application
	 */
	private static Image imgIcon;
	
	/**
	 * the path to the current skin
	 */
	private String skinPath;
	/**
	 * The path to the skin folder
	 */
	private String skinDir;
	
	private static boolean closableLogWindow = false ;
	
	private static boolean LogWindowExits = false ;
	
	private static boolean EnableUnfinishedFeatures = true ;
	
    /**
     * The index were the list of recent files start
     */
    public static final int RecentIndexStart = 6 ;
    
    private static int contRecent = 0 ;

    /* MENU items */

    private JMenuBar menu;
	
	private JMenu menuAbout;
	
	private JMenuItem menuAboutAuthors;
	
	private JMenuItem menuAboutLicense;
	
	private JMenu menuMidi;
	
	private JMenuItem menuMidiDevices;
	
	private JMenuItem menuMidiTest;
	
	private JMenu menuOptions;
	
	private JCheckBoxMenuItem menuOptionsConfirmLoad;
	
	private JCheckBoxMenuItem menuOptionsEvaluate;
	
	private JCheckBoxMenuItem menuOptionsFastLoad;
	
	private JMenuItem menuOptionsFileLocation;
	
	private static SongOptionsMenu menuOptionsForAllSongs;
	
	private JMenu menuOptionsLanguage;
	
	//menu options related to Logging 
	private JMenuItem menuOptionsLog;
	private JCheckBoxMenuItem menuOptionsLogToFile;
	private JCheckBoxMenuItem menuOptionsLogToWindow;
	
    private JMenuItem menuOptionsSkins ; 
	//menu options related to test features
	private JCheckBoxMenuItem menuOptionsEnableUnfinishedFeatures ;
	
	private JMenuItem menuOptionsReloadStoredSettings;
	
	private JMenuItem menuOptionsSaveCurrentSettings;
	
	private JMenu menuProgram;
	
	private JMenuItem menuProgramExit;
	
	private JMenuItem menuProgramNew;
	
	private JMenuItem menuProgramOpenGP;
	
	private JMenuItem menuProgramOpenMidi;
	
	private JMenuItem menuProgramOpenURL;
	
	private JMenuItem menuProgramStats;
	
	private JMenu menuWindows;
	
	private JMenuItem menuWindowsCascade ;
	
	private JMenuItem menuWindowsCloseAll ;
	
	private int menuWindowsOperationsCount ; 
	
	private ScrollText STstats;
	
	/**
	 * A list of the supportedExtension according to the available CoDecs
	 */
	public java.util.Vector<String> supportedExtensions;
	
	/**
	 * A variable that points to the user.home System property
	 */
	public String userHome;
	
	private SwingWorker worker;
	
	/**
	 * The About component that is shown.
	 */
	private static About about;
	
	private static String authors;
	
	/*The buttons used for the PlayToolbar (play, stop, pause..etc)*/
	protected static final Image Buttons[] = new Image[2];

	/*The disabled buttons used for the PlayToolbar..see above*/
	protected static final Image DisabledButtons[] = new Image[2];

	/**
	 * The Clefs represented as ImagesIcon[]
	 */
	protected static final ImageIcon Clefs[] = new ImageIcon[2];
	
	public static DGDesktopPane desktopPane;
	
	/**
	 * A Properties object that stores current values of options
	 */
	private static Properties DGO;
	
	/**
	 * Name of the file were the preferences of DGuitar are stored
	 */
	private static final String DGproperties = "DGuitar.properties";
	
	/** The current DisplayOptions for all the songs */
	private DisplayOptions displayOptions;
	
	/** The icons that hold the 8 Dynamics simbols, paino, fortissimo,etc.. */
	protected static final ImageIcon Dynamics[] = new ImageIcon[8];
	
	/** The EffectsOnNote images */
	//Every time a new effect needs to be painted modify this
	protected static final ImageIcon EONs[] = new ImageIcon[7];
	
	//load the minimum things
	private static boolean fastLoad;
	
	public static GPFileFilter gpFileFilter;
	
	/**
	 * Is this version a "release" (true) or a "developer"
	 */
	public static final boolean isRelease = true;
	
	/**
	 * A string that contains the operational system information on a String
	 */
	public static String operationalSystem ; 
	
	public static final short OS_LINUX = 0 ;
	
	public static final short OS_MAC = 2 ;
	
	public static final short OS_WIN = 3 ;
	
	public static final short OS_OTHER = 4 ; 
	
	/**
	 * A short that contains one of the OS_constants to easily handle native things
	 */
	short OS ;

	public static Playable prevPlayToolBarPlayable = null ;
	
	/**
	 * A class variable that stores the expected/supported java vendors
	 */
	public static final String javaVendorExpected []= { "Sun Microsystems Inc." ,"Apple Computer, Inc."};
	
	/**
	 * A class variable that holds the information on the detected java vendor
	 */
	public static String javaVMvendor;
	
	/**
	 * A class variable that holds the information on the detected java version
	 */
	public static String javaSpecVersion;
	
	/**
	 * A class variable that stores the expected java version
	 */
	public static final String javaVersionExpected = "1.4";
	
	/** The current cursor for every window */
	//protected Cursor currentCursor = null ;
    
	/** The resource bundle that allows INTERNATIONALIZATION (also called I18N) */
	public static PropertyResourceBundle lang;
	
    /**The path to the LANG folder**/
	private static String langPath;
	
	/** The locale (INTERNATIONIZATION) that java detected */
	private static Locale locale;
    
    /**The language code that is currently used**/
    private static String langCode ;
	
	protected static ImageIcon Logo;
	
	/*
	 * this toolbars are not used... 
	 * private ImageToolBar NoteToolBar ; 
	 * private ImageToolBar SilenceToolBar ;
	 */
	
	/**A toolbar that contains a PLAY button, an other controls and information*/
	public static PlayToolBar playToolBar ;

	protected static LogStream LS;
	
	private static Midi midi;
	
	public static MIDFileFilter midiFileFilter;
	
	/**
	 * The minimum required Height to run DGuitar
	 */
	private static int MIN_HEIGHT = 480;
	
	/**
	 * The mininum required Width to run DGuitar
	 */
	private static int MIN_WIDTH = 640;
	
	//At this moment cursors are not used
	//private final Cursor Cursors[] = new Cursor[14] ; //7 NOTES + 7 Silences
	
	/**
	 * A variable that holds the current message that should be showed.
	 */
	protected static String msg;
	
	/**
	 * there are 8 note durations (including 128th duration !!)
	 */
	protected static final Image Notes[] = new Image[8];
	
	private static Dimension screenSize;
	
	/**
	 * The Silence represented as ImagesIcon[]
	 */
	protected static final ImageIcon Silences[] = new ImageIcon[7];
	
	private static PrintStream sysErr;
	
	/**
	 * a static, or class variable, that stores the current ToolKit
	 */
	public static Toolkit toolkit;
	
	/**
	 * The version of the DGuitar program
	 */
	
	//GPCodec version = "0.8.1" <---provided by Matthieu Wipliez
	//
	//------------------------------------------------------------
	//
	// viewer version = "0.4.2" <---provided by Mauricio Gracia, May 28 2005
	// player version = "0.1.2" <---provided by Chris Nash
	//printer version = "0.0.0"
	//				    "0.0.4" other new features
	//                  -------
	//	                "0.5.8"
	public static final String version = "0.5.8";
	
	/**
	 * this method displays information about the autors, this is used also by
	 * the DGuitarApplet
	 */
	private static void aboutAuthors() {
		final String esp = "\n\t\t" ;
		
		if (authors == null) {
			authors = "";
			authors += "\t\t" + "DEVELOPERS:";
			authors += esp
			+ "Mauricio Gracia Gutierrez - Project Creator/Admin and User Interface";
			authors += esp
			+ "Matthieu Wipliez - GPDecoding and GP4Format Translator";
			authors += esp + "Chris Nash - Guitar Pro file player";
			authors += esp;
			authors += esp + "TRANSLATORS AND CONTRIBUTORS";
			authors += esp
			+ "Avery Ceo - GPFormat Translator French-English";
			authors += esp + "Timoth�e Birckel - Logo creation";
			
			//------------------TRANSLATORS---------
			authors += esp + "Catherine Beauchemin - French Tranlation";
			authors += esp + "Stefan Bethge - German Translation";
			authors += esp + "Hugo Doria - Portuguese Translation";
			authors += esp + "Rabbin \"Robin\" - Swedish Translation";
			authors += esp + "Petr \u0160igut - Czech Translation";
			authors += esp + "Marcin Wo\u0142oszczuk - Polish translation";
			authors += esp + "Dino Michelon - Italian translation (revised by Martello Giorgio)";
            authors += esp + "Anton Roslavsky - Russian translation" ; 
            authors += esp + "Pratima Rao - Recent File list and more to come";
			
			authors += esp;
			authors += esp + "For updates and more info visit:";
			authors += esp + "\t" + "http://DGuitar.sourceforge.net/";
		}
		if (about == null) {
			//this height is changed every time the authors string grows
			about = new About(600,480);
		}
		about.authors.setText(authors);
		about.setTitle(DGuitar.version());
		about.setVisible(true);
		
	}
	
	static void correctJavaVersion() {
		String aux;
		String err;
		int countVendors ;
		boolean correctVendor ;

		//System properties strings:
		//java.version Java Runtime Environment version
		//java.vendor Java Runtime Environment vendor
		try {
			DGuitar.javaVMvendor = System.getProperty("java.vm.vendor");
			DGuitar.javaSpecVersion = System.getProperty("java.specification.version");
			aux = "Java" + ": " + DGuitar.javaVMvendor;
			aux += " " + "version" + " " + DGuitar.javaSpecVersion;
			System.err.print(aux);
			//check if it is correct vendor and version
			
			//NEW
			correctVendor = false ;
			err = "\"" + DGuitar.javaVMvendor + "\" " + "is not one of these" + ": " ;
			
			for(countVendors = 0; !correctVendor && (countVendors < DGuitar.javaVendorExpected.length) ;countVendors++) 
			{
				correctVendor = !DGuitar.javaVMvendor.equals(DGuitar.javaVendorExpected[countVendors]) ;
				if(!correctVendor) {
					err += "\"" + javaVersionExpected + "\"";
				}
					
			}
			if(!correctVendor) {
				System.err.println(err);
				Util.showDialogOk(null,
					"Warning - incorrect Java vendor found", err);
				
			} else if (!DGuitar.higherJavaVersion()) {
				err = "\"" + DGuitar.javaSpecVersion + "\" " + "should be" + " " ;
				
				err += " " + "or higher";
				System.err.println(err);
				Util.showDialogOk(null,
						"Warning - incorrect Java version found", err);
			}
		} catch (SecurityException SE) {
			System.err.println(lang.getString("ERROR_EXCEPTION"));
			SE.printStackTrace();
		}
	}
	
	/**
	 * @return Returns the lang.
	 */
	public static PropertyResourceBundle getLang() {
		return lang;
	}
	
	static boolean higherJavaVersion() {
		boolean resp;
		int dotIndexA;
		int dotIndexB;
		String tokenA;
		String tokenB;
		int a;
		int b;
		
		resp = !DGuitar.javaSpecVersion.startsWith(javaVersionExpected);
		if (!resp) {
			//check if it is higher than expected version
			tokenA = new String(DGuitar.javaSpecVersion);
			tokenB = new String(DGuitar.javaVersionExpected);
			dotIndexA = tokenA.indexOf(".");
			dotIndexB = tokenA.indexOf(".");
			
			//DEBUG
			//System.err.println(dotIndexA + "," + dotIndexB) ;
			
			resp = true;
			while ((resp) && (dotIndexA >= 0) && (dotIndexB >= 0)) {
				a = Integer.valueOf(tokenA.substring(0, dotIndexA)).intValue();
				b = Integer.valueOf(tokenB.substring(0, dotIndexB)).intValue();
				
				//DEBUG
				//System.err.println(a + "," + b) ;
				
				resp = (a >= b);
				
				if (dotIndexA < tokenA.length()) {
					tokenA = tokenA.substring(dotIndexA + 1);
				}
				if (dotIndexB < tokenB.length()) {
					tokenB = tokenB.substring(dotIndexB + 1);
				}
				dotIndexA = tokenA.indexOf(".");
				dotIndexB = tokenA.indexOf(".");
				
			}
		}
		
		return resp;
	}
	
	/**
	 * this method locates a new internalFrame, the desktopPane.add(JIF) has
	 * alreacdy been called
	 */
	protected static void internalFrameLocate(JInternalFrame JIF) {
		int dx;
		int dy;
		int maxH;
		int maxRows;
		int row;
		int col;
		int n;
		int snapH;
		
		//calculate the size of the desktop ;
		
		maxH = desktopPane.getRootPane().getHeight();
		//TODO..use this information
		// maxW = desktopPane.getRootPane().getWidth() ;
		
		//this was increazed from 30 to 50, to allow the menus to be seen
		snapH = 50;
		maxRows = maxH / snapH;
		//the desktopPane.add(JIF) has alreacdy been called
		n = desktopPane.getComponentCount() - 1;
		
		col = n / maxRows;
		row = n % maxRows;
		dx = col * 30;
		dy = row * snapH;
		
		JIF.setBounds(dx, dy, 0, 0);
		internalFrameOptSize(JIF);
	}
	
	/**
	 * Sets the optimum size for a internalFrame ;
	 */
	protected static void internalFrameOptSize(JInternalFrame JIF) {
		int maxH;
		int maxW;
		int x;
		int y;
		Dimension dim;
		int h;
		int w;
		maxH = desktopPane.getRootPane().getHeight();
		maxW = desktopPane.getRootPane().getWidth();
		
		x = JIF.getX();
		y = JIF.getY();
		dim = JIF.getPreferredSize();
		h = dim.height;
		w = dim.width;
		
		//TODO when the playPanel was added this correction was made
		h = h + 40;
		if (y + h > maxH) {
			h = maxH - y;
		}
		
		//to fit the window to the parent size
		if (x + w > maxW) {
			w = maxW - x;
		}
		
		JIF.setSize(w, h);
		
	}
	/**
	 * convert a String array to a ArrayList, this is used when Dguitar is called with parameters
	 * @param args
	 * @return the list of the converted strings
	 */
	private ArrayList<File> convertStringsArgToFileList(String args[]) {
		ArrayList<File> someFiles ;
		short i ;
		
        someFiles = new ArrayList<File>();
        
		for (i = 0; i < args.length; i++) {
			//before doing this..args[i] has to be converted to full path
            someFiles.add(new File(args[i]));
		}
		return someFiles;
	}
	/**
	 * @param args
	 *            the command line arguments
	 * @throws FileNotFoundException 
	 */
	public static void main(String args[]) throws FileNotFoundException {
		DGuitar DG;
		String aux;
//		String logo;
		final LogStream LogS;
		LogStreamOptions LogSOptions ;
		String initS ;
		
		//Creates a new DGuitar instance
		DG = new DGuitar();
		
		//Sets the initial value for the Log Window and/or File
		initS = DGuitar.version() + "\t" + DateFormat.getDateTimeInstance().format(new Date() ) + "\n" ;
		
		aux = DG.getLaunchPath() ;
		//set the settings of the type of loggin
		DG.userHome = System.getProperty("user.home");
		aux = DG.userHome + File.separator + "DGuitarLog.txt" ;
		LogSOptions = new LogStreamOptions(LogStreamOptions.LOG_TO_FILE_AND_WINDOW,initS,aux) ;
		LogS = new common.LogStream(System.err, LogSOptions);
		
		//the second paramenter if you want to capture errors
		DG.init(LogS, true);
		
		//This creates a new thread for the LOG window to
		// allow GUI to stay active/responsive
		
		final SwingWorker worker = new SwingWorker() {
			public Object construct() {
				return (LogS);
			}
		};
		worker.start();
		
		//create and opens the Main Window
		
		if (DGuitar.requirementsOK()) {
			//load the skins, preferences and other settings
			DG.load();

			//the log window is now not closable, and does not perform exit
			LogS.setClosable(DGuitar.closableLogWindow, DGuitar.LogWindowExits);
			
			//sets the main window visible
			DG.setVisible(true);
			
			//if there are any files on the paramenter open them
			
			DG.displayMultipleFiles(DG.convertStringsArgToFileList(args));
			
		}
		
		else {
			//The log windows is allowed to be closed and exit
			aux = DGuitar.lang.getString("Screen_Size_should_be") + " ";
			aux += MIN_WIDTH + "x" + MIN_HEIGHT
			+ DGuitar.lang.getString("fix_this_an_try_again");
			LS.print(aux);
		}
		
	}
	/**
	 * this method detects the operational system and stores it on DGuitar.operationalSystem
	 *
	 */
	public static void detectOperationalSystem() {
		//Example of operational system properties 
		//os.arch=x86
		//os.name=Windows XP
		//os.version=5.1
		DGuitar.operationalSystem = System.getProperty("os.name") ;
		DGuitar.operationalSystem += " " + "version" + " " + System.getProperty("os.version") ;
		DGuitar.operationalSystem += " " + "architecture" + " " + System.getProperty("os.arch") ;
		//Print the operational system information
		System.err.print(lang.getString("OS") + ": " + DGuitar.operationalSystem) ;
		
	}
	/**
	 * this method checks for requirements:
	 * screen resolution is checked
	 * Java vendor and version
	 */
	public static boolean requirementsOK() {
		boolean success;
		
		success = true;
		
		screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		if (screenSize.width < MIN_WIDTH) {
			success = false;
		}
		if (screenSize.height < MIN_HEIGHT) {
			success = false;
		}
		//detect the operational System
		DGuitar.detectOperationalSystem() ;
		
		//correct java vendor and version ?
		DGuitar.correctJavaVersion();
		
		
		//TODO check for sound card
		//check for input device (mouse,keyboard,etc) ?
		//TODO check for printer ?
		return success;
	}
	
	/**
	 * @param lang
	 *            The lang to set.
	 */
	public static void setLang(PropertyResourceBundle lang) {
		DGuitar.lang = lang;
	}
	
	/**
	 * 
	 * @param parent -
	 *            the parent component
	 * @param title -
	 *            the title of the dialog
	 * @param message -
	 *            the String or Panel you want to show
	 * @param input -
	 *            if you want an input box or not
	 * @param init -
	 *            the initial value if you want input
	 * 
	 * @return a Dialog as you requested to be created
	 */
	public static Object showCustomInputDialog(Component parent, String title,
			Object message, boolean input, String init) {
		JOptionPane JOP;
		JDialog dialog;
		Object o;
		
		JOP = new JOptionPane(message);
		
		JOP.setWantsInput(input);
		if ((input) && (init != null)) {
			JOP.setInitialSelectionValue(init);
		}
		JOP.setOptionType(JOptionPane.OK_CANCEL_OPTION);
		
		dialog = JOP.createDialog(parent, title);
		dialog.setBounds(10, 100, 638, 140);
		dialog.setVisible(true);
		
		o = JOP.getInputValue();
		return o;
	}
	
	/**
	 * Returns a string like "DGuitar A.B.C" or "DGuitar (under testing) A.B.C"
	 */
	public static String version() {
		String str;
		
		str = "DGuitar";
		if (!isRelease) {
			//str += " (under testing - launched at )" + new
			// java.sql.Time(System.currentTimeMillis()) ;
			str += " (under testing)";
		}
		str += " " + DGuitar.version;
		
		return str;
	}
	
	/**
	 * Constructs a DGuitar main window using LS as the LosStream
	 */
	public DGuitar() {
		this.getContentPane().setLayout(new BorderLayout()) ;
		this.determineLaunchPath();
		
		this.DGpropertiesAbsolute = System.getProperty("user.home")
		+ File.separator + DGuitar.DGproperties;
		
		locale = Locale.getDefault();
		
		langPath = getLangPath();
        
		lang = loadLanguage(langPath, locale.getLanguage());
		
		authors = null;
		toolkit = this.getToolkit();
		
		fileChooser = null;
		this.hasPathChanged = true;
		this.worker = null;
        

        DGuitar.currentSkinIndex = 1 ;
        
		this.setTitle(DGuitar.version());
		
		this.files = new Vector<String>(0, 1);
		DGuitar.DGO = new Properties();
		
	}
	
	/**
	 * this lets any body knows that this program is released unde GNU/GPL
	 */
	private void aboutLicense() {
		/*
		 * this code compile...but cretes a GIANT dialog, unclosable
		 * if(STPLicense == null) { STPLicense = new ScrollTextPanel() ;
		 * STPLicense.displayFile("LICENSE.TXT") ; }
		 * JOptionPane.showMessageDialog(null, new
		 * javax.swing.JScrollPane(STPLicense),
		 * lang.getString("GNU/GPL_License,_version_2"),JOptionPane.INFORMATION_MESSAGE) ;
		 */
		showTextFile(lang.getString("GNU/GPL_License,_version_2"),
				"LICENSE.TXT", true, 550, 450);
		
	}
	

	/**
     * this method is use to adds the Midi Menu to the given MenuBar
     *  
     * @param MB    The menu bar were the menu will be added
	 */
     public void addMenuMidi(javax.swing.JMenuBar MB) {
        menuMidi = new javax.swing.JMenu();
        menuMidiTest = new javax.swing.JMenuItem();
        menuMidiDevices = new javax.swing.JMenuItem();
    
        menuMidiDevices.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMidiDevicesActionPerformed();
            }
        });
        
        menuMidi.add(menuMidiDevices);
        
        menuMidiTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuMidiTestActionPerformed();
            }
        });
        
        menuMidi.add(menuMidiTest);
        
        MB.add(menuMidi);
    }      
    /**
      * this method is use to adds the About Menu to the given MenuBar
      *  
      * @param MB    The menu bar were the menu will be added
     */

	public void addMenuAbout(javax.swing.JMenuBar MB) {
		menuAbout = new javax.swing.JMenu();
		menuAboutAuthors = new javax.swing.JMenuItem();
		menuAboutLicense = new javax.swing.JMenuItem();
		
		
		menuAboutAuthors.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutAuthors();
			}
		});

		menuAbout.add(menuAboutAuthors);
		
		menuAboutLicense.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				aboutLicense();
			}
		});
		
		menuAbout.add(menuAboutLicense);
		//first add a GLUE component so the about goes to the right
        MB.add(Box.createHorizontalGlue());

        //actually add the menu 
		MB.add(menuAbout);
	}
    

	/**
	 * The toolbars are added with this method
	 * moment
	 */
	

	 private void addToolBars() {
		 LS.print("adding ToolBars") ;
		 
         DGuitar.playToolBar = new PlayToolBar(DGuitar.Buttons,DGuitar.desktopPane,DGuitar.DisabledButtons) ;

	     //Add the toolbar to the the TOP of DGuitar main window
         DGuitar.playToolBar.setPlayable(DGuitar.prevPlayToolBarPlayable);
		 this.getContentPane().add(DGuitar.playToolBar,BorderLayout.PAGE_START) ;
		 this.pack() ;
	 
	 }
     
     /* LS.print("addToolBars") ; this.NoteToolBar = new ImageToolBar() ;
     * this.NoteToolBar.setFloatable(false) ;
     * this.NoteToolBar.setNumButtons(7) ;
     * this.NoteToolBar.setImages(this.Notes) ;
     * this.NoteToolBar.addActionListener(new NoteToolBarListener() );
     * this.toolBars.add(this.NoteToolBar) ; this.SilenceToolBar = new
     * ImageToolBar() ; this.SilenceToolBar.setFloatable(false) ;
     * this.SilenceToolBar.setNumButtons(7) ;
     * this.SilenceToolBar.setImageIcons(this.Silences) ;
     * this.SilenceToolBar.addActionListener(new SilenceToolBarListener() );
     * this.toolBars.add(this.SilenceToolBar) ;
     * 
     */
	 /**
	  * this method removes the toolbar usually to refresh the skin
	  */
     private void removeToolBars() {
         LS.print("removing ToolBars") ;
         
         DGuitar.prevPlayToolBarPlayable = DGuitar.playToolBar.getPlayable() ;
         this.getContentPane().remove(DGuitar.playToolBar) ;
         
         DGuitar.playToolBar = null ;
     }
	
	/**
	 * Tries to load the boolean key from the properties file
	 * 
	 * @param key
	 *            the name of the kay
	 * @param defaultValue
	 *            the value to return if key is not found
	 * @return a boolean value of the key or the default if key is not found
	 */
	private boolean booleanProperty(String key, String defaultValue) {
		String value;
		boolean resp;
		
		value = DGuitar.DGO.getProperty(key, defaultValue);
		resp = Boolean.valueOf(value).booleanValue();
		if (!DGuitar.DGO.containsKey(key)) {
			DGuitar.DGO.setProperty(key, value);
		}
		return resp;
	}
	private short shortProperty(String key, short defaultValue) {
		String value;
		short resp;
		
		value = DGuitar.DGO.getProperty(key, "" + defaultValue);
		resp = Integer.valueOf(value).shortValue() ;
		if (!DGuitar.DGO.containsKey(key)) {
			DGuitar.DGO.setProperty(key, value);
		}
		return resp;		
	}
	
	/**
	 * When the location specify by the file is incorrect this method is used
	 *  
	 */
	private void checkFileLocation() {
		File folder;
		boolean isNull;
		String filePathAux;
		
		isNull = (this.filePath == null);
		filePathAux = this.filePath.getAbsolutePath();
		folder = this.filePath;
		//
		if ((isNull) || (!this.filePath.exists())) {
			folder = new File(this.getFilePath());
			if (!folder.exists()) {
				folder = folder.getParentFile();
			}
			if (!isNull) {
				msg = "\'" + filePathAux + "\' "
				+ lang.getString("ERROR_DOES_NOT_EXIST");
				msg += "\n" + lang.getString("The_location_has_been_set_to_");
				msg += "\'";
				msg += folder;
				msg += "\'";
				showDialogOk(lang.getString("FileLocation_is_incorrect"), msg);
				hasOptionsChanged = true ;
			}
			
		}
		//check for folder permissions...and set to user.home if needed
		if ((!folder.canRead()) || (!folder.canWrite())) {
			folder = new File(this.userHome);
		}
		this.filePath = folder;
	}
	/**
	 * Closes all the windows, used when exiting and when user selects it from the Window menu
	 *
	 */
	public void closeAllWindows() {
		SongActionParameters SAP;
	
		//create a SongActionParameter object that closes the windows
		SAP = new SongActionParameters(SongActionParameters.CLOSE_DISPOSE);
		SongAction.perform(DGuitar.desktopPane, SAP);
        //after closing all the windows set the playToolBar to NOTHING_PLAYABLE
        playToolBar.setStatus(Playable.NOTHING_PLAYABLE) ;
        prevPlayToolBarPlayable = null ;
	}
	public void close() {
		
		
		if (worker != null) {
			worker.interrupt();
		}
		closeAllWindows() ;

		//if we were capturing errors, restore the original sysError handler
		if (captureErrors) {
			System.setErr(sysErr);
			LS.close();
		}
		//dispose this window...bye,bye
		this.dispose();
		//and perform an exit
		System.exit(0);
	}
	
	private void configureMenuOptionsLanguage() {
		ButtonGroup JBG;
		String ls[];
		JCheckBoxMenuItem JCBMI;
		Locale aLocale;
		int index;
		String language;
		String country;
		
        //get the available languages from the LANG folder
		ls = getLangAvailable();
        
        //create a new Button group to allow only one language to be selected
		JBG = new ButtonGroup();
		if (ls != null) {
			for (int i = 0; i < ls.length; i++) {
				//this is done to handle LANGUAGECODES_COUNTRYCODE "pt_BR" for
				// portuguese_BRazil
				index = ls[i].indexOf('_');
				if (index > 0) {
					language = ls[i].substring(0, index);
					country = ls[i].substring(index + 1, index + 3);
					aLocale = new Locale(language, country);
				} else {
					language = ls[i];
					aLocale = new Locale(language);
				}
				
				//DEBUG
				//System.err.println(aux.getDisplayLanguage()) ;
                
                //create a checkBoxItem with a friendly name for the language
				JCBMI = new JCheckBoxMenuItem(aLocale.getDisplayName());
				
                //if current language match the one stored on properties...                
                if(ls[i].equals(DGuitar.langCode)) { 
                    JCBMI.setSelected(true);
                }
                    /*
				if (ls[i].equals(DGuitar.locale.getLanguage())) {
                    //if the language is equal to the one detected by the LOCALE
					JCBMI.setSelected(true);

                    if(!ls[i].equals(DGuitar.langCode)) { 
                    //DGO.setProperty("lang",ls[i]) ;
                    
                    }
				}   
                */
				JCBMI.setActionCommand(ls[i]);
				JCBMI.addActionListener(new ActionListener() {
					
					public void actionPerformed(ActionEvent e) {
						
						Object comp;
						JCheckBoxMenuItem abc;
						
						comp = e.getSource();
						abc = (JCheckBoxMenuItem) comp;
                        DGuitar.langCode = abc.getActionCommand() ;
						lang = loadLanguage(langPath,DGuitar.langCode);
                        //NEW store the new language setting
                        DGO.setProperty("lang",DGuitar.langCode) ;
                        
						//refresh the string of the mainwindow
						setLangText();
						//refresh the string on other components
						refreshLangText();
						
						//the settings have been changed...so..
                        hasOptionsChanged = true ;
					}
					
				});
				JBG.add(JCBMI);
				menuOptionsLanguage.add(JCBMI);
			}
		}
	}
	
	/**
	 * Checks if the fileChooser exists, creates it if necesary and resets its
	 * values
	 *  
	 */
	protected void createFileChooser() {
		if (fileChooser == null) {
			//taken from http://forum.java.sun.com/thread.jspa?forumID=57&threadID=632724
			//only works on java 1.5 or later...
//TODO      how can this be done on earlier version ?
			UIManager.put("FileChooser.readOnly", Boolean.TRUE);
			
			fileChooser = new DGFileChooser(this.filePath);
			fileChooser.setAcceptAllFileFilterUsed(false);
		}
		//PieceInternal frame set this value to File.MID when exporting..
		//reset it here....

		fileChooser.setFileFilter(null);
		//erase the File filter, to avoid the history on the filters
		fileChooser.resetChoosableFileFilters();
		
		//to erase the current file name on the file Chooser
		fileChooser.setSelectedFile(new File(""));
	}
	
	/**
	 * this methods detects the supported extensions of DGuitar and fills the
	 * <code>java.util.List supportedExtensions</code> array.
	 *  
	 */
	public void detectSupportedExtensions() {
		this.supportedExtensions = new java.util.Vector<String>();
		this.supportedExtensions.addAll(DGuitar.gpFileFilter.getExtensions());
		this.supportedExtensions.addAll(DGuitar.midiFileFilter.getExtensions());
		Collections.sort(this.supportedExtensions);
		System.err.println(lang.getString("SupportedExtensions") + ":"
				+ this.supportedExtensions);
	}
	
	/**
	 * Returns true if the Filename is on of the supported extensions
	 * 
	 * @param aFileName
	 *            the fileName to check
	 */
	public boolean isSupportedExtension(String aFileName) {
		boolean resp;
		Iterator<String> iterator;
		String ext;
		
		resp = false;
		iterator = this.supportedExtensions.iterator();
		while ((!resp) && (iterator.hasNext())) {
			ext = iterator.next();
			resp = aFileName.toUpperCase().endsWith(ext);
		}
		return resp;
	}
	
	/**
	 * this method display a GP file
	 */
	void displayGP(String aFileName, byte[] bytes) {
		
		if (confirmLoad) {
			msg = lang.getString("File") + "\'" + aFileName + "\'\n";
			msg += lang.getString("Bytes") + ": " + bytes.length;
			showDialogOk(lang.getString("File_SUCCESSFULLY_Loaded"), msg);
		}
		this.readAndShow(aFileName, new ByteArrayInputStream(bytes));
	}
	
	/**
	 * this method display a MID file
	 */
	
	void displayMID(String aFileName) {
		MidiInternalFrame MIF;
		Midi newMidi;
		
		newMidi = new Midi(lang.getString("doesNotHaveAnyReceivers"));
		MIF = new MidiInternalFrame(aFileName, newMidi,DGuitar.playToolBar);
		internalFrameAdd(MIF);
	}
	
	/**
	 * this method iterates over the listOfFiles and opens them. if the type of
	 * file is not supported, and error message is printed
	 * 
	 * @param listOfFiles
	 *            a List that contains File objects
	 */
	public void displayMultipleFiles(List<File> listOfFiles) {
		Iterator<File> iterator;
		boolean checkExtension;
		File f;
		
		//DEBUG
		//System.out.println("displayMultipleFiles called !") ;
		if (listOfFiles != null) {
            if(listOfFiles.size() >= 1) {
                checkExtension = true;
                System.err.println("Opening files" + ":" + listOfFiles);
                iterator = listOfFiles.iterator();
                while (iterator.hasNext()) {
                    f = iterator.next();
                    
                    this.loadAndDisplay(f, checkExtension);
                }
            }
		}
	}
	
	/**
	 * Display and error message about loading a skins
	 */
	private void errorSkin() {
		String aux;
		
		//aux = lang.getString("ERROR_LOADING") + " \'" + currentSkin + "\' ";
        aux = lang.getString("ERROR_LOADING") + " \'" + nameForSkinIndex(currentSkinIndex) + "\' ";
		aux += lang.getString("skin");
		showDialogOk(aux, msg);
	}
	
	/**
	 * this methods loads a FILE, using a ProgressBar as necesary
	 */
	private void fileLoad(String file) {
		new FileLoaderHandler(this, file, evaluate).start();
	}
	
	/**
	 * this methods cloases any open Internal frame, and then exits
	 */
	private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
		this.close();
	}//GEN-LAST:event_formWindowClosing
	
	private String getFilePath() {
		return this.lauchPath + File.separator + "files";
	}
	
	/**
	 * @return Returns the lauchPath.
	 */
	public String getLaunchPath() {
		return lauchPath;
	}
	

	
	/**
	 * this method gets the AvailableLanguages from the LANG folder
	 * 
	 * @return a String array in the form of { "en", "es", "de", "fr", "pt_BR" }
	 */
	
	public String[] getLangAvailable() {
		/*
		 * String resp[] = { "en", "es", "de", "fr" } ;
		 */
		
		String aux[];
		String resp[];
		File langFolder;
		int index;
		int indexDot;
		int i;
		int j;
		
		langFolder = new File(this.getLangPath());
		//DEBUG
		//System.err.println(DGuitar.getLangPath());
		aux = langFolder.list(new FilenameFilterAcceptExtension(".properties"));
		resp = null;
		if ((aux != null) && (aux.length > 0)) {
			j = 0;
			//resp contains the language codes..without the extension or the
			// "lang.properties" file
			resp = new String[aux.length - 1];
			for (i = 0; i < aux.length; i++) {
				index = aux[i].indexOf('_');
				if (index > 0) {
					//                  "lang_de.properties" -----> "de"
					//                  "lang_pt_BR.properties" -----> "pt_BR" for portuguese
					// Brazil
					indexDot = aux[i].indexOf('.');
					resp[j] = aux[i].substring(index + 1, indexDot);
					j++;
				}
			}
		}
		return resp;
	}
	
	public String getLangPath() {
		String resp;
		
		resp = this.lauchPath; //which ends in "\" or in "/"
		
		resp += "lang";
		
		return resp;
	}
	
	public void init(LogStream LogS, boolean captureErrs) {
		
		LS = LogS;
		
		this.captureErrors = captureErrs;
		
		if (captureErrors) {
			//capture the Error stream to display it on the LOG window
			sysErr = System.err;
			System.setErr(LS);
		}
	}
	public short OStype() {
		short ostype ;
		
		ostype = OS_OTHER ;
		if(operationalSystem.indexOf("Mac") >= 0) {
			ostype = OS_MAC ;
		}
		else if(operationalSystem.indexOf("inux") >= 0) {
			ostype = OS_LINUX ;
		}
		else if(operationalSystem.indexOf("indows") >= 0) {
			ostype = OS_LINUX ;
		}
		else  {
			ostype = OS_OTHER ;
		}
		
		
		return ostype ;
	}
	
	
	public void load() throws FileNotFoundException {
		String iconName ;

		//initialize the File/Codec Filters
		DGuitar.gpFileFilter = new GPFileFilter();
		DGuitar.midiFileFilter = new MIDFileFilter();
		
		
		//detect the supported extensions
		this.detectSupportedExtensions();
		
		//add the drag and DROP support
		this.addDropSupport();
		
        //load the preferences
		this.preferencesLoad();

        /*------------------------*/
        //use the language setting or set it to ENGLISH if does not exist
        DGuitar.langCode = DGO.getProperty("lang","en") ;
        
        DGuitar.lang = loadLanguage(langPath,DGuitar.langCode) ;

        LS.log(DGuitar.lang.getString("applyPreferences"));
        
        /*------------------------*/
        
        //create the main menu
        this.menuCreate();
		
		//the preferences are applied on the components (menus)
		this.preferencesUse();
		
        //load the currentSking, and set the skinPath and skinDir variables
		this.loadCurrentSkin();
		
		if (!fastLoad) 
		{
			LS.log(lang.getString("Creating_JFileChooser"));
			createFileChooser();
		}
		
		//add the toolbars
		this.addToolBars() ;
		
//      if Operational System is MAC then
		OS = OStype() ;
		
		iconName = skinDir ;
		switch (OS) {
		
		case OS_MAC:
			//set the BIG ICON since is displayed on the DOCK
			iconName += "dguitar_32x32.png" ;
			break ;
		case OS_LINUX:
			iconName += "dguitar_32x32.png" ;
			break ;
		case OS_WIN:
			iconName += "dguitar_16x16.png" ;
			break ;
		default:
			iconName += "dguitar_16x16.png" ;
			break;
		}
         //DEBUG
         System.err.println("Loading ICON:" + iconName) ;
         
		//load the defined ICON
		imgIcon = loadImage(iconName) ;
		
		//set the ICON for the main window
		this.setIconImage(imgIcon) ;
		
		//set the ICON for the LOG window
		LS.setIconImage(imgIcon) ;
		
		//Maximized the main Window
		this.setExtendedState(MAXIMIZED_BOTH) ;
	}
	
	/**
	 * this method is used to add a internalFrame
	 */
	private void internalFrameAdd(JInternalFrame JIF) {
		String tit;
		
		tit = JIF.getTitle();
		OpenedListAdd(tit);
		
		try {
			JIF.setSelected(true);
		} catch (java.beans.PropertyVetoException pve) {
			showDialogOk("PropertyVetoException", lang
					.getString("ERROR_EXCEPTION")
					+ "\n" + pve.getStackTrace().toString());
		}
		
		JIF.addInternalFrameListener(new DGInternalFrameListener(this));
		JIF.setFrameIcon(Clefs[1]);
		desktopPane.add(JIF);
		internalFrameLocate(JIF);
		//dgc.setCursorDGP(this.currentCursor) ;
		JIF.setVisible(true);
	}
	
	/**
	 * this methods loads the Current skin or the defualt if current skin fails
	 */
	private void loadCurrentSkin() {
		boolean success;
        String currentSkin ;

        //store the name of the current skin 
        
        currentSkin = nameForSkinIndex(currentSkinIndex) ;

		LS.log(lang.getString("LoadCurrentSkin") + ":" + currentSkin);
        
        success = this.loadSkin(currentSkin);

        //if was not successfull to load the current skin
        if (!success) {
            //and the current skin is not the default
			if (!currentSkin.equalsIgnoreCase("default")) {
                //display message with previous skin
				errorSkin();
                
                //load the default
				success = this.loadSkin(currentSkin);
			}
		}
		if (!success) {
			errorSkin();
			System.exit(1);
		}
	}
	
	/**
	 * Loads a Image from the given path, returns null if unsuccessfull
	 */
	private Image loadImage(String path) {
		File file;
		boolean success;
		Image img;
		
		img = null;
		file = new File(path);
		success = Util.validFile(file);
		if (success) {
			img = toolkit.getImage(path);
		} else {
			showDialogOk(lang.getString("File_is_not_valid"), Util.getError());
		}
		return img;
	}
	
	/**
	 * this method loadImages into a Image array
	 */
	
	private boolean loadImages(String basePath, String baseName, int from,
			int to, Image img[]) {
		boolean success;
		int i;
		String currentPath;
		
		success = true;
		for (i = from; (i <= to) && (success); i++) {
			currentPath = basePath + baseName + i + ".gif";
			img[i - 1] = loadImage(currentPath);
			success = (img[i - 1] != null);
		}
		return success;
	}
	
	/**
	 * this method loadImages into a ImageIcon array
	 */
	private boolean loadImages(String basePath, String baseName, int from,
			int to, ImageIcon ii[]) {
		boolean success;
		int i;
		String currentPath;
		Image image;
		
		success = true;
		for (i = from; (i <= to) && (success); i++) {
			currentPath = basePath + baseName + i + ".gif";
			image = loadImage(currentPath);
			success = (image != null);
			if (success) {
				ii[i - 1] = new ImageIcon(image);
			}
		}
		
		return success;
	}
	
	private PropertyResourceBundle loadLanguage(String pathToLangFolder, String language) {
		PropertyResourceBundle resp;
		String path ;
        
        path = pathToLangFolder + File.separator + "lang" ; //al the language files start with "lang"
		resp = null;
		resp = loadPropertyResourceBundle(path + "_" + language);
		if (resp == null) {
			//try to load in ENGLISH
			resp = loadPropertyResourceBundle(path + "_" + "en");
			if (resp == null) {
				//try to load de default STRINGS
				resp = loadPropertyResourceBundle(path);
				if (resp == null) {
					showDialogOk("This ERROR happend", path + " " + "NOT FOUND");
				}
			}
		}
		return resp;
	}
	
	/**
	 * load a ResourceBungle for the I18N
	 */
	private PropertyResourceBundle loadPropertyResourceBundle(String path) {
		PropertyResourceBundle resp;
		FileInputStream f;
		
		resp = null;
		try {
			f = new FileInputStream(path + ".properties");
			resp = new PropertyResourceBundle(f);
		} catch (FileNotFoundException FNF) {
			//showDialogOk( path + " not found", FNF.getLocalizedMessage() ) ;
		} catch (IOException IOE) {
			//showDialogOk("This EXCEPTION happend", IOE.getLocalizedMessage()
			// ) ;
		}
		
		return resp;
	}
	
	/**
	 * this method loads the skin nameOfSkin
	 */
	public boolean loadSkin(String nameOfSkin) {
		boolean success;
		Image img;
		
		success = false;
		skinDir = lauchPath + "skins" + File.separator;
		//try to Load the logo
		img = loadImage(skinDir + "DGuitarLogo.gif");
		if (img != null) {
			Logo = new ImageIcon(img);
		} else {
			msg = "DGuitarLogo.gif" + lang.getString("ERROR_WAS_NOT_FOUND");
			System.err.println(msg);
		}
		
		//now set the skinPath to the location of the SKIN
		skinPath = skinDir + nameOfSkin + File.separator;
		
		/* Check that the path of the SKINS exist */
		success = new File(skinPath).isDirectory();
		if (success) {
			LS.logNoNewLine("Loading" + ": ");
			
			//Load the 7 notes for the MidiForm object
			LS.logNoNewLine("Notes");
			loadImages(skinPath, "n", 1, 8, Notes);
			
			//load the 2 cleffs (claves)
			LS.logNoNewLine("," + "Clefs");
			loadImages(skinPath, "c", 1, 2, Clefs);
			
			// LOADS THE 7 SILENCES, for buttons and diplaying them on bars
			LS.logNoNewLine("," + "Silences");
			loadImages(skinPath, "s", 1, 7, Silences);
			
			// LOADS THE 8 DYNAMICS
			LS.logNoNewLine("," + "Dynamics");
			loadImages(skinPath, "d", 1, 8, Dynamics);
			
			//LOAD as many EFFECTS ON NOTE
			LS.logNoNewLine("," + "Effects");
			loadImages(skinPath, "en", 1, EONs.length, EONs);
			
			//load the miscenalous buttons (play,stop...etc)
			LS.log("," + "Buttons");
			loadImages(skinPath, "b", 1, 2, Buttons);
		} else {
			msg = lang.getString("The_path") + " \'" + skinPath + "\' "
			+ lang.getString("ERROR_WAS_NOT_FOUND");
		}
		
        //select in the menu the currently selected skin
        if(success) {
        	setSkinCheckBoxes() ;
        }
		return (success);
	}
	
	//Switches between different types of loggin
	private void switchLog() throws FileNotFoundException {
		short type ;
		boolean window ;
		
		window = menuOptionsLogToWindow.isSelected() ;
		if(menuOptionsLogToFile.isSelected()) {
			if(window) {
				type = LogStreamOptions.LOG_TO_FILE_AND_WINDOW ;
			}
			else {
				type = LogStreamOptions.LOG_TO_FILE_ONLY ;
			}
		}
		else {
			if(window) {
				type = LogStreamOptions.LOG_TO_WINDOW_ONLY ;
			}
			else {
				type = LogStreamOptions.NO_LOG ;
			}
		}
		DGuitar.LS.switchTo(type,DGuitar.lang.getString("DGUITAR_LOG_WINDOW"),DGuitar.closableLogWindow,DGuitar.LogWindowExits) ;
		DGuitar.DGO.setProperty("typeOfLog","" + DGuitar.LS.getTypeOfLog()) ;
		DGuitar.hasOptionsChanged = true ;
	}
	/**
	 * Returns a String with the skin name given its index
	 * @param index	the index to check
	 * @return	a String with the skin name or null if index is out of bounds
	 */
	public static String nameForSkinIndex(short index) {
		String resp ;
		
		resp = null ;
		
		//if index is valid
		if( (index >= 0) && (index < DGuitar.arraySkinNames.length ) ) {
			resp = DGuitar.arraySkinNames[index] ;
		}
		//else resp stays null
		
		return resp  ;
	}
	/**
	 * this method performs the commands necesary for skin change to happen automaticly
	 *
	 */
	private boolean performSkinChange(String newSkin) {
		boolean resp ;
		
		resp = false ;
		
		//if the parameter newSkin is not null and is different than the current skin
		if( (newSkin != null) && (!newSkin.equals(nameForSkinIndex(currentSkinIndex))) ) {
			DGuitar.currentSkinIndex = getIndexForSkinName(newSkin) ;
			
			DGO.setProperty("skin",newSkin);
			
			loadSkin(newSkin) ;
			
			//more methods will probably be added here to make sure 
			//all buttons and skinable objects are updated
			//BEGIN
			removeToolBars() ;
			addToolBars() ;
			
			//FIXME why is the window being resized ?? 
			//seems like the this.pack method from addToolBars is causing this
			//but if the method is not used..then the toolbar will not be located were expected
			
			this.setExtendedState(MAXIMIZED_BOTH) ;
			
			//END
			
			//since this skin was changed
			DGuitar.hasOptionsChanged = true ;
			resp = true ;
		}
		return resp ;
	}

    private void detectSkins(JMenuItem jm) {
        short countSkinFolders ;
        String tempSkin ; 
        
        if(DGuitar.buttonGroupSkin == null) {
            DGuitar.buttonGroupSkin = new ButtonGroup() ;            
        }
        //count and detect the available folders 
        countSkinFolders = 0 ;
        
        //TODO check that each folder contains the minimum files
        tempSkin = null ;
        
        while(countSkinFolders < 2) {
            if(countSkinFolders == 0) {
                tempSkin = new String("default") ;
            }
            else if (countSkinFolders == 1){
                tempSkin = new String("compact") ;
            }
            
            DGuitar.arraySkinNames[countSkinFolders] = tempSkin ;
            //allocate space on the arraySkin to store the item
            DGuitar.arraySkinCheckBoxes[countSkinFolders] = new JCheckBoxMenuItem(nameForSkinIndex(countSkinFolders)) ;
            
            //add a action listener to change from one skin to the other
            DGuitar.arraySkinCheckBoxes[countSkinFolders].addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent event) {
                    JCheckBoxMenuItem JCBMI ;
                    String aux ;
                    
                    JCBMI = (JCheckBoxMenuItem) event.getSource() ;
                    aux = JCBMI.getText() ;
                    
                    //actually perform the skin change
                    performSkinChange(aux) ;

                }
                
            });
            
            //add it to the buttonGroup
            DGuitar.buttonGroupSkin.add(DGuitar.arraySkinCheckBoxes[countSkinFolders]);
            
            jm.add(DGuitar.arraySkinCheckBoxes[countSkinFolders]) ;
        
            countSkinFolders++;
        }
        

        
     /*   jm.add( ) ;
        
        DGuitar.buttonGroupSkin.add(JCBMI);
        */
    }
    private void setSkinCheckBoxes() {
    	arraySkinCheckBoxes[currentSkinIndex].setSelected(true) ;
    	
    }
	/**
	 * Set the log CheckBoxes according to the <code>DGuitar.typeOfLog<code>
	 *
	 */
	private void setLogCheckBoxes() {
		boolean logToWindow ;
		boolean logToFile ;
		
		logToWindow = logToFile = false ;
		
		if(DGuitar.LS.isFileType()) {
			logToFile = true ;
		}
		if(DGuitar.LS.isWindowType()) {
			logToWindow = true ;
		}
		this.menuOptionsLogToWindow.setSelected(logToWindow) ;
		this.menuOptionsLogToFile.setSelected(logToFile) ;
	}
	
	private void menuCreate() {
		//Create the desktop pane
		
		desktopPane = new DGDesktopPane();
		
		//create the menus
		menu = new JMenuBar();
		
		menuProgram = new JMenu();
		menuProgramOpenGP = new JMenuItem();
		menuProgramOpenMidi = new JMenuItem();
		menuProgramOpenURL = new JMenuItem();
		menuProgramNew = new JMenuItem();
		
		menuProgramStats = new JMenuItem();
		
		menuProgramExit = new JMenuItem();
		
		menuOptions = new JMenu();
		menuOptionsFileLocation = new JMenuItem();
		menuOptionsConfirmLoad = new JCheckBoxMenuItem();
		menuOptionsEvaluate = new JCheckBoxMenuItem();
		menuOptionsFastLoad = new JCheckBoxMenuItem();
		
		menuOptionsLanguage = new JMenu();
		
		//menu options for type of log
		this.menuOptionsLog = new JMenu();
		
		this.menuOptionsLogToFile = new JCheckBoxMenuItem() ;
		this.menuOptionsLogToWindow = new JCheckBoxMenuItem() ;
		
		setLogCheckBoxes() ;
		//enable unfinished features
		menuOptionsEnableUnfinishedFeatures = new JCheckBoxMenuItem() ;
		
        
        menuOptionsSkins = new JMenu() ;
        
		menuOptionsSaveCurrentSettings = new JMenuItem();
		
		menuOptionsReloadStoredSettings = new JMenuItem();
		
		menuWindows = new JMenu();
		
		setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});
		//Testing DGDesktop by adding the posible window operations (cascade,tile)
		menuWindowsOperationsCount = 0 ;
		
		//add a separator --------------------------
		menuWindows.add(new JSeparator()) ;
		menuWindowsOperationsCount++ ;

		//seting an action listener to the cascade option
		
		//create the Cascade option
		menuWindowsCascade = new JMenuItem()  ;
		
		//set the action listener for Cascade
		menuWindowsCascade.addActionListener( new ActionListener () {

			public void actionPerformed(ActionEvent e) {
				desktopPane.arrangeWindows(DGDesktopPane.CASCADE_WINDOWS,desktopPane.getAllFrames()) ;
				
			}
			
		});
		
		

		//add the Cascade option
		menuWindows.add(menuWindowsCascade) ;
		menuWindowsOperationsCount++;
		
		//create the CLOSE all WINDOWS option
		menuWindowsCloseAll = new JMenuItem() ;
		
		menuWindowsCloseAll.addActionListener(new ActionListener () {

			public void actionPerformed(ActionEvent e) {
				closeAllWindows() ;
				
			}
			
		});
		
		//add a separator --------------------------
		menuWindows.add(new JSeparator()) ;
		menuWindowsOperationsCount++ ;
		
		//add the CLOSE ALL option
		menuWindows.add(menuWindowsCloseAll) ;
		menuWindowsOperationsCount++;
		
		//add the DesktopPane in the CENTER
		getContentPane().add(desktopPane, java.awt.BorderLayout.CENTER);
		
		menu.setBorder(null);

		menuProgramOpenGP
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuOpenGPActionPerformed(evt);
			}
		});
		
		menuProgram.add(menuProgramOpenGP);
		
		menuProgramOpenMidi
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuOpenMidiActionPerformed(evt);
			}
		});
		
		menuProgram.add(menuProgramOpenMidi);
		menuProgramOpenURL
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuOpenURLActionPerformed(evt);
			}
		});
		
		this.menuProgram.add(menuProgramOpenURL);
		
		//MENU PROGRAM NEW..not used yet
		/*
		 * menuProgramNew.addActionListener(new java.awt.event.ActionListener() {
		 * public void actionPerformed(java.awt.event.ActionEvent evt) {
		 * menuProgramNewActionPerformed(evt); } });
		 * 
		 * this.menuProgram.add(menuProgramNew);
		 */
		menuProgram.add(new JSeparator());
		
		menuProgramStats.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuStatsActionPerformed(evt);
			}
		});
		
		menuProgram.add(menuProgramStats);
		
		menuProgram.add(new JSeparator());
		
		menuProgramExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuExitActionPerformed(evt);
			}
		});
		
		menuProgram.add(menuProgramExit);
		
		menu.add(menuProgram);
		
		menuOptionsFileLocation
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuOptionsFileLocationActionPerformed(evt);
			}
		});
		
		menuOptions.add(menuOptionsFileLocation);
		
		menuOptionsConfirmLoad
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuOptionsConfirmLoadActionPerformed(evt);
			}
		});
		
		menuOptions.add(menuOptionsConfirmLoad);
		
		menuOptionsEvaluate
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuOptionsEvaluateActionPerformed(evt);
			}
		});
		
		menuOptions.add(menuOptionsEvaluate);
		
		menuOptionsFastLoad
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				menuOptionsFastLoadActionPerformed(evt);
			}
		});
		
		menuOptions.add(menuOptionsFastLoad);
		
		//LOG options
		menuOptionsLogToFile.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					switchLog() ;
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		});
		
		menuOptionsLog.add(menuOptionsLogToFile) ;
		
		menuOptionsLogToWindow.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					switchLog() ;
				} catch (FileNotFoundException e) {
					
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}
			}
		});
		menuOptionsLog.add(menuOptionsLogToWindow) ;
		
		menuOptions.add(menuOptionsLog);
		
		
		//MENU OPTIONS FOR ALL SONGS
		this.displayOptions = new DisplayOptions();
		SongOptionsListener SOL;
		
        
//      detect and add skins
        detectSkins(menuOptionsSkins) ;
        
        menuOptions.add(menuOptionsSkins);
        
		menuOptionsForAllSongs = new SongOptionsMenu("menuDGOptionsForAllSongs");
		
		//set the DisplayOption value int the menus
		menuOptionsForAllSongs.setDisplayOptions(this.displayOptions);
		
		//"this, as JFRAME and "this" as and OptionsDisplay object...only used here
        //TODO FIX this and make sure second parameter is being used
		SOL = new SongOptionsListener(this, this);
		menuOptionsForAllSongs.setMenuIndexedListener(SOL);
		menuOptions.add(menuOptionsForAllSongs);
		
		//--------------------------
		menuOptions.add(new JSeparator());
		
		
		
		//SAVE CURRENT SETTINGS
		menuOptionsSaveCurrentSettings
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				preferencesStore();
				DGuitar.hasOptionsChanged = false ;
			}
		});
		
		menuOptions.add(menuOptionsSaveCurrentSettings);
		
		//LOAD STORED SETTINGS
		menuOptionsReloadStoredSettings
		.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				try {
					preferencesApply();
				} catch (FileNotFoundException e) {
					//e.printStackTrace();
				}
				DGuitar.hasOptionsChanged = false ;
			}
		});
		menuOptions.add(menuOptionsReloadStoredSettings);
		
		menuOptions.add(new JSeparator());
		//LANGUAGES
		configureMenuOptionsLanguage();
		menuOptions.add(menuOptionsLanguage);

        menuOptions.add(new JSeparator());
        //TODO this WILL DISAPPER..SOME DAY...since everything will be finished !!
        menuOptionsEnableUnfinishedFeatures.setSelected(DGuitar.EnableUnfinishedFeatures) ;
        
        menuOptionsEnableUnfinishedFeatures.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //swith from true to false..and back
                DGuitar.EnableUnfinishedFeatures = !DGuitar.EnableUnfinishedFeatures ;
                DGuitar.setEnableUnfinishedFeatures(DGuitar.EnableUnfinishedFeatures) ;
                DGuitar.DGO.setProperty("EnableUnfinishedFeatures", "" + EnableUnfinishedFeatures);
                DGuitar.hasOptionsChanged = true ;
            }
        });
        
        menuOptions.add(menuOptionsEnableUnfinishedFeatures) ;

		
		menu.add(menuOptions);
		

		
		//attach the bar to this Frame (using the java API)
		setJMenuBar(menu);

		menuWindows.setEnabled(false);
		
        addMenuMidi(this.menu);
		menu.add(menuWindows);
        
        addMenuAbout(this.menu);
		
		

		//INTERNATINALIZE menus here
		this.setLangText();
		
		pack();
	}
	/**
	 * The action that is performed when the program exits
	 * @param evt
	 */
	private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {
		int result ;
		
		result = JOptionPane.OK_OPTION ;
		if(DGuitar.hasOptionsChanged) {
			//Display a message if asking user if he wants to save settings
			result = JOptionPane.showConfirmDialog(this,DGuitar.lang.getString("msgSaveCurrentPreferences" ));
		}	
		if(result == JOptionPane.OK_OPTION) {
			preferencesStore() ;
		}
		if(result != JOptionPane.CANCEL_OPTION)
		{
			this.close();
		}
	}
    /**
     * Sets up the DGuitar.midi variable 
     * and displays a dialog depending on the parameter
     * @param showDialog    pass TRUE to see a dialog after success
     */
    private void setupMidi(boolean showDialog) {
        if (DGuitar.midi == null) {
            midi = new Midi(lang.getString("doesNotHaveAnyReceivers"));
            midi.selectFirstAvailableMidiDevice();
            if(showDialog) {
                Util.showDialogOk(this,lang.getString("currentMidiDeviceHasHeenConfigured"),midi.deviceMessage(lang.getString("MidiDevice"),lang.getString("isNowPreferredDevice") ) ) ;
            }
        }
    }
	
	/**
	 * this method creates and displays a MidiDevices form
	 */
	private void menuMidiDevicesActionPerformed() {
		MidiDevices MD;
		
        setupMidi(true) ;
		if (midi != null) {
			//MidiDevices(java.awt.Frame parent, boolean modal, Midi m, String mesage)
            //FIXME the first time this message appear in english..then in the correct lanaguage
			MD = new MidiDevices(null, true, midi,lang.getString("midiDeviceConfigured"));
			MD.setVisible(true);
			
		} else {
			showDialogOk(lang.getString("ERROR_MIDI_INIT"), lang
					.getString("Take_a_look_a_the_DGuitar_log_window"));
		}
	}
	
	/**
	 * this method creates and displays a MidiTest form
	 */
	private void menuMidiTestActionPerformed() {
		MidiTest MT;
		String aux ;
		short attempts ;
		
        setupMidi(true) ;

        if (midi != null) {
        	//parent,modal or not 
			MT = new MidiTest(this, true) ;
			
			attempts = MT.createMidiTestWindow( midi, DGuitar.Notes,DGuitar.Buttons[0]);
			if (attempts >= 0) {
				MT.setVisible(true);
			}
			else {
				attempts = (short)(-1*attempts) ;
                aux =  "Devices found:" + " " + attempts + "\n"  ;
                aux += "Devices with receivers:" + "0" ;
                common.Util.showDialogOk(this, "Midi support failed for this computer",aux);
                midi.selectNextAvailableMidiDevice() ;
			}
		} else {
			showDialogOk(lang.getString("ERROR_MIDI_INIT"), lang
					.getString("Take_a_look_a_the_DGuitar_log_window"));
		}
		
	}
	
	private void menuOpenGPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenGPActionPerformed
		this.currentFileType = FileLoaderHandler.GUITAR_PRO;
		this.showFileChooserAndOpen();
	}//GEN-LAST:event_menuOpenGPActionPerformed
	
	/**
	 * When a MID is about to be open, isGuitarPro is set to false
	 */
	private void menuOpenMidiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenMidiActionPerformed
		this.currentFileType = FileLoaderHandler.MIDI;
		this.showFileChooserAndOpen();
	}//GEN-LAST:event_menuOpenMidiActionPerformed



    //PRATIMA's CHANGE
    private void menuOpenRecentFilesActionPerformed(java.awt.event.ActionEvent evt) {
        fileLoad(evt.getActionCommand());
    }

	/**
	 * 
	 * Opens a URL (http) direclty
	 * 
	 * @param evt
	 */
	private void menuOpenURLActionPerformed(java.awt.event.ActionEvent evt) {
		String pathIni;
		String path;
		String aux;
		String host;
		
		InputStream IS;
		URL url;
		Object o;
		
		path = null;
		
		//"http://www.mysongbook.com/files/-/-1691/Soda%20Stereo%20-%20Te%20Hacen%20Falta%20Vitaminas.gp4";
		//"http://www.mysongbook.com/files/c/collins/Collins%2C%20Phil%20-%20Another%20Day%20In%20Paradise.gp4";
		pathIni = "http://www.mysongbook.com/files/c/clapt/Clapton%2C%20Eric%20-%20Tears%20In%20Heaven%20%282%29.gp4";
		
		o = DGuitar.showCustomInputDialog(this, lang
				.getString("Open_GP_file_from_the_Internet"), lang
				.getString("Paste_Type_URL"), true, pathIni);
		
		if (o.getClass().isInstance(pathIni)) {
			path = (String) o;
		} else {
			LS.println(o.toString() + " is a " + o.getClass().toString());
		}
		
		if ((path != null) && (!o.equals(JOptionPane.UNINITIALIZED_VALUE))) {
			//TODO test https, other protocols...FTP
			if (path.startsWith("http://")
					&& (DGuitar.gpFileFilter.accept(path))) {
				IS = null;
				host = null;
				
				//Try to open a HTTP connection
				try {
					LS.println("Trying to open: " + path);
					url = new URL(path);
					host = url.getHost();
					IS = url.openStream();
					
					this.readAndShow(path, IS);
					
				}
				//the URL is not types correctly
				catch (MalformedURLException MUE) {
					Util.showDialogOk(this, "Malformed URL" + host, MUE
							.getLocalizedMessage());
					//DEBUG MUE.printStackTrace() ;
				}
				//not connected to internet or a DNS problem
				catch (java.net.UnknownHostException UHE) {
					Util
					.showDialogOk(
							this,
							"Unknown host",
							host
							+ " : was not found. Check address, connect to the internet or check DNS settings\n");
					//DEBUG UHE.printStackTrace() ;
				} catch (IOException IOE) {
					IOE.printStackTrace();
				} finally {
					Util.closeIS(IS);
				}
			} else {
				aux = "\"" + path + "\"";
				aux += " "
					+ "must start with \"http://\" and end with \".gp4\",\".gp3\" or \".gtp\"";
				showDialogOk("please type a correct URL", aux);
			}
		}
	}
	
	/**
	 * Does the user want to confirm file loads ?
	 */
	private void menuOptionsConfirmLoadActionPerformed(
			java.awt.event.ActionEvent evt) {
		this.confirmLoad = !this.confirmLoad;
		DGuitar.DGO.setProperty("ConfirmLoad", "" + this.confirmLoad);
		DGuitar.hasOptionsChanged = true ;
	}
	
	/**
	 * do you want to evaluate the performance of the program ?
	 */
	private void menuOptionsEvaluateActionPerformed(
			java.awt.event.ActionEvent evt) {
		this.evaluate = !this.evaluate;
		DGuitar.DGO.setProperty("EvaluatePerformance", "" + this.evaluate);
		DGuitar.hasOptionsChanged = true ;
	}
	
	private void menuOptionsFastLoadActionPerformed(
			java.awt.event.ActionEvent evt) {
		DGuitar.fastLoad = !DGuitar.fastLoad;
		DGuitar.DGO.setProperty("FastLoad", "" + DGuitar.fastLoad);
		DGuitar.hasOptionsChanged = true ;
	}
	
	/**
	 * this methods allow the user to set the location of the files
	 */
	private void menuOptionsFileLocationActionPerformed(
			java.awt.event.ActionEvent evt) {
		int val;
		File aux;
		
		createFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setApproveButtonText(lang.getString("Select"));
		val = fileChooser.showOpenDialog(this);
		if (val == JFileChooser.CANCEL_OPTION)
			return;
		//ELSE
		if (val == JFileChooser.ERROR_OPTION)
			return; //replace this by something more helpfull
		//ELSE
		aux = fileChooser.getSelectedFile();
		if (!this.filePath.getAbsoluteFile().equals(aux.getAbsoluteFile())) {
			this.filePath = aux;
			this.hasPathChanged = true;
			this.setTitle(shortTitle());
			DGO.setProperty("FileLocation", this.filePath.getAbsolutePath());
			DGuitar.hasOptionsChanged = true ;
		}
	}
	
	/**
	 * this method must be called when the values are changed direclty without
	 * user intervention
	 *  
	 */
	private void menuOptionsValuesUpdate() {
		menuOptionsConfirmLoad.setSelected(this.confirmLoad);
		menuOptionsEvaluate.setSelected(this.evaluate);
		menuOptionsFastLoad.setSelected(DGuitar.fastLoad);
		menuOptionsEnableUnfinishedFeatures.setSelected(DGuitar.EnableUnfinishedFeatures) ;
		this.setLogCheckBoxes() ;
		this.setSkinCheckBoxes() ;
		this.setTitle(shortTitle());
	}
	
	/**
	 * When the statistics options is selected, display them
	 */
	private void menuStatsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuStatsActionPerformed
		final GPStats GS;
		
		if (STstats == null) {
			STstats = new ScrollText();
			STstats.setEditable(false);
			STstats.setClosable(true, false);
		}
		if (this.hasPathChanged) {
			if (worker != null) {
				worker.interrupt();
			}
			STstats.setText(lang.getString("pleaseWait") + "....");
			STstats.setTitle(lang.getString("Statistics") + "-"
					+ this.filePath.getAbsolutePath());
			
			GS = new GPStats();
			final FolderIterator FI = new FolderIterator(GS);
			GS.setDetailed(false);
			FI.setStartingPath(filePath);
			FI.setFileFolderFilter(new FileFolderFilter(DGuitar.gpFileFilter));
			FI.setHowDeep(0);
			
			worker = new SwingWorker() {
				public Object construct() {
					String aux;
					
					aux = menuProgramStats.getText();
					menuProgramStats.setEnabled(false);
					menuProgramStats.setText(lang.getString("pleaseWait"));
					
					FI.run();
					
					STstats.setText(GS.getResults());
					
					
					menuProgramStats.setText(aux);
					menuProgramStats.setEnabled(true);
					return (null);
				}
			};
			worker.start();
			this.hasPathChanged = false;
		}
		
		STstats.setVisible(true);
		
	}//GEN-LAST:event_menuStatsActionPerformed
	
	/**
	 * Add a String to the Window Menu, and set the ActionListener
	 */
	private void OpenedListAdd(String file) {
		JMenuItem tempItem;
		
		//if its the first internal frame to be added
		if (!this.menuWindows.isEnabled()) {
			//..activate the window menu ;
			this.menuWindows.setEnabled(true);
			
			//TESTING DGDesktop
			
		}
        //TODO allow the user to change this value
		tempItem = new javax.swing.JMenuItem(Util.compactAndReadableURL(file,30));

		tempItem.setActionCommand(file);
		tempItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JInternalFrame JIF;
				int i;
				String cmd;
				int max;
				boolean found;
				
				cmd = e.getActionCommand();
				max = desktopPane.getComponentCount();
				found = false;
				for (i = 0; (!found) && (i < max); i++) {
					JIF = desktopPane.getJIF(i);
					if (JIF != null) {
						if (JIF.getTitle().equals(cmd)) {
							found = true;
							
							try {
								if (JIF.isIcon()) {
									JIF.setIcon(false);
								}
								JIF.setSelected(true);
							} catch (java.beans.PropertyVetoException pve) {
								showDialogOk(lang.getString("ERROR_EXCEPTION"),
										pve.getStackTrace().toString());
							}
						}
					} else {
						showDialogOk(
								lang
								.getString("Take_a_look_a_the_DGuitar_log_window"),
								msg);
					}
				}
			}
		});
		
		//add the name of the window before the window operations
		this.menuWindows.add(tempItem,this.menuWindows.getItemCount()-this.menuWindowsOperationsCount);
		
		//validate the menu to include the added item
		this.menuWindows.validate();
		this.files.addElement(file);
	}
	
	/**
	 * returns true if the FILE is on the list
	 */
	
	boolean OpenedListContains(String file) {
		boolean resp;
		
		resp = this.files.contains(file);
		return (resp);
	}
	
	/**
	 * Remove a string from the Windows menu
	 */
	public void OpenedListRemove(String file) {
		int pos;
		
		pos = this.files.indexOf(file);
		if (pos >= 0) {
			this.menuWindows.remove(pos);
			this.menuWindows.validate();
			this.files.removeElement(file);
			if (this.files.size() == 0) {
				this.menuWindows.setEnabled(false);
			}
		}
	}
	
	/**
	 * this method apply the preferences loaded from the file or the default
	 * @throws FileNotFoundException 
	 */
	private void preferencesApply() throws FileNotFoundException {
		DGuitar.DGO = new Properties();
		LS.log(DGuitar.lang.getString("applyPreferences"));
		
		this.preferencesLoad();
		this.preferencesUse();
	}
	
	/**
	 * this method tries to load the preferences file
	 * 
	 * @return true if the file was loaded.
	 */
	private boolean preferencesLoad() {
		FileInputStream FIS;
		boolean success;
		
		success = false;
		
		try {
			//try to load the preferences
			FIS = new FileInputStream(DGpropertiesAbsolute);
			DGuitar.DGO.load(FIS);
			success = true;
			DGuitar.LS.print("\'" + DGpropertiesAbsolute + "\' "
					+ lang.getString("File_SUCCESSFULLY_Loaded"));
		} catch (FileNotFoundException e) {
			// let the success variable stay as false
		} catch (IOException e) {
			// let the success variable stay as false
		}
		
		return success;
	}
	
	/**
	 * this method tries to save the preferences to a file
	 * 
	 * @return true if the file was stored.
	 */
	private boolean preferencesStore() {
		FileOutputStream FOS;
		boolean success;
		File f;
		String aux;
		
		success = false;
		try {
			//try to store the preferences
			f = new File(DGpropertiesAbsolute);
			//if the file does not exist
			if (!f.exists()) {
				//create it.
				f.createNewFile();
			}
			FOS = new FileOutputStream(f);
			aux = DGuitar.version() + " Properties file, dont delete any lines";
			DGuitar.DGO.store(FOS, aux);
			success = true;
			DGuitar.LS.print(f.getAbsolutePath() + " "
					+ lang.getString("File_SUCCESSFULLY_Saved"));
		} catch (FileNotFoundException e) {
			// let the success variable stay as false
		} catch (IOException e) {
			// let the success variable stay as false
		}
		//DEBUG
		//System.out.println("store preferrences was successfull ?"+ success) ;
		
		return success;
	}
	/**
	 * Returns 
	 * @param skn the name of the skin to look for
	 * @return -1 when skinName does not exist
	 */
	private static short getIndexForSkinName(String skn) {
		short resp ;
		boolean found ;
		
		resp = (short) (arraySkinNames.length -1) ;
		found = false ;
		
		//repeat until is FOUND or ARRAY begining is reached
		while (!found && (resp >= 0) ) {
			if(!nameForSkinIndex(resp).equals(skn)) {
				resp-- ;
			}
			else { 
				found = true ;
			}
		}
		return resp ;
	}
	
	/**
	 * reads the Keys from the Properties file and set the related variables or
	 * the default ones
	 * @throws FileNotFoundException 
	 *  
	 */
	private void preferencesUse() throws FileNotFoundException {
		String value;
		String pathFiles;
		String sknName ;
		
		pathFiles = this.getFilePath();
		value = DGuitar.DGO.getProperty("FileLocation", pathFiles);
		this.filePath = new File(value);
		checkFileLocation();
		DGO.setProperty("FileLocation", this.filePath.getAbsolutePath());
		
		this.confirmLoad = booleanProperty("ConfirmLoad", "false");
		
		this.evaluate = booleanProperty("EvaluatePerformance", "true");
		
		DGuitar.fastLoad = booleanProperty("FastLoad", "false");
		
		DGuitar.EnableUnfinishedFeatures = booleanProperty("EnableUnfinishedFeatures","false") ;
		
		setEnableUnfinishedFeatures(DGuitar.EnableUnfinishedFeatures) ;
        
		DGuitar.LS.switchTo(shortProperty("typeOfLog",LogStreamOptions.LOG_TO_FILE_ONLY),DGuitar.lang.getString("DGUITAR_LOG_WINDOW"),DGuitar.closableLogWindow,DGuitar.LogWindowExits) ;
		this.setLogCheckBoxes() ;
		
		sknName = DGO.getProperty("skin", "compact") ;
		DGuitar.currentSkinIndex = getIndexForSkinName(sknName) ;
		
		DGO.setProperty("skin",sknName) ;
		
		//this is called to refresh the options menu..if you add a property above..modify this method
		this.menuOptionsValuesUpdate();
	}
	/**
	 * this method performs the actual enabling or disabling of features
	 */
	private static void setEnableUnfinishedFeatures(boolean enable) {
		menuOptionsForAllSongs.setVisible(enable) ;
	}
	/**
	 * It reads from the InputStream, and display the PIECE or and error
	 * readAndShow used the variable fileName for the title of the window
	 */
	private void readAndShow(String aFileName, InputStream IS) {
		GPInputStream GPIS;
		SongInternalFrame SIF;
		boolean success;
		
		GPIS = null;
		success = false;
		
		GPIS = new GPInputStream(IS);
		SIF = new SongInternalFrame(aFileName, this, this.displayOptions,DGuitar.playToolBar);
		success = SIF.readAndDisplay(GPIS);
		if (success) {
			internalFrameAdd(SIF);
		} else {
			showDialogOk("SIF.readAndDisplay has FAILED", msg);
		}
		try {
			GPIS.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//TODO some windows are added to the desktop pane other are childs of
	// DGuitar. all the windows should be InternalFrames �?
	
	private void refreshDisplayOptions() {
		SongActionParameters SAP;
		
		//false means that the refresh happens only on SongInternalFrames
		SAP = new SongActionParameters(
				SongActionParameters.REFRESH_DISPLAY_OPTIONS, false, this);
		SongAction.perform(DGuitar.desktopPane, SAP);
	}
	
	private void refreshLangText() {
		SongActionParameters SAP;
		
		SAP = new SongActionParameters(SongActionParameters.REFRESH_LANG);
		
		SongAction.perform(DGuitar.desktopPane, SAP);
	}
	/**
	 * 
	 *
	 */
	
	private void determineLaunchPath() {
		lauchPath = new File("").getAbsolutePath();
		
		//if the user doubleClicks on the DGuitar.jar 
		// remove the "dist" folder name
		if (lauchPath.endsWith("dist")) {
			lauchPath = lauchPath.substring(0, lauchPath
					.length() - 4);
		}
		//add the File Separator (either "/" or this "\")
		lauchPath += File.separator;
	}
	
	/**
	 * Set the language messages for the menus
	 */
	public void setLangText() {
		LS.setTitle(DGuitar.lang.getString("DGUITAR_LOG_WINDOW"));
		
		menuProgram.setText(lang.getString("menuDGProgram"));
		menuProgramOpenGP.setText(lang.getString("menuDGOpenGP"));
		menuProgramOpenMidi.setText(lang.getString("menuDGOpenMID"));
		menuProgramOpenURL.setText(lang.getString("menuDGOpenURL"));
		menuProgramNew.setText(lang.getString("menuProgramNew"));
		menuProgramStats.setText(lang.getString("menuDGDirStats"));
		menuProgramExit.setText(lang.getString("menuDGExit"));
		menuOptions.setText(lang.getString("menuDGOptions"));
		menuOptionsFileLocation.setText(lang
				.getString("menuDGOptionsFileLocation"));
		menuOptionsConfirmLoad.setText(lang
				.getString("menuDGOptionsConfirmLoad"));
		menuOptionsEvaluate.setText(lang.getString("menuDGOptionsEvaluate"));
		menuOptionsFastLoad.setText(lang.getString("menuDGOptionsFastLoad"));
		menuOptionsLanguage.setText(lang.getString("languages"));
		//LOG texts
		menuOptionsLog.setText(lang.getString("menuDGOptionsLog")) ;
		menuOptionsLogToFile.setText(lang.getString("menuDGOptionsLogToFile")) ;
		menuOptionsLogToWindow.setText(lang.getString("menuDGOptionsLogToWindow")) ;
		
		//EnabledUnfinishedFeatures
		menuOptionsEnableUnfinishedFeatures.setText(lang.getString("menuDGOptionsEnabledUnfinishedFeatures")) ;
		
        menuOptionsSkins.setText(lang.getString("menuDGOptionsSkins"));
        
		menuOptionsSaveCurrentSettings.setText(lang
				.getString("menuDGSaveCurrentSettings"));
		menuOptionsReloadStoredSettings.setText(lang
				.getString("menuDGReloadStoredSettings"));
		menuOptionsForAllSongs.setLangText();
		
		menuWindows.setText(lang.getString("menuDGWindows"));
		
		menuWindowsCascade.setText(lang.getString("menuDGWindowsCascade"));
		
		menuWindowsCloseAll.setText(lang.getString("menuDGWindowsCloseAll"));
		
		menuMidiDevices.setText(lang.getString("menuMidiDevices"));
		menuMidiTest.setText(lang.getString("menuMidiTest"));
		menuMidi.setText(lang.getString("menuMidi"));
		menuAboutAuthors.setText(lang.getString("menuAboutAuthors"));
		menuAboutLicense.setText(lang.getString("menuAboutLicense"));
		menuAbout.setText(lang.getString("menuAbout"));
		
		//TODO iterate over the different windows calling the setLangText()
	}
	
	public String shortTitle() {
		return (DGuitar.version() + " @ " + filePath.getAbsolutePath());
	}
	
	/**
	 * this method allows to show a message on a information dialog the Object
	 * <message>can be a String or a Component see JOptionPane
	 */
	protected void showDialogOk(String title, Object message) {
		Util.showDialogOk(this, title, message);
	}
	
	/**
	 * Obtains a CodecFileFilter object for the current <code>fileType</code>
	 * 
	 * @return the CodecFileFilter object associated for a filetype
	 */
	
	public CodecFileFilter getCodecFileFilter() {
		CodecFileFilter resp;
		
		resp = null;
		switch (this.currentFileType) {
		case FileLoaderHandler.GUITAR_PRO:
			resp = DGuitar.gpFileFilter;
			break;
		case FileLoaderHandler.MIDI:
			resp = DGuitar.midiFileFilter;
			break;
		}
		return resp;
	}
	
	/**
	 * Show file chooser, respond to the actions, and load a file as necesary
	 */
	private void showFileChooserAndOpen() {
		int val;
		File f;
		
		//If the fileChooser was not created at the load process...then create
		// it here
		createFileChooser();
		fileChooser.setCurrentDirectory(this.filePath);
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		fileChooser.setApproveButtonText(lang.getString("Open"));
		
		fileChooser.setFileFilter(this.getCodecFileFilter());
		//since FileType has been set by the DisplayXX method..then we can call
		
		val = fileChooser.showOpenDialog(this);
		if (val == JFileChooser.CANCEL_OPTION)
			return;
		//ELSE
		if (val == JFileChooser.ERROR_OPTION)
			//TODO replace this by something more helpfull
			return;
		//ELSE
		f = fileChooser.getSelectedFile();
		//false means...no need to check for extension
		this.loadAndDisplay(f, false);
	}
	
	/**
	 * this method loads and displays a File according to its extension
	 * 
	 * @param file
	 */
	public void loadAndDisplay(File file, boolean checkExtension) {
		String aFileName;
		JMenuItem recentJMI ;
        
		aFileName = file.getName();
		if ((!checkExtension) || (this.isSupportedExtension(aFileName))) {
			if (Util.validFile(file)) {
				aFileName = file.getAbsolutePath();
				//PRATIMA's CHANGE
                
                //create the JMenutItem that holds the name of the recent file
                recentJMI = new JMenuItem(Util.compactAndReadableURL(aFileName,30)) ;
                //set the action commmand for this item.
                
                recentJMI.setActionCommand(aFileName) ;
                //Add and action listener that loads the file
                recentJMI.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        menuOpenRecentFilesActionPerformed(evt);
                    }
                });
                //add the item at the desired location
                this.menuProgram.add(recentJMI,RecentIndexStart+contRecent) ;
                //if is the first file on the recent list
                if(contRecent == 0) {
                    this.menuProgram.add(new JSeparator(),RecentIndexStart+contRecent+1) ;
                }
                //in-validate the menu so the content is updated and fit
                this.menuProgram.invalidate() ;
                //increment the counter of recent files
                contRecent++ ;
                
                //actually load the file
				this.fileLoad(aFileName);
				
			} else {
				showDialogOk(lang.getString("File_is_not_valid"), Util
						.getError());
			}
		} else {
			System.err.println("File Extension is not supported" + ":"
					+ aFileName);
		}
	}
	
	/**
	 * this message is showed when a file is already open
	 */
	void showMessageAlreadyOpen(String aFileName) {
		String message;
		
		message = "\n" + lang.getString("File") + " \'" + aFileName + "\' "
		+ lang.getString("already_open");
		showDialogOk(lang.getString("File") + " "
				+ lang.getString("already_open"), message);
	}
	
	/**
	 * this method shows a file on a ScrollText or ScrollTextPane according to
	 * the value of scroll
	 */
	private void showTextFile(String title, String file, boolean scroll, int w,
			int h) {
		
		if (scroll) {
			ScrollText ST;
			
			ST = new ScrollText("");
			ST.setTitle(title);
			ST.displayFile(file);
			
			ST.setBounds((DGuitar.screenSize.width - w) / 2, 50, w, h);
			ST.setSTPColor(java.awt.Color.WHITE);
			//The window is allowed to be closed but not exit
			ST.setClosable(true, false);
			ST.setVisible(true);
		} else {
			ScrollTextPanel STP;
			
			STP = new ScrollTextPanel();
			STP.displayFile(file);
			showDialogOk(title, STP);
		}
	}
	
	//--------------------------------------------------------
	//This classes below handle events on the Note and Silence Toolbars
	//--------------------------------------------------------
	
	/*
	 * class NoteToolBarListener implements ActionListener {
	 * 
	 * public void actionPerformed(ActionEvent e) { Object obj ; JButton jbutton ;
	 * int val ;
	 * 
	 * obj = e.getSource() ; jbutton = new JButton() ;
	 * if(obj.getClass().isInstance(jbutton)) { jbutton = (JButton) obj ; try {
	 * val = Integer.parseInt(jbutton.getActionCommand()) ; if(val <
	 * Cursors.length){ setChildCursor(Cursors[val]) ; } else { msg = "not
	 * enough cursor or too many notes" ; } } catch (NumberFormatException NFE) {
	 * msg = "invalid note pressed !!" ; } } } }
	 */
	/*
	 * class SilenceToolBarListener implements ActionListener {
	 * 
	 * public void actionPerformed(ActionEvent e) { Object obj ; JButton jbutton ;
	 * int val ;
	 * 
	 * obj = e.getSource() ; jbutton = new JButton() ;
	 * if(obj.getClass().isInstance(jbutton)) { jbutton = (JButton) obj ; try {
	 * val = Integer.parseInt(jbutton.getActionCommand()) ; if(val <
	 * Cursors.length){ setChildCursor(Cursors[7+val]) ; } else { msg = "not
	 * enough cursor or too many notes" ; } } catch (NumberFormatException NFE) {
	 * msg = "invalid note pressed !!" ; } } } }
	 */
	/**
	 * this method add suppor for the DROP part of "Drag and Drop"
	 */
	private void addDropSupport() {
		/* CODE TO SUPPORT DRAG AND DROP */
		
		DropTarget DT; //DND
		ADropTargetListener DTL; //DND
		
		//        create a DropTargetListener
		DTL = new ADropTargetListener(this); //DND
		
		//        The DropTarget is associated with a Component when that
		//        Component wishes to accept drops during Drag and Drop operations.
		DT = new DropTarget(this, DTL); //DND
	
        //very dirty..just to avoid a warning
        DT.toString() ;
	}
	
	/**
	 * @param displayOptions
	 *            The displayOptions to set.
	 */
	public void setDisplayOptions(DisplayOptions displayOptions) {
		this.displayOptions = displayOptions;
		this.refreshDisplayOptions();
	}
	
	/**
	 * @return Returns the displayOptions.
	 */
	public DisplayOptions getDisplayOptions() {
		return displayOptions;
	}
	
}