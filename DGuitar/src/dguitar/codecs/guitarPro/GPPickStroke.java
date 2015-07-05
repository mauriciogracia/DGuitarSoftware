package dguitar.codecs.guitarPro;

/**
 * This class describes a pickstroke.
 * 
 * @author Matthieu Wipliez
 */
public class GPPickStroke {
	/**
	 * A pickstroke is represented by an integer.
	 */
	private int _type;
	
	/**
	 * None.
	 */
	public static final GPPickStroke NONE = new GPPickStroke(0);
	
	/**
	 * Upward.
	 */
	public static final GPPickStroke UPWARD = new GPPickStroke(1);
	
	/**
	 * Downward.
	 */
	public static final GPPickStroke DOWNWARD = new GPPickStroke(2);

	/**
	 * The pickstrokes.
	 */
	private static GPPickStroke[] _pickStrokes = {
		NONE, 
		UPWARD,
		DOWNWARD
	};
	
	/**
	 * The pickstrokes' names.
	 */
	private static String[] _pickStrokesNames = {
		"None",
		"Upward",
		"Downward"
	};

	/**
	 * Creates a new PickStroke whose type is <code>type</code>.
	 * @param type the PickStroke type.
	 */
	private GPPickStroke(int type) {
		_type = type;
	}
	
	/**
	 * Returns a string representation of this GPPickStroke.
	 */
	public String toString() {
		return _pickStrokesNames[_type];
	}

    /**
     * Returns the GPPickStroke associated with the value
     * <code>type</code>.
     * 
     * @param type
     *            the pickstroke integer value.
     * @return the pickstroke object associated.
     */
    public static GPPickStroke valueOf(int type) {
        return _pickStrokes[type];
    }
}
