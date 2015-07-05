/*
 * Created on Mar 7, 2005
 */
package test.songs;


/**
 * @author crnash
 */
public class RepeatedSongComponent extends SongComponent
{
    int repeatCount;
    
    /**
     * @param numerator
     * @param denominator
     * @param start
     * @param end
     * @param repeat
     */
    public RepeatedSongComponent(int numerator, int denominator, int start, int end, int repeat)
    {
        super(numerator,denominator,start,end);
        this.repeatCount=repeat;
    }
    /**
     * @return Returns the repeatCount.
     */
    public int getRepeatCount()
    {
        return repeatCount;
    }
    /**
     * @param repeatCount The repeatCount to set.
     */
    public void setRepeatCount(int repeatCount)
    {
        this.repeatCount = repeatCount;
    }
}
