/*
 * Created on 03-mar-2005
 *
 */
package common;

/**
 * @author Mauricio Gracia Gutiérrez
 * 
 */
public class UtilErrors
implements LicenseString
{
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}

	private int errorCode ;
    private String detail ;
    
	/** No error happend */
	public static final UtilErrors NO_ERROR = new UtilErrors(0) ;
	
	/** UNKNOWN error */
	public static final UtilErrors UNKNOWN_ERROR = new UtilErrors(1) ;

	/** A required parameter is null */
	public static final UtilErrors PARAM_NULL = new UtilErrors(2) ;

	/** The OBJECT (file,folder,etc..) does not exist */
	public static final UtilErrors DOES_NOT_EXIST = new UtilErrors(3) ;	

	/** The File Object is not a File */
	public static final UtilErrors NOT_A_FILE = new UtilErrors(4) ;

	/** The OBJECT is not readable */
	public static final UtilErrors NOT_READABLE = new UtilErrors(5) ;
	

	private static final UtilErrors errors[] = {
			NO_ERROR,
			UNKNOWN_ERROR,
			PARAM_NULL,
			DOES_NOT_EXIST,
			NOT_A_FILE,
			NOT_READABLE
			} ;

	private String errorStrings[] = {
			"NO ERROR",
			"UNKNOWN ERROR",
			"PARAMETER IS NULL",
			"DOES NOT EXIST",
			"IS NOT A FILE",
			"IS NOT READABLE",
	};

	public UtilErrors(int error) {
		this.errorCode = error ;
		this.detail = "NO MORE DETAILS - " ;
	}

	public int getErrorCode() {
		return this.errorCode ;
	}
	public String toString() {
		String resp ;
		
		resp = this.errorStrings[1] ; //UNKNOWN ERROR
		if( (this.errorCode == 0) || ((this.errorCode > 1) && (errorCode < errors.length)) ) {
			resp = this.errorStrings[this.errorCode] ;
		}
		
		resp = this.detail + " " + resp ;
		
		return resp ;
	}
	public void setDetails(String s) {
	    this.detail = s ;
	}
}
