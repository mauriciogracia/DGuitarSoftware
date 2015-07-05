package test;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import common.LicenseString;

public class TabUtil 
extends JPanel 
implements LicenseString
{
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}
	JButton JBfind ;
	
	JTextField JTFfind ;
    
    JTextField JTFtosearchFor ;
    
    JTextField JTFindex ;
    
    JTextField JTFocurrence ;
    
    JTextField JTFresult ;
    
    JTextField JTFcompact ;
	
	public TabUtil(MainForTesting MFT) {
		super() ;
		
        this.setLayout(new FlowLayout()) ;
        
        JTFfind = new JTextField("http://algo.com/ruta/archivo.html",50) ;
        
        JTFtosearchFor = new JTextField("/",10) ;
        
        JTFocurrence = new JTextField("3", 3) ;
        
        JTFresult = new JTextField( 10) ;
        
        JTFcompact = new JTextField( 50) ;
        
		this.add(JTFfind) ;
        
        this.add(JTFtosearchFor) ;
        
        this.add(JTFocurrence) ;
        
        this.add(JTFresult) ;
        
        this.add(JTFcompact) ;
		
		JBfind = new JButton("Find") ;
		
		JBfind.addActionListener(new JButtonListener(this,JButtonListener.BTN_FIND_ID)) ;
		
		this.add(JBfind) ;
			
	}
    public String getString() 
    {
        String res ;
        
        res = JTFfind.getText() ;
        
        return res ;
    }
    
    public String getStringToSearch() 
    {
        String res ;
        
        res = JTFtosearchFor.getText() ;
        
        return res ;
    }
    public void setStringResults(String newT) 
    {
        JTFresult.setText(newT) ;
    }
    public void setStringCompact(String newT) 
    {
        JTFcompact.setText(newT) ;
    }
    public int getOcurrence() {
        return Integer.valueOf(JTFocurrence.getText()).intValue() ;
    }
}
