package dguitar.codecs.guitarPro.version2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import dguitar.codecs.guitarPro.GPBeat;
import dguitar.codecs.guitarPro.GPBend;
import dguitar.codecs.guitarPro.GPBendPoint;
import dguitar.codecs.guitarPro.GPBendType;
import dguitar.codecs.guitarPro.GPChordDiagram;
import dguitar.codecs.guitarPro.GPChordNote;
import dguitar.codecs.guitarPro.GPChordType;
import dguitar.codecs.guitarPro.GPDuration;
import dguitar.codecs.guitarPro.GPDynamic;
import dguitar.codecs.guitarPro.GPEffectsOnBeat;
import dguitar.codecs.guitarPro.GPEffectsOnNote;
import dguitar.codecs.guitarPro.GPFormatException;
import dguitar.codecs.guitarPro.GPGraceNote;
import dguitar.codecs.guitarPro.GPHarmonic;
import dguitar.codecs.guitarPro.GPKey;
import dguitar.codecs.guitarPro.GPMIDIChannel;
import dguitar.codecs.guitarPro.GPMeasure;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;
import dguitar.codecs.guitarPro.GPMixTableChange;
import dguitar.codecs.guitarPro.GPMixTableElement;
import dguitar.codecs.guitarPro.GPNote;
import dguitar.codecs.guitarPro.GPSlide;
import dguitar.codecs.guitarPro.GPSong;
import dguitar.codecs.guitarPro.GPTonalityType;
import dguitar.codecs.guitarPro.GPTrack;
import dguitar.codecs.guitarPro.GPVibrato;
import dguitar.codecs.guitarPro.GPInputStream;

/**
 * The class GP2InputStream allows to parse an InputStream containing a GTP file
 * which is encoded in the 2.20 or 2.21 format.
 * 
 * @author Matthieu Wipliez
 */
public class GP2InputStream extends GPInputStream {
    /**
     * The versions this parser supports.
     */
    private static final String supportedVersions[] = {
            "FICHIER GUITAR PRO v2.20", "FICHIER GUITAR PRO v2.21" };

    /**
     * Creates a new GP2InputStream from an existing stream.
     */
    public GP2InputStream(InputStream in) {
        super(in);
    }

    /**
     * Creates a new GP4InputStream by cloning an existing GPInputStream.
     * 
     * @param gpIn
     *            the original GPInputStream.
     */
    public GP2InputStream(GPInputStream gpIn) {
        super(gpIn);
    }

    /**
     * This method checks if the String version if supported.
     * 
     * @param version
     *            the version described by a string.
     * @return true if the version is supported.
     */
    public static boolean supportedVersion(String version) {
        int i;
        boolean correct;

        correct = false;
        for (i = 0; (i < supportedVersions.length) && (!correct); i++) {
            correct = version.equals(supportedVersions[i]);
        }
        return correct;
    }

    /**
     * This methods returns a String with the supported versions separated by
     * '\n'.
     * 
     * @return "FICHIER GUITAR PRO v2.20", "FICHIER GUITAR PRO v2.21"
     */
    public static String supportedVersions() {
        int i;
        String s;

        s = "";
        for (i = 0; i < supportedVersions.length; i++) {
            s += supportedVersions[i];
            if (i + 1 < supportedVersions.length)
                s += "\n";
        }
        return s;
    }

    /**
     * Reads a piece from the stream.
     * 
     * @return the GPSong read.
     * @throws IOException
     */
    public GPSong readPiece() throws IOException {
        GPSong piece = new GPSong();
        String s;

        // Version
        if (__version.equals("")) {
            __version = readStringByte(30);
        }
        
        piece.setVersion(__version);

        // Title
        s = readStringByteMaxLengthPlusOne();
        piece.setTitle(s);

        // Author of the song
        s = readStringByteMaxLengthPlusOne();
        piece.setAuthorSong(s);

        // Instructions
        s = readStringByteMaxLengthPlusOne();
        piece.setInstruction(s);

        // Tempo
        piece.setTempo(readInt());

        // Triplet Feel
        piece.tripletFeel = (readInt() == 1);

        // Key (usually C)
        GPKey key = GPKey.valueOf(readInt());
        piece.setKey(key);

        // No octave
        piece.setOctave(0);

        // Reads tracks' tuning
        List<GPTrack> tracks = piece.getTracks();
        for (int i = 0; i < 8; i++) {
            GPTrack track = new GPTrack();
            tracks.add(track);

            int numStrings = readInt();
            track.setNumberOfStrings(numStrings);

            for (int j = 0; j < numStrings; j++) {
                track.setStringsTuning(j, readInt());
            }
        }

        // Number of measures
        int numberOfMeasures = readInt();

        // Reads tracks' information
        for (int i = 0; i < 8; i++) {
            GPTrack track = (GPTrack) tracks.get(i);
            track.setPort(1);
            track.setChannel(i + 1);

            GPMIDIChannel channel = readTrack(track);
            piece.setChannels(i, channel);
        }

        //TODO: check what this information could be.
        // Dump infos
        skip(1); // assertBytesEqualTo(1, 1);

        // Status byte? Generally equals to 48.
        // If it equals 49 (bit 0 set), one track is solo.
        int b = readByte();

        // n often equals 45119 or 382679111
        int n = readInt();

        // The number of the solo track.
        int soloTrackNumber = readInt();

        // Reads all measures
        List<GPMeasure> measures = piece.getMeasures();
        List<GPMeasureTrackPair> measuresTracksPairs = piece.getMeasuresTracksPairs();
        for (int i = 0; i < numberOfMeasures; i++) {
            GPMeasure measure = new GPMeasure();
            measure.setNumber(i + 1);
            measures.add(measure);

            readMeasureTrackPairs(tracks, measure, measuresTracksPairs);
        }
        
        // For fun
        int i = 0, numBytes = 1;
        boolean different = false;
        b = read();
        while (b != -1) {
            i++;
            if (!different && i > 4) {
                different = true;
            }
            b = read();
            numBytes++;
        }

        if (different) {
            System.err
                    .println("GP2InputStream: This GTP file has something special (" + numBytes + " additional bytes)...");
        }

        return piece;
    }

    /**
     * Returns a beat read from a GP2 file.
     * 
     * @param trackNumberOfStrings
     *            the number of strings of the track we are currently reading.
     * @return a GPBeat read from a GP2 file.
     * @throws IOException
     */
    private GPBeat readBeat(int trackNumberOfStrings) throws IOException {
        GP2EffectsOnBeat gp2effects = new GP2EffectsOnBeat();
        GPBeat beat = new GPBeat();

        // An integer which is believed to correspond to
        // some duration or something.
        // The time for a whole is 1920. When notes are dotted,
        // the half of the normal time is added.
        int beatDuration = readInt();
        GPDuration duration = GPDuration.valueOf(readByte());
        beat.setDuration(duration);

        // Effects on the beat
        int effectBeat = readUnsignedByte();
        int specialEffects = readUnsignedByte();

        // Reads the mix table change
        if ((specialEffects & 0x02) != 0) {
            beat.mixTableChange = readMixTableChange();
        }

        // Reads the upstroke/downstroke effect
        if ((specialEffects & 0x01) != 0) {
            int strokeType = readUnsignedByte();

            // Duration
            int d = readUnsignedByte();
            //OLDd = (int) (Math.log((double) d) / Math.log(2.0));
            d = (int) (Math.log(d) / Math.log(2.0));
            duration = GPDuration.valueOf(d);

            beat.effects = new GPEffectsOnBeat();
            if (strokeType == 1) {
                beat.effects.upStroke = duration;
            } else if (strokeType == 2) {
                beat.effects.downStroke = duration;
            }
        }

        // Some effects imply that the beat has no content, so we MUST NOT read
        // anything after the effects, and return immediatly.
        boolean returnNow = false;

        // Reads the effects on the beat
        if (effectBeat != 0) {
            // This beat is linked with the previous one
            if ((effectBeat & 0x40) != 0) {
                List<GPNote> notes = beat.getNotes();
                GPNote note = new GPNote();
                note.setTieNote(true);
                notes.add(note);
                returnNow = true;
            }

            // Triplet
            if ((effectBeat & 0x20) != 0) {
                beat.setNTuplet(3);
                // The number of the note in the triplet...
                // What is it used for?
                int n = readUnsignedByte();
                n += 0;
            }

            // Dotted beat
            if ((effectBeat & 0x10) != 0) {
                beat.dottedNotes = true;
            }

            // Rest beat
            if ((effectBeat & 0x08) != 0) {
                beat.setRestBeat(true);
                returnNow = true;
            }

            // Effects on the beat
            if ((effectBeat & 0x04) != 0) {
                readEffectsOnBeat(gp2effects, beat.effects);
            }

            // Presence of a chord diagram
            if ((effectBeat & 0x02) != 0) {
                beat.chordDiagram = readChordDiagram();
            }

            // Presence of a text
            if ((effectBeat & 0x01) != 0) {
                beat.text = readStringByte(0);
            }
        }

        // If the flag has been set, we return immediatly
        if (returnNow) {
            return beat;
        }

        // Finds out which strings are played
        // i = 0 => lowest string
        int stringsPlayed = readUnsignedByte();

        // Gets the effects header
        int effectsHeader = readUnsignedByte();
        int graceNotePresent = readUnsignedByte();

        // Gets the corresponding notes
        List<GPNote> notes = beat.getNotes();
        int numberOfStrings = 0;
        for (int i = 5; i >= 0; i--) {
            GPNote note = null;
            if ((stringsPlayed & (1 << i)) != 0) {
                note = readNote();
                notes.add(note);

                // Effect set on this note
                if ((effectsHeader & (1 << i)) != 0) {
                    note.effects = readEffectsOnNote();
                }

                // Adds effects on note if
                if (gp2effects.hasEffects()) {
                    if (note.effects == null) {
                        note.effects = new GPEffectsOnNote();
                    }

                    note.effects.harmonic = gp2effects.harmonic;
                    note.effects.leftHandVibrato = gp2effects.vibrato;
                    note.effects.wideVibrato = gp2effects.wideVibrato;
                }

                numberOfStrings++;
                beat.setString(i + (7 - trackNumberOfStrings), true);
            }

            // Grace note
            if ((graceNotePresent & (1 << i)) != 0) {
                if (note == null) {
                    note = new GPNote();
                    notes.add(note);
                }
                if (note.effects == null) {
                    note.effects = new GPEffectsOnNote();
                }
                note.effects.graceNote = readGraceNote();
            }
        }

        return beat;
    }

    /**
     * Reads a bend from the stream.
     * 
     * @return the Bend read.
     * @throws IOException
     */
    private GPBend readBend(GPBendType bendType) throws IOException {
        // We have to construct the bend for it to be usable
        // by the software as the bends in GP4 are far more complicated...
        GPBend bend = new GPBend();

        // The only data we have is the bend value
        int value = (int) (readDouble() * 100.0);
        bend.setType(bendType);
        bend.setValue(value);

        // Sets the points
        List<GPBendPoint> points = bend.getPoints();
        GPBendPoint bendPoint = new GPBendPoint();
        bendPoint.setPosition(0);
        bendPoint.setValue(0);
        bendPoint.setVibrato(GPVibrato.NONE);
        points.add(bendPoint);

        bendPoint = new GPBendPoint();
        bendPoint.setPosition(30);
        bendPoint.setValue(-value);
        bendPoint.setVibrato(GPVibrato.NONE);
        points.add(bendPoint);

        bendPoint = new GPBendPoint();
        bendPoint.setPosition(60);
        bendPoint.setValue(0);
        bendPoint.setVibrato(GPVibrato.NONE);
        points.add(bendPoint);

        return bend;
    }

    /**
     * Reads a ChordDiagram from the stream.
     * 
     * @return the ChordDiagram read.
     * @throws IOException
     * @throws GPFormatException
     */
    private GPChordDiagram readChordDiagram() throws IOException {
        GPChordDiagram cd = new GPChordDiagram();

        // Chord name
        cd.setName(readStringByte(0));

        // Data
        cd.setSharp(readBoolean());

        // Root of the chord
        int root = readInt();
        
        // If the root is correct, there are some additional information
        if ((root <= 11) && (root >= 0)) {
            cd.setRoot(GPChordNote.valueOf(root));
            cd.setChordType(GPChordType.valueOf(readInt()));

            cd.setNineElevenThirteen(readInt());
            cd.setBass(GPChordNote.valueOf(readInt()));

            cd.setTonalityType(GPTonalityType.valueOf(readInt()));
            cd.setTonalityFive(GPTonalityType.valueOf(readInt()));
            cd.setTonalityNine(GPTonalityType.valueOf(readInt()));
            cd.setTonalityEleven(GPTonalityType.valueOf(readInt()));

            // TODO: add a omission field
            // The ticked boxes: the "omission" field!
            readUnsignedByte();

            // 3 NULL bytes
            skip(3);
        }  else {
            // Otherwise, the chord type is "user"
            cd.setRoot(GPChordNote.USER);
        }
        
        // Base
        int base = readInt();
        cd.setBaseFret(base);
        
        // If the base is valid, read the frets position
        if (base != 0) {
            for (int i = 0; i < 6; i++) {
                cd.setFret(i + 1, readInt());
            }
        }

        return cd;
    }

    /**
     * Reads effects on the current beat from the stream.
     * 
     * @return the GPEffectsOnBeat read.
     * @throws IOException
     */
    private void readEffectsOnBeat(GP2EffectsOnBeat gp2effects,
            GPEffectsOnBeat eob) throws IOException {
        // Creates a new EffectsOnBeat if it does not exist yet
        if (eob == null) {
            eob = new GPEffectsOnBeat();
        }

        // Reads the header
        int header = readUnsignedByte();

        switch (header) {
        case 1:
            // Vibrato
            gp2effects.vibrato = true;
            break;

        case 2:
            // Wide vibrato
            gp2effects.wideVibrato = true;
            break;

        case 3:
            // Stroke of the Vibrato (GP2)
            // Dive and return with whammy bar (GP3)
            // Tremolo bar (GP4) = Dip Bend
            // We have to construct it...
            eob.tremoloBar = readBend(GPBendType.DIP);
            break;

        case 4:
            eob.fadeIn = true;
            break;

        case 5:
            eob.tapping = true;
            break;

        case 6:
            eob.slapping = true;
            break;

        case 7:
            eob.popping = true;
            break;

        case 8:
            gp2effects.harmonic = GPHarmonic.NATURAL;
            break;

        case 9:
            gp2effects.harmonic = GPHarmonic.ARTIFICIAL_12;
            break;

        default:
            //TODO assert(false);
        }
    }

    /**
     * Reads effects on the current note from the stream.
     * 
     * @return the GPEffectsOnNote read.
     * @throws IOException
     */
    private GPEffectsOnNote readEffectsOnNote() throws IOException {
        GPEffectsOnNote eon = new GPEffectsOnNote();

        // Effect
        int effect = readUnsignedByte();

        switch (effect) {
        case 1:
            // Hammer-on present
            eon.hammerOnPullOff = true;
            break;

        case 2:
            // Pull off present
            eon.hammerOnPullOff = true;
            break;

        case 3:
            // Slide up
            eon.slide = GPSlide.SHIFT_SLIDE;
            break;

        case 4:
            // Slide down
            eon.slide = GPSlide.OUT_DOWNWARD;
            break;

        case 5: {
            // Bend
            eon.bend = readBend(GPBendType.BEND);
            break;
        }

        case 6: {
            // Bend and release
            eon.bend = readBend(GPBendType.BEND_RELEASE);
            break;
        }

        case 7: {
            // Let Ring
            eon.letRing = true;
            break;
        }

        default:
//          TODO assert(false);
        }

        return eon;
    }

    /**
     * The grace notes are stored in the file with 4 variables, written in the
     * followingorder. Fret: byte The fret number the grace note is made from.
     * 
     * GPDynamic: byte The grace note dynamic is coded like this: - 1: ppp - 2:
     * pp - 3: p - 4: mp - 5: mf - 6: f - 7: ff - 8: fff The default value is 6.
     * 
     * Transition: byte this variable determines the transition type used to
     * make the grace note: - 0: None - 1: GPSlide - 2: GPBend - 3: Hammer
     * 
     * GPDuration: byte Determines the grace note duration, coded this way: - 1:
     * Sixteenth note. - 2: Twenty-fourth note. - 3: Thirty-second note.
     * 
     * @return the GPGraceNote read.
     * @throws IOException
     */
    private GPGraceNote readGraceNote() throws IOException {
        GPGraceNote gn = new GPGraceNote();
        byte b[] = new byte[3];

        read(b);

        gn.setFret(b[0]);
        gn.setDynamic(GPDynamic.valueOf(b[1]));
        gn.setDuration(GPDuration.valueOf(5 - b[2]));
        return gn;
    }

    /**
     * Reads a pair of "measure-track".
     * 
     * @param tracks
     *            a list containing the information about the tracks.
     * @param measure
     *            the GPMeasure that will hold the data read from the file.
     * @param mtpList
     *            the list of GPMeasureTrackPair.
     * @return the GPMeasureTrackPair read.
     * @throws IOException
     */
    private void readMeasureTrackPairs(List<GPTrack> tracks, GPMeasure measure,
            List<GPMeasureTrackPair> mtpList) throws IOException {
        // Time signature
        int numerator = readUnsignedByte();
        measure.setNumerator(numerator);

        int denominator = readUnsignedByte();
        measure.setDenominator(denominator);

        // 6 NULL bytes
        skip(6);

        // The f fraction is a number that either is not defined
        // (when d = 0), or otherwise it SHOULD be the same as
        // the measure signature (like 4/4 = 1.0, 13/8 = 1.625).
        int n;
        int d;
        // Number of notes in this measure, for each track
        int[] numberOfBeats = new int[8];
        for (int i = 0; i < 8; i++) {
            n = readUnsignedByte();
            d = readUnsignedByte();
            // The number of beats for this track
            numberOfBeats[i] = readUnsignedByte();

            // 9 NULL bytes
            skip(9);
        }

        // 2 bytes
        n = readUnsignedByte();
        d = readUnsignedByte();
        // Decode the measure header
        int measureHeader = readUnsignedByte();
        if ((measureHeader & 0x01) != 0) {
            measure.repeatStart = true;
        }

        if ((measureHeader & 0x02) != 0) {
            int numberOfRepetitions = readUnsignedByte();
            measure.setNumberOfRepetitions(numberOfRepetitions);
        }

        if ((measureHeader & 0x04) != 0) {
            int numberOfAlternateEndings = readUnsignedByte();
            measure.setNumberOfAlternateEnding(numberOfAlternateEndings);
        }

        // Reads the note in the different tracks
        for (int i = 0; i < 8; i++) {
            GPTrack track = (GPTrack) tracks.get(i);

            GPMeasureTrackPair mtp = new GPMeasureTrackPair();
            mtpList.add(mtp);
            List<GPBeat> beats = mtp.getBeats();
            for (int j = 0; j < numberOfBeats[i]; j++) {

                // Add the beat to the beat list
                GPBeat beat = readBeat(track.getNumberOfStrings());
                beats.add(beat);
            }
        }
    }

    /**
     * Reads a mix table change event from the stream.
     * 
     * @return The MixTableChange read.
     * @throws IOException
     */
    private GPMixTableChange readMixTableChange() throws IOException {
        GPMixTableChange mtc = new GPMixTableChange();
        int changes = readUnsignedByte();

        // Tempo
        if ((changes & 0x20) != 0) {
            mtc.tempo = new GPMixTableElement();
            mtc.tempo.setNewValue(readInt());
            mtc.tempo.setChangeDuration(readUnsignedByte());
        }

        // Reverb
        if ((changes & 0x10) != 0) {
            mtc.reverb = new GPMixTableElement();
            mtc.reverb.setNewValue(readUnsignedByte());
            mtc.reverb.setChangeDuration(readUnsignedByte());
        }

        // Chorus
        if ((changes & 0x08) != 0) {
            mtc.chorus = new GPMixTableElement();
            mtc.chorus.setNewValue(readUnsignedByte());
            mtc.chorus.setChangeDuration(readUnsignedByte());
        }

        // Balance
        if ((changes & 0x04) != 0) {
            mtc.balance = new GPMixTableElement();
            mtc.balance.setNewValue(readUnsignedByte());
            mtc.balance.setChangeDuration(readUnsignedByte());
        }

        // Volume
        if ((changes & 0x02) != 0) {
            mtc.volume = new GPMixTableElement();
            mtc.volume.setNewValue(readUnsignedByte());
            mtc.volume.setChangeDuration(readUnsignedByte());
        }

        // Instrument
        if ((changes & 0x01) != 0) {
            mtc.instrument = new GPMixTableElement();
            mtc.instrument.setNewValue(readUnsignedByte());
        }

        return mtc;
    }

    /**
     * Reads a note (which here means only the fret and the dynamic).
     * 
     * @return the GPNote read.
     * @throws IOException
     */
    private GPNote readNote() throws IOException {
        GPNote note = new GPNote();

        int fret = readUnsignedByte();
        if (fret == 100) {
            note.setDeadNote(true);
        } else {
            note.setFretNumber(fret);
        }

        GPDynamic dynamic = GPDynamic.valueOf(readUnsignedByte());
        note.setDynamic(dynamic);

        return note;
    }

    /**
     * Reads a track from the stream. The GPTrack is given as a parameter rather
     * than being created and returned by the function, as some of the data
     * contained in the GPTrack have already been read earlier. Additionally, it
     * creates a GPMIDIChannel to be used by this track.
     * 
     * @param track
     *            a GPTrack where the data will be written
     * @return the MIDI channel used by this track.
     * @throws IOException
     */
    private GPMIDIChannel readTrack(GPTrack track) throws IOException {
        GPMIDIChannel channel = new GPMIDIChannel();
        channel.setInstrument(readInt());

        // Number of frets
        track.setNumberOfFrets(readInt());

        // Name
        track.setName(readStringByteMaxLengthPlusOne());

        // Solo
        // Funny how this information is already stored in
        // the file (see in readPiece).
        boolean isSolo = readBoolean();

        // Infos
        channel.setVolume(readInt());
        channel.setBalance(readInt());
        channel.setChorus(readInt());
        channel.setReverb(readInt());

        // Capo
        track.setCapo(readInt());

        return channel;
    }

    private static final List<String> _files = new ArrayList<String>();

    private static int _total = 0;

    /**
     * The main function.
     * 
     * @param args
     */
    public static void main(String[] args) {
        try {
            GPSong p = (GPSong) new GPInputStream(
                    "D:\\Partoches\\PopRock\\Vai Steve\\Hand On Heart.gtp")
                    .readObject();
             System.out.println(p);
        } catch (Exception e) {
            e.printStackTrace();
        }

        File dir = new File("D:\\Partoches");
        decodeGP2InDirectory(dir);

        float p = 1.0f - ((float) _files.size() / (float) _total);
        System.out.println(_total + " files read.");
        System.out.println("Ratio of files read correctly: " + p);
        Iterator<String> it = _files.iterator();
        while (it.hasNext()) {
            System.err.println(it.next());
        }
    }

    /**
     * Decode recursively all the GP2 files in the directory specified.
     * 
     * @param dir
     *            The directory to decode files from.
     */
    private static void decodeGP2InDirectory(File dir) {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                decodeGP2InDirectory(f);
            } else if (f.isFile()) {
                String name = f.getName();
                int index = name.lastIndexOf(".gtp");
                if (index == (name.length() - 4)) {
                    try {
                        GPInputStream gpIs = new GPInputStream(f);
                        GPSong p = (GPSong) gpIs.readObject();
                        _total++;
                         System.out.println(p);
                    } catch (AssertionError e) {
                        e.printStackTrace();
                        System.err.println(f.getAbsolutePath());
                    } catch (GPFormatException e) {
                        // System.out.println(e.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.err.println(f.getAbsolutePath());
                        _files.add(f.getAbsolutePath());
                        _total++;
                    }
                }
            }
        }
    }
}
