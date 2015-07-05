package dguitar.codecs.guitarPro.version3;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dguitar.codecs.guitarPro.GPBend;
import dguitar.codecs.guitarPro.GPBendPoint;
import dguitar.codecs.guitarPro.GPBendType;
import dguitar.codecs.guitarPro.GPChordDiagram;
import dguitar.codecs.guitarPro.GPChordNote;
import dguitar.codecs.guitarPro.GPChordType;
import dguitar.codecs.guitarPro.GPColor;
import dguitar.codecs.guitarPro.GPDuration;
import dguitar.codecs.guitarPro.GPDynamic;
import dguitar.codecs.guitarPro.GPEffectsOnBeat;
import dguitar.codecs.guitarPro.GPEffectsOnNote;
import dguitar.codecs.guitarPro.GPFingering;
import dguitar.codecs.guitarPro.GPGraceNote;
import dguitar.codecs.guitarPro.GPGraceNoteTransition;
import dguitar.codecs.guitarPro.GPKey;
import dguitar.codecs.guitarPro.GPMIDIChannel;
import dguitar.codecs.guitarPro.GPMarker;
import dguitar.codecs.guitarPro.GPMeasure;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;
import dguitar.codecs.guitarPro.GPMixTableChange;
import dguitar.codecs.guitarPro.GPMixTableElement;
import dguitar.codecs.guitarPro.GPNote;
import dguitar.codecs.guitarPro.GPSlide;
import dguitar.codecs.guitarPro.GPSong;
import dguitar.codecs.guitarPro.GPTonalityType;
import dguitar.codecs.guitarPro.GPTrack;
import dguitar.codecs.guitarPro.GPTrackLyrics;
import dguitar.codecs.guitarPro.GPVibrato;
import dguitar.codecs.guitarPro.GPHarmonic;
import dguitar.codecs.guitarPro.GPBeat;
import dguitar.codecs.guitarPro.GPInputStream;

/**
 * The class GP3InputStream allows to parse an InputStream containing a file
 * encoded with the Guitar Pro 3 format.
 * 
 * @author Matthieu Wipliez
 * @author Mauricio Gracia Gutierrez
 */
public class GP3InputStream extends GPInputStream {
    /**
     * The versions this parser supports.
     */
    private static final String supportedVersions[] = { "FICHIER GUITAR PRO v3.00" };

    /**
     * Creates a new GP3InputStream from an existing stream.
     * 
     * @param in
     *            the original stream
     */
    public GP3InputStream(InputStream in) {
        super(in);
    }

    /**
     * Creates a new GP4InputStream by cloning an existing GPInputStream.
     * 
     * @param gpIn
     *            the original GPInputStream.
     */
    public GP3InputStream(GPInputStream gpIn) {
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
     * @return "FICHIER GUITAR PRO v3.00"
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
     * Reads a GPSong from the stream.
     * 
     * @return the GPSong read.
     * @throws IOException
     */
    public GPSong readPiece() throws IOException {
        GP3Piece piece = new GP3Piece();
        String s;
        
        // Version
        if (__version.equals("")) {
            __version = readStringByte(30);
        }
        
        piece.setVersion(__version);

        // Title
        s = readStringIntegerPlusOne();
        piece.setTitle(s);

        // Subtitle
        s = readStringIntegerPlusOne();
        piece.setSubtitle(s);

        // Interpret
        s = readStringIntegerPlusOne();
        piece.setInterpret(s);

        // Album
        s = readStringIntegerPlusOne();
        piece.setAlbum(s);

        // Author of the song
        s = readStringIntegerPlusOne();
        piece.setAuthorSong(s);

        // Copyright
        s = readStringIntegerPlusOne();
        piece.setCopyright(s);

        // Author of the piece
        s = readStringIntegerPlusOne();
        piece.setAuthorPiece(s);

        // Instructions
        s = readStringIntegerPlusOne();
        piece.setInstruction(s);

        // Notes
        int nbNotes = readInt();
        String note = "";
        for (int i = 0; i < nbNotes; i++) {
            note += readStringIntegerPlusOne();
            note += "\n";
        }
        piece.setNote(note);

        // Triplet Feel
        piece.tripletFeel = readBoolean();

        // Lyrics
        // No Lyrics in GP3
        GPTrackLyrics lyrics = new GPTrackLyrics();
        piece.setLyrics(lyrics);

        // Tempo
        int tempo = readInt();
        piece.setTempo(tempo);

        // GPKey (usually C)
        GPKey k = GPKey.valueOf(readInt());
        piece.setKey(k);

        // No octave
        piece.setOctave(0);

        // MIDI Channels
        for (int i = 0; i < 64; i++) {
            GPMIDIChannel channel = readMIDIChannel();
            piece.setChannels(i, channel);
        }

        // Number of measures and tracks
        int numberOfMeasures = readInt();
        int numberOfTracks = readInt();

        // Reads measures
        List<GPMeasure> measures = piece.getMeasures();
        GPMeasure measure;
        if (numberOfMeasures > 0) {
            measure = readMeasure(null); // no previous
            measure.setNumber(1);
            measures.add(measure);
            for (int i = 1; i < numberOfMeasures; i++) {
                GPMeasure previous = (GPMeasure) measures.get(i - 1);
                measure = readMeasure(previous);
                measure.setNumber(i + 1);
                measures.add(measure);
            }
        }

        // Reads tracks
        List<GPTrack> tracks = piece.getTracks();
        for (int i = 0; i < numberOfTracks; i++) {
            tracks.add(readTrack());
        }

        // Reads measures-tracks pairs
        List<GPMeasureTrackPair> measuresTracksPairs = piece.getMeasuresTracksPairs();
        for (int i = 0; i < numberOfMeasures; i++) {
            for (int j = 0; j < numberOfTracks; j++) {
                GPMeasureTrackPair mtp = readMeasureTrackPair();
                measuresTracksPairs.add(mtp);
            }
        }

        // For fun
        int i = 0;
        int b;
        boolean different = false;
        b = read();
        while (b != -1) {
            i++;
            if (!different && i > 4) {
                different = true;
            }
            b = read();
        }

        if (different) {
            System.err
                    .println("GP3InputStream: This GP3 file has something special...");
        }

        return piece;
    }

    /**
     * Returns a beat read from a GP3 file.
     * 
     * @return a GPBeat read from a GP3 file.
     * @throws IOException
     */
    private GPBeat readBeat() throws IOException {
        GPBeat beat = new GPBeat();
        GP3EffectsOnBeat gp3effects = new GP3EffectsOnBeat();

        int header = readUnsignedByte();

        // Okay, I've had a problem with ONE file, and this made it work
        // The measure was incomplete, and I don't know if that's what the 0x80
        // flag is made for, but anyway, it works. (ugly I agree though)
        if ((header & 0x80) != 0) {
            System.err.println("Oops, header & 0x80 !");
            header &= 0x3f; // Disables the two last bits
        }

        // GPBeat status
        if ((header & 0x40) != 0) {
            int beatStatus = readUnsignedByte();
            beat.setEmptyBeat(beatStatus == 0x00);
            beat.setRestBeat(beatStatus == 0x02);
        }

        // Dotted notes
        beat.dottedNotes = ((header & 0x01) != 0);

        // GPBeat duration
        GPDuration duration = GPDuration.valueOf(readByte());
        beat.setDuration(duration);

        // N-Tuplet
        if ((header & 0x20) != 0) {
            int n = readInt();
            beat.setNTuplet(n);
        }

        // Chord diagram
        if ((header & 0x02) != 0) {
            beat.chordDiagram = readChordDiagram();
        }

        // Text
        if ((header & 0x04) != 0) {
            beat.text = readStringIntegerPlusOne();
        }

        // Effects on the beat
        if ((header & 0x08) != 0) {
            // Harmonics on a note cause this flag to be set
            // In GP3, a vibrato is set on the beat, not on notes...
            beat.effects = readEffectsOnBeat(gp3effects);
        }

        // Mix table change
        if ((header & 0x10) != 0) {
            beat.mixTableChange = readMixTableChange();
        }

        // Finds out which strings are played
        int stringsPlayed = readUnsignedByte();
        int numberOfStrings = 0;

        for (int i = 0; i < 7; i++) {
            if ((stringsPlayed & (1 << i)) != 0) {
                numberOfStrings++;
                beat.setString(i, true);
            }
        }

        // Gets the corresponding notes
        List<GPNote> notes = beat.getNotes();
        for (int i = 0; i < numberOfStrings; i++) {
            GPNote note = readNote();
            if (gp3effects.hasEffects()) {
                if (note.effects == null) {
                    note.effects = new GPEffectsOnNote();
                }

                note.effects.harmonic = gp3effects.harmonic;
                note.effects.leftHandVibrato = gp3effects.vibrato;
                note.effects.wideVibrato = gp3effects.wideVibrato;
            }
            notes.add(note);
        }

        return beat;
    }

    /**
     * Reads a bend from the stream.
     * 
     * @return the GPBend read.
     * @throws IOException
     */
    private GPBend readBend() throws IOException {
        GPBend bend = new GPBend();

        GPBendType type = GPBendType.valueOf(readByte());
        bend.setType(type);
        bend.setValue(readInt());

        // Reads the points
        int numPoints = readInt();
        List<GPBendPoint> points = bend.getPoints();
        for (int i = 0; i < numPoints; i++) {
            points.add(readBendPoint());
        }

        return bend;
    }

    /**
     * Reads a bend point from the stream.
     * 
     * @return the GPBendPoint read.
     * @throws IOException
     */
    private GPBendPoint readBendPoint() throws IOException {
        GPBendPoint bendPoint = new GPBendPoint();

        bendPoint.setPosition(readInt()); // Absolute time position
        bendPoint.setValue(readInt()); // Vertical height
        GPVibrato vibrato = GPVibrato.valueOf(readByte());
        bendPoint.setVibrato(vibrato);

        return bendPoint;
    }

    /**
     * Reads a chord diagram from the stream.
     * 
     * @return the GPChordDiagram read.
     * @throws IOException
     */
    private GPChordDiagram readChordDiagram() throws IOException {
        boolean debug = false;
        GPChordDiagram cd = new GPChordDiagram();
        int header = readUnsignedByte();

        // Simple chord
        if ((header & 0x01) == 0) {
            cd.setName(readStringIntegerPlusOne());
            int base = readInt();
            cd.setBaseFret(base);

            if (base != 0) {
                for (int i = 0; i < 6; i++) {
                    cd.setFret(i + 1, readInt());
                }
            }
        } else {
            cd.setSharp(readBoolean());

            // Skips 3 bytes
            skip(3);

            // GPRoot
            cd.setRoot(GPChordNote.valueOf(readInt()));

            // Chord type
            cd.setChordType(GPChordType.valueOf(readInt()));

            // 9, 11, 13
            cd.setNineElevenThirteen(readInt());

            // Bass
            cd.setBass(GPChordNote.valueOf(readInt()));

            // Augmented or diminished
            cd.setTonalityType(GPTonalityType.valueOf(readUnsignedByte()));

            // Skips 3 bytes
            skip(3);

            // Added note
            cd.setAddedNote(readUnsignedByte());

            // Name
            cd.setName(readStringByte(34));

            // Base fret
            cd.setBaseFret(readInt());

            // Reads the 6 frets
            for (int i = 0; i < 6; i++) {
                cd.setFret(i + 1, readInt());
            }

            // Reads 28 bytes
            for (int i = 0; i < 28; i++) {
                int n = readUnsignedByte();
                if (debug) {
                    System.out.print(n + " ");
                }
            }
            if (debug) {
                System.out.println();
            }

            // Omission : gives the notes present in the chord
            for (int i = 0; i < 7; i++) {
                readUnsignedByte();
            }
            // TODO: add a field in the Chord class

            int n = readUnsignedByte();
            if (debug) {
                System.out.println(n);
            }
        }

        return cd;
    }

    /**
     * Reads a color from the stream.
     * 
     * @return the GPColor read.
     * @throws IOException
     */
    private GPColor readColor() throws IOException {
        GPColor color = new GPColor();

        color.red = readUnsignedByte();
        color.green = readUnsignedByte();
        color.blue = readUnsignedByte();
        read(); // white byte, always 0x00

        return color;
    }

    /**
     * Reads effects on the current beat from the stream.
     * 
     * @return the GPEffectsOnBeat read.
     * @throws IOException
     */
    private GPEffectsOnBeat readEffectsOnBeat(GP3EffectsOnBeat gp3effects)
            throws IOException {
        GPEffectsOnBeat eob = new GPEffectsOnBeat();
        int header = readUnsignedByte();

        // Tapping/Slapping/Popping effect
        if ((header & 0x20) != 0) {
            int effect = readUnsignedByte();
            if (effect == 0) {
                // Tremolo bar (GP3) = Dip GPBend
                // We have to construct it...
                int value = readInt();
                GPBend tremoloBar = new GPBend();
                tremoloBar.setType(GPBendType.DIP);
                tremoloBar.setValue(value);

                // Sets the points
                List<GPBendPoint> points = tremoloBar.getPoints();
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

                // Sets the effect
                eob.tremoloBar = tremoloBar;
            } else {
                switch (effect) {
                case 1:
                    eob.tapping = true;
                    break;

                case 2:
                    eob.slapping = true;
                    break;

                case 3:
                    eob.popping = true;
                    break;

                default:
                    throw new IOException("Unexpected value in effect: "
                            + effect);
                }

                // TODO: dummy int ?
                int dummy = readInt();
                if (dummy != 0) {
                    System.err.println("dummy = " + dummy);
                }
            }
        }

        // Stroke effect
        if ((header & 0x40) != 0) {
            // Upstroke
            int durationValue = readByte();
            if (durationValue != 0) {
                // Converts value to a GPDuration value
                // The conversion is ugly, but conceptually the
                // stroke speed is very similar to a GPDuration
                // (well it is for me)
                // However, it is coded in a different way, so we need this...
                eob.upStroke = GPDuration.valueOf(6 - durationValue);
            }

            // Downstroke
            durationValue = readByte();
            if (durationValue != 0) {
                // Converts value to a GPDuration value
                eob.downStroke = GPDuration.valueOf(6 - durationValue);
            }
        }

        // GPVibrato
        gp3effects.vibrato = ((header & 0x01) != 0);

        // Whammy bar (GP3) / Wide vibrato (GP)
        if ((header & 0x02) != 0) {
            gp3effects.wideVibrato = true;
        }

        // Natural GPHarmonic
        if ((header & 0x04) != 0) {
            gp3effects.harmonic = GPHarmonic.NATURAL;
        }

        // Artificial GPHarmonic
        if ((header & 0x08) != 0) {
            gp3effects.harmonic = GPHarmonic.ARTIFICIAL_12;
        }

        // Fade in
        eob.fadeIn = ((header & 0x10) != 0);

        return eob;
    }

    /**
     * Reads effects on the current note from the stream.
     * 
     * @return the GPEffectsOnNote read.
     * @throws IOException
     */
    private GPEffectsOnNote readEffectsOnNote() throws IOException {
        int header;
        GPEffectsOnNote eon = new GPEffectsOnNote();

        header = readUnsignedByte();

        // GPBend present
        if ((header & 0x01) != 0) {
            eon.bend = readBend();
        }

        // Grace note present
        if ((header & 0x10) != 0) {
            eon.graceNote = readGraceNote();
        }

        // GPSlide from the current note present
        if ((header & 0x04) != 0) {
            eon.slide = GPSlide.SHIFT_SLIDE;
        }

        // Let-ring present
        eon.letRing = ((header & 0x08) != 0);

        // hammer-on or a pull-off from the current note present
        eon.hammerOnPullOff = ((header & 0x02) != 0);

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
        byte b[] = new byte[4];

        read(b);

        gn.setFret(b[0]);
        gn.setDynamic(GPDynamic.valueOf(b[1]));
        gn.setTransition(GPGraceNoteTransition.valueOf(b[2]));
        gn.setDuration(GPDuration.valueOf(5 - b[3]));
        return gn;
    }

    /**
     * Reads a marker from the stream.
     * 
     * @return the GPMarker read.
     * @throws IOException
     */
    private GPMarker readMarker() throws IOException {
        GPMarker marker = new GPMarker();

        marker.setName(readStringIntegerPlusOne());
        marker.setColor(readColor());

        return marker;
    }

    /**
     * Reads a measure from the stream. If previous is not null, this measure
     * will inherit the previous one's settings.
     * 
     * @param previous
     *            the previous measure.
     * @return the GPMeasure read.
     * @throws IOException
     */
    private GPMeasure readMeasure(GPMeasure previous) throws IOException {
        GPMeasure measure = new GPMeasure(previous);
        int header = readUnsignedByte();

        // Numerator
        if ((header & 0x01) != 0) {
            measure.setNumerator(readByte());
        }

        // Denominator
        if ((header & 0x02) != 0) {
            measure.setDenominator(readByte());
        }

        // Beginning of repeat
        measure.repeatStart = ((header & 0x04) != 0);

        // End of repeat
        if ((header & 0x08) != 0) {
            measure.setNumberOfRepetitions(readByte());
        }

        // Number of alternate endings
        if ((header & 0x10) != 0) {
            measure.setNumberOfAlternateEnding(readByte());
        }

        // GPMarker
        if ((header & 0x20) != 0) {
            measure.marker = readMarker();
        }

        // Tonality
        if ((header & 0x40) != 0) {
            int type = readByte();
            GPKey key = GPKey.valueOf(readByte(), type);
            measure.setTonality(key);
        }

        // Presence of a double bar
        if ((header & 0x80) != 0) {
            System.err.println("prout");
        }
        measure.hasDoubleBar = ((header & 0x80) != 0);

        return measure;
    }

    /**
     * Reads a pair of "measure-track".
     * 
     * @return the GPMeasureTrackPair read.
     * @throws IOException
     */
    private GPMeasureTrackPair readMeasureTrackPair() throws IOException {
        GPMeasureTrackPair mtp = new GPMeasureTrackPair();
        List<GPBeat> beats = mtp.getBeats();
        int numberOfBeats = readInt();

        for (int i = 0; i < numberOfBeats; i++) {
            GPBeat beat = readBeat();
            beats.add(beat);
        }

        return mtp;
    }

    /**
     * Reads a MIDI Channel from the stream.
     * 
     * @return the GPMIDIChannel read.
     * @throws IOException
     */
    private GPMIDIChannel readMIDIChannel() throws IOException {
        GPMIDIChannel channel = new GPMIDIChannel();

        channel.setInstrument(readInt());
        channel.setVolume(readByte());
        channel.setBalance(readByte());
        channel.setChorus(readByte());
        channel.setReverb(readByte());
        channel.setPhaser(readByte());
        channel.setTremolo(readByte());

        // Supposed because of backward compatibility
        skip(2);

        return channel;
    }

    /**
     * Reads a mix table change event from the stream.
     * 
     * @return the GPMixTableChange read.
     * @throws IOException
     */
    private GPMixTableChange readMixTableChange() throws IOException {
        int pos[] = new int[8]; // Array containing the elements that change
        int i; // Loop index (must be external to loops in this function)
        int n; // The index in the "pos" array
        int aux; // The value read

        GPMixTableChange mtc = new GPMixTableChange();

        // For easier processing, creates an array
        GPMixTableElement elements[] = new GPMixTableElement[8];

        for (i = 0; i < 8; i++) {
            elements[i] = new GPMixTableElement();
        }
        // Sets all the values
        mtc.instrument = elements[0];
        mtc.volume = elements[1];
        mtc.balance = elements[2];
        mtc.chorus = elements[3];
        mtc.reverb = elements[4];
        mtc.phaser = elements[5];
        mtc.tremolo = elements[6];
        mtc.tempo = elements[7];

        // Reads the new values (-1 if not set)
        // Adds the elements with new values to the "pos" array
        n = 0;
        for (i = 0; i < 7; i++) {
            aux = readByte();
            if ((i != 0) && (aux != -1)) {
                pos[n] = i;
                n++;
            }
            elements[i].setNewValue(aux);
        }

        // The tempo field is different (needs an integer)
        aux = readInt();
        if (aux != -1) {
            pos[n] = i;
            n++;
        }

        elements[7].setNewValue(aux);

        // Skip the instrument field, and read the change duration
        for (i = 0; i < n; i++) {
            aux = readByte();
            if (elements[pos[i]].getNewValue() != -1) {
                elements[pos[i]].setChangeDuration(aux);
            }
        }

        // There is nothing such as "apply to all tracks" in Guitar Pro 3
        for (i = 0; i < elements.length; i++) {
            elements[i].applyToAllTracks = false;
        }

        return mtc;
    }

    /**
     * Reads a note (with possibly some effects on it) from the stream.
     * 
     * @return the GPNote read.
     * @throws IOException
     */
    private GPNote readNote() throws IOException {
        GPNote note = new GPNote();

        int header = readUnsignedByte();

        // Note status
        note.isAccentuated = ((header & 0x40) != 0);
        note.isDotted = ((header & 0x02) != 0);
        note.setGhostNote((header & 0x04) != 0);

        // Note type
        if ((header & 0x20) != 0) {
            int noteType = readUnsignedByte();
            note.setTieNote(noteType == 0x02);
            note.setDeadNote(noteType == 0x03);
        }

        // Note duration
        if ((header & 0x01) != 0) {
            note.duration = GPDuration.valueOf(readByte());
            note.setNTuplet(readByte());
        }

        // Note dynamic
        if ((header & 0x10) != 0) {
            note.setDynamic(GPDynamic.valueOf(readByte()));
        }

        // Fret number
        if ((header & 0x20) != 0) {
            note.setFretNumber(readByte());
        }

        // GPFingering
        if ((header & 0x80) != 0) {
            note.fingeringLeftHand = GPFingering.valueOf(readByte());
            note.fingeringRightHand = GPFingering.valueOf(readByte());
        }

        // Effects on the note
        if ((header & 0x08) != 0) {
            note.effects = readEffectsOnNote();
        }

        return note;
    }

    /**
     * Reads a track from the stream.
     * 
     * @return the GPTrack read.
     * @throws IOException
     */
    private GPTrack readTrack() throws IOException {
        GPTrack track = new GPTrack();

        int header = readUnsignedByte();

        // Flags
        track.isDrumsTrack = ((header & 0x01) != 0);
        track.is12StringedGuitarTrack = ((header & 0x02) != 0);
        track.isBanjoTrack = ((header & 0x04) != 0);

        // Name
        track.setName(readStringByte(40));

        // Number of strings
        track.setNumberOfStrings(readInt());

        // Tuning of the strings
        for (int i = 0; i < 7; i++) {
            track.setStringsTuning(i, readInt());
        }

        // Port
        track.setPort(readInt());

        // Channel
        track.setChannel(readInt());

        // Channel Effects
        track.setChannelEffects(readInt());

        // Number of frets
        track.setNumberOfFrets(readInt());

        // Height of the capo
        track.setCapo(readInt());

        // Track's color
        track.setColor(readColor());

        return track;
    }
}
