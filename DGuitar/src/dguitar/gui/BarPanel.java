/*
 * BarPanel.java
 *
 * Created on 27 de diciembre de 2004, 01:26 PM
 */

package dguitar.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;

import dguitar.codecs.guitarPro.GPMeasure;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;
import dguitar.codecs.guitarPro.GPTrack;

/**
 * A BarPanel might contains many Bar object inside
 * 
 * Those Bar Objects can be BarTablature, BarStandar or any other compent that
 * extend Bar
 * 
 * @author Mauricio Gracia Gutiérrez
 *  
 */
public class BarPanel extends JPanel
implements OptionsDisplay {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2650186040549931521L;

	protected static Font repeatFont;

    protected static Font notesFont;

    protected static Font timeSignatureFont;

    protected static Font measureNumberFont;

    protected static Font graceNoteFont;

    //UNUSED private GPTrack track;

    public int LINES;

    public int H;

    private DisplayOptions displayOptions ;

    private Vector<Bar> barVector;

    /**
     * Creates an empty barPanel used for casting or other operations
     *
     */
    public BarPanel() {
        super();
    }
    /**
     * Creates new BarPanel that can contain BarTablature, BarStandar components
     */
    public BarPanel(DisplayOptions currentDisplayOptions) {
        super();
        if (currentDisplayOptions != null) {
            this.displayOptions = currentDisplayOptions;

            this.setBackground(Color.WHITE);
            this.setForeground(Color.BLACK);

            initComponents();
            this.setRepeatFont(new Font("Arial", Font.BOLD + Font.ITALIC, 12));
            this.setNotesFont(new Font("Arial", Font.BOLD, 11));
            this
                    .setTimeSignatureFont(new Font("ITCCenturyBook", Font.BOLD,
                            18));
            this.setMeasureNumberFont(new Font("Arial", Font.PLAIN, 9));
            //TODO implement grace notes to test this font
            this.setGraceNoteFont(new Font("Arial", Font.PLAIN, 9));

            this.barVector = new Vector<Bar>(0, 1);
        } else {
            throw new NullPointerException(
                    "currentDisplayOptions display options passed to the BarPanel are null");
        }
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
    public MusicCursorPosition getMusicCursorPosition(int numMeasure, int numBeat) {
        Bar bar ;
        MusicCursorPosition MCP ;
        Point p;
        
        MCP = null ;
        if( (numMeasure >= 1) && (numMeasure <= this.barVector.size()) ) {
            bar = (Bar) this.barVector.get(numMeasure-1) ;
            MCP = bar.getMusicCursorPosition(numBeat) ;
            p = MCP.getPoint() ;
            p.x = p.x + bar.getX() ;
            MCP.setPoint(p) ;
        }
        
        return MCP ;
    }
    
    public MusicCursorPosition getMusicCursorPosition(int numMeasure,MusicCursor musicCursor) {
        Bar bar ;
        MusicCursorPosition MCP ;
        Point p;
        
        MCP = null ;
        if( (numMeasure >= 1) && (numMeasure <= this.barVector.size()) ) {
            bar = (Bar) this.barVector.get(numMeasure-1) ;
            MCP = bar.getMusicCursorPosition(musicCursor) ;
            p = MCP.getPoint() ;
            p.x = p.x + bar.getX() ;
            MCP.setPoint(p) ;
        }
        
        return MCP ;
    }


    public MusicCursorPosition snap(Point p, MusicCursor musicCursor) {
        Component comp;
        Bar bar;
        Point newP;
        Point aux;
        int barX;
        MusicCursorPosition MCP;
        
        comp = this.getComponentAt(p);
        //TODO chec if the object is a BAR subclass should go here ?
        bar = (Bar) comp;
        barX = bar.getX();
        MCP = bar.snapX(p.x - barX, musicCursor);

        aux = MCP.getPoint();
        newP = new Point(barX + aux.x, aux.y);
        MCP.setPoint(newP);

        return MCP;
    }

    private void setLines(int value) {
        this.LINES = value;
        this.H = (this.LINES - 1) * this.displayOptions.LS;
    }

    public void setTrack(GPTrack t) {
        if (t != null) {
//        	UNUSEDthis.track = t;
            this.setLines(t.getNumberOfStrings());
        }
    }

    /*
     * gets the current noteSpacing for this BARPANEL
     */
    public int getNoteSpacing() {
        return this.displayOptions.NS;
    }

    /*
     * Sets the noteSpacing for this BARPANEL
     */
    public void setNoteSpacing(int ns) {
        if (ns > 0) {
            this.displayOptions.NS = ns;
        }
    }

    public void setRepeatFont(Font f) {
        repeatFont = f;
    }

    public void setNotesFont(Font f) {
        notesFont = f;
    }

    public void setTimeSignatureFont(Font f) {
        timeSignatureFont = f;
    }

    public void setMeasureNumberFont(Font f) {
        measureNumberFont = f;
    }

    public void setGraceNoteFont(Font f) {
        graceNoteFont = f;
    }

    public void setMeasures(List<GPMeasure> Measures) {
        Iterator<Bar> itBar;
        Iterator<GPMeasure> itMeasure;
        Bar barPrev;
        Bar bar;
        GPMeasure m;

        if (Measures != null) {
            barPrev = null;

            if (Measures.size() == this.barVector.size()) {
                itMeasure = Measures.iterator();
                itBar = this.barVector.iterator();
                while (itMeasure.hasNext()) {
                    bar = (Bar) itBar.next();
                    m = (GPMeasure) itMeasure.next();
                    bar.setMeasure(m);
                    //	If the signature does not change...dont display it
                    bar.displayTimeSignature = !bar.equalTimeSignature(barPrev);
                    barPrev = bar;
                }
                //DEBUG System.err.println(Measures.size() + "measures set");
            } else {
                System.err
                        .println("The number of measures and number of bars does not match");
                //todo throw somenthing..or what ?
            }
        }
    }

    public void setMeasureTrackPair(int measure, GPMeasureTrackPair mtp) {
        BarMTP BMTP;

        if (mtp != null) {
            if (measure < this.barVector.size()) {
                try {
                    BMTP = (BarMTP) this.barVector.get(measure);
                    BMTP.setMeasureTrackPair(mtp);
                } catch (ClassCastException CCE) {
                    System.err
                            .println("setMeasureTrackPair must be called on a BarPanel that contains BarMTP objects");
                }
            }
        }
    }

    /**
     * Get the Bar located at measure
     */
    public Bar getBar(int measure) {
        Bar bar;

        bar = null;
        try {
            bar = (Bar) this.barVector.get(measure);
        } catch (ClassCastException CCE) {
            System.err
                    .println("Barpanel.barVector cotains en element that could not be casted to Bar !!");
        }

        return bar;
    }

    public void addBar(Bar bar) {
        if (bar != null) {
            /*
             * TODO..when the DisplayOptions class was introduce..this was
             * commented BMTP.setExtendTop(extendTop) ;
             * BMTP.setExtendBottom(extendBottom) ;
             */
            //new
            this.barVector.add(bar);
            this.add(bar);
        } else {
            //DEBUG System.err.println("tried to add a null bar");
        }
    }

    public int getTopOffset() {
        return this.displayOptions.TopOffset;
    }

    /**
     * Set the TopOffset of all the Bars in this BarPanel caller of this method
     * should call validate or repaint() as neccesary
     */
    public void setTopOffset(int topOffset) {
        Bar bar;
        Iterator<Bar> it;

        if (topOffset > 0) {
            this.displayOptions.TopOffset = topOffset;
            it = this.barVector.iterator();
            while (it.hasNext()) {
                bar = (Bar) it.next();
                bar.setTopOffset(topOffset);
            }
        }
    }

    public int getBottomOffset() {
        return this.displayOptions.BottomOffset;
    }

    /**
     * Set the Bottom Offset of all the Bars in this BarPanel caller of this
     * method should call validate or repaint() as neccesary
     */

    public void setBottomOffset(int bottomOffset) {
        Bar bar;
        Iterator<Bar> it;

        if (bottomOffset > 0) {
            it = this.barVector.iterator();
            this.displayOptions.BottomOffset = bottomOffset;

            while (it.hasNext()) {
                bar = (Bar) it.next();
                bar.setTopOffset(bottomOffset);
            }
        }
    }

    public boolean getExtendBottom() {
        return this.displayOptions.extendBottom;
    }

    /**
     * Extend the measure lines to the bottom The caller should call validate or
     * repaint()
     */

    public void setExtendBottom(boolean value) {
        Bar bar;
        Iterator<Bar> it;

        it = this.barVector.iterator();
        this.displayOptions.extendBottom = value;

        while (it.hasNext()) {
            bar = (Bar) it.next();
            bar.setExtendBottom(value);
        }
    }

    public boolean getExtendTop() {
        return this.displayOptions.extendTop;
    }

    /**
     * Extend the measure lines to the top The caller should call validate or
     * repaint()
     */
    public void setExtendTop(boolean value) {
        Bar bar;
        Iterator<Bar> it;

        it = this.barVector.iterator();
        this.displayOptions.extendTop = value;

        while (it.hasNext()) {
            bar = (Bar) it.next();
            bar.setExtendTop(value);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
        setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 0, 0));

    }//GEN-END:initComponents
}