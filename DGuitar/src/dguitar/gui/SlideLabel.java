/*
 * Created on 26/03/2005
 *
 */
package dguitar.gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;

import javax.swing.JFrame;
import javax.swing.JLabel;

import common.Util;
import dguitar.codecs.guitarPro.GPSlide;

/**
 * Creates/Paints different types of SLIDEs between two notes.
 * 
 * @author Mauricio Gracia G.
 *  
 */
public class SlideLabel extends JLabel 
implements OptionsDisplay {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5517409142682835275L;

	private DisplayOptions displayOptions;

    private int typeOfSlide;

    public static final int SLIDE_SHIFT = 1;

    public static final int SLIDE_LEGATO = 2;

    public static final int SLIDE_IN_FROM_BELOW = 3;

    public static final int SLIDE_IN_FROM_ABOVE = 4;

    public static final int SLIDE_OUT_UPWARDS = 5;

    public static final int SLIDE_OUT_DOWNWARDS = 6;

    /* to set the toolTips, this were taken from GPSlide.java */
    public static String[] slidesNames = { "Shift slide", "Legato slide",
            "Slide in from below", "Slide in from above", "Slide out upwards",
            "Slide out downwards" };

    /**The color that is used to paint the Slides*/
    
    private Color color;
    
    private int width ;
    
    private static final int MIN_WIDTH = 5 ;

    /**
     * This constructor exists only to to allow casting and such...
     *  
     */
    public SlideLabel() {
        super();
        this.typeOfSlide = SLIDE_SHIFT;
        this.displayOptions = null;
        this.color = Color.PINK;
    }

    /**
     * Creates a SlideLabel that can be linked to a GPSlide object
     * 
     * @param currentDisplayOptions
     *            the current displayOptions to be used
     */
    public SlideLabel(DisplayOptions currentDisplayOptions, int desiredWidth) {
        super();
        if ( (currentDisplayOptions != null) && (desiredWidth >= MIN_WIDTH)) {
            //the default type of slide ...
            this.typeOfSlide = SLIDE_SHIFT;
            this.width = desiredWidth ;
            
            this.setDisplayOptions(currentDisplayOptions) ;
            
            //TODO...what color ?
            this.color = Color.BLUE ;
            
        } else {
            String msg ;
            if (currentDisplayOptions == null) {
                msg = "displayOptions is null" ;
                
            }else {
                msg = "desiredWidth is <= " + MIN_WIDTH ;
            }
            throw new NullPointerException(msg);
        }
    }

    public Dimension getMinimumSize() {
        Dimension dim;

        dim = new Dimension(this.width, this.displayOptions.LS);

        return dim;
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

    public Point getPoint(int x, int y) {
        return this.getPoint(new Point(x, y));
    }

    /**
     * This allows the correct location of the Slide according to the
     * typeOfSlide
     * 
     * @param anchor
     *            is the point were the slide is anchor to
     * @return a point were the bounds can be set to the anchor is correct
     */
    public Point getPoint(Point anchor) {
        Point p;
        int h;
        int w;
        int deltaY;
        int deltaX;

        /*
         * w = this.getWidth() - 1 ; h = this.getHeight() - 1 ;
         */
        w = this.width - 1;
        h = this.displayOptions.LS - 1;

        p = new Point();
        if ((this.typeOfSlide == SLIDE_IN_FROM_ABOVE)
                || (this.typeOfSlide == SLIDE_IN_FROM_BELOW)) {
            deltaX = -w - 5;
            deltaY = (this.typeOfSlide == SLIDE_IN_FROM_BELOW) ? 0 : -h;
            //deltaY = 0 ;
        } else {
            deltaX = 5;
            deltaY = (this.typeOfSlide == SLIDE_OUT_DOWNWARDS) ? 0 : -h;
        }
        p.x = anchor.x + deltaX;
        p.y = anchor.y + deltaY;

        return p;
    }

    public Point setAnchor(Point anchor) {
        Point resp;

        resp = this.getPoint(anchor);
        this.setLocation(resp);

        return resp;
    }

    public Point setAnchor(int x, int y) {
        Point resp;

        resp = this.getPoint(x, y);
        this.setLocation(resp);

        return resp;
    }

    /**
     * @param aTypeOfSlide
     *            The typeOfSlide to set.
     */
    public void setTypeOfSlide(int aTypeOfSlide) {
        if ((aTypeOfSlide >= SLIDE_SHIFT)
                && (aTypeOfSlide <= SLIDE_OUT_DOWNWARDS)) {
            this.typeOfSlide = aTypeOfSlide;
            if (this.typeOfSlide == SLIDE_LEGATO) {
                this.setSize(this.width, 2 * this.displayOptions.LS);
            }
        } else {
            throw new IllegalArgumentException(
                    "invalid range for a aTypeOfSlide");
        }

    }

    /**
     * @return Returns the typeOfSlide.
     */
    public int getTypeOfSlide() {
        return typeOfSlide;
    }

    private void paintImpl(Graphics g) {
        Point p1;
        Point p2;
        int h;
        int w;

        p1 = new Point();
        p2 = new Point();

        w = this.width - 1;
        h = this.displayOptions.LS - 1;

        p1.x = 0;
        p2.x = w;

        if ((this.typeOfSlide == SLIDE_IN_FROM_ABOVE)
                || (this.typeOfSlide == SLIDE_OUT_DOWNWARDS)) {
            p1.y = 0;
            p2.y = h;
        } else {
            p1.y = h;
            if (this.typeOfSlide == SLIDE_LEGATO) {
                p2.y = h / 2;
            } else {
                p2.y = 0;
            }
        }
        //set the color and paint
        g.setColor(this.color);
        Util.drawLine(g, p1, p2);

        if (this.typeOfSlide == SLIDE_LEGATO) {
            //draw the arc
            g.drawArc(0, 0, w, h / 2, 0, 180);
        }
    }

    //this component is not opaque...
    protected void paintComponent(Graphics gg) {
        Graphics g;

        //Copy the graphics object as required by Java
        g = gg.create();

        super.paintComponent(g);

        paintImpl(g);

        //finish
        g.dispose();
    }

    public static void main(String args[]) {
        JFrame jFrame;
        int i;
        SlideLabel slide;
        DisplayOptions aDO;
        Container pane;
        Point anchor;
        Point p;
        int aux;

        jFrame = new JFrame("Types of Slide Test");
        pane = jFrame.getContentPane();

        pane.setLayout(null); //absolute layout
        aDO = new DisplayOptions();

        anchor = new Point();
        anchor.x = 50;
        anchor.y = 20;
        for (i = SLIDE_SHIFT; i <= SLIDE_OUT_DOWNWARDS; i++) {

            slide = new SlideLabel(aDO,20);
            //slide.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED))
            // ;
            slide.setTypeOfSlide(i);
            slide.setToolTipText(slidesNames[i - 1]);
            slide.setAnchor(anchor);
            /*
             * p = slide.getPoint(anchor) ; slide.setLocation(p) ;
             */
            pane.add(slide);

            anchor.y += 2 * aDO.LS + 5;
        }
        /* random slides */
        anchor.x += 5 * aDO.NS;
        anchor.y = 20;
        for (i = 1; i <= 7; i++) {

            slide = new SlideLabel(aDO,50);
            //slide.setBorder(javax.swing.BorderFactory.createLineBorder(Color.RED))
            // ;
            aux = 1 + (int) (Math.random() * 6);
            slide.setTypeOfSlide(aux);
            slide.setToolTipText(slidesNames[aux - 1]);
            p = slide.getPoint(anchor);
            slide.setLocation(p);
            pane.add(slide);

            anchor.y += 2 * aDO.LS + 5;
        }

        jFrame.setBounds(10, 10, 300, 300);
        jFrame.setVisible(true);
    }

    public void setTypeOfSlide(GPSlide aGPSlide) {
        int type;
        int GPStype;

        GPStype = aGPSlide.getIndex();
        type = -99;
        switch (GPStype) {
        case 0:
            type = SlideLabel.SLIDE_IN_FROM_ABOVE;
            break;
        case 1:
            type = SlideLabel.SLIDE_IN_FROM_BELOW;
            break;
        case 2:
            type = SlideLabel.SLIDE_SHIFT;
            break;
        case 3:
            type = SlideLabel.SLIDE_LEGATO;
            break;
        case 4:
            type = SlideLabel.SLIDE_OUT_DOWNWARDS;
            break;
        case 5:
            type = SlideLabel.SLIDE_OUT_UPWARDS;
            break;

        }
        this.setTypeOfSlide(type);
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
     * @param displayOptions The displayOptions to set.
     */
    public void setDisplayOptions(DisplayOptions displayOptions) {
        this.displayOptions = displayOptions;
        
        //TODO set the size occording to the displayOptons LineSpacing  !!!why ?!?!
        this.setSize(this.width, this.displayOptions.LS);

    }

    /**
     * @return Returns the displayOptions.
     */
    public DisplayOptions getDisplayOptions() {
        return displayOptions;
    }
}