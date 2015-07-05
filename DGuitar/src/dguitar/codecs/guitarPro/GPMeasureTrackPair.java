package dguitar.codecs.guitarPro;

import java.util.ArrayList;
import java.util.List;

/**
 * This class describes a measure-track pair.
 * 
 * @author Matthieu Wipliez
 */
public class GPMeasureTrackPair {
    /**
     * The beats present in the measure-track pair.
     */
    private List<GPBeat> _beats;

    /**
     * Creates a new MeasureTrackPair
     *
     */
    public GPMeasureTrackPair() {
        _beats = new ArrayList<GPBeat>(0);
    }

    /**
     * Returns true if the given object equals to this
     * GPMeasureTrackPair.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        GPMeasureTrackPair other;
        boolean resp;

        resp = false;
        if ((o != null) && (o.getClass().isInstance(this))) {
            other = (GPMeasureTrackPair) o;
            resp = _beats.equals(other._beats);
        }

        return resp;
    }

    /**
     * Returns the list of beats.
     * @return the list of beats.
     */
    public List<GPBeat> getBeats() {
        return _beats;
    }

    /**
     * Returns a string representation of the MeasureTrackPair.
     */
    public String toString() {
        int i;
        String res;

        res = "Measure/Track Pair - BEGIN" + "\n";
        if (_beats != null) {
            for (i = 0; i < _beats.size(); i++) {
                res += "\tBeat #" + i + "\n";
                res += _beats.get(i).toString() + "\n";
            }
        }
        res += "\tMeasure/Track Pair - END" + "\n";

        return (res);
    }
}