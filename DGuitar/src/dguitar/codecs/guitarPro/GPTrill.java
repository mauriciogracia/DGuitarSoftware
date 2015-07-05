package dguitar.codecs.guitarPro;

/**
 * This class describes a trill.
 * 
 * @author Matthieu Wipliez
 */
public class GPTrill {
    /**
     * The fret the trill is made with.
     */
    private int _fret;

    /**
     * The period between each note.
     */
    private GPDuration _period;

    /**
     * Creates a new Trill.
     * 
     */
    public GPTrill() {
        _fret = 0;
        _period = GPDuration.QUARTER;
    }

    /**
     * Returns this trill's fret.
     * 
     * @return this trill's fret.
     */
    public int getFret() {
        return _fret;
    }

    /**
     * Returns this trill's period.
     * 
     * @return this trill's period.
     */
    public GPDuration getPeriod() {
        return _period;
    }

    /**
     * Sets this trill's fret.
     * 
     * @param fret
     *            the fret to set.
     */
    public void setFret(int fret) {
        _fret = fret;
    }

    /**
     * Sets this trill's period.
     * 
     * @param period
     *            the period to set.
     */
    public void setPeriod(GPDuration period) {
        _period = period;
    }

    /**
     * Returns a string representation of the Trill.
     */
    public String toString() {
        String res;

        res = "";
        res += "Fret: " + _fret;
        res += ",Period: " + _period;
        res += "\n";

        return (res);
    }
}