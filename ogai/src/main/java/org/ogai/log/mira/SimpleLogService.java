package org.ogai.log.mira;

import org.ogai.core.Closeable;
import org.ogai.log.LogService;
import static org.ogai.log.LogService.LogLevel.*;

/**
 * Простая имплементация логгера
 * //TODO переписать
 *
 * TODO replace by our exception everywhere in code
 */
public class SimpleLogService implements LogService, Closeable {

	protected LogWriter outWriter;
	protected LogWriter errWriter;

	private LogFormatter formatter;

	private LogLevel level;

	public SimpleLogService(LogFormatter formatter, LogWriter out, LogWriter err) throws Exception {
		this.formatter = formatter;
		this.outWriter = out;
		this.errWriter = err;
		this.level = LogLevel.DEBUG;

		outWriter.open();
		errWriter.open();
	}

	public SimpleLogService(LogFormatter formatter) throws Exception {
		this(formatter, new SystemWriter(System.out), new SystemWriter(System.err));
	}

	protected String getMessage(LogLevel type, String name, String s) {
		return formatter.getMessage(type.toString(), name, s);
	}

	@Override
	public void close() {
		if (outWriter != null) outWriter.close();
		if (errWriter != null) errWriter.close();
	}

	@Override
	public void debug(String name, String s) {
		if (level.isChecked(DEBUG))
			outWriter.write(getMessage(DEBUG, name, s), null);
	}

	@Override
	public void error(String name, String s, Throwable e) {
		if (level.isChecked(ERROR))
			errWriter.write(getMessage(ERROR, name, s), e);
	}

	@Override
	public void error(String name, String s) {
		if (level.isChecked(ERROR))
			errWriter.write(getMessage(ERROR, name, s), null);
	}

	@Override
	public void info(String name, String s) {
		if (level.isChecked(INFO))
			outWriter.write(getMessage(INFO, name, s), null);
	}

	@Override
	public void warn(String name, String s, Throwable e) {
		if (level.isChecked(WARN))
			outWriter.write(getMessage(WARN, name, s), e);
	}

	@Override
	public void warn(String name, String s) {
		if (level.isChecked(WARN))
			outWriter.write(getMessage(WARN, name, s), null);
	}

//	public LogWriter getErrWriter() {
//		return errWriter;
//	}
//
//	public LogWriter getOutWriter() {
//		return outWriter;
//	}

//	public File getLogDir() {
//		FileWriter errFileWriter = getFirstFileWriter(errWriter);
//		if (errFileWriter != null){
//			return errFileWriter.getDir();
//		}
//		throw new CoreException("Not found File Writer in errWriter");
//	}
//
//	/**
//	 * Первый найденный FileWriter
//	 */
//	public FileWriter getFirstFileWriter(LogWriter writer) {
//		if (writer instanceof FileWriter) {
//			return ((FileWriter) writer);
//		} else if (writer instanceof CompositeLogWriter){
//			for (LogWriter logWriter : ((CompositeLogWriter)writer).getLogWriters()){
//				FileWriter fileWriter = getFirstFileWriter(logWriter);
//				if (fileWriter != null){
//					return fileWriter;
//				}
//			}
//		}
//		return null;
//	}

	//TODO сделать доступным runtime из JMX
	public LogLevel getLevel() {
		return level;
	}

	public void setLevel(LogLevel level) {
		this.level = level;
	}
}
