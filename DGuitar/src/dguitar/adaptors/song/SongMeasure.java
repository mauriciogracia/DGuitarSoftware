/*
 * Created on Feb 28, 2005
 */
package dguitar.adaptors.song;

/**
 * A SongMeasure is a single measure in a song.
 * @author crnash
 */
public interface SongMeasure extends SongPhrase
{
    public void addTrack(SongMeasureTrack track);

    /**
     * Retrieves the SongMeasureTrack (list of SongEvents) for the given track in
     * this measure.
     * @param track the track for which to retrieve the measure data.
     * @return the song measure track of the score of this measure on this track.
     */
    public SongMeasureTrack getTrack(SongTrack track);
    
    /**
     * @return the length of this phrase, in resolution units
     *      
     */
    int getLength();

    /**
     * @return the index of the measure
     */
    public int getIndex();
    
    public TimeSignature getTimeSignature();
}
