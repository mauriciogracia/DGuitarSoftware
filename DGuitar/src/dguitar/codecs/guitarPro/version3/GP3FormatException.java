package dguitar.codecs.guitarPro.version3;

import dguitar.codecs.guitarPro.GPFormatException;

/**
 * The class GP3FormatException is an exception thrown when
 * there is a problem with the decoding of a GP3 file.
 *
 * @author Matthieu Wipliez
 */
public class GP3FormatException extends GPFormatException {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3257284721246810674L;
    
    public GP3FormatException(String s) {
        super(s);
    }
}
