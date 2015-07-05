package dguitar.codecs.guitarPro;

/**
 * This class describes a note belonging to a chord diagram.
 * 
 * @author Matthieu Wipliez
 */
public class GPChordNote {
    /**
     * A chord note is represented by an integer.
     */
    private int _type;

    /**
     * User.
     */
    public static final GPChordNote USER = new GPChordNote(-1);

    /**
     * C.
     */
    public static final GPChordNote C = new GPChordNote(0);

    /**
     * C sharp.
     */
    public static final GPChordNote C_SHARP = new GPChordNote(1);

    /**
     * D.
     */
    public static final GPChordNote D = new GPChordNote(2);

    /**
     * D sharp.
     */
    public static final GPChordNote D_SHARP = new GPChordNote(3);

    /**
     * E.
     */
    public static final GPChordNote E = new GPChordNote(4);

    /**
     * F.
     */
    public static final GPChordNote F = new GPChordNote(5);

    /**
     * F sharp.
     */
    public static final GPChordNote F_SHARP = new GPChordNote(6);

    /**
     * G.
     */
    public static final GPChordNote G = new GPChordNote(7);

    /**
     * G sharp.
     */
    public static final GPChordNote G_SHARP = new GPChordNote(8);

    /**
     * A.
     */
    public static final GPChordNote A = new GPChordNote(9);

    /**
     * A sharp.
     */
    public static final GPChordNote A_SHARP = new GPChordNote(10);

    /**
     * B.
     */
    public static final GPChordNote B = new GPChordNote(11);

    /**
     * The allowed chord notes.
     */
    private static GPChordNote[] _chordNotes = { USER, C, C_SHARP, D, D_SHARP,
            E, F, F_SHARP, G, G_SHARP, A, A_SHARP, B

    };

    /**
     * The allowed chord notes names.
     */
    private static String[] _chordNames = { "User", "C", "C#", "D", "D#", "E",
            "F", "F#", "G", "G#", "A", "A#", "B" };

    /**
     * Creates a new GPChordNote whose type is <code>type</code>.
     * 
     * @param type
     *            the GPChordNote's type
     */
    private GPChordNote(int type) {
        _type = type;
    }

    /**
     * Returns a string representation of this GPChordNote.
     */
    public String toString() {
        return _chordNames[_type + 1];
    }

    /**
     * Returns the GPChordNote associated with the value <code>type</code>.
     * 
     * @param value
     *            the chord note integer value.
     * @return the chord note object associated.
     */
    public static GPChordNote valueOf(int value) {
        if (value > 11) {
            value = -1;
        }
        return (_chordNotes[value + 1]);
    }
}