package org.ogai.log.mira;

import java.io.PrintStream;

public class SystemWriter implements LogWriter{

	private PrintStream ps;

	public SystemWriter(PrintStream ps){
		this.ps = ps;
	}

	public void open(){
	}

	public void write(String s, Throwable e) {
		ps.println(s);
		if (e!=null) e.printStackTrace(ps);
		ps.flush();
	}

	public void close() {
	}
}
