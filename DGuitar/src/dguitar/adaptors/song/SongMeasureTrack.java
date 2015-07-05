/*
 * Created on Feb 26, 2005
 */
package dguitar.adaptors.song;


/**
 * A collection of one or more SongVirtualTrack objects listing the events on a given
 * guitar string. Unstring instruments (such as piano or drums) list all their events
 * on a single virtual track.
 *  
 * @author Chris
 */
public interface SongMeasureTrack
{

    /**
     * @param virtualTrackID
     * @return a SongVirtualTrack for the parameter received
     */
    SongVirtualTrack getVirtualTrack(int virtualTrackID);

    /**
     * @return returns a list of the events
     */
    //List getEvents();

    /**
     * @return the track for this SongMeasureTrack
     */
    SongTrack getTrack();
}
