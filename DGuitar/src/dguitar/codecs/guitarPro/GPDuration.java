package dguitar.codecs.guitarPro;

/**
 * This class describes a duration read from a Guitar Pro file.
 * Please note there is still an undocumented value used
 * by this class.
 * 
 * @author Matthieu Wipliez
 * @see GPDuration#valueOf(int)
 */
public class GPDuration implements Comparable<Object> {
    /**
     * A duration is represented by an integer.
     */
    private int _type;

    /**
     * A whole note.
     */
    public static final GPDuration WHOLE = new GPDuration(-2);

    /**
     * A half note.
     */
    public static final GPDuration HALF = new GPDuration(-1);

    /**
     * A quarter note.
     */
    public static final GPDuration QUARTER = new GPDuration(0);

    /**
     * An eighth note.
     */
    public static final GPDuration EIGHTH = new GPDuration(1);

    /**
     * A sixteenth note.
     */
    public static final GPDuration SIXTEENTH = new GPDuration(2);

    /**
     * A thirty-second note.
     */
    public static final GPDuration THIRTY_SECOND = new GPDuration(3);

    /**
     * A sixty-fourth note.
     */
    public static final GPDuration SIXTY_FOURTH = new GPDuration(4);

    /**
     * A hundred twenty-eighth note.
     */
    public static final GPDuration HUNDRED_TWENTY_EIGHTH = new GPDuration(5);

    /**
     * An undocumented duration.
     */
    public static final GPDuration UNKNOWN_DURATION = new GPDuration(6);

    /**
     * The durations.
     */
    private static GPDuration[] _durations = { WHOLE, HALF, QUARTER, EIGHTH,
            SIXTEENTH, THIRTY_SECOND, SIXTY_FOURTH, HUNDRED_TWENTY_EIGHTH,
            UNKNOWN_DURATION };

    /**
     * The durations' names.
     */
    private static String[] _durationsNames = { "Whole note", "Half note",
            "Quarter note", "Eighth note", "Sixteenth note",
            "Thirty-second note", "Sixty-fourth note",
            "Hundred twenty-eighth note", "UNDOCUMENTED DURATION, VALUE = 6" };

    /**
     * Creates a new Duration whose type is <code>type</code>.
     * @param type the duration value.
     */
    private GPDuration(int type) {
        _type = type;
    }

    /**
     * Compares this duration to another object.
     * @param obj the object to compare this duration to.
     * @return the result of the comparison.
     * @see Comparable#compareTo(Object)
     */
    public int compareTo(Object obj) {
        return (obj instanceof GPDuration) ?
                ((GPDuration) obj)._type - _type
                : -1;
    }

    /**
     * Returns true if the given object is equal to this.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        return (obj instanceof GPDuration) ?
                _type == ((GPDuration) obj)._type
                : false;
    }

    /**
     * Returns this duration's index.
     * @return an index number, 0 for Whole note, 1 for half
     * note... and so on
     */
    public int getIndex() {
        return (_type + 2);
    }
    
    /**
     * Returns this duration's type.
     * @return the number that was read from the stream that
     * represents the duration.
     */
    public int getType() {
        return _type;
    }

    /**
     * Returns a string representation of this Duration.
     */
    public String toString() {
        return _durationsNames[_type + 2];
    }

    /**
     * Returns the GPDuration associated with the value
     * <code>type</code>.
     * 
     * @param type
     *            the duration integer value.
     * @return the duration object associated.
     */
    public static GPDuration valueOf(int type) {
        return _durations[type + 2];
    }
}