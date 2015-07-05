/*
 * Created on Feb 26, 2005
 */
package dguitar.adaptors.song;


/**
 * A Song is a particular kind of SongPhraseList that defines an entire Song
 * @author Chris
 */
public interface Song extends SongPhraseList
{
    /**
     * @return the resolution of this song, in pulses per quarter note (PPQ)
     */
    int getResolution();

	/**
	 * This is a convenience method to return the given measure from the score in
	 * musicians' notation (measure 1 is the first measure printed, not 0)
	 * @see dguitar.adaptors.song#SongPhrase.getScoreMeasure
     * @param measure the measure to return from the score (1-based)
     * @return SongMeasure
     */
    SongMeasure getSongScoreMeasure(int measure);
    /**
	 * This is a convenience method to return the given measure from the score in
	 * musicians' notation (measure 1 is the first measure printed, not 0)
	 * @see dguitar.adaptors.song#getPerformanceMeasure
     * @param measure the measure index (1-based)
     * @return the measure
     */
    SongMeasure getSongPerformanceMeasure(int measure);

    /**
     * @param object
     */
    void addTrack(SongTrack object);

    /**
     * @return the track count
     */
    int getTrackCount();

    /**
     * @param t
     * @return SongTrack
     */
    SongTrack getTrack(int t);
    
    /**
     * @return the default tempo of this song
     */
    Tempo getTempo();
}
