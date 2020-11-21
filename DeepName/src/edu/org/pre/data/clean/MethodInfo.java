package edu.utd.fse19.data.clean;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ChildPropertyDescriptor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimplePropertyDescriptor;

public class MethodInfo {
	private static int ID = 100000000;
	private String id = "";
	public String file;
	public String name;
	public List<String> parametersType;
	public String returnedType;
	public String bodyRaw;
	public String bodyTree;
	public String bodyEncoded;
	public Block bodyAST;
	public String methodRaw;
	private IASTEncoder encoder;
	public String pckg;
	public String doc;
	public String comments;
	public String signature;
	public Set<String> identifiers = new HashSet<>();
	public String className;
	public Set<String> classMates = new HashSet<>();
	public MethodDeclaration decl;

	// public List<String> identifiers = new ArrayList<>();

	public String getKey() {
		String pt = "";
		for (String p : parametersType)
			pt += (p + "-");
		if (parametersType.isEmpty())
			pt = "void";

		return name + "-" + pt;
	}

	public MethodInfo() {
		id = "M" + (ID++);
	}

	public String getID() {
		return id;
	}

	public void encode(IASTEncoder e) {
		this.encoder = e;
		walk(bodyAST, false);
		bodyEncoded = encoder.getEncodedBody();
	}

	public String getName() {
		return String.join(" ", StringUtils.splitByCharacterTypeCamelCase(name)).replace("_", " ")
				.replaceAll("\\s+", " ").trim();
	}

	private void walk(final ASTNode node, final boolean parentIsList) {
		// if (node instanceof Comment || node instanceof BlockComment) {
		// System.out.println(node.toString());
		// }

		encoder.startType(node.getClass().getSimpleName(), parentIsList);
		final List<?> properties = node.structuralPropertiesForType();

		for (Iterator<?> iterator = properties.iterator(); iterator.hasNext();) {
			final Object desciptor = iterator.next();

			if (desciptor instanceof SimplePropertyDescriptor) {
				SimplePropertyDescriptor simple = (SimplePropertyDescriptor) desciptor;
				Object value = node.getStructuralProperty(simple);
				if (simple.getId().equals("identifier")) {
					identifiers.add(value.toString());
				}
				encoder.literal(simple.getId(), value);
			} else if (desciptor instanceof ChildPropertyDescriptor) {
				ChildPropertyDescriptor child = (ChildPropertyDescriptor) desciptor;
				ASTNode childNode = (ASTNode) node.getStructuralProperty(child);
				if (childNode != null) {
					encoder.startElement(child.getId(), false);
					walk(childNode);
					encoder.endElement(child.getId(), false);
				} else {
					encoder.literal(child.getId(), null);
				}
			} else {
				ChildListPropertyDescriptor list = (ChildListPropertyDescriptor) desciptor;
				List<?> value = new ArrayList<>((List<?>) node.getStructuralProperty(list));
				if (value.size() > 0) {
					encoder.startElement(list.getId(), true);
					walk(value);
					encoder.endElement(list.getId(), true);
				} else {
					if (list.getId().equals("identifier")) {
						identifiers.add(value.toString());
					}
					encoder.literal(list.getId(), value);
				}
			}
		}

		encoder.endType(node.getClass().getSimpleName(), parentIsList);
	}

	private void walk(final ASTNode node) {
		walk(node, false);
	}

	private void walk(final List<?> nodes) {
		for (Iterator<?> iterator = nodes.iterator(); iterator.hasNext();) {
			ASTNode node = (ASTNode) iterator.next();
			walk(node, true);
		}
	}

	/**
	 * raw body
	 * 
	 * @return
	 */
	public String getEncodedSrc1() {
		String r = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(bodyRaw));
		r.replace("\n", " ");
		return r.replace("_", " ").replaceAll("\\s+", " ").trim().toLowerCase();
	}

	/**
	 * Names in body only
	 * 
	 * @return
	 */
	public String getEncodedSrc2() {
		StringBuffer result = new StringBuffer();
		for (String identifier : identifiers) {
			result.append(identifier + " ");
		}
		String r = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(result.toString()));
		return r.replace("_", " ").replaceAll("\\s+", " ").trim().toLowerCase();
	}

	/**
	 * Return type + parameters + names in bodies
	 * 
	 * @return
	 */
	public String getEncodedSrc3() {
		StringBuffer result = new StringBuffer();
		result.append(" { ");
		result.append(returnedType);
		result.append(" } ");
		// Parameter
		result.append(" { ");
		for (String t : parametersType) {
			result.append(t + " ");
		}
		result.append(" } ");
		result.append(" { ");
		for (String identifier : identifiers) {
			result.append(identifier + " ");
		}
		result.append(" } ");
		String r = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(result.toString()));
		r = r.replace("_", " ").replaceAll("\\s+", " ").trim().toLowerCase();
		return r.replace("{}", "{ }");
	}

	/**
	 * Return type + parameters + names in bodies + class + classmates
	 * 
	 * @return
	 */
	public String getEncodedSrc4() {
		StringBuffer result = new StringBuffer();
		result.append(" { ");
		result.append(className);
		result.append(" } ");
		result.append(" { ");
		for (String mate : classMates) {
			result.append(mate + " ");
		}
		result.append(" } ");
		result.append(" { ");
		result.append(returnedType);
		result.append(" } ");
		// Parameter
		result.append(" { ");
		for (String t : parametersType) {
			result.append(t + " ");
		}
		result.append(" } ");
		result.append(" { ");
		for (String identifier : identifiers) {
			result.append(identifier + " ");
		}
		result.append(" } ");
		String r = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(result.toString()));
		r = r.replace("_", " ").replaceAll("\\s+", " ").trim().toLowerCase();
		return r.replace("{}", "{ }");
	}

	/**
	 * Return type + parameters + names in bodies + class
	 * 
	 * @return
	 */
	public String getEncodedSrc6() {
		StringBuffer result = new StringBuffer();
		result.append(" { ");
		result.append(className);
		result.append(" } ");

		result.append(" { ");
		result.append(returnedType);
		result.append(" } ");
		// Parameter
		result.append(" { ");
		for (String t : parametersType) {
			result.append(t + " ");
		}
		result.append(" } ");
		result.append(" { ");
		for (String identifier : identifiers) {
			result.append(identifier + " ");
		}
		result.append(" } ");
		String r = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(result.toString()));
		r = r.replace("_", " ").replaceAll("\\s+", " ").trim().toLowerCase();
		return r.replace("{}", "{ }");
	}

	/**
	 * Return type + parameters + names in bodies + class + classmates + callers
	 * 
	 * @return
	 */
	public String getEncodedSrc5() {
		if (identifiers.isEmpty())
			return "";
		StringBuffer result = new StringBuffer();
		for (String identifier : identifiers) {
			result.append(identifier + " ");
		}
		String r = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(result.toString()));
		return r.replace("_", " ").replaceAll("\\s+", " ").trim().toLowerCase();
	}

	/**
	 * Return type + parameters + names in bodies + class + pckg
	 * 
	 * @return
	 */
	public String getEncodedSrc7() {
		StringBuffer result = new StringBuffer();
		result.append(" { ");
		result.append(pckg);
		result.append(" } ");
		result.append(" { ");
		result.append(className);
		result.append(" } ");

		result.append(" { ");
		result.append(returnedType);
		result.append(" } ");
		// Parameter
		result.append(" { ");
		for (String t : parametersType) {
			result.append(t + " ");
		}
		result.append(" } ");
		result.append(" { ");
		for (String identifier : identifiers) {
			result.append(identifier + " ");
		}
		result.append(" } ");
		String r = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(result.toString()));
		r = r.replace("_", " ").replaceAll("\\s+", " ").trim().toLowerCase();
		return r.replace("{}", "{ }");
	}

	/**
	 * Return type + parameters + names in bodies + classmates
	 * 
	 * @return
	 */
	public String getEncodedSrc8() {
		StringBuffer result = new StringBuffer();
		result.append(" { ");
		for (String mate : classMates) {
			result.append(mate + " ");
		}
		result.append(" } ");
		result.append(" { ");
		result.append(returnedType);
		result.append(" } ");
		// Parameter
		result.append(" { ");
		for (String t : parametersType) {
			result.append(t + " ");
		}
		result.append(" } ");
		result.append(" { ");
		for (String identifier : identifiers) {
			result.append(identifier + " ");
		}
		result.append(" } ");
		String r = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(result.toString()));
		r = r.replace("_", " ").replaceAll("\\s+", " ").trim().toLowerCase();
		return r.replace("{}", "{ }");
	}

	/**
	 * Return type + parameters + names in bodies + class+ doc
	 * 
	 * @return
	 */
	public String getEncodedSrc9() {
		StringBuffer result = new StringBuffer();
		result.append(" { ");
		result.append(getDocAndComments(doc));
		result.append(" } ");
		result.append(" { ");
		result.append(className);
		result.append(" } ");
		result.append(" { ");
		result.append(returnedType);
		result.append(" } ");
		// Parameter
		result.append(" { ");
		for (String t : parametersType) {
			result.append(t + " ");
		}
		result.append(" } ");
		result.append(" { ");
		for (String identifier : identifiers) {
			result.append(identifier + " ");
		}
		result.append(" } ");
		String r = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(result.toString()));
		r = r.replace("_", " ").replaceAll("\\s+", " ").trim().toLowerCase();
		return r.replace("{}", "{ }");
	}

	/**
	 * Return type + parameters + names in bodies + class+ comment + doc
	 * 
	 * @return
	 */
	public String getEncodedSrc10() {
		StringBuffer result = new StringBuffer();
		result.append(" { ");
		result.append(getDocAndComments(doc));
		result.append(" } ");
		result.append(" { ");
		result.append(getDocAndComments(comments));
		result.append(" } ");
		result.append(" { ");
		result.append(className);
		result.append(" } ");
		result.append(" { ");
		result.append(returnedType);
		result.append(" } ");
		// Parameter
		result.append(" { ");
		for (String t : parametersType) {
			result.append(t + " ");
		}
		result.append(" } ");
		result.append(" { ");
		for (String identifier : identifiers) {
			result.append(identifier + " ");
		}
		result.append(" } ");
		String r = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(result.toString()));
		r = r.replace("_", " ").replaceAll("\\s+", " ").trim().toLowerCase();
		return r.replace("{}", "{ }");
	}

	private String getDocAndComments(String dc) {
		if (dc == null)
			return "";
		String d = dc.replace("/", "").replace("*", "").replace("@", "");
		d = d.replace("{", "").replace("}", "").replace("#", " ").replace("(", " ").replace(")", " ").replace(".", " ");
		d = d.replace("\n", " ");
		return d;
	}
	
	public String getParametersType() {
		StringBuffer result = new StringBuffer();
		result.append(" { ");
		for (String t : parametersType) {
			result.append(t + "-");
		}
		result.append(" } ");
		return result.toString();
	}
}
