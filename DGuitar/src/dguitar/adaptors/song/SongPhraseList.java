/*
 * Created on Feb 26, 2005
 */
package dguitar.adaptors.song;

/**
 * A SongPhraseList is a list of separate SongPhrases that are played one
 * after the other. 
 * @author Chris
 */
public interface SongPhraseList extends SongPhrase
{
    public void addPhrase(SongPhrase phrase);
    public SongPhrase getPhrase(int index);
    public int getPhraseCount();
}
