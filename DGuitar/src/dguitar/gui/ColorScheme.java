/*
 * ColorScheme.java
 *
 * Created on 2 de marzo de 2005, 10:34 PM
 */

package dguitar.gui;

import java.awt.Color;

import dguitar.codecs.guitarPro.GPDuration;

/**
 * This class lets you associate durations of beats or notes to a color
 * 
 * a ColorScheme has at this moment 8 colors inside or 1 unique color.
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public class ColorScheme implements java.lang.Cloneable {
    private Color uniqueColor;

    private Color colors[] = { 
            new Color(0,200,200), //WHOLE note
            new Color(0,0,255), //HALF = Color.BLUE
            new Color(0,0,0), //QUARTER = Color.BLACK
            new Color(227,0,0), //8th,.. a dark Red
            new Color(0,153,0),//16th... a dark green
            new Color(153,204,0), //32th
            new Color(255,200,0),  //64th = Color.ORANGE
            new Color(255,255,0), //128th
    };

    /**
     * Creates a new instance of ColorScheme, withe de default colors
     */
    public ColorScheme() {
        uniqueColor = null;
    }

    /**
     * Creates a new instance of ColorScheme, where all colors are set to C
     */
    public ColorScheme(Color c) {

        if (c != null) {
            uniqueColor = c;
        }
    }

    /**
     * the maximum value for delta is 31, asuming base is Color.BLACK ;
     */

    public void gradientLighterFrom(Color base, int delta) {
        Color c;
        int R;
        int G;
        int B;
        int i;
        int f;

        if (base != null) {
            c = base;
            R = c.getRed();
            G = c.getGreen();
            B = c.getBlue();
            for (i = 0; i < this.colors.length; i++) {
                this.colors[i] = c;
                f = i * delta;
                if ((R + f > 255) || (G + f > 255) || (B + f > 255)) {
                    c = Color.WHITE;
                } else {
                    c = new Color(R + f, G + f, B + f);
                }
            }
        }

    }

    /**
     * the maximum value for delta is 31, asuming base is Color.BLACK ;
     */
    public void gradientDarkerUntil(Color base, int delta) {
        Color c;
        int R;
        int G;
        int B;
        int i;
        int f;

        if (base != null) {
            c = base;
            R = c.getRed();
            G = c.getGreen();
            B = c.getBlue();
            for (i = 7; i >= 0; i--) {
                this.colors[i] = c;
                f = (8 - i) * delta;
                if ((R + f > 255) || (G + f > 255) || (B + f > 255)) {
                    c = Color.WHITE;
                } else {
                    c = new Color(R + f, G + f, B + f);
                }
            }
        }
    }

    public void setColors(Color array[]) {
        if ((array != null) && (array.length == this.colors.length)) {
            this.colors = array;
            this.uniqueColor = null;
        }
    }

    public boolean isUniqueColor() {
        return (uniqueColor != null);
    }

    /**
     * Sets the <code>color</code> for the position <pos>pos </pos>
     */
    public boolean setColor(Color color, int pos) {
        boolean success;
        int i;

        success = false;
        if ((color != null) && (pos >= 0) && (pos < this.colors.length)) {
            if (uniqueColor != null) {
                for (i = 0; i < this.colors.length; i++) {
                    if (i != pos) {
                        colors[pos] = uniqueColor;
                    } else {
                        colors[pos] = color;
                    }
                }
                uniqueColor = null;
            } else {
                colors[pos] = color;
            }
            success = true;
        }
        return success;
    }

    /**
     * Sets the uniqueColor
     */
    public void setColor(Color color) {

        /*
         * success = false ; if (uniqueColor != null) {
         */
        uniqueColor = color;
        /*
         * success = true ; } return success ;
         */
    }

    /**
     * returns the unique color
     */
    public Color getColor() {
        return this.uniqueColor;
    }

    /**
     * returns the color for the position <pos>pos </pos>
     */
    public Color getColor(int pos) {
        Color resp;

        resp = null;
        if ((pos >= 0) && (pos < this.colors.length)) {
            if (uniqueColor != null) {
                resp = uniqueColor;
            } else {
                resp = colors[pos];
            }
        }

        return resp;
    }

    /**
     * returns the color for a Given GP duration
     */
    public Color getColor(GPDuration duration) {
        Color resp;
        int pos;

        resp = null;
        if (duration != null) {
            if (uniqueColor != null) {
                resp = uniqueColor;
            } else {
                pos = duration.getIndex();
                if ((pos >= 0) && (pos < this.colors.length)) {
                    resp = colors[pos];
                }
                //ELSE the resp stays null
            }
        }
        return resp;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * <p>
     * The <code>equals</code> method implements an equivalence relation on
     * non-null object references:
     * <ul>
     * <li>It is <i>reflexive </i>: for any non-null reference value
     * <code>x</code>,<code>x.equals(x)</code> should return
     * <code>true</code>.
     * <li>It is <i>symmetric </i>: for any non-null reference values
     * <code>x</code> and <code>y</code>,<code>x.equals(y)</code> should
     * return <code>true</code> if and only if <code>y.equals(x)</code>
     * returns <code>true</code>.
     * <li>It is <i>transitive </i>: for any non-null reference values
     * <code>x</code>,<code>y</code>, and <code>z</code>, if
     * <code>x.equals(y)</code> returns <code>true</code> and
     * <code>y.equals(z)</code> returns <code>true</code>, then
     * <code>x.equals(z)</code> should return <code>true</code>.
     * <li>It is <i>consistent </i>: for any non-null reference values
     * <code>x</code> and <code>y</code>, multiple invocations of
     * <tt>x.equals(y)</tt> consistently return <code>true</code> or
     * consistently return <code>false</code>, provided no information used
     * in <code>equals</code> comparisons on the objects is modified.
     * <li>For any non-null reference value <code>x</code>,
     * <code>x.equals(null)</code> should return <code>false</code>.
     * </ul>
     * <p>
     * The <tt>equals</tt> method for class <code>Object</code> implements
     * the most discriminating possible equivalence relation on objects; that
     * is, for any non-null reference values <code>x</code> and <code>y</code>,
     * this method returns <code>true</code> if and only if <code>x</code>
     * and <code>y</code> refer to the same object (<code>x == y</code> has
     * the value <code>true</code>).
     * <p>
     * Note that it is generally necessary to override the <tt>hashCode</tt>
     * method whenever this method is overridden, so as to maintain the general
     * contract for the <tt>hashCode</tt> method, which states that equal
     * objects must have equal hash codes.
     * 
     * @param obj
     *            the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *         argument; <code>false</code> otherwise.
     * @see #hashCode()
     * @see java.util.Hashtable
     */
    public boolean equals(Object obj) {
        ColorScheme other;
        boolean resp;
        int i;

        resp = false;
        if (obj != null) {
            if (obj.getClass().isInstance(this)) {
                other = (ColorScheme) obj;
                if (this.uniqueColor != null) {
                    resp = (this.uniqueColor.equals(other.uniqueColor));
                } else if (other.uniqueColor == null) {
                    i = 0;

                    do {
                        resp = this.colors[i].equals(other.colors[i]);
                        i++;
                    } while (resp && (i < this.colors.length));
                }
            }
        }

        return resp;
    }


    protected Object clone() {
        ColorScheme retValue;

        retValue = new ColorScheme();

        if (this.isUniqueColor()) {
            retValue.uniqueColor = this.uniqueColor;
        } else {
            retValue.colors = (Color[]) this.colors.clone();
        }
        return retValue;
    }
}