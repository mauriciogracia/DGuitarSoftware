/*
 * Created on Mar 17, 2005
 */
package dguitar.players.sound.midi;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import dguitar.adaptors.song.SongMessage;
import dguitar.adaptors.song.event.SongNoteOffMessage;
import dguitar.adaptors.song.event.SongNoteOnMessage;

/**
 * @author Chris
 */
public class EventFactory
{

    /**
     * @param midiTrack
     * @param offset
     * @param message
     */
    public static void generateEvents(Track midiTrack, int offset, SongMessage message,int channel)
    {
        if(message instanceof SongNoteOnMessage)
        {
            SongNoteOnMessage snom=(SongNoteOnMessage)message;

            // create a note on midi message at offset on channel
            int pitch=snom.getPitch();
            int velocity=snom.getVelocity();
            
            ShortMessage noteOn=new ShortMessage();

            try
            {
                noteOn.setMessage(ShortMessage.NOTE_ON+(channel-1),pitch,velocity);                
                MidiEvent eOn =new MidiEvent(noteOn, offset);                
                midiTrack.add(eOn);
            }
            catch (InvalidMidiDataException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }           
        } 
        else if(message instanceof SongNoteOffMessage)
        {
            SongNoteOffMessage snoff=(SongNoteOffMessage) message;
            SongNoteOnMessage snom=snoff.getSource();
            int pitch=snom.getPitch();
            ShortMessage noteOff=new ShortMessage();
            try
            {
                noteOff.setMessage(ShortMessage.NOTE_ON+(channel-1),pitch,0);
                MidiEvent eOff=new MidiEvent(noteOff,offset);
                midiTrack.add(eOff); 
            }
            catch (InvalidMidiDataException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
           
        }
    }

}
