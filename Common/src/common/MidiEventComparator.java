package common;

import java.util.Comparator;

import javax.sound.midi.MidiEvent;

public class MidiEventComparator 
implements Comparator<MidiEvent>,LicenseString 
{
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}

	/*
		public int compare(Object arg0, Object arg1) {
			int resp ;
			MidiEvent m0 ;
			MidiEvent m1 ;
			long t0 ;
			long t1 ;
			
			resp = 0 ;
			t0 = 0 ;
			t1 = 0 ;
			if( (arg0 != null) && (arg1 != null) ) {
				m0 = new MidiEvent(null,0) ;
			
				if ( (arg0.getClass().isInstance(m0)) && (arg1.getClass().isInstance(m0)) ) {
			
					m0 = (MidiEvent) arg0;
					m1 = (MidiEvent) arg1;

					t0 = m0.getTick();
					t1 = m1.getTick();
				}
				
			if (t0 < t1)
				resp = -1;
			if (t0 > t1)
				resp = 1;
			}
			return resp;
		}
		*/

		@Override
		public int compare(MidiEvent m0, MidiEvent m1) {
			int resp ;
			long t0 ;
			long t1 ;
			
			t0 = m0.getTick();
			t1 = m1.getTick();
			
			resp = 0 ;
			
			if (t0 < t1)
			{
				resp = -1;
			}
			else if (t0 > t1)
			{
				resp = 1;
			}
			return resp ;
		}

	}
