package common;

/**
 * Midi.java, this class helps to interact with the JAVA midi package
 * It helps to catch the exceptions and return boolean values and other 
 * usefull tasks
 *
 * Created on 24 de diciembre de 2004, 08:10 PM
 */

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.sound.midi.Instrument;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiFileFormat;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Soundbank;
import javax.sound.midi.Synthesizer;
import javax.sound.midi.Track;
import javax.sound.midi.Transmitter;


/**
 * This class helps to interact with the standard <code>javax.sound.midi</code> 
 * package but it helps to catch exceptions and return boolean values so the 
 * person using the methods can decide what to do. 
 * 
 * @author Mauricio Gracia
 */
public class Midi 
implements LicenseString
{
	private Sequencer sequencer;

	protected Sequence sequence;

	Synthesizer synthesizer;

	Instrument instruments[];

	int channel = 0;

	ShortMessage msg;

	int tick;

	int count;

	static MidiDevice.Info allMidiDevices[] = null;

	int currentDeviceIndex;

	Transmitter transmitter;

	MidiDevice currentDevice;

	private List<MidiEvent> eventList;

	Receiver currentReceiver;

	Track track;

	double ppq;

	double tempo;

	boolean deviceAlreadyOpened;

	boolean playing;

	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}
	/*
	 * when delta = 16, duration = Quarter note delta = 8 -> duration =
	 * sixteenth note delta = 1 -> 1/64
	 */
	public int DELTA = 1;

	//The higher the velocity_on the harder the note will be played
	public final int VELOCITY_ON = 100;

	//The higher the velocity_off the harder the note will be stop playing
	public final int VELOCITY_OFF = 0;

	private final static boolean testing = false;

	/* This array has a list of commands and status messages mixed */
	private static int midiCommandsStatus[] = {
	//          Status byte for Active Sensing message (0xFE, or 254).
			ShortMessage.ACTIVE_SENSING,
			//          Command value for Channel Pressure (Aftertouch) message
			// (0xD0, or 208)
			ShortMessage.CHANNEL_PRESSURE,
			//          Status byte for Continue message (0xFB, or 251).
			ShortMessage.CONTINUE,
			//          Command value for Control Change message (0xB0, or 176)
			ShortMessage.CONTROL_CHANGE,
			//          Status byte for End of System Exclusive message (0xF7, or
			// 247).
			ShortMessage.END_OF_EXCLUSIVE,
			//          Status byte for MIDI Time Code Quarter Frame message (0xF1,
			// or 241).
			ShortMessage.MIDI_TIME_CODE,
			//          Command value for Note Off message (0x80, or 128)
			ShortMessage.NOTE_OFF,
			//          Command value for Note On message (0x90, or 144)
			ShortMessage.NOTE_ON,
			//          Command value for Pitch Bend message (0xE0, or 224)
			ShortMessage.PITCH_BEND,
			//          Command value for Polyphonic Key Pressure (Aftertouch)
			// message (0xA0, or 128)
			ShortMessage.POLY_PRESSURE,
			//          Command value for Program Change message (0xC0, or 192)
			ShortMessage.PROGRAM_CHANGE,
			//          Status byte for Song Position Pointer message (0xF2, or 242).
			ShortMessage.SONG_POSITION_POINTER,
			//         Status byte for MIDI Song Select message (0xF3, or 243).
			ShortMessage.SONG_SELECT,
			//          Status byte for Start message (0xFA, or 250).
			ShortMessage.START,
			//          Status byte for Stop message (0xFC, or 252).
			ShortMessage.STOP,
			//          Status byte for System Reset message (0xFF, or 255).
			ShortMessage.SYSTEM_RESET,
			//          Status byte for Timing Clock messagem (0xF8, or 248).
			ShortMessage.TIMING_CLOCK,
			//          Status byte for Tune Request message (0xF6, or 246).
			ShortMessage.TUNE_REQUEST };

	private static String midiCommandsStatusStrings[] = { "ACTIVE_SENSING",
			"CHANNEL_PRESSURE", "CONTINUE", "CONTROL_CHANGE",
			"END_OF_EXCLUSIVE", "MIDI_TIME_CODE", "NOTE_OFF", "NOTE_ON",
			"PITCH_BEND", "POLY_PRESSURE", "PROGRAM_CHANGE",
			"SONG_POSITION_POINTER", "SONG_SELECT", "START", "STOP",
			"SYSTEM_RESET", "TIMING_CLOCK", "TUNE_REQUEST" };
	/**
     * stores the latest errorMsg  
	 */
    private String errorMsg = null;
    /**
     * message to be displayed when the MIDI devices does not have any receivers
     */
    private String doesNotHaveAnyReceivers ;
    
	/** Creates a new instance of Midi */
	public Midi(String msgDoesNotHaveAnyReceivers) {
        doesNotHaveAnyReceivers = msgDoesNotHaveAnyReceivers ;
		playing = false;
		tick = 0;
		currentDevice = null;
		currentDeviceIndex = -1;
		currentReceiver = null;
		ppq = 0;
		eventList = null;
		transmitter = null;
		this.getMidiDevices();

	}
	/**
     * gets the current device represented by an index 
     * @return  an integer number that represents the index
	 */
	public int getCurrentDeviceIndex() {
		return currentDeviceIndex;
	}

	public MidiDevice getCurrentDevice() {
		return currentDevice;
	}
    /**
     * is the index passed valid.
     * @param i the index to test
     * @return true if (i >= 0) && (i < allMidiDevices.length)
     */
    public boolean isValidDeviceIndex(int i) {
        boolean resp ;
        
        resp = false ;
        if(allMidiDevices != null) {
            resp = (i >= 0) && (i < allMidiDevices.length) ;
        }
        //ELSE the index is always INVALID since allMidiDevices is null
        return resp ;
    }

    /**
     * Set the current device to index i
     * @param i is the index you want to set
     * @return true if the device could be set
     */

	public boolean setCurrentDevice(int i, String msgDoesNotHaveAnyReceivers) {
		MidiDevice testDevice;
		int numReceivers;
		boolean resp;
		String deviceName;

		resp = false;
        if (isValidDeviceIndex(i)) {
			try {
				deviceName = allMidiDevices[i].getName();
				testDevice = MidiSystem.getMidiDevice(allMidiDevices[i]);
				numReceivers = testDevice.getMaxReceivers();
                
                //the receiver is the object that process the midi messages
                //without one..playing is imposible
				if (numReceivers != 0) {
					currentDevice = testDevice;
					currentDeviceIndex = i;
					currentReceiver = currentDevice.getReceiver();
					resp = true;
				} else {
                    errorMsg = "Midi Device:" + deviceName + " " ;
                    if(msgDoesNotHaveAnyReceivers != null) {
                        errorMsg +=  msgDoesNotHaveAnyReceivers ;
                    }
                    else {
                        errorMsg +=  "does not have any receivers, try with a different one" ;
                    }
                    
                    errorMsg += ": 0 -" + allMidiDevices.length ;
				}
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}

		return resp;
	}
    /**
     * gets the number of available midi devices
     * 
     * @return the number of available midi devices
     */

	public int getNumberOfMidiDevices() {
		if (Midi.allMidiDevices == null) {
			// Get all the MIDI devices on the system.
			allMidiDevices = MidiSystem.getMidiDeviceInfo();
		}
		return allMidiDevices.length;
	}
	/**
     * obtains the list of midi devices 
     * 
     * @return an array of  MidiDevice
	 */
	public MidiDevice.Info[] getMidiDevices() {

		if (Midi.allMidiDevices == null) {
			// Get all the MIDI devices on the system.
			allMidiDevices = MidiSystem.getMidiDeviceInfo();
		}

		return Midi.allMidiDevices;
	}

	class timerListener implements java.awt.event.ActionListener {
		long eventClick;

		MidiEvent ME;

		boolean processed;

		long currentTime;

		Iterator<MidiEvent> it;

		long eventTime;

		double millisecondsPerPulse;

		int delay;

		public timerListener(List<MidiEvent> eventList, double MSPP, int Delay) {
			processed = true;
			currentTime = 0;
			it = eventList.iterator();
			millisecondsPerPulse = MSPP;
			delay = Delay;
		}

		public void actionPerformed(ActionEvent e) {
			if (it.hasNext()) {
				if (processed) {
					ME = (MidiEvent) it.next();
					eventClick = ME.getTick();
					eventTime = (long) (eventClick * millisecondsPerPulse);
				}
				if (eventTime <= currentTime) {
					currentReceiver.send(ME.getMessage(), -1);
					/* DEBUG System.err.println("currentReceiver.send"); */
					processed = true;
				} else {
					processed = false;
				}
				currentTime += this.delay;
			}
		}
	}

	public boolean play() {
		boolean success;

		success = false;
		if (!testing) {
			success = standardPlay();
		} else {
			success = developmentPlay();
		}
		return success;
	}

	private boolean openSequencer() {
		boolean success;

		success = false;

		try {
			if (sequencer == null) {
				sequencer = MidiSystem.getSequencer();
			}
			if (sequencer == null) {
				System.err.println("getSequencer() failed!");
				success = false;

			} else {
				if (!sequencer.isOpen()) {
					sequencer.open();
					success = true;
				}
			}
		} catch (MidiUnavailableException MUE) {
			MUE.printStackTrace();
		}

		return success;
	}

	public void prepareToPlay() {
		openSequencer();
		openSynthesizer();

		if (transmitter == null) {
			try {
				transmitter = sequencer.getTransmitter();
				transmitter.setReceiver(synthesizer.getReceiver());
			} catch (MidiUnavailableException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean standardPlay() {
		boolean success;

		success = true;
		try {
			prepareToPlay();
			/*
			 * openSequencer() ; openSynthesizer() ; // Connection Transmitter
			 * transmitter = sequencer.getTransmitter();
			 * transmitter.setReceiver(synthesizer.getReceiver());
			 */
			sequencer.setSequence(sequence);
			// Starts to play
			sequencer.start();
			this.playing = true;
			//Wait for the sequencer to play
			while (sequencer.isRunning())
				;
			this.playing = false;
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			success = false;
		}
		return success;

	}

	private boolean developmentPlay() {
		double millisecondsPerPulse;
		boolean success;
		long eventOffset;
		long startTime;
		long eventClick;
		Iterator<MidiEvent> it;
		long currentTime;
        
		success = false;
		if (currentDevice == null) {
			this.setCurrentDevice(0,doesNotHaveAnyReceivers);
		}

		if (currentReceiver != null) {
			if (ppq == 0) {
				ppq = 16;
			}

			// Most MIDI devices need to be opened before they can be used.
			// Some are
			// always open. If we have to open it, remember we have to close
			// it (if
			// it was already open, we must not close it).
			deviceAlreadyOpened = currentDevice.isOpen();

			if (!deviceAlreadyOpened) {
				try {
					currentDevice.open();
				} catch (MidiUnavailableException e1) {
					e1.printStackTrace();
				}
			}
			playing = true;
			// compute the conversion factor for time in the events to real
			// system time
			tempo = 120;
			millisecondsPerPulse = 60000.0 / (tempo * ppq);

			it = eventList.iterator();
			startTime = System.currentTimeMillis();
			while (playing && (it.hasNext())) {
				MidiEvent e = it.next();
				eventClick = e.getTick();
				eventOffset = (long) (eventClick * millisecondsPerPulse);
				currentTime = System.currentTimeMillis() - startTime;

				if (currentTime < eventOffset) {
					try {
						// 	This timing is more accurate on some systems than
						// on
						// others, but it should be OK for the moment.
						Thread.sleep(eventOffset - currentTime);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
				try {
					currentReceiver.send(e.getMessage(), -1);
					/*
					 * DEBUG System.err.println("Playing message:" +
					 * MidiMessageToString(e.getMessage()) );
					 */
				} catch (IllegalStateException ISE) {
					Util.showDialogOk(null, "IllegalStateException", ISE.getLocalizedMessage());
					playing = false;
				}
			}

			success = true;
			this.stop();
		} else {
			System.err.println("current Devices is null");
		}
		return success;
	}

	/* close the receiver and close the device as necesary */
	public void stop() {
		if (Midi.testing) {
			if (playing) {
				currentReceiver.close();
				if (!deviceAlreadyOpened) {
					currentDevice.close();
				}
				playing = false;
			}
		} else {
			if ((sequencer != null) && (sequencer.isRunning())) {
				sequencer.stop();
			}
		}
	}

	private boolean openSynthesizer() {
		boolean success;

		success = false;
		try {
			if (synthesizer == null) {
				synthesizer = MidiSystem.getSynthesizer();

			}
			if (synthesizer == null) {
				System.err.println("getSynthesizer() failed!");
				success = false;
			} else {
				if (!synthesizer.isOpen()) {
					synthesizer.open();
				}
			}
		} catch (MidiUnavailableException MUE) {
			MUE.printStackTrace();
		}
		return success;
	}

	private boolean loadInstruments() {
		boolean success;
		Soundbank sb;

		success = false;

		openSynthesizer();
		sb = synthesizer.getDefaultSoundbank();
		if (sb != null) {
			instruments = synthesizer.getDefaultSoundbank().getInstruments();
			/*
			 * 0 = Grand Piano, 24 = Acoustic Guitar
			 */
			success = synthesizer.loadInstrument(instruments[23]);

			if (!success) {
				System.err.println("loadInstrument() failed!");
				return success;
			}
		}
		synthesizer.close();

		return success;
	}

	public String[] getInstrumentStrings() {
		String[] res;
		int max;
		String aux;
		int pos;
        
		res = null;
		if (instruments == null) {
			loadInstruments();
		}
		if (instruments != null) {
			max = instruments.length;
			res = new String[max];
			for (int i = 0; i < instruments.length; i++) {
				aux = instruments[i].toString();
				pos = aux.indexOf("Instrument");

				res[i] = new String(aux.substring(pos + "Instrument".length()));
			}
		}
		return res;
	}

	public boolean setSequencePPQ(int value) {
		boolean success;

		success = false;
		if (value > 0) {
			try {
				sequence = new Sequence(Sequence.PPQ, 16, 1);
				success = true;
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
		}
		return success;
	}

	/**
	 * This method sets the instrument and clears the current sequence setting it
	 * to 16 PPQs
	 */

	public boolean setInstrument(int instrumentNumber) {
		boolean success;
		int inst;
		MidiEvent event;
		success = true;
		tick = 0;
		//	Sequence(float divisionType, int resolution, int numTracks)
		//	PPQ = The tempo-based timing type, for which the resolution is
		// expressed
		// in
		//	pulses (ticks) per quarter note

		if (sequence == null) {
			System.err
					.println("Sequence is null, make sure you call setSequencePPQ or loadMID");
		} else {
			try {
				msg = new ShortMessage();
				channel = instrumentNumber / 128;
				inst = instrumentNumber % 128;
				msg.setMessage(ShortMessage.PROGRAM_CHANGE, channel, inst, 0);
				event = new MidiEvent(msg, 0);
				track = sequence.createTrack();
				track.add(event);

			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
				success = false;
			}
		}
		return success;
	}

	public boolean addNote(int note) {
		MidiEvent event;
		boolean success;

		success = true;
		try {
			msg = new ShortMessage();
			msg.setMessage(ShortMessage.NOTE_ON, channel, note, VELOCITY_ON);
			event = new MidiEvent(msg, tick);
			track.add(event);

			msg = new ShortMessage();
			msg.setMessage(ShortMessage.NOTE_OFF, channel, note, VELOCITY_OFF);
			event = new MidiEvent(msg, tick + DELTA);
			track.add(event);

			tick = tick + DELTA;
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	public boolean loadMID(String song) {
		boolean success;
		File myMidiFile;

		success = false;
		try {
			myMidiFile = new File(song);
			MidiFileFormat mff = MidiSystem.getMidiFileFormat(myMidiFile);
			ppq = mff.getResolution();
			sequence = MidiSystem.getSequence(myMidiFile);
			prepare();
			success = true;
		} catch (IOException IOE) {
			IOE.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return success;
	}
    /**
     * Selects the first available midi device that has receivers
     * 
     * @return true if successfull
     */

	public boolean selectFirstAvailableMidiDevice() {
		int j;
		boolean success;

		j = 0;
		success = false;
		while ((!success) && (j < this.getNumberOfMidiDevices())) {
			success = this.setCurrentDevice(j,doesNotHaveAnyReceivers);
			if (success) {
				//DEBUG System.err.println(deviceMessage());
			}
			j++;
		}
		return success;
	}
    /**
     * returns the next index available..if end of device list is reached it returns 0
     * 
     * @param i the current index
     * @return the next device index
     */
    public int nextDeviceIndex(int i ) {
        int j ;
    
        j = i + 1 ;
        if(j >= this.getNumberOfMidiDevices()) {
            j = 0 ;
        }
        
        return j ;
    }
	/**
     * Selects the next available midi device that has receivers
     * 
     * @return if the next available midi device could be set
	 */
    public boolean selectNextAvailableMidiDevice() {
        int j,orig ;
        boolean success ;
        
        
        orig = currentDeviceIndex ;
        //we start trying with the next device...
        j = nextDeviceIndex(currentDeviceIndex) ;

        success = false ;
        
        //while not successfull AND next device <> from current one...
        while ((!success) && (j != orig)) {
            //wrap the counter to the start
            success = this.setCurrentDevice(j,doesNotHaveAnyReceivers);
            if (success) {
                //DEBUG System.err.println(deviceMessage());
            }
            else {
                j = nextDeviceIndex(j) ;
            }
        }
    
        return success;
        
        
    }
    /**
     * 
     * @return a string with the current device name
     */
    public String getCurrentDeviceName() {
        return getCurrentDevice().getDeviceInfo().getName() ;
    }
    
    
    /*
     * @param errorMsg The errorMsg to set.
     
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
    */
    /**
     * @return Returns the latest errorMsg.
     */
    public String getErrorMsg() {
        return errorMsg;
    }
    public String deviceMessage(String midiDevice, String isNowPreferredMidi) {
        String res ;
        
        if(midiDevice != null){
            res = midiDevice ;
        }
        else{
            res = "Midi Device" ;
        }
        res += ":" + this.getCurrentDeviceName() + " " ;

        if(isNowPreferredMidi != null){
            res += isNowPreferredMidi ;
        }
        else{
            res += "is now the preferred device" ;
        }

        return res ;
    }
    
	/**
	 * orgnizes the midiEvents acoording to the TICK number
	 *  
	 */
	private void prepare() {
		int t;
		Track trackAux;
		int size;
		int sizeNeeded;
		Track[] tracks;

		// read the MIDI events from the sequence .
		// Get the "pulses per quarter note"
		// setting, then read all the tracks and merge them together with the
		// events in the right order

		tracks = this.sequence.getTracks();

		sizeNeeded = 0;
		for (t = 0; t < tracks.length; t++) {
			sizeNeeded += tracks[t].size();
		}
		eventList = new ArrayList<MidiEvent>(sizeNeeded);

		for (t = 0; t < tracks.length; t++) {
			trackAux = tracks[t];
			size = trackAux.size();
			for (int j = 0; j < size; j++) {
				MidiEvent e = trackAux.get(j);
				eventList.add(e);
			}
		}
		Collections.sort(eventList, new MidiEventComparator());
	}

	/*
	 * Below this start the functions that allow the conversion to String of a
	 * MID file
	 */

	/**
	 * This method converts to String a Midi command or status int, obtain from
	 * a MidiMessage
	 */
	private static String CommandStatusToString(int value) {
		int i;
		String res;
		boolean match;

		//res = null ;
		res = "UNKNOWN (" + value + ")"; //add all the types to the
		// midiCommands and Strings
		match = false;
		for (i = 0; (!match) && (i < midiCommandsStatus.length); i++) {
			match = (value == midiCommandsStatus[i]);
			if (match) {
				res = midiCommandsStatusStrings[i] + "(" + value + ")";
			}
		}

		return res;
	}

	/*
	 * If you are processing MIDI data that originated outside Java Sound and
	 * now is encoded as signed bytes, the bytes can can be converted to
	 * integers using this conversion: int i = (int)(byte & 0xFF)
	 */
	public static int convert(byte b) {
		return (b & 0xFF);
	}

	/**
	 * This is a workaround for the ShortMessage classes that are in a Sequence
	 * that do not return correct values in getLength() and getMessage().
	 * 
	 * @param status
	 * @return an integer that represents the "correct" message length
	 */
	private static int midiMessageLength(int status) {
		// doesn't work for system exclusives, but shouldn't need to.
		switch (status & 0x00F0) {
		case ShortMessage.PROGRAM_CHANGE:
		case ShortMessage.CHANNEL_PRESSURE:
			return 2;
		case 0x00F0: // realtime and sysex
			if (status == ShortMessage.SONG_SELECT)
				return 2;
			if (status >= ShortMessage.TUNE_REQUEST)
				return 1;
		default:
			return 3;
		}
	}

	private static String MidiMessageToString(MidiMessage MM) {
		String res;
		int cmd; // the midiCommand
		int chn; //the chanel
		int data1; //which might be a note ;
		int data2; //which might be the velocity of the note
		int status;
		byte[] msg;
		int length;
		int i;

		/*
		 * getLength() Obtains the total length of the MIDI message in bytes. A
		 * MIDI message consists of one status byte and zero or more data bytes.
		 * The return value ranges from: - 1 for system real-time messages, - to
		 * 2 or 3 for channel messages, - to any value for meta and system
		 * exclusive messages.
		 */
		status = MM.getStatus();
		if (MM instanceof ShortMessage) {
			ShortMessage sm = (ShortMessage) MM;
			// guess what, Java Sound is broken.
			length = midiMessageLength(status);
			msg = new byte[3];
			msg[0] = (byte) status;
			if (length > 1)
				msg[1] = (byte) sm.getData1();
			if (length > 2)
				msg[2] = (byte) sm.getData2();
		} else {
			msg = MM.getMessage();
			length = MM.getLength();
		}

		//res = "message is " + length + " bytes long, " ;

		res = "";
		if (status == MetaMessage.META) {
			//Meta message or System exclusive message
			res += "Meta message = {";
			for (i = 0; i < length; i++) {
				res += convert(msg[i]);
				if (i < length - 1) {
					res += ",";
				}
			}
			res += " }";
		} else if (length == 1) {
			//its a System Real-Time message
			res += "System Real-Time message = { ";
			res += CommandStatusToString(status);
			res += " }";
		} else if ((length == 2) || (length == 3)) {
			//2 or 3 for channel messages
			/*
			 * For channel messages, - the upper four bits of the status byte
			 * are specified by a command value - and the lower four bits are
			 * specified by a MIDI channel number.
			 * 
			 * To convert incoming MIDI data bytes that are in the form of
			 * Java's signed bytes, you can use the conversion code given in the
			 * MidiMessage class description
			 */

			res += "Channel Message = { ";

			//Obtain the upper four bits ;
			cmd = (status & 0xF0);
			res += "Command: " + CommandStatusToString(cmd);

			//obtain the lower for bits
			chn = (status & 0x0F);
			res += ", Channel: " + chn;

			data1 = convert(msg[1]);
			data2 = -1;
			if (length == 3) {
				data2 = convert(msg[2]);
			}

			if ((cmd == ShortMessage.NOTE_ON) || (cmd == ShortMessage.NOTE_OFF)) {
				//when is a NOTE_X message data1 is the note and data2 the
				// velocity, and the lenght is always 3
				res += ", Note: " + data1;
				res += ", Velocity: " + data2;
			} else {
				res += ", Data1: " + data1;
				if (length == 3) {
					res += ", Data2: " + data2;
				}
			}
			res += " }";
		} else {
			//Meta message or System exclusive message
			res += "Meta message = {";
			for (i = 0; i < length; i++) {
				res += convert(msg[i]);
				if (i < length - 1) {
					res += ",";
				}
			}
			res += " }";
		}
		return res;
	}

	//MidiEvent to String.
	public static String MidiEventToString(MidiEvent ME) {
		String res;
		MidiMessage MM;
		//ShortMessage SM ;
		//int i = (int)(byte & 0xFF)

		//msg = new ShortMessage();
		//msg.setMessage(ShortMessage.NOTE_ON, channel, note, VELOCITY_ON);
		//event = new MidiEvent(msg, tick);

		res = "";
		res += "Tick: " + ME.getTick();
		MM = ME.getMessage();
		//CURRENT WORK
		//SM = new ShortMessage(MM.getMessage()) ;
		res += ", " + MidiMessageToString(MM);

		return res;
	}

	/**
	 * This method return a Vector of MidiTrackEvents, usefull when you want to
	 * process events or display them in another way
	 */

	public java.util.Vector<MidiTrackEvent> SequenceToMidiTrackEvents() {
		Track tracks[];
		MidiEvent ME;
		int t;
		int e;
		java.util.Vector<MidiTrackEvent> V;
		MidiTrackEvent MTE;

		V = new java.util.Vector<MidiTrackEvent>(0, 1);
		tracks = this.sequence.getTracks();

		int maxEvents;

		maxEvents = 0;
		for (t = 0; t < tracks.length; t++) {
			maxEvents = Math.max(maxEvents, tracks[t].size());
		}
		for (e = 0; e < maxEvents; e++) {
			for (t = 0; t < tracks.length; t++) {
				if (e < tracks[t].size()) {
					ME = tracks[t].get(e);
					MTE = new MidiTrackEvent(t, e, ME);
					V.add(MTE);

				}
			}
		}
		/*
		 * This iterates the array in a different way for(t = 0 ; t <
		 * tracks.length ; t++) { for(i = 0 ; i < tracks[t].size() ; i++) { ME =
		 * tracks[t].get(i) ; MTE = new MidiTrackEvent(t,i,ME); if(toString) {
		 * V.add(MTE.toString()) ; } else { V.add(MTE) ; } } }
		 */
		return V;
	}

	public boolean isPlaying() {
		return (playing);
		//return (sequencer.isRunning()) ;
	}
}