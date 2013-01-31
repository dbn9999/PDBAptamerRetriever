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

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class Ligand {
	private String pdbId = null;
	private String chemicalId = null;
	private String type = null;
	private Double molecularWeight = 0.0;
	private String chemicalName = null;
	private String formula = null;
	private String inchiKey = null;
	private String inchi = null;
	private String smiles = null;

	/**
	 * @param pdbId
	 *            the pdb id
	 * @param chemicalId
	 *            the chemical identifier provided by ligandexpo
	 * @param type
	 *            the mmCIF type
	 * @param molecularWeight
	 *            molecular weigth
	 * @param chemicalName
	 *            a chemical name
	 * @param formula
	 *            the chemical formula
	 * @param inchiKey
	 *            inchikey string
	 * @param inchi
	 *            the inchi string
	 * @param smiles
	 *            a smiles string
	 */
	public Ligand(String pdbId, String chemicalId, String type,
			Double molecularWeight, String chemicalName, String formula,
			String inchiKey, String inchi, String smiles) {
		super();
		this.pdbId = pdbId;
		this.chemicalId = chemicalId;
		this.type = type;
		this.molecularWeight = molecularWeight;
		this.chemicalName = chemicalName;
		this.formula = formula;
		this.inchiKey = inchiKey;
		this.inchi = inchi;
		this.smiles = smiles;
	}
	public String getCSVHeader(){
		String b ="";
		b += "PDBID\tCHEMICAL ID\tCHEMICAL NAME\tTYPE\tMW\tFORMULA\tINCHI\tINCHIKEY\tSMILES\n";
		return b;
	}
	public String getCSVLine() {
		String b = "";
		b += getPdbId() + "\t" + getChemicalId() + "\t"
				+ getChemicalName().replaceAll(",", "") + "\t" + getType()
				+"\t"+getMolecularWeight()+"\t"+getFormula()+"\t"+getSmiles()+"\n";
		return b;
	}

	/**
	 * @return the pdbId
	 */
	public String getPdbId() {
		return pdbId;
	}

	/**
	 * @param pdbId
	 *            the pdbId to set
	 */
	private void setPdbId(String pdbId) {
		this.pdbId = pdbId;
	}

	/**
	 * @return the chemicalId
	 */
	public String getChemicalId() {
		return chemicalId;
	}

	/**
	 * @param chemicalId
	 *            the chemicalId to set
	 */
	private void setChemicalId(String chemicalId) {
		this.chemicalId = chemicalId;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	private void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the molecularWeight
	 */
	public Double getMolecularWeight() {
		return molecularWeight;
	}

	/**
	 * @param molecularWeight
	 *            the molecularWeight to set
	 */
	private void setMolecularWeight(Double molecularWeight) {
		this.molecularWeight = molecularWeight;
	}

	/**
	 * @return the chemicalName
	 */
	public String getChemicalName() {
		return chemicalName;
	}

	/**
	 * @param chemicalName
	 *            the chemicalName to set
	 */
	private void setChemicalName(String chemicalName) {
		this.chemicalName = chemicalName;
	}

	/**
	 * @return the formula
	 */
	public String getFormula() {
		return formula;
	}

	/**
	 * @param formula
	 *            the formula to set
	 */
	private void setFormula(String formula) {
		this.formula = formula;
	}

	/**
	 * @return the inchiKey
	 */
	public String getInchiKey() {
		return inchiKey;
	}

	/**
	 * @param inchiKey
	 *            the inchiKey to set
	 */
	private void setInchiKey(String inchiKey) {
		this.inchiKey = inchiKey;
	}

	/**
	 * @return the inchi
	 */
	public String getInchi() {
		return inchi;
	}

	/**
	 * @param inchi
	 *            the inchi to set
	 */
	private void setInchi(String inchi) {
		this.inchi = inchi;
	}

	/**
	 * @return the smiles
	 */
	public String getSmiles() {
		return smiles;
	}

	/**
	 * @param smiles
	 *            the smiles to set
	 */
	private void setSmiles(String smiles) {
		this.smiles = smiles;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Ligand [pdbId=" + pdbId + ", chemicalId=" + chemicalId
				+ ", type=" + type + ", molecularWeight=" + molecularWeight
				+ ", chemicalName=" + chemicalName + ", formula=" + formula
				+ ", inchiKey=" + inchiKey + ", inchi=" + inchi + ", smiles="
				+ smiles + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ligand other = (Ligand) obj;
		if (chemicalId == null) {
			if (other.chemicalId != null)
				return false;
		} else if (!chemicalId.equals(other.chemicalId))
			return false;
		if (chemicalName == null) {
			if (other.chemicalName != null)
				return false;
		} else if (!chemicalName.equals(other.chemicalName))
			return false;
		if (formula == null) {
			if (other.formula != null)
				return false;
		} else if (!formula.equals(other.formula))
			return false;
		if (inchi == null) {
			if (other.inchi != null)
				return false;
		} else if (!inchi.equals(other.inchi))
			return false;
		if (inchiKey == null) {
			if (other.inchiKey != null)
				return false;
		} else if (!inchiKey.equals(other.inchiKey))
			return false;
		if (molecularWeight == null) {
			if (other.molecularWeight != null)
				return false;
		} else if (!molecularWeight.equals(other.molecularWeight))
			return false;
		if (pdbId == null) {
			if (other.pdbId != null)
				return false;
		} else if (!pdbId.equals(other.pdbId))
			return false;
		if (smiles == null) {
			if (other.smiles != null)
				return false;
		} else if (!smiles.equals(other.smiles))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
