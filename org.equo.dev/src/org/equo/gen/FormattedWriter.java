package org.equo.gen;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Formatted output
 * 
 * @author j0rd1
 *
 */
public class FormattedWriter extends BufferedWriter {
	
	/**
	 * The enable formatting flag 
	 */
	private boolean format = true;

	/** 
	 * Tab level 
	 */
	private int tabLevel = 0;
	
	/**
     * Creates a buffered character-output stream that uses a default-sized
     * output buffer.
     *
     * @param out A Writer
     */
	public FormattedWriter(Writer out) {
		super(out);
	}
	
	public void setEnableFormatting(boolean format) {
		this.format = format;
	}
	
	/**
	 * Increases the tab level.
	 */
	public void increaseTabLevel() {
		tabLevel++;
	}

	/**
	 * Decreases the tab level.
	 */
	public void decreaseTabLevel() {
		tabLevel--;
	}
	
	/**
	 * @return A '\t' character string based in the current tab level.
	 */
	public String tab() {
		StringBuilder b  = new StringBuilder(tabLevel);
		for (int i = 0; i < tabLevel; i++) {
			b.append('\t');
		}
		return b.toString();
	}
	
	public void writeTab() throws IOException {
		write(tab());
	}
	
//	public void write(String str) throws IOException {
//		write(tab());
//		write(str);
//	}
	
	public void writeLine(String... args) throws IOException {
		if (format) writeTab();
		for (int i = 0; i < args.length; i++) {
			if (i > 0) write(' ');
			write(args[i]);
		}
		if (format) newLine();
	}

}
