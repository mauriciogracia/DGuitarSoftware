/*
 * Created on Mar 8, 2005
 */
package dguitar.players.sound;

import dguitar.adaptors.song.SongMeasure;


/**
 * @author Chris
 */
public class PerformanceTimerEvent extends PerformanceEvent
{
    SongMeasure measure;
    int beat;
    int division;
    int totalDivisions;
    
    /**
     * 
     * @param es
     * @param offset
     * @param measure
     * @param beat
     * @param division
     * @param totalDivisions
     */
    public PerformanceTimerEvent(EventStream es, int offset,
            SongMeasure measure, int beat, int division, int totalDivisions)
    {
        super(es,offset);
        this.measure = measure;
        this.beat = beat;
        this.division = division;
        this.totalDivisions = totalDivisions;
    }
    /**
     * @return Returns the beat.
     */
    public int getBeat()
    {
        return beat;
    }
    /**
     * @return Returns the division.
     */
    public int getDivision()
    {
        return division;
    }
    /**
     * @return Returns the measure.
     */
    public int getMeasure()
    {
        return measure.getIndex();
    }
    /**
     * @return Returns the totalBeats.
     */
    public int getTotalBeats()
    {
        return measure.getTimeSignature().getDenominator();
    }
    /**
     * @return Returns the totalDivisions.
     */
    public int getTotalDivisions()
    {
        return totalDivisions;
    }
}
