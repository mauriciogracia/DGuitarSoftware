/*
 * Created on 3/04/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dguitar.gui.listeners;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import dguitar.gui.DGuitar;
import dguitar.gui.PlayToolBar;
import dguitar.gui.Playable;



public class DGInternalFrameListener implements InternalFrameListener {
    private final DGuitar dGuitar;
    JInternalFrame JIF;

    public DGInternalFrameListener(DGuitar guitar) {
        this.dGuitar = guitar;

    }

    /***
     * Invoked when an internal frame is activated.
     */
    public void internalFrameActivated(InternalFrameEvent e) {
        String tit ;
        
        JIF = e.getInternalFrame();
        tit = JIF.getTitle() ;
        this.dGuitar.setTitle(DGuitar.version() + "-" + tit);

        if(DGuitar.playToolBar.getStatus() != Playable.PLAYING)
        {
        	Playable p ;
        	
        	p = PlayToolBar.asPlayable(JIF) ;
        	
        	if(p != null)
        	{
        		DGuitar.playToolBar.setPlayable(p);

        	}
        }
    }

    //Invoked when an internal frame has been closed.
    public void internalFrameClosed(InternalFrameEvent e) {
        JIF = e.getInternalFrame();

        this.dGuitar.OpenedListRemove(JIF.getTitle()) ;
        
        //if this is the last window
        if(DGuitar.desktopPane.getComponentCount() == 0) {
            this.dGuitar.setTitle(this.dGuitar.shortTitle());
        //after closing all the windows set the playToolBar to NOTHING_PLAYABLE
            DGuitar.playToolBar.setStatus(Playable.NOTHING_PLAYABLE) ;
        }

    }

    //Invoked when an internal frame is in the process of being closed.
    //The close operation can be overridden at this point.
    public void internalFrameClosing(InternalFrameEvent e) {

    }

    public void internalFrameDeactivated(
            javax.swing.event.InternalFrameEvent internalFrameEvent) {

    }

    //Invoked when an internal frame is de-iconified.
    public void internalFrameDeiconified(InternalFrameEvent e) {

    }

    public void internalFrameIconified(
            javax.swing.event.InternalFrameEvent internalFrameEvent) {
        
        String tit ;
        
        JIF = DGuitar.desktopPane.getSelectedFrame();
        if (JIF != null) {
            tit = DGuitar.version() + "-" + JIF.getTitle();
        } else {
            tit = this.dGuitar.shortTitle();
        }
        this.dGuitar.setTitle(tit);
    }

    public void internalFrameOpened(
            javax.swing.event.InternalFrameEvent internalFrameEvent) {
    }

}