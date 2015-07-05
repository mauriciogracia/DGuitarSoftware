package dguitar.gui;


import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.plaf.ComponentUI;

/*
 * This code was taken from
 * 
 * http://forum.java.sun.com/thread.jspa?forumID=257&threadID=392361
 * 		
 */
/**
 * To change the behaviour of the FileChooser to remove the posibility to
 * RENAME folders and files
 * 
 * TODO: 
 * - The NEW FOLDER button still appears (usefull when saving..but not when opening)
 * - When the view is changed to DETAILS the renaming is still posible !!
 * - files can not be sorted using columns

 */
public class DGFileChooser extends JFileChooser {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4034930724226851911L;
	public DGFileChooser(){
		super();
	}
	public DGFileChooser(File file) {
		super(file) ;
	}
	protected void setUI(ComponentUI newUI) {
		super.setUI(new DGFileChooserUI(this));
	}
	/**
	 * A main to test the Chooser
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		DGFileChooser fc = new DGFileChooser();
		int returnVal = fc.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			
			System.out.println("Path:" + file.getAbsolutePath()) ;
			System.out.println(" File:" + file.getAbsoluteFile()) ;
		}
	}
}

