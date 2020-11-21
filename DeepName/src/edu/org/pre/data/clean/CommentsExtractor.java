package edu.utd.fse19.data.clean;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class CommentsExtractor {
	private Map<String, String> mt_comments = new HashMap<>();

	public static void main(String[] args) throws Exception {
		CommentsExtractor ms = new CommentsExtractor();
		Map<String, String> mt_comments = ms.getMT_CMNTs("/Users/sonnguyen/Desktop/Test.java");
		for (String mt : mt_comments.keySet()) {
			System.out.println(mt + "--" + mt_comments.get(mt));
		}
	}

	public Map<String, String> getMT_CMNTs(String file) throws Exception {
		FileInputStream in = new FileInputStream(file);
		try {
			CompilationUnit cu = JavaParser.parse(in);
			MethodVisitor mv = new MethodVisitor();
			cu.accept(mv, null);
			return mt_comments;
		} catch (ParseProblemException e) {
		}
		return null;
	}

	private class MethodVisitor extends VoidVisitorAdapter<Void> {
		@Override
		public void visit(MethodDeclaration n, Void arg) {
			String parametersType = "";
			for (Parameter p : n.getParameters())
				parametersType += p.getTypeAsString() + "-";
			if (parametersType.isEmpty())
				parametersType = "void";
			try {
				String comments = extractComments(n.getBody().toString());
				mt_comments.put(n.getNameAsString() + "-" + parametersType, comments);
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.visit(n, arg);
		}

		public String extractComments(String content) throws Exception {
			if (content.length() > 5000)
				return "";

			StringBuffer comments = new StringBuffer();

			String slComment = "//[^\r\n]*";
			String mlComment = "/\\*[\\s\\S]*?\\*/";
			String strLit = "\"(?:\\\\.|[^\\\\\"\r\n])*\"";
			String chLit = "'(?:\\\\.|[^\\\\'\r\n])+'";
			String any = "[\\s\\S]";

			Pattern p = Pattern.compile(String.format("(%s)|(%s)|%s|%s|%s", slComment, mlComment, strLit, chLit, any));

			Matcher m = p.matcher(content);

			while (m.find()) {
				String hit = m.group();
				if (m.group(1) != null) {
					comments.append(hit.replace("\n", " ") + " ");
				}
				if (m.group(2) != null) {
					comments.append(hit.replace("\n", " ") + " ");
				}
			}
			return comments.toString();
		}
	}
}
