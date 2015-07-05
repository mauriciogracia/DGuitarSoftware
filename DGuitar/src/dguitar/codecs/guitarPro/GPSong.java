/*
 * Created on 13-mar-2005
 *
 */
package dguitar.codecs.guitarPro;

import java.util.ArrayList;
import java.util.List;


import common.ScrollTextPanel;
import common.SwingWorker;

/**
 * This class is created to join the different GP*Piece objects
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public class GPSong {
	public GPSong() {
		album = "";
        authorPiece = "";
        authorSong = "";
        channels = new GPMIDIChannel[64];
        //chordsDiagrams = new ArrayList(0);
        copyright = "";
        instruction = "";
        interpret = "";
        key = GPKey.G;
        lyrics = new GPTrackLyrics();
        measures = new ArrayList<GPMeasure>(0);
        measuresTracksPairs = new ArrayList<GPMeasureTrackPair>(0);
        note = "";
        octave = 8;
        subtitle = "";
        tempo = 100;
        title = "";
        tracks = new ArrayList<GPTrack>(0);
        tripletFeel = false;
        version = "unknown";
	}
    /** This class performs the actual loading of the file
	 *
	 */
	class StringImpl extends SwingWorker {
	    int i ;
	    int j ;
	    int numTracks ;
	    int numMeasures ;        
	    int deep ;
	    ScrollTextPanel STP ;
	    
	    public String res  ;
		private int tabsStrinImpl;
		//the number of \t to include
		/* (non-Javadoc)
		 * @see Common.SwingWorker#construct()
		 */
		public Object construct() {

	        return (this.toString()) ;
		}
	
	        
	    StringImpl(int howDeep, ScrollTextPanel stp) {
	    	STP = stp ;
	        this.tabsStrinImpl = 0 ;
	        this.deep = howDeep ;
	    }
	    /**Modifies the RES variable and Append to the ScrollTextPanel as necesary
	     * the number of tabs added depend on the tabsStrinImpl variable.
	     */
	    private void resAppend(String s) {
	        String aux ;
	        
	        aux = common.Util.tabs(tabsStrinImpl) + s ;
	        res += aux ;
	        if(this.STP != null) {
	            this.STP.append(aux) ;
	        }
	    }
	
	    
	    public String toString() {
	    	//DEBUG
	    	//System.err.println("TEXT VIEW!!") ;
	        
	    	res = "" ;
	        resAppend("GP - BEGIN\n") ;
	        
	        tabsStrinImpl++ ;
	        resAppend("HEADER - BEGIN\n") ;
	        resAppend("Version: " + version + "\n");
	        resAppend("Title: " + title + "\n") ;
	        resAppend("Subtitle: " + subtitle + "\n") ;
	        resAppend("Interpret: " + interpret + "\n") ;
	        resAppend("Album: " + album + "\n") ;
	        resAppend("Author of the song: " + authorSong + "\n");
	        resAppend("Copyright: " + copyright + "\n");
	        resAppend("Author of the piece: " + authorPiece + "\n");
	        resAppend("Instructions: " + instruction + "\n");
	        resAppend("Notes: " + note);
	        resAppend("Use triplet feel? " + tripletFeel + "\n");
	        resAppend(lyrics.toString());
	        resAppend("Tempo: " + tempo + "\n");
	        resAppend("Key: " + key + "\n");
	        resAppend("Octave: " + octave + "\n");
	        
	        numTracks =  tracks.size() ;            
	        resAppend("Number of measures: " + measures.size() + "\n");
	        resAppend("Number of tracks: " + tracks.size() + "\n");
	        resAppend("HEADER - END" + "\n") ;
	        tabsStrinImpl-- ;
	
	        
	        if(deep > 1) {
	            tabsStrinImpl++ ;
	            resAppend("MEASURES - BEGIN" + "\n") ;
	            for (i = 0; i < measures.size(); i++) {
	                resAppend("Measure #" + i + "\n") ;
	                resAppend(measures.get(i).toString() + "\n");
	            }
	            resAppend("MEASURES - END" + "\n") ;
	            tabsStrinImpl-- ;
	        }
	        
	        if(deep > 1) {
	            tabsStrinImpl++ ;
	            resAppend("TRACKS - BEGIN\n");
	            
	            for (i = 0; i < numTracks ; i++) {
	                resAppend("Track #" + (i+1) + "\n") ;
	                resAppend(tracks.get(i).toString() + "\n");
	            }
	            resAppend("TRACKS - END\n");
	            tabsStrinImpl-- ;
	        }
	        if(deep > 1) {
	            tabsStrinImpl++;
	            resAppend("MEASURE-TRACK PAIRS - BEGIN\n");
	            for (i = 0; i < measuresTracksPairs.size(); i++) {
	                resAppend("Measure #" + ((i/numTracks)+1) + " Track #" + (i%numTracks) + "\n") ;
	                resAppend(measuresTracksPairs.get(i).toString() + "\n");
	            }
	            resAppend("MEASURE-TRACK PAIRS - END\n");
	            tabsStrinImpl-- ;
	        }
	        if(deep > 1) {
	            tabsStrinImpl++ ;
	            resAppend("MIDI Channels - BEGIN\n");
	            for (i = 0; i < 4; i++) {
	                resAppend("Port: " + i + "\n" );
	                for (j = 0; j < 16; j++) {
	                    resAppend("Channel: " + j + "\n" );
	                    resAppend("" + channels[i*16+j] + "\n" ) ;
	                }
	            }
	            resAppend("MIDI Channels - END" + "\n");
	            tabsStrinImpl-- ;
	        }
	        tabsStrinImpl++ ;
	        resAppend("CHORD DIAGRAMS - BEGIN\n");
	        
	        resAppend("At this moment Chord Diagrams are not being read\n") ;
	        
	        resAppend("CHORD DIAGRAMS - END\n");
	        tabsStrinImpl-- ;
	        
	        resAppend("GP - END") ;
	        return res ;
	    }
	    /**
	     * this is called to find out how much work needd to be done to complete 
	     * the task of converting toString() ;
	     */
	    /*
	    public int getLengthOfTask() {
	        lengthOfTask = 10 ;
	        
	        return lengthOfTask;
	    }
	     */
	    
	    /**
	     * this is called to find how much progress has been made 
	     */
	    /*
	    public int getCurrent() {
	        return current;
	    }
	     */
	    /*
	     * Stops the toStringMethod
	     */
	    /*
	    public void stop() {
	        canceled = true;
	        statMessage = null;
	    }
	     */
	    
	    /**
	     * this is called to find out if the task has completed.
	     */
	    /*
	    public boolean isDone() {
	        return done;
	    }
	     */
	    
	    /**
	     * Returns the most recent status message, or null
	     * if there is no current status message.
	     */
	    /*
	    public String getMessage() {
	        return statMessage;
	    }
	     */
	}

	/**
     * The triplet feel.
     */
    public boolean tripletFeel;
    
    /**
     * The piece title.
     */
    protected String title;
    
    /**
     * The piece subtitle.
     */
    protected String subtitle;
    
    /**
     * The piece interpret.
     */
    protected String interpret;
    
    /**
     * The piece album.
     */
    protected String album;
    
    /**
     * The author of the song.
     */
    protected String authorSong;
    
    /**
     * The piece copyright.
     */
    protected String copyright;
    
    /**
     * The piece author.
     */
    protected String authorPiece;
    
    /**
     * The instructions to use with this piece.
     */
    protected String instruction;
    
    /**
     * Notes about the piece.
     */
    protected String note;
    
    /**
     * Lyrics associated with the tracks.
     */
    protected GPTrackLyrics lyrics;
    
    /**
     * The piece tempo.
     */
    protected int tempo;

	/**
	 * The MIDI channels used.
	 */
	protected GPMIDIChannel[] channels;

	/**
	 * A list of tracks present in this piece.
	 */
	protected List<GPTrack> tracks;

	/**
	 * A list of measures present in this piece.
	 */
	public List<GPMeasure> measures;

	/**
	 * A list of measures-tracks pairs containing data about the music.
	 */
	protected List<GPMeasureTrackPair> measuresTracksPairs;

	/**
	 * The version of the file the piece was loaded from.
	 */
	protected String version;

	SwingWorker worker ;

	/**
	 * A list of chords diagrams.
	 */
	//protected List chordsDiagrams;

	/**
	 * The key signature at the beginning of the piece.
	 */
	protected GPKey key;

	/**
	 * The octave (not used).
	 */
	protected int octave;

	/**
	 * @return Returns the album.
	 */
	public String getAlbum() {
	    return album;
	}

	/**
	 * @return Returns the authorPiece.
	 */
	public String getAuthorPiece() {
	    return authorPiece;
	}

	/**
	 * @return Returns the authorSong.
	 */
	public String getAuthorSong() {
	    return authorSong;
	}

	/**
	 * @return Returns the channels.
	 */
	public GPMIDIChannel getChannels(int i) {
	    return channels[i];
	}

	/**
	 * @return Returns the copyright.
	 */
	public String getCopyright() {
	    return copyright;
	}

	/**
	 * @return Returns the instruction.
	 */
	public String getInstruction() {
	    return instruction;
	}

	/**
	 * @return Returns the interpret.
	 */
	public String getInterpret() {
	    return interpret;
	}

	/**
	 * @return Returns the lyrics.
	 */
	public GPTrackLyrics getLyrics() {
	    return lyrics;
	}

	/**
	 * @return Returns the note.
	 */
	public String getNote() {
	    return note;
	}

	/**
	 * @return Returns the subtitle.
	 */
	public String getSubtitle() {
	    return subtitle;
	}

	/**
	 * @return Returns the tempo.
	 */
	public int getTempo() {
	    return tempo;
	}

	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
	    return title;
	}
	
	public List<GPTrack> getTracks() {
        return tracks ;
	}

	/**
	 * @return Returns the version.
	 */
	public String getVersion() {
	    return version;
	}

	/**
	 * @return Returns the measures.
	 */
	public List<GPMeasure> getMeasures() {
	    return measures;
	}

	/**
	 * @return Returns the measuresTracksPairs.
	 */
	public List<GPMeasureTrackPair> getMeasuresTracksPairs() {
	    return measuresTracksPairs;
	}
	/**
	 * 
	 * @return Returns the number of Tracks of this Song
	 */
	public int getNumTracks() {
	    int res ;
	    
	    res = 0 ;
	    if(this.tracks != null) {
	        res = this.tracks.size() ;
	    }
	    
	    return res ;
	}
	/**
	 * 
	 * @return the number of measures of this Song
	 */
	public int getNumMeasures() {
	    int res ;
	    
	    res = 0 ;
	    if(this.measures != null) {
	        res = this.measures.size() ;
	    }
	    
	    return res ;
	}

	/**
	 * @return Returns a MeasureTrackPair
	 */
	public GPMeasureTrackPair getTrackMeasurePair(int track, int measure) {
	    return measuresTracksPairs.get(
	            (measure - 1) * tracks.size() + track - 1);
	}

	public void startDisplayString(final ScrollTextPanel stp) {
	    worker = new StringImpl(2,stp) ;
	    worker.start() ;
	    //DEBUG
	    //System.err.println("TEXT VIEW!!") ;
	}

	public void stopDisplayString() {
		if(worker != null) {
			worker.interrupt() ;
		}
	}

	/**
	 * Returns a string representation of this piece.
	 */
	public String toString() {
	    //the default DEEP is 100...in other words print every detail ;
	    //new StringImpl(100,null) ;
	    //return res ;
		return new StringImpl(100,null).toString() ;
	}

	/**
	 * @param album
	 *            The album to set.
	 */
	public void setAlbum(String album) {
	    this.album = album;
	}

	/**
	 * @param authorPiece
	 *            The authorPiece to set.
	 */
	public void setAuthorPiece(String authorPiece) {
	    this.authorPiece = authorPiece;
	}

	/**
	 * @param authorSong
	 *            The authorSong to set.
	 */
	public void setAuthorSong(String authorSong) {
	    this.authorSong = authorSong;
	}

	/**
	 * @param channels
	 *            The channels to set.
	 */
	public void setChannels(int i, GPMIDIChannel channels) {
	    this.channels[i] = channels;
	}

	/**
	 * @param copyright
	 *            The copyright to set.
	 */
	public void setCopyright(String copyright) {
	    this.copyright = copyright;
	}

	/**
	 * @param instruction
	 *            The instruction to set.
	 */
	public void setInstruction(String instruction) {
	    this.instruction = instruction;
	}

	/**
	 * @param interpret
	 *            The interpret to set.
	 */
	public void setInterpret(String interpret) {
	    this.interpret = interpret;
	}

	/**
	 * @param key
	 *            The key to set.
	 */
	public void setKey(GPKey key) {
	    this.key = key;
	}

	/**
	 * @param lyrics
	 *            The lyrics to set.
	 */
	public void setLyrics(GPTrackLyrics lyrics) {
	    this.lyrics = lyrics;
	}

	/**
	 * @param note
	 *            The note to set.
	 */
	public void setNote(String note) {
	    this.note = note;
	}

	/**
	 * @param octave
	 *            The octave to set.
	 */
	public void setOctave(int octave) {
	    this.octave = octave;
	}

	/**
	 * @param subtitle
	 *            The subtitle to set.
	 */
	public void setSubtitle(String subtitle) {
	    this.subtitle = subtitle;
	}

	/**
	 * @param tempo
	 *            The tempo to set.
	 */
	public void setTempo(int tempo) {
	    this.tempo = tempo;
	}

	/**
	 * @param title
	 *            The title to set.
	 */
	public void setTitle(String title) {
	    this.title = title;
	}

	/**
	 * @param version
	 *            The version to set.
	 */
	public void setVersion(String version) {
	    this.version = version;
	}

	/**
	 * @return Returns the chordsDiagrams.
	 */
	/*
	public List getChordsDiagrams() {
	    return chordsDiagrams;
	}
	 */
	/**
	 * @return Returns the key.
	 */
	public GPKey getKey() {
	    return key;
	}

	/**
	 * @return Returns a MeasureTrackPair
	 */
	public GPMeasureTrackPair getMeasureTrackPair(int measure, int track) {
	    return measuresTracksPairs.get(
	            (measure - 1) * tracks.size() + track - 1);
	}

	/**
	 * @return Returns the octave.
	 */
	public int getOctave() {
	    return octave;
	}

}
