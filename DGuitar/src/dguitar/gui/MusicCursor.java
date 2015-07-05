/*
 * Created on 14/03/2005
 *
 */
package dguitar.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;

//UNUSED import dguitar.codecs.guitarPro.GPBeat;

/**
 * 
 * This class is a class that represent a cursor for music edition
 * 
 * @author Mauricio Gracia Gutiérrez
 *  
 */
//public class MusicCursor extends JComponent {
public class MusicCursor extends JLabel {
	/* The information abouth the current beat */
	//UNUSED GPBeat beatInfo;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 921777185309797859L;

	private MusicCursorPosition musicCursorPosition ;

	private Color colorBorder ;

	/** The track panel were this cursor will be drawn* */
	private JComponent component ;

	private int leftOffset = 8 ;
	
	private int rightOffset = 10 ;

	/**
	 * Creates a cursor located at measure 1 and beat 1 linked to a BarPanel
	 * 
	 */

	public MusicCursor(BarPanel BP) {
	    super();
	    commonConstructor(BP) ;
	}
	/**
	 * Creates a cursor located at measure 1 and beat 1 linked to a Bar
	 * 
	 */

	public MusicCursor(Bar b) {
	    super();
	    commonConstructor(b) ;
	}
	private void commonConstructor(JComponent aComponent) {
		
		if(aComponent != null) {
			this.component = aComponent ;
			this.musicCursorPosition = new MusicCursorPosition() ;
			this.musicCursorPosition.setWidth(leftOffset + rightOffset) ;
			this.colorBorder = Color.RED ;
			this.setBorder(BorderFactory.createLineBorder(this.colorBorder) ) ;
			//This component is NOT opaque (its transparent !!)
			this.setOpaque(false);
			//this.setForeground(this.colorBorder) ;

		}else {
			throw new NullPointerException("the parameter for MusicCursor(JLayeredPane) is null!") ;
		}
	}

	public Dimension getMinimumSize() {
		int h ;
		
		h = this.component.getHeight()-2 ;

		return new Dimension(this.musicCursorPosition.getWidth(),h);
	}


	
	public Dimension getPreferredSize() {
		return this.getMinimumSize();
	}
	

	public String toString() {
		String resp;

		resp = "MusicCursor" + this.musicCursorPosition ; 
		return resp;
	}
	/**
	 * Everty time the field point is updated you should call this
	 */
	private void pointUpdated() {
		int h ;
		Point p ;
		int w ;
		
		h = this.component.getHeight()-2 ;
		p = this.musicCursorPosition.getPoint() ;
		//this.setBounds(p.x-leftOffset, p.y, leftOffset + rightOffset, h);
		w = this.musicCursorPosition.getWidth() ;
		//DEBUG 
		//System.err.println("currentPosition = " + p) ;
		this.setBounds(p.x-leftOffset, p.y,w, h);
	}
	/*try to solve the refresh problem of Bars when scrolling
	public void validate() {
	    super.validate() ;
	    java.awt.Component comp ;
	    
	    comp = this.musicCursorPosition.getGuiComponent() ;
	    if(comp != null) {
	        comp.validate() ;
	    }
	}
	
*/
	/**
	 * @param musicCursorPosition The musicCursorPosition to set.
	 */
	public void setMusicCursorPosition(MusicCursorPosition musicCursorPosition) {
		this.musicCursorPosition = musicCursorPosition;
		this.pointUpdated() ;
	}

	/**
	 * @return Returns the musicCursorPosition.
	 */
	public MusicCursorPosition getMusicCursorPosition() {
		return musicCursorPosition;
	}

	/**
	 * @param halfWidth The leftOffset to set.
	 */
	public void setLeftOffset(int halfWidth) {
		this.leftOffset = halfWidth;
	}

	/**
	 * @return Returns the leftOffset.
	 */
	public int getLeftOffset() {
		return leftOffset;
	}

	/**
	 * @param rightOffset The rightOffset to set.
	 */
	public void setRightOffset(int rightOffset) {
		this.rightOffset = rightOffset;
	}

	public void setWidth(int value) {
		this.musicCursorPosition.setWidth(value) ;
		this.rightOffset = value - this.leftOffset ;
		this.pointUpdated() ;
	}
	/**
	 * @return Returns the rightOffset.
	 */
	public int getRightOffset() {
		return rightOffset;
	}
    /**
     * @param aColorBorder The colorBorder to set.
     */
    public void setColorBorder(Color aColorBorder) {
        if(aColorBorder != null) {
            this.colorBorder = aColorBorder;
            this.setBorder(BorderFactory.createLineBorder(this.colorBorder) ) ;
        }
    }
    /**
     * @return Returns the colorBorder.
     */
    public Color getColorBorder() {
        return colorBorder;
    }
}

