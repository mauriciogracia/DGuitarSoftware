package dguitar.codecs.guitarPro;

/**
 * This class describes the type of a chord (ie major, minor,
 * 7M...).
 * 
 * @author Matthieu Wipliez
 */
public class GPChordType {
    /**
     * A chord type is represented by an integer.
     */
    private int _type;

    /**
     * A major chord.
     */
    public static final GPChordType MAJOR = new GPChordType(0);

    /**
     * A 7th chord.
     */
    public static final GPChordType SEVEN = new GPChordType(1);

    /**
     * A 7th major chord
     */
    public static final GPChordType SEVEN_MAJOR = new GPChordType(2);

    /**
     * A 6th chord.
     */
    public static final GPChordType SIX = new GPChordType(3);

    /**
     * A minor chord.
     */
    public static final GPChordType MINOR = new GPChordType(4);

    /**
     * A 7th minor chord.
     */
    public static final GPChordType MINOR_SEVEN = new GPChordType(5);

    /**
     * A 7th minor major chord.
     */
    public static final GPChordType MINOR_SEVEN_MAJOR = new GPChordType(6);

    /**
     * A 6th minor chord.
     */
    public static final GPChordType MINOR_SIX = new GPChordType(7);

    /**
     * A sus2 chord.
     */
    public static final GPChordType SUS2 = new GPChordType(8);

    /**
     * A sus4 chord.
     */
    public static final GPChordType SUS4 = new GPChordType(9);

    /**
     * A 7th sus2 chord.
     */
    public static final GPChordType SEVEN_SUS2 = new GPChordType(10);

    /**
     * A 7th sus4 chord.
     */
    public static final GPChordType SEVEN_SUS4 = new GPChordType(11);

    /**
     * A diminished chord.
     */
    public static final GPChordType DIMINISHED = new GPChordType(12);

    /**
     * An augmented chord.
     */
    public static final GPChordType AUGMENTED = new GPChordType(13);

    /**
     * A 5th chord.
     */
    public static final GPChordType FIFTH = new GPChordType(14);

    /**
     * The chord types.
     */
    private static GPChordType[] _chordTypes = { MAJOR, SEVEN, SEVEN_MAJOR, SIX, MINOR, MINOR_SEVEN,
            MINOR_SEVEN_MAJOR, MINOR_SIX, SUS2, SUS4, SEVEN_SUS2, SEVEN_SUS4, DIMINISHED, AUGMENTED, FIFTH };

    /**
     * The chord types names.
     */
    private static String[] _chordTypeNames = { "M", "7", "7M", "6", "m", "m7",
            "m7M", "m6", "sus2", "sus4", "7sus2", "7sus4", "dim", "aug", "5" };

    /**
     * Creates a new ChordType whose type is <code>type</code>.
     * @param type
     */
    private GPChordType(int type) {
        _type = type;
    }

    /**
     * Returns a string representation of this ChordDiagram.
     */
    public String toString() {
        return _chordTypeNames[_type];
    }

    /**
     * Returns the GPChordType associated with the value
     * <code>type</code>.
     * 
     * @param type
     *            the chord type integer value.
     * @return the chord type associated.
     */
    public static GPChordType valueOf(int type) {
        return _chordTypes[type];
    }
}