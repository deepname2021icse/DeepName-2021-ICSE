
package bm.project.scm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.hadoop.io.SequenceFile;

public abstract class AbstractConnector implements AutoCloseable {

	protected static final boolean debug = bm.project.util.Properties.getBoolean("debug",
			bm.project.util.DefaultProperties.DEBUG);
	protected static final String classpathRoot = bm.project.util.Properties.getProperty("libs",
			bm.project.util.DefaultProperties.CLASSPATH_ROOT);

	protected String path;
	protected List<AbstractCommit> revisions = new ArrayList<AbstractCommit>();
	protected List<Long> revisionKeys = new ArrayList<Long>();
	protected List<String> branchNames = new ArrayList<String>(), tagNames = new ArrayList<String>();
	protected List<Integer> branchIndices = new ArrayList<Integer>(), tagIndices = new ArrayList<Integer>();
	protected Map<String, Integer> revisionMap = new HashMap<String, Integer>();
	protected String projectName;
	protected int headCommitOffset = -1;
	protected SequenceFile.Writer astWriter, commitWriter, contentWriter;
	protected long astWriterLen = 1, commitWriterLen = 1, contentWriterLen = 1;

	public long getAstWriterLen() {
		return astWriterLen;
	}

	public long getCommitWriterLen() {
		return commitWriterLen;
	}

	public long getContentWriterLen() {
		return contentWriterLen;
	}

	public int getHeadCommitOffset() {
		return this.headCommitOffset;
	}


	public abstract void setRevisions();

	public List<AbstractCommit> getRevisions() {
		return revisions;
	}

	public List<String> getBranchNames() {
		return branchNames;
	}

	public List<String> getTagNames() {
		return tagNames;
	}

	public List<Integer> getBranchIndices() {
		return branchIndices;
	}

	public List<Integer> getTagIndices() {
		return tagIndices;
	}
	public String getPath() {
		return path;
	}
	public String getProjectName() {
		return projectName;
	}
}
