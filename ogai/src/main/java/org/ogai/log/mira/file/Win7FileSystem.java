package org.ogai.log.mira.file;

import org.apache.commons.io.FileUtils;
import org.ogai.log.Log;
import org.ogai.log.LogFactory;
import org.ogai.util.Util;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Class Description
 *
 * @author Побединский Евгений
 *         27.03.14 21:51
 */
public class Win7FileSystem extends FileSystem {

	private static final Log log = LogFactory.create(Win7FileSystem.class);

	protected static final String separator = java.io.File.separator;

	/**
	 * Путь ко всем файлам
	 */
	private String path;

	public Win7FileSystem() {
		this.path = "";
	}

	public Win7FileSystem(String path) {
		this.path = path;
	}

	@Override
	public String getAbsolutePath(String path) {
		return createFile(path).getAbsolutePath();
	}

	private java.io.File createFile(String path) {
		if (Util.isEmpty(this.path)) {
			return new java.io.File(path);
		} else {
			return new java.io.File(this.path, path);
		}
	}

	/**
	 * Преобразовываем java.io.File в mira File
	 *
	 * @param file
	 * @return
	 */
	private File getFile(java.io.File file) {
		return new File(this, file.getAbsolutePath());
	}

	private java.io.File localFile(File file) {
		return new java.io.File(file.getAbsolutePath());
	}

	private File unlocalFile(File parent, java.io.File file) {
		return new File(parent, file.getName());
	}

	@Override
	protected long getLength(File file, boolean withChilds) {
		java.io.File localFile = localFile(file);
		return getFileSize(localFile, withChilds);
	}

	private long getFileSize(java.io.File file, boolean withChilds) {
		if (file.isDirectory()) {
			java.io.File[] fileList = file.listFiles();
			long size = 0;
			if (fileList == null) return size;
			for (int i = 0; i < fileList.length; i++) {
				if (withChilds){
					size += getFileSize(fileList[i], true);
				} else {
					size += getFileSizeWithoutChilds(fileList[i]);
				}
			}
			return size;
		} else {
			return file.length();
		}
	}

	private long getFileSizeWithoutChilds(java.io.File file){
		if (file.isDirectory()){
			return 0;
		} else {
			return file.length();
		}

	}

	@Override
	protected boolean rename(File file, File dest) {
		try{
			FileUtils.moveFile(localFile(file), localFile(dest));
			return true;
		} catch (Exception e){
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String getSeparator() {
		return java.io.File.separator;
	}

	@Override
	public boolean exists(File file) {
		return localFile(file).exists();
	}

	@Override
	public long getLastModifiedTime(File file) {
		return localFile(file).lastModified();
	}

	@Override
	public boolean createNewFile(File file) throws Exception {
		java.io.File localFile = localFile(file);
		try {
			//Создаем родительские директории
			localFile.getParentFile().mkdirs();
			if (!localFile.exists()) {
				return localFile.createNewFile();
			} else {
				return false;
			}
		} catch (IOException e) {
			throw new Exception("Error create a new file:" + file.getAbsolutePath(), e);
		}
	}

	@Override
	public boolean delete(File file) {
		//Удаляем директорию со всем содержимым
		return delete(localFile(file));
	}

	private boolean delete(java.io.File file) {
		if (file.isDirectory()) {
			for (java.io.File child : file.listFiles()) {
				delete(child);
			}
		}

		return file.delete();
	}

	@Override
	public boolean isDirectory(File file) {
		return localFile(file).isDirectory();
	}

	@Override
	public InputStream createInputStream(File file) throws Exception {
		try {
			return new FileInputStream(localFile(file));
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public OutputStream createOutputStream(File file) throws Exception {
		//Если файл не создан, создаем его
		if (!file.exists()) {
			file.createNewFile();
		}
		java.io.File localFile = localFile(file);
		try {
			return new FileOutputStream(localFile);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	@Override
	public OutputStream createOutputStreamForAppend(File file) throws Exception {
		//Если файл не создан, создаем его
		if (!file.exists()) {
			file.createNewFile();
		}
		java.io.File localFile = localFile(file);
		try {
			return new FileOutputStream(localFile, true);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public static final int BUFFER_SIZE = 10240;

	@Override
	public void writeToOutputWithRange(File file, OutputStream outputStream, long start, long end) {

		RandomAccessFile in = null;
		try {

			in = new RandomAccessFile(localFile(file), "r");

			byte[] buffer = new byte[BUFFER_SIZE];
			int read;

			if (in.length() == end) {
				// Write full range.
				while ((read = in.read(buffer)) > 0) {
					outputStream.write(buffer, 0, read);
				}
			} else {
				// Write partial range.
				in.seek(start);
				long toRead = end;

				while ((read = in.read(buffer)) > 0) {
					if ((toRead -= read) > 0) {
						outputStream.write(buffer, 0, read);
					} else {
						outputStream.write(buffer, 0, (int) toRead + read);
						break;
					}
				}
			}
			/*

			in.skipBytes((int) start);

			long contentLength = end - start;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bufferFullness;


			while (contentLength > 0) {
				bufferFullness = (int) (contentLength > buffer.length ? buffer.length : contentLength);

				in.read(buffer, 0, bufferFullness);
				outputStream.write(buffer, 0, bufferFullness);
				contentLength = contentLength - bufferFullness;
			}
			outputStream.flush();  */
		} catch (IOException e) {
			log.error("Error on write file:" + file.getAbsolutePath(), e);
		} finally {
			// Gently close streams.
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// Do your thing with the exception. Print it, log it or mail it.
					log.error("Error on close input", e);
				}
			}
		}
	}

	@Override
	public List<File> listFiles(File file) {
		return getFiles(file, localFile(file).listFiles());
	}

	private List<File> getFiles(File parent, java.io.File[] localChilds) {
		List<File> files = new ArrayList<File>();
		if (localChilds == null) return files;
		for (java.io.File localChild : localChilds) {
			File child = unlocalFile(parent, localChild);
			files.add(child);
		}
		return files;
	}

	@Override
	public List<File> listFiles(File file, FileFilter filter) {
		return getFiles(file, localFile(file).listFiles(new LocalFileFilter(filter, file)));
	}

	private class LocalFileFilter implements java.io.FileFilter {
		private FileFilter filter;
		private File parent;

		private LocalFileFilter(FileFilter filter, File parent) {
			this.filter = filter;
			this.parent = parent;
		}

		@Override
		public boolean accept(java.io.File pathname) {
			return filter.accept(unlocalFile(parent, pathname));
		}
	}

	@Override
	public String normalize(String path) {
		return new java.io.File(path).getPath();
	}

	@Override
	public String resolve(String parent, String child) {
		return new java.io.File(parent, child).getPath();
	}


	/**
	 * Если входной файл находится локально, то ничего не делаем,
	 * если же он находится в другой файловой системе, то создаем временный файл и копируем в него
	 *
	 * @param file
	 * @return
	 */
	public static java.io.File createTempCopyOfFile(File file) throws Exception {
		boolean isLocalFile = file.fs.getClass().getSimpleName().equals(Win7FileSystem.class.getSimpleName());
		if (!isLocalFile) {
			File tempFile = File.createTempFile("tempcopy", null, getLocalTempDirectory());
			file.copy(tempFile);
			file = tempFile;
		}
		java.io.File localFile = new java.io.File(file.getAbsolutePath());
		if (!isLocalFile) {
			localFile.deleteOnExit();
		}
		return localFile;
	}

	/**
	 * Получаем ссылку на файл по локальному абсолютному пути
	 *
	 * @param absolutePath
	 * @return
	 */
	public static File getFile(String absolutePath) {
		return new Win7FileSystem().getFile(new java.io.File(absolutePath));
	}

	/**
	 * Получаем ссылку на локальный файл по URI
	 *
	 * @param uri
	 * @return
	 */
	public static File getFile(URI uri) {
		return new Win7FileSystem().getFile(new java.io.File(uri));
	}

	/**
	 * @return ссылка на системную папку Temp
	 */
	public static File getLocalTempDirectory() {
		java.io.File tmpdir = new java.io.File(System.getProperty("java.io.tmpdir"));
		return new Win7FileSystem().getFile(tmpdir);
	}

	/**
	 * @return Создаем ссылку на временную директорию с уникальным именем, но после ее использования, необходимо ее удалять
	 */
	public static File getLocalUniqueTempDirectory() {
		final int TEMP_DIR_ATTEMPTS = 10000;
		File baseDir = Win7FileSystem.getLocalTempDirectory();
		String baseName = System.currentTimeMillis() + "-";

		for (int counter = 0; counter < TEMP_DIR_ATTEMPTS; counter++) {
			File tempDir = new File(baseDir, baseName + counter);
			if (!tempDir.exists()) {//Заместо создания директории, проверяем, что ее не существует.
				return tempDir;
			}
		}
		throw new IllegalStateException("Failed to create directory within "
				+ TEMP_DIR_ATTEMPTS + " attempts (tried "
				+ baseName + "0 to " + baseName + (TEMP_DIR_ATTEMPTS - 1) + ')');
	}
}
