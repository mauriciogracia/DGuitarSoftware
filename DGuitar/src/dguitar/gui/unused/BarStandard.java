/*
 * Bar.java
 *
 * Created on 27 de diciembre de 2004, 12:25 PM
 */

package dguitar.gui.unused;


import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import dguitar.codecs.guitarPro.GPBeat;
import dguitar.codecs.guitarPro.GPEffectsOnNote;
import dguitar.codecs.guitarPro.GPNote;
import dguitar.gui.BarMTP;
import dguitar.gui.BarPanel;
import dguitar.gui.DisplayOptions;
import dguitar.gui.MusicCursor;
import dguitar.gui.MusicCursorPosition;
/**
 * This class extends BarMTP, it represents a Bar with a StandardNotation display
 * 
 * This class still exists in order to be re-used again in not so distant future.
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public class BarStandard extends BarMTP implements MouseListener,MouseMotionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = -6853214537506589696L;

	private Vector<NoteComponent> Notes ;
    
    private Point p ;
    private Point pAnt  ;
    private boolean avoidNoteClipping ;
    private boolean showSnap ;
    
    private Rectangle snapRect ;
    
    /** Creates a new instance of Bar */
    public BarStandard(DisplayOptions cdo, BarPanel BP) {
        super(cdo,BP) ;
        this.pAnt = null ;
        this.avoidNoteClipping = true ;
        this.showSnap = true ;
        /*
        this.LINES = 5 ;
        this.LS = 7 ;
        this.H = (LINES-1)*LS ;
         */
        this.Notes = new Vector<NoteComponent>(0,1) ;
        this.addMouseListener(this) ;
        this.addMouseMotionListener(this) ;
    }
    //UNUSED
    /*
    private void paintNotes(Graphics g) {
        NoteComponent n ;
        int i ;
        
        
        //Consider using the clip area to determine which notes to draw ?
        for(i = 0 ; i < Notes.size() ; i++) {
            n = (NoteComponent) Notes.elementAt(i) ;
            g.setColor(this.getForeground()) ;
            n.paint(g) ;
        }
        
    }
    */
    public NoteComponent findNote(Point pos) {
        NoteComponent n ;
        int i ;
        boolean found ;
        
        n = null ;
        found = false ;
        for(i = 0 ; (i < Notes.size()) && (!found) ; i++) {
            n = (NoteComponent) Notes.elementAt(i) ;
            found = n.p.equals(pos) ;
        }
        if(!found)
            n = null ;
        return n ;
    }
    
    public MusicCursorPosition snapX(int x, MusicCursor ms) {
    	return null ;
    }
    public Point snap(Point pos, Point rel) {
        int snapX ;
        int snapY ;
        
        
        //TODO this snap should consider if the displayTimeSignature is true, currently it asumes it does
        Point resp = new Point();
        
        snapX = 0 ; //for termparry compilation...removed and correct line below
        //snapX = (pos.x) / Bar.NS ;
        snapY = 2*(pos.y-this.getDisplayOptions().TopOffset) / this.getDisplayOptions().LS ;
        //resp.x =  snapX * Bar.NS ;
        resp.y =  (snapY * this.getDisplayOptions().LS)/2 + this.getDisplayOptions().TopOffset;
        
        if(rel != null) {
            //This variable indicates the beat
            rel.x = snapX ;
            //This variable indicates the offset from the MIDDLE C
            rel.y = 4*(this.getDisplayOptions().TopOffset + this.BP.H + this.getDisplayOptions().LS - pos.y)/this.getDisplayOptions().LS ;
        }
        
        return resp ;
    }
    
    private void drawSnap(Point pp) {
        Graphics g ;
        
        
        g = this.getGraphics() ;
        //g.setColor(BP.getSnapColor()) ;
        this.snapRect = new Rectangle() ;
        
        this.snapRect.x = pp.x-NoteComponent.RX ;
        this.snapRect.y = pp.y-NoteComponent.RY ;
        
        this.snapRect.width = 2*NoteComponent.RX ;
        this.snapRect.height = 2*NoteComponent.RY ;
        
        g.drawRect(snapRect.x, snapRect.y, snapRect.width, snapRect.height) ;
    }
    
    public void mouseClicked(MouseEvent mouseEvent) {
        Point newP ;
        Point newPRelative ;
        NoteComponent n ;
        Integer aux ;
        String str ;
        /*Graphics g ;
        Rectangle r ;
        
        
        r = this.getBounds() ;
        g = this.getGraphics() ;
        */
        p = mouseEvent.getPoint() ;
        
        newPRelative = new Point() ;
        newP = snap(p,newPRelative) ;
        if( (avoidNoteClipping) && (newP.y <= NoteComponent.H) && (pAnt != null) ) {
            //Use last position
            newP = pAnt ;
        }
        n = findNote(newP) ;
        if(n != null){
            Notes.remove(n) ;
            this.repaint() ;
        } else {
            /*
            if( (avoidNoteClipping) && (newP.y <= Note.LY) && (pAnt != null) ) {
                //Paint the note were the cursor was for the last time
                newP = pAnt ;
            }
             */
            //This is to avoid drawing to near to the TOP..if we dont check this will crop the notes.
            if(newP.y > NoteComponent.H) {
                if( (newP.x >  NoteComponent.W) && (newP.x < this.getWidth() - NoteComponent.W) )  {
                    str = this.getCursor().getName() ;
                    aux = new Integer(str) ;
                    n = new NoteComponent(newP,aux.intValue()-1,newPRelative) ;
                    Notes.add(n) ;
                    this.repaint() ; //to paint the note that was just added??
                /*r.width = this.getWidth() ;
                this.setBounds(r) ;
                this.getParent().validate() ; //invalidate only current bar and the ones on the right
                 */
                        /*if(!this.changed) {
                this.changed = true ;
                         */
                }
            }
        }
    }
    
    public void mouseEntered(MouseEvent mouseEvent) {
    }
    
    public void mouseExited(MouseEvent mouseEvent) {
        //Graphics g ;
        
        //This is to clear the Snap when the mouse leaves the component.
        //g = this.getGraphics() ;
        //clear the snap for this component
        if(pAnt != null) {
            //The clip area is the size of the snapRectangle
            if(showSnap) {
                //g.setClip(snapRect.x, snapRect.y, snapRect.width+1, snapRect.height+1) ;
                this.repaint() ;
                pAnt = null ;
            }
        }
        
    }
    
    public void mousePressed(MouseEvent mouseEvent) {
    }
    
    public void mouseReleased(MouseEvent mouseEvent) {
    }
    
    public void mouseDragged(MouseEvent mouseEvent) {
    }
    public void mouseMoved(MouseEvent mouseEvent) {
        Graphics g ;
        Point newP ;
        Point newPRelative ;
        
        
        p = mouseEvent.getPoint() ;
        newPRelative = new Point() ;
        newP = snap(p,newPRelative) ;
        //This is to avoid drawing to near to the TOP..this will crop the notes.
        
        if( (!avoidNoteClipping) || (avoidNoteClipping) && (newP.y >  NoteComponent.H)) {
            if( (newP.x >  NoteComponent.W) && (newP.x < this.getWidth() - NoteComponent.W) )  {
                if(!newP.equals(pAnt) ) {
                    g = this.getGraphics() ;
                    if(pAnt != null) {
                        //The clip area is the size of the snapRectangle
                        if(showSnap) {
                            g.setClip(snapRect.x, snapRect.y, snapRect.width+1, snapRect.height+1) ;
                            //TODO paint method should not be called directly..use repaint.
                            this.paint(g) ;
                        }
                    }
                    /*
                    if(DGuitar.midi != null) {
                        DGuitar.midi.play(60 + newPRelative.y) ; //60 is MIDDLE C
                    }
                     */
                    if(showSnap) {
                        drawSnap(newP) ;
                    }
                    pAnt = newP ;
                }
            }
        }
    }
    //THIS METHOD WAS COPIED FROM BARTABLATURE...MODIFY
    
    public int calculateHeightOfString(int s) {
        int y ;
        
        //The higher the value of the string the closest it is to the top
        y = this.getDisplayOptions().TopOffset + this.BP.H - s*this.getDisplayOptions().LS ;
        
        //string are represented in a 7 string scale
        //this line below locates the notes acoording to that
        y = y + (7 - this.BP.LINES)*this.getDisplayOptions().LS ;
        
        //center the text in the middle verticly
        y = y + fontAscent/2 ;
        
        return (y) ;
    }
    //THIS METHOD WAS COPIED FROM BARTABLATURE...MODIFY
    public void paintRhythmHL(Graphics g, int x, int lowest, int n, GPBeat prev) {
        int i ;
        int aux ;
        int x2 ;
        
        aux = 0 ;
        x2 = x ;
        //there is a previous beat
        if(prev != null) {
            for(i = 0; i <= n; i++) {
                //3 is the number of pix. between horizontal rhytm lines
                aux = lowest-i*3 ;
                g.setColor(colorPrev) ;
                if(i <= lastNumHLines) {
                    x2 = x - calculateWidthOfBeat(prev) + this.getDisplayOptions().NS/2 + 1;
                } else {
                    x2 = x - this.getDisplayOptions().NS/2 ;
                }
            }
        } else {
            for(i = 0; i <= n; i++) {
                //3 is the number of pix. between horizontal rhytm lines
                aux = lowest-i*3 ;
                g.setColor(colorNext) ;
                x2 = x + this.getDisplayOptions().NS/2 ;
                
            }
        }
        g.drawLine(x , aux-1 ,x2, aux-1) ;
        g.drawLine(x , aux   ,x2, aux) ;
    }
    public void paintNote(Graphics g,int x,int y,GPNote note) {
        
    }
    public void paintSilence(Graphics g, int currentX, GPBeat beat, int contSilences) {
        
    }
    public void paintEffects(Graphics g, int currentX, int y, GPEffectsOnNote EON, int contEON, GPBeat beat) {
    
    }
}