package dguitar.codecs.guitarPro;

/**
 * This class describes effects present on a beat.
 * 
 * @author Matthieu Wipliez
 */
public class GPEffectsOnBeat {
    /**
     * Downstroke possibly associated with this effect.
     */
    public GPDuration downStroke;
    
    /**
     * Presence of a "fade in".
     */
    public boolean fadeIn;

    /**
     * Presence of a rasgueado.
     */
    public boolean hasRasgueado;

    /**
     * Pickstroke possibly associated with this effect.
     */
    public GPPickStroke pickStrokes;

    /**
     * Presence of a popping effect (bass guitar).
     */
    public boolean popping;

    /**
     * Presence of a slapping effect.
     */
    public boolean slapping;

    /**
     * Presence of a tapping effect.
     */
    public boolean tapping;

    /**
     * Tremolo bar possibly associated with this effect.
     */
    public GPBend tremoloBar;

    /**
     * Upstroke possibly associated with this effect.
     */
    public GPDuration upStroke;

    /**
     * Creates a new GPEffectsOnBeat.
     * 
     */
    public GPEffectsOnBeat() {
        downStroke = null;
        hasRasgueado = false;
        pickStrokes = null;
        // OLD pickStrokes = GPPickStroke.NONE;
        popping = false;
        slapping = false;
        tapping = false;
        tremoloBar = null;
        upStroke = null;
    }

    /**
     * Returns a string representation of this GPEffectsOnBeat.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String res;

        res = null;
        if (downStroke != null) {
            res = "downStroke" + ": " + downStroke.toString();
        }
        if (hasRasgueado) {
            res += ", hasRasgueado";
        }
        if (pickStrokes != null) {
            res += ", pickStrokes" + ": " + pickStrokes.toString();
        }
        if (popping) {
            res += ", popping";
        }
        if (slapping) {
            res += ", slapping";
        }
        if (tapping) {
            res += ", tapping";
        }
        if (tremoloBar != null) {
            res += "tremoloBar" + ": " + tremoloBar.toString();
        }
        if (upStroke != null) {
            res += ", upStroke" + ": " + upStroke.toString();
        }

        return res;
    }
}