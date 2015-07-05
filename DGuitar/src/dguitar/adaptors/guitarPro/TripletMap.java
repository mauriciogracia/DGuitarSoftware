/*
 * Created on Mar 11, 2005
 */
package dguitar.adaptors.guitarPro;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The triplet map stores all the triplets you are likely to find in Guitar Pro.
 * @author Chris
 */
public class TripletMap
{
    private static String className=TripletMap.class.toString();
    private static Logger logger=Logger.getLogger(className);
    private static Map<Integer,TripletValue> tripletMap=createMap();
    
    private static Map<Integer,TripletValue> createMap()
    {
        Map<Integer,TripletValue> m=new HashMap<Integer,TripletValue>();
        
        m.put(new Integer(1),new TripletValue(1,1));
        m.put(new Integer(3),new TripletValue(3,2));
        m.put(new Integer(5),new TripletValue(5,4));
        m.put(new Integer(6),new TripletValue(6,4));
        m.put(new Integer(7),new TripletValue(7,4));
        m.put(new Integer(9),new TripletValue(9,8));
        m.put(new Integer(10),new TripletValue(10,8));
        m.put(new Integer(11),new TripletValue(11,8));
        m.put(new Integer(12),new TripletValue(12,8));
        m.put(new Integer(13),new TripletValue(13,8));
        
        return m;
    }
    
    public static TripletValue getTriplet(int n)
    {
        Object x=tripletMap.get(new Integer(n));
        if(x==null)
        {
            logger.warning("Requested unexpected triplet value "+n+", revert to non-triplet");
            return new TripletValue(1,1);
        }
        return (TripletValue)x;
    }
}
