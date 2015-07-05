/*
 * GPFormatException.java
 *
 * Created on 15 de enero de 2005, 01:34 PM
 */

package dguitar.codecs.guitarPro.version4;

import dguitar.codecs.guitarPro.GPFormatException;



/**
 * this class is declared to throw RunTimeExcpetions when parsing the file
 * this might include (but not limited to):
 *      - invalid version found
 *      - number of mesasures and tracks do not match MeasureTracPairs
 *      - any other discrepancies/erros in the file (negative frets, etc).
 * @author Mauricio Gracia Gutiérrez
 */
public class GP4FormatException extends GPFormatException {
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3256440304923128114L;

    /** Creates a new instance of GPFormatException */
    public GP4FormatException(String message) {
        super(message) ;
    }
    /** Creates a new instance of GPFormatException */
    public GP4FormatException(String message, Throwable cause) {
        super(message,cause) ;
    }

    
}
