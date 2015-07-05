/*
 * Created on Feb 26, 2005
 */
package dguitar.adaptors.song;


/**
 * A RepeatedSongPhrase defines an underlying phrase and a repeat count.
 * @author Chris
 */
public interface RepeatedSongPhrase extends SongPhrase
{
    SongPhrase getPhrase();
    int getRepeatCount();
}
