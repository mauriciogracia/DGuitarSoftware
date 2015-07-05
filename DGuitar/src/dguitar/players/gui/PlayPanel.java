/*
 * Created on 11-mar-2005
 *
 */
package dguitar.players.gui;


import javax.swing.JLabel;
import javax.swing.JPanel;

import dguitar.players.sound.PerformanceTimerEvent;
import dguitar.players.sound.PerformanceTimerListener;



/**
 * An example of an object that implements the PerformanceTimerListener
 * interface to diplay GUI information about what is being played.

 * @author Mauricio Gracia Gutierrez
 *
 */
public class PlayPanel extends JPanel implements PerformanceTimerListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -463729372392830471L;
	private JLabel measureLbl ;
	private JLabel measureValue ;
	
	private JLabel beatsLbl ;
	private JLabel beatsValue ;
	
	private JLabel divisionsLbl ;
	private JLabel divisionsValue ;

	private int numMeasures ;
	/**
	 * Creates a PlayPanel
	 * 
	 */
	public PlayPanel() {
		super() ;
		
		measureLbl = new JLabel("Measure:") ;
		measureValue = new JLabel() ;
		
		beatsLbl = new JLabel("Beat:") ;
		beatsValue = new JLabel() ;
		
		divisionsLbl = new JLabel("Division:") ;
		divisionsValue = new JLabel() ;
		
		this.add(measureLbl) ;
		this.add(measureValue) ;
		
		
		this.add(beatsLbl) ;
		this.add(beatsValue) ;
		
		this.add(divisionsLbl) ;
		this.add(divisionsValue) ;

	}
//	this.gpSong.getNumMeasures()
    public void onTimer(PerformanceTimerEvent pte)
    {
        //DEBUG
        /*System.out.println("Measure:" + pte.getMeasure() + " beat:"
                + pte.getBeat() + "/" + pte.getTotalBeats() + " div:"
                + pte.getDivision() + "/" + pte.getTotalDivisions());
                */
    	measureValue.setText( "" + pte.getMeasure() + "/" + this.numMeasures) ;
    	//measureValue.repaint() ;
    	
    	beatsValue.setText( "" + pte.getBeat() + "/" + pte.getTotalBeats() ) ;
    	//beatsDivisionsValue.repaint() ;
    	
    	divisionsValue.setText( "" + pte.getDivision() + "/" + pte.getTotalDivisions() ) ;
    }
    /**
     * @param numMeasures The numMeasures to set.
     */
    public void setNumMeasures(int numMeasures) {
        this.numMeasures = numMeasures;
    }
    /**
     * @return Returns the numMeasures.
     */
    public int getNumMeasures() {
        return numMeasures;
    }
}
