package dguitar.codecs.guitarPro.version4;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import dguitar.codecs.guitarPro.GPBeat;
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
import dguitar.codecs.guitarPro.GPFormatException;
import dguitar.codecs.guitarPro.GPGraceNote;
import dguitar.codecs.guitarPro.GPGraceNoteTransition;
import dguitar.codecs.guitarPro.GPHarmonic;
import dguitar.codecs.guitarPro.GPKey;
import dguitar.codecs.guitarPro.GPMIDIChannel;
import dguitar.codecs.guitarPro.GPMarker;
import dguitar.codecs.guitarPro.GPMeasure;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;
import dguitar.codecs.guitarPro.GPMixTableChange;
import dguitar.codecs.guitarPro.GPMixTableElement;
import dguitar.codecs.guitarPro.GPNote;
import dguitar.codecs.guitarPro.GPPickStroke;
import dguitar.codecs.guitarPro.GPSlide;
import dguitar.codecs.guitarPro.GPSong;
import dguitar.codecs.guitarPro.GPTonalityType;
import dguitar.codecs.guitarPro.GPTrack;
import dguitar.codecs.guitarPro.GPTrackLyrics;
import dguitar.codecs.guitarPro.GPTrill;
import dguitar.codecs.guitarPro.GPVibrato;
import dguitar.codecs.guitarPro.GPInputStream;

/**
 * The class GP4InputStream allows to parse an InputStream containing a file
 * encoded with the Guitar Pro 4 format.
 * 
 * @author Mauricio Gracia Gutierrez
 * @author Matthieu Wipliez
 */
public class GP4InputStream extends GPInputStream {
    /**
     * The versions this parser supports.
     */
    private static final String supportedVersions[] = {
            "FICHIER GUITAR PRO v4.00", "FICHIER GUITAR PRO v4.06",
            "FICHIER GUITAR PRO L4.06" };

    /**
     * Creates a new GP4InputStream from an existing stream.
     * 
     * @param in
     *            the original stream.
     */
    public GP4InputStream(InputStream in) {
        super(in);
    }

    /**
     * Creates a new GP4InputStream by cloning an existing GPInputStream.
     * 
     * @param gpIn
     *            the original GPInputStream.
     */
    public GP4InputStream(GPInputStream gpIn) {
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
     * <p>
     * 
     * @return a string with the supported versions.
     *         <p>
     *         Currently this is "FICHIER GUITAR PRO v4.00\nFICHIER GUITAR PRO
     *         v4.06\nFICHIER GUITAR PRO L4.06"
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
     * Reads a Piece from the stream.
     * 
     * @return The Piece read.
     * @throws IOException
     */
    public GPSong readPiece() throws IOException, GPFormatException {
        GPSong piece = new GPSong();
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
        GPTrackLyrics lyrics = new GPTrackLyrics();
        lyrics.setTrackNumber(readInt());
        for (int i = 0; i < 5; i++) {
            lyrics.setMeasureNumber(i, readInt());
            lyrics.setLine(i, readStringInteger());
        }
        piece.setLyrics(lyrics);

        // Tempo
        int tempo = readInt();
        piece.setTempo(tempo);

        // Key
        GPKey key = GPKey.valueOf(readByte());
        piece.setKey(key);

        // Octave
        int octave = readInt();
        piece.setOctave(octave);

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

        return piece;
    }

    /**
     * Reads a Guitar Pro beat 
     * @return	the GPBeat that was read
     * @throws IOException
     */
    private GPBeat readBeat() throws IOException, GPFormatException {
        GPBeat beat = new GPBeat();

        int header = readUnsignedByte();

        // Beat status
        if ((header & 0x40) != 0) {
            int beatStatus = readUnsignedByte();
            beat.setEmptyBeat(beatStatus == 0x00);
            beat.setRestBeat(beatStatus == 0x02);
        }

        // Dotted notes
        beat.dottedNotes = ((header & 0x01) != 0);

        // Beat duration
        GPDuration duration = GPDuration.valueOf(readByte());
        beat.setDuration(duration);

        // N-Tuplet
        if ((header & 0x20) != 0) {
            int n = readInt();
            beat.setNTuplet(n);
        }

        // Chord diagram
        if ((header & 0x02) != 0) {
            GPChordDiagram CD = readChordDiagram();
            beat.chordDiagram = CD;
        }

        // Text
        if ((header & 0x04) != 0) {
            beat.text = readStringIntegerPlusOne();
            // throw new IOException("Texts not handled yet");
        }

        // Effects on the beat
        if ((header & 0x08) != 0) {
            // Harmonics on a note cause this flag to be set
            beat.effects = readEffectsOnBeat();
            // throw new IOException("Effects on the beat not handled yet");
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
            notes.add(note);
        }

        return beat;
    }

    /**
     * Reads a Guitar Pro color object 
     * @return the GPColor object read
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
     * Read a Guitar Pro marker
     * @return	the GPMarker that was read
     * @throws IOException
     */
    private GPMarker readMarker() throws IOException {
        GPMarker marker = new GPMarker();

        marker.setName(readStringIntegerPlusOne());
        marker.setColor(readColor());

        return marker;
    }

    /**
     * Reads a Guitar Pro measure
     * @param previous
     * @return the GP measure that was read
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

        // Marker
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
        measure.hasDoubleBar = ((header & 0x80) != 0);

        return measure;
    }

    /**
     * Reads a measure track pair
     * @return the GPMeasureTrackPair that was read
     * @throws IOException
     */
    private GPMeasureTrackPair readMeasureTrackPair() throws IOException,
            GPFormatException {
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
     * @return The MIDI Channel read.
     * @throws IOException
     */
    private GPMIDIChannel readMIDIChannel() throws IOException {
        GPMIDIChannel channel = new GPMIDIChannel();
        byte[] b = { 0, 0 };

        channel.setInstrument(readInt());
        channel.setVolume(readByte());
        channel.setBalance(readByte());
        channel.setChorus(readByte());
        channel.setReverb(readByte());
        channel.setPhaser(readByte());
        channel.setTremolo(readByte());

        // Backward compatibility with version 3.0
        read(b);

        return channel;
    }

    /**
     * Reads a Guitar Pro note
     * 
     * @return the GPNote object that was read
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
            try {
                // TODO: understand why we can have here a Duration whose value
                // is SIX
                // In GP, it seems to be some kind of special value (a triplet
                // appears...)
                note.duration = GPDuration.valueOf(readByte());
            } catch (Exception e) {
                e.printStackTrace();
            }
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

        // Fingering
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
     * Reads a Guitar Pro track
     * @return the track object that was read
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

    // Mauricio Gracia Gutiérrez modified from here below
    private GPChordType readChordType() throws IOException {
        return (GPChordType.valueOf(readUnsignedByte()));
    }

    private GPChordNote readRoot() throws IOException {
        return (GPChordNote.valueOf(readByte()));
    }

    private GPTonalityType readTonalityType(int numBytes) throws IOException {
        GPTonalityType TT;

        TT = null;
        if (numBytes == 1) {
            TT = GPTonalityType.valueOf(readUnsignedByte());
        } else if (numBytes == 4) {
            TT = GPTonalityType.valueOf(readInt());
        }
        return (TT);
    }

    private GPChordDiagram readChordDiagram() throws IOException,
            GPFormatException {
        GPChordDiagram CD;
        int header;
        int i;

        CD = new GPChordDiagram();

        header = readUnsignedByte();

        if ((header & 0x01) == 0) {
            // TODO: GP3 chord diagrams inside GP files !! Matthieu, please help
            // me with this part
            String cad;

            cad = "\nChord Diagrams are in GP3 format";
            cad += "\nWhich are not documented";
            cad += "\nWe are working on this";
            throw new GP4FormatException(cad);
        }
        // else {

        CD.setSharp(readBoolean());

        // ignore 3 bytes
        this.skip(3);

        CD.setRoot(readRoot());

        CD.setChordType(readChordType());

        CD.setNineElevenThirteen(readUnsignedByte());

        CD.setBass(GPChordNote.valueOf(readInt()));

        CD.setTonalityType(readTonalityType(4));

        CD.setAddedNote(readUnsignedByte());

        CD.setName(readStringByte(20));

        // ignore 2 bytes
        this.skip(2);

        CD.setTonalityFive(readTonalityType(1));

        CD.setTonalityNine(readTonalityType(1));

        CD.setTonalityEleven(readTonalityType(1));

        CD.setBaseFret(readInt());

        for (i = 1; i <= 7; i++) {
            CD.setFret(i, readInt());
        }

        CD.setNumBarres(readUnsignedByte());

        for (i = 1; i <= 5; i++) {
            CD.setFretOfBarre(i, readUnsignedByte());
        }
        for (i = 1; i <= 5; i++) {
            CD.setBarreStart(i, readUnsignedByte());
        }
        for (i = 1; i <= 5; i++) {
            CD.setBarreEnd(i, readUnsignedByte());
        }

        /*
         * SKIP THIS FIELDS Omission1, Omission3, Omission5, Omission7,
         * Omission9, Omission11, Omission13: Bytes Blank6: byte
         */
        this.skip(8);

        // Fingering: List of 7 Bytes
        for (i = 1; i <= 7; i++) {
            CD.setFingering(i, readByte());
        }
        /*
         * ShowDiagFingering: byte
         */
        CD.setchordFingeringDisplayed(readBoolean());

        return CD;
    }

    /**
     * The grace notes are stored in the file with 4 variables, written in the
     * followingorder. Fret: byte The fret number the grace note is made from.
     * 
     * Dynamic: byte The grace note dynamic is coded like this: - 1: ppp - 2: pp -
     * 3: p - 4: mp - 5: mf - 6: f - 7: ff - 8: fff The default value is 6.
     * 
     * Transition: byte this variable determines the transition type used to
     * make the grace note: - 0: None - 1: Slide - 2: Bend - 3: Hammer
     * 
     * Duration: byte Determines the grace note duration, coded this way: - 1:
     * Sixteenth note. - 2: Twenty-fourth note. - 3: Thirty-second note.
     */
    private GPGraceNote readGraceNote() throws IOException {
        GPGraceNote GN;
        int i;
        byte b[] = new byte[4];
        GN = new GPGraceNote();

        for (i = 0; i < 4; i++) {
            b[i] = (byte) readUnsignedByte();
        }

        GN.setFret(b[0]);
        GN.setDynamic(GPDynamic.valueOf(b[1]));
        GN.setTransition(GPGraceNoteTransition.valueOf(b[2]));
        /*
         * TODO update the SPECIFICATION No other values are accepted, these
         * number are related to the position of the note in the GraceNote
         * dialog (reversed to other uses of the Duration) - 1: Thirty-second
         * note. - 2: Sixteenth note. - 3: Eight note
         */
        GN.setDuration(GPDuration.valueOf(3 - b[3]));
        return (GN);
    }

    /**
     * Reads a bend point from the stream.
     * 
     * @return The BendPoint read.
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
     * Reads a bend from the stream.
     * 
     * @return The Bend read.
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
     * Reads effects on the current note from the stream.
     * 
     * @return The EffectsOnNote read.
     * @throws IOException
     */
    private GPEffectsOnNote readEffectsOnNote() throws IOException {
        int header1;
        int header2;
        GPEffectsOnNote EON;
        int b;

        EON = new GPEffectsOnNote();

        header1 = readUnsignedByte();
        header2 = readUnsignedByte();

        // Bend present
        if ((header1 & 0x01) != 0) {
            EON.bend = readBend();
            // throw new IOException("Bends not supported yet") ;
        }

        // Interpretation of the FIRST header
        // Grace note present
        if ((header1 & 0x10) != 0) {
            EON.graceNote = readGraceNote();
        }

        // Tremolo picking present
        if ((header2 & 0x04) != 0) {
            EON.tremoloPicking = GPDuration.valueOf(readUnsignedByte());
        }

        // Slide from the current note present
        if ((header2 & 0x08) != 0) {
            EON.slide = GPSlide.valueOf(readByte());
        }

        // Harmonic note present..this causes the Effects on Beat to be set
        if ((header2 & 0x10) != 0) {
            b = readByte();
            EON.harmonic = GPHarmonic.valueOf(b);
        }

        // Trill present
        if ((header2 & 0x20) != 0) {
            EON.trill = new GPTrill();
            EON.trill.setFret(readByte());
            EON.trill.setPeriod(GPDuration.valueOf(readByte()));
        }

        // Let-ring present
        if ((header1 & 0x08) != 0) {
            EON.letRing = true;
        }

        // hammer-on or a pull-off from the current note present
        if ((header1 & 0x02) != 0) {
            EON.hammerOnPullOff = true;
        }

        // Interpretation of the SECOND header
        // Left-hand vibrato present..this causes the Effects on Beat to be set
        if ((header2 & 0x40) != 0) {
            EON.leftHandVibrato = true;
        }

        // Palm Mute present
        if ((header2 & 0x02) != 0) {
            EON.palmMute = true;
        }
        // Note played staccato
        if ((header2 & 0x01) != 0) {
            EON.staccato = true;
        }

        return EON;
    }

    /**
     * Reads effects on the current beat from the stream.
     * 
     * @return The EffectsOnBeat read.
     * @throws IOException
     */
    private GPEffectsOnBeat readEffectsOnBeat() throws IOException {
        GPEffectsOnBeat eob = new GPEffectsOnBeat();
        int header[] = { 0, 0 };

        header[0] = readUnsignedByte();
        header[1] = readUnsignedByte();

        // Tapping/Slapping/Popping effect
        if ((header[0] & 0x20) != 0) {
            int effect = readUnsignedByte();
            switch (effect) {
            case 0:
                break; // no effect

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
                throw new IOException("Unexpected value in effect: " + effect);
            }
        }

        // Tremolo bar effect
        if ((header[1] & 0x04) != 0) {
            eob.tremoloBar = readBend();
        }

        // Stroke effect
        if ((header[0] & 0x40) != 0) {
            // Upstroke
            int durationValue = readByte();
            if (durationValue != 0) {
                // Converts value to a Duration value
                // The conversion is ugly, but conceptually the
                // stroke speed is very similar to a Duration
                // (well it is for me)
                // However, it is coded in a different way, so we need this...
                eob.upStroke = GPDuration.valueOf(6 - durationValue);
            }

            // Downstroke
            durationValue = readByte();
            if (durationValue != 0) {
                // Converts value to a Duration value
                eob.downStroke = GPDuration.valueOf(6 - durationValue);
            }
        }

        // Rasgueado
        if ((header[1] & 0x01) != 0) {
            eob.hasRasgueado = true;
        }

        // Pickstroke
        if ((header[1] & 0x02) != 0) {
            eob.pickStrokes = GPPickStroke.valueOf(readByte());
        }

        return eob;
    }

    /**
     * Reads a mix table change event from the stream.
     * 
     * @return The MixTableChange read.
     * @throws IOException
     */
    private GPMixTableChange readMixTableChange() throws IOException {
        int pos[] = new int[8];
        int i;
        int n;
        int aux;

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

        // Skip the instrument field

        for (i = 0; i < n; i++) {
            aux = readByte();
            if (elements[pos[i]].getNewValue() != -1) {
                elements[pos[i]].setChangeDuration(aux);
            }
        }

        int applyToAllTracks = readUnsignedByte();

        // The instrument and the tempo are not affected
        for (i = 0; i < 6; i++) {
            if ((applyToAllTracks & (1 << i)) != 0) {
                elements[i + 1].applyToAllTracks = true;
            }
        }
        // The tempo always applies to all the tracks.
        elements[7].applyToAllTracks = true;
        return mtc;
    }
}