package dguitar.codecs.guitarPro;

/**
 * This class describes the tonality of a measure.
 * 
 * @author Matthieu
 */
public class GPKey {
    /**
     * The measure tonality mode: major or minor.
     */
    private int _mode;

    /**
     * A measure tonality is represented by an integer.
     */
    private int _type;

    /**
     * C Flat
     */
    public static final GPKey C_FLAT = new GPKey(0, -7);

    /**
     * G Flat
     */
    public static final GPKey G_FLAT = new GPKey(0, -6);

    /**
     * D Flat
     */
    public static final GPKey D_FLAT = new GPKey(0, -5);

    /**
     * A Flat
     */
    public static final GPKey A_FLAT = new GPKey(0, -4);

    /**
     * E Flat
     */
    public static final GPKey E_FLAT = new GPKey(0, -3);

    /**
     * B Flat
     */
    public static final GPKey B_FLAT = new GPKey(0, -2);

    /**
     * F
     */
    public static final GPKey F = new GPKey(0, -1);

    /**
     * C
     */
    public static final GPKey C = new GPKey(0, 0);

    /**
     * G
     */
    public static final GPKey G = new GPKey(0, 1);

    /**
     * D
     */
    public static final GPKey D = new GPKey(0, 2);

    /**
     * A
     */
    public static final GPKey A = new GPKey(0, 3);

    /**
     * E
     */
    public static final GPKey E = new GPKey(0, 4);

    /**
     * B
     */
    public static final GPKey B = new GPKey(0, 5);

    /**
     * F sharp
     */
    public static final GPKey F_SHARP = new GPKey(0, 6);

    /**
     * C sharp
     */
    public static final GPKey C_SHARP = new GPKey(0, 7);

    /**
     * A flat minor
     */
    public static final GPKey A_FLAT_MINOR = new GPKey(1, -7);

    /**
     * E flat minor
     */
    public static final GPKey E_FLAT_MINOR = new GPKey(1, -6);

    /**
     * B flat minor
     */
    public static final GPKey B_FLAT_MINOR = new GPKey(1, -5);

    /**
     * F minor
     */
    public static final GPKey F_MINOR = new GPKey(1, -4);

    /**
     * C minor
     */
    public static final GPKey C_MINOR = new GPKey(1, -3);

    /**
     * G minor
     */
    public static final GPKey G_MINOR = new GPKey(1, -2);

    /**
     * D minor
     */
    public static final GPKey D_MINOR = new GPKey(1, -1);

    /**
     * A minor
     */
    public static final GPKey A_MINOR = new GPKey(1, 0);

    /**
     * E minor
     */
    public static final GPKey E_MINOR = new GPKey(1, 1);

    /**
     * B minor
     */
    public static final GPKey B_MINOR = new GPKey(1, 2);

    /**
     * F sharp minor
     */
    public static final GPKey F_SHARP_MINOR = new GPKey(1, 3);

    /**
     * C sharp minor
     */
    public static final GPKey C_SHARP_MINOR = new GPKey(1, 4);

    /**
     * G sharp minor
     */
    public static final GPKey G_SHARP_MINOR = new GPKey(1, 5);

    /**
     * D sharp minor
     */
    public static final GPKey D_SHARP_MINOR = new GPKey(1, 6);

    /**
     * A sharp minor
     */
    public static final GPKey A_SHARP_MINOR = new GPKey(1, 7);

    /**
     * The keys
     */
    private static GPKey[][] _keys = {
            { C_FLAT, G_FLAT, D_FLAT, A_FLAT, E_FLAT, B_FLAT, F, C, G, D, A, E,
                    B, F_SHARP, C_SHARP },

            { A_FLAT_MINOR, E_FLAT_MINOR, B_FLAT_MINOR, F_MINOR, C_MINOR,
                    G_MINOR, D_MINOR, A_MINOR, E_MINOR, B_MINOR, F_SHARP_MINOR,
                    C_SHARP_MINOR, G_SHARP_MINOR, D_SHARP_MINOR, A_SHARP_MINOR }

    };

    /**
     * The keys' names
     */
    private static String[][] _keyNames = {
            { "C_FLAT", "G_FLAT", "D_FLAT", "A_FLAT", "E_FLAT", "B_FLAT", "F",
                    "C", "G", "D", "A", "E", "B", "F_SHARP", "C_SHARP" },

            { "A_FLAT_MINOR", "E_FLAT_MINOR", "B_FLAT_MINOR", "F_MINOR",
                    "C_MINOR", "G_MINOR", "D_MINOR", "A_MINOR", "E_MINOR",
                    "B_MINOR", "F_SHARP_MINOR", "C_SHARP_MINOR",
                    "G_SHARP_MINOR", "D_SHARP_MINOR", "A_SHARP_MINOR" }

    };

    /**
     * Creates a new Key whose type is <code>type</code>.
     * 
     * @param type
     */
    private GPKey(int mode, int type) {
        _mode = mode;
        _type = type;
    }

    /**
     * Returns a string representation of this Key.
     */
    public String toString() {
        return _keyNames[_mode][_type + 7];
    }

    /**
     * Returns the GPKey associated with the value type. (mode = 0).
     * 
     * @param type
     *            the key's type integer value.
     * @return the key associated.
     */
    public static GPKey valueOf(int type) {
        return valueOf(0, type);
    }

    /**
     * Returns the GPKey associated with the values mode and type.
     * 
     * @param mode
     *            the key's mode integer value.
     * @param type
     *            the key's type integer value.
     * @return the key associated.
     */
    public static GPKey valueOf(int mode, int type) {
        return _keys[mode][type + 7];
    }

}
