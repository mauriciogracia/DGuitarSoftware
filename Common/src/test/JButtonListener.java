/**
 * 
 */
package test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import common.LicenseString;
import common.Util;
/**
 * @author mgracia
 *
 */
public class JButtonListener 
implements ActionListener,LicenseString
{
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}
	public static final int BTN_FIND_ID = 0 ;   
	
	private int id ;
    private TabUtil tab ;
	
	//public JButtonListener( MainForTesting MFT,int buttonId) {
    public JButtonListener( TabUtil tabPanel,int buttonId) {
		super();
		this.id = buttonId ;
        this.tab = tabPanel ;
	}

	public void actionPerformed(ActionEvent e) {
        String str ;
        
		switch (id) {
		case BTN_FIND_ID :
			//parent.getComponent(BTN_FIND_ID)
			//Util.showDialogOk( this.parent,"Find :" + , "Searching:"+ ) ;
            str = this.tab.getString() ;
			this.tab.setStringResults("" + Util.nIndexOf(str,this.tab.getStringToSearch(),this.tab.getOcurrence())) ;
            this.tab.setStringCompact(Util.compactAndReadableURL(str,25)) ;
			break ;
			
		}
	}

}
