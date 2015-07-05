/*
 * Created on Mar 23, 2005
 */
package dguitar.adaptors.song;

/**
 * @author crnash
 */
public class TimeSignature
{
    int numerator;
    int denominator;
    
    /**
     * @param numerator
     * @param denominator
     */
    public TimeSignature(int numerator, int denominator)
    {
        this.numerator = numerator;
        this.denominator = denominator;
    }
    /**
     * @return Returns the denominator.
     */
    public int getDenominator()
    {
        return denominator;
    }
    /**
     * @return Returns the numerator.
     */
    public int getNumerator()
    {
        return numerator;
    }
    
    public String toString()
    {
        return numerator+"/"+denominator;
    }
    
    public boolean equals(Object x)
    {
        if(x instanceof TimeSignature)
        {
            TimeSignature ts=(TimeSignature) x;
            return((numerator==ts.numerator)&&(denominator==ts.denominator));
        }
        return false;
    }
    
    public int hashCode()
    {
        return toString().hashCode();
    }
}
