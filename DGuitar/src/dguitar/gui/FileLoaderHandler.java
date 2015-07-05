/*
 * Created on 20/04/2005

 */
package dguitar.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.PropertyResourceBundle;

import javax.swing.ProgressMonitor;
import javax.swing.Timer;

import common.FileLoader;

/**
 * 
 * this class is listening for the FileLoader to finished or be canceled
 * 
 * 
 * @author Mauricio Gracia G
 *  
 */
public class FileLoaderHandler implements ActionListener {
    private DGuitar parent;

    private String msg;

    private String fileName;

    private ProgressMonitor progressMonitor;

    private FileLoader fileLoader;

    private PropertyResourceBundle lang;

    private Timer timer;

    /* FileType handling fields */
    //TODO change this to a "table" were the extension is linked to a FileType
    // and
    //also linked to a CodecFileFilter for the FileChooser, see method
    // setFileType
    public static final int EMPTY = 0;

    public static final int GUITAR_PRO = 1;

    public static final int MIDI = 2;

    private int fileType;
    
    private boolean evaluate ;
    
    private long start ;
    
    private long finish ;

    public FileLoaderHandler(DGuitar dguitar, String aFileName, boolean Evaluate) {
        this.parent = dguitar;
        this.fileName = aFileName;
        this.setFileType(this.fileName) ;
        this.lang = DGuitar.lang;
        this.evaluate = Evaluate ;
    }

    public void start() {
        if (parent.OpenedListContains(fileName)) {
            parent.showMessageAlreadyOpen(fileName);
        } else {
            //try {
            DGuitar.LS.print(lang.getString("Opening_file") + " " + fileName);
            this.fileLoader = new FileLoader(fileName);
            this.progressMonitor = new ProgressMonitor(this.parent, lang
                    .getString("Loading_FILE"), "", 0, fileLoader
                    .getLengthOfTask());
            progressMonitor.setProgress(0);
            progressMonitor.setMillisToDecideToPopup(1000);
            this.timer = new Timer(1000, this);
            start = System.currentTimeMillis() ;
            fileLoader.loadFile();
            timer.start();
        }
    }

    public void actionPerformed(ActionEvent evt) {
        progressMonitor.setProgress(fileLoader.getCurrent());
        String s = fileLoader.getMessage();
        if (s != null) {
            progressMonitor.setNote(s);
        }
        if (progressMonitor.isCanceled() || fileLoader.isDone()) {
            progressMonitor.close();
            fileLoader.stop();
            timer.stop();
            if (fileLoader.isDone()) {
                if (fileLoader.success) {
                    //call the method that DISPLAY the read bytes
                    this.finishLoadAndDisplay(this.fileName, fileLoader.resp);
                } else {
                    msg = DGuitar.lang.getString("File") + "\'" + fileName
                            + "\' ";
                    msg += lang.getString("load_FAILED") + fileLoader.errorCode;
                    parent.showDialogOk(
                            lang.getString("ERROR_FILE_NOT_LOADED"), msg);
                }
            } else {
                msg = lang.getString("Loading_of") + "\'" + fileName + "\' ";
                msg += lang.getString("was_canceled_by_USER")
                        + fileLoader.errorCode;
                parent.showDialogOk(lang.getString("Load_was_CANCELED"), msg);
            }
        }
    }

    /**
     * this method is called when the file has been loaded into the byte array
     * and must be displayed (for both GP or MID)
     */
    private void finishLoadAndDisplay(String aFileName, byte[] bytes) {
        String aux;

        switch (this.fileType) {
        //TODO This should be changed to something like File.display()..were
        //File might be GuitarProFile, MidiFile, etc...
        case GUITAR_PRO:
            parent.displayGP(aFileName, bytes);
            break;
        case MIDI:
            parent.displayMID(aFileName);
            break;
        }
        if (evaluate) {
            finish = System.currentTimeMillis();
            aux = this.fileName + " " ;
            aux += lang.getString("evaluateCompleteDisplayProcessTook") + ":";
            aux += (finish - start) / 1000 + " " + lang.getString("seconds");
            DGuitar.LS.log(aux);
        }
    }

    /**
     * Sets the <code>fileType</code> field according to the fileName
     * extension
     * 
     * @param fileName
     */
    private void setFileType(String fileName) {
        if (DGuitar.gpFileFilter.accept(fileName)) {
            this.fileType = GUITAR_PRO;
        } else if (DGuitar.midiFileFilter.accept(fileName)) {
            this.fileType = MIDI;
        }
    }

}

