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
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.semanticscience.PDBAptamerRetriever.lib.PDBRestQueryer;

/**
 * Use PDB's REST interface to retrieve PDBids for aptamers AND riboswitches
 * 
 * @author Jose Cruz-Toledo
 * 
 */
public class PDBAptamerIDRetriever {
	// The type of aptamer that will be retrieved (DNA,RNA or Both)
	private String aptamerType;
	// the pdbrequester used by this object?
	private PDBRestQueryer prq = null;
	// the experimental type (XRAY, NMR, all)
	private String experimentalType;
	// if true find structures with ligands
	private boolean hasLigands = false;
	private String keywords = "aptamer or riboswitch";
	// The PDBids
	private List<String> pdbids;
	// the directory where to place the resulting pdb files
	private File outputDirectory;
	// the filetype desired for the output (PDB, XML or FASTA)
	private String outputType;
	// the query to be posted
	private String query;

	public PDBAptamerIDRetriever(String anAptamerType, String anExpType) {
		aptamerType = anAptamerType;
		experimentalType = anExpType;
		query = constructXMLQuery(anAptamerType, anExpType);
		prq = new PDBRestQueryer(query);
		pdbids = prq.getPdbIds();
	}

	/**
	 * 
	 * @param anAptamerType
	 *            the molecule type. Accepted values: RNA | DNA | BOTH
	 * @param anExpType
	 *            the experimental method used. Accepted values: X-RAY or NMR
	 *            leave blank to not restrict
	 * @param aHasLigands
	 *            if true then the structure must have ligands in it.
	 */
	public PDBAptamerIDRetriever(String anAptamerType, String anExpType,
			boolean aHasLigands) {
		aptamerType = anAptamerType;
		experimentalType = anExpType;
		hasLigands = aHasLigands;
		query = constructXMLQuery(anAptamerType, anExpType, aHasLigands,
				keywords);
		prq = new PDBRestQueryer(query);
		pdbids = prq.getPdbIds();
	}

	public PDBAptamerIDRetriever(String anAptamerType, String anExpType,
			boolean aHasLigands, String aKeyword) {
		aptamerType = anAptamerType;
		experimentalType = anExpType;
		hasLigands = aHasLigands;
		keywords = aKeyword;
		query = constructXMLQuery(anAptamerType, anExpType, aHasLigands,
				keywords);
		prq = new PDBRestQueryer(query);
		pdbids = prq.getPdbIds();
	}

	/**
	 * Retrieve PDB files for all pdbids. Creates the downloaded PDB files
	 * inside aDirectory
	 * 
	 * @param aDirectory
	 *            the directory where the PDB files will be stored
	 * @return false if something did not work :)
	 * @throws Exception
	 *             if output directory is not empty
	 */
	public boolean retrievePDB(File aDirectory) throws Exception {
		int checkCount = 0;
		if (aDirectory.isDirectory()) {
			if (isDirEmpty(aDirectory)) {
				String base = "http://www.rcsb.org/pdb/files/";
				if (getPdbids().size() >= 1) {
					System.out.println("Retrieving " + getPdbids().size()
							+ " PDB files...");
					// iterate over the pdbids
					Iterator<String> itr = getPdbids().iterator();
					while (itr.hasNext()) {
						InputStream in = null;
						try {
							String pdbid = itr.next();
							URL u = new URL(base + pdbid + ".pdb");
							in = u.openStream();
							String pdb = IOUtils.toString(in);
							if (pdb.length() > 0) {
								// now create a file from a string
								File f = new File(aDirectory.getPath() + "/"
										+ pdbid + ".pdb");
								FileUtils.writeStringToFile(f, pdb);
								checkCount++;
							}
						} catch (MalformedURLException e) {
							e.printStackTrace();
							return false;
						} catch (IOException e) {
							e.printStackTrace();
							return false;
						} finally {
							IOUtils.closeQuietly(in);
						}
					}
				}
			} else {
				throw new Exception("PDB output directory : "
						+ aDirectory.getAbsolutePath()
						+ " is not empty!\nPlease provide an empty directory!");

			}
		} else {
			return false;
		}
		if (checkCount > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Retrieve FASTA files for all pdbids. Creates the downloaded FASTA files
	 * in aDirectory
	 * 
	 * @param aDirectory
	 *            the directory where the fasta files will be stored
	 * @param concatenate
	 *            if true only one file will be placed in aDirectory with all
	 *            fasta entries
	 * @return false if something did not work right :)
	 * @throws Exception
	 *             if output directory is not empty
	 */
	public boolean retrieveFasta(File aDirectory, boolean concatenate)
			throws Exception {
		int checkCount = 0;
		if (aDirectory.isDirectory()) {
			// check if the directory is emptry
			if (isDirEmpty(aDirectory)) {
				String base = "http://www.rcsb.org/pdb/files/fasta.txt?structureIdList=";
				if (getPdbids().size() >= 1) {
					System.out.println("Retrieving " + getPdbids().size()
							+ " FASTA files...");
					// iterate over the pdbids
					File concatOut = new File(aDirectory.getPath()
							+ "/output.fasta");
					Iterator<String> itr = getPdbids().iterator();
					while (itr.hasNext()) {
						InputStream in = null;
						try {
							String pdbid = itr.next();
							URL u = new URL(base + pdbid);
							in = u.openStream();
							String fasta = IOUtils.toString(in);
							if (fasta.length() > 0) {
								// now create a file from a string
								if (concatenate == false) {
									File f = new File(aDirectory.getPath()
											+ "/" + pdbid + ".fasta");
									FileUtils.writeStringToFile(f, fasta);
									checkCount++;
								} else {
									FileUtils.writeStringToFile(concatOut,
											fasta, true);
									checkCount++;
								}
							}
						} catch (MalformedURLException e) {
							e.printStackTrace();
							return false;
						} catch (IOException e) {
							e.printStackTrace();
							return false;
						} finally {
							IOUtils.closeQuietly(in);
						}
					}
				}
			} else {
				throw new Exception("FASTA output directory :"
						+ aDirectory.getAbsolutePath()
						+ " is not empty!\nPlease empty before continuing!");
			}
		} else {
			return false;
		}
		if (checkCount > 0) {
			return true;
		} else {
			return false;
		}

	}

	private String constructXMLQuery(String anAptamerType, String anExpType) {
		String buf = "";
		String at = makeChainTypeQuery(anAptamerType);
		String et = makeExperimentalMethodQuery(anExpType);
		String kw = makeKeywordQuery(getKeywords());
		buf += "<orgPdbCompositeQuery>";
		if (at != null) {
			buf += "<queryRefinement>";
			buf += at;
			buf += "</queryRefinement>";
		}
		if (et != null) {
			buf += "<queryRefinement>";
			buf += "<conjunctionType>and</conjunctionType>";
			buf += et;
			buf += "</queryRefinement>";
		}
		// ADD the default keyword
		if (kw != null) {
			buf += "<queryRefinement>";
			buf += "<conjunctionType>and</conjunctionType>";
			buf += kw;
			buf += "</queryRefinement>";
		}
		buf += "</orgPdbCompositeQuery>";
		return buf;
	}

	private String constructXMLQuery(String anAptamerType, String anExptype,
			boolean aHasLigands, String aKeyword) {
		String buf = "";
		String at = makeChainTypeQuery(anAptamerType);
		String et = makeExperimentalMethodQuery(anExptype);
		String ligs = makeLigandQuery(aHasLigands);
		String kw = makeKeywordQuery(aKeyword);

		buf += "<orgPdbCompositeQuery>";
		if (at != null) {
			buf += "<queryRefinement>";
			buf += at;
			buf += "</queryRefinement>";
		}
		if (et != null) {
			buf += "<queryRefinement>";
			buf += "<conjunctionType>and</conjunctionType>";
			buf += et;
			buf += "</queryRefinement>";
		}
		if (ligs != null) {
			buf += "<queryRefinement>";
			buf += "<conjunctionType>and</conjunctionType>";
			buf += ligs;
			buf += "</queryRefinement>";
		}
		if (kw != null) {
			buf += "<queryRefinement>";
			buf += "<conjunctionType>and</conjunctionType>";
			buf += kw;
			buf += "</queryRefinement>";
		}
		buf += "</orgPdbCompositeQuery>";
		return buf;
	}

	private String makeChainTypeQuery(String aType) {
		if (aType.equalsIgnoreCase("dna")) {
			return "<orgPdbQuery><queryType>org.pdb.query.simple.ChainTypeQuery</queryType><containsProtein>N</containsProtein><containsDna>?</containsDna><containsRna>N</containsRna><containsHybrid>N</containsHybrid></orgPdbQuery>";
		} else if (aType.equalsIgnoreCase("rna")) {
			return "<orgPdbQuery><queryType>org.pdb.query.simple.ChainTypeQuery</queryType><containsProtein>N</containsProtein><containsDna>N</containsDna><containsRna>?</containsRna><containsHybrid>N</containsHybrid></orgPdbQuery>";
		} else if (aType.equalsIgnoreCase("both")) {
			return "<orgPdbQuery><queryType>org.pdb.query.simple.ChainTypeQuery</queryType><containsProtein>N</containsProtein><containsDna>?</containsDna><containsRna>?</containsRna><containsHybrid>N</containsHybrid></orgPdbQuery>";
		} else {
			return null;
		}
	}

	private String makeExperimentalMethodQuery(String aMeth) {
		if (aMeth.equalsIgnoreCase("X-RAY")) {
			return "<orgPdbQuery> <queryType>org.pdb.query.simple.ExpTypeQuery</queryType><mvStructure.expMethod.value>X-RAY</mvStructure.expMethod.value> </orgPdbQuery>";
		} else if (aMeth.equalsIgnoreCase("NMR")) {
			return "<orgPdbQuery> <queryType>org.pdb.query.simple.ExpTypeQuery</queryType><mvStructure.expMethod.value>NMR</mvStructure.expMethod.value> </orgPdbQuery>";
		} else {
			return null;
		}
	}

	private String makeKeywordQuery(String aKeyWord) {
		if (aKeyWord.length() > 0) {
			return "<orgPdbQuery><queryType>org.pdb.query.simple.AdvancedKeywordQuery</queryType><keywords>"
					+ aKeyWord + "</keywords></orgPdbQuery>";
		} else {
			return null;
		}
	}

	private String makeLigandQuery(boolean ligandFlag) {
		if (ligandFlag) {
			return "<orgPdbQuery> <queryType>org.pdb.query.simple.NoLigandQuery</queryType>   <haveLigands>yes</haveLigands></orgPdbQuery>";
		} else {
			return "<orgPdbQuery> <queryType>org.pdb.query.simple.NoLigandQuery</queryType>   <haveLigands>no</haveLigands></orgPdbQuery>";
		}
	}

	/**
	 * @return the aptamerType
	 */
	public String getAptamerType() {
		return aptamerType;
	}

	/**
	 * @param aptamerType
	 *            the aptamerType to set
	 */
	private void setAptamerType(String aptamerType) {
		this.aptamerType = aptamerType;
	}

	/**
	 * @return the experimentalType
	 */
	public String getExperimentalType() {
		return experimentalType;
	}

	/**
	 * @param experimentalType
	 *            the experimentalType to set
	 */
	private void setExperimentalType(String experimentalType) {
		this.experimentalType = experimentalType;
	}

	/**
	 * @return the hasLigands
	 */
	public boolean isHasLigands() {
		return hasLigands;
	}

	/**
	 * @param hasLigands
	 *            the hasLigands to set
	 */
	private void setHasLigands(boolean hasLigands) {
		this.hasLigands = hasLigands;
	}

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keyword
	 *            the keyword to set
	 */
	private void setKeyword(String keyword) {
		this.keywords = keyword;
	}

	/**
	 * @return the pdbids
	 */
	public List<String> getPdbids() {
		return pdbids;
	}

	/**
	 * @param pdbids
	 *            the pdbids to set
	 */
	private void setPdbids(List<String> pdbids) {
		this.pdbids = pdbids;
	}

	/**
	 * @return the outputDirectory
	 */
	private File getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * @param outputDirectory
	 *            the outputDirectory to set
	 */
	public void setOutputDirectory(File outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	/**
	 * @return the outputType
	 */
	private String getOutputType() {
		return outputType;
	}

	/**
	 * @param outputType
	 *            the outputType to set
	 */
	private void setOutputType(String outputType) {
		this.outputType = outputType;
	}

	/**
	 * @return the query
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @param query
	 *            the query to set
	 */
	private void setQuery(String query) {
		this.query = query;
	}

	private boolean isDirEmpty(File aDir) {
		if (aDir.isDirectory()) {
			String[] files = aDir.list();
			if (files.length > 0) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
}
