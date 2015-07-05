/*
 * Created on Mar 17, 2005
 */
package dguitar.players.sound;

import java.util.Iterator;
import java.util.List;

import dguitar.adaptors.song.SongEvent;
import dguitar.adaptors.song.SongMessage;
import dguitar.adaptors.song.SongTrack;
import dguitar.adaptors.song.event.SongEventImpl;
import dguitar.adaptors.song.event.SongNoteOffMessage;
import dguitar.adaptors.song.event.SongNoteOnMessage;
import dguitar.adaptors.song.event.SongTieMessage;

/**
 * An EventStream is the ordered list of PerformanceEvents on a specific virtual
 * track, representing the behavior of a single string through an entire
 * performance.
 * 
 * @author crnash
 */
public class EventStream
{
    /**
     * The track to which this stream of events belongs.
     */
    SongTrack track;
    /**
     * The virtual track within <code>track</code> that produced this stream
     * of events
     */
    int virtualTrackIndex;

    /**
     * The first event in this event stream. It will typically not be
     * <code>null</code> since tracks should begin with an initialization
     * event.
     */
    PerformanceEvent first;

    /**
     * The last event in this event stream.
     */
    PerformanceEvent last;
    
    /**
     * The last event parsed.
     */
    int lastEventEndOffset;
    
    /**
     * The location of the last event.
     */
    int lastEventLocation;
    
    /**
     * The last note on event parsed.
     */
    SongEvent lastNoteOnEvent;

    /**
     * @param songTrack
     * @param v
     */
    public EventStream(SongTrack songTrack, int v)
    {
        this.track = songTrack;
        this.virtualTrackIndex = v;
        first = null;
        last = null;
    }

    /**
     * Add a list of song events as PerformanceEvents to this event stream.
     * 
     * @param events
     *            the list of events to add
     * @param location
     *            the offset to apply to these events, in resolution units
     */
    public void addEvents(List<SongEvent> events, int location)
    {
        for (Iterator<SongEvent> it = events.iterator(); it.hasNext();)
        {
            SongEvent event = (SongEvent) it.next();
            int baseTime = event.getTime();
            SongMessage message=event.getMessage();
                        
            // This is where you should handle note on, note off, ties
            if(message instanceof SongNoteOnMessage)
            {
                closeOpenNote();
                PerformanceEvent perfEvent = new PerformanceNoteEvent(this,
                        baseTime + location, event);
                connect(perfEvent);
     
                lastNoteOnEvent=event;                
                SongNoteOnMessage noteOn=(SongNoteOnMessage)message;                
                lastEventEndOffset=baseTime+noteOn.getDuration();
                lastEventLocation=location;
            }
            else if(message instanceof SongTieMessage)
            {
                PerformanceEvent perfEvent = new PerformanceNoteEvent(this,
                        baseTime + location, event);
                connect(perfEvent);
     
                SongTieMessage tie=(SongTieMessage)message;
                lastEventEndOffset=baseTime+tie.getDuration();
                lastEventLocation=location;                                
            }
        }
    }
    
    private void closeOpenNote()
    {
        if(lastNoteOnEvent!=null)
        {
            SongNoteOnMessage noteOn=(SongNoteOnMessage)lastNoteOnEvent.getMessage();
            SongNoteOffMessage snom=new SongNoteOffMessage(noteOn);
            SongEvent snoe=new SongEventImpl(lastEventEndOffset,snom);
            snoe.setVirtualTrack(lastNoteOnEvent.getVirtualTrack());
            PerformanceEvent perfEvent2 = new PerformanceNoteEvent(this,
                    lastEventEndOffset+lastEventLocation, snoe);
            connect(perfEvent2);
            
            lastNoteOnEvent=null;
        }
    }

    private void connect(PerformanceEvent perfEvent)
    {
        if (last != null)
        {
            last.setNext(perfEvent);
        }
        last = perfEvent;
        if (first == null)
        {
            first = perfEvent;
        }
    }

    /**
     * @return Returns the first.
     */
    public PerformanceEvent getFirst()
    {
        return first;
    }

    /**
     * @return Returns the track.
     */
    public SongTrack getTrack()
    {
        return track;
    }

    /**
     * @return Returns the virtualTrackIndex.
     */
    public int getVirtualTrackIndex()
    {
        return virtualTrackIndex;
    }

    /**
     * 
     */
    public void close()
    {    
        closeOpenNote();
    }
}