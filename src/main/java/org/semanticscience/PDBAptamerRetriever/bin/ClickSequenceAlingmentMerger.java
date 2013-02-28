/**
 * Copyright (c) 2013  Jose Cruz-Toledo
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.semanticscience.PDBAptamerRetriever.bin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

/**
 * Merge the results from running Click and a pairwise all against all sequence alignment 
 * @author  Jose Cruz-Toledo
 *
 */
public class ClickSequenceAlingmentMerger {

	public ClickSequenceAlingmentMerger(File clickCsv, File pairwiseAAACsv, File mergedCSV) throws IOException{
		/**read in clickCSV into map<String, String> where the key is the pairing id and the value is rmsd and so**/
		Map<String, String> clickMap = computeClickMap(clickCsv);
		Map<String, String> pairWiseMap = computePairwseMap(pairwiseAAACsv);
		//now merge the two maps
		Map<String, String> merged = mergeMaps(clickMap, pairWiseMap);
		
		String csv_str = makeCSVString(merged);
		FileUtils.write(mergedCSV, csv_str);
	}

	/**
	 * @param merged
	 * @return
	 */
	private String makeCSVString(Map<String, String> merged) {
		String rm = null;
		//add the header
		rm = "PDBID-PAIR,RMSD,SO,LENGTH,GAPS,IDENTITY,SCORE,SIMILARITY\n";
		for (String pId : merged.keySet()) {
			rm += pId+","+merged.get(pId)+"\n";
		}
		return rm;
	}

	/**
	 * @param clickMap
	 * @param pairWiseMap
	 * @return
	 */
	private Map<String, String> mergeMaps(Map<String, String> clickMap,
			Map<String, String> pairWiseMap) {
		Map<String, String> rm = new HashMap<String, String>();
		for (String anId : pairWiseMap.keySet()) {
			String[] r = anId.split("-");
			if(r.length>1){
				String r_id = r[1]+"-"+r[0];
				if(clickMap.containsKey(anId)){
					String cstr = clickMap.get(anId);
					String pwstr = pairWiseMap.get(anId);
					rm.put(anId, cstr+","+pwstr);
				}
				if(clickMap.containsKey(r_id)){
					String cstr = clickMap.get(r_id);
					String pwstr = pairWiseMap.get(anId);
					rm.put(anId, cstr+","+pwstr);
				}
			} 
		}
		return rm;
	}

	/**
	 * @param pairwiseAAACsv
	 * @return
	 */
	private Map<String, String> computePairwseMap(File pairwiseAAACsv) {
		try {
			Map<String, String> rm = new HashMap<String, String>();
			List<String> pl = FileUtils.readLines(pairwiseAAACsv);
			for (String aLine : pl) {
				if(!aLine.startsWith("PAIR")){
					String[] s = aLine.split(",");
					String id = s[0];
					String len = s[1];
					String gaps = s[2];
					String ident = s[3];
					String score = s[4];
					String sim = s[5];
					rm.put(id, len+","+gaps+","+ident+","+score+","+sim);
				}
			}
			return rm;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * @param clickCsv
	 * @return
	 */
	private Map<String, String> computeClickMap(File clickCsv) {
		//read in the file
		try {
			Map<String, String> rm = new HashMap<String, String>();
			List<String> cl = FileUtils.readLines(clickCsv);
			for (String aLine : cl) {
				if(!aLine.startsWith("NAME")){
					String[] s = aLine.split(",");
					String id = s[0];
					String rmsd = s[1];
					String so = s[2];
					rm.put(id, rmsd+","+so);
				}
			}
			return rm;
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

}
