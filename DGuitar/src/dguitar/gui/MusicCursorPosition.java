/*
 * Created on 14/03/2005
 *
 */
package dguitar.gui;

import java.awt.Component;
import java.awt.Point;

import dguitar.codecs.guitarPro.GPBeat;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;

/**
 * @author Mauricio Gracia Gutiérrez
 *
 */
public class MusicCursorPosition {

	/**
	 * On which measure is the cursor located 
	 */
	private int numMeasure;
	/**
	 * On which beat is the cursor located 
	 */
	private int numBeat;
	/**
	 * The point where this measure and beat is located
	 */
	private Point point;
	
	/**
	 * This object holds information about the current beat or bar
	 */
	private Object songObject ;
	
	/**
	 * This object holds information about the GUI object 
	 * where the MusciCursror will be draw.
	 */
	private Component guiComponent ;

	/**
	 * To know if the object is a beat or a bar
	 */
	private boolean beat ;
	
	/**
	 * The graphical width of the Object
	 */
	private int width ;
	/**
	 * Creates en new MusicCursorPosition at measure 1 and beat 1
	 * 
	 */
	public MusicCursorPosition (){
		this.numBeat = 1 ;
		this.numMeasure = 1 ;
		this.point = new Point() ;
		this.songObject = null ;
		this.width = 10 ;
	}
	/**
	 * @return Returns the numMeasure.
	 */

	public int getNumMeasure() {
		return numMeasure;
	}
	/**
	 * @param numBeat The numBeat to set.
	 */
	public void setNumBeat(int numBeat) {
		if(numBeat >= 1) {
			this.numBeat = numBeat;
		}
	}
	/**
	 * @param numMeasure
	 *            The numMeasure to set.
	 */
	public void setNumMeasure(int numMeasure) {
		if(numMeasure >= 1) {
			this.numMeasure = numMeasure;
		}
	}
	/**
	 * @return Returns the numBeat.
	 */
	public int getNumBeat() {
		return numBeat;
	}
	public String toString() {
		String res ;
		
		res = "Music Cursor Position " ;
		res += "Measure: " + this.numMeasure ;
		res += ", Beat: " + this.numBeat ;
		res += ", point: " + this.point ;
		if(this.songObject != null) {
		    res += (this.beat) ? "isBeat" : "isBar" ;
			/*detaile printing
			  
			if(this.beat) {
				res += ", beat: " ;
			}else {
				res += ", MTP: " ;
			}
			res += this.beatOrBar ;
			*/
		}
		return res ;
	}
	/**
	 * @param obj The Bar or Beat you want the cursor to be associated with
	 */
	public boolean setSongObject(Object obj) {
		boolean success ;
		Class<? extends Object> c ;
		
		success = false ;
		if(obj != null) {
			c = obj.getClass() ;
			if(c.isInstance(new GPBeat() )) {
				this.beat = true ;
			}
			else if(c.isInstance(new GPMeasureTrackPair() )) {
				this.beat = false ;
			}
			else {
				throw new ClassCastException("Object must be GPBeat or GPMeasureTrackPair") ;
			}
			this.songObject = obj ;
		}
		else {
			throw new NullPointerException("Object must not bet null") ;
		}
		return success ;
	}
	/**
	 * @return Returns true if the cursor is linked to a beat.
	 */
	public boolean isBeat() {
		return this.beat;
	}
	/**
	 * @return Returns true if the cursor is linked to a bar
	 */
	public boolean isMTP() {
		return (!this.beat) ;
	}
	/**
	 * @param point The point to set.
	 */
	public void setPoint(Point point) {
		this.point = point;
	}
	/**
	 * @return Returns the point.
	 */
	public Point getPoint() {
		return point;
	}
	/**
	 * @param width The width to set.
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	/**
	 * @return Returns the width.
	 */
	public int getWidth() {
		return width;
	}
    /**
     * @return Returns the songObject.
     */
    public Object getSongObject() {
        return songObject;
    }
    /**
     * @param aGuiComponent The guiObject to set.
     */
    public void setGuiComponent(Component aGuiComponent) {
        if(aGuiComponent != null) {
            this.guiComponent = aGuiComponent;
        }
    }
    /**
     * @return Returns the guiObject.
     */
    public Component getGuiComponent() {
        return guiComponent;
    }
}
