package dguitar.codecs.guitarPro;

/**
 * This class describes a track.
 * 
 * @author Matthieu Wipliez
 */
public class GPTrack {
    /**
     * Whether this track is a drums track.
     */
    public boolean isDrumsTrack;

    /**
     * Whether this track is a 12-stringed guitar track.
     */
    public boolean is12StringedGuitarTrack;

    /**
     * Whether this track is a banjo track.
     */
    public boolean isBanjoTrack;

    /**
     * The capo number (if present).
     */
    private int _capo;

    /**
     * The MIDI Channel.
     */
    private int _channel;

    /**
     * The MIDI Channel used for effects.
     */
    private int _channelEffects;

    /**
     * The track's color.
     */
    private GPColor _color;

    /**
     * The track's name
     */
    private String _name;

    /**
     * The number of frets of the track's instrument.
     */
    private int _numberOfFrets;

    /**
     * The number of strings in this track.
     */
    private int _numberOfStrings;

    /**
     * The MIDI port.
     */
    private int _port;

    /**
     * The track's instrument tuning.
     */
    private int[] _stringsTuning;

    /**
     * Creates a new Track.
     * 
     */
    public GPTrack() {
        _capo = 0;
        _channel = 0;
        _channelEffects = 0;
        _color = new GPColor();
        is12StringedGuitarTrack = false;
        isBanjoTrack = false;
        isDrumsTrack = false;
        _name = "";
        _numberOfFrets = 0;
        _numberOfStrings = 6;
        _port = 0;
        _stringsTuning = new int[7];
    }

    /**
     * Returns this track's capo.
     * 
     * @return this track's capo.
     */
    public int getCapo() {
        return _capo;
    }

    /**
     * Returns this track's channel.
     * 
     * @return this track's channel.
     */
    public int getChannel() {
        return _channel;
    }

    /**
     * Returns this track's channel for effects.
     * 
     * @return this track's channel for effects.
     */
    public int getChannelEffects() {
        return _channelEffects;
    }

    /**
     * Returns this track's color.
     * 
     * @return this track's color.
     */
    public GPColor getColor() {
        return _color;
    }

    /**
     * Returns this track's name.
     * 
     * @return this track's name.
     */
    public String getName() {
        return _name;
    }

    /**
     * Returns this track's number of frets.
     * 
     * @return this track's number of frets.
     */
    public int getNumberOfFrets() {
        return _numberOfFrets;
    }

    /**
     * Returns this track's number of strings.
     * 
     * @return this track's number of strings.
     */
    public int getNumberOfStrings() {
        return _numberOfStrings;
    }

    /**
     * Returns this track's port.
     * 
     * @return this track's port.
     */
    public int getPort() {
        return _port;
    }

    /**
     * Returns this track's string tuning.
     * 
     * @param i
     *            the string number.
     * @return this track's string tuning.
     */
    public int getStringsTuning(int i) {
        return _stringsTuning[i];
    }

    /**
     * Sets this track's capo.
     * 
     * @param capo
     *            the capo to set.
     */
    public void setCapo(int capo) {
        _capo = capo;
    }

    /**
     * Sets this track's channel.
     * 
     * @param channel
     *            the channel to set.
     */
    public void setChannel(int channel) {
        _channel = channel;
    }

    /**
     * Sets this track's channel for effects.
     * 
     * @param channelEffects
     *            the channel for effects to set.
     */
    public void setChannelEffects(int channelEffects) {
        _channelEffects = channelEffects;
    }

    /**
     * Sets this track's color.
     * 
     * @param color
     *            the color to set.
     */
    public void setColor(GPColor color) {
        _color = color;
    }

    /**
     * Sets this track's name.
     * 
     * @param name
     *            the name to set.
     */
    public void setName(String name) {
        _name = name;
    }

    /**
     * Sets this track's number of frets.
     * 
     * @param numberOfFrets
     *            the number of frets to set.
     */
    public void setNumberOfFrets(int numberOfFrets) {
        _numberOfFrets = numberOfFrets;
    }

    /**
     * Sets this track's number of strings.
     * 
     * @param numberOfStrings
     *            the number of strings to set.
     */
    public void setNumberOfStrings(int numberOfStrings) {
        _numberOfStrings = numberOfStrings;
    }

    /**
     * Sets this track's port.
     * 
     * @param port
     *            the port to set.
     */
    public void setPort(int port) {
        _port = port;
    }

    /**
     * Sets this track's string tuning.
     * 
     * @param i
     *            the string number.
     * @param stringsTuning
     *            the tuning of the string i to set.
     */
    public void setStringsTuning(int i, int stringsTuning) {
        _stringsTuning[i] = stringsTuning;
    }

    /**
     * Returns a string representation of this GPTrack.
     */
    public String toString() {
        String res;

        res = "";
        res += "Drums track? " + isDrumsTrack;
        res += "\n\t12-stringed guitar track? " + is12StringedGuitarTrack;
        res += "\n\tBanjo track? " + isBanjoTrack;
        res += "\n\tName: " + _name;
        res += "\n\tNumber of strings: " + _numberOfStrings;

        res += "\n\tTuning: ";
        for (int i = 0; i < _numberOfStrings; i++) {
            res += _stringsTuning[i] + " ";
        }

        res += "\n\tPort: " + _port;
        res += "\n\tChannel: " + _channel;
        res += "\n\tChannel effects: " + _channelEffects;
        res += "\n\tNumber of frets: " + _numberOfFrets;
        res += "\n\tHeight of the capo: " + _capo;
        res += "\n\tColor: " + _color;

        return res;
    }
}