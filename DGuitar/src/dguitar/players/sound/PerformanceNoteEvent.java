/*
 * Created on Mar 9, 2005
 */
package dguitar.players.sound;

import dguitar.adaptors.song.SongEvent;

/**
 * @author crnash
 */
public class PerformanceNoteEvent extends PerformanceEvent
{
    /**
     * The event in the Song that corresponds to this performance event. 
     */
    SongEvent event;
    
    /**
     * @param stream
     * @param offset
     * @param event
     */
    public PerformanceNoteEvent(EventStream stream, int offset, SongEvent event)
    {
        super(stream,offset);
        this.event=event;
    }

    /**
     * @return Returns the event.
     */
    public SongEvent getEvent()
    {
        return event;
    }
}
