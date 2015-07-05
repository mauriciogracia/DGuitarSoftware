/*
 * Created on 12-mar-2005
 *
 */
package dguitar.codecs.guitarPro.statistics;

import java.util.List;

import dguitar.codecs.guitarPro.GPMeasure;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;
import dguitar.codecs.guitarPro.GPSong;
import dguitar.codecs.guitarPro.GPTrack;


/**
 * 
 * this class calculates statistics for a Piece
 *
 *
 * @author Mauricio Gracia Gutiérrez
 */

public class GPStatsPiece extends GPStats {
    int numTracks ;
    int numMeasures ;
    GPStatsMeasureTrackPair MTPtotal ;
    String pieceName ;
    java.util.Vector<GPStatsMeasureTrackPair> MTPdetails ;
    int numTracksWithLyrics ;
    
    public GPStatsPiece() {
        numTracks = 0 ;
        numMeasures = 0 ;
        numTracksWithLyrics = 0 ;
        MTPtotal = new GPStatsMeasureTrackPair() ;
        MTPdetails = null ;
        pieceName = null ;
    }
    /**
     * this class gathers information about a single GPSong, you can dedicde
     * if you want detailed, The Sring pieceName contains the name to display.
     */
    public void getStats(GPSong p, String PieceName ) {
        int i ;
        int m ;
        int t ;
        int numMTPs ;
        List<GPMeasure> measures ;
        List<GPTrack> tracks ;
        List<GPMeasureTrackPair> MTPs ;
        GPStatsMeasureTrackPair detail ;
        GPMeasureTrackPair MTP ;
        
        this.pieceName = PieceName ;
        measures =  p.getMeasures() ;
        if(measures != null) {
            numMeasures = measures.size() ;
        }
        
        tracks = p.getTracks() ;
        if(tracks != null) {
            numTracks = tracks.size() ;
        }
        
        
        this.numTracksWithLyrics = (p.getLyrics() != null) ? 1 : 0 ;
        
        MTPs = p.getMeasuresTracksPairs() ;
        
        numMTPs = MTPs.size() ;
        MTPtotal = new GPStatsMeasureTrackPair() ;
        if(this.isDetailed()) {
            MTPdetails = new java.util.Vector<GPStatsMeasureTrackPair>(0,1) ;
        }
        for (i = 0; i < numMTPs; i++) {
            MTP = (GPMeasureTrackPair) MTPs.get(i) ;
            t = (i%numTracks) ;
            m = (i/numTracks)+1 ;
            detail = new GPStatsMeasureTrackPair(MTP, m, t) ;
            if( this.isDetailed() && (detail.nonZero()) ) {
                MTPdetails.add(detail) ;
            }
            MTPtotal.add(detail) ;
        }
    }
    public String toString() {
        String res ;
        String tabs ;
        GPStatsMeasureTrackPair MTPdetail ;
        int i ;
        
        tabs = common.Util.tabs(level) ;
        res = "" ;
        if( (pieceName != null) ) {
            res = tabs ;
            for(int j = 0 ; j < 6 ; j ++) {
                res += "--------------------------" ;
            }
            res +="\n" ;
            if (MTPdetails != null)  {
                res += tabs + "Statistics for piece \'" + pieceName + "\'\n" ;
            }
        }
        
    /* are there usefull statistics about number of repetitions, time signatures ??
    for (i = 0; i < numMeasures ; i++) {
        resAppend("\n\tMeasure #" + i) ;
        resAppend("\n\t" + measures.get(i).toString() + "\n");
    }*/
        
        
        //boolean isDrumsTrack;
        //boolean is12StringedGuitarTrack;
        //boolean isBanjoTrack;
    /*
            for (i = 0; i < numTracks ; i++) {
                resAppend("\n\tTrack #" + i) ;
                resAppend("\n\t" + tracks.get(i).toString() + "\n");
            }
     */
        //level++ ;
        if(MTPdetails != null) {
        	level ++ ;
            for (i = 0; i < MTPdetails.size() ; i++) {
                MTPdetail = MTPdetails.get(i) ;
                res += MTPdetail.toString() ;
            }
            level -- ;
            res += tabs ;
            for(int j = 0 ; j < 3 ; j ++) {
                res += "==========================" ;
            }
            res += "\n" ;
        }
        //level-- ;
        
        
        if(pieceName != null)  {
            
            res += tabs + "Totals for piece \'" + pieceName + "\'\n" ;
        }
        //level++ ;
        
        res += tabs + "\t" + numMeasures + " measures" + "\n" ;
        res += tabs + "\t" + numTracks +  " tracks" + "\n" ;
        res += "\t" + this.stringFor(numTracksWithLyrics,"track with lyrics",numTracks, "tracks" ) ;
        if(MTPtotal != null) {
            level++ ;
            res += MTPtotal.toString() ;
            level-- ;
        }
        //level-- ;
        
        
/*
            resAppend("\n\tMIDI Channels - BEGIN");
            for (i = 0; i < 4; i++) {
                resAppend("\n\t\tPort: " + i );
                for (j = 0; j < 16; j++) {
                    resAppend("\n\t\t\tChannel: " + j);
                    resAppend("\n\t\t\t" + channels[i*16+j]) ;
                }
            }
            resAppend("\n\tMIDI Channels - END");


        resAppend("\nCHORD DIAGRAMS - BEGIN\n");
        resAppend("At this moment Chord Diagrams are not being read") ;
        resAppend("\nCHORD DIAGRAMS - END\n");
*/
        
        return res ;
    }
    public void add(GPStatsPiece a) {
        if(a != null) {
            this.numTracks+= a.numTracks ;
            this.numMeasures += a.numMeasures ;
            this.numTracksWithLyrics += a.numTracksWithLyrics ;
            if(this.MTPtotal != null) {
                this.MTPtotal.add(a.MTPtotal) ;
            }
            
        }
    }
}