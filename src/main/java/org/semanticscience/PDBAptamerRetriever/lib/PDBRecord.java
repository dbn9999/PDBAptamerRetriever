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

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.semanticscience.PDBAptamerRetriever.shared.URLReader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class PDBRecord {
	private final String scheme = "http";
	private final String host = "www.rcsb.org";
	private final String path = "/pdb/rest/describePDB";
	private String pdbId = null;
	private String title = null;
	private int pmid = -1;
	private String expMethod = null;
	private Double resolution = -1.0;
	private int numberOfResidues = -1;
	private int numberOfAtoms = -1;
	private List<Ligand> ligands = null;

	public PDBRecord(String aPdbid) {
		retrieveRecordInfo(aPdbid);
		ligands = retrieveLigands(aPdbid);
	}

	/**
	 * @param aPdbid
	 * @return
	 */
	private List<Ligand> retrieveLigands(String aPdbid) {
		List<Ligand> rm = new ArrayList<Ligand>();
		try {
			String p = "/pdb/rest/ligandInfo";
			String qs = "structureId=" + aPdbid;
			URLReader uread = new URLReader(scheme, host, p, qs);
			Document doc = loadXMLFromString(uread.getContents());
			doc.getDocumentElement().normalize();
			NodeList nl = doc.getElementsByTagName("ligand");
			for (int i = 0; i < nl.getLength(); i++) {
				Node n = nl.item(i);
				String chemicalId = null;
				String type = null;
				String chemicalName = null;
				String formula = null;
				String inchiKey = null;
				String inchi = null;
				String smiles = null;
				Double mw = -1.0;
				if (n.getNodeType() == Node.ELEMENT_NODE) {
					Element e = (Element) n;
					NamedNodeMap atts = e.getAttributes();
					for (int j = 0; j < atts.getLength(); j++) {
						if (atts.item(j).getNodeName().equals("chemicalID")) {
							chemicalId = atts.item(j).getNodeValue();
						}
						if (atts.item(j).getNodeName().equals("type")) {
							type = atts.item(j).getNodeValue();
						}
						if (atts.item(j).getNodeName()
								.equals("molecularWeight")) {
							mw = Double
									.parseDouble(atts.item(j).getNodeValue());
						}
					}// attributes
						// chemicalName
					chemicalName = e.getElementsByTagName("chemicalName")
							.item(0).getTextContent();
					formula = e.getElementsByTagName("formula").item(0)
							.getTextContent();
					inchiKey = e.getElementsByTagName("InChIKey").item(0)
							.getTextContent();
					inchi = e.getElementsByTagName("InChI").item(0)
							.getTextContent();
					smiles = e.getElementsByTagName("smiles").item(0)
							.getTextContent();
					Ligand l = new Ligand(this.getPdbId(), chemicalId, type,
							mw, chemicalName, formula, inchiKey, inchi, smiles);
					rm.add(l);
				}
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rm;
	}

	private void retrieveRecordInfo(String aPDBId) {
		try {
			String qs = "structureId=" + aPDBId;
			URLReader uread = new URLReader(scheme, host, path, qs);
			Document doc = loadXMLFromString(uread.getContents());
			doc.getDocumentElement().normalize();
			NodeList nl = doc.getElementsByTagName("PDB");
			if (nl.getLength() > 0) {
				for (int i = 0; i < nl.getLength(); i++) {
					Node n = nl.item(i);
					if (n.getNodeType() == Node.ELEMENT_NODE) {
						Element e = (Element) n;
						NamedNodeMap atts = e.getAttributes();
						for (int j = 0; j < atts.getLength(); j++) {

							if (atts.item(j).getNodeName()
									.equals("structureId")) {
								pdbId = atts.item(j).getNodeValue();
							}
							if (atts.item(j).getNodeName().equals("title")) {
								title = atts.item(j).getNodeValue().replaceAll(",", "");								
							}
							if (atts.item(j).getNodeName().equals("pubmedId")) {
								pmid = Integer.parseInt(atts.item(j)
										.getNodeValue());
							}
							if (atts.item(j).getNodeName().equals("expMethod")) {
								expMethod = atts.item(j).getNodeValue();
							}
							if (atts.item(j).getNodeName().equals("resolution")) {
								resolution = Double.parseDouble(atts.item(j)
										.getNodeValue());
							}
							if (atts.item(j).getNodeName()
									.equals("nr_residues")) {
								numberOfResidues = Integer.parseInt(atts
										.item(j).getNodeValue());
							}
							if (atts.item(j).getNodeName().equals("nr_atoms")) {
								numberOfAtoms = Integer.parseInt(atts.item(j)
										.getNodeValue());
							}
						}
					}
				}
			}else{
				throw new Exception("Invalid PDB ID: "+aPDBId);
			}

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public String getCSVLine() {
		String b = "";
		b += getPdbId() + ",\"" + getTitle() + "\",\"" + getExpMethod() + "\","
				+ getResolution() + "," + getNumberOfAtoms() + ","
				+ getNumberOfResidues()
				+ ",http://www.ncbi.nlm.nih.gov/pubmed/" + getPmid() + ","
				+ getLigandsCSV() + "\n";
		return b;
	}

	/**
	 * @return
	 */
	private String getLigandsCSV() {
		if (getLigands().size() > 1) {
			Iterator<Ligand> itr = getLigands().iterator();
			String b = "";
			while (itr.hasNext()) {
				Ligand l = itr.next();
				b += l.getChemicalId() + "; ";
			}
			// remove the last character
			b = b.substring(0, b.length() - 2);
			return b;
		}
		return "";
	}

	private static Document loadXMLFromString(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		InputSource is = new InputSource(new StringReader(xml));
		return builder.parse(is);
	}

	/**
	 * @return the title
	 */
	private String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	private void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the resolution
	 */
	private Double getResolution() {
		return resolution;
	}

	/**
	 * @return the ligands
	 */
	public List<Ligand> getLigands() {
		return ligands;
	}

	/**
	 * @return the pmid
	 */
	private int getPmid() {
		return pmid;
	}

	/**
	 * @return the expMethod
	 */
	private String getExpMethod() {
		return expMethod;
	}

	/**
	 * @return the numberOfResidues
	 */
	private int getNumberOfResidues() {
		return numberOfResidues;
	}

	/**
	 * @return the numberOfAtoms
	 */
	private int getNumberOfAtoms() {
		return numberOfAtoms;
	}

	public String getPdbId() {
		return pdbId;
	}

}
