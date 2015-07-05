/*
 * Created on Mar 4, 2005
 */
package dguitar.adaptors.song.event;

/**
 * A note off message.
 * @author crnash
 */
public class SongNoteOffMessage extends SongMessageImpl
{
    SongNoteOnMessage source;
    
    public SongNoteOffMessage(SongNoteOnMessage source)
    {
        this.source=source;
    }

    /* (non-Javadoc)
     * @see Song.event.SongMessageImpl#equals(java.lang.Object)
     */
    public boolean equals(Object o)
    {
        if(o instanceof SongNoteOffMessage)
        {
            SongNoteOffMessage o2=(SongNoteOffMessage) o;
            return source.equals(o2.source);
        }
        return false;
    }

    /* (non-Javadoc)
     * @see Song.event.SongMessageImpl#toString()
     */
    public String toString()
    {
        return source.toString()+"OFF";
    }
    public SongNoteOnMessage getSource()
    {
        return source;
    }
}
