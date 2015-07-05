/**
 * 
 */
package dguitar.gui;

import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JInternalFrame;
import javax.swing.JToolBar;

import common.ImageToolBar;
import common.Util;

/**
 * @author Mauricio Gracia G
 *
 */
public class PlayToolBar extends JToolBar 
implements ActionListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5774627345849320075L;
	private ImageToolBar buttons ;
	private PlayPanelLabels playPanel ;
	private DGDesktopPane desktop ;
	private Playable playable ;
	
    /**
     * Determine the status of the playPanel if it is NOTHING_PLAYABLE, PLAYABLE or PLAYING
     */
    private short status ;
    
    /*this buttons are not FINAL user could arrange them on different order*/
    
    public static short BTN_INDEX_PLAY = 0 ;
    
    public static short BTN_INDEX_STOP = 1 ;
	/**
	 * Creates a PlayToolBar that can be used to play Midi/SongInternalFrames
	 * 
	 * @param images		the images that are shown in the toolbar
	 * @param aDesktop		a JDesktopPane that contains Midi/SongInternalFrames
	 */
	public PlayToolBar(Image images[],DGDesktopPane aDesktop,Image disableImages[]) {
		super() ;
		
		this.setFloatable(false) ;
		desktop = aDesktop ;
		this.buttons = new ImageToolBar() ;
		this.buttons.setFloatable(false) ;
		this.buttons.setNumButtons(images.length) ;
		this.buttons.setImages(images) ;

        status = Playable.NOTHING_PLAYABLE ;
		//		Add the PlayPanel to the toolbar, false means is not playing anything
		this.playPanel = new PlayPanelLabels(status);
		
		this.playPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		
		this.buttons.add(playPanel);
		
		
		this.buttons.addActionListener(this) ;
		
		this.add(this.buttons) ;		 
        
        this.setStatus(status) ;
        
        this.playable = null ;

	}

	public void setPlayable(Playable p)
	{
		String tit ;
		
		this.playable = p  ;
		
		if(p != null)
		{
			
			this.setStatus(this.playable.getStatus());
			
			tit = this.playable.getSongTitle() ;
			
			this.setSongTitle(Util.compactAndReadableURL(tit,80) );
		}
	}
	
	public Playable getPlayable() {
		return this.playable;
	}
	
	public void setStatus(short st) {
		this.status = st ;
       
        switch(st) {
        case Playable.NOTHING_PLAYABLE :
            this.buttons.setButtonEnabled(BTN_INDEX_PLAY,false) ;
            this.buttons.setButtonEnabled(BTN_INDEX_STOP,false) ;
            this.playable = null ;
            break ;
        case Playable.NOT_PLAYING :
            this.buttons.setButtonEnabled(BTN_INDEX_PLAY,true) ;
            this.buttons.setButtonEnabled(BTN_INDEX_STOP,false) ;
            break ;
        case Playable.PLAYING :
            this.buttons.setButtonEnabled(BTN_INDEX_PLAY,false) ;
            this.buttons.setButtonEnabled(BTN_INDEX_STOP,true) ;
            break ;
        
        }
		this.playPanel.setStatus(st) ;
		
	}
	
	/***
	 * Returns the current status of the PlayToolBar
	 */
    public short getStatus() {
        return this.status ;
    }
    
    /***
     * Determines if a JInternalFrame is playable 
     * 
     * @param jif
     * @return
     */
	public static Playable asPlayable(JInternalFrame jif) {
		Playable p ;
		
		p = null ;
		try {
			p = (Playable) jif ;
		} catch (ClassCastException cce) {
			System.err.println("Internal Frame is not playable") ;
			//InternalFrame is not playable
		}
		return p ;
	}		
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent event) {
		JInternalFrame JIF ;
		boolean changeStatus ;
		
        short newStatus = 0 ;
		Playable paux ;
		
		changeStatus = false ;
        if(status != Playable.NOTHING_PLAYABLE)
        {
        	JIF = desktop.getSelectedFrame() ;
        	
        	
        	if(status == Playable.PLAYING)
        	{
        		newStatus = Playable.NOT_PLAYING ;
        		        	
        		paux = asPlayable(JIF) ;
        		
        		if(paux != null) {
        			this.setSongTitle(paux.getSongTitle());
        		}
        		changeStatus = true ;
        	}
        	else
        	{
        		this.playable = asPlayable(JIF) ;
       		
        		if(playable != null) {
        			newStatus = Playable.PLAYING ;
        			changeStatus = true ;
        		}
        	}
            
        	
        	if(changeStatus)
        	{
        		//ask the playable object to set the new status (object = MidiInternalFrame or SongInternalFrame..etc)
        		playable.setStatus(newStatus) ;
        		this.setStatus(newStatus) ;
        	}
        }
	}
    /**
     * set the Song title to be displayed by this toolbar
     * @param s
     */
    public void setSongTitle(String s) 
    {
        this.playPanel.setSongTitle(s) ;
    }
	/**
	 * @return Returns the playPanel.
	 */
	public PlayPanelLabels getPlayPanel() {
		return playPanel;
	}

	

}
