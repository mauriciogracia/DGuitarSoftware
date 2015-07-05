/*
 * Created on Feb 28, 2005
 */
package dguitar.adaptors.song.impl;

import java.util.HashMap;
import java.util.Map;

import dguitar.adaptors.song.SongMeasure;
import dguitar.adaptors.song.SongMeasureTrack;
import dguitar.adaptors.song.SongTrack;
import dguitar.adaptors.song.TimeSignature;



/**
 * Implementation of SongMeasure
 * @author crnash
 */
public class SongMeasureImpl implements SongMeasure
{
    int length;
    int index;
    TimeSignature timeSignature;
    
    Map<SongTrack, SongMeasureTrack> trackMap;
    
    /**
     * @return Returns the timeSignature.
     */
    public TimeSignature getTimeSignature()
    {
        return timeSignature;
    }
    
    /**
     * @param index
     * @param length
     * @param timeSignature
     */
    public SongMeasureImpl(int index, int length, TimeSignature timeSignature)
    {
        super();
        this.index = index;
        this.length = length;
        this.timeSignature = timeSignature;
        
        trackMap=new HashMap<SongTrack, SongMeasureTrack>();
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.dguitar.song.SongMeasure#addTrack(net.sourceforge.dguitar.song.SongMeasureTrack)
     */
    public void addTrack(SongMeasureTrack smt)
    {
        trackMap.put(smt.getTrack(),smt);
    }

    /* (non-Javadoc)
     * @see net.sourceforge.dguitar.song.SongPhrase#getMeasureCount()
     */
    public int getScoreMeasureCount()
    {
        return 1;
    }

    /* (non-Javadoc)
     * @see net.sourceforge.dguitar.song.SongPhrase#getIndexedMeasure(int)
     */
    public SongMeasure getScoreMeasure(int i)
    {
        if(i==0) return this;
        throw new ArrayIndexOutOfBoundsException();
    }

    /* (non-Javadoc)
     * @see Song.SongPhrase#getPerformanceMeasureCount()
     */
    public int getPerformanceMeasureCount()
    {
        return 1;
    }

    /* (non-Javadoc)
     * @see Song.SongPhrase#getPerformanceMeasure(int)
     */
    public SongMeasure getPerformanceMeasure(int measure)
    {
        if(measure==0) return this;
        throw new ArrayIndexOutOfBoundsException();
    }

    /* (non-Javadoc)
     * @see Song.SongMeasure#getLength()
     */
    public int getLength()
    {
        return length;
    }

    /**
     * @return Returns the index.
     */
    public int getIndex()
    {
        return index;
    }
    
    /* (non-Javadoc)
     * @see adaptors.song.SongMeasure#getTrack(adaptors.song.SongTrack)
     */
    public SongMeasureTrack getTrack(SongTrack track)
    {
        SongMeasureTrack smt=trackMap.get(track);
        return smt;
    }
}
