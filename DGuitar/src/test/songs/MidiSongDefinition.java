/*
 * Created on Mar 19, 2005
 */
package test.songs;

/**
 * @author Chris
 */
public class MidiSongDefinition extends SongDefinition
{
    String midiFileName;
    String[] eventRemap;

    /**
     * @return Returns the midiFileName.
     */
    public String getMidiFileName()
    {
        return midiFileName;
    }

    /**
     * @param midiFileName The midiFileName to set.
     */
    public void setMidiFileName(String midiFileName)
    {
        this.midiFileName = midiFileName;
    }
    /**
     * @return Returns the eventRemap.
     */
    public String[] getEventRemap()
    {
        return eventRemap;
    }
    /**
     * @param eventRemap The eventRemap to set.
     */
    public void setEventRemap(String[] eventRemap)
    {
        this.eventRemap = eventRemap;
    }
}
