/*
 * Created on Mar 29, 2005
 */
package dguitar.adaptors.song.event;

/**
 * @author Chris
 */
public class SongNotationMessage extends SongMessageImpl
{
    private int duration;
    
    public SongNotationMessage(int duration)
    {
        this.duration=duration;
    }

    /**
     * @return Returns the duration.
     */
    public int getDuration()
    {
        return duration;
    }

    /**
     * @param duration The duration to set.
     */
    public void setDuration(int duration)
    {
        this.duration = duration;
    }

    /* (non-Javadoc)
     * @see adaptors.song.event.SongMessageImpl#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
        if(o instanceof SongNotationMessage)
        {
            SongNotationMessage snm=(SongNotationMessage)o;
            return(duration==snm.duration);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see adaptors.song.event.SongMessageImpl#toString()
     */
    public String toString()
    {
        return "D"+duration;
    }
}
