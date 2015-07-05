/*
 * MidiEventPanel.java
 *
 * Created on 25 de febrero de 2005, 12:00 AM
 */

package dguitar.gui.midi;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import common.MidiTrackEvent;

/**
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public class MidiEventPanel extends JPanel {
    /**
	 * 
	 */
	private static final long serialVersionUID = -1087725802908049536L;

	int maxTracks;

    int maxEvents;

    //left margin
    private static int LM = 80;

    //top margin
    private static int TM = 15;

    //width of a event
    private static int W = 30;

    //height of a TRACK
    private static int H = 16;

    private Vector<MidiEventPanelItem> Items;

    private ActionListener Listener;

    private Font labelFont;

    //UNUSED private boolean toolTip;

    /** Creates a new instance of MidiEventPanel */
    public MidiEventPanel(java.awt.event.ActionListener listener) {
        this.setLayout(null);
        maxTracks = -1;
        maxEvents = -1;
//      UNUSED this.toolTip = false;
        this.Listener = listener;
        labelFont = new java.awt.Font("MS Sans Serif", 0, 10);
    }

    private int calculateY(int track) {
        return (TM + (track * H));
    }

    private int calculateX(int event) {
        return (LM + (event * W));
    }

    private void locate(java.awt.Component C, MidiTrackEvent MTE) {
        int x;
        int y;

        y = calculateY(MTE.getTrack());
        x = calculateX(MTE.getEvent());
        C.setBounds(x + 6, y + 1, W, H - 2);
    }

    private void addLabel(String s, int x, int y, int w, int h) {
        JLabel JL;

        JL = new JLabel(s);
        JL.setFont(labelFont);
        JL.setBounds(x, y, w + 1, h + 1);
        JL.setVisible(true);
        JL.setHorizontalAlignment(SwingConstants.CENTER);
        JL.setBorder(javax.swing.BorderFactory
                .createLineBorder(java.awt.Color.BLACK));
        this.add(JL);
    }

    /**
     * This method iterates over V and creates objects acoording to that
     *  
     */

    /*
     * TODO all the previous components should be removed in case this method 
     * is called more than once.
     */

    public void setMidiTrackEvents(Vector<MidiTrackEvent> V) {
        int i;
        MidiTrackEvent MTE;
        MidiEventPanelItem item;

        maxTracks = 0;
        maxEvents = 0;
        Items = new Vector<MidiEventPanelItem>(0, 1);
        for (i = 0; i < V.size(); i++) {
            MTE = (MidiTrackEvent) V.get(i);
            if (MTE.getEvent() == 0) {
                //add the number of track
                if (MTE.getTrack() == 0) {
                    addLabel("Track \\ Event", 0, 0, LM, TM);
                }
                addLabel("" + MTE.getTrack(), 0, calculateY(MTE.getTrack()),
                        LM, H);
            }
            maxTracks = Math.max(maxTracks, MTE.getTrack());
            maxEvents = Math.max(maxEvents, MTE.getEvent());
            item = new MidiEventPanelItem(MTE, i, false, Listener);
            locate(item.JC, MTE);
            this.add(item.JC);
            Items.add(item);
        }
        for (i = 0; i <= maxEvents; i++) {
            //add the number of event
            addLabel("" + i, calculateX(i), 0, W, TM);
        }

    }


    public java.awt.Dimension getMinimumSize() {
        Dimension dim;

        dim = new Dimension(250, 50);
        if (maxTracks >= 0) {
            dim.height = 2 * TM + H * (maxTracks + 1) + 2;
        }
        if (maxEvents >= 0) {
            dim.width = LM + W * (maxEvents + 1);
        }

        return (dim);
    }

    /**
     * Selects a given MidiTrackEvent
     */
    public void setSelected(MidiTrackEvent mte, boolean b) {
        int i;
        MidiEventPanelItem item;
        boolean found;

        found = false;
        for (i = 0; (!found) && (i < Items.size()); i++) {
            item = Items.get(i);
            if (item.MTE.equals(mte)) {
                found = true;
                item.setSelected(b);
            }
        }
    }

    public MidiTrackEvent getMidiTrackEventAt(int index) {
        MidiEventPanelItem item;
        MidiTrackEvent res;

        res = null;
        if ((index >= 0) && (index <= Items.size())) {
            item = Items.get(index);
            res = item.MTE;
        }
        return res;
    }

    public int getIndexFor(JCheckBox jc) {
        int i;
        MidiEventPanelItem item;
        boolean found;
        int res;

        found = false;
        res = -1;
        for (i = 0; (!found) && (i < Items.size()); i++) {
            item = Items.get(i);
            if (item.JC.equals(jc)) {
                found = true;
                res = i;
            }
        }
        return res;
    }

    public Dimension getPreferredSize() {
        return getMinimumSize();
    }

}