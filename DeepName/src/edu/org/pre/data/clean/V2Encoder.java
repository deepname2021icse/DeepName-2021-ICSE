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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Stack;

import org.apache.commons.lang3.StringUtils;

public class V2Encoder extends Indenter implements IASTEncoder {
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

	public V2Encoder() {
		writer = new StringWriter();
		this.printer = new PrintWriter(writer);
	}

	public void startElement(String name, boolean isList) {
		if (hasItemsStack.peek() == true) {
			printer.print(" , ");
		} else {
			hasItemsStack.pop();
			hasItemsStack.push(true);
		}
		printer.print((isList ? " ( " : " { "));
		hasItemsStack.push(false);
	}

	public void endElement(String name, boolean isList) {
		printer.print((isList ? " ) " : " } "));
		hasItemsStack.pop();
	}

	public void startType(String name, boolean parentIsList) {
		if (hasItemsStack.peek() == true) {
			printer.print(",");
		} else {
			hasItemsStack.pop();
			hasItemsStack.push(true);
		}

		if (parentIsList) {
			printer.print(" { ");
		}
		
		printer.print(name);
	}

	public void endType(String name, boolean parentIsList) {
		if (parentIsList) {
			printer.print(" } ");
		}
	}

	public void literal(String name, Object value) {
		if (!("modifiers annotations escapedValue extraDimensions2 extendedOperands typeArguments arguments").contains(name)) {
			if (hasItemsStack.peek() == true) {
				printer.print(" , ");
			} else {
				hasItemsStack.pop();
				hasItemsStack.push(true);
			}
			String v = "null";
			if (value != null)
				v = String.join(" ", StringUtils.splitByCharacterTypeCamelCase(value.toString()));
			
			printer.print(v);
		}
	}

	public void startPrint() {
		printer.print(" { ");
	}

	public void endPrint() {
		printer.print(" } ");
		printer.flush();
		printer.close();
	}

}
