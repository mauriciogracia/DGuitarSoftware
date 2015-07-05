/*
 * Created on Mar 11, 2005
 */
package dguitar.adaptors.guitarPro;

/**
 * The number and size of triplets. We define a triplet as
 * 'notes' notes in the 'timeOf' time, so for instance, a 13:8 is 13 notes
 * played in the time of eight.
 * @author Chris
 */
public class TripletValue
{
    int notes;
    int timeOf;
    /**
     * @param notes
     * @param timeOf
     */
    public TripletValue(int notes, int timeOf)
    {
        this.notes = notes;
        this.timeOf = timeOf;
    }
    /**
     * @return Returns the notes.
     */
    public int getNotes()
    {
        return notes;
    }
    /**
     * @return Returns the timeOf.
     */
    public int getTimeOf()
    {
        return timeOf;
    }
}
