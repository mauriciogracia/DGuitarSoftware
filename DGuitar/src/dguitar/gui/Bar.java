/*
 * Bar.java
 *
 * Created on 27 de diciembre de 2004, 12:25 PM
 */

package dguitar.gui;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;

import dguitar.codecs.guitarPro.GPMeasure;

/**
 * This class represents a musicl bar (it contains a musical measure)
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public abstract class Bar extends JPanel
implements OptionsDisplay {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5563901485349728572L;

	/**The width increased if the Time Signature is being displayed*/
    public int TS ;
    
    /**is this the first bar of a track*/
    protected boolean isFirst ;
    
    /**is the last bar of a track*/
    protected boolean isLast ;
    
    private DisplayOptions displayOptions ;
    
    
    /**is the Time Signature displayed */
    protected boolean displayTimeSignature ;
    
    /**the minimun X position to start drawing ;*/
    protected int minx ;
    
    /**The numerator of the KeySignature*/
    private String numerator ;
    
    /**The denominator of the KeySignature*/
    private String denominator ;
    
    /**
     * If the Bar is link to a GPMeasure, this field is NOT null
     * */
    protected GPMeasure measure ;
    
    /**
     * The barPanel that contains this Bar
     */
    protected BarPanel BP ;
    
    private final Color backGround = Color.WHITE ;
    
    private final Color foreGround = Color.BLACK ;
    /**
     *  Creates a new instance of Bar with the default values
     */
    
    
    /**
     * Creates a new instance of Bar based on a BarPanel
     * @param currentDisplayOptions the current display options to use
     * @param bp                    the barpanel that contains this bar (or parent)
     */
    
    public Bar(DisplayOptions currentDisplayOptions, BarPanel bp) {
    	super() ;
        this.displayOptions = currentDisplayOptions ;
        this.BP = bp ;
        commonConstructor() ;
    }
    
    
    private void commonConstructor() {
        this.setOpaque(true) ;
        this.setBackground(this.backGround) ;
        this.setForeground(this.foreGround) ;
        this.setLayout(null) ; //this means an absolute layout
        this.measure = null ;
        this.isFirst = false ;
        this.isLast = false;
        this.setOpaque(true) ;
        this.displayTimeSignature = true ;
    }
    
    
    /**
     * @param displayOptions The displayOptions to set.
     */
    public void setDisplayOptions(DisplayOptions displayOptions) {
        this.displayOptions = displayOptions;
    }


    /**
     * @return Returns the displayOptions.
     */
    public DisplayOptions getDisplayOptions() {
        return displayOptions;
    }


    /**
     * This method returns true if This Time signature is equal to the Bar
     * @param bar the bar to compare it to
     * @return true if the signature is the same
     */
    public boolean equalTimeSignature(Bar bar) {
        boolean equalTS ;
        
        equalTS = false ;
        if(bar != null) {
            if( (this.measure != null) && (bar.measure != null) ) {
                equalTS = (this.measure.getNumerator() == bar.measure.getNumerator()) ;
                equalTS = equalTS && (this.measure.getDenominator() == bar.measure.getDenominator()) ;
            } else if ( (this.measure == null) && (bar.measure == null) ) {
                equalTS = true ;
            }
        }
        return equalTS ;
    }
    public boolean equals(Object o) {
    	boolean equal ;
    	Bar other ;
    	
    	equal = false ;
    	if( (o != null) && (o.getClass().isInstance(this)) ) {
    		other = (Bar) o ;
    		equal = this.equalTimeSignature(other) ;
    		if(this.measure != null) {
    			equal = equal && (this.measure.equals(other.measure)) ;
    		}
    	}
    		
    		
    	return equal ;
    }
    public int getTopOffset() {
        return (this.displayOptions.TopOffset) ;
    }
    public void setTopOffset(int topOffset) {
        if(topOffset >= 0) {
            this.displayOptions.TopOffset = topOffset ;
        }
    }
    public int getBottomOffset() {
        return (this.displayOptions.BottomOffset) ;
    }
    public void setBottomOffset(int bottomOffset) {
        if(bottomOffset >= 0) {
            this.displayOptions.BottomOffset = bottomOffset ;
        }
    }
    public boolean getExtendTop() {
        return (this.displayOptions.extendTop) ;
    }
    
    public void setExtendTop(boolean value) {
        this.displayOptions.extendTop = value ;
    }
    public boolean getExtendBottom() {
        return (this.displayOptions.extendBottom) ;
    }
    
    public void setExtendBottom(boolean value) {
        this.displayOptions.extendBottom = value ;
    }
    public GPMeasure getMeasure(){
        return (this.measure) ;
    }
    /*
     * When using this method make sure you call Bar.invalidate() ;
     */
    public void setMeasure(GPMeasure m){
        this.measure = m ;
    }
    //Its assumed that measure is not null
    private int calculateTS(Graphics g) {
        int dx, dxAux ;
        int resp ;
        int max ;
        int num ;
        int den ;
        
        resp = -80 ;
        num = measure.getNumerator() ;
        den = measure.getDenominator() ;
        if(g == null ) {
            max = Math.max(num,den) ;
            if(max <= 9)
                resp = 14 ;
            else {
                resp = 22 ;
            }
        }  else {
            this.numerator = "" + num ;
            this.denominator = "" + den ;
            
            dx = g.getFontMetrics(BarPanel.timeSignatureFont).stringWidth(this.numerator) ;
            dxAux = g.getFontMetrics(BarPanel.timeSignatureFont).stringWidth(this.denominator) ;
            resp = Math.max(dxAux,dx) ;
        }
        return (resp) ;
    }
    /**
     * Calculate how mucho to INCrease the WIDTH of a bar because of repeteations
     */
    private int calculateWidthInc() {
        int incW ;
        int numRep ;
        //is ths diameter of a repeat circle..wich causes this component to be wider
        int d ;
        
        incW = 0 ;
        /* since the highest value of LS is currently 10 then d = 5 + 1
         * if we use this exprension..
         *    d = LS/2  + 1 ;
         * then the width of Tablature and Standard bar will not match !!
         */
        d = 5 + 1 ;
        if(this.isFirst) {
            incW = 3 ;
        }
        if(this.isLast) {
            incW += 2 ;
        }
        
        if(this.measure != null) {
            numRep = this.measure.getNumberOfRepetitions() ;
            if( (!this.isLast) && (this.measure.hasDoubleBar) ) {
                incW += 2 ;
            }
            
            if(this.measure.repeatStart){
                if(!this.isFirst) {
                    incW = 3 ;
                }
                incW += d/2 + d ;
            }
            if(numRep > 0) {
                if(!this.isLast) {
                    incW += 2 ;
                }
                incW += d/2 + d ;
            }
            
            if(this.displayTimeSignature) {
                this.TS = this.calculateTS(null);
                incW += this.TS ;
            }
            
        }
        
        return (incW) ;
    }
    /*
     * this method draws as many horizontal LINES, separated by LS pixels.
     */
    
    //VERY IMPORTANT...any change is this method should be reflected on the
    //calculateIncWidth()
    protected void paintMeasure(Graphics g) {
        int i ;
        int x ;
        int w ;
        int y ;
        int y2 ;
        int maxX ;
        //TESTING CLIPPING        Rectangle clip ;
        int dx ;
        int dy ;
        int numRep ;
        int d ; //the diameter of the repeat circle
        int yAux ;
        String aux ;
        int aboveText ;
        
        //TESTING CLIPPING        clip = (Rectangle) g.getClip() ;
        x = 0 ;
        w = this.getWidth() - 1 ;
        maxX = w ;
        aboveText = this.displayOptions.TopOffset - 7 ;
        //Draw the measure number in RED
        g.setColor(Color.RED) ;
        g.setFont(BarPanel.measureNumberFont) ;
        aux = "" + this.measure.getNumber() ;
        g.drawString(aux, 1 ,aboveText) ;
        
        
        g.setColor(this.getForeground()) ;
        //TESTING CLIPPING
        /*
        if(clip != null) {
            if (clip.x > x)  {
                x = clip.x ;
            }
            if(clip.x + clip.width < w) {
                maxX = clip.x+clip.width ;
            }
        }
        */
        
        for(i = 0 ; i < this.BP.LINES; i++) {
            y = this.displayOptions.TopOffset + i*this.displayOptions.LS ;
            //TESTING CLIPPING         if( (clip == null) || ((clip != null) && (y >= clip.y) && (y <= clip.y + clip.height)) ) {
                g.drawLine(x, y, maxX, y) ;
                //TESTING CLIPPING         }
        }
        //Draw the Vertical line of the measure
        y = this.displayOptions.TopOffset ;
        if(this.displayOptions.extendTop) {
            y = 0 ;
        }
        y2 = this.displayOptions.TopOffset + this.BP.H ;
        if(this.displayOptions.extendBottom) {
            y2 = y2 + this.displayOptions.BottomOffset ;
        }
        
        //The line at the left of the Bar
        this.minx = 0 ;
        g.drawLine(0, y, 0, y2) ;
        
        if(this.isFirst) {
            g.drawLine(1, y, 1, y2) ;
            g.drawLine(3, y, 3, y2) ;
            this.minx = 3 ;
        }
        dx = 0 ;
        if(this.isLast) {
            dx = 1 ;
            //The lines that represt the last bar
            g.drawLine(w, y, w, y2 ) ;
            g.drawLine(w-dx, y, w-dx, y2) ;
            g.drawLine(w-dx-2, y, w-dx-2, y2) ;
        }
        
        if(this.measure != null) {
            d = this.displayOptions.LS/2  + 1 ;
            numRep = this.measure.getNumberOfRepetitions() ;
            if( (!this.isLast) && (this.measure.hasDoubleBar) ) {
                g.drawLine(w-2, y, w-2, y2) ;
            }
            
            if(this.measure.repeatStart){
                if(!this.isFirst) {
                    g.drawLine(1, y, 1, y2) ;
                    g.drawLine(3, y, 3, y2) ;
                    this.minx = 3 ;
                }
                yAux = (this.displayOptions.LS*(this.BP.LINES-2))/2 + this.displayOptions.TopOffset ;
                g.fillOval(this.minx + d/2, yAux - d/2 +1 , d,d) ;
                g.fillOval(this.minx + d/2, yAux + this.displayOptions.LS- d/2+ 1, d,d) ;
                this.minx = this.minx + d/2 + d ;
            }
            if(numRep > 0) {
                if(!this.isLast) {
                    g.drawLine(w, y, w, y2) ;
                    g.drawLine(w-2, y, w-2, y2) ;
                }
                yAux = (this.displayOptions.LS*(this.BP.LINES-2))/2 + this.displayOptions.TopOffset ;
                g.fillOval(w - 2 - d - d/2, yAux - d/2 +1 , d,d) ;
                g.fillOval(w - 2 - d - d/2, yAux + this.displayOptions.LS- d/2+ 1, d,d) ;
                //Draw the number of repetitions
                g.setFont(BarPanel.repeatFont) ;
                aux = "" + numRep + "x" ;
                dx = g.getFontMetrics().stringWidth(aux) ;
                g.drawString(aux, w-dx - 3,aboveText) ;
            }
            if(this.displayTimeSignature) {
                y = this.BP.H/2 + this.displayOptions.TopOffset ;
                
                this.minx = this.minx + 5 ;
                g.setFont(BarPanel.timeSignatureFont) ;
                dy = g.getFontMetrics().getAscent() ;
                this.TS = this.calculateTS(g) ;
                
                g.drawString(this.numerator, this.minx , y-1) ;
                g.drawString(this.denominator, this.minx , y + dy - 3) ;
                
                this.minx = this.minx + this.TS ;
            }
        }
    }
    
    /**
     * Returns the current height of this component.
     * this method is preferable to writing
     * <code>component.getBounds().height</code>, or
     * <code>component.getSize().height</code> because it doesn't cause any
     * heap allocations.
     *
     * @return the current height of this component
     */
    public int getHeight() {
        int res ;
        
        res = this.displayOptions.TopOffset + this.BP.H + this.displayOptions.BottomOffset ;
        
        return res ;
    }
    protected abstract int calculateWidth(int inc) ;
    
    public abstract MusicCursorPosition getMusicCursorPosition(int beat) ;
    
    public abstract MusicCursorPosition getMusicCursorPosition(MusicCursor musicCursor) ;
    
    //UNUSED public abstract int getNumBeat(Point point ) ;
    
    public abstract MusicCursorPosition snapX(int x, MusicCursor musicCursor) ;
    /**
     * Returns the current width of this component.
     * this method is preferable to writing
     * <code>component.getBounds().width</code>, or
     * <code>component.getSize().width</code> because it doesn't cause any
     * heap allocations.
     *
     * @return the current width of this component
     */
    public int getWidth() {
        int w ;
        int inc ;
        
        inc = this.calculateWidthInc() ;
        w = this.calculateWidth(inc) ;
        
        return w ;
    }
    
    public Dimension getMinimumSize() {
        Dimension retValue;
        
        retValue = new Dimension(this.getWidth(),this.getHeight()) ;
        return retValue;
    }
    public Dimension getPreferredSize() {
        /*Dimension retValue;
        
        retValue = new Dimension(this.getWidth(),this.getHeight()) ;
        return retValue;
        */
    	return this.getMinimumSize() ;
    }
    

    /**
     * Calls the UI delegate's paint method, if the UI delegate
     * is non-<code>null</code>.  We pass the delegate a copy of the
     * <code>Graphics</code> object to protect the rest of the
     * paint code from irrevocable changes
     * (for example, <code>Graphics.translate</code>).
     * <p>
     * if you override this in a subclass you should not make permanent
     * changes to the passed in <code>Graphics</code>. for example, you
     * should not alter the clip <code>Rectangle</code> or modify the
     * transform. if you need to do these operations you may find it
     * easier to create a new <code>Graphics</code> from the passed in
     * <code>Graphics</code> and manipulate it. Further, if you do not
     * invoker super's implementation you must honor the opaque property,
     * that is
     * if this component is opaque, you must completely fill in the background
     * in a non-opaque color. if you do not honor the opaque property you
     * will likely see visual artifacts.
     *
     * @param gg the <code>Graphics</code> object to protect
     * @see java.awt.Component#paint
     */
    protected void paintComponent(Graphics gg) {
        //METHOD A....        Rectangle clip ;
        Graphics g;
        
        //Copy the graphics object as required above
        g = gg.create() ;

        //METHOD B        
        super.paintComponent(g) ;
        
//      METHOD A....
        /*
        clip = (Rectangle) g.getClip() ;
        //draw the background...this component its opaque !!
        if(clip != null) {
            g.setColor(this.getBackground()) ;
            g.fillRect(clip.x,clip.y,clip.width,clip.height) ;
        }
        else {
            g.fillRect(0,0,this.getWidth(),this.getHeight()) ;
        }
        */
        //draws the lines of the PENTAGRAM, double bars, repeat open, close,
        //double bars
        this.paintMeasure(g) ;
        //finish
        g.dispose() ;
    }
    /**
     * if the maximum size has been set to a non-<code>null</code> value
     * just returns it.  if the UI delegate's <code>getMaximumSize</code>
     * method returns a non-<code>null</code> value then return that;
     * otherwise defer to the component's layout manager.
     *
     * @return the value of the <code>maximumSize</code> property
     */
    public Dimension getMaximumSize() {
        return (new Dimension(this.getWidth(),this.getHeight())) ;
    }
}


