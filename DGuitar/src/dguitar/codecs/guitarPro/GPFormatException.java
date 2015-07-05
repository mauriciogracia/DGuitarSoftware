package dguitar.codecs.guitarPro;

import dguitar.codecs.CodecFormatException;

/**
 *
 * The class GPFormatException describes a posible format exception when
 * parsing a stream
 *
 * @author Mauricio Gracia Gutierrez
 */
public abstract class GPFormatException extends CodecFormatException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 485177597894441179L;

	/**
     * Creates a new GPFormatException described by s.
     * @param s the exception message.
     */
    public GPFormatException(String s) {
        super(s);
    }
    
    /**
     * Creates a new GPFormatException described by s, and cause.
     * @param s the exception message.
     * @param cause the exception cause.
     */
    public GPFormatException(String s, Throwable cause) {
        super(s, cause) ;
    }
}