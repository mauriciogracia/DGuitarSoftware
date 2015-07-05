/*
 * Created on Mar 18, 2005
 */
package dguitar.players.sound.midi;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;

import dguitar.adaptors.song.Tempo;
import dguitar.players.sound.Performance;
import dguitar.players.sound.SoundPlayer;

/**
 * @author crnash
 */
public class MidiFiler implements SoundPlayer
{
    private String className=MidiFiler.class.toString();
    private Logger logger=Logger.getLogger(className);
    
    PerformanceFile performanceFile;
    private String fileName;

    /**
     * @param fileName
     */
    public MidiFiler(String fileName)
    {
        this.fileName=fileName;
    }

    /**
     * 
     */
    public MidiFiler()
    {
        this.fileName=null;
    }

    public Performance createPerformance(int tracks, Tempo tempo, int resolution)
    {
        try
        {
            return new PerformanceFile(tracks,tempo,resolution);
        }
        catch (InvalidMidiDataException e)
        {
            logger.severe("Could not create a performance object");
            return null;
        }
    }

    /* (non-Javadoc)
     * @see players.sound.SoundPlayer#start()
     */
    public void start()
    {
        if(fileName!=null)
        {
	        //OLD Sequence sequence=(Sequence)performanceFile;
	        Sequence sequence = performanceFile;
	        File file=new File(fileName);
	        try
	        {
	            MidiSystem.write(sequence,1,file);
	        }
	        catch (IOException e)
	        {
	            logger.severe("IOException when writing file");
	        }
        }
    }

    /* (non-Javadoc)
     * @see players.sound.SoundPlayer#close()
     */
    public void close()
    {
    }

    /* (non-Javadoc)
     * @see players.sound.SoundPlayer#setPerformance(players.sound.Performance)
     */
    public void setPerformance(Performance performance)
    {
        performanceFile=(PerformanceFile)performance;        
    }
}
