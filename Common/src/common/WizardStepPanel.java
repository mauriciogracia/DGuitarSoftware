/*
 * Created on 25/03/2005
 *
 */
package common;

import javax.swing.JPanel;

/**
 * Any Class that wants to be part of a Wizard Panel must extedn this abstract class
 * 
 * @author Mauricio Gracia Gutierrez
 *
 */
public abstract class WizardStepPanel 
extends JPanel 
implements WizardStep,LicenseString
{
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}

	/**The WizardPanel that contains this panel*/ 
    private WizardPanel WP ;
    
    /**
     * This method is called by the WizardPanel.addStepPanel
     * 
     * @param aWizardPanel the WizardPanel were this step is contained
     */
    protected final void setWizardPanel(WizardPanel aWizardPanel) {
        if(aWizardPanel != null) {
            this.WP = aWizardPanel ;
        }
        else {
            throw new NullPointerException("aWizardPanel is null") ;
        }
    }
    /**
     * This method allows you to obtain the WizardPanel that this panel belongs to. 
     *  
     * @return the WizardPanel that contains this step.
     */
    public final WizardPanel getWizardPanel() {
    	return this.WP ;
    }
}
