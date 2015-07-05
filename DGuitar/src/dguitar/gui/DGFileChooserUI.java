package dguitar.gui;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.basic.*;
import javax.swing.plaf.metal.*;

/*
 * This code was taken from
 * 
 * http://forum.java.sun.com/thread.jspa?forumID=257&threadID=392361
 */
/**
 * To change the behavior of the FileChooser to remove the posibility to
 * RENAME folders and files
 * 
 * TODO: 
 * The NEW FOLDER button still appears (useful when saving..but no when opening)
 * When the view is changed to DETAILS the renaming is still posible !!
 * 		- files can not be sorted using columns
 * 
 * 
 * @author Obtained from http://forum.java.sun.com/thread.jspa?forumID=257&threadID=392361
 */
public class DGFileChooserUI extends MetalFileChooserUI {
	/**
	 * Constructs a MetalFileChooserUI
	 * 
	 * @param filechooser
	 */
	public DGFileChooserUI(JFileChooser filechooser) {
		super(filechooser);
	}
	protected class DoubleClickListener extends BasicFileChooserUI.DoubleClickListener {
		JList<Object> list;
		public DoubleClickListener(JList<Object> list) {
			super(list);
			this.list = list;
		}
		public void mouseEntered(MouseEvent e) {
			//DEBUG System.out.println("mouse entered");
			MouseListener [] l = list.getMouseListeners();
			for (int i = 0; i < l.length; i++) {
				if (l[i] instanceof MetalFileChooserUI.SingleClickListener) {
					list.removeMouseListener(l[i]);
				}
			}
			super.mouseEntered(e);
		}
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected MouseListener createDoubleClickListener(JFileChooser fc, JList list) {
		return new DoubleClickListener(list);
	}
}