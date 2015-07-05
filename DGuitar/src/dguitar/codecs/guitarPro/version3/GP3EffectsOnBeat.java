package dguitar.codecs.guitarPro.version3;

import dguitar.codecs.guitarPro.GPHarmonic;

/**
 * This class describes effects that are set on a beat in 
 * Guitar Pro 3 files, but that are to be set for each note
 * in this beat according to the GP4 format.
 * 
 * @author Matthieu Wipliez
 */
public class GP3EffectsOnBeat {
    /**
     * An harmonic presence
     */
    public GPHarmonic harmonic;

    /**
     * The "vibrato" effect presence.
     */
    public boolean vibrato;

    /**
     * The "wide vibrato" effect presence.
     */
    public boolean wideVibrato;

    /**
     * Creates a new GP3EffectsOnBeat with no effects set.
     * 
     */
    public GP3EffectsOnBeat() {
        harmonic = null;
        vibrato = false;
        wideVibrato = false;
    }

    /**
     * Returns true if there is at least one effect set in this beat.
     * @return true if there is at least one effect set in this beat.
     */
    public boolean hasEffects() {
        return (harmonic != null) || vibrato || wideVibrato;
    }
}
