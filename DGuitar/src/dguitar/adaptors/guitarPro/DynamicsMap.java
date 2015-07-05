/*
 * Created on Mar 5, 2005
 */
package dguitar.adaptors.guitarPro;

import java.util.HashMap;
import java.util.Map;

import dguitar.codecs.guitarPro.GPDynamic;

/**
 * Transform a GP4Dynamic into a MIDI velocity
 * @author Chris
 */
public class DynamicsMap
{
    // TODO a better way to transform dynamics into MIDI note on.
    private static final Map<String,Integer> dynamicsMap=createDynamicsMap();    
    
    private static Map<String,Integer> createDynamicsMap()
    {
        Map<String,Integer> map=new HashMap<String,Integer>();
        map.put("PPP",new Integer(16));
        map.put("PP",new Integer(32));
        map.put("P",new Integer(47));
        map.put("MP",new Integer(63));
        map.put("MF",new Integer(79));
        map.put("F",new Integer(95));
        map.put("FF",new Integer(111));
        map.put("FFF",new Integer(127));
        return map;
    }
    
    public static int velocityOf(GPDynamic dynamic)
    {
        int velocity=95;
        if(dynamic!=null)
        {
            String stringValue=dynamic.toString();
            Integer object=(Integer) dynamicsMap.get(stringValue);
            if(object!=null)
            {
                velocity=object.intValue();
            }
        }
        return velocity;
    }
}
