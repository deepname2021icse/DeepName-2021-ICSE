package bm.commit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class FixNameMethodReader {
	public static LevenshteinDistance distance = new LevenshteinDistance();

	public static void main(String[] args) {
		File storage = new File(FixNameCommitFilter.FIX_NAME_COMMIT_STORAGE);
		File[] commits = storage.listFiles();
		int counter = 0;
		StringBuffer iNames = new StringBuffer();
		StringBuffer cNames = new StringBuffer();
		StringBuffer srcs = new StringBuffer();
		Set<String> test = new HashSet<>();
		int x = 0;
		List<String> addInfo = new ArrayList<>();
		for (File commit : commits) {
			File[] changedFiles = commit.listFiles();
			if (changedFiles == null)
				continue;
			String[] info = Utils.read(commit.getAbsolutePath() + File.separator + "info.txt").trim().split("\n");
			for (File changedFile : changedFiles) {
				if (changedFile.isDirectory()) {
					File[] fixes = changedFile.listFiles();
					for (File f : fixes) {
						if (f.isFile() && f.getName().endsWith(".encoded6")) {
							String content = Utils.read(f.getAbsoluteFile()).trim();
							String[] lines = content.split("\n");
							String[] ic = lines[0].trim().split("-");
							String raw = Utils.read(f.getAbsolutePath().replace(".encoded6", ".raw")).trim();
							int i = raw.indexOf("\n");
							if (isAccepted(ic[0], ic[1]) && raw.length() < 1000) {
								srcs.append(lines[1] + "\n");
								iNames.append(ic[0] + "\n");
								cNames.append(ic[1] + "\n");

								// System.out.println(ic[1]);
								// System.out.println(raw);
								String tmpName = f.getName().replace(".encoded6", "").replace(".java.0", ".java")
										.replace(".java.1", ".java").replace(".java.2", ".java");
								String tmp = tmpName + "," + commit.getName() + "," + info[0] + "," + info[1] + ","
										+ info[2];
								addInfo.add(tmp);
							}
						}
					}
				}
			}
		}

		String content = Utils.read("/Users/sonnguyen/Desktop/names.txt").trim();
		String[] names = content.split("\n");
		Set<String> ns = new HashSet<>();
		for (String n : names) {
			ns.add(n);
		}
		Set<String> cnames = new HashSet<>();
		StringBuffer cnamesFilted = new StringBuffer();
		try (BufferedReader br = new BufferedReader(new FileReader("/Users/sonnguyen/Desktop/test.csv"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(",");
				if (ns.contains(parts[2])) {
					cnames.add(parts[2]);
					cnamesFilted.append(line + "," + addInfo.get(Integer.parseInt(parts[0]) - 1) + "\n");
				}
				if (cnames.size() >= 599) {
					System.out.println(line + "," + addInfo.get(Integer.parseInt(parts[0]) - 1) + "\n");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		Utils.write("/Users/sonnguyen/Desktop/test_info.csv", cnamesFilted.toString().trim());
		// System.out.println((double) x / counter);
		// System.out.println(counter);
		// Utils.write(FixNameCommitFilter.FIX_NAME_COMMIT_STORAGE + File.separator +
		// "inames.txt",
		// iNames.toString().trim());
		// Utils.write(FixNameCommitFilter.FIX_NAME_COMMIT_STORAGE + File.separator +
		// "cnames.txt",
		// cNames.toString().trim());
		// Utils.write(FixNameCommitFilter.FIX_NAME_COMMIT_STORAGE + File.separator +
		// "srcs_6.txt",
		// srcs.toString().trim());
	}

	public static boolean isAccepted(String bf, String af) {
		af = af.toLowerCase();
		bf = bf.toLowerCase();
		if (bf.length() < 4 || af.length() < 4)
			return false;
		if ("to string".equals(bf.toLowerCase()) || "to string".equals(af.toLowerCase()))
			return false;
		if ("copy".equals(bf.toLowerCase()) || "copy".equals(af.toLowerCase()))
			return false;
		if ("init".equals(bf.toLowerCase()) || "init".equals(af.toLowerCase()))
			return false;
		if ("run".equals(bf.toLowerCase()) || "run".equals(af.toLowerCase()))
			return false;
		if ("clone".equals(bf.toLowerCase()) || "clone".equals(af.toLowerCase()))
			return false;
		// Set<String> ws1 = getSetOfWords(bf);
		// Set<String> ws2 = getSetOfWords(af);
		// if (ws1.containsAll(ws2) || ws2.containsAll(ws1)) {
		// return false;
		// } else {
		// if (containEachOther(ws1, ws2)) {
		// return false;
		// }
		// }
		// if ((double)distance.apply(bf, af)/bf.length() < 0.5) {
		// return false;
		// }

		return true;
	}

	private static boolean containEachOther(Set<String> ws1, Set<String> ws2) {
		int counter1 = 0;
		for (String w1 : ws1) {
			for (String w2 : ws2) {
				if (w2.contains(w1)) {
					counter1++;
					break;
				}
			}
		}
		if (counter1 >= ws1.size())
			return true;
		int counter2 = 0;
		for (String w2 : ws2) {
			for (String w1 : ws1) {
				if (w1.contains(w2)) {
					counter2++;
					break;
				}
			}
		}
		if (counter2 >= ws2.size())
			return true;
		return false;
	}

	private static Set<String> getSetOfWords(String s) {
		Set<String> words = new HashSet<>();
		String[] ws = s.toLowerCase().split(" ");
		for (String w : ws) {
			if (w.length() > 3 && w.endsWith("s")) {
				w = w.substring(0, w.length() - 1);
			}
			if (w.equals("remove"))
				w = "delete";
			if (w.equals("add"))
				w = "create";
			if (w.equals("bind"))
				w = "process";
			if (w.equals("param"))
				w = "parameter";
			if (w.equals("populate"))
				w = "add";
			if (w.equals("self"))
				w = "run";
			if (w.equals("retrieve") || w.equals("lookup"))
				w = "get";
			if (w.equals("initialize"))
				w = "init";
			if (w.equals("master"))
				w = "supervisor";
			if (w.equals("undeploy"))
				w = "remove";
			if (w.equals("deploy"))
				w = "add";
			if (w.equals("activate"))
				w = "init";
			if (w.equals("start"))
				w = "init";
			if (w.equals("execute"))
				w = "run";
			if (w.equals("exec"))
				w = "run";
			if (w.equals("register"))
				w = "add";
			if (w.equals("integer"))
				w = "int";
			if (w.equals("boolean"))
				w = "bool";
			if (w.equals("binary"))
				w = "bool";
			if (w.equals("configuration"))
				w = "config";
			if (w.equals("close"))
				w = "shutdown";
			if (w.equals("evict"))
				w = "delete";
			if (w.equals("release"))
				w = "free";
			if (w.equals("install"))
				w = "add";
			words.add(w);
		}
		return words;
	}

	private static String nomalize(String s) {
		s = s.replace("undeploy", "remove").replace("remove", "delete").replace("add", "create")
				.replace("bind", "process").replace("param", "parameter").replace("populate", "add")
				.replace("self", "run").replace("retrieve", "get").replace("initialize", "init")
				.replace("master", "supervisor").replace("deploy", "add").replace("activate", "init")
				.replace("execute", "run").replace("exec", "run").replace("integer", "int").replace("binary", "bool")
				.replace("boolean", "bool").replace("configuration", "config").replace("shutdown", "close")
				.replace("stop", "close");
		return s;
	}

}
