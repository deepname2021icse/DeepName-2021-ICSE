package edu.utd.fse19.data.clean;

/**
 * Maintains an indentation string.
 */
public class Indenter {

	private final StringBuffer indent = new StringBuffer();

	private int indentationSize = 4;

	/**
	 * Gets the indentation string.
	 *
	 * @return a string of blanks which match the indentation level.
	 */
	protected String getIndentString() {
		return indent.toString();
	}

	/**
	 * Sets the space indentation size (default = 4).
	 *
	 * @param indentationSize
	 *            the size of the indentations.
	 */
	public void setIndentationSize(int indentationSize) {
		this.indentationSize = indentationSize;
	}

	/**
	 * Increments the indentation.
	 */
	protected void indent() {
		for (int i = 0; i < indentationSize; i++) {
			indent.append(" ");
		}
	}

	/**
	 * Decrements the indentation.
	 */
	protected void unindent() {
		indent.setLength(indent.length() - indentationSize);
	}
}
