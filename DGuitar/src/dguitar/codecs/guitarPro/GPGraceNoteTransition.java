package dguitar.codecs.guitarPro;

/**
 * This class describes the transitions for a grace note.
 * 
 * @author Matthieu Wipliez
 */
public class GPGraceNoteTransition {
    /**
     * A grace note transition is represented by an integer.
     */
    private int _type;

    /**
     * None.
     */
    public static final GPGraceNoteTransition NONE = new GPGraceNoteTransition(
            0);

    /**
     * Slide.
     */
    public static final GPGraceNoteTransition SLIDE = new GPGraceNoteTransition(
            1);

    /**
     * Bend.
     */
    public static final GPGraceNoteTransition BEND = new GPGraceNoteTransition(
            2);

    /**
     * Hammer.
     */
    public static final GPGraceNoteTransition HAMMER = new GPGraceNoteTransition(
            3);

    /**
     * The transitions.
     */
    private static GPGraceNoteTransition[] _transitions = { NONE, SLIDE, BEND,
            HAMMER };

    /**
     * The durations' names.
     */
    private static String[] _transitionsNames = { "None", "Slide", "Bend",
            "Hammer" };

    /**
     * Creates a new GraceNoteTransition whose type is <code>type</code>.
     * 
     * @param type
     *            the grace note transition type as an integer.
     */
    private GPGraceNoteTransition(int type) {
        _type = type;
    }

    /**
     * Returns a string representation of this GPGraceNoteTransition.
     */
    public String toString() {
        return _transitionsNames[_type];
    }

    /**
     * Returns the GPGraceNoteTransition associated with the value <code>type</code>.
     * 
     * @param type
     *            the grace note transition integer value.
     * @return the grace note transition associated.
     */
    public static GPGraceNoteTransition valueOf(int type) {
        return _transitions[type];
    }
}
