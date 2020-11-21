package edu.utd.fse19.data.clean;

//===========================================================================
//
//  This program use Eclipse JDT to parse java source files 
//  and dumps resulting AST in JSON representation.
//
//---------------------------------------------------------------------------
//
//  Copyright (C) 2015
//  by Oleg Mazko(o.mazko@mail.ru).
//
//  The author gives unlimited permission to copy and distribute
//  this file, with or without modifications, as long as this notice
//  is preserved, and any changes are properly documented.
//

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class App {
	static StringBuffer encodedSrcs_1 = new StringBuffer();
	static StringBuffer encodedSrcs_2 = new StringBuffer();
	static StringBuffer encodedSrcs_3 = new StringBuffer();
	static StringBuffer encodedSrcs_4 = new StringBuffer();
	static StringBuffer methodNames = new StringBuffer();
	static int COUNTER = 0;
	static int COUNTER2 = 0;

	static String readFile(final String path, final Charset encoding) throws IOException {
		final byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public static void main(final String[] args) throws Exception {
		try (BufferedReader br = new BufferedReader(new FileReader(DataCleaner.DATA + File.separator + "files.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
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
									info.classMates = collector.getClassMates(info.name);
									String name = info.getName();
									String source_1 = info.getEncodedSrc1();
									String source_2 = info.getEncodedSrc2();
									String source_3 = info.getEncodedSrc3();
									String source_4 = info.getEncodedSrc4();
									if (name.length() > 2 && info.bodyRaw.length() > 5) {
										encodedSrcs_1.append(source_1 + "\n");
										encodedSrcs_2.append(source_2 + "\n");
										encodedSrcs_3.append(source_3 + "\n");
										encodedSrcs_4.append(source_4 + "\n");
										methodNames.append(name + "\n");
										COUNTER++;
										COUNTER2++;
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
					} catch (Exception e) {
					}
				}
				if (COUNTER > 20000) {
					System.out.println(DataCleaner.DATA + "/corpus6/name/" + COUNTER2 + "_names.txt");
					Utils.write(DataCleaner.DATA + "/corpus6/name/" + COUNTER2 + "_names.txt", methodNames.toString());
					Utils.write(DataCleaner.DATA + "/corpus6/src_1/" + COUNTER2 + "_srcs.txt", encodedSrcs_1.toString());
					Utils.write(DataCleaner.DATA + "/corpus6/src_2/" + COUNTER2 + "_srcs.txt", encodedSrcs_2.toString());
					Utils.write(DataCleaner.DATA + "/corpus6/src_3/" + COUNTER2 + "_srcs.txt", encodedSrcs_3.toString());
					Utils.write(DataCleaner.DATA + "/corpus6/src_4/" + COUNTER2 + "_srcs.txt", encodedSrcs_4.toString());
					methodNames = new StringBuffer("");
					encodedSrcs_1 = new StringBuffer("");
					encodedSrcs_2 = new StringBuffer("");
					encodedSrcs_3 = new StringBuffer("");
					encodedSrcs_4 = new StringBuffer("");
					COUNTER = 0;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (!methodNames.toString().isEmpty()) {
			Utils.write(DataCleaner.DATA + "/corpus6/name/" + COUNTER2 + "_names.txt", methodNames.toString());
			Utils.write(DataCleaner.DATA + "/corpus6/src_1/" + COUNTER2 + "_srcs.txt", encodedSrcs_1.toString());
			Utils.write(DataCleaner.DATA + "/corpus6/src_2/" + COUNTER2 + "_srcs.txt", encodedSrcs_2.toString());
			Utils.write(DataCleaner.DATA + "/corpus6/src_3/" + COUNTER2 + "_srcs.txt", encodedSrcs_3.toString());
			Utils.write(DataCleaner.DATA + "/corpus6/src_4/" + COUNTER2 + "_srcs.txt", encodedSrcs_4.toString());
		}
	}
}