/*
 * Created on 03-mar-2005
 *
 */
package common;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 * @author Mauricio Gracia Gutiérrez
 *  
 */
public class Util
implements LicenseString

{
	public String getLicenseString() {
		return "This file is covered by the GNU GENERAL PUBLIC LICENSE, Version 2, June 1991";
	}

    public static final String internetURLs[] =    {
        "http://", "ftp://"
    } ;
    /** The variable that contains the error, if any */
    private static UtilErrors error = UtilErrors.NO_ERROR;

    /**
     * Returns the N index of a String
     * 
     * @param str			the String were the search will be perfomed 
     * 		Example: "http://yoursitehere.com/user/files"
     * @param strToSearch	the String to search for
     * 		Example: "/"
     * 
     * @param whichOcurrence the ocurrence number you wish to find
     * 		Example: 3
     * 
     * @return Returns the N index of a String
     *      Example: 23 ..since position 23 is the 3rd occurence of "strToSearch" inside "str" the  
     */
    public static int nIndexOf(String str, String strToSearch, int whichOcurrence) {
    	int index ;
    	int currentIndex ;
    	int prevIndex ;
    	int count ;
    	boolean found ;
    	
    	index = -1 ;
    	if(whichOcurrence >= 1) {
    		prevIndex = -1 ;
    		currentIndex = -1 ;
    		count = 1 ;
    		do {
    			prevIndex = currentIndex ;
    			currentIndex = str.indexOf(strToSearch, currentIndex+1) ;
    			found = (currentIndex >= 0) ;
   				count++ ;
    		} while (found && (count <= whichOcurrence)) ;
    		if(!found && (count > whichOcurrence)) {
    			if(prevIndex >= 0) {
    				index = (-1)*prevIndex ;
    			}
    		}
    		index = currentIndex ;
    	}
    	
    	return index ;
    }
    public static int indexInternetURL(String str) {
        int res ;
        int which ;
        String internetStr ;
        res = -1 ;
        
        if( (str != null) && (str.length() > 0) )
        {
            which = 0 ;
            do {
                internetStr = internetURLs[which] ;
                if(str.startsWith(internetStr )) {
                    res = internetStr.length() ;
                }
                else {
                    which++ ;
                }
            } while ( (which < internetURLs.length) && (res == -1)) ; 
        }
        return res ;
    }
    /**
     * This method changes a URL to make it more compact and readable depending on the desired maxLength
     *  
     * @param url
     * @param maxLength
     * @return a compacted but readable URL see examples below
     * 
     *
     * 
     *For internet files
     *  FROM:
     *   "http://www.mysongbook.com/files/c/clapt/Clapton%2C%20Eric%20-%20Tears%20In%20Heaven%20%282%29.gp4"
     *  TO:
     *   "http://www.mysongbook.com/.../Clapton, Eric - Tears In Heaven (2).gp4"
     *   
     *For local files...
     *  FROM:
     *    "c:\myfiles\guitar\clasical\artistname\etc\example.gp4"
     *  TO:
     *    "c:\myfiles\...\example.gp4"
     *
     */
    
    public static String compactAndReadableURL(String url, int maxLength) {
    	String res ;
    	int firstCut ;
        int length ;
        String urlOrig ;
        int ocurrence ;

        res = "" ;
        urlOrig = new String(url) ;
        length = urlOrig.length() ;
        if(maxLength < length) {
            if(maxLength < 10) {
                maxLength = 10 ;
            }
            firstCut = indexInternetURL(urlOrig) ;

            //if the beginin is not an internet URL
            if(firstCut == -1) {
                ocurrence = 2 ;
            }
            else {
                //interpret %20 as " " since is the 32 ascci code
                ocurrence = 3 ;
            }
            //determine the first cut
            firstCut = nIndexOf(urlOrig,File.separator,ocurrence) + 1 ;

            //res starts with 'firstcut' byte or urlOrig
            res = urlOrig.substring(0,firstCut) + "..." ;
            
            //res is concatenated with the rest of the string
            res = res + urlOrig.substring(length - maxLength - firstCut - 3 ) ;
        }
        else {
            res = urlOrig ;
        }
        
        return  res ;
        /*

 int    indexOf(int ch)
          Returns the index within this string of the first occurrence of the specified character.
 int    indexOf(int ch, int fromIndex)
          Returns the index within this string of the first occurrence of the specified character, starting the search at the specified index.
 int    indexOf(String str)
          Returns the index within this string of the first occurrence of the specified substring.
 int    indexOf(String str, int fromIndex)
          Returns the index within this string of the first occurrence of the specified substring, starting at the specified index.
 String     intern()
          Returns a canonical representation for the string object.
 int    lastIndexOf(int ch)
          Returns the index within this string of the last occurrence of the specified character.
 int    lastIndexOf(int ch, int fromIndex)
          Returns the index within this string of the last occurrence of the specified character, searching backward starting at the specified index.
 int    lastIndexOf(String str) 
         * 
         * 
         */
    }
    /**
     * Checks if F is a file, if it exists and can be read
     */

    public static boolean validFile(File f) {
        boolean valid;

        error.setDetails("File passed to method validFile()");
        valid = false;
        if (f == null) {
            error = UtilErrors.PARAM_NULL;
        } else {
            if (!f.exists()) {
                error = UtilErrors.DOES_NOT_EXIST;
            } else {
                if (!f.isFile()) {
                    error = UtilErrors.NOT_A_FILE;
                } else {
                    if (!f.canRead()) {
                        error = UtilErrors.NOT_READABLE;
                    } else {
                        valid = true;
                    }
                }
            }
            error.setDetails(f.getAbsolutePath());
        }
        return (valid);
    }

    public static UtilErrors getError() {
        return error;
    }

    /*
     * 2 to the x
     */
    public static int pow2(int x) {
        int res;
        int i;

        res = 1;
        for (i = 0; i < x; i++) {
            res = 2 * res;
        }

        return res;
    }

    /**
     * Creates a String of N tabs ("\t")
     */
    public static String tabs(int n) {
        String res;
        int i;

        i = 0;
        res = "";
        for (i = 0; i < n; i++) {
            res += "\t";
        }

        return res;
    }
    /**
     * A method that displays a MODAL dialog with OK and the messages that you decide
     * @param parent
     * @param title
     * @param message
     */
    public static void showDialogOk(java.awt.Component parent, String title,
            Object message) {
        JOptionPane.showMessageDialog(parent, message, title,
                JOptionPane.INFORMATION_MESSAGE);
    }
    /**
     * A method that creates a dialog the message/object that you decide
     * 
     * @param parent        The parent object of the one created
     * @param msgORcomp     The message or Component to display/add as content
     * @param title         The title of the dialog
     */
    public static JDialog createDialog(java.awt.Frame parent, Object msgORcomp, String title, boolean modal) {
        //METHOD UNDER CONSTRUCTION
        JDialog jdialog ;
        JLabel jlabel ;
        Container content ;
        Component comp ;
        
    	jdialog = new JDialog(parent,title,modal) ;
        content = jdialog.getContentPane() ;
        
        content.setLayout(new BorderLayout()) ;
        
        if(msgORcomp instanceof Component) {
            comp = (Component)msgORcomp ;
        }
        else { 
            //if the msgORcomp is not one of the expected instances
            if(! (msgORcomp instanceof String) ){
                msgORcomp = "msgORcomp must be String or Component (or subclass of Component) " ;
            }
            
            //create a label that contains the string
            jlabel= new JLabel((String)msgORcomp) ;
            
            //more space around to the border of the label
            jlabel.setBorder(BorderFactory.createMatteBorder(20,20,20,20,jlabel.getBackground())) ;

            comp = jlabel ;    
        }
        //add the component at the CENTER of the conent
        content.add(comp,BorderLayout.CENTER ) ;
        
        //Pack the components so the dialog can fit them
    	jdialog.pack() ;
        
//      center the component based on the parent size
        centerComponentToParent(jdialog,parent) ;
    	
        jdialog.setResizable(false) ;
        return jdialog ;
    }

    /**
     * Closes and input stream and returns and error (null if successfull)
     */
    public static String closeIS(InputStream IS) {
        String res;

        res = null;

        if (IS != null) {
            try {
                IS.close();
            } catch (IOException IOE) {
                res = IOE.toString();
            }
        }

        return res;
    }

    /* not used yet */
    public static void waitFor(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException IE) {

        }
    }

    /**
     * 
     * 
     * @param fieldName -
     *            The name of the field to be placed in the String "Vibrato" or
     *            null
     * @param object -
     *            The object that contains the value to be placed in the String
     * @param firstField -
     *            Is this the first field ? (this is changed if both fieldName
     *            and object are not null
     * 
     * @return a string with "fieldName: object" if object is not boolean or
     *         "fieldName" otherwise.
     */
    public static String fieldFor(String fieldName, Object object,
            Boolean firstField) {
        String resp;
        Boolean booleanField;

        resp = "";
        if (object != null) {
            booleanField = new Boolean(false);
            //if its not a BOOLEAN field...then...
            if (!object.getClass().isInstance(booleanField)) {

                resp = (fieldName != null) ? fieldName + ": " : "";

                //if its the first field, start keep the RESP value
                if (firstField.booleanValue()) {
                    firstField = new Boolean(false);
                } else {
                    resp = ", " + resp;
                }
                resp += object ;
            } else {
                booleanField = (Boolean) object;
                if (booleanField.booleanValue()) {
                    firstField = new Boolean(false);
                    resp = fieldName;
                }
            }
        }
        return resp;
    }

    public static String fieldFor(String fieldName, boolean aFlag,
            Boolean firstField) {
        return fieldFor(fieldName, new Boolean(aFlag), firstField);
    }

    public static void drawLine(Graphics g , Point a, Point b) {
    	g.drawLine(a.x,a.y,b.x,b.y) ;
    }
    /**
     * Returns a point that represents the center of component C
     * 
     * @param c The component to be used to calculate the center
     * @return a point that represents the center of component C
     */
    public static Point centerOfComponent(Component c) {
        Point p ;

        p = null ;
        
        if(c != null) {
            p = new Point();
            p.x = c.getX() + c.getWidth()/2 ;
            p.y = c.getY() + c.getHeight()/2 ;
        }
        
        return p ;
    }
    /**
     * Center a component to te center of the Parent
     * @param c         The component to Center
     * @param Parent    The parent to be used to center C
     */
    public static void centerComponentToParent(Component c, Component Parent) {
        centerComponent(c,centerOfComponent(Parent),c.getSize()) ;
    }
    /**
     * Centers component C at point P, using Dim as current dimension
     * 
     * @param c - is the component
     * @param p - is the Point that represents the center to use
     * @param dim - is the size of the component
     */
    public static void centerComponent(Component c, Point p, Dimension dim) {
    	if( (c != null) && (p != null) && (dim != null) ) {
    		c.setBounds(p.x - dim.width / 2, p.y - dim.height / 2, dim.width,dim.height);
    	}
    }

    public static void drawString(Graphics g, String text, Point p) {
    	g.drawString(text, p.x, p.y);
    }

    /**
     * Returns a Point were the method Util.drawString(text, Point) can be
     * called
     * 
     * @param g
     * @param text
     * @param x
     * @param y
     * @return returns a Point that can be used to center the string
     *
     */
    public static Point centerString(Graphics g, String text, int x, int y) {
    	int dx;
    	int fontAscent;
    	Point p;
    
    	p = null;
    	if (g != null) {
    		//calculate the widht of the text
    		dx = g.getFontMetrics().stringWidth(text);
    		//calculate the font Ascent of the text
    		fontAscent = g.getFontMetrics().getAscent();
    
    		p = new Point();
    		// to center the text horizontally
    		p.x = x - dx / 2 + 1;
    		// to place the base of the font at y
    		p.y = y - fontAscent;
    	}
    	return (p);
    }
}