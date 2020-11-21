package bm.commit;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

public class Utils {

	public Set<String> extractAllOptions(String filePath) {
		Set<String> options = new HashSet<>();
		String fileName = "/Users/sonnguyen/Downloads/bash-4.4/config.h";
		String content = read(fileName);

		return options;
	}

	public static List<File> getAllFiles(File dir) {
		Collection<File> files = FileUtils.listFiles(dir, new RegexFileFilter("^(.*?)(\\.c)$"),
				DirectoryFileFilter.DIRECTORY);
		return new ArrayList<>(files);
	}

	public static String read(String fileName) {
		StringBuffer content = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				content.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	public static String read(File f) {
		StringBuffer content = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line;
			while ((line = br.readLine()) != null) {
				content.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return content.toString();
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

	public static boolean isValidUTF8(byte[] input) {
		CharsetDecoder cs = Charset.forName("UTF-8").newDecoder();
		try {
			cs.decode(ByteBuffer.wrap(input));
			return true;
		} catch (CharacterCodingException e) {
			return false;
		}
	}
	public static void write(List<String> content, String filePath) {
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
