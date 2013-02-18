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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.semanticscience.PDBAptamerRetriever.lib.Ligand;
import org.semanticscience.PDBAptamerRetriever.lib.PDBRecord;
import org.semanticscience.click_runner.Click;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class PDBRecordRetriever {

	private List<String> pdbIds = null;
	private List<PDBRecord> records = null;

	public PDBRecordRetriever(List<String> aPdbids) {
		pdbIds = aPdbids;
		// load the records
		records = new ArrayList<PDBRecord>();
		Iterator<String> itr = pdbIds.iterator();
		while (itr.hasNext()) {
			String anId = itr.next();
			PDBRecord p = new PDBRecord(anId);
			records.add(p);
		}
	}

	public String getCSVString() {
		Iterator<PDBRecord> itr = records.iterator();
		String b = "";
		while (itr.hasNext()) {
			PDBRecord p = itr.next();
			b += p.getCSVLine();
		}
		return b;
	}

	public String getLigandCSVReport() {
		String rm = "";
		Iterator<PDBRecord> itr = records.iterator();
		while (itr.hasNext()) {
			PDBRecord p = itr.next();
			// get the ligands for this record
			Iterator<Ligand> litr = p.getLigands().iterator();
			while (litr.hasNext()) {
				Ligand l = litr.next();
				rm += l.getCSVLine();
			}
		}
		return rm;
	}

	private Map<Ligand, Integer> computeLigandFrequencies(List<PDBRecord> aList) {
		Map<Ligand, Integer> rm = new HashMap<Ligand, Integer>();
		Iterator<PDBRecord> itr = aList.iterator();
		while (itr.hasNext()) {
			PDBRecord pdbr = itr.next();
			List<Ligand> ligs = pdbr.getLigands();
			Iterator<Ligand> itr2 = ligs.iterator();
			while (itr2.hasNext()) {
				Ligand l = itr2.next();
				if (rm.containsKey(l)) {
					rm.put(l, rm.get(l) + 1);
				} else {
					rm.put(l, 1);
				}
			}
		}
		return rm;
	}

	public String getLigandFrequenciesCSV() {
		String rm = "";
		Map<Ligand, Integer> freqs = this
				.computeLigandFrequencies(this.records);
		for (Map.Entry<Ligand, Integer> entry : freqs.entrySet()) {
			Ligand l = entry.getKey();
			Integer count = entry.getValue();
			rm += l.getChemicalId() + "\t" + count + "\n";
		}
		return rm;
	}

	/**
	 * Execute click on a directory of PDB files
	 * 
	 * @param pdbDir
	 *            a directory with PDB files
	 * @param anOutputDirectory
	 *            a directory where the output of click will be stored
	 * @param csvSummary
	 *            the name of the output CSV file with the summary results for
	 *            running click
	 */
	public void runClick(File pdbDir, File anOutputDirectory, File csvSummary) {
		if (anOutputDirectory != null) {
			String[] ext = new String[] { "pdb" };
			Collection<File> cf = FileUtils.listFiles(pdbDir, ext, false);
			System.out.println("Running Click on " + cf.size()
					+ " pdb files...");
			System.out.println("Performing " + choose(cf.size(), 2)
					+ " comparisons with Click ...");
			try {
				Click.compareAAADirectory(pdbDir, anOutputDirectory, csvSummary);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Compute x choose y using (x!/((x-y)!*y!)
	 * @param x
	 * @param y
	 * @return the number of xCy combinations
	 */
	public double choose(int x, int y) {
		if (y < 0 || y > x)
			return 0;
		if (y > x / 2) {
			y = x - y;
		}
		double denominator = 1.0, numerator = 1.0;
		for (int i = 1; i <= y; i++) {
			denominator *= i;
			numerator *= (x + 1 - i);
		}
		return numerator / denominator;
	}
}
