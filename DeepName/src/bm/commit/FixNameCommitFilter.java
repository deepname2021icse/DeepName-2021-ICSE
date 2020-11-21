package bm.commit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import edu.utd.fse19.data.clean.ASTReader;
import edu.utd.fse19.data.clean.CommentsExtractor;
import edu.utd.fse19.data.clean.MethodCollector;
import edu.utd.fse19.data.clean.MethodInfo;
import edu.utd.fse19.data.clean.MethodRawReader;
import edu.utd.fse19.data.clean.AstVisitor;
import edu.utd.fse19.data.clean.Utils;

public class FixNameCommitFilter {
	public static String FIX_NAME_COMMIT_STORAGE = "/Users/sonnguyen/Desktop/storage/";
	public static int METHOD_NAME_FIXES = 0;
	public static LevenshteinDistance distance = new LevenshteinDistance();
	public static List<String> ALL_METHODS_TXT = new ArrayList<String>();
	public static boolean ALL = false;

	public static void main(String[] args) throws Exception {
		if (ALL) {
			getAllMethods();
			System.out.println(ALL_METHODS_TXT.size());
		}
		File storage = new File(FIX_NAME_COMMIT_STORAGE);
		File[] commits = storage.listFiles();
		int counter = 0;
		for (File commit : commits) {
			if (isCorrectFixNameCommit(commit)) {
				counter++;
			} else {
				// try {
				// FileUtils.deleteDirectory(commit);
				// } catch (IOException e) {
				// e.printStackTrace();
				// }
			}
		}
		System.out.println(METHOD_NAME_FIXES);
		System.out.println(counter);
	}

	private static boolean isCorrectFixNameCommit(File commit) {
		String[] changedFiles = Utils.read(commit.getAbsoluteFile() + File.separator + "changed_files.txt").trim()
				.split("\n");
		int i = 0;
		for (String cf : changedFiles) {
			String fname = new File(cf).getName();
			String bfPath = commit.getAbsolutePath() + File.separator + i + File.separator + fname + ".bf";
			String afPath = commit.getAbsolutePath() + File.separator + i + File.separator + fname + ".af";
			String storePath = commit.getAbsolutePath() + File.separator + i + File.separator + fname;
			List<MethodInfo> methodsBf = extractMethodInfo(bfPath);
			List<MethodInfo> methodsAf = extractMethodInfo(afPath);
			int c = 0;
			for (MethodInfo infoBf : methodsBf) {
				MethodInfo af = getFixNameMethod(infoBf, methodsAf);
				if (af != null) {
					String bug = storePath + "." + c + ".raw";
					String content = "//" + af.name + "\n";
					content += infoBf.methodRaw;
					Utils.write(bug, content);
					// 6
					String encodedFile = storePath + "." + c + ".encoded6";
					String encodedContent = infoBf.getName() + "-" + af.getName() + "\n";
					encodedContent = encodedContent + infoBf.getEncodedSrc6();
					Utils.write(encodedFile, encodedContent);
					// 10
					encodedFile = storePath + "." + c + ".encoded10";
					encodedContent = infoBf.getName() + "-" + af.getName() + "\n";
					encodedContent = encodedContent + infoBf.getEncodedSrc10();
					Utils.write(encodedFile, encodedContent);
					c++;
					// return true;
				}
			}
			i++;
		}
		return false;
	}

	private static boolean isFixNameMethod(MethodInfo bf, List<MethodInfo> afs) {
		if (bf.bodyRaw.length() < 10)
			return false;
		List<MethodInfo> afsHavingSameBody = new ArrayList<MethodInfo>();
		for (MethodInfo af : afs) {
			if (bf.bodyRaw.equals(af.bodyRaw))
				afsHavingSameBody.add(af);
		}
		if (afsHavingSameBody.isEmpty())
			return false;
		String bfName = bf.getName().toLowerCase().replace(" ", "");
		for (MethodInfo af : afsHavingSameBody) {
			String afName = af.getName().toLowerCase().replace(" ", "");
			if (afName.equals(bfName))
				return false;
		}
		// TODO:
		if (afsHavingSameBody.size() != 1)
			return false;
		MethodInfo af = afsHavingSameBody.get(0);
		if (distance.apply(bf.getName(), af.getName()) < 3)
			return false;
		if (ALL) {
			String method_txt = bf.getName() + "____" + bf.bodyRaw;
			if (ALL_METHODS_TXT.contains(method_txt))
				return false;
		}
		System.out.println(distance.apply(bf.getName(), af.getName()) + "--" + bf.getName() + "--->" + af.getName());
		METHOD_NAME_FIXES++;
		return true;
	}

	private static MethodInfo getFixNameMethod(MethodInfo bf, List<MethodInfo> afs) {
		String bd = bf.bodyRaw.replace(";", "").replace("{", "").replace("}", "").replace("return", "")
				.replace("true", "").replace("false", "").replace("null", "").trim();
		if (bd.length() < 5)
			return null;
		List<MethodInfo> afsHavingSameBody = new ArrayList<MethodInfo>();
		for (MethodInfo af : afs) {
			if (bf.bodyRaw.equals(af.bodyRaw))
				afsHavingSameBody.add(af);
		}
		if (afsHavingSameBody.isEmpty())
			return null;
		String bfName = bf.getName().toLowerCase().replace(" ", "");
		for (MethodInfo af : afsHavingSameBody) {
			String afName = af.getName().toLowerCase().replace(" ", "");
			if (afName.equals(bfName))
				return null;
		}
		// TODO:
		if (afsHavingSameBody.size() != 1)
			return null;
		MethodInfo af = afsHavingSameBody.get(0);
		if (distance.apply(bf.getName(), af.getName()) < 3)
			return null;
		if(!FixNameMethodReader.isAccepted(bf.getName(), af.getName())) {
			return null;
		}
		if (ALL) {
			String method_txt = bf.getName() + "____" + bf.bodyRaw;
			if (ALL_METHODS_TXT.contains(method_txt))
				return null;
		}
		System.out.println(distance.apply(bf.getName(), af.getName()) + "--" + bf.getName() + "--->" + af.getName());
		METHOD_NAME_FIXES++;
		return af;
	}

	private static List<MethodInfo> extractMethodInfo(String path) {
		List<MethodInfo> methodsInfo = new ArrayList<MethodInfo>();
		File file = new File(path);
		try {
			final ASTParser parser = ASTParser.newParser(AST.JLS8);
			final String src = readFile(file.getAbsolutePath(), StandardCharsets.UTF_8);
			CommentsExtractor ce = new CommentsExtractor();
			Map<String, String> mt_comments = ce.getMT_CMNTs(file.getAbsolutePath());
			MethodRawReader ms = new MethodRawReader();
			Map<String, String> mt_raw = ms.getMT_RAW(file.getAbsolutePath());
			parser.setSource(src.toCharArray());
			parser.setKind(ASTParser.K_COMPILATION_UNIT);
			final Map<?, ?> options = JavaCore.getOptions();
			JavaCore.setComplianceOptions(JavaCore.VERSION_1_7, options);
			parser.setCompilerOptions(options);
			final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
			cu.accept(new AstVisitor());
			String pkg = "";
			try {
				String pck = cu.getPackage().getName().getFullyQualifiedName();
				pkg = pck.split(".")[0];
			} catch (Exception e) {

			}
			final MethodCollector collector = new MethodCollector();
			List<MethodInfo> methods = collector.collect(cu);
			for (MethodInfo info : methods) {
				try {
					info.encode(new ASTReader());
					info.classMates = collector.getClassMates(info.name);
					String comment = "";
					String mtRaw = "";
					try {
						String key = info.getKey();
						comment = mt_comments.get(key);
						mtRaw = mt_raw.get(key);
					} catch (Exception e) {
						e.printStackTrace();
					}
					info.comments = comment;
					info.methodRaw = mtRaw;
					info.pckg = pkg;
					info.comments = mt_comments.get(info.getKey());
					methodsInfo.add(info);
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return methodsInfo;
	}

	static String readFile(final String path, final Charset encoding) throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void getAllMethods() throws Exception {
		try (BufferedReader br = new BufferedReader(
				new FileReader("C:\\Users\\Son\\Desktop" + File.separator + "files.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.contains("\\test\\")) {
					line = line.replace("\\", File.separator);
					File file = new File("C:\\Users\\Son\\Workspace\\2019\\data\\fse19\\" + File.separator + "repos"
							+ File.separator + line);
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
						String pkg = "";
						List<MethodInfo> methods = collector.collect(cu);
						for (MethodInfo info : methods) {
							if (info.bodyRaw.trim().length() > 4) {
								try {
									info.file = line;
									info.encode(new ASTReader());
									info.classMates = collector.getClassMates(info.name);
									String comment = "";
									info.comments = comment;
									info.pckg = pkg;
									String name = info.getName();
									if (name.length() > 2 && info.bodyRaw.length() > 5) {
										String method_txt = info.getName() + "____" + info.bodyRaw;
										ALL_METHODS_TXT.add(method_txt);
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
