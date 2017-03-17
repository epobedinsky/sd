package org.ogai.log.mira.file;

import java.io.PrintWriter;
import java.io.Writer;
import java.util.Vector;

public class VectorWriter extends PrintWriter {

	private Vector v;

	public VectorWriter() {
		super(new NullWriter());
		v = new Vector();
	}

	public void print(Object o) {
		v.addElement(o.toString());
	}

	public void print(char[] chars) {
		v.addElement(new String(chars));
	}

	public void print(String s) {
		v.addElement(s);
	}

	public void println(Object o) {
		v.addElement(o.toString());
	}

	// JDK 1.1.x apprenly uses this form of println while in
	// printStackTrace()
	public
	void println(char[] chars) {
		v.addElement(new String(chars));
	}

	public
	void println(String s) {
		v.addElement(s);
	}

	public void write(char[] chars) {
		v.addElement(new String(chars));
	}

	public void write(char[] chars, int off, int len) {
		v.addElement(new String(chars, off, len));
	}

	public void write(String s, int off, int len) {
		v.addElement(s.substring(off, off+len));
	}

	public void write(String s) {
		v.addElement(s);
	}

	public String[] toStringArray() {
		int len = v.size();
		String[] sa = new String[len];
		for(int i = 0; i < len; i++) {
			sa[i] = (String) v.elementAt(i);
		}
		return sa;
	}

}

class NullWriter extends Writer {

	public void close() {
		// blank
	}

	public void flush() {
		// blank
	}

	public void write(char[] cbuf, int off, int len) {
		// blank
	}
}
