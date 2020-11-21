package edu.utd.fse19.data.clean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class MethodCollector {
	String className;
	Set<String> classMates = new HashSet<>();

	public String getClassName() {
		return className;
	}

	public Set<String> getClassMates(String me) {
		Set<String> mates = new HashSet<>(classMates);
		mates.remove(me);
		return mates;
	}

	public MethodCollector() {
	}

	public List<MethodInfo> collect(final ASTNode rootNode) {
		List<MethodInfo> methodInfos = new ArrayList<>();
		for (ASTNode n : getChildren(rootNode)) {
			if (n instanceof TypeDeclaration) {
				TypeDeclaration type = (TypeDeclaration) n;
				className = type.getName().getIdentifier();
				MethodDeclaration[] methods = type.getMethods();
				for (ASTNode node : methods) {
					if (node instanceof MethodDeclaration) {
						MethodInfo info = extractMethodInfo(node);
						if (info != null) {
							methodInfos.add(info);
							info.decl = (MethodDeclaration) node;
						}
					}
				}
			}
		}
		return methodInfos;
	}

	private MethodInfo extractMethodInfo(ASTNode method) {
		MethodDeclaration dcl = (MethodDeclaration) method;
		if (!dcl.isConstructor()) {
			if (dcl.getBody() == null)
				return null;
			String methodName = dcl.getName().toString();
			String returnType = "";
			if (dcl.getReturnType2() != null)
				returnType = dcl.getReturnType2().toString();
			List<ASTNode> parameters = dcl.parameters();
			List<String> parametersType = new ArrayList<>();
			MethodInfo mi = new MethodInfo();
			mi.signature = method.toString();
			for (ASTNode n : parameters) {
				SingleVariableDeclaration pn = (SingleVariableDeclaration) n;
				parametersType.add(pn.getType().toString());
			}
			mi.bodyRaw = dcl.getBody().toString();
			mi.returnedType = returnType;
			mi.name = methodName;
			mi.parametersType = parametersType;
			mi.bodyAST = dcl.getBody();
			mi.methodRaw = dcl.toString();
			mi.className = className;
			mi.doc = "";
			if (dcl.getJavadoc() != null)
				mi.doc = dcl.getJavadoc().toString();
			classMates.add(methodName);
			return mi;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private static ArrayList<ASTNode> getChildren(ASTNode node) {
		ArrayList<ASTNode> flist = new ArrayList<ASTNode>();
		List<Object> list = node.structuralPropertiesForType();
		for (int i = 0; i < list.size(); i++) {
			StructuralPropertyDescriptor curr = (StructuralPropertyDescriptor) list.get(i);
			Object child = node.getStructuralProperty(curr);
			if (child instanceof List) {
				flist.addAll((Collection<? extends ASTNode>) child);
			} else if (child instanceof ASTNode) {
				flist.add((ASTNode) child);
			} else {
			}
		}
		return flist;
	}
}