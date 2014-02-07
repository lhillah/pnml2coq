/**
 *  Copyright 2010 Universite Paris 13 - CNRS UMR 7030 (LIPN)
 *
 *  All rights reserved.   This program and the accompanying materials
 *  are made available under the terms of the Eclipse Public License v1.0
 *  which accompanies this distribution, and is available at
 *  http://www.eclipse.org/legal/epl-v10.html
 *
 *  Project leader / Initial Contributor:
 *    Laure Petrucci - <Laure.Petrucci@lipn.univ-paris13.fr>
 *
 *  Contributors:
 *    Lom Messan Hillah - <$oemails}>
 *
 *  Mailing list:
 *    Laure.Petrucci@lipn.univ-paris13.fr
 */
package fr.lipn.lcr.pnmltocoq.processors;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import fr.lip6.move.pnml.framework.hlapi.HLAPIRootClass;

public abstract class Processor {

	private static final int BUFFER_SIZE_KB = 8;
	private static final int CONTENTS_SIZE_KB = 4;
	private static final int BUFFER_SIZE = BUFFER_SIZE_KB * 1024;
	private static final int CONTENTS_SIZE = CONTENTS_SIZE_KB * 1024;
	private static final String ISO_8859_1 = "ISO-8859-1";
    protected PrintWriter fwloc;
	private File outputFile;
	private FileOutputStream fos ;
	private FileChannel fc; 
	private ByteBuffer bytebuf;

    public abstract void process(HLAPIRootClass rcl, PrintWriter fr);
    
    /**
     * Processes an input net and write the resulting model into a file, the
     * path to which is provided as argument. Recommended method.
     * @param rcl
     * @param outputFilePath
     * @throws IOException
     */
    public abstract void process(HLAPIRootClass rcl, String outputFilePath) throws IOException;
    	
    
    protected void openOutputChannel(String outputFilePath) throws FileNotFoundException {
    	this.outputFile = new File(outputFilePath);
    	this.fos = new FileOutputStream(outputFile);
		fc = fos.getChannel();
		bytebuf = ByteBuffer.allocateDirect(BUFFER_SIZE);
    }
    
    protected void closeOutputChannel() throws IOException {
    	fc.force(true);
    	fos.flush();
    	fc.close();
		fos.close();
		outputFile = null;
		fos = null;
		fc = null;
		bytebuf = null;
    }

    protected StringBuilder decalage;
    protected int decalagevalue ;
    
    public Processor() {
    	decalage = new StringBuilder();
    	decalagevalue = 0;
    }

    protected void incrementDecalage() {
        setdecalage(decalagevalue + 1);
    }

    protected void decrementdecalage() {
        setdecalage(decalagevalue - 1);
    }
    
    protected void resetDecalage() {
    	int len = decalage.length();
    	decalage.delete(0, len);
    	decalagevalue = 0;
    }

    private void setdecalage(int decal) {
        decalagevalue = decal;
        int len = decalage.length();
        decalage.delete(0, len);
        for (int i = 0; i < decalagevalue; i++) {
            decalage.append("    ");
        }
    }

    protected void print(String s) throws IOException  {
    	if (fwloc != null) {
    		fwloc.println(decalage + s);
    	} else {
    		writeToFile(s);
    	}
    }
    
    protected void fastPrint(String s) throws IOException {
    	writeToFile(decalage + s);
    }
    
    /**
	 * Writes output to file using bytebuffer. Buffer size is 8K and chopped
	 * contents size is 4K.
	 * 
	 * @param file
	 *            destination file
	 * @param output
	 *            the string to write there
	 * @throws IOException
	 * @see {@link #chopString(String, int)}
	 */
	private void writeToFile(String output)
			throws IOException {

		List<byte[]> contents = chopString(output, CONTENTS_SIZE);
		for (byte[] cont : contents) {
			bytebuf.put(cont);
			bytebuf.flip();
			fc.write(bytebuf);
			bytebuf.clear();
		}
	}

	/**
	 * Chops a string into chunks of len long.
	 * 
	 * @param src
	 *            the string to chop
	 * @param len
	 *            the length of each chunk
	 * @return the list of chunks
	 */
	private List<byte[]> chopString(String src, int len) {
		List<byte[]> res = new ArrayList<byte[]>();
		int iterations = (int) Math.ceil((double) src.length() / (double) len);

		for (int i = 0; i < iterations; i++) {
			res.add(src.substring(i * len,
					Math.min(src.length(), (i + 1) * len)).getBytes(
					Charset.forName(ISO_8859_1)));
		}
		return res;
	}

}
