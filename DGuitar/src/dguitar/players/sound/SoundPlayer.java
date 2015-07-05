/*
 * Created on Mar 9, 2005
 */
package dguitar.players.sound;

import dguitar.adaptors.song.Tempo;

/**
 * @author crnash
 */
public interface SoundPlayer
{

    /**
     * Create a performance object that is compatible with this class of SoundPlayer.
     * @param tracks the number of physical tracks in this performance
     * @param tempo the tempo at which to play it
     * @param resolution the resolution (in ticks per quarter note)
     * @return the Performance object that was created
     */
    Performance createPerformance(int tracks, Tempo tempo, int resolution);

    /**
     * 
     */
    void start();

    /**
     * 
     */
    void close();



    /**
     * @param performance
     */
    void setPerformance(Performance performance);





}
