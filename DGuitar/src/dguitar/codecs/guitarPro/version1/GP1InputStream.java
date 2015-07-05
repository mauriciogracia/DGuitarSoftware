package dguitar.codecs.guitarPro.version1;

import java.io.File;
import java.io.FileNotFoundException;
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
import dguitar.codecs.guitarPro.GPEffectsOnBeat;
import dguitar.codecs.guitarPro.GPEffectsOnNote;
import dguitar.codecs.guitarPro.GPFormatException;
import dguitar.codecs.guitarPro.GPHarmonic;
import dguitar.codecs.guitarPro.GPInputStream;
import dguitar.codecs.guitarPro.GPKey;
import dguitar.codecs.guitarPro.GPMIDIChannel;
import dguitar.codecs.guitarPro.GPMeasure;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;
import dguitar.codecs.guitarPro.GPNote;
import dguitar.codecs.guitarPro.GPSlide;
import dguitar.codecs.guitarPro.GPSong;
import dguitar.codecs.guitarPro.GPTonalityType;
import dguitar.codecs.guitarPro.GPTrack;
import dguitar.codecs.guitarPro.GPVibrato;

/**
 * The class GP1InputStream allows to parse an InputStream containing a GTP file
 * which is encoded in the 1, 1.01, 1.02, 1.03, 1.04 format.
 * 
 * @author Matthieu Wipliez
 */
public class GP1InputStream extends GPInputStream {
    /**
     * The versions this parser supports.
     */
    private static final String supportedVersions[] = {
            "FICHIER GUITARE PRO v1", "FICHIER GUITARE PRO v1.01",
            "FICHIER GUITARE PRO v1.02", "FICHIER GUITARE PRO v1.03",
            "FICHIER GUITARE PRO v1.04" };

    /**
     * The number of tracks. In versions from 1 to 1.02 included, it is 1; it is
     * 8 for versions 1.03 and 1.04.
     */
    private int _numTracks;

    /**
     * The version number read from the stream. Allows the parser to take
     * different paths according to the file version.
     */
    private int _versionNumber;

    /**
     * Creates a new GP2InputStream from an existing stream.
     */
    public GP1InputStream(InputStream in) {
        super(in);
    }

    /**
     * Creates a new GP4InputStream by cloning an existing GPInputStream.
     * 
     * @param gpIn
     *            the original GPInputStream.
     */
    public GP1InputStream(GPInputStream gpIn) {
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
     * @return "FICHIER GUITARE PRO v1" <br />
     *         "FICHIER GUITARE PRO v1.01" <br />
     *         "FICHIER GUITARE PRO v1.02" <br />
     *         "FICHIER GUITARE PRO v1.03" <br />
     *         "FICHIER GUITARE PRO v1.04"
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

        // Sets the version number, and attributes that depend of it.
        checkVersionNumber();

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
        if (_versionNumber > 2) {
            GPKey key = GPKey.valueOf(readInt());
            piece.setKey(key);
        } else {
            piece.setKey(GPKey.C);
        }

        // No octave
        piece.setOctave(0);

        // Reads tracks' tuning
        List<GPTrack> tracks = piece.getTracks();
        for (int i = 0; i < _numTracks; i++) {
            GPTrack track = new GPTrack();
            tracks.add(track);

            int numStrings;
            if (_versionNumber > 1) {
                numStrings = readInt();
            } else {
                numStrings = 6;
            }
            track.setNumberOfStrings(numStrings);

            for (int j = 0; j < numStrings; j++) {
                track.setStringsTuning(j, readInt());
            }
        }

        // Number of measures
        int numberOfMeasures = readInt();

        // Reads tracks' information
        for (int i = 0; i < _numTracks; i++) {
            GPTrack track = (GPTrack) tracks.get(i);
            track.setPort(1);
            track.setChannel(i + 1);

            GPMIDIChannel channel = readTrack(track);
            piece.setChannels(i, channel);
        }
        
        if (_versionNumber > 2) {
            // TODO: check what this information could be.
            // Dump infos
            skip(1); // assertBytesEqualTo(1, 1);
    
            // Status byte? Generally equals to 48.
            // If it equals 49 (bit 0 set), one track is solo.
            int b = readByte();
    
            // n often equals 45119 or 382679111
            int n = readInt();
    
            // The number of the solo track.
            int soloTrackNumber = readInt();
        }

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
        int b = read();
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
                    .println("GP1InputStream: This GTP file has something special ("
                            + numBytes + " additional bytes)...");
        }

        return piece;
    }

    /**
     * Sets the _versionNumber attribute, according to the version read from the
     * file, and sets attributes that are version-dependant.
     */
    private void checkVersionNumber() {
        int index = __version.indexOf(".");
        if (index < 0) {
            _versionNumber = 0;
        } else {
            String sub = __version.substring(index + 1, index + 3);
            _versionNumber = Integer.parseInt(sub);
        }

        if (_versionNumber > 2) {
            _numTracks = 8;
        } else {
            _numTracks = 1;
        }
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
        GP1EffectsOnBeat gp1effects = new GP1EffectsOnBeat();
        GPBeat beat = new GPBeat();

        // An integer which is believed to correspond to
        // some duration or something.
        // The time for a whole is 1920 (and a eight is 240). When notes are dotted,
        // the half of the normal time is added.
        int beatDuration = readInt();
        GPDuration duration = GPDuration.valueOf(readByte());
        beat.setDuration(duration);

        // Effects on the beat
        int effectBeat = readUnsignedByte();

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
                readEffectsOnBeat(gp1effects, beat.effects);
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

        // Gets the corresponding notes
        List<GPNote> notes = beat.getNotes();
        int numberOfStrings = 0;
        for (int i = 5; i >= 0; i--) {
            if ((stringsPlayed & (1 << i)) != 0) {
                GPNote note = readNote();
                notes.add(note);

                // Effect set on this note
                if ((effectsHeader & (1 << i)) != 0) {
                    note.effects = readEffectsOnNote();
                }

                // Adds effects on note if
                if (gp1effects.hasEffects()) {
                    if (note.effects == null) {
                        note.effects = new GPEffectsOnNote();
                    }

                    note.effects.harmonic = gp1effects.harmonic;
                    note.effects.leftHandVibrato = gp1effects.vibrato;
                    note.effects.wideVibrato = gp1effects.wideVibrato;
                }

                numberOfStrings++;
                beat.setString(i + (7 - trackNumberOfStrings), true);
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
        if (_versionNumber > 3) {
            // A "complete" chord diagram
            cd.setName(readStringByte(0));
    
            // Sharp
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
                int b = readUnsignedByte();
    
                // 3 NULL bytes
                skip(3);
            } else {
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
        } else {
            cd.setName(readStringByteMaxLengthPlusOne());
        }

        return cd;
    }

    /**
     * Reads effects on the current beat from the stream.
     * 
     * @return the GPEffectsOnBeat read.
     * @throws IOException
     */
    private void readEffectsOnBeat(GP1EffectsOnBeat gp1effects,
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
            gp1effects.vibrato = true;
            break;

        case 2:
            // Wide vibrato
            gp1effects.wideVibrato = true;
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
            gp1effects.harmonic = GPHarmonic.NATURAL;
            break;

        case 9:
            gp1effects.harmonic = GPHarmonic.ARTIFICIAL_12;
            break;

        default:
            //TODO
            //assert (false);
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
            //TODO
            //assert (false);
        }

        return eon;
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
        int[] numberOfBeats = new int[_numTracks];
        for (int i = 0; i < _numTracks; i++) {
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
        for (int i = 0; i < _numTracks; i++) {
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

        if (_versionNumber > 2) {
            // Number of frets
            track.setNumberOfFrets(readInt());
            
            // Name
            track.setName(readStringByteMaxLengthPlusOne());
            
            // Solo
            boolean isSolo = readBoolean();

            // Infos
            channel.setVolume(readInt());
            channel.setBalance(readInt());
            channel.setChorus(readInt());
            channel.setReverb(readInt());

            // Capo
            track.setCapo(readInt());
        } else {
            track.setNumberOfFrets(24);
            track.setName("Track 1");

            // Infos
            channel.setVolume(127);
            channel.setBalance(89);

            // Capo
            track.setCapo(0);
        }

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
        /*
         * try { GPSong p = (GPSong) new GPInputStream( "D:\\01.gtp")
         * .readObject(); // System.out.println(p); } catch (Exception e) {
         * e.printStackTrace(); }
         */

        File dir = new File("D:\\Partoches");
        try {
            decodeGP2InDirectory(dir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

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
    private static void decodeGP2InDirectory(File dir) throws FileNotFoundException {
        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                decodeGP2InDirectory(f);
            } else if (f.isFile()) {
                String name = f.getName();
                int index = name.lastIndexOf(".gtp");
                if (index == (name.length() - 4)) {
                    GPInputStream gpIs = new GPInputStream(f);
                    try {    
                        GPSong p = (GPSong) gpIs.readObject();
                        _total++;
                        // System.out.println(p);
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
