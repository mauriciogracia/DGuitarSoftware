package dguitar.codecs.guitarPro;

/**
 * This class describes a note's dynamic.
 * 
 * @author Matthieu Wipliez
 */
public class GPDynamic {
    /**
     * A note's dynamic is represented by an integer.
     */
    private int _type;

    /**
     * An invalid dynamic value.
     */
    public static final GPDynamic INVALID = new GPDynamic(0);

    /**
     * piano pianissimo
     */
    public static final GPDynamic PPP = new GPDynamic(1);

    /**
     * pianissimo
     */
    public static final GPDynamic PP = new GPDynamic(2);

    /**
     * piano
     */
    public static final GPDynamic P = new GPDynamic(3);

    /**
     * mezzo piano
     */
    public static final GPDynamic MP = new GPDynamic(4);

    /**
     * mezzo forte
     */
    public static final GPDynamic MF = new GPDynamic(5);

    /**
     * forte
     */
    public static final GPDynamic F = new GPDynamic(6);

    /**
     * fortissimo
     */
    public static final GPDynamic FF = new GPDynamic(7);

    /**
     * forte fortissimo
     */
    public static final GPDynamic FFF = new GPDynamic(8);

    /**
     * The different dynamics.
     */
    private static final GPDynamic[] dynamics = { INVALID, PPP, PP, P, MP, MF,
            F, FF, FFF };

    /**
     * The different dynamics's names.
     */
    private static final String[] dynamicsNames = { "INVALID", "PPP", "PP",
            "P", "MP", "MF", "F", "FF", "FFF" };

    /**
     * Creates a new GPDynamic whose type is <code>type</code>.
     * 
     * @param type this dynamic's integer value.
     */
    private GPDynamic(int type) {
        _type = type;
    }

    /**
     * Returns an index that represents the dynamic <br>
     * 0 = INVALID <br>
     * 1 = PPP <br>
     * 2 = PP <br>
     * and so on.
     */
    public int getIndex() {
        return _type;
    }

    /**
     * Returns a string representation of this Dynamic.
     */
    public String toString() {
        return dynamicsNames[_type];
    }

    /**
     * Returns the GPDynamic associated with the value
     * <code>type</code>.
     * 
     * @param type
     *            the dynamic integer value.
     * @return the dynamic object associated.
     */
    public static GPDynamic valueOf(int type) {
        return dynamics[type];
    }
}