/*
 * Created on Feb 26, 2005
 */
package dguitar.adaptors.song;

/**
 * A SongPhrase is a section of a song consisting of multiple SongPhraseTracks
 * @author Chris
 */
public interface SongPhrase
{
    /**
     * @return The count of the number of measures in this phrase on a printed score
     */
    int getScoreMeasureCount();

    /**
     * @param measure The measure index (0 based)
     * @return the measure at this index when printed on a score
     */
    SongMeasure getScoreMeasure(int measure);

    
    /**
     * @return The count of the number of measures in this phrase when it is performed
     */
    int getPerformanceMeasureCount();

    /**
     * @param measure the measure index (0 based)
     * @return the measure at this index when performed
     */
    SongMeasure getPerformanceMeasure(int measure);
}
