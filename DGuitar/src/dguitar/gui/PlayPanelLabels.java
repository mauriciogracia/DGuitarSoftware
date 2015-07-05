/*
 * Created on 11-mar-2005
 *
 */
package dguitar.gui;


import javax.swing.JLabel;
import javax.swing.JPanel;

import dguitar.players.sound.PerformanceTimerEvent;
import dguitar.players.sound.PerformanceTimerListener;



/**
 * A practical example of an object that implements the PerformanceTimerListener
 * interface to diplay information about what is being played. (measure,beat,bar)
 
 * @author Mauricio Gracia Gutierrez
 *
 */
public class PlayPanelLabels extends JPanel 
implements PerformanceTimerListener,Playable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3571445967708731379L;
	private JLabel songLbl ;
	private JLabel songText ;
	
	private JLabel measureLbl ;
	private JLabel measureValue ;
	
	private JLabel beatsLbl ;
	private JLabel beatsValue ;
	
	private JLabel divisionsLbl ;
	private JLabel divisionsValue ;
	
	private int numMeasures ;
	
	/**
	 * Determine the staus of the playPanel if it is NOTHING_PLAYABLE, PLAYABLE or PLAYING
	 */
	private short status ;
	private String title;
	
	/**
	 * Creates a PlayPanel 
	 */
	public PlayPanelLabels(short st) {
		super() ;
		
		numMeasures = -1 ;
		
		songLbl = new JLabel(DGuitar.lang.getString("Song") + ":") ;
		songText = new JLabel() ;
		
		measureLbl = new JLabel(DGuitar.lang.getString("Measure") + ":") ;
		measureValue = new JLabel() ;
		
		beatsLbl = new JLabel(DGuitar.lang.getString("Beat") + ":") ;
		beatsValue = new JLabel() ;
		
		divisionsLbl = new JLabel(DGuitar.lang.getString("Division") + ":") ;
		divisionsValue = new JLabel() ;

		setStatus(st);
		updateStatus();

		this.add(songLbl) ;
		this.add(songText) ;
		
		this.add(measureLbl) ;
		
		this.add(measureValue) ;
		
		this.add(beatsLbl) ;
		
		this.add(beatsValue) ;
		
		this.add(divisionsLbl) ;
		
		this.add(divisionsValue) ;
		
	}
	/**
	 * @param st The playing to set.
	 */
	public void setStatus(short st) {
		this.status = st ;
		this.updateStatus();
	}
	/**
	 * Call this method right after a setPlaying if you want to update components
	 *
	 */
	public void updateStatus() {
        boolean show ;
        
        //the labels are shown when the playpanel is PLAYABLE or PLAYING
        show = (this.status >= Playable.NOT_PLAYING ) ;
        
        
		songLbl.setVisible(show) ;
		
		songText.setVisible(show) ;
		
		measureLbl.setVisible(show) ;
		
		measureValue.setVisible(show) ;
		
		beatsLbl.setVisible(show) ;
		
		beatsValue.setVisible(show) ;
		
		divisionsLbl.setVisible(show) ;
		
		divisionsValue.setVisible(show) ;
        
	}
    public short getStatus() {
        return this.status ;
    }

	public void setSongTitle(String title) {
		this.title = title ;
		songText.setText("\"" + title + "\"") ;
	}
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
	@Override
	public String getSongTitle() {
		return this.title;
	}
}
