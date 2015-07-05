package dguitar.codecs.guitarPro;

/**
 * This class describes a slide.
 * 
 * @author Matthieu Wipliez
 */
public class GPSlide {
	/**
	 * A slide is represented by an integer.
	 */
	private int _type;
	
	/**
	 * Slide into from above.
	 */
	public static final GPSlide FROM_ABOVE = new GPSlide(-2);
	
	/**
	 * Slide into from below.
	 */
	public static final GPSlide FROM_BELOW = new GPSlide(-1);
	
	/**
	 * No slide
	 */
	public static final GPSlide NO_SLIDE = new GPSlide(0);
	
	/**
	 * Shift slide.
	 */
	public static final GPSlide SHIFT_SLIDE = new GPSlide(1);
	
	/**
	 * Legato slide.
	 */
	public static final GPSlide LEGATO_SLIDE = new GPSlide(2);

	/**
	 * Slide out of downwards.
	 */
	public static final GPSlide OUT_DOWNWARD = new GPSlide(3);
	
	/**
	 * Slide out of upwards.
	 */
	public static final GPSlide OUT_UPWARD = new GPSlide(4);
	
	/**
	 * The slides.
	 */
	private static GPSlide[] _slides = {
		FROM_ABOVE,
		FROM_BELOW,
		NO_SLIDE,
		SHIFT_SLIDE,
		LEGATO_SLIDE,
		OUT_DOWNWARD,
		OUT_UPWARD
	};

	/**
	 * The slides' names.
	 */
	private static String[] _slidesNames = {
		"Slide into from above",
		"Slide into from below",
		"No slide",
		"Shift slide",
		"Legato slide",
		"Slide out downwards",
		"Slide out upwards"
	};

	/**
	 * Creates a new Slide whose type is <code>type</code>.
	 * @param type the slide integer type.
	 */
	private GPSlide(int type) {
		_type = type;
	}
	
	/**
	 * Returns a string representation of this GPSlide.
	 */
	public String toString() {
		return _slidesNames[_type+2];
	}

    /**
     * Returns the GPSlide associated with the value
     * <code>type</code>.
     * 
     * @param type
     *            the slide integer value.
     * @return the slide object associated.
     */
    public static GPSlide valueOf(int type) {
        return _slides[type+2];
    }
	/**
	 * Returns an index-like value starting from 0 for FROM_ABOVE
	 * 
	 * @return an integer ...0 for FROM_ABOVE..and so on
	 */
	public int getIndex() {
	    int resp ;
	    
	    if(_type < 0) {
	        resp = _type+2 ;
	    } else {
	        resp = _type+1 ;
	    }
	    return resp ;
	}

}