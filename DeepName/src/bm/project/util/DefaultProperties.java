
package bm.project.util;

import java.io.File;

public class DefaultProperties {
	public static String NUM_THREADS = "1";
	public static String MAX_PROJECTS = "1000";
	public static String MAX_COMMITS = "25000";
	public static String MAX_SIZE_FOR_PROJECT_WITH_COMMITS = String.valueOf(1 << 26); // Integer.MAX_VALUE / 3
	
	public static boolean DEBUG = false, DEBUGPARSE = false, CACHE = false;

	// HBase tables
	public static final String HBASE_PROJECTS_TABLE = "projects";
	public static final String HBASE_AST_TABLE = "ast";
	public static final String HBASE_COMMENTS_TABLE = "comments";
	public static final String HBASE_ISSUES_TABLE = "issues";
	public static final String HBASE_LOC_TABLE = "loc";

	public static final String HBASE_PROJECTS_COL = "p";
	public static final String HBASE_AST_COL = "a";
	public static final String HBASE_COMMENTS_COL = "c";
	public static final String HBASE_ISSUES_COL = "i";
	public static final String HBASE_LOC_COL = "l";

	public static final String HBASE_DELIMITER = "!!";

	// Sequence file paths
	public static final String SEQ_PROJECTS_PATH = "projects.seq";
	public static final String SEQ_AST_DIR = "ast";
	public static final String SEQ_AST_PATH = "data";
	public static final String SEQ_COMMENTS_DIR = "comments";
	public static final String SEQ_COMMENTS_PATH = "data";
	public static final String SEQ_ISSUES_DIR = "issues";
	public static final String SEQ_ISSUES_PATH = "data";

	// SF.net paths
	public static final String SF_JSON_PATH = "json";
	public static final String SF_JSON_CACHE_PATH = "json_cache";
	public static final String SF_SVN_PATH = "svn";
	public static final String SF_TICKETS_PATH = "tickets";
	
	// GitHub paths
	public static  String GH_JSON_PATH = "repos-metadata-Boa-upto1213";
	public static String GH_GIT_PATH = "";
	public static String GH_ISSUE_PATH = "";
	public static final String GH_TICKETS_PATH = "tickets";
	public static  String TOKEN = null;
	
	public static String CLASSPATH_ROOT = getClasspathRoot();
	public static  String OUTPUT = "output";
	
	public static boolean STORE_ASCII_PRINTABLE_CONTENTS = true;

	public static String localDataPath = null;
	
	@SuppressWarnings("unused")
	private static String getRoot() {
		File dir = new File(System.getProperty("user.dir"));
		while (dir.getParentFile() != null)
			dir = dir.getParentFile();
		return dir.getAbsolutePath();
	}

	private static String getClasspathRoot() {
		// String path = getRoot() + "/libs";
		String path = System.getProperty("user.dir");
		File dir = new File(path).getParentFile();
		dir = new File(dir, "libs");
		if (!dir.exists())
			dir.mkdirs();
		return dir.getAbsolutePath();
	}
}
