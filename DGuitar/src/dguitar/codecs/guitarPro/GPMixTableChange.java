package dguitar.codecs.guitarPro;

/** 
 * This class describes a mix table change.
 * 
 * @author Matthieu Wipliez
 */
public class GPMixTableChange {
    /**
     * Gives the changes about the balance.
     */
    public GPMixTableElement balance;
    
    /**
     * Gives the changes about the chorus.
     */
    public GPMixTableElement chorus;
    
	/**
	 * Gives the changes about the instrument.
	 */
	public GPMixTableElement instrument;

    /**
     * Gives the changes about the phaser.
     */
    public GPMixTableElement phaser;

    /**
     * Gives the changes about the reverb.
     */
    public GPMixTableElement reverb;

    /**
     * Gives the changes about the tempo.
     */
    public GPMixTableElement tempo;

    /**
     * Gives the changes about the tremolo.
     */
    public GPMixTableElement tremolo;

	/**
	 * Gives the changes about the volume.
	 */
	public GPMixTableElement volume;

	/**
	 * Creates a new MixTableChange.
	 *
	 */
	public GPMixTableChange() {
		balance = null;
		chorus = null;
		instrument = null;
		phaser = null;
		reverb = null;
		tempo = null;
		tremolo = null;
		volume = null;
	}
    
    /**
     * Returns a string representation for this GPMixTableChange.
     * @see java.lang.Object#toString()
     */
	public String toString() {
		String res ;

		res = "empty MixTableChange" ;
		if(balance != null) {
			res = "balance" + ": " + balance.toString() ;
		}
		if(chorus != null) {
			res += ", chorus" + ": " +chorus.toString() ;
		}
		if(instrument != null) {
			res += ", instrument" + ": " +instrument.toString() ;
		}
		if(phaser != null) {
			res += ", phaser" + ": " +phaser.toString() ;
		}
		if(reverb != null) {
			res += ", reverb" + ": " +reverb.toString() ;
		}
		if(tempo != null) {
			res += ", tempo" + ": " +tempo.toString() ;
		}
		if(tremolo != null) {
			res += ", tremolo" + ": " +tremolo.toString() ;
		}
		if(volume != null) {
			res += ", volume" + ": " +volume.toString() ;
		}

		return res ;
	}
}