package dguitar.codecs.guitarPro.version2;

import dguitar.codecs.guitarPro.GPHarmonic;

/**
 * The class GP2EffectsOnBeat describes effects set on a beat in Guitar Pro
 * 2 that are to be set on all notes in the beat (as done by Guitar Pro 4).
 * 
 * One can note that the effect present in this class are ALL effects that
 * concern notes... I love those Guitar Pro folks ;)
 * 
 * @author Matthieu Wipliez
 */
class GP2EffectsOnBeat {
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
    public GP2EffectsOnBeat() {
        harmonic = null;
        vibrato = false;
        wideVibrato = false;
    }

    /**
     * @return true if there is at least one effect set in this beat.
     */
    public boolean hasEffects() {
        return (harmonic != null) || vibrato || wideVibrato;
    }
}
