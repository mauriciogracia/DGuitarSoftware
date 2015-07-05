/*
 * Created on Mar 2, 2005
 */
package dguitar.players.sound;

import java.util.List;

/**
 * An Arrangement is just a section of a song and a tempo to perform it at.
 * @author crnash
 */
public interface Arrangement
{
    /**
     * Gets the list of Integers which are zero-based measure numbers
     * into the song.
     * @return list, of Integer objects
     */
    List<Integer> getMeasureList();
}
