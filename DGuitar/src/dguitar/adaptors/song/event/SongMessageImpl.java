/*
 * Created on Mar 4, 2005
 */
package dguitar.adaptors.song.event;

import dguitar.adaptors.song.SongMessage;


/**
 * The basic message implementation. Allows properties of messages to
 * be retrieved in the same way as a Bean.
 * @author crnash
 */
abstract public class SongMessageImpl implements SongMessage
{  
    abstract public boolean equals(Object o);  
    public int hashCode()
    {
        return toString().hashCode();
    }    
    abstract public String toString();
}
