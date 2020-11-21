package bm.commit;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import edu.utd.fse19.data.clean.ASTReader;
import edu.utd.fse19.data.clean.MethodCollector;
import edu.utd.fse19.data.clean.MethodInfo;
import edu.utd.fse19.data.clean.AstVisitor;

public class MethodInfoExtractor {

	static String readFile(final String path, final Charset encoding) throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void main(final String[] args) throws Exception {
		Map<String, String> B_N = new HashMap<String, String>();
		File file = new File("/Users/sonnguyen/Desktop/Test.java");
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
				try {
					info.encode(new ASTReader());
					B_N.put(info.bodyRaw, info.getName());
				} catch (Exception e) {
				}
			}
			for (String body : B_N.keySet()) {
				System.out.println(B_N.get(body));
				System.out.println(body);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Map<String, String> extract(String src) throws Exception {
		Map<String, String> B_N = new HashMap<String, String>();
		try {
			final ASTParser parser = ASTParser.newParser(AST.JLS8);
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
				try {
					if (info.bodyRaw.length() > 10) {
						info.encode(new ASTReader());
						B_N.put(info.bodyRaw, info.getName());
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return B_N;
	}

	public static Map<String, String> extract2(String src) throws Exception {
		Map<String, String> B_N = new HashMap<String, String>();
		try {
			final ASTParser parser = ASTParser.newParser(AST.JLS8);
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
				try {
					if (info.bodyRaw.length() > 10) {
						info.encode(new ASTReader());
						String id = info.className + "---" + info.name + info.getParametersType();
						B_N.put(id, info.bodyRaw);
					}
				} catch (Exception e) {
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return B_N;
	}
}