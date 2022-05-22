package plagdetect;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class PlagiarismDetector implements IPlagiarismDetector {
	int N;
	Map<String, Set<String>> nGramMap = new HashMap<>();
	Map<String, Map<String, Integer>> commonMap = new HashMap<>();

	public PlagiarismDetector(int n) {
		// TODO implement this method
		this.N = n;
	}

	@Override
	public int getN() {
		// TODO Auto-generated method stub
		return N;
	}

	@Override
	public Collection<String> getFilenames() {
		// TODO Auto-generated method stub
		Set<String> fileName = new HashSet<>();
		fileName = nGramMap.keySet();
		return fileName;
	}

	@Override
	public Collection<String> getNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		return nGramMap.get(filename);
	}

	@Override
	public int getNumNgramsInFile(String filename) {
		// TODO Auto-generated method stub
		return nGramMap.get(filename).size();
	}

	@Override
	public Map<String, Map<String, Integer>> getResults() {
		// TODO Auto-generated method stub
		return commonMap;
	}

	@Override
	public void readFile(File file) throws IOException {
		// TODO Auto-generated method stub
		// most of your work can happen in this method

		Set<String> nGram = new HashSet<>();
		Scanner scan = new Scanner(file);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			String[] word = line.split(" ");
			String ngram = "";

			if (word.length >= N) {
				for (int i = 0; i <= word.length - N; i++) {

					for (int j = 0; j < N; j++) {
						ngram = ngram + word[i + j] + " ";
					}
					nGram.add(ngram.substring(0, ngram.length() - 1));
				}
			}
			nGramMap.put(file.getName(), nGram);
		}

		Map<String, Integer> commonNGram = new HashMap<>();
		for (String file2 : getFilenames()) {
			int numCommon = 0;
			for (String gram : nGramMap.get(file.getName())) {
				if (nGramMap.get(file2).contains(gram)) {
					numCommon = numCommon + 1;
				}
			}
			commonNGram.put(file2, numCommon);
		}
		commonMap.put(file.getName(), commonNGram);
	}


	@Override
	public int getNumNGramsInCommon(String file1, String file2) {
		// TODO Auto-generated method stub
		if (!commonMap.get(file1).containsKey(file2)) {
			if (commonMap.get(file2).containsKey(file1)) {
				return commonMap.get(file2).get(file1);
			} else {
				return 0;
			}
		} else {
			return commonMap.get(file1).get(file2);
		}

	}

	@Override
	public Collection<String> getSuspiciousPairs(int minNgrams) {
//		 TODO Auto-generated method stub
		Set<String> pairs = new HashSet<>();
		String newPair = "";
		for (String file1 : getFilenames()) {
			for (String file2 : getFilenames()) {
				if (getNumNGramsInCommon(file1, file2) >= minNgrams) {
					if (file1.compareTo(file2) < 0) {
						newPair = file1 + " " + file2 + " " + getNumNGramsInCommon(file1, file2);
					} else if (file1.compareTo(file2) > 0) {
						newPair = file2 + " " + file1 + " " + getNumNGramsInCommon(file1, file2);
					}
//					pairs.add(newPair);
				}
				if (!pairs.contains(newPair)){
					pairs.add(newPair);
				}
			}
		}
		return pairs;
	}

	@Override
	public void readFilesInDirectory(File dir) throws IOException {
		// delegation!
		// just go through each file in the directory, and delegate
		// to the method for reading a file
		for (File f : dir.listFiles()) {
			readFile(f);
		}
	}
}
