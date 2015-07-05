package dguitar.codecs.guitarPro;

/**
 * This class describes a vibrato.
 * 
 * @author Matthieu Wipliez
 */
public class GPVibrato {
	/**
	 * A vibrato is represented by an integer.
	 */
	private int _type;

	/**
	 * None.
	 */
	public static final GPVibrato NONE = new GPVibrato(0);

	/**
	 * Fast.
	 */
	public static final GPVibrato FAST = new GPVibrato(1);

	/**
	 * Average.
	 */
	public static final GPVibrato AVERAGE = new GPVibrato(2);

	/**
	 * Slow.
	 */
	public static final GPVibrato SLOW = new GPVibrato(3);

	/**
	 * The vibratos.
	 */
	private static GPVibrato[] _vibratos = {
		NONE,
		FAST,
		AVERAGE,
		SLOW
	};
	
	/**
	 * The vibratos' names.
	 */
	private static String[] _vibratosNames = {
		"None",
		"Fast",
		"Average",
		"Slow"
	};
	
	/**
	 * Creates a new GPVibrato whose type is <code>type</code>.
	 * @param type this vibrato's integer type.
	 */
	private GPVibrato(int type) {
		_type = type;
	}
	
	/**
	 * Returns a string representation of this GPVibrato.
	 */
	public String toString() {
		return _vibratosNames[_type];
	}

    /**
     * Returns the GPVibrato associated with the value
     * <code>type</code>.
     * 
     * @param type
     *            the vibrato integer value.
     * @return the vibrato object associated.
     */
    public static GPVibrato valueOf(int type) {
        return _vibratos[type];
    }
}