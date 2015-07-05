/*
 * Created on Mar 9, 2005
 */
package dguitar.players.sound.midi.demo;

import dguitar.adaptors.song.SongEvent;
import dguitar.adaptors.song.SongMeasureTrack;
import dguitar.adaptors.song.SongMessage;
import dguitar.adaptors.song.SongTrack;
import dguitar.adaptors.song.SongVirtualTrack;
import dguitar.adaptors.song.event.SongNoteOnMessage;
import dguitar.players.sound.PerformanceEvent;
import dguitar.players.sound.PerformanceEventListener;
import dguitar.players.sound.PerformanceNoteEvent;

/**
 * Other events not yet implemented.
 * @author crnash
 */
public class ExampleEventListener implements PerformanceEventListener
{
    LinePrinter out;
    
    /**
     * @param out
     */
    public ExampleEventListener(LinePrinter out)
    {
        this.out=out;
    }

    /* (non-Javadoc)
     * @see Player.PerformanceEventListener#onEvent(Player.PerformanceEvent)
     */
    public void onEvent(PerformanceEvent event)
    {
        if(event instanceof PerformanceNoteEvent)
        {
            onNoteEvent((PerformanceNoteEvent)event);
        }
    }

    public void onNoteEvent(PerformanceNoteEvent event)
    {
        try
        {
	        int tick=event.getOffset();
	        SongEvent songEvent=event.getEvent();
	        
	        SongVirtualTrack virtualTrack=songEvent.getVirtualTrack();
	        SongMeasureTrack measureTrack=virtualTrack.getMeasureTrack();
	        SongTrack track=measureTrack.getTrack();
	        
//UNUSED	        int virtualTrackIndex=virtualTrack.getIndex();
	        
	        SongMessage message=songEvent.getMessage();
	        
	        if(message instanceof SongNoteOnMessage)
	        {
	            SongNoteOnMessage snom=(SongNoteOnMessage)message;
	            out.println("Tick "+tick
	                +" track "+track.getIndex()
	                +" string "+virtualTrack.getIndex()
	                +" fret "+snom.getFret()
	                +" message "+message);
	        }
	        else
	        {
	            out.println("Tick "+tick
	                    +" track "+track.getIndex()
	                    +" string "+virtualTrack.getIndex()
	                    +" message "+message);
	        }
        }
        catch(Exception e)
        {
            // note that the caller is usually a JVM worker thread so it will
            // catch (and ignore) any exceptions thrown here. It is up to the
            // implementor to catch any unchecked exceptions.
            e.printStackTrace();
        }
    }
}
