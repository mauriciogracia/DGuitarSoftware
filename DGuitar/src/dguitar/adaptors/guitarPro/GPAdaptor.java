/*
 * Created on Feb 28, 2005
 */
package dguitar.adaptors.guitarPro;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import dguitar.adaptors.song.RepeatedSongPhrase;
import dguitar.adaptors.song.Song;
import dguitar.adaptors.song.SongMeasure;
import dguitar.adaptors.song.SongMeasureTrack;
import dguitar.adaptors.song.SongPhrase;
import dguitar.adaptors.song.SongPhraseList;
import dguitar.adaptors.song.SongTrack;
import dguitar.adaptors.song.SongVirtualTrack;
import dguitar.adaptors.song.Tempo;
import dguitar.adaptors.song.TimeSignature;
import dguitar.adaptors.song.event.SongEventImpl;
import dguitar.adaptors.song.event.SongNoteOnMessage;
import dguitar.adaptors.song.event.SongTieMessage;
import dguitar.adaptors.song.impl.RepeatedSongPhraseImpl;
import dguitar.adaptors.song.impl.SongDeviceImpl;
import dguitar.adaptors.song.impl.SongImpl;
import dguitar.adaptors.song.impl.SongMeasureImpl;
import dguitar.adaptors.song.impl.SongMeasureTrackImpl;
import dguitar.adaptors.song.impl.SongPhraseListImpl;
import dguitar.adaptors.song.impl.SongTrackImpl;
import dguitar.codecs.guitarPro.GPBeat;
import dguitar.codecs.guitarPro.GPDuration;
import dguitar.codecs.guitarPro.GPDynamic;
import dguitar.codecs.guitarPro.GPFormatException;
import dguitar.codecs.guitarPro.GPMIDIChannel;
import dguitar.codecs.guitarPro.GPMeasure;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;
import dguitar.codecs.guitarPro.GPNote;
import dguitar.codecs.guitarPro.GPSong;
import dguitar.codecs.guitarPro.GPTrack;

/**
 * An adaptor to give GP4Piece's the Song interface. Perhaps eventually we may
 * not need this (the classes themselves could implement the interfaces).
 * 
 * @author crnash
 */
public class GPAdaptor {
    private static String className = GPAdaptor.class.toString();

    private static Logger logger = Logger.getLogger(className);

    // The following definition of pulses per quarter note is for high
    // resolution,
    // supporting up to dotted 128th notes and all n-tuplet types up to 13.
    // Unfortunately you won't get this in a MIDI file. (Who would ever need
    // dotted 128th, 5, 7, 9, 11 and 13 n-tuplets in the same piece?).
    private static final int PPQ_HIGH_RESOLUTION = 2882880;

    // ...so we compromise. We internally run the adaptor at the high resultion
    // but in actual fact generate a score that's at a far more reasonable
    // scale.
    private static final int PPQ_SCALE_FACTOR = 286;

    /**
     * Create and return a regular SongMeasure from the current position of the
     * iterators.
     * 
     * @param itM
     *            an iterator over the measures collection. It must be pointing
     *            so that itM.next() returns the current measure, which will be
     *            marked as a repeatStart.
     * @param itMTP
     *            an iterator over the measure-track pairs collection. It must
     *            be pointing so that itMTP.next() returns the first track of
     *            the current measure
     * @param tracks
     *            The tracks definitions
     * @return a new filled-out SongMeasure
     * @throws GP4AdaptorException
     */
    private static RepeatedSongPhrase makeRepeatedSongPhrase(Song song,
            ListIterator<GPMeasure> itM, ListIterator<GPMeasureTrackPair> itMTP, List<GPTrack> tracks)
            throws GPFormatException {
        SongPhraseList spl = new SongPhraseListImpl();

        // read the first measure without testing for the repeat marker again.
        spl.addPhrase(makeSongMeasure(song, itM, itMTP, tracks));

        // look back at the last measure processed to see if we've done
        GPMeasure measure = itM.previous();
        itM.next();
        int repeatCount = measure.getNumberOfRepetitions();

        // keep calling the phrase factory until you find a measure
        // that's marked with a repeat count. If you run out of measures
        // (Corneille)
        // then assume the last measure has a repeat count of 1.
        while (repeatCount == 0) {
            if (!itM.hasNext()) {
                logger
                        .fine("Got to end of score before finding a closing repeat mark, assuming 1");
                repeatCount = 1;
                break;
            }
            SongPhrase phrase = phraseFactory(song, itM, itMTP, tracks);
            spl.addPhrase(phrase);
            measure = itM.previous();
            itM.next();
            repeatCount = measure.getNumberOfRepetitions();
            logger.fine("Repeat count measure at " + measure.getNumber()
                    + " is " + repeatCount);
        }

        return new RepeatedSongPhraseImpl(spl, repeatCount);
    }

    /**
     * Create and return a regular SongMeasure from the current position of the
     * iterators.
     * 
     * @param itM
     *            an iterator over the measures collection. It must be pointing
     *            so that itM.next() returns the current measure
     * @param itMTP
     *            an iterator over the measure-track pairs collection. It must
     *            be pointing so that itMTP.next() returns the first track of
     *            the current measure
     * @param tracks
     *            The tracks definitions
     * @return a new filled-out SongMeasure
     * @throws GP4AdaptorException
     */
    private static SongMeasure makeSongMeasure(Song song, ListIterator<GPMeasure> itM,
            ListIterator<GPMeasureTrackPair> itMTP, List<GPTrack> tracks) {
        GPMeasure measure = itM.next();

        int ending = measure.getNumberOfAlternateEnding();
        if (ending > 0) {
            logger.warning("Measure " + measure.getNumber()
                    + " is alternate ending " + ending);
        }

        // TODO getNumber
        // TODO getNumberOfAlternateEnding
        // TODO getNumberOfRepetitions
        // TODO getTonality
        int numerator = measure.getNumerator();
        int denominator = measure.getDenominator();

        int measureLength = PPQ_HIGH_RESOLUTION * 4 * numerator / denominator;
        //OLD MGG logger.finer("Measure "+measure.getNumber()+",
        // "+numerator+"/"+denominator+", length " + measureLength);
        TimeSignature ts = new TimeSignature(numerator, denominator);
        SongMeasure sm = new SongMeasureImpl(measure.getNumber(), measureLength
                / PPQ_SCALE_FACTOR, ts);

        for (int trackIndex = 1; trackIndex <= tracks.size(); trackIndex++) {
            GPTrack track = tracks.get(trackIndex - 1);
            // TODO is12StringedGuitarTrack
            // TODO isBanjoTrack
            // TODO getter/setter for isDrumsTrack

            // TODO getChannelEffects
            // TODO getColor
            // TODO getName
            // TODO getNumberOfFrets
            // TODO getPort
            int capo = track.getCapo();
            int strings = track.getNumberOfStrings();
            int stringTuning[] = new int[strings];
            for (int i = 0; i < strings; i++)
                stringTuning[i] = track.getStringsTuning(i);

            //OLD  MGG logger.finer("Measure " + measure.getNumber() + " Track "+
            // trackIndex);
            GPMeasureTrackPair mtp = itMTP.next();
            SongMeasureTrack smt = new SongMeasureTrackImpl(song
                    .getTrack(trackIndex));
            sm.addTrack(smt);

            List<GPBeat> beats = mtp.getBeats();
            int beatOffset = 0;
            for (Iterator<GPBeat> itBeat = beats.iterator(); itBeat.hasNext();) {
                GPBeat beat = itBeat.next();

                // TODO effects

                GPDuration duration = beat.getDuration();
                // TODO add getter and setter for GP4Beat.dottedNotes
                boolean dotted = beat.dottedNotes;
                int ntuplet = beat.getNTuplet();

                int beatDuration = calculateDuration(duration, dotted, ntuplet);

                if (beat.isNoteBeat()) {
                    List<GPNote> notes = beat.getNotes();
                    List<Integer> stringsPlayed = new LinkedList<Integer>();
                    // TODO suggest an easier way of returning the played
                    // string list
                    for (int i = 6; i >= 0; i--) {
                        if (beat.isStringPlayed(i)) {
                            stringsPlayed.add(new Integer(i));
                        }
                    }

                    Iterator<GPNote> itNotes = notes.iterator();
                    Iterator<Integer> itStrings = stringsPlayed.iterator();
                    
                    while (itNotes.hasNext()) {
                        
//						OLD MGG Integer stringObject = (Integer) itStrings.next();
// see TODO.TXT or NEWFEATURES.TXT
//						this lines were added to fix the GTP playing error - BEGIN
                        Integer stringObject;

                        if (!itStrings.hasNext()) 
                        {
                            //this error appears to happen when its *.GTP file
                            //TODO show a friendly error
                            break;

                        }
                        /*ELSE*/
//						END
                        
                        stringObject = itStrings.next();
                        // Note that the tuning array has the highest string
                        // at index 0
                        // but the track has the highest string at index 6.

                        // TODO find out if this is correct. What about a
                        // 4-string bass?
                        int stringID = 6 - stringObject.intValue();

                        GPNote note = itNotes.next();
                        int noteDuration = beatDuration;

                        // see if this note has time independent settings on
                        // it
                        // TODO getter/setter for GP4Note.duration
                        GPDuration tiDuration = note.duration;
                        if (tiDuration != null) {
                            // TODO getter/setter for GP4Note.isDotted
                            boolean tiDotted = note.isDotted;
                            int tiTriplet = note.getNTuplet();
                            noteDuration = calculateDuration(tiDuration,
                                    tiDotted, tiTriplet);
                        }

                        // TODO isDeadNote
                        // TODO isGhostNote
                        // TODO isTieNote
                        // TODO effects

                        // TODO Note effects are a little bit difficult.
                        // Remember these are guitar effects
                        // so we effectively need a 'virtual track' for
                        // every string, so when we playback we
                        // can assign a unique MIDI channel to that track
                        // and so we can apply a channel-wide
                        // controller to that effect (such as pitch bend).
                        // Even the simple effect (let ring)
                        // can't be done easily right now because we can't
                        // tell when to really kill the note.
                        // Of course if we don't need an effect, we should
                        // try to use as few MIDI channels as
                        // we can!

                        int virtualTrackID = stringID;
                        SongVirtualTrack svt = smt
                                .getVirtualTrack(virtualTrackID);
                        int noteStart = beatOffset / PPQ_SCALE_FACTOR;
                        int noteEnd = (beatOffset + noteDuration)
                                / PPQ_SCALE_FACTOR;

                        // FIXME cover the tie note case by grabbing the
                        // last fret
                        if (note.isTieNote()) {
                            svt.addEvent(new SongEventImpl(noteStart,
                                    new SongTieMessage(noteEnd - noteStart)));
                        } else {
                            int baseFret = note.getFretNumber();//OLD
                            // .getNumberOfFret();
                            int fret = baseFret + capo;
                            GPDynamic dynamic = note.getDynamic();
                            int velocity = DynamicsMap.velocityOf(dynamic);
                            int pitch = stringTuning[stringID] + fret;
                            svt.addEvent(new SongEventImpl(noteStart,
                                    new SongNoteOnMessage(pitch, velocity,
                                            noteEnd - noteStart, baseFret)));
                        }

                    }
                }
                beatOffset += beatDuration;
            }
            if (beatOffset != measureLength) {
                // an underfull one isn't fatal, but an overfull one is
                String reason = "Measure: " + measure.getNumber() + " track: "
                        + trackIndex + " length mismatch, expected "
                        + measureLength + ", got " + beatOffset;

                if (beatOffset > measureLength) {
                    logger.fine(reason);
                    System.err.println(reason);
                } else {
                    logger.fine(reason);
                }

            }
        }
        return sm;
    }

    /**
     * @param duration
     * @param dotted
     * @param ntuplet
     * @return the beat duration
     */
    private static int calculateDuration(GPDuration duration, boolean dotted,
            int ntuplet) {
        int durationIndex = duration.getIndex();
        int beatDuration = (PPQ_HIGH_RESOLUTION * 4) >> durationIndex;
        if (dotted) {
            beatDuration += (beatDuration >> 1);
        }
        if (ntuplet != 0) {
            TripletValue tv = TripletMap.getTriplet(ntuplet);
            beatDuration *= tv.getTimeOf();
            beatDuration /= tv.getNotes();
        }
        return beatDuration;
    }

    /**
     * Look ahead at the next measure, build and determine a suitable SongPhrase
     * to handle it.
     * 
     * @param itM
     *            an iterator over the measures collection. It must be pointing
     *            so that itM.next() returns the current measure, which will be
     *            marked as a repeatStart.
     * @param itMTP
     *            an iterator over the measure-track pairs collection. It must
     *            be pointing so that itMTP.next() returns the first track of
     *            the current measure
     * @param tracks
     *            The tracks definitions
     * @return the next SongPhrase
     */
    private static SongPhrase phraseFactory(Song song, ListIterator<GPMeasure> itM,
            ListIterator<GPMeasureTrackPair> itMTP, List<GPTrack> tracks) throws GPFormatException {
        SongPhrase sp = null;

        GPMeasure measure = itM.next(); // peek inside the measure
        // and dispatch to the right
        // factory
        itM.previous(); // rewind before calling the right factory method

        // TODO add getter and setter for GP4Measure.repeatStart
        logger.finer("Measure " + measure.getNumber() + " repeat start "
                + measure.repeatStart);
        logger.finer("Measure " + measure.getNumber() + " repeat count "
                + measure.getNumberOfRepetitions());

        // if it's the start of a repeat, then make a repeated phrase
        if (measure.repeatStart) {
            logger.fine("Creating a repeated phrase at " + measure.getNumber());
            sp = makeRepeatedSongPhrase(song, itM, itMTP, tracks);
        }

        // Failing everything else, just return a single measure
        if (sp == null) {
            logger.fine("Creating a measure phrase at " + measure.getNumber());
            sp = makeSongMeasure(song, itM, itMTP, tracks);
        }
        return sp;
    }

    /**
     * @param piece
     *            a loaded GP4 file
     * @return a representation of the piece that supports the Song interface
     * @throws GP4AdaptorException
     */
    public static Song makeSong(GPSong piece) throws GPFormatException {
        String methodName = "makeSong";
        logger.entering(className, methodName);

        int bpm = piece.getTempo();
        logger.finer("Tempo: " + bpm + "BPM");

        Tempo tempo = new Tempo();
        tempo.setBPM(bpm);

        Song song = new SongImpl(PPQ_HIGH_RESOLUTION / PPQ_SCALE_FACTOR, tempo);

        List<GPMeasure> measures = piece.getMeasures();
        logger.finer("Number of measures: " + measures.size());

        List<GPTrack> tracks = piece.getTracks();
        logger.finer("Number of tracks: " + tracks.size());

        //List fretStates = new LinkedList();

        int index = 1;
        for (Iterator<GPTrack> it = tracks.iterator(); it.hasNext(); index++) {
            GPTrack track = it.next();
            int port = track.getPort();
            int channel = track.getChannel();
            int channelEffects = track.getChannelEffects();

            logger.fine("Track " + index + " port " + port + " channels "
                    + channel + "," + channelEffects);

            int channelIndex = (port - 1) * 16 + (channel - 1);
            GPMIDIChannel mc = piece.getChannels(channelIndex);

            int strings = track.getNumberOfStrings();

            SongTrack st = new SongTrackImpl(index, strings); // channel is
            // 1-based
            st.setPrimaryDevice(new SongDeviceImpl(port, channel));
            if (channel != channelEffects) {
                st.setSecondaryDevice(new SongDeviceImpl(port, channelEffects));
            }

            st.setBendSensitivity(2);

            int instrument = mc.getInstrument();
            logger.fine("Instrument " + instrument);

            // Only set valid instruments. The drum track usually doesn't have
            // one.
            st.setProgram(instrument);

            // TODO Verify the volume setting is correct
            st.setVolume(8 * mc.getVolume());
            // TODO Verify the pan setting is correct
            st.setPan(8 * mc.getBalance());
            // TODO Verify the chorus setting is correct
            st.setChorus(8 * mc.getChorus());
            // TODO verify the reverb setting is correct
            st.setReverb(8 * mc.getReverb());
            // TODO Verify the tremolo setting is correct
            st.setTremolo(8 * mc.getTremolo());
            // TODO Verify the phaser setting is correct
            st.setPhaser(8 * mc.getPhaser());

            song.addTrack(st);
        }

        List<GPMeasureTrackPair> measureTrackPairs = piece.getMeasuresTracksPairs();
        logger.finer("Measure/Track pairs: " + measureTrackPairs.size());

        ListIterator<GPMeasureTrackPair> itMTP = measureTrackPairs.listIterator();
        ListIterator<GPMeasure> itM = measures.listIterator();

        while (itM.hasNext()) {
            SongPhrase sp = phraseFactory(song, itM, itMTP, tracks);
            song.addPhrase(sp);
        }

        logger.exiting(className, methodName, song);
        return song;
    }
}