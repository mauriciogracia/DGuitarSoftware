/*
 * DurationColorsPanel.java
 *
 * Created on 3 de marzo de 2005, 01:25 AM
 */

package dguitar.gui;

import i18n.Internationalized;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Image;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import common.JColorButton;
/**
 *
 * @author  Mauricio Gracia Gutiérrez
 */
public class ColorPanelNote extends JPanel 
implements ActionListener,Internationalized  {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8488398966600388258L;
	private ColorScheme singleColor ;
    private ColorScheme durationColors ;
    private ColorScheme dynamicColors ;
    private ColorScheme lastSelectedColors ;
    
    private int typeOfColoring ;
    /** Creates new form DurationColorsPanel */
    
    public ColorPanelNote() {
        
        initComponents();
        postInit() ;
        lastSelectedColors = null ;
        
        /*Set the duration colors to the default*/
        this.durationColorsSetEnabled(false) ;
        durationColors = new ColorScheme() ;
        this.durationColorsUpdate() ;
        
        /*Set the dynamic colors to the default*/
        this.dynamicColorsSetEnabled(false) ;
        dynamicColors = new ColorScheme() ;
        dynamicColors.gradientDarkerUntil(Color.BLACK, 31) ;
        this.dynamicColorsUpdate() ;
        
        /**Set the single color to black*/
        
        singleColor = new ColorScheme(Color.BLACK) ;
        this.singleButton.setBackground(this.singleColor.getColor()) ;
        
        /**Set the single color as the selected one*/
        this.singleRadioButton.setSelected(true) ;
        this.typeOfColoring = DisplayOptions.RELATED_TO_NOTHING ;
        
        this.durationColorsSetListener(this) ;
        this.dynamicColorsSetListener(this) ;
    }
    private JPanel createMiniPanel(Component CompA,java.awt.Component CompB) {
        JPanel miniPanel ;
        
        miniPanel = new JPanel() ;
        miniPanel.setBorder(javax.swing.BorderFactory.createLineBorder(Color.GRAY)) ;
        miniPanel.setLayout(new java.awt.BorderLayout()) ;
        
        miniPanel.add(CompA,BorderLayout.NORTH) ;
        
        miniPanel.add(CompB,BorderLayout.CENTER) ;
        
        miniPanel.setBackground(Color.WHITE) ;
        return miniPanel ;
    }
    
    private void setIcon(JLabel label, int i, Image imgs[]) {
    	label.setIcon(new ImageIcon(imgs[i])) ;
    }
    private void setIcon(JLabel label, int i, ImageIcon iis[]) {
    	label.setIcon(iis[i]) ;
    }
    private void postInit() {
        int i ;
        java.awt.GridBagConstraints gridBagConstraints;
        JPanel miniPanel ;
        
        singleButton = new JColorButton() ;
        singleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleButtonActionPerformed(evt);
            }
        });
        
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        add(singleButton, gridBagConstraints);
        
        for(i = 0; i < 8; i++) {
            /*Crete the color buttons for the Durations*/
            this.colors[i] = new JColorButton() ;
            this.colors[i].setActionCommand("" + i) ;
            
            ColorPanelNote.durations[i] = new JLabel();
            setIcon(ColorPanelNote.durations[i],i,DGuitar.Notes) ;
            miniPanel = createMiniPanel(this.colors[i],ColorPanelNote.durations[i]) ;
            
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = i;
            gridBagConstraints.gridy = 4;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            
            add(miniPanel, gridBagConstraints);
            
            /*Crete the color buttons for the Dynamics*/
            this.colorsDynamics[i] = new JColorButton() ;
            this.colorsDynamics[i].setActionCommand("" + i) ;
            
            ColorPanelNote.dynamics[i] = new JLabel();
            setIcon(ColorPanelNote.dynamics[i],i,DGuitar.Dynamics) ;
            miniPanel = createMiniPanel(this.colorsDynamics[i],ColorPanelNote.dynamics[i]) ;
            
            gridBagConstraints = new java.awt.GridBagConstraints();
            gridBagConstraints.gridx = i;
            gridBagConstraints.gridy = 8;
            gridBagConstraints.weightx = 1.0;
            gridBagConstraints.weighty = 1.0;
            
            add(miniPanel, gridBagConstraints);
        }
    }
    private void durationDefaultOrSelectedColors() {
        boolean defaultC ;
        
        defaultC = this.durationColors.equals(new ColorScheme()) ;
        if(defaultC) {
            this.durationDefaultColors.setSelected(true) ;
        } else {
            this.durationChooseColors.setSelected(true) ;
        }
        this.lastSelectedColors = durationColors ;
        this.durationColorsSetEnabled(!defaultC) ;
    }
    private void dynamicDefaultOrSelectedColors() {
        boolean defaultC ;
        
        defaultC = this.dynamicColors.equals(new ColorScheme()) ;
        if(defaultC) {
            this.dynamicDefaultColors.setSelected(true) ;
        } else {
            this.dynamicChooseColors.setSelected(true) ;
        }
        this.lastSelectedColors = dynamicColors ;
        this.dynamicColorsSetEnabled(!defaultC) ;
    }
    /**
     * Sets the values acording to desired behavior
     */
    private void durationMultipleValuesSet(boolean b) {
        
        this.durationColorsSetEnabled(b) ;
        this.singleButton.setEnabled(!b) ;
        //new
        this.dynamicDefaultColors.setEnabled(false) ;
        this.dynamicChooseColors.setEnabled(false) ;
        
        this.durationDefaultColors.setEnabled(b) ;
        this.durationChooseColors.setEnabled(b) ;
        if(b) {
            durationDefaultOrSelectedColors() ;
        }
    }
    /**
     * Sets the values acording to desired behavior
     */
    private void dynamicMultipleValuesSet(boolean b) {
        
        this.dynamicColorsSetEnabled(b) ;
        this.singleButton.setEnabled(!b) ;
        //new
        this.durationDefaultColors.setEnabled(false) ;
        this.durationChooseColors.setEnabled(false) ;
        
        this.dynamicDefaultColors.setEnabled(b) ;
        this.dynamicChooseColors.setEnabled(b) ;
        if(b) {
            dynamicDefaultOrSelectedColors() ;
        }
    }
    private void durationColorsSetListener(ActionListener l) {
        int i ;
        JColorButton cbtn ;
        
        if(l != null) {
            for (i = 0 ; i < 8; i++) {
                cbtn = colors[i] ;
                if (cbtn != null) {
                    cbtn.addActionListener(l) ;
                }
            }
        }
    }
    private void dynamicColorsSetListener(ActionListener l) {
        int i ;
        JColorButton cbtn ;
        
        if(l != null) {
            for (i = 0 ; i < 8; i++) {
                cbtn = colorsDynamics[i] ;
                if (cbtn != null) {
                    cbtn.addActionListener(l) ;
                }
            }
        }
    }
    /**
     * This method is NOT called by ACTIONS EVENTS,
     * therefore lastSelectedColors is not changed
     */
    protected void setColors(ColorScheme ndc, int TypeOfColoring) {
        ColorScheme dc ;
        
        if(ndc != null) {
            dc = (ColorScheme) ndc.clone() ;
            this.typeOfColoring = TypeOfColoring ;
            //OLD if(dc.isUniqueColor()) {
            if(typeOfColoring == DisplayOptions.RELATED_TO_NOTHING) {
                this.singleColor = dc ;
                this.singleButton.setBackground(this.singleColor.getColor()) ;
                this.singleRadioButton.setSelected(true) ;
            } else if(typeOfColoring == DisplayOptions.RELATED_TO_DURATION) {
                this.durationColors = dc ;
                durationColorsUpdate() ;
                this.durationRadioButton.setSelected(true) ;
                this.durationMultipleValuesSet(true) ;
            } else if(typeOfColoring == DisplayOptions.RELATED_TO_DYNAMIC) {
                this.dynamicColors = dc ;
                dynamicColorsUpdate() ;
                this.dynamicRadioButton.setSelected(true) ;
                this.dynamicMultipleValuesSet(true) ;
            }
        }
    }
    
    
    protected boolean selectionChanged() {
        return (this.lastSelectedColors != null) ;
    }
    protected ColorScheme getColors() {
        return this.lastSelectedColors ;
    }
    protected void setSingleColor(ColorScheme dc) {
        
        if(dc != null) {
            this.singleColor = dc ;
            this.singleButton.setBackground(this.singleColor.getColor()) ;
            this.singleRadioButton.setSelected(true) ;
        }
    }
    /**
     * call this every time you modify the dynamicColors direcly
     **/
    private void dynamicColorsUpdate() {
        int i ;
        JButton cbtn ;
        
        for (i = 0 ; i < 8; i++) {
            cbtn = colorsDynamics[i] ;
            if(cbtn != null) {
                cbtn.setBackground(this.dynamicColors.getColor(i)) ;
            }
        }
        
    }
    /**
     * call this every time you modif the durationColors direcly
     **/
    private void durationColorsUpdate() {
        int i ;
        JButton cbtn ;
        
        for (i = 0 ; i < 8; i++) {
            cbtn = colors[i] ;
            if(cbtn != null) {
                cbtn.setBackground(this.durationColors.getColor(i)) ;
            }
        }
        
    }
    /**
     * Sets the states of the 8 multiple Colors buttons
     */
    private void dynamicColorsSetEnabled(boolean b) {
        JButton cbtn ;
        int i ;
        
        for (i = 0 ; i < 8; i++) {
            cbtn = colorsDynamics[i] ;
            if(cbtn != null) {
                cbtn.setEnabled(b) ;
            }
        }
    }
    /**
     * Sets the states of the 8 multiple Colors buttons
     */
    private void durationColorsSetEnabled(boolean b) {
        JButton cbtn ;
        int i ;
        
        for (i = 0 ; i < 8; i++) {
            cbtn = colors[i] ;
            if(cbtn != null) {
                cbtn.setEnabled(b) ;
            }
        }
    }
    
    public void actionPerformed(java.awt.event.ActionEvent e) {
        Object obj ;
        JColorButton cbtn ;
        int val ;
        
        obj = e.getSource() ;
        cbtn = new JColorButton() ;
        if(obj.getClass().isInstance(cbtn)) {
            cbtn = (JColorButton) obj ;
            try {
                val = Integer.parseInt(cbtn.getActionCommand()) ;
                if(this.typeOfColoring == DisplayOptions.RELATED_TO_DURATION) {
                    this.durationColors.setColor(cbtn.getBackground(), val) ;
                    this.lastSelectedColors = this.durationColors ;
                } else {
                    this.dynamicColors.setColor(cbtn.getBackground(), val) ;
                    this.lastSelectedColors = this.dynamicColors ;
                }
            } catch (NumberFormatException NFE) {
                //msg = "invalid note pressed !!" ;
            }
        }
    }
    
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    private void initComponents() {//GEN-BEGIN:initComponents
    	componentsCreate() ;
    }//GEN-END:initComponents
    
    private void componentsCreate() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup = new javax.swing.ButtonGroup();
        durationDefaultOrSelected = new javax.swing.ButtonGroup();
        dynamicDefaultOrSelected = new javax.swing.ButtonGroup();
        singleRadioButton = new javax.swing.JRadioButton();
        durationRadioButton = new javax.swing.JRadioButton();
        durationDefaultColors = new javax.swing.JRadioButton();
        durationChooseColors = new javax.swing.JRadioButton();
        dynamicRadioButton = new javax.swing.JRadioButton();
        dynamicDefaultColors = new javax.swing.JRadioButton();
        dynamicChooseColors = new javax.swing.JRadioButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();

        setLayout(new java.awt.GridBagLayout());

        buttonGroup.add(singleRadioButton);

        singleRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                singleRadioButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 7.0;
        gridBagConstraints.weighty = 1.0;
        add(singleRadioButton, gridBagConstraints);

        buttonGroup.add(durationRadioButton);
        durationRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                durationRadioButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 8.0;
        gridBagConstraints.weighty = 1.0;
        add(durationRadioButton, gridBagConstraints);

        durationDefaultOrSelected.add(durationDefaultColors);
        durationDefaultColors.setEnabled(false);
        durationDefaultColors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                durationDefaultColorsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 4.0;
        gridBagConstraints.weighty = 1.0;
        add(durationDefaultColors, gridBagConstraints);

        durationDefaultOrSelected.add(durationChooseColors);

        
        durationChooseColors.setEnabled(false);
        durationChooseColors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                durationChooseColorsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 4.0;
        gridBagConstraints.weighty = 1.0;
        add(durationChooseColors, gridBagConstraints);

        buttonGroup.add(dynamicRadioButton);
        
        dynamicRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dynamicRadioButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 8.0;
        gridBagConstraints.weighty = 1.0;
        add(dynamicRadioButton, gridBagConstraints);

        dynamicDefaultOrSelected.add(dynamicDefaultColors);
        
        dynamicDefaultColors.setEnabled(false);
        dynamicDefaultColors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dynamicDefaultColorsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 4.0;
        gridBagConstraints.weighty = 1.0;
        add(dynamicDefaultColors, gridBagConstraints);

        dynamicDefaultOrSelected.add(dynamicChooseColors);
        
        dynamicChooseColors.setEnabled(false);
        dynamicChooseColors.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dynamicChooseColorsActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 4.0;
        gridBagConstraints.weighty = 1.0;
        add(dynamicChooseColors, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        add(jSeparator1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 8;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 8.0;
        gridBagConstraints.weighty = 1.0;
        add(jSeparator2, gridBagConstraints);

        //INTERNATIONALIZATION
        setLangText() ;
    }
    public void setLangText() {
    	singleRadioButton.setText(DGuitar.lang.getString("colorUnique"));
    	durationRadioButton.setText(DGuitar.lang.getString("colorDuration"));
    	durationDefaultColors.setText(DGuitar.lang.getString("colorDefault"));
    	durationChooseColors.setText(DGuitar.lang.getString("colorSelected"));
    	dynamicRadioButton.setText(DGuitar.lang.getString("colorDynamic"));
    	dynamicDefaultColors.setText(DGuitar.lang.getString("colorDefault"));
    	dynamicChooseColors.setText(DGuitar.lang.getString("colorSelected"));
    }
    private void dynamicRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dynamicRadioButtonActionPerformed
        if(this.dynamicRadioButton.isSelected()) {
            this.typeOfColoring = DisplayOptions.RELATED_TO_DYNAMIC ;
            this.dynamicMultipleValuesSet(true) ;
        }
    }//GEN-LAST:event_dynamicRadioButtonActionPerformed
    
    private void dynamicDefaultColorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dynamicDefaultColorsActionPerformed
        this.dynamicColorsSetEnabled(false) ;
        this.dynamicColors = new ColorScheme() ;
        dynamicColorsUpdate() ;
        this.lastSelectedColors = this.dynamicColors ;
    }//GEN-LAST:event_dynamicDefaultColorsActionPerformed
    
    private void dynamicChooseColorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dynamicChooseColorsActionPerformed
        this.dynamicColorsSetEnabled(true) ;
    }//GEN-LAST:event_dynamicChooseColorsActionPerformed
    
    private void singleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleButtonActionPerformed
        Color newC ;
        
        newC = this.singleButton.getBackground() ;
        this.singleColor.setColor(newC) ;
        this.lastSelectedColors = this.singleColor ;
    }//GEN-LAST:event_singleButtonActionPerformed
    
    private void durationChooseColorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_durationChooseColorsActionPerformed
        this.durationColorsSetEnabled(true) ;
    }//GEN-LAST:event_durationChooseColorsActionPerformed
    
    private void durationDefaultColorsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_durationDefaultColorsActionPerformed
        this.durationColorsSetEnabled(false) ;
        this.durationColors = new ColorScheme() ;
        durationColorsUpdate() ;
        this.lastSelectedColors = this.durationColors ;
    }//GEN-LAST:event_durationDefaultColorsActionPerformed
    
    private void durationRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_durationRadioButtonActionPerformed
        if(this.durationRadioButton.isSelected()) {
            this.typeOfColoring = DisplayOptions.RELATED_TO_DURATION ;
            this.durationMultipleValuesSet(true) ;
        }
    }//GEN-LAST:event_durationRadioButtonActionPerformed
    
    private void singleRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_singleRadioButtonActionPerformed
        Color newC ;
        
        if(this.singleRadioButton.isSelected()) {
            this.singleButton.setEnabled(true) ;
            this.durationMultipleValuesSet(false) ;
            newC = this.singleButton.getBackground() ;
            this.singleColor.setColor(newC) ;
            this.lastSelectedColors = this.singleColor ;
            this.typeOfColoring = DisplayOptions.RELATED_TO_NOTHING ;
        }
    }//GEN-LAST:event_singleRadioButtonActionPerformed
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JRadioButton durationChooseColors;
    private javax.swing.JRadioButton durationDefaultColors;
    private javax.swing.ButtonGroup durationDefaultOrSelected;
    private javax.swing.JRadioButton durationRadioButton;
    private javax.swing.JRadioButton dynamicChooseColors;
    private javax.swing.JRadioButton dynamicDefaultColors;
    private javax.swing.ButtonGroup dynamicDefaultOrSelected;
    private javax.swing.JRadioButton dynamicRadioButton;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JRadioButton singleRadioButton;
    // End of variables declaration//GEN-END:variables
    
    private common.JColorButton singleButton ;
    private common.JColorButton colors[] = new JColorButton[8] ;
    private common.JColorButton colorsDynamics[] = new JColorButton[8] ;
    private static javax.swing.JLabel durations[] = new JLabel[8] ;
    private static javax.swing.JLabel dynamics[] = new JLabel[8] ;
    
    /*Returns the type of coloring the user chosed, see displayOptions constants*/
    public int getTypeOfColoring() {
        return this.typeOfColoring ;
    }
}
