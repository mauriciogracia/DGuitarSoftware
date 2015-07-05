/*
 * Created on Mar 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package dguitar.gui.midi;

import java.awt.event.ActionListener;
import java.util.Comparator;

import javax.swing.JCheckBox;

import common.Midi;
import common.MidiEventComparator;
import common.MidiTrackEvent;

/**
 * @author MGracia
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MidiEventPanelItem  implements Comparator<Object> {
	
        public MidiTrackEvent MTE ;
        public JCheckBox JC ;
        public int index ;
        
        public MidiEventPanelItem() {
            this.MTE = null ;
            this.index = -1 ;
        	this.JC = null ;
        }
        public MidiEventPanelItem(MidiTrackEvent mte, int i,boolean toolTip, ActionListener l) {
            this.MTE = mte ;
            this.index = i ;
            JC = new javax.swing.JCheckBox() ;
            //JC.setHorizontalAlignment(JLabel.CENTER) ;
            if(toolTip) {
            	JC.setToolTipText( Midi.MidiEventToString(this.MTE.getMidiEvent()) ) ;
            }
            //call this in the panel   ....locate(JC,MTE) ;
            JC.addActionListener(l) ;
            JC.setVisible(true) ;
        }
        public void setSelected(boolean b) {
            if(this.JC != null) {
                this.JC.setSelected(b) ;
            }
        }

        public boolean equals(Object obj) {
            boolean res ;
            MidiEventPanelItem i ;
            res = false ;
            
            if( (obj != null) && (obj.getClass().isInstance(this)) ) {
                i = (MidiEventPanelItem) obj ;
                res = i.MTE.equals(this.MTE) ;
            }
            return res ;
        }
    

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(Object o1, Object o2) {
		int resp ;
		MidiEventPanelItem a ;		
		MidiEventPanelItem b ;
		
		resp = 0 ;
		if( (o1 != null) && (o2 != null)) {
			a = new MidiEventPanelItem() ;
			if ( (o1.getClass().isInstance(a)) && (o2.getClass().isInstance(a)) ) {  
				a = (MidiEventPanelItem) o1 ;
				b = (MidiEventPanelItem) o2 ;
				resp =  new MidiEventComparator().compare(a.MTE.getMidiEvent(),b.MTE.getMidiEvent()) ;
			}
		}
		return resp;
	}
}

