/*
 * Created on 18/03/2005
 *
 */
package dguitar.codecs;

import java.io.IOException;

/**
 * This interface must be implemeted bye all Codec classes representing an OUTPUT stream.
 * 
 *
 * @author Mauricio Gracia G.
 *
 */
public interface CodecOutputStream extends Codec {
    /**
     * Write a single single object to the stream (is this good?)
     * @throws CodecFormatException
     */
    public int write(Object obj) throws IOException, CodecFormatException ;

}

