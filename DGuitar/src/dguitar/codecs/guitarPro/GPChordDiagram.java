package dguitar.codecs.guitarPro;

/**
 * This class describes a chord diagram.
 *
 * @author Mauricio Gracia Gutierrez
 */
public class GPChordDiagram {
    /**
     *Determines if the chord is displayed sharp or flat.
     */
    private boolean isSharp ;
    
    /**
     *The root of the Diagram (-1 for the customized chords, 0 when root is C,1 = C#...)
     */
    private GPChordNote root ;
    
    /**
     *Determines the chord type as followed:
     * - 0: M
     * - 1: 7
     * - 2: 7M
     * - 3: 6
     * - 4: m
     * - 5: m7
     * - 6: m7M
     * - 7: m6
     * - 8: sus2
     * - 9: sus4
     * - 10: 7sus2
     * - 11: 7sus4
     * - 12: dim
     * - 13: aug
     * - 14: 5
     */
    private GPChordType chordType ;
    
    /**
     *Nine, Eleven of Thirteen: byte
     *Determines if the chord goes until the ninth, the eleventh, or the thirteenth.
     */
    private int nineElevenThirteen ;
    
    /**
     *Bass: Integer
     *Lowest note of the chord. It gives the chord inversions.
     */
    private GPChordNote bass ;
    
    /**
     * Diminished/Augmented Integer
     * Tonality linked with 9/11/13:
     * 0: perfect ("juste")
     * 1: augmented
     * 2: diminished
     */
    private GPTonalityType tonalityType ;
    
    /**
     * add: byte
     * Allows to determine if a 'add' (added note) is present in the chord.
     */
    private int addedNote ;
    
    /**
     * Name: String
     * 20 characters long string containing the chord name.
     */
    private String name ;
    
    /**
     * Fifth: byte
     * Tonality of the fifth:
     * 0: perfect ("juste")
     * 1: augmented
     * 2: diminished
     */
    private GPTonalityType tonalityFive ;
    
    /**
     * Ninth: byte
     * Tonality of the ninth:
     * 0: perfect ("juste")
     * 1: augmented
     * 2: diminished
     * this tonality is valid only if the value "Nine, Eleven or Thirteen" is 11 or 13.
     */
    private GPTonalityType tonalityNine ;
    
    /**
     * Eleventh: byte
     * Tonality of the eleventh:
     * 0: perfect ("juste")
     * 1: augmented
     * 2: diminished
     * this tonality is valid only if the value "Nine, Eleven or Thirteen" is 13.
     */
    private GPTonalityType tonalityEleven ;
    
    /**
     * Base fret: Integer
     * Shows the fret from which the chord is displayed.
     */
    private int baseFret ;
    
    /**
     * Frets: List of 7 integers.
     * Corresponds to the fret number played on each string, from the highest to the lowest.
     * -1 means a string unplayed.
     * 0 means a string played "blank" (ie no fret).
     */
    private int [] frets ;
    
    /**
     * Number of barres: byte
     * Indicates the number of barres there are in the chord. A chord can contain up to 5 barres.
     */
    private int numBarres ;
    
    /**
     * Fret of the barre: List of 5 Bytes
     * Indicates the fret number of each possible barre.
     */
    private int [] fretOfBarres ;
    
    /**
     * Barre start: List of 5 Bytes
     * Indicates the first string of the barre, 1 being the highest.
     * The list order matches the chord different barres frets list order.
     */
    
    private int [] barreStarts ;
    
    /**
     * Barre end: List of 5 Bytes
     * Indicates the first string of the barre, 1 being the lowest.
     * The list order matches the chord different barres frets list order.
     */
    private int [] barreEnds ;
    
    /**
     * Fingering: List of 7 Bytes
     * Describes the fingering used to play the chord.
     * Below is given the fingering from the highest string to the lowest:
     * -2: unknown;
     * -1: X or 0 (no finger);
     * 0: thumb;
     * 1: index;
     * 2: middle finger;
     * 3: annular;
     * 4: little finger.
     */
    private GPFingering[]fingering ;
    
    /**
     * ShowDiagFingering: byte
     * if it is 0x01, the chord fingering is displayed.
     * if it is 0x00, the chord fingering is masked.
     */
    private boolean chordFingeringDisplayed ;
    
    /** Creates a new instance of ChordDiagram */
    public GPChordDiagram() {
        isSharp = false ;
        root = null ;
        chordType = null ;
        nineElevenThirteen = 0 ;
        bass = null ; 
        //OLD bass = 0 ;
        tonalityType = null ;
        addedNote = 0 ;
        name = "" ;
        tonalityFive = null ;
        tonalityNine = null ;
        tonalityEleven = null ;
        baseFret = 0 ;
        frets = new int[7] ;
        numBarres = 0 ;
        fretOfBarres = new int [5] ;
        barreStarts = new int [5] ;
        barreEnds = new int [5] ;
        fingering = new GPFingering[7] ;
        chordFingeringDisplayed = false ;
    }
    public void setSharp(boolean isSharp) {
        this.isSharp = isSharp ;
    }
    public void setRoot(GPChordNote root) {
        this.root = root ;
    }
    public void setChordType(GPChordType CT) {
        this.chordType = CT ;
    }
    public void setNineElevenThirteen(int NET) {
        this.nineElevenThirteen = NET ;
    }
    public void setBass(GPChordNote bass) {
        this.bass = bass ;
    }
    public void setTonalityType(GPTonalityType TT) {
        this.tonalityType = TT ;
    }
    public void setAddedNote(int addedNote) {
        this.addedNote = addedNote ;
    }
    public void setName(String name) {
        this.name = name ;
    }
    public void setTonalityFive(GPTonalityType TT) {
        this.tonalityFive = TT ;
    }
    
    public void setTonalityNine(GPTonalityType TT) {
        this.tonalityNine = TT ;
    }
    
    public void setTonalityEleven(GPTonalityType TT) {
        this.tonalityEleven = TT ;
    }
    
    public void setBaseFret(int baseFret) {
        this.baseFret = baseFret ;
    }
    
    public void setFret(int pos, int fret) {
        if ( (pos >= 1) && (pos <= 7) ) {
            this.frets[pos-1] = fret ;
        }
    }
    
    public void setNumBarres(int numBarres) {
        this.numBarres = numBarres ;
    }
    
    public void setFretOfBarre(int pos, int fret) {
        if ( (pos >= 1) && (pos <= 5) ) {
            this.fretOfBarres[pos-1] = fret ;
        }
    }
    public void setBarreStart(int pos, int barre) {
        if ( (pos >= 1) && (pos <= 5) ) {
            this.barreStarts[pos-1] = barre ;
        }
    }
    public void setBarreEnd(int pos, int barre) {
        if ( (pos >= 1) && (pos <= 5) ) {
            this.barreEnds[pos-1] = barre ;
        }
    }
    public void setFingering(int pos, int finger) {
        if ( (pos >= 1) && (pos <= 7) ) {
            this.fingering[pos-1] = GPFingering.valueOf(finger) ;
        }
    }
    
    public void setchordFingeringDisplayed(boolean value) {
        this.chordFingeringDisplayed = value ;
    }
    
    public String toString() {
        String res = "";
        int i ;
        
        res += "isSharp: " + this.isSharp ;
        if(root != null) {
        	res += "\n\t\tRoot: " + this.root.toString() ;
        }
        if(chordType != null) {
        	res += "\n\t\tChordType: " + this.chordType.toString() ;
        }
        res += "\n\t\tNineElevenThirteen: " + this.nineElevenThirteen ;
        if(bass != null) {
        	res += "\n\t\tBass: " + this.bass.toString() ;
        }
        if(this.tonalityType != null) {
        	res += "\n\t\tTonality: " + this.tonalityType.toString() ;
        }
        res += "\n\t\tAddedNote: " + this.addedNote ;
        res += "\n\t\tName: " + this.name ;
        if(tonalityFive != null) {
        	res += "\n\t\tTonality Five: " + this.tonalityFive.toString() ;
        }
        if(tonalityNine != null) {
        	res += "\n\t\tTonality Nine: " + this.tonalityNine.toString() ;
        }
        if(tonalityEleven != null) {
        	res += "\n\t\tTonality Eleven: " + this.tonalityEleven.toString() ;
        }
        res += "\n\t\tBase Fret: " + this.baseFret ;
        res += "\n\t\tFrets From Left to Right (including the 7th guitar string): " ;
        for(i = 6; i >= 0; i--) {
            res += "" + this.frets[i] ;
            if(i-1 >= 0)
                res += "," ;
        }
        res += "\n\t\tIsChordFingerinDisplayed: " + this.isSharp ;
        if(this.fingering != null) {
        	res += "\n\t\tFingering: " ;
        	for(i = 6; (i >= 0) && (this.fingering[i] != null); i--) {
        		res += "" + this.fingering[i].toString() ;
        		if(i-1 >= 0)
        			res += "," ;
        	}
        }
        
        res += "\n\t\tNum Barres: " + this.numBarres ;
        
        res += "\n\t\tFrets of Barres: " ;
        for(i = 0; i < 5; i++) {
            res += "" + this.fretOfBarres[i] ;
            if(i+1 < 5)
                res += "," ;
        }
        
        res += "\n\t\tBarres Starts: " ;
        for(i = 0; i < 5; i++) {
            res += "" + this.barreStarts[i] ;
            if(i+1 < 5)
                res += "," ;
        }
        
        res += "\n\t\tBarres Ends: " ;
        for(i = 0; i < 5; i++) {
            res += "" + this.barreEnds[i] ;
            if(i+1 < 5)
                res += "," ;
        }
        
        return(res);
    }
}
    /*
        root = 0 ;
        chordType = null ;
        nineElevenThirteen = 0 ;
        bass = 0 ;
        tonalityType = null ;
        addedNote = 0 ;
        name = "" ;
        tonalityFive = null ;
        tonalityNine = null ;
        tonalityEleven = null ;
        baseFret = 0 ;
        strings = new boolean[7] ;
        numBarres = 0 ;
        fretsOfBarres = new int [5] ;
        barreStarts = new int [5] ;
        barreEnds = new int [5] ;
        fingering = null ;
     chorFingeringDisplayed = false ;
     */


