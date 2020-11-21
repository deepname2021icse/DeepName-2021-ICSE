package edu.utd.fse19.data.clean;
import java.util.HashMap;
import java.util.Map;

public interface IASTEncoder {
	public static final int LEN = 4;
	//use ast to get key;
	public static Map<String, String> AST_KEY_Mapper = new HashMap<>();
	//use key to get ast
	public static Map<String, String> KEY_AST_Mapper = new HashMap<>();
	
	public void startPrint();

	public void endPrint();

	public void startElement(String name, boolean isList);

	public void startType(String name, boolean parentIsList);

	public void endType(String name, boolean parentIsList);

	public void endElement(String name, boolean isList);

	public void literal(String name, Object value);
	
	public String getEncodedBody();
	public static Map<String, String> getASTMapper() {
		return AST_KEY_Mapper;
	}
	public static Map<String, String> getKEYMapper() {
		return AST_KEY_Mapper;
	}
}
