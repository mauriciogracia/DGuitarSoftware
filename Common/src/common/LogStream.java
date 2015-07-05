/*
 * LogStream.java
 *
 * Created on 29 de diciembre de 2004, 02:39 AM
 */

package common;

import java.awt.Image;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *
 * @author Mauricio Gracia Gutiérrez
 */
public class LogStream 
extends PrintStream
implements LicenseString
{
	private ScrollText ST ;
	private PrintWriter PW ;
	private LogStreamOptions LSO ;
	
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}

	public LogStream(OutputStream os, LogStreamOptions lso) throws FileNotFoundException {
		super(os) ;
		
		this.PW = null ;
		this.ST = null ; 
		this.LSO = lso ;
		
		if(lso == null) {
			// throw a NullPointerException
			new NullPointerException("LogStreamOptions is null") ;
		}
		
		if(LSO.getTypeOfLog() == LogStreamOptions.NO_LOG) {
			
		}
		else {
//			log will be shown on a ScrollText window
			if(LSO.getTypeOfLog() >= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) { 
				windowInit(lso.getInitMsg()) ;
			}
			if(LSO.getTypeOfLog() <= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
				if(LSO.getFilePath() != null) {
					
					this.PW = new PrintWriter(new OutputStreamWriter(new FileOutputStream(LSO.getFilePath()) ) ) ;
					this.PW.println(lso.getInitMsg()) ;
				}
			}
		}
	}
	/**
	 * Returns the type of log for this logStream
	 * 
	 * @return a type from LogStreamOptions
	 */
	public short getTypeOfLog() {
		return this.LSO.getTypeOfLog() ;
	}
	public boolean isFileType() {
		return this.LSO.isFileType() ;
	}
	
	public boolean isWindowType() {
		return this.LSO.isWindowType() ;
	}

	/**
	 * Switches between current type of login to the desired one
	 * 
	 * @param type
	 * @throws FileNotFoundException 
	 */
	public void switchTo(short type, String windowTitle,boolean isClosable, boolean doExit) throws FileNotFoundException
	{
		short prevType ;
		
		prevType = LSO.getTypeOfLog() ;
		
		if(prevType == type) { 
			return ;
		}
		//ELSE
		
		//if previous type is Window
		if(LogStreamOptions.isWindowType(prevType)) {
			//and current type is File only or no log
			if( ! LogStreamOptions.isWindowType(type) ) { 
				this.closeWindow() ;
				this.ST = null ;
			}
		}
		//If previous type is file
		if (LogStreamOptions.isFileType(prevType)) {
			//and current type does not use a file
			if(!LogStreamOptions.isFileType(type)) {
				this.PW.close() ;
				this.PW = null ;
			}
		}
		
		//set the current type
		LSO.setTypeOfLog(type) ;
		
		//if current type needs a window
		if(LSO.isWindowType()) {
			//and previous type did NOT USED a window already 
			if( ! LogStreamOptions.isWindowType(prevType)) {
				windowInit(LSO.getInitMsg()) ;
				this.setTitle(windowTitle) ;
				this.setClosable(isClosable, doExit) ;
			}
		}
		//if current type needs a file
		if(LSO.isFileType()) {
			//and previous type did NOT USED a file
			if(! LogStreamOptions.isFileType(prevType)) {
				if(LSO.getFilePath() != null) {
					this.PW = new PrintWriter(new OutputStreamWriter(new FileOutputStream(LSO.getFilePath()) ) ) ;
					this.PW.println(LSO.getInitMsg()) ;
				}
			}
		}
	}
	
	/**
	 * Initializes the Log Window to a fixed behavior
	 *  
	 * @param initialMsg
	 */
	private void windowInit(String initialMsg) {
		if(initialMsg != null) {
			ST = new ScrollText(initialMsg ) ;
		}
		else {
			ST = new ScrollText() ;
		}
		//The log is NOT editable
		ST.setEditable(false) ;
		ST.setDisabledColor() ;
		ST.setClosable(true,true) ;
		ST.addClearLog() ;
		ST.setVisible(true) ;
	}
	/*
	 * Only to be used when loging to a Window and adding a logo on top
	 * 
	 * @param imageName
	 */
	/*
	public void addLogo(String imageName) {
		if(LSO.getTypeOfLog() >= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			ST.addLogo(imageName) ;
		}
	}
	*/
	/**
	 * Set the title of the Log Window
	 * 
	 * @param str
	 */
	public void setTitle(String str) {
		if(LSO.getTypeOfLog() >= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			ST.setTitle(str) ;
		}
	}
	/**
	 * Method used to set the logWindow to closable or not 
	 * @param isClosable	can user close the log window
	 * @param doExit		perform a System.exit after closing
	 * 
	 */
	public void setClosable(boolean isClosable, boolean doExit) {
		if(LSO.getTypeOfLog() >= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			ST.setClosable(isClosable,doExit) ;
		}
	}
	
	public void println() {
		if(LSO.getTypeOfLog() >= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			this.ST.append("\n") ;
			this.ST.setVisible(true) ;
		}
		if(LSO.getTypeOfLog() <= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			if(LSO.getTypeOfLog() == LogStreamOptions.NO_LOG) {
				
			}
			else {
				this.PW.println()  ;
			}
		}
	}
	/**
	 *  Prints a String and then it calls println()
	 */     
	
	public void print(String s) {
		if(LSO.getTypeOfLog() >= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			this.ST.append(s) ;
		}
		if(LSO.getTypeOfLog() <= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			if(LSO.getTypeOfLog() == LogStreamOptions.NO_LOG) {
				
			}
			else {
				this.PW.print(s) ;
			}
		}
		this.println() ;
	}
	/**
	 *  is the same as print(String)
	 */     
	public void println(String s) {
		this.print(s) ;
	}
	
	public void println(Object obj) {
		String aux ;
		
		aux = "null" ;
		if(obj != null) {
			aux = obj.toString() ;
		}
		this.println(aux) ;
	}
	/**
	 * is the same as <code>this.println(obj) ;<code>
	 */
	public void print(Object obj) {
		this.println(obj) ;
	}
	/**
	 * Use this method to add a string with "\n" to the log, without bringing the window forward (if any)
	 * 
	 * @param s		The String to be added
	 */
	public void log(String s) {
		if(LSO.getTypeOfLog() >= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			this.ST.append(s+ "\n") ;
		}
		if(LSO.getTypeOfLog() <= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			if(LSO.getTypeOfLog() == LogStreamOptions.NO_LOG) {
				
			}
			else {
				this.PW.println(s) ;
			}
		}
	}
	/**
	 * Use this method to add a string with to the log, without bringing the window forward (if any)
	 * 
	 * @param s		The String to be added
	 */
	public void logNoNewLine(String s) {
		if(LSO.getTypeOfLog() >= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			this.ST.append(s) ;
		}
		if(LSO.getTypeOfLog() <= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			if(LSO.getTypeOfLog() == LogStreamOptions.NO_LOG) {
				
			}
			else {
				this.PW.print(s) ;
			}
		}
	}
	/* (non-Javadoc)
	 * @see java.io.OutputStream#close()
	 */
	public void close() {
		super.close() ;
		if(LSO.getTypeOfLog() <= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			if(LSO.getTypeOfLog() == LogStreamOptions.NO_LOG) {
				
			}
			else {
				this.PW.close() ;
			}
		}
	}
	/* (non-Javadoc)
	 * @see java.io.OutputStream#flush()
	 */
	public void flush() {
		super.flush();
		if(LSO.getTypeOfLog() <= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			if(LSO.getTypeOfLog() == LogStreamOptions.NO_LOG) {
				
			}
			else {
				this.PW.flush() ;
			}
		}
	}
	/*
	public void removeLogo() {
		if(LSO.getTypeOfLog() >= LogStreamOptions.LOG_TO_FILE_AND_WINDOW) {
			ST.removeLogo() ;
		}
	}
	*/
	/**
	 * Close the window, usefull when switching from one type of log to another
	 */
	public void closeWindow() {
		if(ST != null) {
			ST.setVisible(false) ;
			ST.dispose() ;
		}
		else { 
			this.print("Hidding window") ;
		}
	}
	/**
	 * Sets the iconImage for a log window..on a file..does nothing
	 * @param imgIcon the icon to be used
	 */
	public void setIconImage(Image imgIcon) { 
		if(ST != null) {
			ST.setIconImage(imgIcon) ;
		}
	}
}
