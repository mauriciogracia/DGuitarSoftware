/*
 * Created on Mar 17, 2005
 */
package dguitar.adaptors.song.impl;

import dguitar.adaptors.song.SongDevice;

/**
 * The MIDI implementation of a SongDevice.
 * @author crnash
 */
public class SongDeviceImpl implements SongDevice
{
    int port;
    int channel;
    
    /**
     * @param port
     * @param channel
     */
    public SongDeviceImpl(int port, int channel)
    {
        this.port = port;
        this.channel = channel;
    }

    /**
     * @return an integer thar represent the channel
     */
    public int getChannel()
    {
        return channel;
    }
}
