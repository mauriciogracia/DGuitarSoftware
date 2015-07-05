/*
 * Created on Mar 1, 2005
 */
package dguitar.adaptors.song.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import dguitar.adaptors.song.SongMeasure;
import dguitar.adaptors.song.SongPhrase;
import dguitar.adaptors.song.SongPhraseList;



/**
 * Implementation of SongPhraseList
 * @author crnash
 */
public class SongPhraseListImpl implements SongPhraseList
{
    private List<SongPhrase> phrases;

    public SongPhraseListImpl()
    {
        phrases=new LinkedList<SongPhrase>();
    }
    
    public void addPhrase(SongPhrase phrase)
    {
        phrases.add(phrase);
    }

    public int getScoreMeasureCount()
    {
        int count=0;
        for(Iterator<SongPhrase> it=phrases.iterator();it.hasNext();)
        {
            SongPhrase phrase=it.next();
            count+=phrase.getScoreMeasureCount();
        }
        return count;
    }

    public SongMeasure getScoreMeasure(int measure)
    {
        int startMeasure=0;
        for(Iterator<SongPhrase> it=phrases.iterator();it.hasNext();)
        {
            SongPhrase phrase=it.next();
            int measureCount=phrase.getScoreMeasureCount();
            if((measure>=startMeasure)&&(measure<startMeasure+measureCount))
            {
                return phrase.getScoreMeasure(measure-startMeasure);
            }
            startMeasure+=measureCount;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    public int getPerformanceMeasureCount()
    {
        int count=0;
        for(Iterator<SongPhrase> it=phrases.iterator();it.hasNext();)
        {
            SongPhrase phrase=it.next();
            count+=phrase.getPerformanceMeasureCount();
        }
        return count;
    }

    public SongMeasure getPerformanceMeasure(int measure)
    {
        int startMeasure=0;
        for(Iterator<SongPhrase> it=phrases.iterator();it.hasNext();)
        {
            SongPhrase phrase=it.next();
            int measureCount=phrase.getPerformanceMeasureCount();
            if((measure>=startMeasure)&&(measure<startMeasure+measureCount))
            {
                return phrase.getPerformanceMeasure(measure-startMeasure);
            }
            startMeasure+=measureCount;
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /* (non-Javadoc)
     * @see Song.SongPhraseList#getPhrase(int)
     */
    public SongPhrase getPhrase(int index)
    {
        return phrases.get(index);
    }

    /* (non-Javadoc)
     * @see Song.SongPhraseList#getPhraseCount()
     */
    public int getPhraseCount()
    {
        return phrases.size();
    }

}
