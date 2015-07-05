package dguitar.codecs.guitarPro;

/**
 * This class describes a mix table element.
 * 
 * @author Matthieu Wipliez
 */
public class GPMixTableElement {
    /**
     * Whether the change applies to all tracks.
     */
    public boolean applyToAllTracks;

    /**
     * The duration of the change in beats.
     */
    private int _changeDuration;

    /**
     * The new value of the element.
     */
    private int _newValue;

    /**
     * Creates a new MixTableElement.
     * 
     */
    public GPMixTableElement() {
        applyToAllTracks = false;
        _changeDuration = 0;
        _newValue = 0;
    }

    /**
     * Returns this element's duration of change.
     * 
     * @return this element's duration of change.
     */
    public int getChangeDuration() {
        return _changeDuration;
    }

    /**
     * Returns this element's new value.
     * 
     * @return this element's new value.
     */
    public int getNewValue() {
        return _newValue;
    }

    /**
     * Sets this element's duration of change.
     * 
     * @param changeDuration
     *            the duration of change to set.
     */
    public void setChangeDuration(int changeDuration) {
        _changeDuration = changeDuration;
    }

    /**
     * Sets this element's new value.
     * 
     * @param newValue
     *            the new value to set.
     */
    public void setNewValue(int newValue) {
        _newValue = newValue;
    }

    /**
     * Returns a string representation of this GPMixTableElement.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String res;

        res = "";
        if (this.applyToAllTracks) {
            res = "applyToAllTracks:" + ": " + applyToAllTracks;
            res += ", ";
        }
        res += "changeDuration" + ": " + _changeDuration;
        res += ", newValue" + ": " + _newValue;

        return res;
    }
}