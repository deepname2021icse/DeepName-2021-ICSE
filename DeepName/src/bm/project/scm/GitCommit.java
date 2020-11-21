
package bm.project.scm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffEntry.ChangeType;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.util.io.NullOutputStream;

import bm.project.types.Diff.ChangedFile;
import bm.project.types.Diff.ChangedFile.FileKind;
import bm.project.types.Shared.ChangeKind;

public class GitCommit extends AbstractCommit {
	private Repository repository;
	private RevWalk revwalk;
	Map<String, ObjectId> filePathGitObjectIds = new HashMap<String, ObjectId>();
	Map<String, DiffEntry> filePathDiffs = new HashMap<String, DiffEntry>();
	GitCommit previousVersion;
	String buggyVersion;
	int index;

	public void setPreviousVersion(GitCommit previousVersion) {
		this.previousVersion = previousVersion;
	}
	
	public GitCommit getPreviousVersion() {
		return previousVersion;
	}

	public Map<String, ObjectId> getFilePathGitObjectIds() {
		return filePathGitObjectIds;
	}

	public GitCommit(final GitConnector cnn, final Repository repository, final RevWalk revwalk, String projectName) {
		super(cnn);
		this.repository = repository;
		this.revwalk = revwalk;
		this.projectName = projectName;
	}

	@Override
	/** {@inheritDoc} */
	public String getFileContents(final String path) {
		ObjectId fileid = filePathGitObjectIds.get(path);
		try {
			buffer.reset();
			buffer.write(repository.open(fileid, Constants.OBJ_BLOB).getCachedBytes());
		} catch (final Throwable e) {
//			e.printStackTrace();
			return "";
		}
		return buffer.toString();
	}

	void getChangeFiles(RevCommit rc) {
		if (rc.getParentCount() == 0) {
			TreeWalk tw = new TreeWalk(repository);
			tw.reset();
			try {
				tw.addTree(rc.getTree());
				tw.setRecursive(true);
				while (tw.next()) {
					if (!tw.isSubtree()) {
						String path = tw.getPathString();
						ChangedFile.Builder cfb = ChangedFile.newBuilder();
						cfb.setChange(ChangeKind.ADDED);
						cfb.setName(path);
						cfb.setKind(FileKind.OTHER);
						cfb.setKey(0);
						cfb.setAst(false);
						fileNameIndices.put(path, changedFiles.size());
						changedFiles.add(cfb);
						filePathGitObjectIds.put(path, tw.getObjectId(0));
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			tw.close();
		} else {
			// for (int i = 0; i < rc.getParentCount(); i++) {
			if (rc.getParentCount() == 1) {
				try {
					Integer parentIndex = connector.revisionMap.get(rc.getParent(0).getName());
					if (parentIndex != null) {
						getChangeFiles(revwalk.parseCommit(rc.getParent(0).getId()), parentIndex, rc);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			// }
		}
	}

	private void getChangeFiles(final RevCommit parent, final int parentIndex, final RevCommit rc) {
		this.buggyVersion = parent.getName();
		final DiffFormatter df = new DiffFormatter(NullOutputStream.INSTANCE);
		df.setRepository(repository);
		df.setDiffComparator(RawTextComparator.DEFAULT);
		df.setDetectRenames(true);

		try {
			final AbstractTreeIterator parentIter = new CanonicalTreeParser(null, repository.newObjectReader(),
					parent.getTree());

			List<DiffEntry> diffs = df.scan(parentIter,
					new CanonicalTreeParser(null, repository.newObjectReader(), rc.getTree()));
			for (final DiffEntry diff : diffs) {
				if (diff.getChangeType() == ChangeType.MODIFY) {
					if (diff.getNewMode().getObjectType() == Constants.OBJ_BLOB) {
						getChangeFile(parent, parentIndex, diff, ChangeKind.MODIFIED);
					}
				} else if (diff.getChangeType() == ChangeType.RENAME) {
					if (diff.getNewMode().getObjectType() == Constants.OBJ_BLOB) {
						getChangeFile(parent, parentIndex, diff, ChangeKind.RENAMED);
					}
				} else if (diff.getChangeType() == ChangeType.COPY) {
					if (diff.getNewMode().getObjectType() == Constants.OBJ_BLOB) {
						getChangeFile(parent, parentIndex, diff, ChangeKind.COPIED);
					}
				} else if (diff.getChangeType() == ChangeType.ADD) {
					if (diff.getNewMode().getObjectType() == Constants.OBJ_BLOB) {
						String path = diff.getNewPath();
						ChangedFile.Builder cfb = getChangeFile(path);
						if (cfb.getChange() == null || cfb.getChange() == ChangeKind.UNKNOWN)
							cfb.setChange(ChangeKind.ADDED);
						else if (cfb.getChange() != ChangeKind.ADDED)
							cfb.setChange(ChangeKind.MERGED);
						cfb.addChanges(ChangeKind.ADDED);
						cfb.addPreviousNames("");
						cfb.addPreviousVersions(parentIndex);
						if (cfb.getChange() != ChangeKind.MERGED) {
							filePathGitObjectIds.put(path, diff.getNewId().toObjectId());
							filePathDiffs.put(path, diff);
						}
					}
				} else if (diff.getChangeType() == ChangeType.DELETE) {
					if (diff.getOldMode().getObjectType() == Constants.OBJ_BLOB) {
						String path = diff.getOldPath();
						ChangedFile.Builder cfb = getChangeFile(path);
						if (cfb.getChange() == null || cfb.getChange() == ChangeKind.UNKNOWN)
							cfb.setChange(ChangeKind.DELETED);
						else if (cfb.getChange() != ChangeKind.DELETED)
							cfb.setChange(ChangeKind.MERGED);
						if (cfb.getChange() != ChangeKind.MERGED) {
							filePathGitObjectIds.put(path, diff.getNewId().toObjectId());
							filePathDiffs.put(path, diff);
						}
					}
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
		df.close();
	}

	private void getChangeFile(final RevCommit parent, int parentIndex, final DiffEntry diff, final ChangeKind kind) {
		String path = diff.getNewPath();
		ChangedFile.Builder cfb = getChangeFile(path);
		if (cfb.getChange() == null || cfb.getChange() == ChangeKind.UNKNOWN)
			cfb.setChange(kind);
		else if (cfb.getChange() != kind) {
			// cfb.setChange(ChangeKind.MERGED);
			return;
		}
		cfb.addChanges(kind);
		String oldPath = diff.getOldPath();
		if (oldPath.equals(path))
			cfb.addPreviousNames("");
		else
			cfb.addPreviousNames(oldPath);
		cfb.addPreviousVersions(parentIndex);
		filePathGitObjectIds.put(path, diff.getNewId().toObjectId());
		filePathDiffs.put(path, diff);
	}

	public Map<String, DiffEntry> getFilePathDiffs() {
		return filePathDiffs;
	}

	public int countChangedFiles(RevCommit rc) {
		int count = 0;
		if (rc.getParentCount() == 0) {
			TreeWalk tw = new TreeWalk(repository);
			tw.reset();
			try {
				tw.addTree(rc.getTree());
				tw.setRecursive(true);
				while (tw.next()) {
					if (!tw.isSubtree()) {
						count++;
					}
				}
			} catch (IOException e) {
				if (debug)
					System.err.println(e.getMessage());
			}
			tw.close();
		} else {
			for (int i = 0; i < rc.getParentCount(); i++) {
				final DiffFormatter df = new DiffFormatter(NullOutputStream.INSTANCE);
				df.setRepository(repository);
				df.setDiffComparator(RawTextComparator.DEFAULT);
				df.setDetectRenames(true);

				try {
					RevCommit parent = revwalk.parseCommit(rc.getParent(i).getId());
					final AbstractTreeIterator parentIter = new CanonicalTreeParser(null, repository.newObjectReader(),
							parent.getTree());
					List<DiffEntry> diffs = df.scan(parentIter,
							new CanonicalTreeParser(null, repository.newObjectReader(), rc.getTree()));
					if (diffs.size() > count)
						count = diffs.size();
				} catch (final IOException e) {
					if (debug)
						System.err.println("Git Error getting commit diffs: " + e.getMessage());
				}
				df.close();
			}
		}
		return count;
	}

	public String getFileContent(String path) {
		DiffEntry diff = filePathDiffs.get(path);
		ObjectId fileid = diff.getNewId().toObjectId();
		try {
			buffer.reset();
			buffer.write(repository.open(fileid, Constants.OBJ_BLOB).getCachedBytes());
		} catch (final Throwable e) {
//			e.printStackTrace();
			return "";
		}
		return buffer.toString();
	}

	public String getDiff(String path) {
		DiffEntry diff = filePathDiffs.get(path);
		OutputStream outputStream = new ByteArrayOutputStream();
		try (DiffFormatter formatter = new DiffFormatter(outputStream)) {
			formatter.setRepository(repository);
			formatter.format(diff);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String d = outputStream.toString();
		return d;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public int getIndex() {
		return index;
	}

	public String getBuggyVersion() {
		return buggyVersion;
	}

	public void setBuggyVersion(String buggyVersion) {
		this.buggyVersion = buggyVersion;
	}

	public Set<String> getChangedFiles() {
		Set<String> changedFiles = new HashSet<String>();
		for (String f : this.filePathDiffs.keySet()) {
			if (f.endsWith(".java"))
				changedFiles.add(f);
		}
		return changedFiles;
	}
}
