/*
 * Created on 18/03/2005
 *
 */
package dguitar.codecs;

import java.io.IOException;

/**
 * This interface must be implemeted by all Codec classes classes representing an input stream.
 * 
 *
 * @author Mauricio Gracia G.
 *
 */
public interface CodecInputStream extends Codec {
    /**
     * Read the input stream on to a single object !! (is this good?)
     * @throws CodecFormatException
     */
    public Object readObject() throws IOException, CodecFormatException;

}
