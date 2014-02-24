package org.equo.gen;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

public class JavaWriter {

	/** The java file to write */
	private File f;
	
	/** The writer */
	private FormattedWriter w;

	/** The raw bytes generated. */
	private ByteArrayOutputStream s;
	
	/** The character encoding used */
	private String encoding;
	
	/**
	 * Creates a new writer instance with the specific character encoding.
	 * 
	 * @param file The file to write
	 * @param encoding The character encoding
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public JavaWriter(File file, String encoding) throws UnsupportedEncodingException {
		this.f = file;
		this.s = new ByteArrayOutputStream();
		this.w = new FormattedWriter(new OutputStreamWriter(
			this.s = new ByteArrayOutputStream(), 
			this.encoding = encoding)
		);
	}
	
	/**
	 * Creates a new writer instance with the default character encoding.
	 * 
	 * @param file The file to write
	 * @throws FileNotFoundException 
	 * @throws UnsupportedEncodingException 
	 */
	public JavaWriter(File file) {
		this.f = file;
		this.w = new FormattedWriter(new OutputStreamWriter(
			this.s = new ByteArrayOutputStream())
		);
	}
	
	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return encoding;
	}
	
	public void increaseTabLevel() {
		w.increaseTabLevel();
	}

	public void decreaseTabLevel() {
		w.decreaseTabLevel();
	}

	public String tab() {
		return w.tab();
	}

	public void writeTab() throws IOException {
		w.writeTab();
	}

	public void writeLine(String... args) throws IOException {
		w.writeLine(args);
	}

	public void write(String str) throws IOException {
		w.writeTab();
		w.write(str);
	}
	
	public void append(String str) throws IOException {
		w.write(str);
	}

	public void append(char c) throws IOException {
		w.write(c);
	}
	
	public String toString() {
		return w.toString();
	}

	public void endStatm() throws IOException {
		w.write(';');
		w.newLine();
	}
	
	public void endLine() throws IOException {
		w.newLine();
	}
	
	public void endLine(String s) throws IOException {
		w.write(s);
		w.newLine();
	}
	
	public void endLine(char c) throws IOException {
		w.write(c);
		w.newLine();
	}
	
	public void writeStatm(String... args) throws IOException {
		w.writeTab();
		for (int i = 0; i < args.length; i++) {
			if (i > 0) w.write(' ');
			w.write(args[i]);
		}
		endStatm();
	}

	public void writeBlock(String... args) throws IOException {
		w.writeTab();
		for (int i = 0; i < args.length; i++) {
			if (i > 0) w.write(' ');
			w.write(args[i]);
		}
		w.write(" {");
		w.newLine();
		w.increaseTabLevel();
	}	
	
	public void endBlock() throws IOException {
		w.decreaseTabLevel();
		w.writeTab();
		w.write('}');
		w.newLine();
	}

	/**
	 * @throws IOException
	 */
	public void dump(boolean override) throws IOException {
		w.close();
		boolean writeFile = false;
		if (!f.exists()) {
			writeFile = true;
		} else if (override) {
			// Compare byte a byte from the file and the byte array.
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));
			ByteArrayInputStream ais = new ByteArrayInputStream(s.toByteArray());
			int bbis, bais;
			do {
				bbis = bis.read();
				bais = ais.read();
				if (bbis != bais) {
					writeFile = true;
					break;
				}
			} while (bbis != -1 || bais != -1);
			bis.close();
		}
		if (writeFile) {
			FileOutputStream fos = new FileOutputStream(f);
			fos.write(s.toByteArray());
			fos.close();
		}
	}
}

