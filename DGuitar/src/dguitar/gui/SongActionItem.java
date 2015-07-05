/**
 * 
 */
package dguitar.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

/**
 * A component that contains a Song title, status/actions and more details
 * The idea is to test how to create a component that can be used as a 
 * MenuItem but also as a "row" with information
 * 
 * When the item is checked...the window is shown
 * The menu item will have some buttons with posible actions (play,stop,pause)
 * this is a prototype to try to keep everything more compact
 * @author Mauricio Gracia Gutierrez
 *
 */
public class SongActionItem extends JCheckBoxMenuItem {
    
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 336652727741796382L;

	public SongActionItem(String string) {
        //super(string) ;
        super(string) ;
        //this.setSize(200,200) ;
        this.setMinimumSize(new Dimension(200,200)) ;
        this.setLayout(new BorderLayout()) ;
        this.add(new JButton("play"),BorderLayout.EAST) ;
        
        
        
        // TODO Auto-generated constructor stub
    }

    public static void main(String args[]) {
        JFrame jf ;
        JMenuBar mainMenu ;
        JMenu menuWindow ;
        JCheckBoxMenuItem JCBI ;
        SongActionItem PP ;
        
        jf = new JFrame("Testing window") ;
        
        mainMenu = new JMenuBar() ;
        
        menuWindow = new JMenu("Window") ;
        
        JCBI = new JCheckBoxMenuItem("Testing") ;
        PP = new SongActionItem("NAME OF THE SONG") ;
        
        menuWindow.add(JCBI) ;
        menuWindow.add(PP) ;
        mainMenu.add(menuWindow) ;
        jf.setJMenuBar(mainMenu) ;
        
        jf.setVisible(true) ;
        jf.setExtendedState(Frame.MAXIMIZED_BOTH) ;
        
        
    }
}