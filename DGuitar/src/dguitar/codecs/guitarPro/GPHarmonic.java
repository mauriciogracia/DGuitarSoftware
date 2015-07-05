package dguitar.codecs.guitarPro;

/**
 * This class describes an harmonic.
 * 
 * @author Matthieu Wipliez
 */
public class GPHarmonic {
    private int _index;

    /**
     * An harmonic is represented by an integer.
     */
    private int _type;

    /**
     * This declaration must be located before any other static final GPHarmonic
     */
    private static int[] harmonicValues = { 0, 1, 3, 4, 5, 15, 17, 22 };

    /**
     * None.
     */
    public static final GPHarmonic NONE = new GPHarmonic(0);

    /**
     * Natural.
     */
    public static final GPHarmonic NATURAL = new GPHarmonic(1);

    /**
     * Tapped.
     */
    public static final GPHarmonic TAPPED = new GPHarmonic(3);

    /**
     * Pitch.
     */
    public static final GPHarmonic PITCH = new GPHarmonic(4);

    /**
     * Semi.
     */
    public static final GPHarmonic SEMI = new GPHarmonic(5);

    /**
     * Artificial + 5.
     */
    public static final GPHarmonic ARTIFICIAL_5 = new GPHarmonic(15);

    /**
     * Artificial + 7.
     */
    public static final GPHarmonic ARTIFICIAL_7 = new GPHarmonic(17);

    /**
     * Artificial + 12.
     */
    public static final GPHarmonic ARTIFICIAL_12 = new GPHarmonic(22);

    /**
     * The harmonics.
     */
    private static GPHarmonic[] _harmonics = { NONE, NATURAL, TAPPED, PITCH,
            SEMI, ARTIFICIAL_5, ARTIFICIAL_7, ARTIFICIAL_12 };

    /**
     * The slides' names.
     */
    private static String[] _harmonicStrings = { "None", "Natural", "Tapped",
            "Pitch", "Semi", "Artificial + 5", "Artificial + 7",
            "Artificial + 12" };

    /**
     * Creates a new Harmonic whose type is <code>type</code>.
     * 
     * @param value
     *            the harmonic's value.
     */
    private GPHarmonic(int value) {
        _type = value;
        _index = indexOf(_type);
    }

    /**
     * Returns a string representation of this Harmonic.
     */
    public String toString() {
        String res;

        res = "Harmonic (type = " + _type + ", index = " + _index + ")";
        if ((_index >= 0) && (_index < harmonicValues.length)) {
            res = _harmonicStrings[_index];
        }
        return (res);
    }

    /**
     * Returns the index of the harmonic of type <code>value</code>.
     * 
     * @param value
     *            the harmonic type as an integer.
     * @return the harmonic index.
     */
    private static int indexOf(int value) {
        int i;
        boolean found;
        int res;

        found = false;
        res = -1;
        for (i = harmonicValues.length - 1; (!found) && (i >= 0); i--) {
            found = (harmonicValues[i] == value);
            if (found) {
                res = i;
            }
        }

        return res;
    }

    /**
     * Returns the GPHarmonic associated with the value <code>type</code>.
     * 
     * @param value
     *            the harmonic integer value.
     * @return the harmonic associated.
     */
    public static GPHarmonic valueOf(int value) {
        int i;
        GPHarmonic res;

        res = null;
        i = indexOf(value);
        if (i >= 0) {
            res = _harmonics[i];
        }
        return (res);
    }
}