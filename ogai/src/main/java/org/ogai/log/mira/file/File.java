package org.ogai.log.mira.file;

import org.apache.commons.io.IOUtils;
import org.ogai.util.Util;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.List;

public class File {

	private static final String EXT_SEPARATOR = ".";

	private String path;
	protected FileSystem fs;

	public File(FileSystem fs, String path) {
		this.fs = fs;
		this.path = fs.normalize(path);
	}

	public File(File parent, String... childPath) {
		this.fs = parent.fs;
		String path = parent.path;
		for (String child : childPath) {
			path = fs.resolve(path, child);
		}
		this.path = path;
	}

	private String getSeparator() {
		return fs.getSeparator();
	}

	/**
	 * Возвращаем полное имя файла или директории, после последнего разделителя
	 *
	 * @return
	 */
	public String getFileName() {
		int index = path.lastIndexOf(getSeparator());
		return path.substring(index + 1);
	}

	/**
	 * @return расширение файла
	 */
	public String getFileNameExt() {
		int pIndex = path.lastIndexOf(EXT_SEPARATOR);
		if (pIndex > -1) {
			return path.substring(pIndex + 1);
		} else {
			return "";
		}
	}

	/**
	 * @return имя файла без расширения
	 */
	public String getFileNameWithoutExt() {
		String fileName = getFileName();
		int pIndex = fileName.lastIndexOf(EXT_SEPARATOR);
		if (pIndex > -1) {
			return fileName.substring(0, pIndex);
		} else {
			return fileName;
		}
	}

	/**
	 * Полный путь
	 *
	 * @return
	 */
	public String getAbsolutePath() {
		return fs.getAbsolutePath(path);
	}

	/**
	 * Текущий путь к файлу относительно файловой системы
	 * Не стоит использовать данный метод без очень серьезной необходимости
	 * Поэтому он пока private
	 *
	 * @return
	 */
	private String getPath() {
		return path;
	}

	/**
	 * Родительская папка
	 *
	 * @return
	 */
	public File getParentFile() {
		String p = this.getParent();
		if (p == null) return null;
		return new File(fs, p);
	}

	/**
	 * @return родительский путь(например, для test/test.xml вернет test
	 */
	private String getParent() {
		int index = path.lastIndexOf(getSeparator());
		if (index < 0) {
			return null;
		}
		return path.substring(0, index);
	}

	/**
	 * Убирает из адреса, адрес родителя и возвращает в виде строки
	 *
	 * @param parent
	 * @return
	 */
	public String getPathWithoutParent(File parent) {
		String parentAbsolutePath = parent.getAbsolutePath();
		String absolutePath = getAbsolutePath();
		if (!absolutePath.startsWith(parentAbsolutePath)) {
			throw new IllegalArgumentException("path:\'" + parentAbsolutePath + "\' is not a prefix of file's full path:\'" + absolutePath + "\'");
		}
		if (absolutePath.length() < parentAbsolutePath.length() + 1) {
			throw new IllegalArgumentException("parent path:" + parentAbsolutePath + " and file path:" + absolutePath + " are equals");
		}
		return absolutePath.substring(parentAbsolutePath.length() + 1);
	}

	/**
	 * Существует или нет файл
	 *
	 * @return
	 */
	public boolean exists() {
		return fs.exists(this);
	}

	/**
	 * Время последнего изменения
	 *
	 * @return
	 */
	public long lastModified() {
		return fs.getLastModifiedTime(this);
	}

	/**
	 * Размер файла, если идет ссылка на директорию, то подсчитывается размер всех вложенных в нее файлов и вложенных директорий
	 *
	 * @return
	 */
	public long length() {
		return fs.getLength(this, true);
	}

	/**
	 * @return Размер файла, если идет ссылка на директорию, то подсчитывается размер всех вложенных в нее файлов
	 */
	public long lengthWithoutChilds(){
		return fs.getLength(this, false);
	}

	/**
	 * Создаем новый файл + вначале создаем все директории до этого файла
	 *
	 * @return true - Если файл был создан, false-если он уже существовал
	 */
	public boolean createNewFile() throws Exception {
		return fs.createNewFile(this);
	}

	/**
	 * Удаляем файл или директорию со всем содержимым
	 *
	 * @return
	 */
	public boolean delete() {
		if (exists()) {
			return fs.delete(this);
		} else {
			return true;
		}
	}

	/**
	 * Переименовать
	 *
	 * @param dest
	 * @return
	 */
	public boolean renameTo(File dest) {
		return fs.rename(this, dest);
	}

	/**
	 * Директория это или файл
	 *
	 * @return
	 */
	public boolean isDirectory() {
		return fs.isDirectory(this);
	}

	public boolean isFile() {
		return !isDirectory();
	}

	/**
	 * Открыть поток для чтения файла, после чтения необходимо данный поток закрыть: IOHelper.close
	 *
	 * @return
	 */
	public InputStream createInputStream() throws Exception {
		return fs.createInputStream(this);
	}

	/**
	 * Открыть поток для записи файла, если что-то было в файле, то это перетирается. После необходимо данный поток закрыть
	 *
	 * @return
	 */
	public OutputStream createOutputStream() throws Exception {
		return fs.createOutputStream(this);
	}

	/**
	 * Открыть поток для записи, если что-то было в файле, то новая информация добавляется
	 *
	 * @return
	 */
	public OutputStream createOutputStreamForAppend() throws Exception {
		return fs.createOutputStreamForAppend(this);
	}

	/**
	 * Записать в поток outputStream данные из файла с позиции start по end
	 *
	 * @param outputStream
	 * @param start
	 * @param end
	 */
	public void writeToOutputWithRange(OutputStream outputStream, long start, long end) {
		fs.writeToOutputWithRange(this, outputStream, start, end);
	}

	/**
	 * Возвращает список дочерних файлов и директорий
	 * первого уровня
	 *
	 * @return
	 */
	public List<File> listFiles() {
		return fs.listFiles(this);
	}


	public List<File> listFiles(FileFilter filter) {
		return fs.listFiles(this, filter);
	}

	public void copy(File dst) throws Exception {
		if (isDirectory()) {

			List<File> children = listFiles();
			for (File aChildren : children) {
				aChildren.copy(new File(dst, aChildren.getFileName()));
			}
		} else {

			//Копируем содержимое файла
			InputStream in = null;
			OutputStream out = null;
			try {
				in = createInputStream();
				out = dst.createOutputStream();
				IOUtils.copy(in, out);
			} catch (Exception e) {
				throw new Exception(e);
			} finally {
				IOUtils.closeQuietly(in);
				IOUtils.closeQuietly(out);
			}
		}
	}

	public String toString() {
		return getAbsolutePath();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof File)) return false;

		File file = (File) o;

		if (fs != null ? !fs.equals(file.fs) : file.fs != null) return false;
		if (path != null ? !path.equals(file.path) : file.path != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = path != null ? path.hashCode() : 0;
		result = 31 * result + (fs != null ? fs.hashCode() : 0);
		return result;
	}

	public static String getNames(String... names) {
		return Util.join(names, Win7FileSystem.separator);
	}

	/**
	 * Добавляем к текущему path суффикс
	 *
	 * @param file
	 * @param suffix
	 * @return
	 */
	public static File addSuffix(File file, String suffix) {
		return new File(file.fs, file.path + suffix);
	}

	//--------------------Создание временного файла
	public static File createTempFile(String prefix, String suffix, File baseDirectory) throws Exception {
		if (prefix.length() < 3)
			throw new IllegalArgumentException("Prefix string too short");
		if (suffix == null)
			suffix = ".tmp";

		File f;
		do {
			f = generateFile(prefix, suffix, baseDirectory);

		} while (!f.createNewFile());
		return f;
	}

	private static final SecureRandom random = new SecureRandom();

	public static File generateFile(String prefix, String suffix, File dir) {
		long n = random.nextLong();
		if (n == Long.MIN_VALUE) {
			n = 0;	  // corner case
		} else {
			n = Math.abs(n);
		}
		return new File(dir, prefix + Long.toString(n) + suffix);
	}
}
