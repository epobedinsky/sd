package org.ogai.log.mira.file;

public interface FileFilter {
	/**
	 * Удовлетворяет условия файл или нет
	 *
	 * @param file
	 * @return
	 */
	boolean accept(File file);
}