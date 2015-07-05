/*
 * Created on 12-mar-2005
 *
 */
package dguitar.codecs.guitarPro.statistics;

import dguitar.codecs.guitarPro.GPEffectsOnBeat;


/**
 * this class calculates statistics for EfffectsOnBeat
 */

public class GPStatsEffectsOnBeat extends GPStats {
	public int downStroke;

	public int upStroke;

	public int pickStrokes;

	public int tremoloBar;

	public int hasRasgueado;

	public int popping;

	public int slapping;

	public int tapping;

	//number of effects
	private int numEffects;

	public GPStatsEffectsOnBeat() {
		this.numEffects = 0;
		downStroke = 0;
		upStroke = 0;
		pickStrokes = 0;
		tremoloBar = 0;
		hasRasgueado = 0;
		popping = 0;
		slapping = 0;
		tapping = 0;
	}

	public GPStatsEffectsOnBeat(GPEffectsOnBeat EOB) {
		this.numEffects = 1;
		downStroke = (EOB.downStroke != null) ? 1 : 0;
		upStroke = (EOB.upStroke != null) ? 1 : 0;
		pickStrokes = (EOB.pickStrokes != null) ? 1 : 0;
		tremoloBar = (EOB.tremoloBar != null) ? 1 : 0;
		hasRasgueado = (EOB.hasRasgueado) ? 1 : 0;
		popping = (EOB.popping) ? 1 : 0;
		slapping = (EOB.slapping) ? 1 : 0;
		tapping = (EOB.tapping) ? 1 : 0;
	}

	public void add(GPStatsEffectsOnBeat a) {
		if (a != null) {
			this.numEffects += a.numEffects;
			this.downStroke += a.downStroke;
			this.upStroke += a.upStroke;
			this.pickStrokes += a.pickStrokes;
			this.tremoloBar += a.tremoloBar;
			this.popping += a.popping;
			this.slapping += a.slapping;
			this.tapping += a.tapping;
		}
	}

	public String toString() {
		String res;
		String aux;

		aux = "effects";
		//DEBUG
		//System.out.println("DEBUG = " + level) ;
		res = common.Util.tabs(level) + numEffects + " " + aux + "\n";
		level++;
		res += stringFor(downStroke, "downStrokes", numEffects, aux);
		res += stringFor(upStroke, "upStrokes", numEffects, aux);
		res += stringFor(pickStrokes, "pickStrokes", numEffects, aux);
		res += stringFor(tremoloBar, "tremoloBars", numEffects, aux);
		res += stringFor(popping, "poppin", numEffects, aux);
		res += stringFor(slapping, "slapping", numEffects, aux);
		res += stringFor(tapping, "tapping", numEffects, aux);

		/*
		if (empty) {
			res = common.Util.tabs(level) + "All Statistics of EffectsOnBeat are Empty(0)"+ "\n";
		}
		*/
		level--;
		return res;
	}
}