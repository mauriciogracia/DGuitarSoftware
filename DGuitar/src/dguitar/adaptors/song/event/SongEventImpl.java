/*
 * Created on Mar 4, 2005
 */
package dguitar.adaptors.song.event;

import dguitar.adaptors.song.SongEvent;
import dguitar.adaptors.song.SongMessage;
import dguitar.adaptors.song.SongVirtualTrack;


/**
 * @author crnash
 */
public class SongEventImpl implements SongEvent
{    
    SongVirtualTrack virtualTrack;
    int time;
    SongMessage message;
    
    public SongEventImpl(int time,SongMessage message)
    {
        this.time=time;
        this.message=message;
    }
 
    /**
     * @return Returns the time.
     */
    public int getTime()
    {
        return time;
    }
    /**
     * @param time The time to set.
     */
    public void setTime(int time)
    {
        this.time = time;
    }
    
    public boolean equals(Object o)
    {
        SongEvent se=(SongEvent)o;
        return((time==se.getTime())&&message.equals(se.getMessage()));
    }
    
    public int hashCode()
    {
        return toString().hashCode();
    }
    
    public String toString()
    {
        return "T"+time+message.toString();
    }

    /* (non-Javadoc)
     * @see Song.SongEvent#getMessage()
     */
    public SongMessage getMessage()
    {
        return message;
    }

    /* (non-Javadoc)
     * @see Song.SongEvent#setMessage(Song.SongMessage)
     */
    public void setMessage(SongMessage message)
    {
        this.message=message;
    }

    /* (non-Javadoc)
     * @see adaptors.song.SongEvent#getVirtualTrack()
     */
    public SongVirtualTrack getVirtualTrack()
    {
        return virtualTrack;
    }

    /* (non-Javadoc)
     * @see adaptors.song.SongEvent#setVirtualTrack(adaptors.song.SongVirtualTrack)
     */
    public void setVirtualTrack(SongVirtualTrack svt)
    {
        this.virtualTrack=svt;
    }
}
