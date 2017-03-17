package org.ogai.log.mira;

import org.ogai.core.Closeable;

/**
 * @author Usov Andrey
 *
 */
public interface LogWriter extends Closeable {
	//TODO replace with our exception
	public void open() throws Exception;
	public void write(String s, Throwable e);
}
