/**
 * 
 */
package dguitar.gui;

/**
 * Any object that wants to be played by the PlayPanel must implement this 
 * interface 
 * 
 * @author Mauricio Gracia
 *
 */
public interface Playable {
    public final static short NOTHING_PLAYABLE = 0 ;
    
    public final static short NOT_PLAYING = 1 ;
    
    public final static short PLAYING = 2 ;

	/**
	 * @return Returns the status of the playable object
	 */
	public short getStatus();
	
	public void setStatus(short st);
	
	//to be used public void pause() ;
	
}
