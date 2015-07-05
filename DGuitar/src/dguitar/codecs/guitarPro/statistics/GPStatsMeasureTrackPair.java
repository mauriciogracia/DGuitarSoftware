/*
 * Created on 12-mar-2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dguitar.codecs.guitarPro.statistics;

import java.util.List;

import dguitar.codecs.guitarPro.GPBeat;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;
import dguitar.codecs.guitarPro.GPNote;


/**
 * @author Mauricio Gracia Gutiérrez
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GPStatsMeasureTrackPair extends GPStats {
    public int numChordDiagrams ;
    public int numEffects ;
    public int numMixTableChanges ;
    /**number of beats in this Measure Track Pair*/
    private int numBeats ;
    /**number of notes in this Measure Track Pair*/
    private int numNotes ;
    /**number of notes with effects in this Measure Track Pair*/
    private int numNoteEffects ;
    
    private int measure ;
    private int track ;
    
    
    List<GPBeat> beats ;
    GPStatsEffectsOnBeat totalEffectsOnBeat ;
    GPStatsEffectsOnNote totalEffectsOnNote ;
    
    private void common() {
        this.numChordDiagrams = 0 ;
        this.numEffects = 0 ;
        this.numMixTableChanges = 0 ;
        this.numNotes = 0 ;
        this.numNoteEffects = 0 ;
        totalEffectsOnBeat = new GPStatsEffectsOnBeat() ;
        totalEffectsOnNote = new GPStatsEffectsOnNote() ;
        
    }
    
    public GPStatsMeasureTrackPair() {
    	this.numBeats = 0 ;
        this.measure = -1 ;
        this.track = -1 ;
        common() ;
    }
    public GPStatsMeasureTrackPair(GPMeasureTrackPair MTP, int Measure, int Track) {
        int i ;
        GPBeat beat ;
        GPStatsEffectsOnBeat EOBI ;
        GPStatsEffectsOnNote EONI ;
        List<GPNote> notes ;
        //to iterate over the notes ;
        int n ;
        GPNote Note ;
        
        this.beats = MTP.getBeats() ;
        if(beats != null) {
            this.numBeats = beats.size() ;
            this.measure = Measure ;
            this.track = Track ;
            
            common() ;
            
            for (i = 0; i < numBeats ; i++) {
                beat = (GPBeat) beats.get(i) ;
                if(beat.chordDiagram != null) {
                    this.numChordDiagrams++ ;
                }
                if(beat.effects != null) {
                    this.numEffects++ ;
                    EOBI = new GPStatsEffectsOnBeat(beat.effects) ;
                    totalEffectsOnBeat.add(EOBI) ;
                }
                if(beat.mixTableChange != null) {
                    this.numMixTableChanges++ ;
                }
                notes = beat.getNotes() ;
                if(notes != null) {
                    this.numNotes += notes.size() ;
                    for(n = 0; n < notes.size() ; n++ ){
                        Note = (GPNote) notes.get(n) ;
                        if(Note.effects != null) {
                            this.numNoteEffects++ ;
                            EONI = new GPStatsEffectsOnNote(Note.effects) ;
                            totalEffectsOnNote.add(EONI) ;
                        }
                    }
                }
                //private boolean emptyBeat;
                //private boolean restBeat;
                //private GPDuration duration;
                //private List notes;
                //private int nTuplet;
                //private boolean[] strings;
                //public boolean dottedNotes;
                
            }
        }
    }
    public void add(GPStatsMeasureTrackPair a) {
        if(a != null) {
            this.numBeats += a.numBeats ;
            this.numNotes += a.numNotes ;
            this.numNoteEffects += a.numNoteEffects ;
            this.numChordDiagrams += a.numChordDiagrams ;
            this.numEffects += a.numEffects ;
            this.numMixTableChanges += a.numMixTableChanges  ;
            if(this.totalEffectsOnBeat != null) {
                this.totalEffectsOnBeat.add(a.totalEffectsOnBeat) ;
            }
            if(this.totalEffectsOnNote != null) {
                this.totalEffectsOnNote.add(a.totalEffectsOnNote) ;
            }
        }
    }
    public boolean nonZero() {
        boolean condA ;
        
        condA = (numChordDiagrams != 0) || (numEffects != 0) ;
        return ( condA || (this.numMixTableChanges != 0) || (numNoteEffects != 0) ) ;
    }
    public String toString() {
        String res ;
        String aux ;
        
        res = "" ;
        aux = "beats" ;
        if(measure >= 0) {
            res += common.Util.tabs(level) + "Measure: " + measure + "\n";
        }
        if(track >= 0) {
            res += common.Util.tabs(level) + "Track: " + track + "\n";
        }
        level ++ ;
        res += common.Util.tabs(level) + numBeats + " " + aux + "\n";
        res += stringFor(numChordDiagrams, "ChordDiagrams",  numBeats,aux) ;
        res += stringFor(numEffects, "Effects",  numBeats,aux) ;
        if(numEffects > 0) {
            level++ ;
            //System.out.println("DEBUG MTP = " + level) ;
            res += totalEffectsOnBeat.toString();
            level-- ;
//            empty = false ;
        }
        if(this.numNoteEffects > 0) {
            level++ ;
            res += totalEffectsOnNote.toString();
            level-- ;
  //        empty = false ;
        }
        res += stringFor(numMixTableChanges, "MixTableChanges",  numBeats,aux) ;
        /*
        if(empty) {
            res = common.Util.tabs(level) + "All Statistics of MeasureTrackPair are Empty(0)" + "\n" ;
        }
        */
        level-- ;
        
        return res ;
    }
}
