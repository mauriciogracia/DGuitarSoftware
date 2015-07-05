/*
 * Created on Feb 26, 2005
 */
package dguitar.adaptors.song;


/**
 * A SongEvent is a generic event in a song
 * @author Chris
 */
public interface SongEvent
{
    public int getTime();
    public void setTime(int time);
    public SongMessage getMessage();
    public void setMessage(SongMessage message);
    
    public boolean equals(Object o);
    public int hashCode();
    public String toString();
    /**
     * @return the virtual track for this Song Event
     */
    public SongVirtualTrack getVirtualTrack();
    public void setVirtualTrack(SongVirtualTrack svt);
}
