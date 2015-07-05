/*
 * Created on Mar 1, 2005
 */
package dguitar.adaptors.song.impl;

import dguitar.adaptors.song.RepeatedSongPhrase;
import dguitar.adaptors.song.SongMeasure;
import dguitar.adaptors.song.SongPhrase;

/**
 * Implementation of RepeatedSongPhrase
 * @author crnash
 */
public class RepeatedSongPhraseImpl implements RepeatedSongPhrase
{
    SongPhrase phrase;
    int repeatCount;
    
    public RepeatedSongPhraseImpl(SongPhrase phrase,int repeatCount)
    {
        this.phrase=phrase;
        this.repeatCount=repeatCount;
    }

    /* (non-Javadoc)
     * @see Song.SongPhrase#getScoreMeasureCount()
     */
    public int getScoreMeasureCount()
    {
        return phrase.getScoreMeasureCount();
    }

    /* (non-Javadoc)
     * @see Song.SongPhrase#getScoreMeasure(int)
     */
    public SongMeasure getScoreMeasure(int measure)
    {	
        int sc=phrase.getScoreMeasureCount();
        if((measure<0)||(measure>=sc)) throw new ArrayIndexOutOfBoundsException();
        
        return phrase.getScoreMeasure(measure);
    }

    /* (non-Javadoc)
     * @see Song.SongPhrase#getPerformanceMeasureCount()
     */
    public int getPerformanceMeasureCount()
    {
        return (repeatCount+1)*phrase.getPerformanceMeasureCount();
    }

    /* (non-Javadoc)
     * @see Song.SongPhrase#getPerformanceMeasure(int)
     */
    public SongMeasure getPerformanceMeasure(int measure)
    {
        int pc=phrase.getPerformanceMeasureCount();
        if((measure<0)||(measure>=(repeatCount+1)*pc)) throw new ArrayIndexOutOfBoundsException();
        
        return phrase.getPerformanceMeasure(measure%pc);
    }
    /**
     * @return Returns the phrase.
     */
    public SongPhrase getPhrase()
    {
        return phrase;
    }
    /**
     * @return Returns the repeatCount.
     */
    public int getRepeatCount()
    {
        return repeatCount;
    }
}
