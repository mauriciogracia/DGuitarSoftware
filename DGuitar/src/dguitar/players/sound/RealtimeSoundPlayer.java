/*
 * Created on Mar 22, 2005
 */
package dguitar.players.sound;

/**
 * @author crnash
 */
public interface RealtimeSoundPlayer extends SoundPlayer, TimerSettings
{
    /**
     * @param listener
     */
    void addTimerListener(PerformanceTimerListener listener);
    
    /**
     * 
     */
    void stop();

    /**
     * 
     */
    void waitForCompletion();
}
