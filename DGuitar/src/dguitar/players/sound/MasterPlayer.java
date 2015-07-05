/*
 * Created on Mar 9, 2005
 */
package dguitar.players.sound;

import java.util.List;

import dguitar.adaptors.song.Song;
import dguitar.adaptors.song.SongEvent;
import dguitar.adaptors.song.SongMeasure;
import dguitar.adaptors.song.SongMeasureTrack;
import dguitar.adaptors.song.SongTrack;
import dguitar.adaptors.song.SongVirtualTrack;
import dguitar.adaptors.song.Tempo;
import dguitar.adaptors.song.TimeSignature;

/**
 * @author crnash
 */
public class MasterPlayer implements RealtimeSoundPlayer, EventSoundPlayer
{
//UNUSED      private static String className = MasterPlayer.class.toString();
//UNUSED      private static Logger logger = Logger.getLogger(className);

    int timerFrequency = 4;
    boolean noteEventsEnabled = false;

    SoundPlayer soundPlayer;

    /*
     * (non-Javadoc)
     * 
     * @see Player.TimerSettings#setTimerFrequency(int)
     */
    public void setTimerFrequency(int ppq)
    {
        timerFrequency = ppq;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Player.EventSettings#enableNoteEvents(boolean)
     */
    public void enableNoteEvents(boolean enable)
    {
        noteEventsEnabled = enable;
    }

    public void setSoundPlayer(SoundPlayer soundPlayer)
    {
        this.soundPlayer = soundPlayer;
    }

    /**
     * @param listener
     */
    public void addEventListener(PerformanceEventListener listener)
    {
         ((EventSoundPlayer)soundPlayer).addEventListener(listener);
    }

    /**
     * @param listener
     */
    public void addTimerListener(PerformanceTimerListener listener)
    {
        ((RealtimeSoundPlayer)soundPlayer).addTimerListener(listener);
    }

    /**
     * @param song
     * @param passedArrangement
     */
    public Performance arrange(Song song, Arrangement passedArrangement)
    {
        Arrangement arrangement=null;
        if(passedArrangement==null)
        {
            arrangement=new DefaultArrangement(song);
        }
        else
        {
            arrangement=passedArrangement;
        }
        
        int tracks = song.getTrackCount();
        List<Integer> arrangedMeasures=arrangement.getMeasureList();
        int measures = arrangedMeasures.size();
        
        if(soundPlayer instanceof TimerSettings)
        {
            ((TimerSettings)soundPlayer).setTimerFrequency(timerFrequency);
        }
        if(soundPlayer instanceof EventSettings)
        {
            ((EventSettings)soundPlayer).enableNoteEvents(noteEventsEnabled);
        }

        Performance performance = soundPlayer.createPerformance(tracks, song.getTempo(),
                song.getResolution());
        
        performance.setTimerFrequency(timerFrequency);

        // add timing events to the performance
        TimeSignature lastTimeSignature=null;
        int location = 0;
        for (int m = 0; m < measures; m++)
        {
            Integer measureIndex=(Integer)arrangedMeasures.get(m);
            SongMeasure measure = song.getPerformanceMeasure(measureIndex.intValue());
            
            TimeSignature currentTimeSignature=measure.getTimeSignature();
            if((lastTimeSignature==null)||(!currentTimeSignature.equals(lastTimeSignature)))
            {
                performance.setTimeSignature(location,currentTimeSignature);
                lastTimeSignature=currentTimeSignature;
            }
            
            performance.addTimerEvents(measure, location);
            location += measure.getLength();
        }

        // loop for all tracks
        for (int t = 1; t <= tracks; t++)
        {
            SongTrack songTrack = song.getTrack(t);
            performance.initializeTrack(songTrack);

            int virtualTracks = songTrack.getVirtualTrackCount();

            // loop for all virtual tracks
            for (int v = 0; v < virtualTracks; v++)
            {
                EventStream es = new EventStream(songTrack, v);
                location = 0;
                for (int m = 0; m < measures; m++)
                {
                    Integer measureIndex=(Integer)arrangedMeasures.get(m);
                    SongMeasure measure = song.getPerformanceMeasure(measureIndex.intValue());

                    SongMeasureTrack smt = measure.getTrack(songTrack);
                    SongVirtualTrack svt = smt.getVirtualTrack(v);
                    List<SongEvent> events = svt.getEvents();
                    es.addEvents(events, location);
                    
                    location+=measure.getLength();
                }
                es.close();
                performance.addMusicalEvents(es);
            }
        }
        
        soundPlayer.setPerformance(performance);
        return performance;
    }

    /**
     *  
     */
    public void start()
    {
        soundPlayer.start();
    }

    /**
     *  
     */
    public void stop()
    {
        ((RealtimeSoundPlayer)soundPlayer).stop();
    }

    /**
     *  
     */
    public void waitForCompletion()
    {
        ((RealtimeSoundPlayer)soundPlayer).waitForCompletion();
    }

    /**
     *  
     */
    public void close()
    {
        soundPlayer.close();
    }

    /* (non-Javadoc)
     * @see players.sound.SoundPlayer#createPerformance(int, adaptors.song.Tempo, int)
     */
    public Performance createPerformance(int tracks, Tempo tempo, int resolution)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see players.sound.SoundPlayer#setPerformance(players.sound.Performance)
     */
    public void setPerformance(Performance performance)
    {
        soundPlayer.setPerformance(performance);
    }
}