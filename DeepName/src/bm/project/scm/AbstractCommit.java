
package bm.project.scm;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import bm.project.types.Diff.ChangedFile;
import bm.project.types.Diff.ChangedFile.FileKind;
import bm.project.util.DefaultProperties;
import bm.project.util.FileIO;
import bm.project.util.Properties;

public abstract class AbstractCommit {
	protected static final boolean debug = Properties.getBoolean("debug", DefaultProperties.DEBUG);
	protected static final boolean debugparse = Properties.getBoolean("debugparse", DefaultProperties.DEBUGPARSE);
	protected static final boolean STORE_ASCII_PRINTABLE_CONTENTS = Properties.getBoolean("ascii",
			DefaultProperties.STORE_ASCII_PRINTABLE_CONTENTS);

	protected AbstractConnector connector;
	protected String projectName;

	protected AbstractCommit(AbstractConnector cnn) {
		this.connector = cnn;
	}

	protected Map<String, Integer> fileNameIndices = new HashMap<String, Integer>();

	protected List<ChangedFile.Builder> changedFiles = new ArrayList<ChangedFile.Builder>();

	protected ChangedFile.Builder getChangeFile(String path) {
		ChangedFile.Builder cfb = null;
		Integer index = fileNameIndices.get(path);
		if (index == null) {
			cfb = ChangedFile.newBuilder();
			cfb.setName(path);
			cfb.setKind(FileKind.OTHER);
			cfb.setKey(0);
			cfb.setAst(false);
			fileNameIndices.put(path, changedFiles.size());
			changedFiles.add(cfb);
		} else
			cfb = changedFiles.get(index);
		return cfb;
	}

	protected String id = null;

	public String getId() {
		return id;
	}

	public void setId(final String id) {
		this.id = id;
	}

	protected String message;

	public void setMessage(final String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	protected Date date;

	public void setDate(final Date date) {
		this.date = date;
	}

	public Date getDate() {
		return date;
	}

	protected int[] parentIndices;

	protected List<Integer> childrenIndices = new LinkedList<Integer>();

	protected static final ByteArrayOutputStream buffer = new ByteArrayOutputStream(4096);

	public abstract String getFileContents(final String path);

	protected String processLOC(final String path) {
		String loc = "";

		final String lowerPath = path.toLowerCase();
		if (!(lowerPath.endsWith(".txt") || lowerPath.endsWith(".xml") || lowerPath.endsWith(".java")))
			return loc;

		final String content = getFileContents(path);

		final File dir = new File(new File(System.getProperty("java.io.tmpdir")), UUID.randomUUID().toString());
		final File tmpPath = new File(dir, path.substring(0, path.lastIndexOf("/")));
		tmpPath.mkdirs();
		final File tmpFile = new File(tmpPath, path.substring(path.lastIndexOf("/") + 1));
		FileIO.writeFileContents(tmpFile, content);

		try {
			final Process proc = Runtime.getRuntime()
					.exec(new String[] { "/home/boa/ohcount/bin/ohcount", "-i", tmpFile.getPath() });

			final BufferedReader outStream = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while ((line = outStream.readLine()) != null)
				loc += line;
			outStream.close();

			proc.waitFor();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		try {
			FileIO.delete(dir);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		return loc;
	}
}
