package org.ogai.log.mira;

import org.ogai.log.mira.file.File;
import org.ogai.log.mira.file.FileFilter;
import org.ogai.log.mira.file.LogHistoryFileFilter;
import org.ogai.log.mira.file.VectorWriter;
import org.ogai.util.Util;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.regex.Pattern;

public class FileWriter implements LogWriter {
	private static class AutoDiscardingDeque<E> extends LinkedBlockingDeque<E> {
		public AutoDiscardingDeque() {
			super();
		}

		public AutoDiscardingDeque(int capacity) {
			super(capacity);
		}

		@Override
		public synchronized boolean offerLast(E e) {
			if (remainingCapacity() == 0) {
				removeFirst();
			}
			super.offerLast(e);
			return true;
		}
	}

	public static final String SIZE = "size";
	public static final String COUNT = "count";
	public static final String CHARSET = "charset";
	public static final int LAST_MESSAGES_SIZE = 300;

	private static final Pattern HISTORY_PATTERN = Pattern.compile(".*_[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}_[0-9]{1,2}.[0-9]{1,2}.[0-9]{1,2}.log");

	private List<String> history;
	private String fileName;
	private String charset;
	private File dir;
	private OutputStream fos;
	private BufferedOutputStream bos;
	private PrintWriter ps;
	private long fileSize;

	// Очередь последних сообщений об ошибках - пока не заполняется
	private Deque<String> lastMessages;

	private long maxFileSize;
	private int maxFileCount;

	/**
	 * Используется в beans.xmls
	 *
	 * @param dir
	 * @param fileName
	 * @param props
	 */
	public FileWriter(File dir, String fileName, Properties props) {
		this(fileName, dir,
				Long.parseLong(props.getProperty(SIZE)),
				Integer.parseInt(props.getProperty(COUNT)),
				props.getProperty(CHARSET));
	}

	public FileWriter(String fileName, File dir, long maxFileSize, int maxFileCount, String charset) {
		this.fileName = fileName;
		this.dir = dir;
		this.maxFileSize = maxFileSize;
		this.maxFileCount = maxFileCount;
		if (charset != null && Charset.isSupported(charset)) {
			this.charset = charset;
		} else {
			this.charset = "cp1251";
		}

		this.lastMessages = new AutoDiscardingDeque<String>(LAST_MESSAGES_SIZE);
	}


	public void open() throws Exception {
		open(true);
	}

	private void open(boolean isLoadHistory) throws Exception {
		try {
			File file = getFilePath();
			fos = file.createOutputStreamForAppend();
			bos = new BufferedOutputStream(fos);
			ps = new PrintWriter(new OutputStreamWriter(fos, this.charset));
			if (isLoadHistory) loadHistory();
			fileSize = file.length();
		} catch (Throwable e) {
			throw new Exception("Error on create open log:" + fileName, e);
		}
	}

	private void loadHistory() {
		List<File> files = dir.listFiles(new LogHistoryFileFilter(fileName));
		history = new ArrayList<String>();
		for (File file : files) {
			history.add(file.getFileName());
		}
		Collections.sort(history);
	}

	protected String[] getThrowableStr(Throwable throwable) {
		VectorWriter vw = new VectorWriter();
		throwable.printStackTrace(vw);
		return vw.toStringArray();
	}

	private void rollOver() {
		try {
			closeForRollover();
			File file = getFilePath();
			String historyFileName = getHistoryFileName();
			File target = new File(dir, historyFileName);
			delete(target);
			if (!file.renameTo(target)){
				System.err.println(Util.formatNow()+" Error on rename " + file + " to " + target);
				fileName = fileName+"1";//Заплатка, чтобы если файл заблокирован, писать в новый файл
			}
			if (history.indexOf(historyFileName) == -1) history.add(historyFileName);
			if (history.size() > maxFileCount) {
				String deleteFileName = history.get(0);
				history.remove(0);
				File delFile = new File(dir, deleteFileName);
				delFile.delete();
			}
			open(false);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	private void delete(File target) {
		if (target.exists()) {
			//noinspection ResultOfMethodCallIgnored
			target.delete();
		}
	}

	private String getHistoryFileName() {
		int index = fileName.lastIndexOf(".");
		String name = fileName.substring(0, index);
		String ext = fileName.substring(index);
		String date = getCurTime();
		date = replace(date, " ", "_");
		date = replace(date, ":", ".");
		return name + '_' + date + ext;
	}

	protected void closeForRollover() {
		close();
	}

	public void close() {
		try {
			ps.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void write(String s, Throwable e) {
		try{
			write(s);
			if (e != null) {
				String[] sList = getThrowableStr(e);
				for (String trace : sList) {
					write(trace);
				}
			}
			ps.flush();
		} catch (Exception exp){
			//Если произошла ошибка при записи в файл, необходимо вывести это в консольный лог
			exp.printStackTrace();
		}
	}

	private void write(String s) {
		synchronized (this) {
			long newFileSize = fileSize + s.length() + 2;
			if (newFileSize > maxFileSize) {
				rollOver();
			}
			ps.println(s);
			lastMessages.add(s);
			fileSize = fileSize + s.length() + 2;
		}
	}

	private static String replace(String template, String placeholder, String replacement) {
		if (template == null) return template;
		int lastLoc = 0;
		int loc = template.indexOf(placeholder, lastLoc);
		if (loc == -1) return template;
		StringBuilder buf = new StringBuilder();
		while (loc >= 0) {
			buf.append(template.substring(lastLoc, loc));
			buf.append(replacement);
			lastLoc = loc + placeholder.length();
			loc = template.indexOf(placeholder, lastLoc);
		}
		buf.append(template.substring(lastLoc));
		return buf.toString();
	}

	private static String getCurTime() {
		Timestamp time = new Timestamp(new Date().getTime());
		String rTime = time.toString();
		return rTime;
	}

	/**
	 * Удаляет файлы истории
	 * <p/>
	 * Для выполнения метода при инициализации приложения требуется
	 * добавить атрибут _init-method_ в конфигурацию прилолжения:
	 * <bean name="outWriter" class="org.mirapolis.log.filelogger.FileWriter" init-method="dropHistory">
	 */
	
	public void dropHistory() {
		int index = fileName.lastIndexOf(".");
		final String namePrefix = fileName.substring(0, index);

		List<File> oldHistory = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getFileName().startsWith(namePrefix) && HISTORY_PATTERN.matcher(file.getFileName()).matches();
			}
		});

		for (File history : oldHistory) {
			delete(history);
		}
	}

	
	public File getFilePath() {
		return new File(dir, fileName);
	}

	
	public File getDir() {
		return dir;
	}

	
	public void setDir(File dir) {
		this.dir = dir;
	}

	
	public String getFileName() {
		return fileName;
	}

	
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	public String getCharset() {
		return charset;
	}

	
	public void setCharset(String charset) {
		this.charset = charset;
	}

	
	public long getMaxFileSize() {
		return maxFileSize;
	}

	
	public void setMaxFileSize(long maxFileSize) {
		this.maxFileSize = maxFileSize;
	}

	
	public int getMaxFileCount() {
		return maxFileCount;
	}

	
	public void setMaxFileCount(int maxFileCount) {
		this.maxFileCount = maxFileCount;
	}

	
	public String getLastMessages() {
		return Util.join(lastMessages.toArray(new String[0]), "\n");
	}
}
