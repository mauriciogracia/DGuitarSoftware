/*
 * JColorButton.java
 *
 * Created on 3 de marzo de 2005, 03:44 AM
 */

package common;


import java.awt.event.ActionListener;

import javax.swing.JColorChooser;

/**
 * This a JButton that launches the JColorChoosser, it handles the events
 * When the color is changed is shown as the background
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public class JColorButton 
extends javax.swing.JButton 
implements ActionListener,LicenseString 
{
    private static JColorChooser JCC ;
    
    private ActionListener userListener ;
    
    public String getLicenseString() {
    	return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
    }

    public JColorButton() {
        super() ;
        super.addActionListener(this) ;
        this.userListener = null ;
    }
    
    public void actionPerformed(java.awt.event.ActionEvent actionEvent) {
        java.awt.Color current ;
        String value ;
        
        current = this.getBackground() ;
        if(JCC == null) {
            JCC = new JColorChooser(current);
        }
        else {
            JCC.setColor(current) ;
        }
        value =  javax.swing.JOptionPane.showInputDialog(this.getParent(), JCC) ;
        if(value != null) {
            this.setBackground(JCC.getColor()) ;
            
            if(this.userListener != null) {
                this.userListener.actionPerformed(actionEvent) ;
            }
        }
    }
    
    /**
     * Adds an <code>ActionListener</code> to the button.
     * @param l the <code>ActionListener</code> to be added
     *
     * The actionPerformed method l will be called after a color is choosed
     */
    public void addActionListener(ActionListener l) {
        userListener = l ;
    }
}
