package dguitar.codecs.guitarPro;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describes a Beat read from a Guitar Pro file. <br />
 * A beat can contain several notes, and can hold information about a chord
 * diagram, a mix table change event, a text marker and effects.
 * 
 * @author Matthieu Wipliez
 */
public class GPBeat {
    /**
     * Whether this beat is empty.
     */
    private boolean _emptyBeat;

    /**
     * Whether this beat is a rest.
     */
    private boolean _restBeat;

    /**
     * This beat's duration.
     * @see GPDuration
     */
    private GPDuration _duration;

    /**
     * Notes possibly associated with this beat.
     */
    private List<GPNote> _notes;

    /**
     * When the beat is a n-tuplet.
     */
    private int _nTuplet;

    /**
     * The strings played in this beat. <br>
     * 7 strings maximum can be played. <br>
     * The higher the index, the higher the string.
     */
    private boolean[] _strings;

    /**
     * Chord diagram possibly associated with this beat.
     * @see GPChordDiagram
     */
    public GPChordDiagram chordDiagram;

    /**
     * Whether the notes contained in this beat are dotted notes.
     */
    public boolean dottedNotes;
    
    /**
     * Effects possibly associated with this beat.
     * @see GPEffectsOnBeat
     */
    public GPEffectsOnBeat effects;

    /**
     * Mix table change possibly associated with this beat.
     * @see GPMixTableChange
     */
    public GPMixTableChange mixTableChange;

    /**
     * Text possibly associated with this beat.
     */
    public String text;

    /**
     * Creates a new Beat.
     * 
     */
    public GPBeat() {
        chordDiagram = null;
        dottedNotes = false;
        _duration = GPDuration.QUARTER;
        effects = null;
        _emptyBeat = false;
        mixTableChange = null;
        _notes = new ArrayList<GPNote>(0);
        _nTuplet = 0;
        _restBeat = false;
        _strings = new boolean[7];
        text = "";
    }

    /**
     * Returns true if the given object is equals to this Beat.
     */
    public boolean equals(Object o) {
        GPBeat other;
        boolean resp;

        resp = false;
        if ((o != null) && (o.getClass().isInstance(this))) {
            other = (GPBeat) o;
            resp = _emptyBeat == other._emptyBeat;
            resp = resp && (_restBeat == other._restBeat);
            resp = resp && (dottedNotes == other.dottedNotes);
            resp = resp && (_duration == other._duration);
            resp = resp && _notes.equals(other._notes);
        }
        return resp;
    }

    /**
     * Returns this beat's duration.
     * 
     * @return this beat's duration.
     * @see GPDuration
     */
    public GPDuration getDuration() {
        return _duration;
    }

    /**
     * Returns the notes contained in this beat.
     * 
     * @return a list of the notes contained in this beat.
     */
    public List<GPNote> getNotes() {
        return _notes;
    }

    /**
     * Returns the number of the n-tuplet (3, 5, 7...).
     * 
     * @return the number of the n-tuplet (3, 5, 7...).
     */
    public int getNTuplet() {
        return _nTuplet;
    }

    /**
     * Returns true if this beat is empty.
     * 
     * @return whether this beat is empty.
     */
    public boolean isEmptyBeat() {
        return _emptyBeat;
    }

    /**
     * Returns true if this beat contains at least one note.
     * 
     * @return true if this beat contains at least one note. This occurs when
     *         the beat is not empty and does not contain only a rest.
     */
    public boolean isNoteBeat() {
        return ((!isEmptyBeat()) && (!isRestBeat()));
    }

    /**
     * Returns true if this beat is a rest beat.
     * 
     * @return whether this beat is a rest beat.
     */
    public boolean isRestBeat() {
        return _restBeat;
    }

    /**
     * Returns true if the string i is played.
     * 
     * @return true if the string i is played.
     */
    public boolean isStringPlayed(int i) {
        return _strings[i];
    }

    /**
     * Sets this beat's duration.
     * 
     * @param duration
     *            the duration to set.
     * @see GPDuration
     */
    public void setDuration(GPDuration duration) {
        _duration = duration;
    }

    /**
     * Sets if this beat is empty or not (according to emptyBeat).
     * 
     * @param emptyBeat
     *            the boolean value.
     */
    public void setEmptyBeat(boolean emptyBeat) {
        _emptyBeat = emptyBeat;
    }

    /**
     * Sets the number of the n-tuplet (3, 5, 7...).
     * 
     * @param tuplet
     *            the number of the n-tuplet
     */
    public void setNTuplet(int tuplet) {
        _nTuplet = tuplet;
    }

    /**
     * Sets this beat to be a rest or not according to restBeat.
     * 
     * @param restBeat
     *            the boolean value indicating if the beat is a rest beat.
     */
    public void setRestBeat(boolean restBeat) {
        _restBeat = restBeat;
    }

    /**
     * Changes the status of the string i.
     * 
     * @param i
     *            the index of the string.
     * @param string
     *            true if this string is played.
     */
    public void setString(int i, boolean string) {
        _strings[i] = string;
    }

    /**
     * Returns a string representation of the Beat.
     */
    public String toString() {
        String res;
        int i;

        res = "\tBeat - BEGIN";

        if (chordDiagram != null) {
            res += "\n\t\t" + chordDiagram.toString();
        }
        res += "\n\t\tdotted notes: " + this.dottedNotes;
        res += "\n\t\tduration: " + _duration.toString();
        if (effects != null) {
            res += "\n\t\t" + effects.toString();
        }
        res += "\n\t\tEmpty Beat:" + _emptyBeat;
        if (mixTableChange != null) {
            res += "\n\t\tMixTableChange" + mixTableChange.toString();
        }
        if (_notes != null) {
            res += "\n\t\tNotes: ";
            for (i = 0; i < _notes.size(); i++) {
                res += "\n\t\t\t" + _notes.get(i).toString();
            }
        }
        res += "\n\t\tnTuplet: " + _nTuplet;
        res += "\n\t\tRest Beat:" + _restBeat;
        res += "\n\t\tStrings played: ";
        for (i = 6; i >= 0; i--) {
            if (_strings[i]) {
                res += "" + i;
                if (i - 1 >= 0)
                    res += ",";
            }
        }
        res += "\n\t\tText: " + text;
        res += "\n\tBeat - END";

        return (res);
    }
}
