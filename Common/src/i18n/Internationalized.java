/*
 * Created on 4/04/2005
 *
 */
package i18n;

/**
 * Objects that are internationalized implement this interface
 * 
 * @author Mauricio Gracia
 *
 */
public interface Internationalized
{
	/*
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}
	*/

    /**
     * This method must by implemented were all the call to setText,setTitle methods and any
     * other method that involves a VISIBLE string are placed in here
     */
    public void setLangText() ; 
}
