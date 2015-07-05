package dguitar.codecs.guitarPro;

/**
 * This class describes a bend's type read from a Guitar Pro file. <br />
 * Indeed, a bend can be a simple bend, a bend/release, a prebend, and so on.
 * 
 * @author Matthieu Wipliez
 */
public class GPBendType {
    /**
     * The bend type is represented by an integer.
     */
    private int _type;

    /**
     * NONE indicates a bend of type None.
     */
    public static final GPBendType NONE = new GPBendType(0);

    /**
     * BEND indicates a bend of type Bend.
     */
    public static final GPBendType BEND = new GPBendType(1);

    /**
     * BEND_RELEASE indicates a bend of type Bend and Release.
     */
    public static final GPBendType BEND_RELEASE = new GPBendType(2);

    /**
     * BEND_RELEASE_BEND indicates a bend of type Bend and Release and Bend.
     */
    public static final GPBendType BEND_RELEASE_BEND = new GPBendType(3);

    /**
     * PREBEND indicates a bend of type Prebend.
     */
    public static final GPBendType PREBEND = new GPBendType(4);

    /**
     * PREBEND_RELEASE indicates a bend of type Prebend and Release.
     */
    public static final GPBendType PREBEND_RELEASE = new GPBendType(5);

    /**
     * Tremolo bar specific: DIP indicates a bend of type Dip.
     */
    public static final GPBendType DIP = new GPBendType(6);

    /**
     * Tremolo bar specific: DIVE indicates a bend of type Dive.
     */
    public static final GPBendType DIVE = new GPBendType(7);

    /**
     * Tremolo bar specific: RELEASE_UP indicates a bend of type Release (up).
     */
    public static final GPBendType RELEASE_UP = new GPBendType(8);

    /**
     * Tremolo bar specific: INVERTED_DIP indicates a bend of type Inverted dip.
     */
    public static final GPBendType INVERTED_DIP = new GPBendType(9);

    /**
     * Tremolo bar specific: RETURN indicates a bend of type Return.
     */
    public static final GPBendType RETURN = new GPBendType(10);

    /**
     * Tremolo bar specific: RELEASE_DOWN indicates a bend of type Release
     * (down).
     */
    public static final GPBendType RELEASE_DOWN = new GPBendType(11);

    /**
     * The bend types.
     */
    private static GPBendType[] _bendTypes = { NONE, BEND, BEND_RELEASE,
            BEND_RELEASE_BEND, PREBEND, PREBEND_RELEASE, DIP, DIVE, RELEASE_UP,
            INVERTED_DIP, RETURN, RELEASE_DOWN };

    /**
     * The bend types' names.
     */
    private static String[] _bendTypesNames = { "None", "Bend",
            "Bend and Release", "Bend and Release and Bend", "Prebend",
            "Prebend and Release", "Dip", "Dive", "Release (up)",
            "Inverted dip", "Return", "Release (down)" };

    /**
     * Creates a new BendType whose type is <code>type</code>.
     * 
     * @param type
     *            the bend type
     */
    private GPBendType(int type) {
        _type = type;
    }

    /**
     * Returns true if the object equals this.
     */
    public boolean equals(Object o) {
        return (o instanceof GPBendType) ?
                _type == ((GPBendType) o)._type
                : false;
    }
    
    /**
     * Returns a string representation of this GPBendType.
     */
    public String toString() {
        return _bendTypesNames[_type];
    }
    
    /**
     * Returns the GPBendType associated with the value type.
     * 
     * @param type
     *            the bend's type integer value.
     * @return the bend's type associated.
     */
    public static GPBendType valueOf(int type) {
        return _bendTypes[type];
    }
}
