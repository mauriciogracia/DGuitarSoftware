package dguitar.gui.unused;
/*
 * Notes.java
 *
 * Created on 6 de diciembre de 2004, 18:41
 */


import java.awt.Graphics;
import java.awt.Point;



/**
 *
 * @author  mgracia
 */
public class NoteComponent {
    /**The duration of the note (0 = whole note, 1 = half note, 2=quarter note...*/
    int duration ;
    
    /**The position in pixels relative to the BarComponent*/
    public Point p ;
    
    /**The position of the note, where X coordinate is the beat, and Y is how many lines below*/ 
    public Point relative ;
    
    /**The radio in X axis of the note oval*/
    public static final int RX = 4 ; //dont change this 4
    
    /**The radio in Y axis of the note oval*/
    public static final int RY = 3 ; //dont change this 3
    
    /**The size of the COLA both in X and Y axis*/
    public static final int L = 6 ; //dont change this 6
    public static final int LY = 6*RY ; //dont change this 6*RY
    public static final int INC = 2 ; //increment (2)
    public static final int Q = 4 ; //separation between COLAS (4)
    public static final int W = 2*RX + L ; //max width of the note
    public static final int H = RY + LY + 4*INC ;  //max height of the note
    
    /** Creates a new instance of Notes */
    public NoteComponent(Point pp, int i, Point relativePos) {
        p = pp ;
        duration = i ;
        relative = relativePos ;
    }
    
    
    public void paint(Graphics g) {
        int x ;
        int y ;
        int i ;

        
        if(this.duration <= 1) {
            x = this.p.x-RX ;
            y = this.p.y-RY ;
            g.drawArc(x,y+1,2*RX, 2*RY-1, 0, 360) ;
        }
        if(this.duration >= 1) {
            x = this.p.x + RX ;
            g.drawLine(x,  this.p.y, x, this.p.y-LY) ;
        }
        
        if(duration >= 2) {
            x = this.p.x - RX ;
            y = this.p.y - RY ;
            
            g.fillOval(x,y,2*RX+2, 2*RY+2) ;
            if(duration >=3) {
                
                x = this.p.x + RX ;
                y = this.p.y-LY ;
                g.drawLine(x, y-(duration-1)*INC, x, y+L) ;
                y = y-(duration-2)*INC ;
                for(i = 0 ; i < (duration-2) ; i++) {
                    g.drawLine(x, y+i*Q, x+L, y+i*Q+L ) ;
                    g.drawLine(x, y+i*Q+1, x+L, y+i*Q+1+L ) ;
                }
                x = this.p.x + RX + L ;
                g.drawLine(x, y+i*Q+L, x, y+L) ;
            }
        }
    }
    /*
    public boolean equals(Object n)
    {
        boolean equal ;
        NoteComponent other ;
        
        equal = false ;
        if (n.getClass().isInstance(this)) {
            other = (NoteComponent) n;
            equal = (this.which = other.which) && (this.relative.equals(other.relative)) ;
        }
        return equal ;
    }
     */
}

/*    public boolean isCotained(Rectangle rect) {
        boolean contained;
        int top ;
        Point aux = new Point();
        
        
        //Cheeck the left-top corner
        aux.x = this.p.x-RX-1 ;
        if(this.which == 0 )
            top = this.p.y-RY-1 ;
        else
            top = this.p.y-LY ;
        aux.y = top ;
        contained = rect.contains(aux) ;
        if(!contained) {
            //Cheeck the left-bottom corner
            aux.y = this.p.y+RY+1 ;
            contained = rect.contains(aux) ;
            if(!contained) {
                //Cheeck the right-bottom corner
                aux.x = this.p.x+RX+1 ;
                contained = rect.contains(aux) ;
                if(!contained) {
                    //Cheeck the right-top corner
                    aux.y = top ;
                    contained = rect.contains(aux) ;
                }
                
            }
            
        }
        return(contained) ;
    }
 */

