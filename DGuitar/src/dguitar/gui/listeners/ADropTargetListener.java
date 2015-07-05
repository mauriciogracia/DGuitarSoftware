/*
 * ADropTargetListener
 * Created on 20/04/2005
 *
 *
 */
package dguitar.gui.listeners;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import dguitar.gui.DGuitar;

/**
 * The Drop action happens in this order: 1. dragEnter =
when the mouse enters
 * the component 2. dragOver = after the mouse has entered
but has not been
 * released 3. drop = when the mouse is released
 * 
 * @author Mauricio Gracia G
 */
public class ADropTargetListener implements DropTargetListener {
    /**
     * The parent component of this one.
     */
	private DGuitar parent;

    /**
     * creates a DropTargetListener for DGuitar
     * @param dguitar
     */
	public ADropTargetListener(DGuitar dguitar) {
		if (dguitar != null) {
			this.parent = dguitar;
		} else {
			throw new NullPointerException("dguitar is null");
		}
	}

	/**
	 * overrides the DROP method.
	 * 
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent dtde) {
		Transferable transfer;
//		UNUSED String str;
//		UNUSED java.util.List list;

		//      DEBUG
		//      System.out.println("drop" + ":" + dtde) ;

		transfer = dtde.getTransferable();

//		UNUSED		DataFlavor[] currentFlavors = dtde.getCurrentDataFlavors();

		//we must accept the transfer to process it
		//TODO other actions to accept ?
		dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

//		UNUSED		DataFlavor[] flavors = transfer.getTransferDataFlavors();
		importData(parent, transfer);
		/*try {
		 boolean success = false;
		 for (int i = 0; i < flavors.length; i++) {
		 if (success)
		 break;
		 DataFlavor flavor = flavors[i];
		 //list = (List)
		 // transfer.getTransferData(DataFlavor.javaFileListFlavor);
		 list = (List) transfer.getTransferData(flavor);
		 parent.displayMultipleFiles(list);
		 success = true;
		 }
		 } catch (UnsupportedFlavorException e) {
		 e.printStackTrace();
		 } catch (IOException e) {
		 e.printStackTrace();
		 }*/
	}
    /**
     * Imports the data of the Drop action
     * @param dguitar the dguitar window
     * @param t the transferable object
     */

	public void importData(DGuitar dguitar, Transferable t) {
		if (canImport(t.getTransferDataFlavors())) {
			try {
				DataFlavor[] flavors = t.getTransferDataFlavors();
				for (int i = 0; i < flavors.length; i++) {
					//    	 Drop from Windows
					if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
						
						Object gtd ;
						
						gtd = t.getTransferData(DataFlavor.javaFileListFlavor) ;
						if (gtd instanceof List) {
							@SuppressWarnings("unchecked")
							List<File> fileList = (List<File>) gtd;
							dguitar.displayMultipleFiles(fileList);

						}

					}
					//    	 Drop from GNOME or kde
					// delimited by \n  (\r\n) on gnome
					// will need to remove file:// at start
					else if (DataFlavor.stringFlavor.equals(flavors[i])) {
						if (t.getTransferData(DataFlavor.stringFlavor)
instanceof String) {
							String path = (String) t
									.getTransferData(DataFlavor.stringFlavor);
							List<File> fileList = convertStringsToFileList(path);
							dguitar.displayMultipleFiles(fileList);

						}

					}
				}
			} catch (UnsupportedFlavorException e) {
				e.printStackTrace();

			} catch (IOException e) {
				e.printStackTrace();

			}
		}
	}
	/**
     * converts Strings in certain format to a FileList 
     * @param filestr   the list of files in a single string
     * @return          a list of files
	 */

	private ArrayList<File> convertStringsToFileList(String filestr) {
		ArrayList<File> files = new ArrayList<File>();
		String[] tokenizedFiles = filestr.split("\n");
		for (int i = 0; i < tokenizedFiles.length; i++) {
			String path = tokenizedFiles[i];
			if (path.startsWith("file://")) {
				if (path.endsWith("\r")) {
					//appears to be the case for gnome but not kde
					path = path.substring(7);
					path = path.replaceAll("\r", "");

					path = path.replaceAll("%20", " ");
				} else {
					path = path.substring(7);
				}
			}
			files.add(new File(path));

		}
		return files;
	}
	/**
     * Determine if it can import certain flavor
     * 
     * @param flavors   an array of DataFlavors
     * @return true or not depending on the flavor
	 */
	public boolean canImport(DataFlavor[] flavors) {
		for (int i = 0; i < flavors.length; i++) {
			if (DataFlavor.javaFileListFlavor.equals(flavors[i])) {
				return true;
			} else if (DataFlavor.stringFlavor.equals(flavors[i])) {
				return true;
			}
		}
		return false;
	}


    /**
     * method that is called when the drag starts or enters the window
     * @param dtde a DropTargetDragEvent
     * 
     * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
     */
	public void dragEnter(DropTargetDragEvent dtde) {
		//DEBUG
		//System.out.println("dragEnter" + ":" + dtde) ;
	}

	/**
	 * a method that is called when the DRAG is OVER ??
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */

	public void dragOver(DropTargetDragEvent dtde) {
		//      DEBUG
		//System.out.println("dragOver" + ":" + dtde) ;
	}

	/**
	 * when the drop action is changes this method is called
	 * 
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent dtde) {
		//      DEBUG
		//System.out.println("dropActionChanged" + ":" + dtde) ;
	}

	/**
	 * method called when the drag exits the window
	 * 
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent dte) {
		//      DEBUG
		//System.out.println("dragExit" + ":" + dte) ;
	}

}