/*
 * Created on 25/03/2005
 *
 */
package common;

/**
 * Any Panel that wants to be added to a Wizard Panel must implement this interface.
 * 
 * @author Mauricio Gracia Gutierrez
 *
 */
public interface WizardStep
{
	/*
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}
	*/

	/*
	 * This method is called by the Wizard Panel to GET information FROM the panel
	 * 
	 * This is called when the panel is added or updated 
	 * 
	 * @param obj The return object contains all the information this steps has.
	 */

	//UNUSED public Object informationGet() ;
	
	/*
	 * This method is called by the Wizard Panel to SET the information of the panel
	 * 
	 * This is mainly used to go BACK one step, obj might be null if informationGet() 
	 * returned null 
	 * 
	 * @param obj This object is obtain by the wizard with the informationGet() method
	 */
	//UNUSED public void informationSet(Object obj) ;
	
    /**
     * This method MUST determine if the current information is accepted or not.
     * 
     * When the user clicks on the NEXT button it calls this method to determine if going
     * to the next step is allowed 
     * 
     * You can use the method <code>WizarPanel.setNextEnable(boolean value)</code> when 
     * the information of the panel is updated and you want to enable/disable the Next button 
     * depending that information.
     * 
     * @return true when the information is complete and accepted by this panel
     */
    public boolean isInformationAccepted() ; 
}
