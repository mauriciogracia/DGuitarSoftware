package test;

/**
 * 
 */


import java.awt.Frame;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import common.LicenseString;

/**
 * @author mgracia
 *
 */
public class MainForTesting 
extends JFrame
implements LicenseString
{
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}

	//private static JFrame JFMain ;
	
	private static JTabbedPane JTP ;
	
	
	public MainForTesting() {
		super("Main For testing") ;
		
		this.setExtendedState(Frame.MAXIMIZED_BOTH) ;
		
		JTP= new  JTabbedPane() ;
		
		//add the differnt tabs to the tab pane
		
		//util tab
		JTP.add("UTIL",new TabUtil(this)) ;
		
		
		//add the tab pane to the main window
		
		this.getContentPane().add(JTP) ;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		new MainForTesting().setVisible(true) ;
	}
	

}
