package dguitar.codecs.guitarPro;

/**
 * This class describes a marker.
 * 
 * @author Matthieu Wipliez
 */
public class GPMarker {
    /**
     * The marker's color.
     */
    private GPColor _color;

    /**
     * The marker's name.
     */
    private String _name;

    /**
     * Creates a new Marker.
     * 
     */
    public GPMarker() {
        _color = new GPColor();
        _name = "";
    }

    /**
     * Returns this marker's color.
     * 
     * @return this marker's color.
     */
    public GPColor getColor() {
        return _color;
    }

    /**
     * Returns this marker's name.
     * 
     * @return this marker's name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Sets this marker's color.
     * 
     * @param color
     *            the color to set.
     */
    public void setColor(GPColor color) {
        _color = color;
    }

    /**
     * Sets this marker's name.
     * 
     * @param name
     *            the name to set.
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Returns a string representation of this Marker.
     */
    public String toString() {
        String res = "";
        res += "Name: " + _name;
        res += "\nColor: " + _color;
        return res;
    }
}