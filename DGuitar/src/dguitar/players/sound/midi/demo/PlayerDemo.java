/*
 * Created on Mar 9, 2005
 */
package dguitar.players.sound.midi.demo;

import java.io.IOException;
import java.util.logging.Logger;

import dguitar.adaptors.guitarPro.GPAdaptor;
import dguitar.adaptors.song.Song;
import dguitar.codecs.guitarPro.GPFormatException;
import dguitar.codecs.guitarPro.GPInputStream;
import dguitar.codecs.guitarPro.GPSong;
import dguitar.players.sound.Arrangement;
import dguitar.players.sound.MasterPlayer;
import dguitar.players.sound.SoundPlayer;
import dguitar.players.sound.midi.MidiPlayer;

/**
 * @author crnash
 */
public class PlayerDemo
{
    private static String className=PlayerDemo.class.toString();
    private static Logger logger=Logger.getLogger(className);
    
    public static void main(String[] args)
    {
        if(args.length!=1)
        {
            logger.severe("Syntax is PlayerDemo fileName");
        }
        else
        {    
            playerDemo(args[0],new MidiPlayer(),new SystemLinePrinter());            
        }
        
        // WARNING You must exit a program that uses Java Sound like this... Java Sound
        // leaves threads running, and if you don't do this your application will hang.
        // (jsresources). Swing applications will appear to exit, however they will
        // be still running without windows (on a Windows machine, the cup of coffee
        // icon stays in the toolbar).
        System.exit(0); 
    }
    /**
     * 
     * @param fileName
     * @param sp
     * @param out
     */
    public static void playerDemo(String fileName,SoundPlayer sp, LinePrinter out)
    {
        try
        {
            // Load the GP* file using the DGuitar decoder, and then "bless" it
            // with the generic Song interfaces (still not yet finalized).
            // TODO Some Song classes still contain raw MIDI data, this needs
            // to be moved down into the player.
            Song song=loadGPToSong(fileName);
            
            // Create the master player. The master player is a 'framework' on
            // which we can hang all sorts of EventListeners and SoundPlayers.
            // We issue all instructions through the MasterPlayer and it sends
            // them to the appropriate SoundPlayer, or timer device.
            MasterPlayer player=new MasterPlayer();
            
            // Specify a SoundPlayer. This one is the MIDI implementation.
            // SoundPlayers have to be sligthly different than event listeners.
            // The main difference is a SoundPlayer must create whatever timer
            // resources it needs (while event listeners can react to any type
            // of timer, the sound implementations need a more accurate clock).
            player.setSoundPlayer(sp);
            
            // Specify the subdivision of timing frequency. Here we will specify
            // we want the timer to click 4 times for every musical notation
            // (for example, if the score is 4/4, then we will click 16 times
            // in each measure). Some timer implementations will not allow you
            // to change the clock frequency like this.
            player.setTimerFrequency(4);
            
            // Specify we also want to receive other types of events
            player.enableNoteEvents(true);		// deliver all note events to all Listeners
            
            // Add listeners for the timer and for the other events. Listeners
            // just implement the PerformanceXXXListener interfaces. It is up
            // to the SoundPlayer and the timer to decide how to implement
            // these. I believe the MIDI module uses the standard Java event
            // mechanism, so you should be able to do normal Swing
            // event handling.
            player.addTimerListener(new ExampleTimerListener(out));
            player.addEventListener(new ExampleEventListener(out));
            
            // specify what song to play, and how to arrange it
            // (arrangement not supported yet, so this will play the entire song
            // at the default tempo set at the top of the song). Note that the
            // Arrangement is done by the particular player, so for instance
            // the MIDI player will make a MIDI file
            Arrangement arrangement=null;
            player.arrange(song,arrangement);
            
            // Tell the master player to start playing, which will start the
            // performance, all the clocks, and begin sending events to listeners.
            player.start();
            
            // wait until the player is finished. You can call player.stop() if
            // you want to stop early. You may even be able to stop and restart.
            player.waitForCompletion();
            
            // And then close it. You must close the master player when you have
            // finished with it, and throw it away. Later I will make sure you
            // can just start one at the start of the application and reuse it
            // again and again.
            player.close();
        }
        catch (GPFormatException e1)
        {
            logger.severe("gpformatexception");
        }
        catch (IOException e1)
        {
            logger.severe("ioexception");
        }        
    }

    /**
     * Attempt to load a GP file as a Song.
     * @param fileName
     * @return a Song
     * @throws GPFormatException
     * @throws IOException
     */
    public static Song loadGPToSong(String fileName) throws GPFormatException, IOException
    {
        GPInputStream gpis = new GPInputStream(fileName);
        GPSong piece = (GPSong) gpis.readObject();
        gpis.close();
        Song song=GPAdaptor.makeSong(piece);
        return song;
    }
}
