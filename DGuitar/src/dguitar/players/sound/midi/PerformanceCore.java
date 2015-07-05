/*
 * Created on Mar 2, 2005
 */
package dguitar.players.sound.midi;

import java.util.logging.Logger;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import dguitar.adaptors.song.SongEvent;
import dguitar.adaptors.song.SongTrack;
import dguitar.adaptors.song.Tempo;
import dguitar.adaptors.song.TimeSignature;
import dguitar.adaptors.song.impl.SongDeviceImpl;
import dguitar.players.sound.EventStream;
import dguitar.players.sound.Performance;
import dguitar.players.sound.PerformanceEvent;
import dguitar.players.sound.PerformanceNoteEvent;


/**
 * Implementation of a Performance
 * @author crnash
 */
public abstract class PerformanceCore extends Sequence implements Performance
{
    private static String className=PerformanceCore.class.toString();
    private static Logger logger=Logger.getLogger(className);
       
    Tempo tempo;
    
    /**
     * @param tracks
     * @param tempo
     * @param resolution
     * @throws InvalidMidiDataException
     */
    public PerformanceCore(int tracks, Tempo tempo, int resolution) throws InvalidMidiDataException
    {
        super(PPQ,resolution,tracks);
        setTempo(tempo);
    }

    /**
     * @param track
     * @param time
     * @param mm
     */
    private void makeEvent(int track, long time, MidiMessage mm)
    {
        Track[] tracksB=getTracks();
        Track target=tracksB[track];
        
        MidiEvent me=new MidiEvent(mm,time);
        target.add(me);
    }

    private void makeEvent(int track, long time, int status, int data1, int data2)
    {
        ShortMessage sm=new ShortMessage();
        try
        {
            sm.setMessage(status,data1,data2);
            makeEvent(track,time,sm);
        }
        catch(InvalidMidiDataException e)
        {
            logger.warning("Invalid midi data exception "+status+" "+data1+" "+data2);
        }
    }

    /**
     * @param tempo
     * @throws InvalidMidiDataException
     */
    private void setTempo(Tempo tempo) throws InvalidMidiDataException
    {
        this.tempo=tempo;
        
        int usq=(int)tempo.getUSQ();
        MetaMessage mm=new MetaMessage();
        byte[] data=new byte[3];
        
        data[0]=(byte)((usq>>16)&0x00FF);
        data[1]=(byte)((usq>> 8)&0x00FF);
        data[2]=(byte)((usq    )&0x00FF);
        mm.setMessage(0x51,data,3);
        
        makeEvent(0,0,mm);
    }
    
    /* (non-Javadoc)
     * @see players.sound.Performance#getTempo()
     */
    public Tempo getTempo()
    {
        return tempo;
    }

    /* (non-Javadoc)
     * @see players.sound.Performance#initializeTrack(adaptors.song.SongTrack)
     */
    public void initializeTrack(SongTrack track)
    {
        int index=track.getIndex();
        SongDeviceImpl device=(SongDeviceImpl)track.getPrimaryDevice();
        int channel=device.getChannel();
           
        // set the pitch bend sensitivity
        int sensitivity=track.getBendSensitivity();        
        makeEvent(index,0,ShortMessage.CONTROL_CHANGE+(channel-1),100,0);
        makeEvent(index,0,ShortMessage.CONTROL_CHANGE+(channel-1),101,0);
        makeEvent(index,0,ShortMessage.CONTROL_CHANGE+(channel-1),6,sensitivity);
        
        // center the wheel
        makeEvent(index,0,ShortMessage.PITCH_BEND+(channel-1),0x00,0x40);
        
        // program change
        int program=track.getProgram();
        makeEvent(index,0,ShortMessage.PROGRAM_CHANGE+(channel-1),program,0);

        int volume=track.getVolume();
        makeEvent(index,0,ShortMessage.CONTROL_CHANGE+(channel-1),7,volume);

        int pan=track.getPan();
        makeEvent(index,0,ShortMessage.CONTROL_CHANGE+(channel-1),10,pan);
        
        int chorus=track.getChorus();
        makeEvent(index,0,ShortMessage.CONTROL_CHANGE+(channel-1),93,chorus);

        int reverb=track.getReverb();
        makeEvent(index,0,ShortMessage.CONTROL_CHANGE+(channel-1),91,reverb);

        int tremolo=track.getTremolo();
        makeEvent(index,0,ShortMessage.CONTROL_CHANGE+(channel-1),92,tremolo);
        
        int phaser=track.getPhaser();
        makeEvent(index,0,ShortMessage.CONTROL_CHANGE+(channel-1),95,phaser);       
    }



    /* (non-Javadoc)
     * @see players.sound.Performance#addMusicalEvents(players.sound.EventStream)
     */
    public void addMusicalEvents(EventStream events)
    {
        SongTrack track=events.getTrack();
        SongDeviceImpl device=(SongDeviceImpl)track.getPrimaryDevice();
        int channel=device.getChannel();
        int index=track.getIndex();
        
        Track[] midiTracks=getTracks();
        Track midiTrack=midiTracks[index];
        
        PerformanceEvent event=events.getFirst();
        while(event!=null)
        {
            PerformanceNoteEvent noteEvent=(PerformanceNoteEvent)event;
            SongEvent songEvent=noteEvent.getEvent();
            
            int offset=event.getOffset();
                        
            EventFactory.generateEvents(midiTrack,offset,songEvent.getMessage(),channel);
   
            recordPerformanceEvent(event);
            event=event.getNext();
        }   
    }

    /**
     * @param event
     */
    protected abstract void recordPerformanceEvent(PerformanceEvent event);

    public void setTimeSignature(int location, TimeSignature timeSignature)
    {
        MetaMessage mm=new MetaMessage();
        byte data[]=new byte[4];
        
        int numerator=timeSignature.getNumerator();
        int denominator=timeSignature.getDenominator();
        
        data[0]=(byte)numerator;
        
        // denominator must be an exact power of 2 that divides 96
        switch(denominator)
        {
        case 1:
            data[1]=0;
            break;
        case 2:
            data[1]=1;
            break;
        case 4:
            data[1]=2;
            break;
        case 8:
            data[1]=3;
            break;
        case 16:
            data[1]=4;
            break;
        case 32:
            data[1]=5;
            break;
        default:
            logger.severe("Time signature denominator "+denominator+" is not supported");
        }
        data[2]=(byte)(96/denominator);
        data[3]=8;
        try
        {
            mm.setMessage(0x58,data,4);
            makeEvent(0,location,mm); 
        }
        catch (InvalidMidiDataException e)
        {
            // TODO consider better exceptions round here
            logger.severe("Invalid time signature MIDI data");
        }
       
    }
}
