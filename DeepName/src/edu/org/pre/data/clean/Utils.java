package edu.utd.fse19.data.clean;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Utils {
	public static void main(String[] args) {
		String str = getSaltString(5);
		System.out.println(str);
	}

	public static String read(String fileName) {
		String content = "";
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				// System.out.println(line);
				content += (line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		content = preprocess(content);
		return content;
	}

	public static String read(File f) {
		String content = "";
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				content += (line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		content = preprocess(content);
		return content;
	}

	private static String preprocess(String content) {
		for (int i = 0; i < 3; i++)
			content = content.replace("# ", "#");
		return content;
	}

	public static Set<String> extractOptions(String condition) {
		Set<String> options = new HashSet<>();
		condition = condition.replace("(", " ").replace(")", " ").replace("[", " ").replace("<", " ").replace("=", " ")
				.replace("]", " ").replace("&&", " ").replace("||", " ").replace(">", " ").replace("!", " ")
				.replace("defined ", " ");
		String[] parts = condition.split(" ");
		for (String p : parts) {
			if (!p.trim().isEmpty())
				options.add(p);
		}
		return options;
	}

	public static void write(String filePath, String content) {
		if (new File(filePath).exists())
			return;
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
			bw.write(content);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getSaltString(int len) {
		String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		StringBuilder salt = new StringBuilder();
		Random rnd = new Random();
		while (salt.length() < len) { // length of the random string.
			int index = (int) (rnd.nextFloat() * SALTCHARS.length());
			salt.append(SALTCHARS.charAt(index));
		}
		String saltStr = salt.toString();
		return saltStr;

	}
	/**
	 * Write raw data to a big file - use BufferedOutputStream
	 */
	public static void write(Collection<String> content, String filePath) {
		BufferedOutputStream bout = null;
		try {
			bout = new BufferedOutputStream(new FileOutputStream(filePath));
			for (String line : content) {
				line += System.getProperty("line.separator");
				bout.write(line.getBytes("UTF-8"));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (bout != null) {
				try {
					bout.close();
				} catch (Exception e) {
				}
			}
		}
	}

}
