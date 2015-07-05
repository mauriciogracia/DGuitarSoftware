/*
 * Created on Mar 9, 2005
 */
package dguitar.adaptors.guitarPro;

/**
 * This is a workaround for the tie note frets, When a tie note appears
 * in a tab, it is marked as fret 0. Which of course makes things sound
 * really awful until we do tie notes properly. The workaround is to maintain
 * a copy of the fret state for each track.
 * @author Chris
 */
public class FretState
{
    int fretStates[]=new int[7];
    
    public void setFretState(int string,int fret)
    {
        fretStates[string]=fret;
    }
    public int getFretState(int string)
    {
        return fretStates[string];
    }
    
}
