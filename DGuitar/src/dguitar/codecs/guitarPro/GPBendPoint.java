package dguitar.codecs.guitarPro;

/**
 * This class describes a point in a bend diagram.
 * 
 * @author Matthieu Wipliez
 */
public class GPBendPoint {
	/**
	 * Absolute time position, in sixtieths of the note duration.
	 */
	private int _position;

	/**
	 * The bend height (100 per tone).
	 */
	private int _value;

	/**
	 * Determines how to play the section
	 */
	private GPVibrato _vibrato;

	/**
	 * Creates a new BendPoint.
	 * 
	 */
	public GPBendPoint() {
		_position = 0;
		_value = 0;
		_vibrato = GPVibrato.NONE;
	}

	/**
     * Returns this point's position.
	 * @return this point's position.
	 */
	public int getPosition() {
		return _position;
	}
    
    /**
     * Returns this point's value.
     * @return this point's value (height).
     */
    public int getValue() {
        return _value;
    }
    
    /**
     * Returns this point's vibrato.
     * @return this point's vibrato.
     */
    public GPVibrato getVibrato() {
        return _vibrato;
    }

	/**
     * Sets this point's position.
	 * @param position the position to set.
	 */
	public void setPosition(int position) {
		_position = position;
	}

	/**
     * Sets this point's value.
	 * @param value
	 *            the value to set.
	 */
	public void setValue(int value) {
		_value = value;
	}

	/**
     * Sets this point's vibrato effect.
	 * @param vibrato
	 *            the vibrato to set.
	 */
	public void setVibrato(GPVibrato vibrato) {
		_vibrato = vibrato;
	}
   
    /**
     * Returns a string representation of this point.
     * @see java.lang.Object#toString()
     */
	public String toString() {
		String res ;
		
		res = "" ;
		res += "Position" + ": " + _position ;
		res += ", Hight" + ": " +  _value ;
		if(_vibrato != null) {
			res += ", Vibrato" + ": " + _vibrato.toString() ;	
		}
		
		return res ;
	}
}