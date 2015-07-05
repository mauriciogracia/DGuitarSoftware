/*
 * Created on Mar 8, 2005
 */
package dguitar.players.sound.midi;

import javax.sound.midi.MidiEvent;

import dguitar.players.sound.PerformanceEvent;



/**
 * @author Chris
 */
public interface MetaContainer
{

    /**
     * @param item
     * @return a MidiEvent based on the item
     */
    MidiEvent create(PerformanceEvent item);

    /**
     * @param key
     * @return the PerformanceEvent associadte with the key
     */
    PerformanceEvent lookup(int key);

}
