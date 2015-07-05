/*
 * Created on Mar 7, 2005
 */
package test.songs;

/**
 * @author crnash
 */
public class SongComponent
{
    int numerator;
    int denominator;
    int start;
    int end;
    
    public SongComponent(int numerator, int denominator, int start, int end)
    {
        this.numerator=numerator;
        this.denominator=denominator;
        this.start=start;
        this.end=end;
    }

    /**
     * @return the measure count
     */
    public int getMeasureCount()
    {
        return end+1-start;
    }

}
