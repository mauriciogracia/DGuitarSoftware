package dguitar.codecs.guitarPro;

/**
 * This class describes a MIDI channel.
 * 
 * @author Matthieu Wipliez
 */
public class GPMIDIChannel {
    /**
     * Balance.
     */
    private int _balance;

    /**
     * Chorus.
     */
    private int _chorus;

    /**
     * Instrument.
     */
    private int _instrument;

    /**
     * Phaser.
     */
    private int _phaser;

    /**
     * Reverb.
     */
    private int _reverb;

    /**
     * Tremolo.
     */
    private int _tremolo;

    /**
     * Volume.
     */
    private int _volume;

    /**
     * Creates a new MIDIChannel
     * 
     */
    public GPMIDIChannel() {
        _balance = 0;
        _chorus = 0;
        _instrument = 0;
        _phaser = 0;
        _reverb = 0;
        _tremolo = 0;
        _volume = 0;
    }

    /**
     * Returns this MIDI channel's balance.
     * 
     * @return this MIDI channel's balance.
     */
    public int getBalance() {
        return _balance;
    }

    /**
     * Returns this MIDI channel's chorus.
     * 
     * @return this MIDI channel's balance.
     */
    public int getChorus() {
        return _chorus;
    }

    /**
     * Returns this MIDI channel's instrument.
     * 
     * @return this MIDI channel's instrument.
     */
    public int getInstrument() {
        return _instrument;
    }

    /**
     * Returns this MIDI channel's phaser.
     * 
     * @return this MIDI channel's phaser.
     */
    public int getPhaser() {
        return _phaser;
    }

    /**
     * Returns this MIDI channel's reverb.
     * 
     * @return this MIDI channel's reverb.
     */
    public int getReverb() {
        return _reverb;
    }

    /**
     * Returns this MIDI channel's tremolo.
     * 
     * @return this MIDI channel's tremolo.
     */
    public int getTremolo() {
        return _tremolo;
    }

    /**
     * Returns this MIDI channel's volume.
     * 
     * @return this MIDI channel's volume.
     */
    public int getVolume() {
        return _volume;
    }

    /**
     * Sets this MIDI channel's balance.
     * 
     * @param balance
     *            the balance to set.
     */
    public void setBalance(int balance) {
        _balance = balance;
    }

    /**
     * Sets this MIDI channel's chorus.
     * 
     * @param chorus
     *            the chorus to set.
     */
    public void setChorus(int chorus) {
        _chorus = chorus;
    }

    /**
     * Sets this MIDI channel's instrument.
     * 
     * @param instrument
     *            the instrument to set.
     */
    public void setInstrument(int instrument) {
        _instrument = instrument;
    }

    /**
     * Sets this MIDI channel's phaser.
     * 
     * @param phaser
     *            the phaser to set.
     */
    public void setPhaser(int phaser) {
        _phaser = phaser;
    }

    /**
     * Sets this MIDI channel's reverb.
     * 
     * @param reverb
     *            the reverb to set.
     */
    public void setReverb(int reverb) {
        _reverb = reverb;
    }

    /**
     * Sets this MIDI channel's tremolo.
     * 
     * @param tremolo
     *            the tremolo to set.
     */
    public void setTremolo(int tremolo) {
        _tremolo = tremolo;
    }

    /**
     * Sets this MIDI channel's volume.
     * 
     * @param volume
     *            the volume to set.
     */
    public void setVolume(int volume) {
        _volume = volume;
    }

    /**
     * Returns a string representation of this MIDIChannel.
     */
    public String toString() {
        String res = "";

        res += "Instrument: " + _instrument;
        res += ",Volume: " + _volume;
        res += ",Balance: " + _balance;
        res += ",Chorus: " + _chorus;
        res += ",Reverb: " + _reverb;
        res += ",Phaser: " + _phaser;
        res += ",Tremolo: " + _tremolo;
        res += "\n";
        return res;
    }
}