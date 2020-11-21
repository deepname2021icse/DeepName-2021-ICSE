package bm.commit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRefNameException;
import org.eclipse.jgit.api.errors.RefAlreadyExistsException;
import org.eclipse.jgit.api.errors.RefNotFoundException;

import bm.project.scm.GitCommit;
import bm.project.scm.GitConnector;

public class FixCommitExtractor {

	public static void main(String[] args) throws RefAlreadyExistsException, RefNotFoundException,
			InvalidRefNameException, CheckoutConflictException, GitAPIException, IOException {
		int i = 0;
		String store = FixNameCommitFilter.FIX_NAME_COMMIT_STORAGE;

		try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Son\\Desktop\\repos.txt"))) {
			String line;
			while ((line = br.readLine()) != null) {
				String project = line.replace("/", "\\");
				try {
					String path = "C:\\Users\\Son\\Workspace\\2019\\data\\fse19\\repos\\" + project;

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
										if (hasFixedNameMethod(contentBefore, contentAfter)) {
											ExtendedFile before = new ExtendedFile(changedFile, contentBefore);
											ExtendedFile after = new ExtendedFile(changedFile, contentAfter);
											fCommit.BA.put(before, after);
										}
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

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static boolean hasFixedNameMethod(String contentBefore, String contentAfter) {
		try {
			Map<String, String> before = MethodInfoExtractor.extract(contentBefore);
			Map<String, String> after = MethodInfoExtractor.extract(contentAfter);
			for (String body 	: before.keySet()) {
				String nameBfore = before.get(body);
				String nameAfter = after.get(body);
				if (nameBfore != null && nameAfter != null
						&& !nameBfore.replace(" ", "").equalsIgnoreCase(nameAfter.replace(" ", ""))) {
					if (!nameAfter.toLowerCase().contains("test")) {
						System.out.println(nameBfore + "-->" + nameAfter);
						return true;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}


}
