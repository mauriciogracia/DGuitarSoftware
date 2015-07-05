/*
 * Created on Mar 4, 2005
 */
package dguitar.adaptors.song;

/**
 * A SongMessage is a single event in a song. Events have an associated time
 * but messages do not.
 * @author crnash
 */
public interface SongMessage
{
    public boolean equals(Object o);
    public int hashCode();
    public String toString();
}
