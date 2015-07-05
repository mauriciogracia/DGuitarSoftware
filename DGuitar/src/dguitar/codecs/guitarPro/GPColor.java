package dguitar.codecs.guitarPro;

/**
 * This class describes a color read from a Guitar Pro file.
 * 
 * @author Matthieu Wipliez
 */
public class GPColor {
    /**
     * The red component.
     */
	public int red;
    
    /**
     * The green component.
     */
    public int green;
    
    /**
     * The blue component.
     */
    public int blue;

	/**
	 * Creates a new Color.
	 *
	 */
	public GPColor() {
		red = 255;
		green = 0;
		blue = 0;
	}

	/**
     * Creates a new color with the specified components.
     * @param r the red component.
     * @param g the green component.
     * @param b the blue component.
	 */
	public GPColor(int r, int g, int b) {
		red = r;
		green = g;
		blue = b;
	}
	
    /**
     * Returns a string representation of this GPColor object.
     */
	public String toString() {
		return "(" + red + ", " + green + ", " + blue + ")";
	}
}
