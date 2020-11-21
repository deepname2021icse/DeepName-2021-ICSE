package bm.commit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

import bm.project.scm.GitCommit;
import bm.project.scm.GitConnector;

public class FixedMethodExtrator {

	private static final double THRESHOLD = 0.1;

	public static void main(String[] args) throws RefAlreadyExistsException, RefNotFoundException,
			InvalidRefNameException, CheckoutConflictException, GitAPIException, IOException {
		int i = 0;
		String store = FixNameCommitFilter.FIX_NAME_COMMIT_STORAGE;
		Map<String, Set<String>> bugs = new HashMap<>();
		try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Son\\Desktop\\repos.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String project = line.replace("/", "\\");
				try {
					String path = "C:\\Users\\Son\\Workspace\\2019\\data\\fse19\\repos\\" + project;
					Map<String, String> allMethods = extractAllMethods(path);
					Set<String> buggyMethodNames = new HashSet<>();
					GitConnector connector = new GitConnector(path, project);
					connector.setRevisions();
					List<GitCommit> fixes = connector.getFixRevisions();
					List<FixNameCommit> fnCommits = new ArrayList<>();
					for (GitCommit fix : fixes) {
						fix.getMessage();
						Set<String> changedFiles = fix.getChangedFiles();
						if (!changedFiles.isEmpty()) {
							FixNameCommit fCommit = new FixNameCommit();
							fCommit.afterChangeCommit = fix.getId();
							for (String changedFile : changedFiles) {
								if (!changedFile.toLowerCase().contains("test") && fix.getPreviousVersion() != null) {
									fCommit.beforeChangeCommit = fix.getPreviousVersion().getId();
									String contentAfter = fix.getFileContent(changedFile);
									String contentBefore = fix.getPreviousVersion().getFileContents(changedFile);
									if (!contentBefore.isEmpty() && !contentAfter.isEmpty()) {
										Map<String, String> before = MethodInfoExtractor.extract2(contentBefore);
										Map<String, String> after = MethodInfoExtractor.extract2(contentAfter);
										Set<String> changedMethods = findChangedOnes(before, after, allMethods);
										buggyMethodNames.addAll(changedMethods);
									}
								}
							}
							if (!fCommit.BA.isEmpty()) {
								i++;
								fCommit.save(store, i, project);
								fnCommits.add(fCommit);
							}
						}
					}
					connector.close();

					bugs.put(line, buggyMethodNames);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static Map<String, String> extractAllMethods(String path) {
		return null;
	}

	public static LevenshteinDistance distance = new LevenshteinDistance();

	private static Set<String> findChangedOnes(Map<String, String> before, Map<String, String> after,
			Map<String, String> allMethods) {
		Set<String> results = new HashSet<>();
		for (String mt : before.keySet()) {
			String bb4 = before.get(mt);
			String baf = after.get(mt);
			if (baf != null && bb4 != null) {
				if (baf.equals(bb4) && mt.toLowerCase().contains("test")) {
					if ((double) distance.apply(bb4.toLowerCase(), baf.toLowerCase()) / bb4.length() > THRESHOLD
							&& allMethods.containsKey(mt)) {
						System.out.println(mt);
						results.add(mt);
					}
				}
			}
		}
		return results;
	}
}
