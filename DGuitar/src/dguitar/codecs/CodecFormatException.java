/*
 * Created on 18/03/2005
 *
 */
package dguitar.codecs;

/**
*
* This abstract class describes a posible format exception when coding/decoding a strem
*
*/
public abstract class CodecFormatException extends Exception {
   /**
	 * 
	 */
	private static final long serialVersionUID = 2890902020152189203L;
public CodecFormatException(String s) {
       super(s);
   }
   public CodecFormatException(String s, Throwable cause) {
       super(s, cause) ;
   }
}


