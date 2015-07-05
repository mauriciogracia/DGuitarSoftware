package dguitar.codecs.guitarPro;

/**
 * This class describes a tonality type.
 * 
 * @author Matthieu Wipliez
 */
public class GPTonalityType {
    /**
     * A tonality type is represented by an integer.
     */
    private int _type;

    /**
     * Perfect.
     */
    public static final GPTonalityType PERFECT = new GPTonalityType(0);

    /**
     * Augmented.
     */
    public static final GPTonalityType DIMINISHED = new GPTonalityType(1);

    /**
     * Diminished.
     */
    public static final GPTonalityType AUGMENTED = new GPTonalityType(2);

    /**
     * The tonality types.
     */
    private static GPTonalityType[] _tonalityTypes = { PERFECT, DIMINISHED,
            AUGMENTED };

    /**
     * The tonality types's names.
     */
    private static String[] _tonalityTypeStrings = { "perfect", "diminished",
            "augmented" };

    /**
     * Creates a new TonalityType whose type is <code>type</code>.
     * @param type
     */
    private GPTonalityType(int type) {
        _type = 0;
    }

    /**
     * Returns a string representation of this GPTonality
     */
    public String toString() {
        return _tonalityTypeStrings[_type];
    }

    /**
     * Returns the GPTonalityType associated with the value
     * <code>type</code>.
     * 
     * @param type
     *            the tonality type integer value.
     * @return the tonality type object associated.
     */
    public static GPTonalityType valueOf(int type) {
        return _tonalityTypes[type];
    }
}