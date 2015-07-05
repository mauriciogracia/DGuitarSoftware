package dguitar.codecs.guitarPro;

/**
 * This class describes a grace note.
 * 
 * @author Matthieu Wipliez
 */
public class GPGraceNote {

    /**
     * The duration of the grace note.
     */
    private GPDuration _duration;

    /**
     * The dynamic of the grace note.
     */
    private GPDynamic _dynamic;

    /**
     * The fret number of the grace note.
     */
    private int _fret;

    /**
     * The transition of the grace note.
     */
    private GPGraceNoteTransition _transition;

    /**
     * Creates a new GPGraceNote.
     * 
     */
    public GPGraceNote() {
        _duration = GPDuration.QUARTER;
        _dynamic = GPDynamic.F;
        _fret = 0;
        _transition = GPGraceNoteTransition.NONE;
    }

    /**
     * Returns this grace note's duration.
     * 
     * @return this grace note's duration.
     * @see GPDuration
     */
    public GPDuration getDuration() {
        return _duration;
    }

    /**
     * Returns this grace note's dynamic.
     * 
     * @return this grace note's dynamic.
     * @see GPDynamic
     */
    public GPDynamic getDynamic() {
        return _dynamic;
    }

    /**
     * Returns this grace note's fret.
     * 
     * @return this grace note's fret.
     */
    public int getFret() {
        return _fret;
    }

    /**
     * Returns this grace note's transition.
     * 
     * @return this grace note's transition.
     * @see GPGraceNoteTransition
     */
    public GPGraceNoteTransition getTransition() {
        return _transition;
    }

    /**
     * Sets this grace note's duration.
     * 
     * @param duration
     *            the duration to set.
     * @see GPDuration
     */
    public void setDuration(GPDuration duration) {
        _duration = duration;
    }

    /**
     * Sets this grace note's dynamic.
     * 
     * @param dynamic
     *            the dynamic to set.
     * @see GPDynamic
     */
    public void setDynamic(GPDynamic dynamic) {
        _dynamic = dynamic;
    }

    /**
     * Sets this grace note's fret.
     * 
     * @param fret
     *            the fret to set.
     */
    public void setFret(int fret) {
        _fret = fret;
    }

    /**
     * Sets this grace note's transition.
     * 
     * @param transition
     *            the transition to set.
     * @see GPGraceNoteTransition
     */
    public void setTransition(GPGraceNoteTransition transition) {
        _transition = transition;
    }

    /**
     * Returns a string representation of the GPGraceNote.
     */
    public String toString() {
        String res;

        res = "Fret: " + _fret;
        res += ",Dynamic: " + _dynamic.toString();
        res += ",Transition: " + _transition.toString();
        res += ",Duration: " + _duration.toString();

        return res;
    }
}