/*
 * BarTablature.java
 *
 * Created on 27 de diciembre de 2004, 12:25 PM
 */

package dguitar.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;
import java.util.Vector;

import javax.swing.JLabel;

import dguitar.codecs.guitarPro.GPBeat;
import dguitar.codecs.guitarPro.GPBendType;
import dguitar.codecs.guitarPro.GPDuration;
import dguitar.codecs.guitarPro.GPDynamic;
import dguitar.codecs.guitarPro.GPEffectsOnNote;
import dguitar.codecs.guitarPro.GPNote;

/**
 * This class extends BarMTP and display the notes using Guitar Tablature
 * representation
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public class BarTablature extends BarMTP {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3566580364024788490L;

	/*
     * How much space on X-axis to the right of the note is the Effect On Note
     * draw
     */
    private static final int EONX = 5;

    /**
     * a collection of Effects On Note Labels
     */
    private List<JLabel> EONlabels;

    /**
     * a collection of SilenceLabelS
     */
    private List<JLabel> SLabels;

    /**
     * Creates a new instance of BarTablature, related the BarPanel BP
     */
    public BarTablature(DisplayOptions dco, BarPanel BP) {
        super(dco, BP);
        commonConstructor();
    }

    /**
     * add a Silence simbol using the currentX position, and duration
     */
    private void addSilence(int currentX, GPDuration d) {
        JLabel SilenceLabel;

        SilenceLabel = new JLabel(DGuitar.Silences[d.getIndex()]);
        locateSilence(SilenceLabel, currentX);
        SilenceLabel.setVisible(true);
        this.add(SilenceLabel);
        this.SLabels.add(SilenceLabel);

    }

    /**
     * Calculate how High is a Instrument String located
     */
    protected int calculateHeightOfString(int s) {
        int y;

        //The higher the value of the string the closest it is to the top
        y = this.getDisplayOptions().TopOffset + this.BP.H - s * this.getDisplayOptions().LS;

        //string are represented in a 7 string scale
        //this line below locates the notes acoording to that
        y = y + (7 - this.BP.LINES) * this.getDisplayOptions().LS;

        //center the text in the middle verticly
        y = y + fontAscent / 2;

        return (y);
    }

    private void commonConstructor() {
        this.SLabels = new Vector<JLabel>(0, 1);
        this.EONlabels = new Vector<JLabel>(0, 1);
    }

    private JLabel createLabelForIndex(int pos, int x, int y,
            GPEffectsOnNote EON, GPBeat beat) {
        int index;
        SlideLabel SL;
        JLabel resp;
        int width;

        index = -1 * pos;
        resp = null;
        //if the index indicates that it is a SLIDE LABEL
        if ((index >= 1) && (index <= 6)) {
            width = this.calculateWidthOfBeat(beat);
            SL = new SlideLabel(this.getDisplayOptions(), width);
            SL.setTypeOfSlide(EON.slide);
            SL.setAnchor(x, y);
            resp = SL;
        }

        return resp;
    }

    /**
     * add a Effect On Note using the currentX position, and duration
     */
    private void eonAdd(int currentX, int currentY, GPEffectsOnNote EON,
            GPBeat beat) {
        JLabel EONlabel;
        int pos;

        pos = index(EON);

        //if it is a GIF EON
        if (pos >= 0) {
            EONlabel = new JLabel();

            EONlabel.setIcon(DGuitar.EONs[pos]);
            eonLocate(EONlabel, currentX, currentY, EON,beat);
        } else {
            //createLabel alse sets the location of the SLIDES
            EONlabel = createLabelForIndex(pos, currentX, currentY, EON, beat);
        }

        //DEBUG
        //System.err.println("pos = " + pos + " x = " + currentX + "y = " +
        // currentY + "EONLabel = " + EONlabel ) ;
        if (pos == 0) {
            //this is used by the paintEffects method below
            EONlabel.setName("?");
            //DO..are the DisplayOptions for this object
            //This if evaluates if the Question marks simbol goes away
            EONlabel.setVisible(getDisplayOptions().displayUnsupportedEffects);
        } else {
            //		  this might be needed in the future...
            EONlabel.setName("" + pos);
            EONlabel.setVisible(true);
        }
        //set the ToolTip text to the String representation of the EffecOnNote
        EONlabel.setToolTipText(EON.toString());
        this.add(EONlabel);
        this.EONlabels.add(EONlabel);

    }

    /**
     * This method sets the position of a Effects on Note label
     */
    private void eonLocate(JLabel EONLabel, int currentX, int currentY,
            GPEffectsOnNote EON, GPBeat beat) {

        Dimension dim;
        Rectangle rect;
        Point p;
        Point aux;
        SlideLabel SL;

        dim = EONLabel.getPreferredSize();
        p = new Point(currentX, currentY);
        SL = new SlideLabel();

        //If it is a SLIDE LABEL
        //if(EONLabel.getClass().isInstance(SL)) {
        if (SL.getClass().isInstance(EONLabel)) {
            SL = (SlideLabel) EONLabel;
            aux = SL.setAnchor(p);
            SL.setWidth(this.calculateWidthOfBeat(beat)) ;
            //DEBUG
            //System.err.println("p = " + p + "aux = " + aux + "SlideLabel:" +
            // SL) ;
        }
        //It is a GIF label
        else {
            aux = eonPosition(EON, p, dim);
            rect = new Rectangle(aux.x, aux.y, dim.width, dim.height);
            EONLabel.setBounds(rect);

        }

    }

    /**
     * Calculates the effects position.
     * 
     * @param EON -
     *            The effects on note to be located
     * @param current -
     *            the current position
     * @param dim -
     *            the size of the Label that holds the EONicon
     * @return a Point with the position.
     */
    private Point eonPosition(GPEffectsOnNote EON, Point current, Dimension dim) {

        int whichEON;
        Point res;
//UNUSED        SlideLabel SL;

        whichEON = index(EON);
        //IF this value is not changed...something went wrong ;
        res = current;

        //	LET -RING
        if (whichEON == 1) {
            res.x = current.x + EONX;
            res.y = current.y - dim.height / 2;
        }
        //UNSUPPORTED EFFECTS have index 0 (whichEON)
        //and are located at the same position as BENDS
        //at this moment are 5 Bends as GIFs
        else if ((whichEON == 0) || ((whichEON >= 2) && (whichEON <= 6))) {
            res.x = current.x + EONX;
            res.y = current.y - 3 * dim.height / 2 + 2;
        }
        //painted EONs have < 0 index values
        else {

        }

        //DEBUG
        //System.err.println("whichEON is:" + whichEON + " for:" + EON) ;

        return res;
    }

    public boolean equals(Object o) {
        BarTablature other;
        boolean resp;

        resp = false;
        if ((o != null) && (o.getClass().isInstance(this))) {
            other = (BarTablature) o;
            resp = ((this.mtp == null) && (other.mtp == null)) || this.mtp.equals(other.mtp);
        }

        return resp;
    }

    private int index(GPBendType bendType) {
        int i;

        i = -1;
        if (bendType != null) {
            if (bendType.equals(GPBendType.BEND)) {
                i = 0;
            } else if (bendType.equals(GPBendType.BEND_RELEASE)) {
                i = 1;
            } else if (bendType.equals(GPBendType.BEND_RELEASE_BEND)) {
                i = 2;
            } else if (bendType.equals(GPBendType.PREBEND)) {
                i = 3;
            } else if (bendType.equals(GPBendType.PREBEND_RELEASE)) {
                i = 4;
            } else {
                //TODO...there are more bends, create the GIFs
            }
        }
        return i;
    }

    /**
     * Returns an index for a array given a Effects On Note
     * 
     * @param EON
     * @return an index for a array given a Effects On Note
     */
    private int index(GPEffectsOnNote EON) {
        int i;

        i = -1;
        if (EON != null) {
            i = 0;
            if (EON.letRing) {
                i = 1;
            } else if (EON.bend != null) {
                i = 2 + index(EON.bend.getType());
            } else if (EON.slide != null) {
                i = -1 * (1 + EON.slide.getIndex());
            } else {
                //	TODO CONTINUE WORKING on this...see bottom of FILE
            }

        }
        return i;
    }

    /**
     * This method sets the position of the silence
     */
    private void locateSilence(JLabel SilenceLabel, int currentX) {
        Dimension dim;
        int y;
        int x;
        Rectangle rect;

        dim = SilenceLabel.getPreferredSize();
        y = this.getDisplayOptions().TopOffset + this.BP.H / 2 - dim.height / 2;
        x = currentX - dim.width / 2;
        rect = new Rectangle(x, y, dim.width, dim.height);
        SilenceLabel.setBounds(rect);
    }

    protected void paintEffects(Graphics g, int currentX, int y,
            GPEffectsOnNote EON, int contEON, GPBeat beat) {
        JLabel eon;

        if (EON != null) {
            Color prevColor;

            prevColor = g.getColor();

            //the first time the labels are added
            if ((this.addEONs) && (EON != null)) {
                eonAdd(currentX, y, EON, beat);
            }
            //the next times...labels are relocated
            else {
                eon = (JLabel) EONlabels.get(contEON);
                //If the EON label is a unsupported effect..paint the "?"
                // simbol
                if (eon.getName().equals("?")) {
                    //DO..are the DisplayOptions for this object
                    eon.setVisible(getDisplayOptions().displayUnsupportedEffects);
                }
                eonLocate(eon, currentX, y, EON,beat);
            }
            g.setColor(prevColor);
        }
    }

    /**
     * This method paint the notes using g, currentX, y and a GPNote
     */

    protected void paintNote(Graphics g, int currentX, int y, GPNote note) {
        String aux;
        int fret;
        int x;
        int pos;
        Color noteColor;
        Color prevColor;
        GPDynamic dynamic;
        prevColor = g.getColor();

        //now the actual fret/note
        fret = note.getFretNumber() ;//OLD getNumberOfFret();
        aux = "" + fret;
        //paint the background the space arround the displayed fret
        x = paintSpaceAroundFret(g, aux, currentX, y);
        /*
         * Most of the colors are actually being selected by beat info..see
         * barMTP
         */
        if (this.getDisplayOptions().coloringForFrets == DisplayOptions.RELATED_TO_DURATION) {
            //set the color of the fret according to the note duration
            if (note.duration != null) {
                pos = note.duration.getIndex();
                noteColor = this.getDisplayOptions().fretColors.getColor(pos);
                g.setColor(noteColor);
            }
        } else if (this.getDisplayOptions().coloringForFrets == DisplayOptions.RELATED_TO_DYNAMIC) {
            //set the color of the fret according to the note dynamic
            dynamic = note.getDynamic();
            if (dynamic != null) {
                //since index 0 is invalid..the the first valid dynamic is 1
                //but POS is used to access a array so....
                pos = dynamic.getIndex() - 1;

                noteColor = this.getDisplayOptions().fretColors.getColor(pos);
                g.setColor(noteColor);
            }
        }
        //ELSE
        //the color that Graphics G has is used, see BarMTP

        //Draw the fret
        g.drawString(aux, x + 1, y - 1);
        g.setColor(prevColor);
    }

    /**
     * Paints the Rhytm Horizonal Lines
     */
    protected void paintRhythmHL(Graphics g, int x, int lowest, int n,
            GPBeat prev) {
        int i;
        int aux;
        int x2;
        Color prevColor;

        prevColor = g.getColor();
        //there is a previous beat
        if (prev != null) {
            for (i = 0; i <= n; i++) {
                //3 is the number of pix. between horizontal rhytm lines
                aux = lowest - i * 3;
                g.setColor(colorPrev);
                if (i <= lastNumHLines) {
                    x2 = x - calculateWidthOfBeat(prev) + this.getDisplayOptions().NS / 2 + 1;
                } else {
                    x2 = x - this.getDisplayOptions().NS / 2;
                }
                g.drawLine(x, aux - 1, x2, aux - 1);
                g.drawLine(x, aux, x2, aux);
            }
        } else {
            for (i = 0; i <= n; i++) {
                //3 is the number of pix. between horizontal rhytm lines
                aux = lowest - i * 3;
                g.setColor(colorNext);
                x2 = x + this.getDisplayOptions().NS / 2;
                g.drawLine(x, aux - 1, x2, aux - 1);
                g.drawLine(x, aux, x2, aux);
            }
        }
        g.setColor(prevColor);
    }

    /**
     * This method paints a Silence
     */
    protected void paintSilence(Graphics g, int currentX, GPBeat beat,
            int contSilences) {
        Color prevColor;

        prevColor = g.getColor();

        if (this.addSilence) {
            addSilence(currentX, beat.getDuration());
        } else {
            locateSilence((JLabel) SLabels.get(contSilences), currentX);
        }
        g.setColor(prevColor);
    }

    /**
     * ANY PAINT method has to do this
     * 
     * Color prevColor ;
     * 
     * prevColor = g.getColor() ;
     * 
     * ///THE ACTUAL PAINTING
     * 
     * g.setColor(prevColor) ;
     *  
     */

    /**
     * Fill a rectangle with the background color of the size of the text to be
     * displayed
     */
    private int paintSpaceAroundFret(Graphics g, String aux, int x, int y) {
        int dx;
        int newx;
        Color prevColor;

        prevColor = g.getColor();

        g.setColor(this.getBackground());
        //calculate the widht of the text
        dx = g.getFontMetrics().stringWidth(aux);
        newx = x - dx / 2; //to center the text
        g.fillRect(newx, y - fontAscent, dx + 2, fontAscent + 1);

        g.setColor(prevColor);
        return newx;
    }
}
/*
 * See Effects.txt
 */

