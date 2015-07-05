package dguitar.codecs.guitarPro;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describes a Bend read from a Guitar Pro file. <br />
 * A Bend is defined by its type, its height, and points that define how the
 * bend is to be played.
 * 
 * @author Matthieu Wipliez
 */
public class GPBend {
    /**
     * A list of points the bend is constitued of.
     */
    private List<GPBendPoint> _points;

    /**
     * The bend type (bend, bend and release,...).
     * 
     * @see GPBendType
     */
    private GPBendType _type;

    /**
     * The bend height
     */
    private int _value;

    /**
     * Creates a new Bend
     * 
     */
    public GPBend() {
        _points = new ArrayList<GPBendPoint>(0);
        _type = GPBendType.NONE;
        _value = 0;
    }

    /**
     * Returns this bend's points.
     * 
     * @return the points.
     */
    public List<GPBendPoint> getPoints() {
        return _points;
    }

    /**
     * Returns this bend's type.
     * 
     * @return this bend's type.
     * @see GPBendType
     */
    public GPBendType getType() {
        return _type;
    }

    /**
     * Returns this bend's value.
     * 
     * @return this bend's value.
     */
    public int getValue() {
        return _value;
    }

    /**
     * Sets the type of this bend.
     * 
     * @param type
     *            this bend's type
     * @see GPBendType
     */
    public void setType(GPBendType type) {
        _type = type;
    }

    /**
     * Sets this bend's value.
     * 
     * @param value
     *            The value to set.
     */
    public void setValue(int value) {
        _value = value;
    }

    /**
     * Returns a string representation of the Bend.
     */
    public String toString() {
        String res;
        int i;
        GPBendPoint GBP;

        res = "";
        if (_type != null) {
            res += "Type" + ": " + _type.toString();
        }
        res += ", Value" + ": " + _value;
        if (_points != null) {
            res += "Bend poits" + ": ";

            for (i = 0; i < _points.size(); i++) {
                GBP = (GPBendPoint) _points.get(i);
                res += i + ": ";
                res += GBP.toString();
            }
        }
        return res;
    }
}
