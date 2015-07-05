/*
 * Created on Mar 7, 2005
 */
package test.songs;

import java.util.LinkedList;
import java.util.List;


/**
 * @author crnash
 */
public class SongDefinition
{
    String gpFileName;
    List<SongComponent> components;
    int[] channels;

    public SongDefinition()
    {
        components=new LinkedList<SongComponent>();
    }
    
    /**
     * @param component
     */
    public void addComponent(SongComponent component)
    {
        components.add(component);        
    }

    /**
     * @param channels
     */
    public void setChannels(int[] channels)
    {
        this.channels=channels;
    }
    /**
     * @return Returns the gpFileName.
     */
    public String getGpFileName()
    {
        return gpFileName;
    }
    /**
     * @param gpFileName The gpFileName to set.
     */
    public void setGpFileName(String gpFileName)
    {
        this.gpFileName = gpFileName;
    }
    /**
     * @return the number of components
     */
    public int getComponentCount()
    {
        return components.size();
    }
    
    public SongComponent getComponent(int index)
    {
        return (SongComponent)components.get(index);
    }

    /**
     * @return the track count
     */
    public int getTrackCount()
    {
        return channels.length/2;
    }

    /**
     * @param i
     * @return the main channel according to i
     */
    public int getMainChannel(int i)
    {
        return channels[2*i];
    }
    /**
     * @return Returns the channels.
     */
    public int[] getChannels()
    {
        return channels;
    }
}
