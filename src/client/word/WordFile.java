package client.word;

import java.io.File;

public class WordFile extends Word {

	private File file;

	public WordFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return this.file;
	}

	@Override
	public String getValue() {
		return this.file.getPath();
	}

}
