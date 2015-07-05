/*
 * Created on 22/03/2005
 *
 */
package dguitar.players.gui;

import javax.swing.SwingUtilities;

import dguitar.gui.TrackPanel;
import dguitar.players.sound.PerformanceTimerEvent;
import dguitar.players.sound.PerformanceTimerListener;


/**
 * @author Mauricio Gracia Gutierrez
 * 
 */
public class MusicCursorScroller implements PerformanceTimerListener {
    private TrackPanel TP;
    private int measurePrev ;
    private boolean scroll ;
    
    /**
     * 
     * @param aTrackPanel is the TrackPanel that has the musicCursor to move
     * @param scrollTrack should the Track be scroll to see the new position ?
     */
    public MusicCursorScroller(TrackPanel aTrackPanel, boolean scrollTrack) {
        if(aTrackPanel != null) {
            this.TP = aTrackPanel;
            this.measurePrev = -1 ;
            this.scroll = scrollTrack ;
        }
        else {
            throw new NullPointerException("aTrackPanel passed to the MusicCursorScroller is null") ;
        }
    }

    class RunLater implements Runnable
    {
        private MusicCursorScroller mcs;
        private PerformanceTimerEvent pte;

        RunLater(MusicCursorScroller mcs,PerformanceTimerEvent pte)
        {
            this.mcs=mcs;
            this.pte=pte;
        }

        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run()
        {
            int measure = pte.getMeasure() ;
            TP.setMusicCursor(measure,mcs.measurePrev,mcs.scroll);
            mcs.measurePrev = measure ;               
        }
    }
    /*
     * (non-Javadoc)
     * 
     * @see players.sound.PerformanceTimerListener#onTimer(players.sound.PerformanceTimerEvent)
     */
    public void onTimer(PerformanceTimerEvent timerEvent) {
//UNUSED        int measure ;
        //TODO continue testing beat cursors
        //tp.setMusicCursor(timerEvent.getMeasure(),timerEvent.getBeat()) ;
        if((timerEvent.getBeat()==1)&&(timerEvent.getDivision()==1)) {
            SwingUtilities.invokeLater(new RunLater(this,timerEvent));
            //DEBUG
            //System.err.println(TP) ;
        }
    }
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        // TODO Auto-generated method stub
        
    }
}

