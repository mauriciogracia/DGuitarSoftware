/*
 * Created on Mar 9, 2005
 */
package dguitar.players.sound.midi.demo;

import dguitar.players.sound.PerformanceTimerEvent;
import dguitar.players.sound.PerformanceTimerListener;

/**
 * An example of an object that implements the PerformanceTimerListener
 * interface. See PlayerDemo for a walkthrough.
 * @author crnash
 */
public class ExampleTimerListener implements PerformanceTimerListener
{
    LinePrinter out;
    
    /**
     * @param out
     */
    public ExampleTimerListener(LinePrinter out)
    {
        this.out=out;
    }

    public void onTimer(PerformanceTimerEvent pte)
    {
        out.println("Measure:" + pte.getMeasure() + " beat:"
                + pte.getBeat() + "/" + pte.getTotalBeats() + " div:"
                + pte.getDivision() + "/" + pte.getTotalDivisions());
    }
}