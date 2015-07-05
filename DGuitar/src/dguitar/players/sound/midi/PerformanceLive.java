/*
 * Created on Mar 18, 2005
 */
package dguitar.players.sound.midi;

import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.Track;

import dguitar.adaptors.song.SongMeasure;
import dguitar.adaptors.song.Tempo;
import dguitar.players.sound.EventSettings;
import dguitar.players.sound.EventStream;
import dguitar.players.sound.PerformanceEvent;
import dguitar.players.sound.PerformanceTimerEvent;
import dguitar.players.sound.TimerSettings;

/**
 * @author crnash
 */
public class PerformanceLive extends PerformanceCore implements MetaContainer, EventSettings, TimerSettings
{
	private static String className=PerformanceLive.class.toString();
    private static Logger logger=Logger.getLogger(className);
	
	int timerFrequency=4;
	Track eventTrack;
	EventStream timerStream;
	List<PerformanceEvent> metaItems;
//	UNUSED  	private boolean noteEventsEnabled;
	
	/**
	 * @param tracks
	 * @param tempo
	 * @param resolution
	 * @throws InvalidMidiDataException
	 */
	public PerformanceLive(int tracks, Tempo tempo, int resolution) throws InvalidMidiDataException
	{
		super(tracks+2,tempo,resolution);
		
		Track[] midiTracks=getTracks();
		eventTrack=midiTracks[tracks+1];
		timerStream=new EventStream(null,0);
		metaItems=new Vector<PerformanceEvent>();
	}
	
	/**
	 * @return Returns the timerFrequency.
	 */
	public int getTimerFrequency()
	{
		return timerFrequency;
	}
	/**
	 * @param timerFrequency The timerFrequency to set.
	 */
	public void setTimerFrequency(int timerFrequency)
	{
		this.timerFrequency = timerFrequency;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see Song.MetaContainer#create(Song.MetaItem, int)
	 */
	public MidiEvent create(PerformanceEvent item)
	{
		int location=item.getOffset();
		
		int key = metaItems.size();
		metaItems.add(item);
		
		MetaMessage mm = new MetaMessage();
		byte data[] = new byte[4];
		
		data[0] = (byte) ((key >> 24) & 0x00FF);
		data[1] = (byte) ((key >> 16) & 0x00FF);
		data[2] = (byte) ((key >> 8) & 0x00FF);
		data[3] = (byte) ((key) & 0x00FF);
		
		try
		{
			mm.setMessage(0x01, data, 4);
			MidiEvent event = new MidiEvent(mm, location);
			logger.finer("Creating new meta item for key " + key);
			return event;
		}
		catch (InvalidMidiDataException e)
		{
			logger.severe("Invalid midi data for meta message");
			return null;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see Song.MetaContainer#lookup(int)
	 */
	public PerformanceEvent lookup(int key)
	{
		if ((key >= 0) && (key < metaItems.size())) { return (PerformanceEvent) metaItems
			.get(key); }
		return null;
	}
	
	/* (non-Javadoc)
	 * @see players.sound.Performance#addTimerEvents(adaptors.song.SongMeasure, int)
	 */
	public void addTimerEvents(SongMeasure measure, int location)
	{
		if (timerFrequency > 0)
		{
			int index = measure.getIndex();
			logger.finer("adding meta events for measure " + index + " at "
					+ location);
			
			int measureLength = measure.getLength();
			int measureBeats = measure.getTimeSignature().getNumerator();
			int totalEvents = measureBeats * timerFrequency;
			
			int beatindex = 0;
			
			for (int beat = 0; beat < measureBeats; beat++)
			{
				for (int subbeat = 0; subbeat < timerFrequency; subbeat++)
				{
					int beatoffset = (measureLength * beatindex) / totalEvents;
					
					PerformanceEvent item = new PerformanceTimerEvent(timerStream,location+beatoffset,
							measure, beat + 1, subbeat + 1, timerFrequency);
					recordPerformanceEvent(item);
					beatindex++;
				}
			}
		}
	}
	
	protected void recordPerformanceEvent(PerformanceEvent item)
	{
		MidiEvent me = create(item);
		//OLD boolean added=eventTrack.add(me);
		eventTrack.add(me);
	}
	
	/* (non-Javadoc)
	 * @see players.sound.EventSettings#enableNoteEvents(boolean)
	 */
	public void enableNoteEvents(boolean enable)
	{
//		UNUSED  noteEventsEnabled=enable;        
	}
}
