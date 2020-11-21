package edu.utd.fse19.data.clean;


import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Stack;

public class ASTReader extends Indenter implements IASTEncoder {
	private final PrintWriter printer;
	private StringWriter writer;
	private final Stack<Boolean> hasItemsStack = new Stack<Boolean>() {
		private static final long serialVersionUID = 1L;
		{
			push(false);
		}
	};

	public String getEncodedBody() {
		return writer.toString();
	}

	public ASTReader(StringWriter destination) {
		writer = destination;
		this.printer = new PrintWriter(destination);
	}

	public ASTReader() {
		writer = new StringWriter();
		this.printer = new PrintWriter(writer);
	}

	public void startElement(String name, boolean isList) {
	}

	public void endElement(String name, boolean isList) {
	}

	public void startType(String name, boolean parentIsList) {
	}

	public void endType(String name, boolean parentIsList) {
	}


	public void literal(String name, Object value) {
	}

	public void startPrint() {
	}

	public void endPrint() {
	}
}
