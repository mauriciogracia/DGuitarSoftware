/*
 * Created on Mar 8, 2005
 */
package dguitar.players.sound;

/**
 * @author Chris
 */
public class PerformanceEvent
{
    /**
     * The event stream within which this performance event lies.
     */
    EventStream eventStream;
    
    /**
     * The time offset into the performance, in resolution units.
     */
    int offset;
    
    /**
     * The next performance event within this event stream.
     */
    PerformanceEvent next;
    
    /**
     * @param stream
     * @param offset
     */
    public PerformanceEvent(EventStream stream, int offset)
    {
        this.eventStream=stream;
        this.offset=offset;
        this.next=null;
    }

    /**
     * Chain this performance event to the next one in the same event stream.
     * @param perfEvent
     */
    public void setNext(PerformanceEvent perfEvent)
    {
        next=perfEvent;
    }
    /**
     * @return Returns the next.
     */
    public PerformanceEvent getNext()
    {
        return next;
    }
    /**
     * @return Returns the offset.
     */
    public int getOffset()
    {
        return offset;
    }
}
