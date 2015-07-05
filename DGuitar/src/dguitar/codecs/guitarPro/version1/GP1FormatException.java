package dguitar.codecs.guitarPro.version1;

import dguitar.codecs.guitarPro.GPFormatException;

/**
 * The class GP1FormatException is an exception thrown when
 * there is a problem with the decoding of a Guitar Pro 1 file.
 *
 * @author Matthieu Wipliez
 */
public class GP1FormatException extends GPFormatException {    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 1L;

    public GP1FormatException(String s) {
        super(s);
    }
}
