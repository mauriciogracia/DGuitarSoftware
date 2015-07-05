package common;
/*
 * ImageToolBar.java
 *
 * Created on 26 de diciembre de 2004, 11:10 PM
 */

import java.awt.Image;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;
/**
 * @author Mauricio Gracia Gutiérrez
 */
public class ImageToolBar 
extends JToolBar 
implements Serializable, LicenseString
{
    private int numButtons ;
    private ImageIcon IconArray[] ;
    private ImageIcon IconDisabledArray[] ;
    private JButton JButtonArray[] ;

    public String getLicenseString() {
    	return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
    }

    /**
     * Constructor method for an ImageToolBar
     *
     */
    public ImageToolBar() {
        numButtons = 0 ;
        IconArray = null ;
        JButtonArray = null ;
    }
    /**
     * Set the images of this ImageToolBar based on the images passed as parameter
     * 
     * @param images
     * @return	success when the toolbar was created
     */
    public boolean setImages(Image images[]) {
        int i ;
        boolean success ;
        
        success = false;
        if( (images != null) && (images.length == numButtons) ) {
            if(IconArray == null) {
                IconArray = new ImageIcon[images.length] ;
            }
            for(i = 0 ; i < images.length ; i++) {
                if(images[i] != null) {
                    IconArray[i] = new ImageIcon(images[i]) ;
                    JButtonArray[i].setIcon(IconArray[i]) ;
                }
            }
            success = true ;
        }
        return success ;
    }
    /**
     * Set the images of this ImageToolBar based on the imageIcons passed as parameter
     * 
     * @param imageIcons
     * @return true when operation has been successfull
     */
    public boolean setImageIcons(ImageIcon imageIcons[]) {
        int i ;
        boolean success = false;
        
        if( (imageIcons != null) && (imageIcons.length == numButtons) ) {
            IconArray = imageIcons ;
            for(i = 0 ; i < imageIcons.length ; i++) {
                if(imageIcons[i] != null) {
                    JButtonArray[i].setIcon(IconArray[i]) ;
                }
            }
            success = true ;
        }
        return success ;
    }
    /**
     * Set the ImageIcons to be used 
     * 
     * @param imageIcons
     * @return  true if the operation has been successfull
     */
    
    public boolean setImageIconsDisabled(ImageIcon imageIcons[]) {
        int i ;
        boolean success = false;
        
        if( (imageIcons != null) && (imageIcons.length == numButtons) ) {
            IconDisabledArray = imageIcons ;
            for(i = 0 ; i < imageIcons.length ; i++) {
                if(imageIcons[i] != null) {
                    JButtonArray[i].setDisabledIcon(IconDisabledArray[i]) ;
                }
            }
            success = true ;
        }
        return success ;
    }
    public int getNumButtons() {
        return numButtons;
    }
    
    public void setNumButtons(int n) {
        int i ;
        
        if(n > 0) {
            numButtons = n ;
            
            JButtonArray = new JButton[numButtons] ;
            for(i = 0 ; i < numButtons ; i++) {
                JButtonArray[i] = new JButton() ;
                JButtonArray[i].setDefaultCapable(false) ;
                JButtonArray[i].setActionCommand("" + i) ;
                this.add(JButtonArray[i]) ;
            }
        }
    }
    
    public void addActionListener(ActionListener listener) {
        int i ;
        
        if(listener != null) {
            for(i = 0 ; i < numButtons ; i++) {
                JButtonArray[i].addActionListener(listener) ;
            }
        }
    }
    /**
     * Returns true if (i >= 0) && (i < this.getNumButtons())
     * @param i
     * @return true if (i >= 0) && (i < this.getNumButtons())
     */
    private boolean validIndex(int i) {
    	return (i >= 0) && (i < this.getNumButtons()) ;
    }


    public void setButtonEnabled(int i, boolean flag) {
    	if(validIndex(i)) {
    		//JButtonArray[i].setDisabledIcon(i) ;
    		JButtonArray[i].setEnabled(flag) ;
    	}
    }
    
    public JButton getButtonAtIndex(int i) {
        JButton resp ;
        
        resp = null ;
        if ( validIndex(i) ) {
             resp = JButtonArray[i] ;
        }
        return resp ;
    }
    
    /*
    public void setButtonStatus(int i, int status) {
    	if ( validIndex(i) ) {
    		JButtonArray[i].setEnabled() ;
    	}
    }
    */
}
