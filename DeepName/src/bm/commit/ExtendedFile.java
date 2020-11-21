package bm.commit;

public class ExtendedFile {
	public String file;
	public String content;
	
	public ExtendedFile(String file, String content) {
		this.file = file;
		this.content = content;
	}
	@Override
	public int hashCode() {
		return (file+content).hashCode();
	}
	@Override
	public boolean equals(Object obj) {
		return this.hashCode() == obj.hashCode();
	}
}
