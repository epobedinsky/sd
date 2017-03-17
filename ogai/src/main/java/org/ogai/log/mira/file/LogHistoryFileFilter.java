package org.ogai.log.mira.file;

public class LogHistoryFileFilter implements FileFilter {

	private class FileName {
		private String name;
		private String ext;

		public FileName(String fileName) {
			int extIndex = fileName.lastIndexOf(".");
			if (extIndex > -1) {
				this.name = fileName.substring(0, extIndex);
				this.ext = fileName.substring(extIndex + 1);
			} else {
				this.name = fileName;
				this.ext = "";
			}
		}
	}

	private FileName fileName;
	private String ext;

	public LogHistoryFileFilter(String fileName) {
		this.fileName = new FileName(fileName);
	}

	public boolean accept(File pathName) {
		String name = pathName.getFileName();
		return name.startsWith(fileName.name) && name.endsWith(fileName.ext);
	}
}
