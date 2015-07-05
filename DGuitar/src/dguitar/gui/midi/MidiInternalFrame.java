/*
 * MidiIF.java
 *
 * Created on 19 de febrero de 2005, 03:29 AM
 */

package dguitar.gui.midi;


import i18n.Internationalized;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.InternalFrameListener;

import common.Midi;
import common.MidiTrackEvent;
import common.SwingWorker;

import dguitar.gui.DGuitar;
import dguitar.gui.PlayToolBar;
import dguitar.gui.Playable;

/**
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public class MidiInternalFrame extends JInternalFrame 
implements	ActionListener, InternalFrameListener,Internationalized,Playable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8142931218005657479L;

	private Vector<MidiTrackEvent> V;
	
	private MidiEventPanel MEP;
	
	List<MidiTrackEvent> prevValues;
	
	SwingWorker worker;
	
	private boolean displayingEvents;
	
	private Dimension currentSize;
	
	//have the display process finished ?
	boolean finished;
	
	//a reference to a midi object
	private Midi midi;
	
	SwingWorker workerPlay ;
	
	private PlayToolBar PTB ;
    
    private short status ;
	
	/**
	 * Creates new empty MidiInternalFrame for instance/casting purposes
	 */
	public MidiInternalFrame() {
		super() ;
	}
	/**
	 * 
	 * @param file
	 * @param m
	 */
	public MidiInternalFrame(String file, Midi m, PlayToolBar ptb) {
		super();
		
		PTB = ptb ;
		
		displayingEvents = false;
		prevValues = null;
		finished = false;
		this.midi = null;
		initComponents();
		this.viewEvents.setSelected(displayingEvents);
		this.setVisibleScrolls(displayingEvents);
		
		this.status = NOT_PLAYING ;
		
		this.setTitle(file);
		this.midi = m;
		m.prepareToPlay();
		m.loadMID(file);
		MEP = new MidiEventPanel(this);
		MEPSP.setViewportView(MEP);
	}
	
	public void setVisibleScrolls(boolean b) {
		this.MEPSP.setVisible(b);
		this.Eventlist.setVisible(b);
		if (b) {
			currentSize = new Dimension(500, 400);
		} else {
			currentSize = new Dimension(500, 100);
		}
		this.setSize(currentSize);
	}
	
	public Dimension getPreferredSize() {
		return currentSize;
	}
	
	public void displayEvents() {
		this.worker = new SwingWorker() {
			public Object construct() {
				String aux;
				
				aux = viewEvents.getText();
				viewEvents.setEnabled(false);
				viewEvents.setText(DGuitar.getLang().getString("pleaseWait"));
				V = midi.SequenceToMidiTrackEvents();
				events.setListData(V);
				MEP.setMidiTrackEvents(V);
				/* this. */validate();
				
				viewEvents.setText(aux);
				viewEvents.setEnabled(true);
				setVisibleScrolls(displayingEvents);
				finished = true;
				return (null);
			}
		};
		this.worker.start();
	}
	
	private void performViewEvent(ActionEvent actionEvent) {
		int id;
		int[] indices;
		int[] newIndices ;
		JCheckBox JC;
		int current;
		int i;
		int j;
		boolean add;
		
		newIndices = null;
		//DEBUG DGuitar.LS.log(actionEvent.toString()) ;
		id = actionEvent.getID();
		if (id == ActionEvent.ACTION_PERFORMED) {
			//TODO...what was this for ? mod = actionEvent.getModifiers();
			indices = this.events.getSelectedIndices();
			JC = (JCheckBox) actionEvent.getSource();
			current = this.MEP.getIndexFor(JC);
			if (!JC.isSelected()) {
				//a checkbox has been selected
				if (indices.length > 1) {
					newIndices = new int[indices.length - 1];
					j = 0;
					for (i = 0; i < indices.length; i++) {
						if (indices[i] != current) {
							newIndices[j] = indices[i];
							j++;
						}
					}
					this.events.setSelectedIndices(newIndices);
				} else {
					this.events.clearSelection();
				}
			} else {
				//a checkbox has been selected
				newIndices = new int[indices.length + 1];
				
				if (indices.length == 0) {
					newIndices[0] = current;
				} else {
					j = 0;
					add = true;
					for (i = 0; i < newIndices.length; i++) {
						if (add) {
							if ((j < indices.length) && (indices[j] < current)) {
								newIndices[i] = indices[j];
								j++;
							} else {
								newIndices[i] = current;
								add = false;
							}
						} else {
							newIndices[i] = indices[j];
							j++;
						}
					}
				}
				//this make the list scroll to the selected event
				this.events.setSelectedValue(this.MEP
						.getMidiTrackEventAt(current), true);
				//this makes the selection include the selecte event.
				this.events.setSelectedIndices(newIndices);
				
			}
		}
	}
	public void setStatus(short st) {
		if(st == PLAYING) {
			if(this.status == NOT_PLAYING) {
				this.status = st ;
				workerPlay = new SwingWorker() {
					public Object construct() {
						midi.play();
                        status = NOT_PLAYING ;
						PTB.setStatus(status) ;
						return (null);
					}
				};
				workerPlay.start();
			}
            //ELSE is NOTHING_PLAYABLE
		}
		else {
			midi.stop() ;
            this.status = NOT_PLAYING ;
		}
	}

	public void actionPerformed(ActionEvent actionEvent) {
		Object o;
		/*java.awt.Component comp;
		 * 
		 */
		
		o = actionEvent.getSource();
		if (o.getClass().isInstance(this.viewEvents)) {
			performViewEvent(actionEvent);
		} 
	}
	
	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	private void initComponents() {//GEN-BEGIN:initComponents
		componentsCreate() ;
	}//GEN-END:initComponents
	
	private void componentsCreate() {
		Eventlist = new javax.swing.JScrollPane();
		events = new javax.swing.JList<MidiTrackEvent>();
		MEPSP = new javax.swing.JScrollPane();
		jMenuBar1 = new javax.swing.JMenuBar();
		File = new javax.swing.JMenu();
		viewEvents = new javax.swing.JCheckBoxMenuItem();
		menuFileExit = new JMenuItem() ;
		
		setClosable(true);
		setIconifiable(true);
		setMaximizable(true);
		setResizable(true);
		
		addInternalFrameListener(this);
		
		events.setFont(new java.awt.Font("MS Sans Serif", 0, 10));
		
		events
		.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(
					javax.swing.event.ListSelectionEvent evt) {
				eventsValueChanged(evt);
			}
		});
		
		Eventlist.setViewportView(events);
		
		getContentPane().add(Eventlist, java.awt.BorderLayout.CENTER);
		
		MEPSP
		.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
		getContentPane().add(MEPSP, java.awt.BorderLayout.SOUTH);
		
		
		viewEvents.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				viewEventsActionPerformed(evt);
			}
		});
		
		File.add(viewEvents);
		
		File.add(new JSeparator());
		
		menuFileExit.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				close() ;
				dispose() ;
			}
		});
		
		File.add(menuFileExit) ;
		
		jMenuBar1.add(File);
		
		setJMenuBar(jMenuBar1);
		
		//INTERNATIONALIZATION
		this.setLangText() ;
		
	}
	public void setLangText() {
		setTitle("MIDI") ;
		events.setToolTipText("List of Midi events for this file") ;
		File.setText(DGuitar.lang.getString("File")) ;
		viewEvents.setText(DGuitar.lang.getString("menuMIFviewMidiEvents")) ;
		menuFileExit.setText(DGuitar.lang.getString("menuClose")) ;
	}
	
	private void viewEventsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewEventsActionPerformed
		if (!this.displayingEvents) {
			displayingEvents = true;
			if (!finished) {
				displayEvents();
			} else {
				setVisibleScrolls(displayingEvents);
			}
		} else {
			displayingEvents = false;
			this.setVisibleScrolls(displayingEvents);
		}
		
	}//GEN-LAST:event_viewEventsActionPerformed
	
	public void close() {
		if (this.worker != null) {
			this.worker.interrupt();
		}
		if (workerPlay != null) {
			workerPlay.interrupt();
			midi.stop() ;
		}
	}
	private void formInternalFrameClosing(
			javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameClosing
		this.close() ;
	}//GEN-LAST:event_formInternalFrameClosing
	
	private void eventsValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_eventsValueChanged
		List<MidiTrackEvent> values;
		int i;
		
		//un-select the previous values
		if (prevValues != null) {
			for (i = 0; i < prevValues.size(); i++) {
				this.MEP.setSelected(prevValues.get(i), false);
			}
		}
		values = this.events.getSelectedValuesList() ;
		for (i = 0; i < values.size(); i++) {
			this.MEP.setSelected(values.get(i), true);
		}
		//now the prevValues are the values
		prevValues = values;
	}//GEN-LAST:event_eventsValueChanged
	
	public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
	}
	
	public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
		
	}
	
	public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
		formInternalFrameClosing(evt);
	}
	
	public void internalFrameDeactivated(
			javax.swing.event.InternalFrameEvent evt) {
	}
	
	public void internalFrameDeiconified(
			javax.swing.event.InternalFrameEvent evt) {
	}
	
	public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
	}
	
	public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
	}
	
	public short getStatus() {
		return status;
	}

	private JMenuItem menuFileExit ;
	
	private JScrollPane Eventlist;
	
	private JMenu File;
	
	private JScrollPane MEPSP;
	
	private JList<MidiTrackEvent> events;
	
	private JMenuBar jMenuBar1;
	
	private JCheckBoxMenuItem viewEvents;

	
}