package dguitar.codecs.guitarPro.statistics;

import java.io.File;


import common.FolderIteratorInfo;
import common.FolderIteratorListener;

import dguitar.codecs.guitarPro.GPFormatException;
import dguitar.codecs.guitarPro.GPInputStream;
import dguitar.codecs.guitarPro.GPSong;

public class GPStats implements FolderIteratorListener {
	//for statistics of a folder
	GPStatsPiece PieceCumulative;

	private boolean detailed;

	//how many "\t" are shown
	protected static int level = 0;

	String msg;

	private String results;


	/**
	 * this class obtains statistics from a GPSong or from a folder that
	 * contains Guitar Pro files The statistics can be obtained using the
	 * stats() method
	 */
	public GPStats() {
	}

	/**
	 * Calculates de porcentaje of a value
	 */
	private static float porcentaje(int v, int max) {
		return (v * 100) / max;
	}

	/**
	 * creates a String that is used for output os statistics
	 */
	protected String stringFor(int value, String sValue, int max, String sMax) {
		String res;

		res = "";
		if (value != 0) {
			res += common.Util.tabs(level);
			res += value + "/" + max + " ";
			res += "(" + sValue + "/" + sMax + ") = ";
			res += porcentaje(value, max) + "%";
			res += "\n";
		}
		return res;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Common.FolderIterator#actionForFile()
	 */
	public void actionForFile(File file,FolderIteratorInfo FII) {
		GPSong p;
		GPStatsPiece pieceInfo;
		GPInputStream GIS;
		boolean error;
		String aux;

		error = false;
		try {

			GIS = new GPInputStream(file);
			//WORKS p = GIS.readPiece();
			p = (GPSong) GIS.readObject();
			if (p != null) {
				pieceInfo = new GPStatsPiece();
				pieceInfo.getStats(p, file.getAbsolutePath());
				level++;
				results += pieceInfo.toString();
				level--;
				PieceCumulative.add(pieceInfo);
			} else {
				msg = "GPInputStream.readPiece() failed";
				error = true;
			}
		} catch (GPFormatException GPFE) {
			error = true;
			msg = GPFE.toString();
		} catch (java.io.IOException FNFE) {
			error = true;
			msg = FNFE.getLocalizedMessage();
		}

		if (error) {
			aux = "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!";
			aux += "\n\tFILE >" + file + "< was not processed\n";
			aux += "\n\tERROR:" + msg;
			aux += "\n!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n";
			results += aux;
			//
			//System.err.println(aux);

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Common.FolderIterator#actionRunInit()
	 */
	public void actionRunInit(FolderIteratorInfo FII) {
		level = 0;
		results = "Statistics for" + " \'" + FII.getStartigPath() + "\'\n";
		PieceCumulative = new GPStatsPiece();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Common.FolderIterator#actionRunFinished()
	 */
	public void actionRunFinished(FolderIteratorInfo FII) {
		for (int j = 0; j < 3; j++) {
			results += "==========================";
		}
		results += "\n";
		results += "Totals for" + " \'" + FII.getStartigPath() + "\'\n";
		results += "\t" + FII.getCountFiles() + " " + "files" + "\n";
		results += "\t" + FII.getCountFolders() + " " + "folders" + "\n";
		results += PieceCumulative.toString();
	}

	/**
	 * @param results
	 *            The results to set.
	 */
	public void setResults(String results) {
		this.results = results;
	}

	/**
	 * @return Returns the results.
	 */
	public String getResults() {
		return results;
	}

	/**
	 * @param detailed
	 *            The detailed to set.
	 */
	public void setDetailed(boolean detailed) {
		this.detailed = detailed;
	}

	/**
	 * @return Returns the detailed.
	 */
	public boolean isDetailed() {
		return detailed;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see common.FolderIteratorListener#actionForFolder(java.io.File,
	 *      java.io.File)
	 */
	public void actionForFolder(File folder, File path,FolderIteratorInfo FII) {
		// TODO Auto-generated method stub

	}
}