/*
 * Created on Mar 2, 2005
 */
package dguitar.adaptors.song;


/**
 * A SongTrack defines the initialization events of a track (such as
 * program change, controller settings)
 * @author crnash
 */
public interface SongTrack
{
    SongDevice getPrimaryDevice();
    void setPrimaryDevice(SongDevice device);
    SongDevice getSecondaryDevice();
    void setSecondaryDevice(SongDevice device);

    /**
     * @param instrument
     */
    void setProgram(int instrument);
    int getProgram();

    /**
     * @param volume
     */
    void setVolume(int volume);
    int getVolume();

    /**
     * @param balance
     */
    void setPan(int balance);
    int getPan();

    /**
     * @param chorus
     */
    void setChorus(int chorus);
    int getChorus();

    /**
     * @param reverb
     */
    void setReverb(int reverb);
    int getReverb();

    /**
     * @param tremolo
     */
    void setTremolo(int tremolo);
    int getTremolo();

    /**
     * @param phaser
     */
    void setPhaser(int phaser);
    int getPhaser();
    
    void setBendSensitivity(int bendSensitivity);
    int getBendSensitivity();
    
    /**
     * @return the virtual track count
     */
    int getVirtualTrackCount();
    /**
     * @return the index of the Track
     */
    int getIndex();

}
