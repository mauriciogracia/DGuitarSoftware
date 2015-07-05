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

/**
 * @author Mauricio Gracia G
 *
 */
public class PlayToolBar extends JToolBar 
implements ActionListener ,Playable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5774627345849320075L;
	private ImageToolBar buttons ;
	private PlayPanelLabels playPanel ;
	private DGDesktopPane desktop ;
	
    /**
     * Determine the staus of the playPanel if it is NOTHING_PLAYABLE, PLAYABLE or PLAYING
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

	}

	public void setStatus(short st) {
		this.status = st ;
       
        switch(st) {
        case NOTHING_PLAYABLE :
            this.buttons.setButtonEnabled(BTN_INDEX_PLAY,false) ;
            this.buttons.setButtonEnabled(BTN_INDEX_STOP,false) ;
            break ;
        case NOT_PLAYING :
            this.buttons.setButtonEnabled(BTN_INDEX_PLAY,true) ;
            this.buttons.setButtonEnabled(BTN_INDEX_STOP,false) ;
            break ;
        case PLAYING :
            this.buttons.setButtonEnabled(BTN_INDEX_PLAY,false) ;
            this.buttons.setButtonEnabled(BTN_INDEX_STOP,true) ;
            break ;
        
        }
		this.playPanel.setStatus(st) ;
	}
    public short getStatus() {
        return this.status ;
    }
	public static Playable getPlayable(JInternalFrame jif) {
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
		Playable p ;
		Object source ;
        short newStatus ;
		//if(arg0.getSource().equals(this.buttons.getButton(0)) ;
		
		//get the currently selected frame
		JIF = desktop.getSelectedFrame() ;

        newStatus = Playable.NOTHING_PLAYABLE ;
        //if there is a Selected Frame
		if(JIF != null) {
			//get the playable object
			p = getPlayable(JIF) ;

            //if the frame is playable (MidiInternalFrame or SongInternalFrame..etc)
            if(p != null) {
                //when the status is NOTHING_PLAYABLE there is not much to do..then...
                if(status >= Playable.NOT_PLAYING)  {
                    source = event.getSource();
                    //if PLAY button is pressed
                    if(source.equals(this.buttons.getButtonAtIndex(BTN_INDEX_PLAY))) {
                        newStatus = Playable.PLAYING ;
                    //if STOP button is pressed
                    } else if(source.equals(this.buttons.getButtonAtIndex(BTN_INDEX_STOP))) {
                        newStatus = Playable.NOT_PLAYING ;
                    }
                }
                //ask the playable object to set the new status (object = MidiInternalFrame or SongInternalFrame..etc)
                p.setStatus(newStatus) ;

            }
            //else the newStatus is left as NOTHING_PLAYABLE
            
		}
        //else the newStatus is left as NOTHING_PLAYABLE
        
        this.setStatus(newStatus) ;
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
