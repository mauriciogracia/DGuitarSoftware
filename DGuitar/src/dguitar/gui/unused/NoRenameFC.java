/**
 * 
 */
package dguitar.gui.unused;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.plaf.metal.MetalFileChooserUI;

/**
 * A class to disable the Rename of the JFileChooser
 * 
 * @author Mauricio Gracia G
 *
 */
public class NoRenameFC {
	
	
	public static void main(String args[])
	{ 
		JFrame mainFrame = new JFrame();

		CustomFileChooser chooser = new CustomFileChooser();
		mainFrame.getContentPane().add(chooser);
		mainFrame.setSize(400,400);
		mainFrame.setVisible(true);
	}
}

class CustomFileChooser extends JFileChooser
{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 8549713083805084837L;

	CustomFileChooser()
	{ 
		super();
		MyFileChooserUI metalUI = new MyFileChooserUI(this);
		setUI(metalUI);
	}
}
class DGFileRenderer
//extends DefaultListCellRenderer 
extends JLabel
implements ListCellRenderer<Object>
{
    /**
	 * 
	 */
	private static final long serialVersionUID = -9160121262069475207L;

	public DGFileRenderer() {
        setOpaque(true);
    }

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	    /*
	    list - The JList we're painting.
	    value - The value returned by list.getModel().getElementAt(index).
	    index - The cells index.
	    isSelected - True if the specified cell was selected.
	    cellHasFocus - True if the specified cell has the focus.
	    */
        setText(value.toString());
        setBackground(isSelected ? Color.red : Color.white);
        setForeground(isSelected ? Color.white : Color.black);
        return this;
    }

 //   ....
}

class MyFileChooserUI extends MetalFileChooserUI
{
	MyFileChooserUI(JFileChooser fileChooser)
	{ 
		super(fileChooser);
	}
	
	protected JPanel createList(JFileChooser fc)
	{ 
		JPanel p = new JPanel(new BorderLayout());
		JList<Object> list = new JList<Object>();
		
		list.setCellRenderer(new DGFileRenderer()) ; 
		
		if (fc.isMultiSelectionEnabled())
		{ 
			list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		}
		else
		{ 
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		}
		list.setModel(getModel());
		list.addListSelectionListener(createListSelectionListener(fc));
		list.addMouseListener(createDoubleClickListener(fc, list));
		JScrollPane scrollpane = new JScrollPane(list);
		p.add(scrollpane, BorderLayout.CENTER);
		
		return p;
	}
}
