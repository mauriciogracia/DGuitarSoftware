/*
 * Created on Feb 28, 2005
 */
package dguitar.adaptors.song.impl;

import java.util.LinkedList;
import java.util.List;

import dguitar.adaptors.song.Song;
import dguitar.adaptors.song.SongMeasure;
import dguitar.adaptors.song.SongTrack;
import dguitar.adaptors.song.Tempo;



/**
 * Implementation of Song
 * @author crnash
 */
public class SongImpl extends SongPhraseListImpl implements Song
{
    int ppq;		///< resolution in pulses per quarter note
    Tempo tempo;
    
    List<SongTrack> tracks;
    
    public SongImpl(int ppq,Tempo tempo)
    {
        super();
        this.ppq=ppq;
        this.tempo=tempo;
        tracks=new LinkedList<SongTrack>();
    }
    
    /* (non-Javadoc)
     * @see net.sourceforge.dguitar.song.Song#getScoreMeasure(int)
     */
    public SongMeasure getSongScoreMeasure(int measure)
    {
        return getScoreMeasure(measure-1);
    }


    /* (non-Javadoc)
     * @see net.sourceforge.dguitar.song.Song#getResolution()
     */
    public int getResolution()
    {
        return ppq;
    }

    /* (non-Javadoc)
     * @see Song.Song#getSongPerformanceMeasure(int)
     */
    public SongMeasure getSongPerformanceMeasure(int measure)
    {
        return getPerformanceMeasure(measure-1);
    }

    /* (non-Javadoc)
     * @see Song.Song#addTrack(Song.SongTrack)
     */
    public void addTrack(SongTrack track)
    {
        tracks.add(track);
    }

    /* (non-Javadoc)
     * @see Song.Song#getTrackCount()
     */
    public int getTrackCount()
    {
        return tracks.size();
    }

    /* (non-Javadoc)
     * @see Song.Song#getTrack(int)
     */
    public SongTrack getTrack(int t)
    {
        return (SongTrack)tracks.get(t-1);
    }

    /* (non-Javadoc)
     * @see Song.Song#getTempo()
     */
    public Tempo getTempo()
    {
        return tempo;
    }
}
