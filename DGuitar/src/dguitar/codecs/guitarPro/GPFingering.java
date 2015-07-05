package dguitar.codecs.guitarPro;

/**
 * This class describes the fingering for a chord.
 * 
 * @author Matthieu Wipliez
 */
public class GPFingering {
	/**
	 * A Fingering is represented by an integer.
	 */
	private int _type;

	/**
	 * Unknown finger.
	 */
	public static final GPFingering UNKNOWN = new GPFingering(-2);

	/**
	 * X or no finger.
	 */
	public static final GPFingering NO_FINGER = new GPFingering(-1);

	/**
	 * Thumb.
	 */
	public static final GPFingering THUMB = new GPFingering(0);

	/**
	 * Index.
	 */
	public static final GPFingering INDEX = new GPFingering(1);

	/**
	 * Middle finger.
	 */
	public static final GPFingering MIDDLE_FINGER = new GPFingering(2);

	/**
	 * Ring finger.
	 */
	public static final GPFingering RING_FINGER = new GPFingering(3);

	/**
	 * Little finger.
	 */
	public static final GPFingering LITTLE_FINGER = new GPFingering(4);

	/**
	 * The fingerings.
	 */
	private static GPFingering[] _fingerings = {
		UNKNOWN,
		NO_FINGER,
		THUMB,
		INDEX,
		MIDDLE_FINGER,
		RING_FINGER,
		LITTLE_FINGER
	};
	
	/**
	 * The fingerings' names.
	 */
	private static String[] _fingeringsNames = {
		"Unknown",
		"No finger",
		"Thumb",
		"Index",
		"Middle finger",
		"Ring finger",
		"Little finger"
	};

	/**
	 * Creates a new Fingering whose type is <code>type</code>.
	 * @param type
	 */
	private GPFingering(int type) {
		_type = type;
	}
  
	/**
	 * Returns a string representation of a Fingering.
	 */
	public String toString() {
		return _fingeringsNames[_type + 2];
	}
    
    /**
     * Returns the GPFingering associated with the value
     * <code>type</code>.
     * 
     * @param type
     *            the fingering integer value.
     * @return the fingering object associated.
     */
    public static GPFingering valueOf(int type) {
        return _fingerings[type + 2];
    }
}
