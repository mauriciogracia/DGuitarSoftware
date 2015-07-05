/*
 * Created on Mar 3, 2005
 */
package dguitar.adaptors.song;

import java.util.logging.Logger;


/**
 * A tempo is commonly expressed in two notations
 * 'beats per minute' (which requires definition of the size of a beat)
 * 'microseconds per quarter note' (which does not)
 * This class handles the conversions between the two.
 * @author crnash
 */
public class Tempo
{   
    private static String className=Tempo.class.toString();
    private static Logger logger=Logger.getLogger(className);
    private static final double ONE_MINUTE_MICROSECONDS=60.0*1000.0*1000.0;
    
    int beat=4;
    double bpm=120.0;
    double usq=500000.0;
    
    public int getBeat()
    {
        logger.finest("Tempo beat="+beat);
        return beat;
    }
    public double getBPM()
    {
        logger.finest("Tempo BPM="+bpm);
        return bpm;
    }
    public double getUSQ()
    {
        logger.finest("Tempo USQ="+usq);
        return usq;
    }
    
    public void setBeat(int b)
    {
        logger.finest("Tempo set beat to "+b);
        beat=b;
        recalculateUSQ();
    }
    public void setBPM(double b)
    {
        logger.finest("Tempo set bpm to "+b);
        bpm=b;
        recalculateUSQ();
    }
    public void setUSQ(double u)
    {
        logger.finest("Tempo set usq to "+u);
        usq=u;
        recalculateBPM();
    }
    
    private void recalculateUSQ()
    {
        // turn BPM into 'quarter notes per minute'
        double qpm=(bpm*4.0)/beat;
        // and now
        usq=ONE_MINUTE_MICROSECONDS/qpm;
        logger.finest("Tempo recalculate usq="+usq);
    }
    private void recalculateBPM()
    {
        double qpm=ONE_MINUTE_MICROSECONDS/usq;
        bpm=(qpm*beat)/4.0;
        logger.finest("Tempo recalculate bpm="+bpm);
    }
}
