/*
 * Created on Mar 4, 2005
 */
package dguitar.adaptors.song.event;


/**
 * A note on message
 * @author crnash
 */
public class SongNoteOnMessage extends SongNotationMessage
{
    int pitch;
    int velocity;
    int fret;
    
    public SongNoteOnMessage(int pitch,int velocity,int duration,int fret)
    {
        super(duration);
        
        this.pitch=pitch;
        this.velocity=velocity;
        this.fret=fret;
    }

    /* (non-Javadoc)
     * @see Song.event.SongMessageImpl#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
        if((o instanceof SongNoteOnMessage)&&(super.equals(o)))
        {
            SongNoteOnMessage o2=(SongNoteOnMessage)o;
            return((pitch==o2.pitch)&&(velocity==o2.velocity)&&(fret==o2.fret));
        }
        return false;
    }

    /* (non-Javadoc)
     * @see Song.event.SongMessageImpl#toString()
     */
    public String toString()
    {
        return "F"+fret+"P"+pitch+"V"+velocity+super.toString();
    }
    /**
     * @return Returns the velocity.
     */
    public int getVelocity()
    {
        return velocity;
    }
    /**
     * @param velocity The velocity to set.
     */
    public void setVelocity(int velocity)
    {
        this.velocity = velocity;
    }
    /**
     * @return Returns the pitch.
     */
    public int getPitch()
    {
        return pitch;
    }
    /**
     * @param pitch The pitch to set.
     */
    public void setPitch(int pitch)
    {
        this.pitch = pitch;
    }
    public int getFret()
    {
        return fret;
    }
    public void setFret(int fret)
    {
        this.fret = fret;
    }
}
