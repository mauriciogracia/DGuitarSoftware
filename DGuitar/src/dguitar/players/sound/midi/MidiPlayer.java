/*
 * Created on Mar 18, 2005
 */
package dguitar.players.sound.midi;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import dguitar.adaptors.song.Tempo;
import dguitar.players.sound.EventSoundPlayer;
import dguitar.players.sound.Performance;
import dguitar.players.sound.PerformanceEventListener;
import dguitar.players.sound.PerformanceTimerListener;
import dguitar.players.sound.RealtimeSoundPlayer;
import dguitar.players.sound.SoundPlayer;

/**
 * @author crnash
 */
public class MidiPlayer implements SoundPlayer, RealtimeSoundPlayer, EventSoundPlayer 
{
    private String className = MidiPlayer.class.toString();
    private Logger logger = Logger.getLogger(className);

    Sequencer sequencer = null;
    PerformanceLive live;
    List<MetaPerformer> listeners = new LinkedList<MetaPerformer>();
    private int timerFrequency;
    private boolean noteEventsEnabled;

    public Performance createPerformance(int tracks, Tempo tempo, int resolution)
    {
        try
        {
            PerformanceLive alive=new PerformanceLive(tracks, tempo, resolution);
            
            alive.enableNoteEvents(noteEventsEnabled);
            alive.setTimerFrequency(timerFrequency);
            
            return alive;
        }
        catch (InvalidMidiDataException e)
        {
            logger.severe("Could not create a performance object");
            return null;
        }
    }

    /**
     *  
     */
    public void start()
    {
        if (sequencer != null)
        {
            sequencer.close();
        }

        try
        {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        }
        catch (MidiUnavailableException e)
        {
            // TODO decide on an exception throing mechanism
            sequencer = null;
        }

        for (Iterator<MetaPerformer> it = listeners.iterator(); it.hasNext();)
        {
            MetaPerformer mp = it.next();
            mp.setContainer(live);
            sequencer.addMetaEventListener((MetaEventListener) mp);
        }
        try
        {
            sequencer.setSequence(live);
        }
        catch (InvalidMidiDataException e1)
        {
            // TODO decide on an exception throing mechanism
            sequencer=null;
        }
        sequencer.start();
    }

    /**
     *  
     */
    public void stop()
    {
        if(sequencer.isOpen()) {
            sequencer.stop();
    	}
    }

    /**
     *  
     */
    public void waitForCompletion()
    {
        // TODO intercept the EOF notification message
        while (sequencer.isRunning())
        {
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
            }
        }
    }

    /**
     * @param performance
     */
    public void setPerformance(Performance performance)
    {
        live = (PerformanceLive) performance;
    }

    /**
     *  
     */
    public void close()
    {
        if (sequencer != null)
        {
            sequencer.close();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see players.sound.SoundPlayer#addTimerListener(players.sound.PerformanceTimerListener)
     */
    public void addTimerListener(PerformanceTimerListener listener)
    {
        MidiPerformanceTimerListenerProxy proxy = new MidiPerformanceTimerListenerProxy(
                listener);

        listeners.add(proxy);
    }

    /*
     * (non-Javadoc)
     * 
     * @see players.sound.SoundPlayer#addEventListener(players.sound.PerformanceEventListener)
     */
    public void addEventListener(PerformanceEventListener listener)
    {
        MidiPerformanceEventListenerProxy proxy = new MidiPerformanceEventListenerProxy(
                listener);

        listeners.add(proxy);
    }

    /* (non-Javadoc)
     * @see players.sound.TimerSettings#setTimerFrequency(int)
     */
    public void setTimerFrequency(int ppq)
    {
        timerFrequency=ppq;        
    }

    /* (non-Javadoc)
     * @see players.sound.EventSettings#enableNoteEvents(boolean)
     */
    public void enableNoteEvents(boolean enable)
    {
        noteEventsEnabled=enable;
    }
}
