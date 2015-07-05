/*
 * Created on Mar 29, 2005
 */
package dguitar.adaptors.song.event;

/**
 * @author Chris
 */
public class SongTieMessage extends SongNotationMessage
{   
    public SongTieMessage(int duration)
    {
        super(duration);
    }
    
    public String toString()
    {
        return "TIE"+super.toString();
    }
}
