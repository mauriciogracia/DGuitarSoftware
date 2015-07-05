/*
 * Created on Mar 19, 2005
 */
package dguitar.codecs.midi;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;



import dguitar.adaptors.guitarPro.GPAdaptor;
import dguitar.adaptors.song.Song;
import dguitar.codecs.CodecFormatException;
import dguitar.codecs.CodecOutputStream;
import dguitar.codecs.guitarPro.GPFormatException;
import dguitar.codecs.guitarPro.GPSong;
import dguitar.players.sound.MasterPlayer;
import dguitar.players.sound.midi.MidiFiler;
import dguitar.players.sound.midi.PerformanceFile;
import dguitar.players.sound.midi.PerformanceLive;

/**
 * @author Chris
 */
public class MidiOutputStream implements CodecOutputStream
{
    OutputStream out;

    public MidiOutputStream(OutputStream out)
    {
        this.out = out;
    }

    public void close() throws IOException
    {
        out.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see codecs.CodecOutputStream#write(java.lang.Object)
     */
    public int write(Object obj) throws IOException, CodecFormatException
    {
        if (obj instanceof GPSong)
        {
            writeGPSong((GPSong) obj);
        }
        else if (obj instanceof Song)
        {
            writeSong((Song) obj);
        }
        else if (obj instanceof PerformanceFile)
        {
            writePerformanceFile((PerformanceFile) obj);
        }
        else if (obj instanceof PerformanceLive)
        {
            writePerformanceLive((PerformanceLive) obj);
        }
        // TODO find out how the error codes work
        return 0;
    }

    /**
     * @param live
     * @throws IOException
     */
    private void writePerformanceLive(PerformanceLive live) throws IOException
    {
        Sequence sequence = live;
        // This sequence has an extra event track. ABout all we can do to make
        // this work is get everything off the track, delete the track, and
        // then recreate a new track with the same events in

        Track[] allTracks = sequence.getTracks();
        Track lastTrack = allTracks[allTracks.length - 1];

        int trackSize = lastTrack.size();
        List<MidiEvent> eventBuffer = new LinkedList<MidiEvent>();

        for (int i = 0; i < trackSize; i++)
        {
            eventBuffer.add(lastTrack.get(i));
        }
        sequence.deleteTrack(lastTrack);

        writeSequence(sequence);

        Track newTrack = sequence.createTrack();
        for (Iterator<MidiEvent> it = eventBuffer.iterator(); it.hasNext();)
        {
            MidiEvent event = it.next();
            newTrack.add(event);
        }
    }

    /**
     * @param file
     * @throws IOException
     */
    private void writePerformanceFile(PerformanceFile file) throws IOException
    {
        Sequence sequence = file;
        writeSequence(sequence);
    }

    /**
     * @param sequence
     * @throws IOException
     */
    private void writeSequence(Sequence sequence) throws IOException
    {
        MidiSystem.write(sequence,1,out);
    }

    /**
     * @param song
     * @throws IOException
     */
    private void writeSong(Song song) throws IOException
    {
        // Arrange the SOng into a PerformanceFile
        MasterPlayer player = new MasterPlayer();
        player.setSoundPlayer(new MidiFiler());
        PerformanceFile pf = (PerformanceFile) player.arrange(song, null);
        writePerformanceFile(pf);
    }

    /**
     * @param gpsong
     * @throws MidiFormatException
     * @throws IOException
     */
    private void writeGPSong(GPSong gpsong) throws MidiFormatException, IOException
    {
        Song song = null;
        try
        {
            song = GPAdaptor.makeSong(gpsong);
        }
        catch (GPFormatException e)
        {
            throw new MidiFormatException(
                    "Could not convert GPSong to Song format", e);
        }
        writeSong(song);
    }

    /*
     * (non-Javadoc)
     * 
     * @see codecs.Codec#supportedExtension(java.lang.String)
     */
    public boolean supportedExtension(String extension)
    {
        return (extension.toUpperCase().equals(".MID"));
    }

}