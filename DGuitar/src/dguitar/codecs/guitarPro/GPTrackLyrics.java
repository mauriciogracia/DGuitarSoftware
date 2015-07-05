package dguitar.codecs.guitarPro;

/**
 * This class describes the track lyrics.
 * 
 * @author Matthieu Wipliez
 */
public class GPTrackLyrics {
    /**
     * Contains 5 strings of lyrics
     */
    private String[] _lines;

    /**
     * The measure a line of lyrics start from.
     */
    private int[] _measureNumber;

    /**
     * The number of the track the lyrics are associated with.
     */
    private int _trackNumber;

    /**
     * Creates a new TrackLyrics
     * 
     */
    public GPTrackLyrics() {
        _lines = new String[5];
        _measureNumber = new int[5];
        _trackNumber = 0;
    }

    /**
     * Returns the ith line of lyrics.
     * 
     * @param i
     *            the line number.
     * @return the ith line of lyrics.
     */
    public String getLines(int i) {
        return _lines[i];
    }

    /**
     * Returns the number of the measure from which the lyrics start on line i.
     * 
     * @param i
     *            the line number.
     * @return the number of the measure from which the lyrics start on line i.
     */
    public int getMeasureNumber(int i) {
        return _measureNumber[i];
    }

    /**
     * Returns the track number.
     * 
     * @return the track number.
     */
    public int getTrackNumber() {
        return _trackNumber;
    }

    /**
     * Sets the ith line of lyrics.
     * 
     * @param i
     *            the line number.
     * @param line
     *            the line to set.
     */
    public void setLine(int i, String line) {
        _lines[i] = line;
    }

    /**
     * Sets the number of the measure from which the lyrics start on line i.
     * 
     * @param i
     *            the index to be used
     * @param measureNumber
     *            the number of the measure from which the lyrics start on line i.
     */
    public void setMeasureNumber(int i, int measureNumber) {
        _measureNumber[i] = measureNumber;
    }

    /**
     * Sets the track number.
     * 
     * @param trackNumber
     *            the trackNumber to set.
     */
    public void setTrackNumber(int trackNumber) {
        _trackNumber = trackNumber;
    }

    /**
     * Returns a string representation of this GPTrackLyrics.
     */
    public String toString() {
        String res;

        res = "\n\tLYRICS - BEGIN";
        res += "\n\tTrack number: " + _trackNumber;
        for (int i = 0; i < 5; i++) {
            res += "\n\t\tLine no " + i;
            res += "\n\t\tStarts from measure " + _measureNumber[i];
            res += "\n\t\t" + _lines[i];
        }
        res += "\n\tLYRIC - END";
        return res;
    }
}
