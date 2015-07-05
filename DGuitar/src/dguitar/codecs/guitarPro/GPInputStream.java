package dguitar.codecs.guitarPro;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import dguitar.codecs.guitarPro.version1.GP1InputStream;
import dguitar.codecs.guitarPro.version2.GP2FormatException;
import dguitar.codecs.guitarPro.version2.GP2InputStream;
import dguitar.codecs.CodecFileFilter;
import dguitar.codecs.CodecInputStream;
import dguitar.codecs.guitarPro.version3.GP3InputStream;
import dguitar.codecs.guitarPro.version4.GP4InputStream;

/**
 * This class is the base class to read Guitar Pro files. It is extended by
 * GP3InputStream and GP4InputStream that read respectively files of version 3
 * and 4.
 * 
 * @author Mauricio Gracia Gutiérrez
 */
public class GPInputStream implements CodecInputStream {
    /**
     * The filename (equals to "" if not available).
     */
    protected String __fileName;

    /**
     * The file version.
     */
    protected String __version;
    
    /**
     * The file filter.
     */
    private CodecFileFilter _codecFileFilter;

    /**
     * The InputStream from which the data is read.
     */
    private InputStream _in;

    /**
     * The offset where we got so far in the file.
     */
    private int _offset;

    /**
     * Creates a new GPInputStream using the file of name <code>fileName</code>.
     * 
     * @param fileName
     *            the given file's name.
     * @throws FileNotFoundException
     */
    public GPInputStream(String fileName) throws FileNotFoundException {
        this(fileName, new FileInputStream(fileName));
    }

    /**
     * Creates a new GPInputStream using the file <code>f</code>.
     * 
     * @param f
     *            the given file.
     * @throws FileNotFoundException
     */
    public GPInputStream(File f) throws FileNotFoundException {
       this(f.getAbsolutePath(), new FileInputStream(f));
    }

    /**
     * Creates a new GPInputStream from an existing stream.
     * 
     * @param in
     *            the original stream.
     */
    public GPInputStream(InputStream in) {
        this("", in);
    }
    
    /**
     * Creates a new GPInputStream from an existing stream.
     * 
     * @param fileName
     *            the name of the file the stream has been opened from.
     * @param in
     *            the original stream.
     */
    private GPInputStream(String fileName, InputStream in) {
        __fileName = fileName;
        __version = "";
        
        //TODO: codec file filter: what is it used for ?
        _codecFileFilter = null;
        _in = in;
        _offset = 0;
    }

    /**
     * Creates a new GPInputStream by cloning an existing GPInputStream. This is
     * very useful when the method readPiece is called. Indeed, it starts by
     * reading the version of Guitar Pro the file was encoded with, and then
     * starts the decoding accordingly ; this implies that it may create a
     * GP2InputStream, GP3InputStream, or GP4InputStream, while keeping the same
     * state of the stream.
     * 
     * @param gpIn
     *            the original GPInputStream.
     */
    protected GPInputStream(GPInputStream gpIn) {
        __fileName = gpIn.__fileName;
        __version = gpIn.__version;

        _codecFileFilter = gpIn._codecFileFilter;
        _in = gpIn._in;
        _offset = gpIn._offset;
    }

    /**
     * Closes the stream.
     * 
     * @throws IOException
     */
    public void close() throws IOException {
        _in.close();
    }

    /**
     * Reads the next byte of data from the input stream.
     * 
     * @return the next byte of data, or -1 if the end of the stream is reached.
     * @throws IOException
     */
    public int read() throws IOException {
        _offset++;
        return _in.read();
    }

    /**
     * Reads some number of bytes from the input stream and stores them into the
     * buffer array b.
     * 
     * @param b
     *            the buffer into which the data is read.
     * @return the total number of bytes read into the buffer, or -1 is there is
     *         no more data because the end of the stream has been reached.
     * @throws IOException
     */
    public int read(byte[] b) throws IOException {
        _offset += b.length;
        return _in.read(b);
    }

    /**
     * 
     * @see dguitar.codecs.CodecInputStream#readObject()
     */
    public Object readObject() throws IOException, GPFormatException {
        return readPiece();
    }

    /**
     * Skips over and discards n bytes of data from this input stream.
     * 
     * @param n
     *            the number of bytes to be skipped.
     * @return the actual number of bytes skipped.
     * @throws IOException
     */
    public long skip(long n) throws IOException {
        return _in.skip(n);
    }

    /**
     * 
     * @see dguitar.codecs.Codec#supportedExtension(java.lang.String)
     */
    public boolean supportedExtension(String extension) {
        boolean supported;

        supported = false;
        if (extension != null) {
            //OLDString[] list = _codecFileFilter.getExtensions();
            String[] list = (String[]) _codecFileFilter.getExtensions().toArray();
            if (list != null) {
                for (int i = 0; (!supported) && (i < list.length); i++) {
                    supported = list[i].equalsIgnoreCase(extension);
                }
            }
        }
        return supported;
    }
    
    /**
     * Asserts that n consecutive bytes are equal to a certain value.
     * @param n the number of bytes.
     * @param v the value.
     * @throws IOException
     */
    protected void assertBytesEqualTo(int n, int v) throws IOException {
        byte[] b = new byte[n];
        read(b);
        for (int i = 0; i < n; i++) {
            //TODO assert(b[i] == v);
        }
    }

    /**
     * Reads n bytes and prints them to the screen
     * 
     * @param n
     *            the number of bytes to read
     * @throws IOException
     */
    protected void dumpBytes(int n) throws IOException {
        for (int i = 0; i < n; i++) {
            System.out.print(readUnsignedByte() + " ");
        }
        System.out.println();
    }

    /**
     * Reads n ints and prints them to the screen
     * 
     * @param n
     *            the number of ints to read
     * @throws IOException
     */
    protected void dumpInts(int n) throws IOException {
        for (int i = 0; i < n; i++) {
            System.out.print(readInt() + " ");
        }
        System.out.println();
    }

    /**
     * Reads a boolean from the stream.
     * 
     * @return the boolean read.
     * @throws IOException
     */
    protected boolean readBoolean() throws IOException {
        return (read() == 1);
    }

    /**
     * Reads a byte from the stream.
     * 
     * @return the byte read.
     * @throws IOException
     */
    protected byte readByte() throws IOException {
        return (byte) read();
    }

    /**
     * Reads a double from the stream.
     * 
     * @return the double read.
     * @throws IOException
     */
    protected double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    /**
     * Reads an float from the stream.
     * 
     * @return the float read.
     * @throws IOException
     */
    protected float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    /**
     * Reads an LSB integer from the stream.
     * 
     * @return the int read.
     * @throws IOException
     */
    protected int readInt() throws IOException {
        int integer = 0;
        byte[] b = { 0, 0, 0, 0 };

        read(b);
        integer = ((b[3] & 0xff) << 24) | ((b[2] & 0xff) << 16)
                | ((b[1] & 0xff) << 8) | (b[0] & 0xff);

        return integer;
    }

    /**
     * Reads a long integer from the stream.
     * 
     * @return the long read.
     * @throws IOException
     */
    protected long readLong() throws IOException {
        long longInt = 0;
        byte[] b = { 0, 0, 0, 0, 0, 0, 0, 0 };

        read(b);
        longInt = ((long) (b[7] & 0xff) << 56) | ((long) (b[6] & 0xff) << 48)
                | ((long) (b[5] & 0xff) << 40) | ((long) (b[4] & 0xff) << 32)
                | ((long) (b[3] & 0xff) << 24) | ((long) (b[2] & 0xff) << 16)
                | ((long) (b[1] & 0xff) << 8) | (b[0] & 0xff);

        return longInt;
    }

    /**
     * Reads a string from the stream ; its length is given by a byte.
     * 
     * @param expectedLength
     *            if superior to 0, gives the maximum length we expect to find.
     * @return the String read.
     * @throws IOException
     */
    protected String readStringByte(int expectedLength) throws IOException {
        byte[] b;
        String str;
        int realLength = readUnsignedByte();

        if (expectedLength != 0) {
            b = new byte[expectedLength];
        } else {
            b = new byte[realLength];
        }

        read(b);
        str = new String(b, 0, realLength);
        return str;
    }

    /**
     * Reads a string from the stream ; its length is given by an integer.
     * 
     * @return the String read.
     * @throws IOException
     */
    protected String readStringInteger() throws IOException {
        byte[] b;
        String str;
        int length = readInt(); // reads the length

        b = new byte[length];
        read(b);

        str = new String(b);
        return str;
    }

    /**
     * Reads a string from the stream ; its length is obtained by substracting 1
     * to an integer read.
     * 
     * @return the String read.
     * @throws IOException
     */
    protected String readStringIntegerPlusOne() throws IOException {
        byte[] b;
        String str;
        int lengthPlusOne;
        int length;
        int r;

        lengthPlusOne = readInt(); // reads the expected length + 1
        length = lengthPlusOne - 1; // computes the real length
        if (lengthPlusOne > 0) {
            // reads the real length (as a byte)
            r = read();
            if (length != r) {
                throw new IOException("Wrong string length: should have been "
                        + length);
            }

            b = new byte[length];
            read(b);

            str = new String(b);
        } else {
            r = read();
            str = "";
        }

        return str;
    }

    /**
     * Reads a string from the stream ; its maximum length is obtained by
     * substracting 1 to a byte read, while its real length is obtained by
     * reading the next byte.
     * 
     * @return the string read.
     * @throws IOException
     */
    protected String readStringByteMaxLengthPlusOne() throws IOException {
        byte[] b;
        String str;
        int maxLength = readUnsignedByte() - 1; // reads the maximum length + 1
        int length = readUnsignedByte(); // computes the real length

        // reads the real length (as a byte)
        b = new byte[maxLength];
        read(b);
        str = new String(b, 0, length);

        return str;
    }

    /**
     * Reads an unsigned byte from the stream.
     * 
     * @return the unsigned byte (as an int) read.
     * @throws IOException
     */
    protected int readUnsignedByte() throws IOException {
        return read();
    }

    /**
     * Reads a piece from the stream.
     * 
     * @return the piece read as a GPSong.
     * @throws IOException
     * @throws GPFormatException
     */
    private GPSong readPiece() throws IOException, GPFormatException {
        __version = readStringByte(30);

        // Ok, let's check it now!
        if (GP4InputStream.supportedVersion(__version)) {
            GP4InputStream is = new GP4InputStream(this);
            return is.readPiece();
        } else if (GP3InputStream.supportedVersion(__version)) {
            GP3InputStream is = new GP3InputStream(this);
            return is.readPiece();
        } else if (GP2InputStream.supportedVersion(__version)) {
            GP2InputStream is = new GP2InputStream(this);
            return is.readPiece();
        } else if (GP1InputStream.supportedVersion(__version)) {
            GP1InputStream is = new GP1InputStream(this);
            return is.readPiece();
        }

        throw new GP2FormatException("Oops, unrecognized version: " + __version);
    }
}