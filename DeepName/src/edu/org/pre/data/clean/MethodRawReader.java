package edu.utd.fse19.data.clean;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

public class MethodRawReader {
	private Map<String, String> mt_raw = new HashMap<>();

	public static void main(String[] args) throws Exception {
		MethodRawReader ms = new MethodRawReader();
		Map<String, String> mt_comments = ms.getMT_RAW("C:\\Users\\Son\\Desktop\\Test.java");
		for (String mt : mt_comments.keySet()) {
			System.out.println(mt + "--" + mt_comments.get(mt));
		}
	}

	public Map<String, String> getMT_RAW(String file) throws Exception {
		FileInputStream in = new FileInputStream(file);
		try {
			CompilationUnit cu = JavaParser.parse(in);
			MethodVisitor mv = new MethodVisitor();
			cu.accept(mv, null);
			return mt_raw;
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
				mt_raw.put(n.getNameAsString() + "-" + parametersType, n.toString());
			} catch (Exception e) {
				e.printStackTrace();
			}
			super.visit(n, arg);
		}

	}
}
