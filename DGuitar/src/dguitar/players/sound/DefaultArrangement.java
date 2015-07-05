/*
 * Created on Mar 27, 2005
 */
package dguitar.players.sound;

import java.util.ArrayList;
import java.util.List;

import dguitar.adaptors.song.Song;

/**
 * @author Chris
 */
public class DefaultArrangement implements Arrangement
{
    ArrayList<Integer> measureList;
    
    public DefaultArrangement(Song song)
    {
        int measureCount=song.getPerformanceMeasureCount();
        measureList=new ArrayList<Integer>(measureCount);
        for(int i=0;i<measureCount;i++)
        {
            measureList.add(new Integer(i));
        }
    }
    /* (non-Javadoc)
     * @see players.sound.Arrangement#getMeasureList()
     */
    public List<Integer> getMeasureList()
    {
        return measureList;
    }

}
