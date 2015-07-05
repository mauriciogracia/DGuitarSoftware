package dguitar.gui;

import java.awt.Component;
import java.beans.PropertyVetoException;

import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class DGDesktopPane extends JDesktopPane {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5675020492448722368L;

	public final static int TILE_WINDOWS_HORIZ = 1;
	
	public final static int TILE_WINDOWS_VERT = 2;
	
	public final static int CASCADE_WINDOWS = 3;
	
	private int numFrames ;
	
	public DGDesktopPane() {
		numFrames = 0 ;
	}
	
	public void arrangeWindows(int mode, JInternalFrame jifs[]) {
		numFrames = jifs.length ; 
		if(numFrames >= 1) {
			switch(mode) {
			case CASCADE_WINDOWS :
				cascade(jifs) ;
				break;
			case TILE_WINDOWS_VERT:
				//TODO
				break;
			case TILE_WINDOWS_HORIZ:
				//TODO
				break;
			}
		}
		else {
			//there is no operation to be done on 1 window only
		}
	}
	private void cascade(JInternalFrame jifs[]) {
		int x ;
		int y ;
		int deltaX ;
		int deltaY ;
		int i ;
		
		deltaX = this.getWidth()/numFrames ;
		deltaY = this.getHeight()/numFrames ;
		
		x = 0 ;
		y = 0 ;
		//iterate over the jifs to set the position
		for(i = 0; i < numFrames ; i++) {
			jifs[i].setLocation(x + i*deltaX, y + i*deltaY) ;

			try {
				jifs[i].setSelected(true) ;
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		}
		
	}
	/**
	 * Given a desktop component it returns a JInternalFrame
	 */
	public JInternalFrame getJIF(int i) {
		JInternalFrame JIF;
		JInternalFrame.JDesktopIcon JDI;
		java.awt.Component comp;
		Class<? extends Component> c;
		String msg ;
		
		comp = this.getComponent(i);
		JIF = null;
		if (comp != null) {
			JIF = new JInternalFrame();
			c = comp.getClass();
			JDI = new JInternalFrame.JDesktopIcon(JIF);
			
			if (c.isInstance(JDI)) {
				JDI = (JInternalFrame.JDesktopIcon) comp;
				JIF = JDI.getInternalFrame();
			} else {
				try {
					JIF = (JInternalFrame) comp;
				} catch (java.lang.ClassCastException CCE) {
					JIF = null;
					msg = "Desktop contains a " + c.toString() + "object !!, please REPORT THIS";
					System.err.println(msg) ;
				}
			}
		}
		return JIF;
	}
	public JInternalFrame getSelectedFrame() {
		JInternalFrame JIF;
		JInternalFrame.JDesktopIcon JDI;
		java.awt.Component comp;
		Class<? extends Component> c;
		String msg ;
		
		comp = super.getSelectedFrame();
		JIF = null;
		if (comp != null) {
			JIF = new JInternalFrame();
			c = comp.getClass();
			JDI = new JInternalFrame.JDesktopIcon(JIF);
			
			if (c.isInstance(JDI)) {
				JDI = (JInternalFrame.JDesktopIcon) comp;
				JIF = JDI.getInternalFrame();
			} else {
				try {
					JIF = (JInternalFrame) comp;
				} catch (java.lang.ClassCastException CCE) {
					JIF = null;
					msg = "Desktop contains a " + c.toString() + "object !!, please REPORT THIS";
					System.err.println(msg) ;
				}
			}
		}
		return JIF;
	}
}
