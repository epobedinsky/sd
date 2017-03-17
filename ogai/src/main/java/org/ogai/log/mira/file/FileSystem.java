package org.ogai.log.mira.file;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public abstract class FileSystem {

	/**
	 * Получаем размер файла
	 *
	 * @param file
	 * @return
	 */
	protected abstract long getLength(File file, boolean withChilds);

	/**
	 * Переименовать
	 *
	 * @param file
	 * @param dest
	 * @return
	 */
	protected abstract boolean rename(File file, File dest);

	/**
	 * @return Разделитель файловой системы
	 */
	public abstract String getSeparator();

	/**
	 * @param file
	 * @return Существует или нет файл
	 */
	public abstract boolean exists(File file);

	/**
	 * @param file
	 * @return Время последнего изменения
	 */
	public abstract long getLastModifiedTime(File file);

	/**
	 * @param file
	 * @return Создать пустой файл
	 */
	public abstract boolean createNewFile(File file) throws Exception;

	/**
	 * Удалить
	 *
	 * @param file
	 * @return
	 */
	public abstract boolean delete(File file);

	/**
	 * Директория это или нет
	 *
	 * @param file
	 * @return
	 */
	public abstract boolean isDirectory(File file);

	/**
	 * Получить входной поток для чтения
	 *
	 * @param file
	 * @return
	 */
	public abstract InputStream createInputStream(File file) throws Exception;

	/**
	 * Записать из файла данных с позиции start по end в выходной поток
	 *
	 * @param file
	 * @param outputStream
	 * @param start
	 * @param end
	 */
	public abstract void writeToOutputWithRange(File file, OutputStream outputStream, long start, long end);

	/**
	 * Открыть поток для записи
	 *
	 * @param file
	 * @return
	 */
	public abstract OutputStream createOutputStream(File file) throws Exception;

	/**
	 * Получить список файлой директории
	 *
	 * @param file
	 * @return
	 */
	public abstract List<File> listFiles(File file);

	/**
	 * Convert the given pathname string to normal form.  If the string is
	 * already in normal form then it is simply returned.
	 */
	public abstract String normalize(String path);

	/**
	 * Добавляем к parent'у child и возвращаем
	 * В случае, если child имеет такое же начало как parent,
	 * то это началу у child должно убраться
	 */
	public abstract String resolve(String parent, String child);

	/**
	 * Получить абсолютный путь к файлу
	 *
	 * @param path
	 * @return
	 */
	public abstract String getAbsolutePath(String path);

	/**
	 * Открыть поток для добавления данных
	 *
	 * @param file
	 * @return
	 */
	public abstract OutputStream createOutputStreamForAppend(File file) throws Exception;

	/**
	 * Получить список файлов при фильтрации
	 *
	 * @param file
	 * @param filter
	 * @return
	 */
	public abstract List<File> listFiles(File file, FileFilter filter);
}
