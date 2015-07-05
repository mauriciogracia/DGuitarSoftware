/**
 * 
 */
package dguitar.gui.unused;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dguitar.gui.DGuitar;

/**
 * @author Mauricio Gracia G
 *
 */
public class EnableUnfinishedListener implements ActionListener {
	DGuitar DG ;
	
	public EnableUnfinishedListener (DGuitar aDG){
		DG = aDG ;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		//swith from true to false..and back
		//DG.enableUnfinishedToogle() ;
		//DG.updateEnableUnfinishedFeatures() ;
	}
}
