package bm.project.scm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.io.SequenceFile.Writer;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand.ResetType;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import bm.project.util.DefaultProperties;

public class GitConnector extends AbstractConnector {
	private Repository repository;
	private Git git;
	private RevWalk revwalk;
	private static final int MAX_COMMITS = Integer.valueOf(DefaultProperties.MAX_COMMITS);
	private List<GitCommit> fixRevisions = new ArrayList<GitCommit>();
	String middleVersionID;
	String headVersionID;
	String currentVersion = "";

	public GitConnector(final String path, String projectName) {
		this.projectName = projectName;
		try {
			this.path = path;
			this.repository = new FileRepositoryBuilder().setGitDir(new File(path + "/.git")).build();
			this.git = new Git(this.repository);
			this.revwalk = new RevWalk(this.repository);
		} catch (final IOException e) {
			if (debug)
				System.err.println("Git Error connecting to " + path + ". " + e.getMessage());
		}
	}

	public GitConnector(String path, String projectName, Writer astWriter, long astWriterLen, Writer commitWriter,
			long commitWriterLen, Writer contentWriter, long contentWriterLen) {
		this(path, projectName);
		this.astWriter = astWriter;
		this.commitWriter = commitWriter;
		this.contentWriter = contentWriter;
		this.astWriterLen = astWriterLen;
		this.commitWriterLen = commitWriterLen;
		this.contentWriterLen = contentWriterLen;
	}

	@Override
	public void close() {
		revwalk.close();
		repository.close();
	}

	public void countChangedFiles(List<String> commits, Map<String, Integer> counts) {
		RevWalk temprevwalk = new RevWalk(repository);
		try {
			revwalk.reset();
			Set<RevCommit> heads = getHeads();
			revwalk.markStart(heads);
			revwalk.sort(RevSort.TOPO, true);
			revwalk.sort(RevSort.COMMIT_TIME_DESC, true);
			revwalk.sort(RevSort.REVERSE, true);
			for (final RevCommit rc : revwalk) {
				final GitCommit gc = new GitCommit(this, repository, temprevwalk, projectName);
				System.out.println(rc.getName());
				commits.add(rc.getName());
				int count = gc.countChangedFiles(rc);
				counts.put(rc.getName(), count);
			}
		} catch (final IOException e) {
			if (debug)
				System.err.println("Git Error getting parsing HEAD commit for " + path + ". " + e.getMessage());
		} finally {
			temprevwalk.dispose();
			temprevwalk.close();
		}
	}

	@Override
	public void setRevisions() {
		RevWalk temprevwalk = new RevWalk(repository);
		try {
			revwalk.reset();
			Set<RevCommit> heads = getHeads();
			revwalk.markStart(heads);
			revwalk.sort(RevSort.TOPO, true);
			revwalk.sort(RevSort.COMMIT_TIME_DESC, true);
			revwalk.sort(RevSort.REVERSE, true);
			revisionMap = new HashMap<String, Integer>();
			List<RevCommit> commitList = new ArrayList<RevCommit>();
			for (RevCommit rc : revwalk) {
				commitList.add(rc);
			}
			int i = 0;
			GitCommit previous = null;
			for (final RevCommit rc : commitList) {
				String message = rc.getFullMessage();
				final GitCommit gc = new GitCommit(this, repository, temprevwalk, projectName);
				gc.setId(rc.getName());
				gc.setDate(new Date(((long) rc.getCommitTime()) * 1000));
				if (message != null && !message.trim().isEmpty()) {
					gc.setMessage(message);
					gc.getChangeFiles(rc);
					if (previous != null)
						gc.setPreviousVersion(previous);
					gc.fileNameIndices.clear();
					fixRevisions.add(gc);
					gc.setIndex(i++);

					if (fixRevisions.size() == 500000)
						return;
					// Previous
					previous = new GitCommit(this, repository, temprevwalk, projectName);
					previous.setId(rc.getName());
					previous.setDate(new Date(((long) rc.getCommitTime()) * 1000));
					previous.setMessage(message);
					previous.getChangeFiles(rc);
					previous.fileNameIndices.clear();
				}
				if (commitList.size() > MAX_COMMITS) {
					revisionMap.put(gc.id, revisionKeys.size());
				} else {
					revisionMap.put(gc.id, revisions.size());
					revisions.add(gc);
				}
			}
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			temprevwalk.dispose();
			temprevwalk.close();
		}
	}

	private Set<RevCommit> getHeads() {
		Set<RevCommit> heads = new HashSet<RevCommit>();
		try {
			for (final Ref ref : git.branchList().call()) {
				heads.add(revwalk.parseCommit(repository.resolve(ref.getName())));
			}
		} catch (final GitAPIException e) {
			if (debug)
				System.err.println("Git Error reading heads: " + e.getMessage());
		} catch (final IOException e) {
			if (debug)
				System.err.println("Git Error reading heads: " + e.getMessage());
		}
		return heads;
	}

	public List<String> getSnapshot(String commit) {
		ArrayList<String> snapshot = new ArrayList<String>();
		TreeWalk tw = new TreeWalk(repository);
		tw.reset();
		try {
			RevCommit rc = revwalk.parseCommit(repository.resolve(commit));
			tw.addTree(rc.getTree());
			tw.setRecursive(true);
			while (tw.next()) {
				if (!tw.isSubtree()) {
					String path = tw.getPathString();
					snapshot.add(path);
				}
			}
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
		tw.close();
		return snapshot;
	}

	public List<GitCommit> getFixRevisions() {
		return fixRevisions;
	}

	public List<String> logCommitIds() {
		List<String> commits = new ArrayList<String>();
		RevWalk temprevwalk = new RevWalk(repository);
		try {
			revwalk.reset();
			Set<RevCommit> heads = getHeads();
			revwalk.markStart(heads);
			revwalk.sort(RevSort.TOPO, true);
			revwalk.sort(RevSort.COMMIT_TIME_DESC, true);
			revwalk.sort(RevSort.REVERSE, true);

			for (final RevCommit rc : revwalk)
				commits.add(rc.getName());
		} catch (final IOException e) {
			e.printStackTrace();
		} finally {
			temprevwalk.dispose();
			temprevwalk.close();
		}
		return commits;
	}

	public void checkout(String commitID) throws CheckoutConflictException, GitAPIException {
		System.out.println("Checkout: " + commitID);
		git.reset().setRef(commitID).setMode(ResetType.HARD).call();
		currentVersion = commitID;
	}

	public void goHead() throws CheckoutConflictException, GitAPIException {
		if (!currentVersion.isEmpty() && !currentVersion.equals(headVersionID)) {
			git.reset().setRef(headVersionID).setMode(ResetType.HARD).call();
		}
	}
}
