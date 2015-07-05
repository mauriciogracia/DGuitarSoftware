package dguitar.gui;

/*
 * TrackPanel.java
 *
 * Created on 5 de diciembre de 2004, 11:25 PM
 */


import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JLayeredPane;

import dguitar.codecs.guitarPro.GPColor;
import dguitar.codecs.guitarPro.GPMeasure;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;
import dguitar.codecs.guitarPro.GPTrack;

/**
 * 
 * @author Mauricio Gracia Gutiérrez
 */


//public class TrackPanel extends JLayeredPane implements Scrollable, MouseListener {
public class TrackPanel extends JLayeredPane implements MouseListener {
    //Class Constants

    //Class variables

    //Instance variables

    /**
	 * 
	 */
	private static final long serialVersionUID = 871682625566515505L;

	//	How far from the left is the start of Track
    private int leftMargin = 0; //OLD 5

    //	How much space is added to the right of the BarPanels
    private int rightMargin = 15;

    //How low is first line draw of the StandardPanel
    //SP- private int beginY ;
    private JLabel TrebleLabel = null;

    private boolean changed = false;

    private BarPanel TablaturePanel;

    //SP- lines that were comented to remove the StandardPanel display
    //SP- private BarPanel StandardPanel ;
    private int numBars;

    //SP- private StandardBar SB ;
    private Dimension clefSize;

    private int spaceBCBP = 5; //The space Between Clef and Bar Panel

    protected DisplayOptions displayOptions;

//UNUSED    private MusicCursorPosition tablatureMusicCursorPosition ;
    
	private MusicCursor tablatureMusicCursor;

	private static Integer CURSOR_LAYER = new Integer(99);
	
	private GPTrack gpTrack ;
	
	private boolean musicCursorsProcesingEvents ;


    /** Creates a new instance of TrackPanel */

    //this method is currently not called by anyone !!
    public TrackPanel() {
        super();
        this.displayOptions = new DisplayOptions();
        this.gpTrack = null ;
        this.musicCursorsProcesingEvents = true ;
        initComponents();
        
    }

    public TrackPanel(DisplayOptions currentDisplayOptions, MusicCursorPosition aMusicCursorPosition) {
        super();
        if (currentDisplayOptions != null) {
            this.displayOptions = currentDisplayOptions;
            this.gpTrack = null ;
            this.musicCursorsProcesingEvents = true ;
            initComponents();
            
//UNUSED            this.tablatureMusicCursorPosition = new MusicCursorPosition();
            //this.musicCursorPosition = aMusicCursorPosition ;
            
            //currentDisplayOptions are the current DisplayOptions...
            // here we decide if they are displayed or not
		    this.tablatureMusicCursor = new MusicCursor(this.TablaturePanel);
		    this.tablatureMusicCursor.setVisible(this.displayOptions.displayMusicCursor);
		    this.add(this.tablatureMusicCursor, CURSOR_LAYER);
            this.addMouseListener(this);
        } else {
            throw new NullPointerException("display options passed are null");
        }
    }

    private void arrange() {
        Dimension dim;
        int y;
        int x;

        dim = this.TablaturePanel.getMinimumSize();
        if (this.clefSize == null) {
            this.clefSize = this.TrebleLabel.getPreferredSize();
        }
        y = (dim.height - this.clefSize.height) / 2;
        TrebleLabel.setBounds(leftMargin, y, this.clefSize.width,
                this.clefSize.height);
        
        x = leftMargin + TrebleLabel.getWidth() + this.spaceBCBP;
     
        this.TablaturePanel.setBounds(x, 1, dim.width, dim.height);
    }

    private void initComponents() {
        //SP- BarStandard bar ;

        this.setLayout(null);
        //this is required to get the LayeredPane to fill its background
        this.setOpaque(true) ;
        
        this.setBackground(Color.WHITE);

        TrebleLabel = new JLabel(DGuitar.Clefs[0]);

        this.TablaturePanel = new BarPanel(this.displayOptions);

        //SP- this.StandardPanel = new BarPanel() ;

        //SP- SB = new StandardBar(this.StandardPanel) ;

        //SP- this.beginY = SB.LS*LINES_ABOVE + NoteComponent.H ;
        this.numBars = 0;

        this.clefSize = null;
        this.arrange();
        //The CLEF only was meaning when its standard notation !!
        //    this.add(TrebleLabel);

        this.TablaturePanel.setExtendTop(false);
        this.TablaturePanel.setExtendBottom(false);

        /*
         * this.TablaturePanel.setTopOffset(0) ;//20) ;
         * this.TablaturePanel.setBottomOffset(0) ; //30) ;
         */
        this.TablaturePanel.setBackground(Color.WHITE);
        this.TablaturePanel.setForeground(Color.BLACK);
        //this.TablaturePanel.setSnapColor(Color.GREEN) ;
        this.add(TablaturePanel);

        //SP- this.StandardPanel.setExtendTop(true) ;
        //SP- this.StandardPanel.setExtendBottom(false) ;

        //SP- this.StandardPanel.setTopOffset(beginY) ;
        //SP- this.StandardPanel.setBottomOffset(SB.LS*LINES_BELOW) ;

        //SP- this.StandardPanel.setBackground(Color.WHITE) ;
        //SP- this.StandardPanel.setForeground(Color.BLACK) ;
        //SP- this.StandardPanel.setSnapColor(Color.RED) ;
        //SP- this.add(StandardPanel) ;

        //the border was usefull for testing
        //this.setBorder(BorderFactory.createLineBorder(Color.BLACK)) ;

    }
    public void setMeasures(java.util.List<GPMeasure> Measures) {
        this.TablaturePanel.setMeasures(Measures);
        //SP- this.StandardPanel.setMeasures(Measures) ;
    }

    public void setMeasureTrackPair(int measure, GPMeasureTrackPair mtp) {
        this.TablaturePanel.setMeasureTrackPair(measure, mtp);
        //SP- this.StandardPanel.setMeasureTrackPair(measure,mtp) ;
    }
    /*
    public void setMusicCursor(int measure, int beat) {
        MusicCursorPosition MCP ;
        Point p ;
        
        MCP = this.TablaturePanel.getMusicCursorPosition(measure,beat) ;
        p = MCP.getPoint() ;
        p.x = p.x + leftMargin + TrebleLabel.getWidth() + this.spaceBCBP;
        MCP.setPoint(p) ;
        
        //DEBUG
        DGuitar.LS.log(MCP.toString()) ;
        //DGuitar.LS.log("measure: "+ measure + " beat: " + beat +" is located at " + p ) ;
        
        //BarMTP bar=tp.getTablatureBar(measure-1);
        //bar.scrollRectToVisible(new Rectangle(bar.getSize(null)));
        //musicCursor.scrollRectToVisible(this.musicCursor.getBounds()) ;
        this.tablatureMusicCursor.setMusicCursorPosition(MCP) ;
        //TODO the mouse is not being painted.
        //this.musicCursor.validate() ;
    }
*/
    public void setMusicCursor(int measure, int measurePrev, boolean scroll) {
        MusicCursorPosition MCP ;
        Point p ;
        Rectangle rect ;
        boolean wasVisible ;
        
        
        MCP = this.TablaturePanel.getMusicCursorPosition(measure,this.tablatureMusicCursor) ;
        p= MCP.getPoint() ;
        p.x = p.x + leftMargin + TrebleLabel.getWidth() + this.spaceBCBP;
        MCP.setPoint(p) ;
        
        //DEBUG
        //DGuitar.LS.log(MCP.toString()) ;
        wasVisible = this.tablatureMusicCursor.isVisible() ;
        if(wasVisible) {
            this.tablatureMusicCursor.setVisible(false) ;
        }
        rect = this.tablatureMusicCursor.getBounds() ;
        //to scroll ahead....rect.x = rect.x + rect.width ;
        //TESTING
        //rect.width += 200 ;
        /*
        this.TablaturePanel.getBar(measure-1).repaint() ;
        */

        //Since the scrollRect moves as little as posible to show the rectangle
        
        if(scroll) {
        //TODO when the music cursor goes back (repetetions..) this does not work.
            if(measure > measurePrev) {
                rect.x += rect.width + 250 ;
            }
            else {
                rect.width = 1 ;
                //	DEBUG
                //DGuitar.LS.log("rect = " + rect + " for measure: "+ measure + " prev:" + measurePrev ) ;
            }
        
            this.scrollRectToVisible(rect) ;        
        }
        
        
        this.tablatureMusicCursor.setMusicCursorPosition(MCP) ;

        //testing..
        /*if(measurePrev >= 0) {
            this.TablaturePanel.getBar(measurePrev).invalidate() ;
            this.TablaturePanel.getBar(measurePrev).repaint() ;
        }
        */
        //TESTING MCP.getGuiComponent().repaint() ;
        
        if(wasVisible) {
            this.tablatureMusicCursor.setVisible(true) ;
        }
        
        /*
        this.TablaturePanel.getBar(measure-1).repaint() ;
        this.TablaturePanel.getBar(measure).repaint() ;
        */
        //TESTING
        //this.tablatureMusicCursor.repaint() ;

        
    }

    /*
     * Get the TablatureBar located at measure
     */

    public BarMTP getTablatureBar(int measure) {
        Bar bar;
        BarMTP barMTP;

        barMTP = null;
        bar = this.TablaturePanel.getBar(measure);
        try {
            barMTP = (BarMTP) bar;
        } catch (ClassCastException CCE) {
            System.err.println("TrackPanel.getTablatureBar returned != BarMTP");
        }

        return barMTP;
    }

    public void addTablatureBar(int n) {
        int i;
        BarTablature bar;

        if (n >= 1) {
            bar = new BarTablature(this.displayOptions, this.TablaturePanel);
            bar.isFirst = true;
            bar.isLast = (n == 1);
            this.TablaturePanel.addBar(bar);
            if (n >= 2) {
                for (i = 1; i < n - 1; i++) {
                    bar = new BarTablature(this.displayOptions, this.TablaturePanel);
                    this.TablaturePanel.addBar(bar);
                }

                bar = new BarTablature(this.displayOptions, this.TablaturePanel);
                bar.isLast = true;
                this.TablaturePanel.addBar(bar);
            }
        }
    }

    /*
     * SP- public void addStandardBar(int n) { int i ; StandardBar bar ; Measure
     * m ;
     * 
     * if(n >= 1) { bar = new StandardBar(this.StandardPanel) ; bar.isFirst =
     * true ; bar.isLast = (n == 1) ; this.StandardPanel.addBar(bar) ; if(n >=
     * 2) { for(i = 1 ; i < n-1; i++) { bar = new
     * StandardBar(this.StandardPanel) ; this.StandardPanel.addBar(bar) ; }
     * 
     * bar = new StandardBar(this.StandardPanel) ; bar.isLast = true ;
     * this.StandardPanel.addBar(bar) ; } } }
     */

    public void addBar(int n) {
        this.addTablatureBar(n);
        
        //SP- this.addStandardBar(n) ;
        //SP- this.StandardPanel.repaint() ;
        this.numBars = this.numBars + n;
    }

    /*
     * public void addBar() { this.BP.addBar() ; this.BP.repaint() ;
     * this.numBars++ ; }
     */

    public int getNumBars() {
        return this.numBars;
    }

    public boolean hasChanged() {
        return this.changed;
    }

    /**
     * Gets the mininimum size of this component.
     * 
     * @return a dimension object indicating this component's minimum size
     */
    public Dimension getMinimumSize() {
        Dimension dim;
        int h;
        int w;
        Dimension aux;

        aux = this.TablaturePanel.getMinimumSize();
        //the widths is leftMArgin + size of Clef + spaceBetweenClefandBarPanel
        //... + rightMargin
        w = leftMargin + this.clefSize.width + spaceBCBP + aux.width
                + this.rightMargin;
        h = aux.height;

        dim = new Dimension(w, h);

        return dim;
    }


    /**
     * Gets the preferred size of this component.
     * 
     * @return a dimension object indicating this component's preferred size
     */
    public Dimension getPreferredSize() {
        Dimension d;

        d = this.getMinimumSize();

        return d;
    }

    public int getLeftMargin() {
        return this.leftMargin;
    }

    public void setLeftMargin(int newx) {
        this.leftMargin = newx;
        this.arrange();
    }

    /**
     * Sets the cursor image to the specified cursor. this cursor image is
     * displayed when the <code>contains</code> method for this component
     * returns true for the current cursor location, and this Component is
     * visible, displayable, and enabled. Setting the cursor of a
     * <code>Container</code> causes that cursor to be displayed within all of
     * the container's subcomponents, except for those that have a non-
     * <code>null</code> cursor.
     * 
     * @param cursor
     *            One of the constants defined by the <code>Cursor</code>
     *            class; if this parameter is <code>null</code> then this
     *            component will inherit the cursor of its parent
     * @see #isEnabled
     * @see #isShowing
     * @see #getCursor
     * @see #contains
     * @see Toolkit#createCustomCursor
     * @see Cursor
     * @since JDK1.1
     */
    /*
     * public void setCursor(java.awt.Cursor cursor) { SP-
     * StandardPanel.setCursor(cursor) ; }
     */

    /**
     * 
     * Validates this container and all of its subcomponents.
     * <p>
     * The <code>validate</code> method is used to cause a container to lay
     * out its subcomponents again. It should be invoked when this container's
     * subcomponents are modified (added to or removed from the container, or
     * layout-related information changed) after the container has been
     * displayed.
     * 
     * @see #add(java.awt.Component)
     * @see javax.swing.JComponent#revalidate()
     */
    
    public void validate() {
        arrange();
        super.validate();
    }
    /*original code
    public void validate() {
        arrange();
        super.validate();
    }
    */

    public void setNoteSpacing(int ns) {
        this.TablaturePanel.setNoteSpacing(ns);
    }

    public int getNoteSpacing() {
        return (this.TablaturePanel.getNoteSpacing());
    }

    public void setTrack(GPTrack t) {


        
        this.gpTrack = t ;
        this.setMusicCursorSingleColor(this.displayOptions.singleMusicCursorColor) ;
        this.TablaturePanel.setTrack(t);
    }

    public void setMusicCursorSingleColor(boolean singleColor) {
        Color color ;
        GPColor gpColor ;
        
        color = this.displayOptions.musicCursorColor ;
        
        //If the option of Displaying each cursor as its selected....
        if(!singleColor) {
            gpColor = gpTrack.getColor() ;
            try {
                color = new Color(gpColor.red,gpColor.green, gpColor.blue) ;
            } catch (IllegalArgumentException IAE) {
                System.err.println("Color of the Guitar Pro file is invalid, using default color") ;
            }
            //if the COLOR of the TRACK is WHITE display it as displayOptions.musicCursorColor
            if(color.equals(Color.WHITE)) {
                color = this.displayOptions.musicCursorColor ;   
            }
        }
        this.tablatureMusicCursor.setColorBorder(color) ;
    }
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void updateMusicCursor(MouseEvent e) {
		BarPanel barPanel;
		Point p;
		Point p2;
		Point newP;
		Point aux;
		int barPanelX;
		MusicCursorPosition MCP ;
		Component comp ;
		Class<? extends Component> c ;
		
		p = e.getPoint();
		//DEBUG
		//System.err.println("MousePressed at " +p) ;
		//get the component at that position.
		comp = this.getComponentAt(p) ;
		c = comp.getClass() ;
		if(c.isInstance(new BarPanel())) {
			barPanel = (BarPanel) comp;
			barPanelX = barPanel.getX();

			p2 = new Point(p.x - barPanelX,p.y) ;
			
			MCP = barPanel.snap(p2, this.tablatureMusicCursor) ;
//			DEBUG
			    //System.err.println(MCP) ;

			aux = MCP.getPoint() ;
			newP = new Point(barPanelX + aux.x, aux.y);
			
			/*TESTING...DOES NOT UPDATE THE CURSOR POSITION*/
			/*
			this.tablatureMusicCursor.setLocation(newP.x,newP.y) ;
			this.tablatureMusicCursor.validate() ;
			this.tablatureMusicCursor.repaint() ;
	*/
			/*WORKS*/
			MCP.setPoint(newP) ;
			this.tablatureMusicCursor.setMusicCursorPosition(MCP) ;

			
			 
			//this.musicCursor.validate() ;
			//this.musicCursor.repaint();
		} else {
		    //DEBUG
		    //System.err.println(comp.getClass()) ;
		    
			//its other type of object...at this moment it might be the MusicCursor or THIS.
			//TODO if the music cursor is selecting the whole bar...then
			//allow the user to select each beat !!!
			
		}
	}

    /**
     * @param musicCursorsProcesingEvents The musicCursorsProcesingEvents to set.
     */
    public void setMusicCursorsProcesingEvents(boolean musicCursorsProcesingEvents) {
        this.musicCursorsProcesingEvents = musicCursorsProcesingEvents;
    }
    public void setMusicCursorsVisible(boolean value) {
        this.tablatureMusicCursor.setVisible(value) ;
    }

    /**
     * @return Returns the musicCursorsProcesingEvents.
     */
    public boolean isMusicCursorsProcesingEvents() {
        return musicCursorsProcesingEvents;
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
     */
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
     */
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
     */
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
     */
    public void mousePressed(MouseEvent e) {
        int btn ;
        
        btn = e.getButton() ;
        if(btn == MouseEvent.BUTTON1) {
            if(this.musicCursorsProcesingEvents) {
                updateMusicCursor(e) ;
            }
        }
        else {
            //DEBUG
            //System.err.println("" + btn);
        }
    }

    /* (non-Javadoc)
     * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
     */
    public void mouseReleased(MouseEvent e) {
    }
    public String toString() {
        String resp ;
        
        resp = this.getClass().getName() ;
        if(this.gpTrack!= null) {
            resp += " " + this.gpTrack.toString() ;
        }
        return resp ;
    }
}