package dguitar.codecs.guitarPro;

import common.Util;

/**
 * This class describes effects present on a note.
 * 
 * @author Matthieu Wipliez
 */
public class GPEffectsOnNote {
    /**
     * Bend possibly associated with this effect.
     */
    public GPBend bend;

    /**
     * Grace note possibly associated with this effect.
     */
    public GPGraceNote graceNote;

    /**
     * Presence of a Hammer-On/Pull-Off
     */
    public boolean hammerOnPullOff;

    /**
     * Harmonic possibly associated with this effect.
     */
    public GPHarmonic harmonic;

    /**
     * Presence of a left-hand vibrato.
     */
    public boolean leftHandVibrato;

    /**
     * Presence of a let ring.
     */
    public boolean letRing;

    /**
     * Presence of a Palm-Mute (PM).
     */
    public boolean palmMute;

    /**
     * Slide possibly associated with this effect.
     */
    public GPSlide slide;

    /**
     * Presence of staccato.
     */
    public boolean staccato;

    /**
     * Tremolo picking possibly associated with this effect.
     */
    public GPDuration tremoloPicking;

    /**
     * Trill possibly associated with this effect.
     */
    public GPTrill trill;

    /**
     * Presence of a "wide vibrato" effect.
     */
    public boolean wideVibrato;

    /**
     * Creates a new GPEffectsOnNote.
     *  
     */
    public GPEffectsOnNote() {
        bend = null;
        graceNote = null;
        tremoloPicking = null;
        slide = null;
        harmonic = null;
        hammerOnPullOff = false;
        leftHandVibrato = false;
        letRing = false;
        palmMute = false;
        staccato = false;
        trill = null;
    }

    /**
     * Returns a string representation of this GPEffectsOnNote.
     */
    public String toString() {
        String res;
        Boolean firstField;
        
        res = "";

        firstField = new Boolean(true);
        res += Util.fieldFor("Grace note", this.graceNote, firstField) ;
        res += Util.fieldFor("Let Ring",this.letRing,firstField) ;
        res += Util.fieldFor("hammerOn/PullOff",this.hammerOnPullOff,firstField) ;
        res += Util.fieldFor("Bend",this.bend,firstField) ;
        res += Util.fieldFor("leftHandVibrato",this.leftHandVibrato,firstField) ;
        res += Util.fieldFor("Trill", this.trill,firstField) ;
        res += Util.fieldFor("Harmonic", this.harmonic,firstField) ;
        res += Util.fieldFor("Slide", this.slide,firstField) ;
        res += Util.fieldFor("Tremolo Picking", this.tremoloPicking,firstField) ;
        res += Util.fieldFor("palmMute", this.palmMute, firstField) ;
        res += Util.fieldFor("staccato", this.staccato, firstField) ;

        //fieldFor(String fieldName, Object object,Boolean firstField)
        /*
         * OLD if (this.graceNote != null) { res += "Grace note: " +
         * this.graceNote.toString(); } if (this.letRing) { res += ", Let Ring"; }
         * if (this.hammerOnPullOff) { res += ", hammerOn/PullOff"; } if
         * (this.bend != null) { res += ", Bend: " + this.bend.toString(); }
         * 
         * if (this.leftHandVibrato) { res += ", leftHandVibrato"; } if
         * (this.trill != null) { res += ", Trill: " + this.trill.toString(); }
         * 
         * if (this.harmonic != null) { res += ", Harmonic: " +
         * this.harmonic.toString(); }
         * 
         * if (this.slide != null) { res += ", Slide: " + this.slide.toString(); }
         * 
         * if (this.tremoloPicking != null) { res += ",Tremolo Picking: " +
         * this.tremoloPicking.toString(); } if (this.palmMute) { res +=
         * ",palmMute"; } if (this.staccato) { res += ",staccato"; }
         */
        return res;
    }
}