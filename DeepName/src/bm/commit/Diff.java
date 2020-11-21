package bm.commit;

import java.util.ArrayList;
import java.util.List;

public class Diff {
	String file;
	String block;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getBlock() {
		return block;
	}

	public void setBlock(String block) {
		this.block = block;
	}

	public Diff(String file, String block) {
		this.file = file;
		this.block = block;
	}

	public Diff(String file) {
		this.file = file;
	}

	String getOldVersion() {
		String old = "";
		String[] lines = block.split("\n");
		for (String line : lines) {
			if (!line.startsWith("+")) {
				line = line.substring(1, line.length());
				old += line + "\n";
			}
		}
		return old;
	}

	String getNewVersion() {
		String newversion = "";
		String[] lines = block.split("\n");
		for (String line : lines) {
			if (!line.startsWith("-")) {
				line = line.substring(1, line.length());
				newversion += line + "\n";
			}
		}
		return newversion;
	}

	List<String> getDeletedLines() {
		List<String> dlines = new ArrayList<>();
		String[] lines = block.split("\n");
		for (String line : lines) {
			if (line.trim().startsWith("-")) {
				line = line.substring(1, line.length()).replace("\\", "");
				if (line.trim().length() > 2 && !line.trim().startsWith("//") && !line.trim().startsWith("*")
						&& !line.trim().startsWith("/*"))
					dlines.add(line);
			}
		}
		return dlines;
	}

	List<String> getOldLines() {
		List<String> slines = new ArrayList<>();
		String[] lines = block.split("\n");
		for (String line : lines) {
			if (!line.trim().startsWith("+")) {
				line = line.substring(1, line.length()).replace("\\", "");
				if (line.trim().length() > 2 && !line.trim().startsWith("//") && !line.trim().startsWith("*")
						&& !line.trim().startsWith("/*"))
					slines.add(line);
			}
		}
		return slines;
	}

	List<String> getNewLines() {
		List<String> slines = new ArrayList<>();
		String[] lines = block.split("\n");
		for (String line : lines) {
			if (!line.trim().startsWith("-")) {
				line = line.substring(1, line.length()).replace("\\", "");
				if (line.trim().length() > 2 && !line.trim().startsWith("//") && !line.trim().startsWith("*")
						&& !line.trim().startsWith("/*"))
					slines.add(line);
			}
		}
		return slines;
	}

	List<String> getStartLines() {
		List<String> slines = new ArrayList<>();
		String[] lines = block.split("\n");
		for (String line : lines) {
			if (line.trim().startsWith("-") || line.trim().startsWith("+")) {
				break;
			}
			line = line.substring(1, line.length()).replace("\\", "");
			if (line.trim().length() > 2 && !line.trim().startsWith("//") && !line.trim().startsWith("*")
					&& !line.trim().startsWith("/*"))
				slines.add(line);
		}
		return slines;
	}

	List<String> getEndLines() {
		List<String> endlines = new ArrayList<>();
		String[] lines = block.split("\n");
		for (int i = lines.length - 1; i >= 0; i--) {
			String line = lines[i];
			if (line.trim().startsWith("-") || line.trim().startsWith("+")) {
				break;
			}
			line = line.substring(1, line.length()).replace("\\", "");
			if (line.trim().length() > 2 && !line.trim().startsWith("//") && !line.trim().startsWith("*")
					&& !line.trim().startsWith("/*"))
				endlines.add(line);
		}
		return endlines;
	}

	List<String> getAddedLines() {
		List<String> dlines = new ArrayList<>();
		String[] lines = block.split("\n");
		for (String line : lines) {
			if (line.trim().startsWith("+")) {
				line = line.substring(1, line.length()).replace("\\", "");
				if (line.trim().length() > 2 && !line.trim().startsWith("//") && !line.trim().startsWith("*")
						&& !line.trim().startsWith("/*"))
					dlines.add(line);
			}
		}
		return dlines;
	}

	@Override
	public boolean equals(Object obj) {
		Diff d = (Diff) obj;
		return d.getFile().equals(file) && d.getBlock().equals(block);
	}
}
