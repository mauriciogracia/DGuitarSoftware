package dguitar.codecs.guitarPro.version2;

import dguitar.codecs.guitarPro.GPFormatException;

/**
 * The class GP2FormatException is an exception thrown when
 * there is a problem with the decoding of a Guitar Pro 2 file.
 *
 * @author Matthieu Wipliez
 */
public class GP2FormatException extends GPFormatException {    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public GP2FormatException(String s) {
        super(s);
    }
}
