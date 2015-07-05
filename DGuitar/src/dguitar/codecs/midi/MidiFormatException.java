/*
 * Created on Mar 19, 2005
 */
package dguitar.codecs.midi;

import dguitar.codecs.CodecFormatException;

/**
 * @author Chris
 */
public class MidiFormatException extends CodecFormatException
{

    /**
	 * 
	 */
	private static final long serialVersionUID = -8905536545325962726L;

	/**
     * @param s
     */
    public MidiFormatException(String s)
    {
        super(s);
    }

    /**
     * @param s
     * @param cause
     */
    public MidiFormatException(String s, Throwable cause)
    {
        super(s, cause);
    }

}
