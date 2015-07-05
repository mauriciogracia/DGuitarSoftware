/*
 * BarTablature.java
 *
 * Created on 27 de diciembre de 2004, 12:25 PM
 */

package dguitar.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Iterator;
import java.util.List;

import dguitar.codecs.guitarPro.GPBeat;
import dguitar.codecs.guitarPro.GPDuration;
import dguitar.codecs.guitarPro.GPEffectsOnNote;
import dguitar.codecs.guitarPro.GPMeasureTrackPair;
import dguitar.codecs.guitarPro.GPNote;

/**
 * This class extends Bar, and it holds a MeasureTrackPair.
 * 
 * Music is represented usually as an array of Tracks and Measures
 * 
 * Tracks are usually represented as rows. Measure are usually represtend as
 * columns.
 * 
 * A music track pair is the intersection of a row with a column.
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public abstract class BarMTP extends Bar {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6578538563394644693L;

	/**
	 * Are silences going to be added for the first time ?
	 */
	protected boolean addSilence;

	/**
	 * Are Effects On Note going to be added for the first time ?
	 */
	protected boolean addEONs;

	/**
	 * The fontAscent of the Fret font
	 */
	protected int fontAscent;

	/**
	 * The last Number of Horinzontal rhythm lines that were draw.
	 */
	protected int lastNumHLines;

	/**
	 * This is the color of the horizontal rhythm line that goes back to the
	 * Previous beat
	 */
	protected Color colorPrev = Color.BLACK;

	/**
	 * This is the color of the horizontal rhytm lina that goes forward to the
	 * Next beat
	 */
	protected Color colorNext = Color.BLACK;

	protected GPMeasureTrackPair mtp;

	private GPDuration minDuration;

	public static final int MIN_WIDTH = 50; //The minimal width of a bar

	private int min_width;

	/**
	 * Creates a new instance of BarTablature, related the BarPanel BP
	 */
	public BarMTP(DisplayOptions currentDisplayOptions, BarPanel BP) {
		super(currentDisplayOptions, BP);
		commonConstructor();
	}

	private void commonConstructor() {
		this.mtp = null;
		this.addSilence = true;
		this.addEONs = true;
		this.min_width = MIN_WIDTH;
		this.minDuration = GPDuration.SIXTY_FOURTH;
	}
	/**
	 * Creates a musicCursorPosition for this Bar
	 * @param musicCursor
	 * @return a musicCursorPosition for a MeasureTrackPair..MTP
	 */
	public MusicCursorPosition getMusicCursorPosition(MusicCursor musicCursor) {
	    Point p;
	    MusicCursorPosition resp;
	    
		p = new Point(0,1) ;
		
		resp = new MusicCursorPosition() ;
		resp.setPoint(p);
		resp.setWidth(this.getWidth() + 2 * musicCursor.getLeftOffset() );
		resp.setSongObject(this.mtp);
		resp.setGuiComponent(this ) ;
		resp.setNumBeat(0);
		
		return resp ;
	}
	public MusicCursorPosition snapX(int xx, MusicCursor musicCursor) {
		Iterator<GPBeat> it;
		GPBeat current;
		int cx;
		int cx2;
		int cw;
		MusicCursorPosition resp;
		boolean found;
		int minX;
		Point p;
		int contBeat;
		int fix;
		int x;

		minX = calculateMinX();
		cx = 0;
		p = new Point();
		p.y = 2	;
		resp = new MusicCursorPosition();

		//    	create a Iterator over the Beats
		it = mtp.getBeats().iterator();
		fix = musicCursor.getLeftOffset();
		found = false;
		if (it != null) {
			contBeat = 1;
			x = xx + fix;
			while ((!found) && it.hasNext()) {
				current = (GPBeat) it.next();
				cw = this.calculateWidthOfBeat(current);
				cx2 = cx + cw;
				found = ((cx + minX <= x) && (x <= cx2 + minX));
				if (found) {
					//DEBUG System.err.println("beat found");
					resp.setSongObject(current);
					resp.setNumBeat(contBeat);
					resp.setGuiComponent(this ) ;
				}
				p.x = cx + minX;
				resp.setPoint(p);
				resp.setWidth(cw);

				cx = cx2;
				contBeat++;
			}
		}
		if (!found) {
		    //Selecting the whole bar
		    resp = this.getMusicCursorPosition(musicCursor) ;
		}

		return resp;
	}
	
	/**
	 * 
	 * @return a sdaPoint where the beat is located. (the top string)
	 */
	public MusicCursorPosition getMusicCursorPosition(int beat) {
	    MusicCursorPosition MCP ;
		Point p;
		Iterator<GPBeat> it;
		GPBeat current;
		int cont;
		int cw ;

		MCP = new MusicCursorPosition() ;
		p = new Point();
		//create a Iterator over the Beats
		it = mtp.getBeats().iterator();
		if (it != null) {
			p.x = calculateMinX();
			current = null ;
			cw = 0 ;
			cont = 0;
			while ((cont < beat) && (it.hasNext())) {
				current = (GPBeat) it.next();
				cw = this.calculateWidthOfBeat(current);
				
				//if (current.isEmptyBeat()) {
					//	What needs to be drawn here ?
				//} else {
				p.x += cw;
				cont++ ;
			}
			p.y = 1;
			MCP.setPoint(p) ;
			MCP.setSongObject(current) ;
			MCP.setNumBeat(cont) ;
			MCP.setWidth(cw) ;
		}

		return MCP;
	}

	/*UNUSED..UNTESTED
	public int getNumBeat(Point point) {
		int cont;
		Iterator it;
		GPBeat prev;
		GPBeat current;
		Point p;
		

		cont = -1;
//		create a Iterator over the Beats
		it = mtp.getBeats().iterator();
		if (it != null) {
			p = new Point();
			p.x = calculateMinX();
			prev = null;
			cont = 0;
			//while( (p.x < point.x+ 5) && (it.hasNext()) ) {
			while ((p.x < point.x) && (it.hasNext())) {
				current = (GPBeat) it.next();

				if (current.isEmptyBeat()) {
					//	What needs to be drawn here ?
				} else {
					p.x += this.calculateWidthOfBeat(prev);
				}
				prev = current;
			}
			p.y = 0;
		}

		return cont;
	}*/


	public GPMeasureTrackPair getMeasureTrackPair() {
		return (this.mtp);
	}

	/*
	 * When using this method make sure you call Bar.invalidate() ;
	 */

	public void setMeasureTrackPair(GPMeasureTrackPair m) {
		this.mtp = m;
	}

	public boolean hasBeats() {
		boolean resp;

		resp = false;
		if (this.mtp != null) {
			resp = (this.mtp.getBeats() != null);
		}
		return resp;
	}

	public List<GPBeat> getBeats() {
		List<GPBeat> aux;

		aux = null;
		if (this.mtp != null) {
			aux = this.mtp.getBeats();
		}

		return aux;
	}

	protected int calculateWidthOfBeat(GPBeat beat) {
		int w;
		GPDuration d;
		int fact;
		int dif;

		w = 0;
		if (beat != null) {
			d = beat.getDuration();
			dif = minDuration.getIndex() - d.getIndex();
			fact = common.Util.pow2(dif);
			w = fact * this.getDisplayOptions().NS;
			//This is done here..because w is alreay in pixels
			//making w/2...not so afected by rounding.
			if (beat.dottedNotes) {
				w = w + w / 2;
			}
		}
		return w;
	}

	/*
	 * this is called when the Bar contains beats (its assumed that beats is not
	 * null)
	 */
	protected int calculateWidth(List<GPBeat> beats) {
		int w;
		int i;
		GPBeat beat;

		w = 0;
		for (i = 0; i < beats.size(); i++) {
			beat = (GPBeat) beats.get(i);
			w = w + calculateWidthOfBeat(beat);
		}

		return w;
	}

	protected int calculateWidth(int inc) {
		int w;
		List<GPBeat> beats;

		w = inc;
		if (this.mtp != null) {
			beats = this.mtp.getBeats();
			if (beats != null) {
				//add space for a Grace note of the first beat
				w += this.getDisplayOptions().NS / 2;

				w += calculateWidth(beats);

				//add space add the end
				w += this.getDisplayOptions().NS / 2;
			}
			if (w < this.min_width) {
				w = min_width;
			}

		}

		return w;
	}

	/**
	 * Gets the minDuration of a measure-track pair This method is used for
	 * proportional spacing
	 */
	public final GPDuration minDurationOfBeats() {
		GPDuration min;
		int b;
		GPBeat beat;
		List<GPBeat> Beats;
		GPDuration d;

		min = GPDuration.WHOLE; //WHOLE NOTE see GPDecoder/Duration.java
		if (this.mtp != null) {
			Beats = mtp.getBeats();
			if (Beats != null) {
				for (b = 0; b < Beats.size(); b++) {
					beat = (GPBeat) Beats.get(b);
					d = beat.getDuration();
					if (d.compareTo(min) < 0) {
						min = d;
					}
				}
			}
		}
		return min;
	}

	protected abstract int calculateHeightOfString(int s);

	protected abstract void paintRhythmHL(Graphics g, int x, int lowest, int n,
			GPBeat prev);

	/**
	 * This method paint the notes using g, currentX, y and a GPNote
	 */

	protected abstract void paintNote(Graphics g, int currentX, int y,
			GPNote note);

	protected abstract void paintSilence(Graphics g, int currentX, GPBeat beat,
			int contSilences);

	protected abstract void paintEffects(Graphics g, int currentX, int y,
			GPEffectsOnNote EON, int contEON, GPBeat beat);

	/**
	 * paints the vertical and horizontal rhythm lines. Its asumed that beat is
	 * not null
	 */
	protected final void paintRhythm(Graphics g, int x, int y, GPBeat beat,
			GPBeat prev, GPBeat next) {
		int top;
		GPDuration d;
		int index;
		int lowest;
		GPDuration pd;
		GPDuration nd;
		int n;
		Color prevColor;
		Color rhythmColor;

		prevColor = g.getColor();
		d = beat.getDuration();
		if (d.equals(GPDuration.WHOLE)) {
			//Whole notes dont have Rhythm lines
		} else {
			lowest = this.getDisplayOptions().TopOffset + this.BP.H + 2 * this.getDisplayOptions().LS;
			index = d.getIndex();

			top = (index == 1) ? lowest - this.getDisplayOptions().LS : y;

			//draw the vertical rhythm line
			rhythmColor = this.getDisplayOptions().rhythmColors.getColor(d);
			g.setColor(rhythmColor);

			if (beat.isNoteBeat()) {
				//only beats that have notes, have vertical lines..
				//...sileces and empty beats dont
				g.drawLine(x, top, x, lowest);
			}
			if (beat.dottedNotes) {
				g.drawRect(x + 2, lowest + 2, 1, 1);
			}

			//draw the horizonal lines
			//all notes that are shorter duration than half
			if (index > 1) {
				//IF there is a PREVious beat, that is NOT empty
				//...draw the horizontal rhytm line
				if ((prev != null) && (!prev.isEmptyBeat())) {
					pd = prev.getDuration();
					//EIGHT notes have horizontal lines
					if (pd.compareTo(GPDuration.EIGHTH) <= 0) {
						n = GPDuration.EIGHTH.compareTo(d);
						if (beat.isRestBeat()) {
							if (d.compareTo(pd) > 0) {
								lastNumHLines -= d.compareTo(pd);
							}
							n = lastNumHLines;
						}
						this.paintRhythmHL(g, x, lowest, n, prev);
					}
				}
				//IF there is a NEXT beat, that is NOT empty
				//...draw the horizontal rhytm line
				if ((next != null) && (!next.isEmptyBeat())) {
					nd = next.getDuration();
					//EIGHT notes, and shorter, have horizontal lines
					if (nd.compareTo(GPDuration.EIGHTH) <= 0) {
						n = GPDuration.EIGHTH.compareTo(d);
						if (beat.isRestBeat()) {
							n = lastNumHLines;
						}
						this.paintRhythmHL(g, x, lowest, n, null);
						lastNumHLines = n;
					}
				}
			}
		}
		g.setColor(prevColor);
	}

	/**
	 * paint a singleBeat, given the currentX position, beat, Nots and the
	 * noteIndex. is also uses the prev and next GPBeat
	 */
	protected final void paintBeat(Graphics g, int currentX, GPBeat beat,
			GPBeat prev, GPBeat next, PaintedBeatInfo I) {
		int s;//the string number
		boolean played;
		GPNote note;
		int y;
		//It its the first (highest) string of the beat
		boolean first;
		Color prevColor;
		Color beatColor;
		prevColor = g.getColor();

		first = true;
		for (s = 6; s >= 0; s--) {
			played = beat.isStringPlayed(s);
			if (played) {
				y = calculateHeightOfString(s);

				//If this is the first string of the beat..(the highest)
				//draw the line from the highest string to the bottom.
				if (first) {
					this.paintRhythm(g, currentX, y, beat, prev, next);
					first = false;
				}
				note = I.getGPNote();
				if (note.effects != null) {

					//first we paint the effects on the note, then the note
					//to make sure that the FRET number is always on top
					this.paintEffects(g, currentX, y, note.effects, I.contEONs, beat);
					I.contEONs++;
				}
				//set the colors accoring to the options
				//if(DO.coloringForFrets == DisplayOptions.RELATED_TO_DURATION)
				// {
				beatColor = this.getDisplayOptions().fretColors.getColor(beat.getDuration());
				//}
				/*
				 * else if(DO.coloringForFrets ==
				 * DisplayOptions.RELATED_TO_DYNAMIC) { beatColor =
				 * this.DO.fretColors.getColor(beat.getbeat.getDuration()) ; }
				 */
				g.setColor(beatColor);
				this.paintNote(g, currentX, y, note);
			}
		}
		g.setColor(prevColor);
	}

	protected final GPBeat nextBeat(List<GPBeat> Beats, int b, int max) {
		GPBeat next;

		next = null;
		if (b + 1 < max) {
			next = (GPBeat) Beats.get(b + 1);
		}

		return next;
	}

	private final int calculateMinX() {
		int currentX;

		//the current position depends on what was draw before:
		// Time Signtures, repeat open, clefs...etc
		currentX = minx;

		//leave enough space for a gracenote to the left of the first beat
		currentX += this.getDisplayOptions().NS / 2;

		//to center the NOTE
		currentX += this.getDisplayOptions().NS / 2;

		return currentX;
	}

	/**
	 * This methods overrides the method of Bar and actually paints the frets
	 * Graphics g object that is passed has already a copy of the
	 */

	protected final void paintMeasureTrackPair(Graphics g) {
		List<GPBeat> Beats;
		//java.util.List Notes ;
		GPBeat beat;
		GPBeat prev;
		GPBeat next;
		int currentX;
		int max;
		int b;
		int contSilences;
		Color prevColor;
		PaintedBeatInfo PBI;

		if (mtp != null) {
			prevColor = g.getColor();
			Beats = mtp.getBeats();
			g.setFont(BarPanel.notesFont);
			fontAscent = g.getFontMetrics().getAscent();
			prev = null;
			lastNumHLines = -1;
			contSilences = 0;
			PBI = new PaintedBeatInfo();
			currentX = calculateMinX();
			max = Beats.size();
			for (b = 0; b < max; b++) {
				beat = (GPBeat) Beats.get(b);
				next = nextBeat(Beats, b, max);
				PBI.setNotes(beat.getNotes());
				if (beat.isEmptyBeat()) {
					//What needs to be drawn here ?
				} else {
					currentX += this.calculateWidthOfBeat(prev);
					if (beat.isRestBeat()) {
						//is a Silence Beat
						this.paintSilence(g, currentX, beat, contSilences);
						this.paintRhythm(g, currentX, 0, beat, prev, next);
						contSilences++;
					} else {
						//is a BEAT WITH NOTES
						paintBeat(g, currentX, beat, prev, next, PBI);
					}
				}
				prev = beat;
			}
			this.addSilence = false;
			this.addEONs = false;
			g.setColor(prevColor);
		}
	}

	protected void paintComponent(Graphics gg) {
		Graphics g;

		//Copy the graphics object as required above
		g = gg.create();

		super.paintComponent(g);
		if (this.mtp != null) {
			//draws the notes contained in the MeasureTrackPair
			this.paintMeasureTrackPair(g);
		}

		//finish
		g.dispose();
	}

	public void setMinDuration(GPDuration d) {
		if (d != null) {
			this.minDuration = d;
			//This line below reset the with...so the Bar can be shrinked
			this.min_width = 5;
		}
	}

	public void setMinWidth(int value) {
		this.min_width = value;
	}

	class PaintedBeatInfo {
		//a list of Notes
		private List<GPNote> Notes;

		//contNote is the "index" to access the Notes list
		private int contNotes;

		//count which Effect on Note are we painting
		public int contEONs;

		public PaintedBeatInfo() {
			this.Notes = null;
			this.contNotes = 0;
			this.contEONs = 0;
		}

		public void setNotes(List<GPNote> notes) {
			this.Notes = notes;
			this.contNotes = 0;
		}

		public GPNote getGPNote() {
			GPNote res;

			res = null;
			if (this.Notes != null) {
				res = (GPNote) this.Notes.get(this.contNotes);
				this.contNotes++;
			}
			return res;
		}
	}
}


/*
 * COPY of this very hard to implement method...just in case

public Point snapX(int xx, int fix) {
	Iterator it;
	GPBeat prev;
	GPBeat current;
	int cx;
	int cx2;
	int cw;
	Point resp;
	boolean found;
	int x;
	int minX;

	minX = calculateMinX();
	cx = 0;
	resp = new Point();
	// create a Iterator over the Beats
	it = mtp.getBeats().iterator();

	found = false;
	if (it != null) {
		prev = null;
		x = xx + fix;
		while ((!found) && it.hasNext()) {
			current = (GPBeat) it.next();
			cw = this.calculateWidthOfBeat(current);
			cx2 = cx + cw;
			found = ((cx + minX <= x) && (x <= cx2 + minX));
			//DEBUG
			//System.err.println("CUMPLE") ;

			resp.x = cx + minX;
			resp.y = cw;
			cx = cx2;
			prev = current;
		}
	}
	if (!found) {
		//Selecting the whole bar
		resp.x = 0;
		resp.y = this.getWidth() + 2 * fix;
		//DEBUG System.err.println("Selection the whole bar") ;
	}

	return resp;
}
*/
