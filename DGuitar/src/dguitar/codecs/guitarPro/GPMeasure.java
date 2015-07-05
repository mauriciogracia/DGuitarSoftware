package dguitar.codecs.guitarPro;

/**
 * This class describes a measure.
 * 
 * @author Matthieu Wipliez
 */
public class GPMeasure {
    /**
     * Presence of a double bar.
     */
    public boolean hasDoubleBar;

    /**
     * The marker possibly associated with this measure.
     */
    public GPMarker marker;

    /**
     * Presence of a beginning of repeat sign.
     */
    public boolean repeatStart;

    /**
     * Denominator of the measure
     */
    private int _denominator;

    /**
     * The number of the measure
     */
    private int _number;

    /**
     * The number of alternate endings of the measure.
     */
    private int _numberOfAlternateEnding;

    /**
     * The number of repetitions of the measure.
     */
    private int _numberOfRepetitions;

    /**
     * Numerator of the measure
     */
    private int _numerator;

    /**
     * The tonality of this measure.
     */
    private GPKey _tonality;

    /**
     * Creates a new measure
     * 
     */
    public GPMeasure() {
        _denominator = 0;
        hasDoubleBar = false;
        marker = null;
        _numberOfAlternateEnding = 0;
        _numberOfRepetitions = 0;
        _numerator = 0;
        repeatStart = false;
        _tonality = GPKey.C;
    }

    /**
     * Creates a new Measure with the same numerator/denominator and tonality
     * 
     */
    public GPMeasure(GPMeasure original) {
        hasDoubleBar = false;
        marker = null;
        _numberOfAlternateEnding = 0;
        _numberOfRepetitions = 0;
        repeatStart = false;

        if (original != null) {
            _denominator = original._denominator;
            _numerator = original._numerator;
            _tonality = original._tonality;
        } else {
            _denominator = 0;
            _numerator = 0;
            _tonality = GPKey.C;
        }
    }

    /**
     * Returns true if the given object is equals to this
     * GPMeasure object.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o) {
        boolean resp;
        GPMeasure other;

        resp = false;
        if ((o != null) && (o.getClass().isInstance(this))) {
            other = (GPMeasure) o;
            resp = (hasDoubleBar == other.hasDoubleBar);
            resp = resp && (repeatStart == other.repeatStart);
            resp = resp && (_denominator == other._denominator);
            resp = resp && (_numerator == other._numerator);
            resp = resp && (_number == other._number);
            resp = resp
                    && (_numberOfAlternateEnding == other._numberOfAlternateEnding);
            resp = resp && (_numberOfRepetitions == other._numberOfRepetitions);
            //TODO there are more fields to compare
        }
        return resp;
    }

    /**
     * Returns this measure's denominator.
     * @return this measure's denominator.
     */
    public int getDenominator() {
        return _denominator;
    }

    /**
     * Returns this measure's number.
     * @return this measure's number.
     */
    public int getNumber() {
        return _number;
    }

    /**
     * Returns this measure's number of alternate endings.
     * @return this measure's number of alternate endings.
     */
    public int getNumberOfAlternateEnding() {
        return _numberOfAlternateEnding;
    }

    /**
     * Returns this measure's number of repetitions.
     * @return this measure's number of repetitions.
     */
    public int getNumberOfRepetitions() {
        return _numberOfRepetitions;
    }

    /**
     * Returns this measure's numerator.
     * @return this measure's numerator.
     */
    public int getNumerator() {
        return _numerator;
    }

    /**
     * Returns this measure's tonality.
     * @return this measure's tonality.
     */
    public GPKey getTonality() {
        return _tonality;
    }

    /**
     * Sets this measure's denominator.
     * @param denominator
     *            the denominator to set.
     */
    public void setDenominator(int denominator) {
        _denominator = denominator;
    }

    /**
     * Sets this measure's number.
     * @param number the number to set.
     */
    public void setNumber(int number) {
        _number = number;
    }

    /**
     * Sets this measure's number of alternate endings.
     * @param numberOfAlternateEnding
     *            the number of alternate endings to set.
     */
    public void setNumberOfAlternateEnding(int numberOfAlternateEnding) {
        _numberOfAlternateEnding = numberOfAlternateEnding;
    }

    /**
     * Sets this measure's number of repetitions.
     * @param numberOfRepetitions
     *            the number of repetitions to set.
     */
    public void setNumberOfRepetitions(int numberOfRepetitions) {
        _numberOfRepetitions = numberOfRepetitions;
    }

    /**
     * Sets this measure's numerator.
     * @param numerator
     *            the numerator to set.
     */
    public void setNumerator(int numerator) {
        _numerator = numerator;
    }

    /**
     * Sets this measure's tonality.
     * @param tonality
     *            The tonality to set.
     */
    public void setTonality(GPKey tonality) {
        _tonality = tonality;
    }

    /**
     * Returns a string representation of this Measure.
     */
    public String toString() {
        String res;

        res = "Time Signture:" + _numerator + "/" + _denominator;
        if (repeatStart) {
            res += ",Repeat start";
        }
        if (_numberOfRepetitions > 0) {
            res += ",Repeat end (" + _numberOfRepetitions + "X)";
        }
        if (_numberOfAlternateEnding > 0) {
            res += ",Alternate ending: " + _numberOfAlternateEnding;
        }
        if (marker != null) {
            res += ",Marker: " + marker;
        }
        res += ",Tonality: " + _tonality;
        if (hasDoubleBar) {
            res += ",Double bar " + hasDoubleBar;
        }

        return res;
    }
}