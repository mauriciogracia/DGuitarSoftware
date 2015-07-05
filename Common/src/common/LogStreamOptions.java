package common;

public class LogStreamOptions
implements LicenseString
{
	public static final short	NO_LOG = 0 ;
	
	public static final short	LOG_TO_FILE_ONLY = 1 ;
	
	public static final short	LOG_TO_FILE_AND_WINDOW = 2 ;
	
	public static final short	LOG_TO_WINDOW_ONLY = 3 ;
	
	private short typeOfLog ;
	
	private String initMsg ;
	
	private String filePath ;

	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}

	private void commonConstructor(short type, String initialMessage) {
		this.typeOfLog = type ;
		this.initMsg = initialMessage ;
	}

	public LogStreamOptions(short type, String initialMessage) {
		commonConstructor(type,initialMessage) ;
	}
	/**
	 * Creates a LosStreamOptions object with specified parameters
	 * @param type		the type of log you want to create
	 * @param initialMessage	the initial message to place at beging of log
	 * @param path		the path were the log will be saved
	 */
	public LogStreamOptions(short type, String initialMessage, String path) {
		commonConstructor(type,initialMessage) ;
		this.setFilePath(path) ;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getInitMsg() {
		return initMsg;
	}

	public void setInitMsg(String initMsg) {
		this.initMsg = initMsg;
	}

	public short getTypeOfLog() {
		return typeOfLog;
	}

	public void setTypeOfLog(short typeOfLog) {
		this.typeOfLog = typeOfLog;
	}
	/**
	 * Returns true if this LogStream stores messages on a File
	 * 
	 * @return true if this LogStream stores messages on a File
	 */
	public boolean isFileType() {
		return ( (this.typeOfLog == LOG_TO_FILE_AND_WINDOW) || (this.typeOfLog == LOG_TO_FILE_ONLY) )  ;
	}
	
	/**
	 *	Returns true if <code>type</code> of LogStream is stored on a file 
	 * @param type The type of log to be evaluated
	 * @return true if <code>type</code> of LogStream is stored on a file
	 */
	
	public static boolean isFileType(short type) {
		return ( (type == LOG_TO_FILE_AND_WINDOW) || (type == LOG_TO_FILE_ONLY) )  ;
	}
	/**
	 * Returns true if this LogStream shows messages on a Window
	 * @return true if this LogStream shows messages on a Window
	 */
	public boolean isWindowType() {
		return ( (this.typeOfLog == LOG_TO_FILE_AND_WINDOW) || (this.typeOfLog == LOG_TO_WINDOW_ONLY) )  ;
	}
	/**
	 * Returns true if <code>type</code> LogStream shows messages on a Window
	 * 
	 * @param type	The type of log to be evaluated
	 * @return true if <code>type</code> LogStream shows messages on a Window
	 */
	public static boolean isWindowType(short type) {
		return ( (type == LOG_TO_FILE_AND_WINDOW) || (type == LOG_TO_WINDOW_ONLY) )  ;
	}
}
