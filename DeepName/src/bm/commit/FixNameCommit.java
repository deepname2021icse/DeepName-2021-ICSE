package bm.commit;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FixNameCommit {
	String beforeChangeCommit;
	String afterChangeCommit;
	public Map<ExtendedFile, ExtendedFile> BA = new HashMap<>();

	public void save(String path, int id, String repo) {
		File dir = new File(path + File.separator + id);
		dir.mkdirs();
		String info = repo + "\n" + beforeChangeCommit + "\n" + afterChangeCommit;
		String changedFile = "";
		int counter = 0;
		for (ExtendedFile bf : BA.keySet()) {
			changedFile += (bf.file + "\n");
			String fileName = new File(bf.file).getName();
			File change = new File(dir.getAbsolutePath() + File.separator + counter);
			change.mkdir();
			Utils.write(change.getAbsolutePath() + File.separator + fileName + ".bf", bf.content);
			Utils.write(change.getAbsolutePath() + File.separator + fileName + ".af", BA.get(bf).content);
			counter++;
		}
		Utils.write(dir.getAbsolutePath() + File.separator + "changed_files.txt", changedFile);
		Utils.write(dir.getAbsolutePath() + File.separator + "info.txt", info);
	}
}
