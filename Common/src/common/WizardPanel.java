/*
 * Created on 25/03/2005
 *
 */
package common;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//UNUSED import java.io.File;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Mauricio Gracia Gutierrez
 *
 */
public class WizardPanel 
extends JPanel 
implements ActionListener ,LicenseString

{
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}

    private Vector<WizardStepPanel> stepPanels ;
    
    //commented since not used yet
    //private Vector stepInformation ;
    
    private int stepCurrent ;
    
    private int stepLast ;
    
    /**a Panel that contains Back,Next,Finish,Cancel buttons*/
    private JPanel stepButtons ;
    
    /**the arrya of buttonss Back,Next,Finish,Cancel buttons*/
    private JButton stepBtns[]  ;
    
    /**This panel is removed when a new step is to be shown*/
    private JPanel currentPanel ;
    
    public final int BTN_BACK = 0 ;
    
    public final int BTN_NEXT = 1 ;
    
    public final int BTN_FINISH = 2 ;
    
    public final int BTN_CANCEL = 3 ;
    
    private String stepBtnsStrings[] = {
        "Back",
        "Next",
        "Finish",
        "Cancel"
    };
    
    public WizardPanel(WizardStepPanel firstStepPanel) {
        super();
        if(firstStepPanel != null) {
        	this.stepPanels = new Vector<WizardStepPanel>(0,1) ;

        	this.stepCurrent = 0 ;
        	this.currentPanel = firstStepPanel ;
        	
        	//create the buttons..since addStepPanel changes their status.
            this.stepBtns = new JButton[4] ;        
    		this.stepButtons = this.stepButtonsCreate() ;
    		
        	//addStepPanel calls ...this.stepButtonsUpdate()  and incrementes this.stepLast
        	this.stepLast = -1 ;
        	this.addStepPanel(firstStepPanel) ;
        	this.init() ;

        }
        else {
        	throw new NullPointerException("firstStepPanel is null") ;
        }
    }
    
    public final boolean addStepPanel(WizardStepPanel nextStepPanel) {
        boolean success ;
        
        success = false ;
        if(nextStepPanel != null) {
            this.stepPanels.add(nextStepPanel) ;
            this.stepLast++ ;
        	//since this.stepLast was updated we call this
            this.stepButtonsUpdate() ;
            
            //TODO does Vector allow NULL objects ??
            //this.stepInformation.add(nextStepPanel.informationGet()) ;
            
            nextStepPanel.setWizardPanel(this) ;
        }
        return success ;
    }
    public final void setNextEnable(boolean value) {
    	//TODO if there is only one panel..do NOT enable it
    	this.stepBtns[1].setEnabled(value) ;
    }

    private final void init() {
    	int numStepPanels ;
    	
    	numStepPanels = this.stepPanels.size() ;
//    	check if there is at least one panel to show...
    	if(numStepPanels > 0) {
    		this.setLayout(new BorderLayout()) ;

        
    		
    		this.add(this.stepButtons ,BorderLayout.SOUTH) ;
    		
    		this.stepCurrent = 0 ;        
    		this.showCurrentStep() ;
    	}
    	else {
    		throw new IllegalStateException("call addStepPanel to add panels to show");
    	}
    }
    private final void showCurrentStep() {
        WizardStepPanel WSP ;

        //udpate the back and next buttons
    	this.stepButtonsUpdate() ;

        if(this.currentPanel != null) {
            //remove the currentPanel
        	this.currentPanel.setVisible(false) ;
            this.remove(this.currentPanel) ;
            //this.validate() ;
        }
        //get the desired Panel
        WSP = this.stepPanels.get(this.stepCurrent) ;

        //set the information for this step
        //TODO WSP.informationSet(this.stepInformation.get(which)) ;
        
        WSP.setVisible(true) ;

        //add the desired panel to the center
        this.add(WSP,BorderLayout.CENTER) ;
        this.validate() ;
    

        //store the information about the current panel 
        this.currentPanel = WSP ;
    }
    //any method that modifies the variables this.stepCurrent or this.stepLast call this method
    //also when a panel is added to enable the Next button. 
    private void stepButtonsUpdate() {
    	this.stepBtns[BTN_BACK].setEnabled((this.stepCurrent != 0)) ;
    	
    	this.stepBtns[BTN_NEXT].setEnabled((this.stepCurrent != this.stepLast)) ;

    }
    private final JPanel stepButtonsCreate() {
        int i ;
        JPanel btnPanel ;
        
        btnPanel = new JPanel(new FlowLayout()) ;
        for(i = 0; i < this.stepBtns.length ; i++) {
            this.stepBtns[i] = new JButton() ;
            this.stepBtns[i].setActionCommand(this.stepBtnsStrings[i]) ;
            this.stepBtns[i].addActionListener(this) ;
            btnPanel.add(this.stepBtns[i]) ;
        }
        this.stepButtonsSetText() ;
     
        return btnPanel ;
    }
    private final void stepButtonsSetText() {
        int i ;
        
        for(i = 0; i < this.stepBtns.length ; i++) {
            this.stepBtns[i].setText(this.stepBtnsStrings[i]) ;
        }
    }

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object obj ;
		JButton btnSrc ;
		String actionCommand ;
		
		obj = e.getSource() ;
		if(obj.getClass().isInstance(this.stepBtns[0])) {
			btnSrc = (JButton) obj ;
			actionCommand = btnSrc.getActionCommand() ;
			//BACK button
			if(actionCommand.equals(this.stepBtns[BTN_BACK].getText())) {
				this.stepCurrent-- ;
				this.showCurrentStep() ;
			}
			//NEXT button
			else if(actionCommand.equals(this.stepBtns[BTN_NEXT].getText())) {
				this.stepCurrent++ ;
				this.showCurrentStep() ;
			}
			else if(actionCommand.equals(this.stepBtns[BTN_FINISH].getText())) {
				
			}
			else if(actionCommand.equals(this.stepBtns[BTN_CANCEL].getText())) {
				
			}

		}
	}
}
