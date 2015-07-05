package dguitar.codecs.guitarPro;

/**
 * This class describes how is represented a complete note.
 * Contrarily to the GPMusicalNote class, this one carries the
 * information contained about one note in Guitar Pro: it can
 * have effects, can be accentuated...
 * 
 * @author Matthieu Wipliez
 */
public class GPNote {
    /**
     * Note duration possibly linked to this note.
     */
    public GPDuration duration;

    /**
     * Effects possibly linked to this note.
     */
    public GPEffectsOnNote effects;
    
    /**
     * Fingering of the left hand possibly linked to this note.
     */
    public GPFingering fingeringLeftHand;
    
    /**
     * Fingering of the right hand possibly linked to this note.
     */
    public GPFingering fingeringRightHand;
    
    /**
     * Whether the note is accentuated.
     */
    public boolean isAccentuated;
    
    /**
     * Whether the note is a dotted note.
     */
    public boolean isDotted;
    
    /**
     * The note dynamic.
     */
    private GPDynamic _dynamic;
    
    /**
     * The number of the fret.
     */
    private int _fretNumber;
    
    /**
     * Whether the note is a dead note (displayed X).
     */
    private boolean _isDeadNote;
    
    /**
     * Whether the note is a ghost note.
     */
    private boolean _isGhostNote;
    
    /**
     * Whether the note is a tied note.
     */
    private boolean _isTieNote;
    
    /**
     * The n-tuplet this note may take part in.
     */
    private int _nTuplet;
    
    /**
     * Creates a new Note.
     *
     */
    public GPNote() {
        duration = null ;
        
        _dynamic = GPDynamic.F;
        effects = null;
        fingeringLeftHand = null;
        fingeringRightHand = null;
        _fretNumber = 0;
        isAccentuated = false;
        _isDeadNote = false;
        isDotted = false;
        _isGhostNote = false;
        _isTieNote = false;
        _nTuplet = 0;
    }

    /**
     * Returns true if the given object is equal to this 
     * GPNote.
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object o ) {
        GPNote other ;
        boolean resp ;
        
        resp = false ;
        if( (o != null) && (o.getClass().isInstance(this)) ) {
            other = (GPNote) o ;
            resp = resp && (_fretNumber == other._fretNumber) ;
            resp = resp && (_nTuplet == other._nTuplet) ;
            resp = resp && (_isDeadNote == other._isDeadNote) ;
            resp = resp && (_isGhostNote == other._isGhostNote) ;
            resp = resp && (_isTieNote == other._isTieNote) ;
            if(this.duration != null) {
                resp = this.duration.equals(other.duration) ;
            }
        }
        return resp ;
    }
    
    /**
     * Returns this note's dynamic.
     * @return this note's dynamic.
     */
    public GPDynamic getDynamic() {
        return _dynamic;
    }

    /**
     * Returns this note's fret number.
     * @return this note's fret number.
     */
    public int getFretNumber() {
        return _fretNumber;
    }

    /**
     * Returns this note's n-tuplet.
     * @return this note's n-tuplet.
     */
    public int getNTuplet() {
        return _nTuplet;
    }
    
    /**
     * Returns this note's fret number.
     * @deprecated
     * @return this note's fret number.
     */
    public int getNumberOfFret() {
        return _fretNumber;
    }
    
    /**
     * Returns true if this note is dead.
     * @return true if this note is dead.
     */
    public boolean isDeadNote() {
        return _isDeadNote;
    }
    
    /**
     * Returns true if this note is a ghost note.
     * @return true if this note is a ghost note.
     */
    public boolean isGhostNote() {
        return _isGhostNote;
    }
    
    /**
     * Returns true if this note is a tie note.
     * @return true if this note is a tie note.
     */
    public boolean isTieNote() {
        return _isTieNote;
    }
    
    /**
     * Sets this note to dead or not according to isDeadNote.
     * @param isDeadNote
     *            whether this note is dead or not.
     */
    public void setDeadNote(boolean isDeadNote) {
        _isDeadNote = isDeadNote;
    }
    
    /**
     * Sets this note's dynamic.
     * @param dynamic
     *            the dynamic to set.
     */
    public void setDynamic(GPDynamic dynamic) {
        _dynamic = dynamic;
    }
    
    /**
     * Sets this note's fret number.
     * @param fretNumber
     *            the fret number to set.
     */
    public void setFretNumber(int fretNumber) {
        _fretNumber = fretNumber;
    }
    
    /**
     * Sets this note as a ghost note or not
     * according to isGhostNote.
     * @param isGhostNote
     *            whether this note is a ghost note or not.
     */
    public void setGhostNote(boolean isGhostNote) {
        _isGhostNote = isGhostNote;
    }
    
    /**
     * Sets this note's n-tuplet.
     * @param tuplet
     *            the n-tuplet to set.
     */
    public void setNTuplet(int tuplet) {
        _nTuplet = tuplet;
    }
    
    /**
     * Sets this note as a tie note or not according to isGhostNote.
     * @param isTieNote
     *            whether this note is a tie note or not.
     */
    public void setTieNote(boolean isTieNote) {
        _isTieNote = isTieNote;
    }
    
    /**
     * Returns a string representation of the Note.
     */
    public String toString() {
        String res ;
        
        res = "" ;
        if(duration != null) {
            res += "Duration: " + duration;
        }
        if(_dynamic != null) {
            res += ", Dynamic: " + _dynamic;
        }
        if(effects != null) {
            res += ", Effects : " + effects;
        }
        if(fingeringLeftHand != null) {
            res += ", fingeringLeftHand : " + fingeringLeftHand ;
        }
        if(fingeringRightHand != null) {
            res += ", fingeringRightHand : " + fingeringRightHand ;
        }
        if(isAccentuated) {
        	res += ", isAccentuated" ;
        }
        if(_isDeadNote) {
        	res += ", isDeadNote" ;
        }
        if(isDotted) {
        	res += ", isDotted" ;
        }
        if(_isGhostNote) {
        	res += ", isGhostNote" ;
        }
        if(_isTieNote) {
        	res += ", isTieNote:" ;
        }
        res += ", nTuplet" + ": " + _nTuplet ;
        res += ", fret number:" +  _fretNumber;
        
        return (res) ;
    }
}