package client.word;

import java.io.File;

public class WordFile extends Word {

	private File file;

	public WordFile(File file) {
		super(Word.FILE);
		this.file = file;
	}

	public File getFile() {
		return this.file;
	}

	@Override
	public String getValue() {
		return this.file.getPath();
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String name = "";
		int index = 0;
		int count = 0;
		while( ( index = this.file.getPath().indexOf("/", index) ) != -1 ) {
            index++;
            count = index;
        }
		name = this.file.getPath().substring(count, this.file.getPath().length());
		return name;
	}

}
