package edu.utd.fse19.data.clean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class Linker {
	static Map<String, String> method_info = new HashMap<>();
	static Map<String, Set<String>> name_srcs = new HashMap<>();

	public static void main(String[] args) {
		load();
		List<String> results = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader("/Users/sonnguyen/Desktop/samples.txt"))) {
			String line;
			while ((line = br.readLine()) != null && !line.trim().isEmpty()) {
				String[] parts = line.split("---");
				if (parts.length != 3)
					continue;
				String ref = parts[0];
				String tran = parts[1];
				String src = parts[2];
				String info = findMethod(ref, src);
				if (info != null) {
					results.add(ref + "---" + tran + "---" + info);
//					line + "---" + info.className + "---" + info.name + "---" + info.decl.toString()
					String[] ps = info.split("---");
					String path = ps[0];
					String cls = ps[1];
					String originalName = ps[2];
					String code = ps[3];
					System.out.println(ref + "---" + tran + "---" + originalName+"---"+cls+"---"+path+"---\n");
					System.out.println(code);
					System.out.println("\n=====\n");
				}
				if (results.size() > 384)
					break;
			}
		} catch (IOException e) {
		}
		System.out.println(results.size());
	}

	private static String findMethod(String ref, String src) {
		Set<String> srcs = name_srcs.get(ref);
		if (srcs == null)
			return null;
		String best_src = "";
		double max = 0;
		for (String s : srcs) {
			double sim = sim(s, src);
			if (sim > max) {
				max = sim;
				best_src = s;
			}
		}
		if (max < 0.97)
			return null;
		if ("to string".equals(ref))
			return null;
		return method_info.get(best_src);
	}

	public static double sim(String x, String y) {
		String[] words1 = x.split(" ");
		String[] words2 = y.split(" ");
		Set<String> ws1 = new HashSet<>(Arrays.asList(words1));
		Set<String> ws2 = new HashSet<>(Arrays.asList(words2));
		Set<String> inter = new HashSet<>(ws1);
		inter.retainAll(ws2);
		double sim = 2 * ((double) inter.size()) / (ws1.size() + ws2.size());
		return sim;
	}

	static String readFile(final String path, final Charset encoding) throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void load() {
		try (BufferedReader br = new BufferedReader(new FileReader(DataCleaner.DATA + File.separator + "files.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (method_info.keySet().size() > 500000)
					break;
				if (!line.contains("/test/")) {
					File file = new File(DataCleaner.DATA + File.separator + "data" + File.separator + line);
					try {
						final ASTParser parser = ASTParser.newParser(AST.JLS8);
						final String src = readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);
						parser.setSource(src.toCharArray());
						parser.setKind(ASTParser.K_COMPILATION_UNIT);
						final Map<?, ?> options = JavaCore.getOptions();
						JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
						parser.setCompilerOptions(options);
						final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
						cu.accept(new AstVisitor());

						final MethodCollector collector = new MethodCollector();

						List<MethodInfo> methods = collector.collect(cu);
						for (MethodInfo info : methods) {
							if (info.bodyRaw.trim().length() > 4) {
								try {
									info.file = line;
									info.encode(new ASTReader());
									method_info.put(info.getEncodedSrc6(), line + "---" + info.className + "---"
											+ info.name + "---" + info.decl.toString());
									String name = info.getName().toLowerCase().trim();
									Set<String> srcs = name_srcs.get(name);
									if (srcs == null)
										srcs = new HashSet<>();
									srcs.add(info.getEncodedSrc6());
									name_srcs.put(name, srcs);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
