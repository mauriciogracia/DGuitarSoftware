/*
 * Created on Mar 22, 2005
 */
package dguitar.players.sound;

/**
 * @author crnash
 */
public interface EventSoundPlayer extends SoundPlayer, EventSettings
{  
    /**
     * @param listener
     */
    void addEventListener(PerformanceEventListener listener);
}
