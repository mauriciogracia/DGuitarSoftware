/*
 * Created on Mar 18, 2005
 */
package dguitar.players.sound.midi;

import javax.sound.midi.InvalidMidiDataException;

import dguitar.adaptors.song.SongMeasure;
import dguitar.adaptors.song.Tempo;
import dguitar.players.sound.PerformanceEvent;

/**
 * @author crnash
 */
public class PerformanceFile extends PerformanceCore
{
    /**
     * @param tracks
     * @param tempo
     * @param resolution
     * @throws InvalidMidiDataException
     */
    public PerformanceFile(int tracks, Tempo tempo, int resolution) throws InvalidMidiDataException
    {
        super(tracks+1,tempo,resolution);
    }

    /* (non-Javadoc)
     * @see players.sound.Performance#setTimerFrequency(int)
     */
    public void setTimerFrequency(int frequency)
    {

    }

    /* (non-Javadoc)
     * @see players.sound.Performance#addTimerEvents(adaptors.song.SongMeasure, int)
     */
    public void addTimerEvents(SongMeasure measure, int location)
    {
    }
    
    protected void recordPerformanceEvent(PerformanceEvent item)
    {      
    }
}
