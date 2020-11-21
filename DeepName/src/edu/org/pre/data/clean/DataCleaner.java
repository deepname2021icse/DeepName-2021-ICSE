package edu.utd.fse19.data.clean;

import java.io.File;

public class DataCleaner {
//	public static String DATA = "";
	public static String DATA = "";
	public static int COUNTER = 0;
	static StringBuffer FILES = new StringBuffer("");

	public static void main(String[] args) {
		File dt = new File(DATA + File.separator + "repos");
		displayAll(dt);
		Utils.write(DATA + File.separator + "files.txt", FILES.toString());
	}

	public static void displayAll(File path) {
		if (path.isFile()) {
			if (!path.getName().endsWith(".java")) {
//				path.delete();
			} else {
				FILES.append("\n" + path.getAbsolutePath().replace(DATA + File.separator + "repos" + File.separator, ""));
			}
		} else {
			File files[] = path.listFiles();
			if (files != null)
				for (File dirOrFile : files) {
					displayAll(dirOrFile);
				}
		}
	}
}
