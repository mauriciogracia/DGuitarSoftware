/*
 * Created on Mar 4, 2005
 */
package dguitar.adaptors.song;

/**
 * A SongChannelEvent is an event to be sent on a particular channel.
 * @author crnash
 */
public interface SongChannelEvent extends SongEvent
{
    public int getChannel();
    public void setChannel(int channel);
}
