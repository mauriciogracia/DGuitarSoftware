/*
 * Created on Mar 18, 2005
 */
package dguitar.adaptors.midi;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

import dguitar.adaptors.song.Song;
import dguitar.adaptors.song.SongDevice;
import dguitar.adaptors.song.SongEvent;
import dguitar.adaptors.song.SongMeasure;
import dguitar.adaptors.song.SongMeasureTrack;
import dguitar.adaptors.song.SongTrack;
import dguitar.adaptors.song.SongVirtualTrack;
import dguitar.adaptors.song.Tempo;
import dguitar.adaptors.song.TimeSignature;
import dguitar.adaptors.song.event.SongEventImpl;
import dguitar.adaptors.song.event.SongNoteOnMessage;
import dguitar.adaptors.song.impl.SongDeviceImpl;
import dguitar.adaptors.song.impl.SongImpl;
import dguitar.adaptors.song.impl.SongMeasureImpl;
import dguitar.adaptors.song.impl.SongMeasureTrackImpl;
import dguitar.adaptors.song.impl.SongTrackImpl;


/**
 * An adaptor to import MIDI files as a Song. This class is to be used mainly
 * for test case implementations (we can roundtrip MIDI-Adaptor-MIDI, and
 * compare MIDI-Adaptor to GPSong-Adaptor).
 * 
 * @author crnash
 */
public class MidiAdaptor
{
//UNUSED private static String className = MidiAdaptor.class.toString();
//UNUSED    private static Logger logger = Logger.getLogger(className);

    public static Song makeSong(String midiFile)
            throws InvalidMidiDataException, IOException
    {
        // load the MIDI file
        File file = new File(midiFile);
        MidiFileFormat mff = MidiSystem.getMidiFileFormat(file);
        int ppq = mff.getResolution();
        Sequence seq = MidiSystem.getSequence(file);

        Track[] midiTracks = seq.getTracks();

        // find the tempo object (MIDIfile meta 0x51) in track 0. We will
        // assume we are processing a format 1 MIDI file
        Track tempoMap = midiTracks[0];
        MetaMessage mm = findMetaMessage(tempoMap, 0, 0x51);
        byte[] messageData = mm.getData();
        int usq = ((messageData[0] & 0x00FF) << 16)
                | ((messageData[1] & 0x00FF) << 8)
                | ((messageData[2] & 0x00FF));

        Tempo tempo = new Tempo();
        tempo.setUSQ(usq);

        // make the song object
        Song song = new SongImpl(ppq, tempo);

        // consider it as containing a single measure of length
        // equal to the number of ticks in the longest track
        long longest = 0;
        for (int t = 1; t < midiTracks.length; t++)
        {
            Track midiTrack = midiTracks[t];
            long thisTrack = midiTrack.ticks();
            if (thisTrack > longest) longest = thisTrack;
        }

        // may as well fake a time signature here
        SongMeasure songMeasure = new SongMeasureImpl(0, (int) longest, new TimeSignature(4,4));
        song.addPhrase(songMeasure);

        // build the track objects. Any events on these tracks, at timestamp 0,
        // will be assumed to define the properties of the track
        int usedTracks = 0;
        for (int t = 1; t < midiTracks.length; t++)
        {
            // OK... now the problem was that we assumed the incoming MIDI file
            // would contain only a single channel of MIDI events. Of course,
            // this isn't actually the case, because effects are written on
            // two channels, but to a single track.

            // To resolve this for the MIDI importer we will walk the track and
            // redirect events onto separate channels.

            Track midiTrack = midiTracks[t];

            // split the track into a list of separate per-channel events
            Collection<List<MidiEvent>> splitTracks = splitTrack(midiTrack);
            for (Iterator<List<MidiEvent>> it = splitTracks.iterator(); it.hasNext();)
            {
                List<MidiEvent> singleTrack = it.next();
                encodeTrack(song, songMeasure, singleTrack, ++usedTracks);
            }

        }

        return song;
    }

    /**
     * @param midiTrack
     * @return a collection with the tracks split
     */
    private static Collection<List<MidiEvent>> splitTrack(Track midiTrack)
    {
        Map<Integer, List<MidiEvent>> channelToTrack=new HashMap<Integer, List<MidiEvent>>();
        
        int count=midiTrack.size();
        for(int i=0;i<count;i++)
        {
            MidiEvent me=midiTrack.get(i);
            MidiMessage mm=me.getMessage();
            int status=mm.getStatus();
            if(status<0x00F0)
            {
                int channel=status&0x0F;
                List<MidiEvent> thisTrack=channelToTrack.get(new Integer(channel));
                if(thisTrack==null)
                {
                    thisTrack=new LinkedList<MidiEvent>();
                    channelToTrack.put(new Integer(channel),thisTrack);
                }
                thisTrack.add(me);
            }
        }
        
        // return all the values in the map
        return channelToTrack.values();
    }

    private static void encodeTrack(Song song, SongMeasure songMeasure,
            List<MidiEvent> singleTrack, int trackIndex)
    {

        // assume it has one virtual track
        SongTrack st = new SongTrackImpl(trackIndex, 1);
        song.addTrack(st);
        // make a measure track pair
        SongMeasureTrack smt = new SongMeasureTrackImpl(st);
        songMeasure.addTrack(smt);
        // get the virtual track
        SongVirtualTrack svt = smt.getVirtualTrack(0);

        // iterate over MIDI events. Add initial controllers to the
        // SongTrack, add musical events to the SongVirtualTrack.
        // Note that note on events need to be combined with their
        // corresponding note offs

        // the registered parameter number
        
//UNUSED        int r100 = 0;
//      UNUSED        int r101 = 0;

        boolean processingControllers = true;
        int channel = 0;
        Map<Integer, SongEvent> openNotes = new HashMap<Integer, SongEvent>();

        int events = singleTrack.size();

        for (int i = 0; i < events; i++)
        {
            MidiEvent midiEvent = singleTrack.get(i);
            long offset = midiEvent.getTick();
            MidiMessage message = midiEvent.getMessage();

            int status = message.getStatus();

            // Throw out system realtimes and file meta
            if (status < 0x00F0)
            {
                int command = status & 0x00F0;
                channel = (status & 0x000F) + 1; // musician units :)

                if (processingControllers)
                {
                    if ((command == ShortMessage.NOTE_ON)
                            || (command == ShortMessage.NOTE_OFF))
                    {
                        processingControllers = false;
                    }
                }

                if (processingControllers)
                {
                    switch (command)
                    {
                    case ShortMessage.CONTROL_CHANGE:
                    {
                        ShortMessage cc = (ShortMessage) message;
                        int controller = cc.getData1();
                        int value = cc.getData2();
                        switch (controller)
                        {
                        // I really, really wish the MIDI classee has these
                        // as static const final!
                        case 100:
//                        	UNUSED                            r100 = value;
                            break;
                        case 101:
//                        	UNUSED                            r101 = value;
                            break;
                        case 6:
                            st.setBendSensitivity(value);
                            break;
                        case 7:
                            st.setVolume(value);
                            break;
                        case 10:
                            st.setPan(value);
                            break;

                        case 93:
                            st.setChorus(value);
                            break;
                        case 91:
                            st.setReverb(value);
                            break;
                        case 92:
                            st.setTremolo(value);
                            break;
                        case 95:
                            st.setPhaser(value);
                            break;
                        default:
                            
                        }
                    }
                        break;
                    case ShortMessage.PROGRAM_CHANGE:
                    {
                        ShortMessage pc = (ShortMessage) message;
                        st.setProgram(pc.getData1());
                    }
                        break;
                    default:
                        // ignore this one
                        
                    }
                }
                else
                {
                    if ((command == ShortMessage.NOTE_ON)
                            || (command == ShortMessage.NOTE_OFF))
                    {
                        ShortMessage sm = (ShortMessage) message;
                        int data1 = sm.getData1();
                        int data2 = sm.getData2();

                        boolean noteOn = ((command == ShortMessage.NOTE_ON) && (data2 != 0));
                        if (noteOn)
                        {
                            // pitch, velocity, duration
                            SongNoteOnMessage snom = new SongNoteOnMessage(
                                    data1, data2, 0,0);
                            SongEvent se = new SongEventImpl((int) offset,snom);
                            openNotes.put(new Integer(data1), se);
                            svt.addEvent(se);
                        }
                        else
                        {
                            SongEvent se = openNotes
                                    .get(new Integer(data1));
                            int noteStart = se.getTime();
                            SongNoteOnMessage snom = (SongNoteOnMessage) se
                                    .getMessage();
                            snom.setDuration((int) offset - noteStart);
                        }
                    }
                }
            }
        }
        SongDevice device = new SongDeviceImpl(1, channel);
        st.setPrimaryDevice(device);
    }

    static MetaMessage findMetaMessage(Track track, long timestamp, int metaType)
    {
        int events = track.size();
        for (int i = 0; i < events; i++)
        {
            MidiEvent me = track.get(i);
            long eventTime = me.getTick();
            if (eventTime > timestamp) return null;
            if (eventTime == timestamp)
            {
                MidiMessage mm = me.getMessage();
                if (mm instanceof MetaMessage)
                {
                    MetaMessage m = (MetaMessage) mm;
                    if (m.getType() == metaType) { return m; }
                }
            }
        }
        return null;
    }
}