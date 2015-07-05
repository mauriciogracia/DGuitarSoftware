/*
 * Created on 12-mar-2005
 *
 */
package dguitar.codecs.guitarPro.statistics;

import dguitar.codecs.guitarPro.GPEffectsOnNote;



/**
 * this class calculates statistics for EfffectsOnNote
 */

public class GPStatsEffectsOnNote extends GPStats {
    public int numBends ;
    public int numGraceNotes  ;
    public int numTremoloPicks  ;
    public int numSlides ;
    public int numHarmonics ;
    public int numTrills ;
    public int numLetRing;
    public int numHammerOnPullOff;
    public int numLeftHandVibrato;
    public int numPalmMute;
    public int numStaccato;
    
    //number of notes
    public int nn ;
    
    public GPStatsEffectsOnNote() {
        nn = 0 ;
        numBends = 0 ;
        numGraceNotes = 0 ;
        numTremoloPicks = 0 ;
        numSlides = 0 ;
        numHarmonics = 0 ;
        numTrills = 0 ;
        numLetRing = 0 ;
        numHammerOnPullOff = 0;
        numLeftHandVibrato = 0;
        numPalmMute = 0 ;
        numStaccato = 0 ;
    }
    public GPStatsEffectsOnNote(GPEffectsOnNote EON) {
        nn = 1 ;
        numBends = (EON.bend != null) ? 1:0 ;
        numGraceNotes = (EON.graceNote != null) ? 1:0 ;
        numTremoloPicks = (EON.tremoloPicking != null) ? 1:0 ;
        numSlides = (EON.slide != null) ? 1:0 ;
        numHarmonics = (EON.harmonic != null) ? 1:0 ;
        numTrills = (EON.trill != null) ? 1:0 ;
        numLetRing = (EON.letRing) ? 1:0 ;
        numHammerOnPullOff = (EON.hammerOnPullOff) ? 1:0 ;
        numLeftHandVibrato = (EON.leftHandVibrato) ? 1:0 ;
        numPalmMute = (EON.palmMute) ? 1:0 ;
        numStaccato = (EON.staccato) ? 1:0 ;
        
    }
    void add(GPStatsEffectsOnNote a) {
        if(a != null) {
            this.nn += a.nn ;
            this.numBends += a.numBends ;
            this.numGraceNotes += a.numGraceNotes ;
            this.numTremoloPicks += a.numTremoloPicks ;
            this.numSlides += a.numSlides;
            this.numHarmonics += a.numHarmonics ;
            this.numTrills += a.numTrills ;
            this.numLetRing += a.numLetRing ;
            this.numHammerOnPullOff += a.numHammerOnPullOff;
            this.numLeftHandVibrato += a.numLeftHandVibrato;
            this.numPalmMute += a.numPalmMute ;
            this.numStaccato += a.numStaccato ;
        }
    }
    public String toString() {
        String res ;
        String aux ;
        
//        empty = true ;
        res = "" ;
        aux = "notes" ;
        res += common.Util.tabs(level) + nn + " " + aux + "\n";
        level++ ;
        res += stringFor(numBends,"bends",nn,aux) ;
        res += stringFor(numGraceNotes,"grace notes",nn,aux) ;
        res += stringFor(numTremoloPicks,"tremolo picks",nn,aux) ;
        res += stringFor(numSlides,"slides",nn,aux) ;
        res += stringFor(numHarmonics,"harmonics",nn,aux) ;
        res += stringFor(numTrills,"trills",nn,aux) ;
        res += stringFor(numLetRing,"letRings",nn,aux) ;
        res += stringFor(numHammerOnPullOff,"hammerOn-PullOffs",nn,aux) ;
        res += stringFor(numLeftHandVibrato,"left Hand Vibratos",nn,aux) ;
        res += stringFor(numPalmMute,"palm Multes",nn,aux) ;
        res += stringFor(numStaccato,"staccatos",nn,aux) ;
        /*
        if(empty) {
            res = common.Util.tabs(level) + "All Statistics of EffectsOnNote are Empty(0)" + "\n" ; ;
        }
        */
        level-- ;
        return res ;
    }
}
