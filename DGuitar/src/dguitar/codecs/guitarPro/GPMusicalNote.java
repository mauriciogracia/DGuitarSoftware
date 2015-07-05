package dguitar.codecs.guitarPro;

/**
 * This class describes how is represented a musical note.
 * It is quite different from the GPNote class, as this one
 * only contain one integer representing how a note is encoded,
 * that is to say what is a C, what is a D...
 * 
 * @author Matthieu Wipliez
 */
public class GPMusicalNote {
    /**
     * A note is represented by a positive integer. <br>
     * Each incrementation represents a half tone.
     */
    private int _note;

    /**
     * Creates a new GPMusicalNote.
     * 
     * @param note
     */
    public GPMusicalNote(int note) {
        _note = note;
    }

    /**
     * Returns the note.
     * 
     * @return the note.
     */
    public int getNote() {
        return _note;
    }

    /**
     * Sets the note.
     * 
     * @param note
     *            the note to set.
     */
    public void setNote(int note) {
        _note = note;
    }

    /**
     * Returns a string representation of this GPMusicalNote.
     */
    public String toString() {
        return "Note: " + _note;
    }
}