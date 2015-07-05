/*
 * MidiTrackEvent.java
 *
 * Created on 25 de febrero de 2005, 12:07 AM
 */

package common;


import javax.sound.midi.MidiEvent;
/**
 *
 * @author Mauricio Gracia Gutiérrez
 */
public class MidiTrackEvent 
implements LicenseString
{
    protected int track ;
    protected int event ;
    protected MidiEvent midiEvent ;
    
    public String getLicenseString() {
    	return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
    }

    /** Creates a new instance of MidiTrackEvent */
    public MidiTrackEvent(int Track, int Event, MidiEvent midiEvent) {
        this.track = Track ;
        this.event = Event ;
        this.midiEvent = midiEvent ;
    }
    public String toString() {
        String res ;
        
        res = "Track" + ": " + track + ", " + "event" + ": " + event + ", " + Midi.MidiEventToString(midiEvent) ;
        
        return res ;
    }
    public int getTrack() {
        return this.track ;
    }
    public int getEvent() {
        return this.event ;
    }
    public MidiEvent getMidiEvent() {
        return this.midiEvent ;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The <code>equals</code> method implements an equivalence relation
     * on non-null object references:
     * <ul>
     * <li>It is <i>reflexive</i>: for any non-null reference value
     *     <code>x</code>, <code>x.equals(x)</code> should return
     *     <code>true</code>.
     * <li>It is <i>symmetric</i>: for any non-null reference values
     *     <code>x</code> and <code>y</code>, <code>x.equals(y)</code>
     *     should return <code>true</code> if and only if
     *     <code>y.equals(x)</code> returns <code>true</code>.
     * <li>It is <i>transitive</i>: for any non-null reference values
     *     <code>x</code>, <code>y</code>, and <code>z</code>, if
     *     <code>x.equals(y)</code> returns <code>true</code> and
     *     <code>y.equals(z)</code> returns <code>true</code>, then
     *     <code>x.equals(z)</code> should return <code>true</code>.
     * <li>It is <i>consistent</i>: for any non-null reference values
     *     <code>x</code> and <code>y</code>, multiple invocations of
     *     <tt>x.equals(y)</tt> consistently return <code>true</code>
     *     or consistently return <code>false</code>, provided no
     *     information used in <code>equals</code> comparisons on the
     *     objects is modified.
     * <li>For any non-null reference value <code>x</code>,
     *     <code>x.equals(null)</code> should return <code>false</code>.
     * </ul>
     * <p>
     * The <tt>equals</tt> method for class <code>Object</code> implements 
     * the most discriminating possible equivalence relation on objects; 
     * that is, for any non-null reference values <code>x</code> and
     * <code>y</code>, this method returns <code>true</code> if and only
     * if <code>x</code> and <code>y</code> refer to the same object
     * (<code>x == y</code> has the value <code>true</code>).
     * <p>
     * Note that it is generally necessary to override the <tt>hashCode</tt>
     * method whenever this method is overridden, so as to maintain the
     * general contract for the <tt>hashCode</tt> method, which states
     * that equal objects must have equal hash codes. 
     * 
     * @param   obj   the reference object with which to compare.
     * @return  <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     * @see     #hashCode()
     * @see     java.util.Hashtable
     */
    public boolean equals(Object obj) {
        boolean res ;
        
        res = false ;
        
        if( (obj != null) && (obj.getClass().isInstance(this)) ) {
            MidiTrackEvent MTE ;
            
            MTE = (MidiTrackEvent) obj ;
            res = (MTE.event == this.event) && (MTE.track == this.track) ;
        }
        return res ;
    }
}
