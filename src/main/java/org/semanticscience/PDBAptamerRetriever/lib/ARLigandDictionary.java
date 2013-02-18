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
package org.semanticscience.PDBAptamerRetriever.lib;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

/**
 * An Aptamer/Riboswitch Ligand Dictionary
 * 
 * @author Jose Cruz-Toledo
 * 
 */
public class ARLigandDictionary {
	private final String dic_fn = "ligands.csv";
	// see http://ligand-expo.rcsb.org/dictionaries/cc-to-pdb.tdd
	private final String pdb_map = "cc-to-pdb.tdd";
	/**
	 * A list of Ligands as described in ligands.csv
	 */
	private List<Ligand> ligands;
	/**
	 * A Map where the key is a Ligand Expo Id for a ligand from ligands.csv and
	 * the value is a list of PDBids where that ligand is found
	 */
	private Map<String, List<String>> ligToPdb = null;

	/**
	 * Load the Aptamer Riboswitch Ligand Dictionary directly from a file
	 * 
	 * @param aFile
	 *            A dictionary
	 */
	public ARLigandDictionary() {
		InputStream is = ARLigandDictionary.class.getClassLoader()
				.getResourceAsStream(dic_fn);
		InputStream is2 = ARLigandDictionary.class.getClassLoader()
				.getResourceAsStream(pdb_map);
		ligands = retrieveLigands(is);
		ligToPdb = retrieveLig2PDBMap(is2);
		// now add the list of pdbids to our ligands
		processLig2PDBMap(ligToPdb);
	}

	/**
	 * Retrieves the list of Ligands found in ligands.csv that are associated
	 * with the input parameter
	 * 
	 * @param aPdbId
	 * @return
	 */
	public List<Ligand> findInDictionary(String aPdbId) {
		if(aPdbId.length() > 0){
			List<Ligand> rm = new ArrayList<Ligand>();
			for (Ligand l : this.ligands) {
				if(l.getPdbIds().contains(aPdbId.toLowerCase())){
					rm.add(l);
				}
			}
			return rm;
		}
		return null;
	}

	/**
	 * This method adds to the ligands parsed from "ligands.csv" the
	 * corresponding PDBids from cc-to-pdb.tdd
	 * 
	 * @param ligToPdb2
	 *            a ligandexpo id to List<PDBIds> map
	 * 
	 */
	private void processLig2PDBMap(Map<String, List<String>> ligToPdb2) {

		for (String aLigExpo : ligToPdb2.keySet()) {
			List<String> al = ligToPdb2.get(aLigExpo);
			if (this.containsLigand(aLigExpo)) {
				// then add al to that ligand

				int anIndx = this.getIndexOfLigand(aLigExpo);
				if (anIndx != -1) {
					// now iterate over the list
					Iterator<String> itr = al.iterator();
					while (itr.hasNext()) {
						this.ligands.get(anIndx).addPdbId(itr.next());
					}
				}
			}
		}
	}

	/**
	 * Reads the ligands from the dictionary (ligands.csv) and finds all
	 * corresponding PDBids where the ligand is found. As specified by
	 * "cc-to-pdb.tdd"
	 * 
	 * @param InputStream
	 *            from the ligands dictionary spreadsheet
	 * @return a map where the key is a LigandExpo id and the valuie is a list
	 *         of PDBids where key exists in structure
	 */
	private Map<String, List<String>> retrieveLig2PDBMap(InputStream is2) {
		Map<String, List<String>> rm = new HashMap<String, List<String>>();
		BufferedReader br = new BufferedReader(new InputStreamReader(is2));
		if (is2 != null) {
			String str = null;
			try {
				while ((str = br.readLine()) != null && str.trim().length() > 0) {
					String[] t = str.split("\\t");
					String ligId = t[0].trim();
					if (ligId.length() > 0) {
						if (this.containsLigand(ligId)) {
							List<String> pdbIds = Arrays.asList(t[1]
									.split("\\s"));
							rm.put(ligId, pdbIds);
						}
					}
				}// while
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return rm;
	}

	/**
	 * @param aFile
	 * @return
	 */
	private List<Ligand> retrieveLigands(InputStream is) {
		List<Ligand> rm = new ArrayList<Ligand>();
		try {
			if (is != null) {
				CSVReader reader = new CSVReader(new InputStreamReader(is));
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					// skip the header line
					if (!nextLine[0].equals("Chemical Id")) {
						String cId = nextLine[0];
						String cN = nextLine[1];
						String type = nextLine[2];
						Double mw = Double.parseDouble(nextLine[3]);
						String formula = nextLine[4];
						String chebi = nextLine[5];
						String isLigand = nextLine[6];
						String isIon = nextLine[8];
						// check isLigand
						if (isLigand.equalsIgnoreCase("y")) {
							Ligand l = new Ligand(cId, type, mw, cN, formula,
									"", "", "", chebi);
							if (isIon.equals("*")) {
								l.setIsION(true);
							}
							rm.add(l);
						}
					}// if
				}// while
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rm;
	}

	/**
	 * Searches dictionary by Ligand Expo
	 * 
	 * @param aLigExpo
	 *            a valid 3 letter code Ligand expo identifier
	 * @return true if found in this object's list of Ligands
	 */
	public boolean containsLigand(String aLigExpo) {
		if (this.ligands != null) {
			for (Ligand aLigand : this.ligands) {
				if (aLigand.getChemicalId().equalsIgnoreCase(aLigExpo)) {
					return true;
				}
			}
		}
		return false;
	}

	private int getIndexOfLigand(String aLigandExpo) {
		if (this.ligands != null) {
			for (Ligand aLigand : this.ligands) {
				if (aLigand.getChemicalId().equalsIgnoreCase(aLigandExpo)) {
					return ligands.indexOf(aLigand);
				}
			}
		}
		return -1;
	}

	/**
	 * Retrieve a Ligand from this local dictionary using a LigandExpo Id
	 * 
	 * @param aLigExpoId
	 *            a valid 3 letter code Ligand expo identifier
	 * @return
	 */
	public Ligand getLigandByLigandExpoId(String aLigExpoId) {
		if (this.ligands != null) {
			for (Ligand aLigand : this.ligands) {
				if (aLigand.getChemicalId().equalsIgnoreCase(aLigExpoId)) {
					return aLigand;
				}
			}
		}
		return null;
	}

	/**
	 * Returns a list of Ligand entries marked with "Y" on column 7 of the
	 * ARLigandDictionary
	 * 
	 * @return a list of ligands
	 */
	public List<Ligand> getLigands() {
		return this.ligands;
	}

}
