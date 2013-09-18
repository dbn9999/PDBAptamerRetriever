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

import java.util.ArrayList;
import java.util.List;

import org.semanticscience.PDBAptamerRetriever.shared.URLReader;

/**
 * This describes the rows the table of verified ligands found in aptamers or
 * riboswitches as published here: https://docs.google.com/spreadsheet/ccc?key=0
 * AnGgKfZdJasrdC00bUxHcVRXaFloSnJYb3VmYkwyVnc#gid=0
 * 
 * @author Jose Cruz-Toledo
 * 
 */
public class VerifiedLigand {
	// The three letter LigandExpo id
	private String chemicalId = null;
	private String name = null;
	private String type = null;
	private Double molecularWeight = -1.0;
	private String formula = null;
	private String chebiId = null;
	private String isLigandCheck = null; // one of: Y|N|?
	private boolean ionFlag = false;

	private static final String scheme = "https";
	private static final String host = "docs.google.com";
	private static final String path = "/spreadsheet/pub";
	private static final String query = "key=0AnGgKfZdJasrdC00bUxHcVRXaFloSnJYb3VmYkwyVnc&single=true&gid=0&output=csv";

	/**
	 * @param chemicalId
	 * @param name
	 * @param type
	 * @param molecularWeight
	 * @param formula
	 * @param chebiId
	 * @param isLigandCheck
	 * @param ionFlag
	 */
	private VerifiedLigand(String chemicalId, String name, String type,
			Double molecularWeight, String formula, String chebiId,
			String isLigandCheck, boolean ionFlag) {
		super();
		this.chemicalId = chemicalId;
		this.name = name;
		this.type = type;
		this.molecularWeight = molecularWeight;
		this.formula = formula;
		this.chebiId = chebiId;
		this.isLigandCheck = isLigandCheck;
		this.ionFlag = ionFlag;
	}

	public static List<VerifiedLigand> getListOfLigands() {
		List<VerifiedLigand> rm = new ArrayList<VerifiedLigand>();
		// get the contents of the google docs
		URLReader ur = new URLReader(VerifiedLigand.scheme,
				VerifiedLigand.host, VerifiedLigand.path, VerifiedLigand.query);
		String tmpTable = ur.getContents();
		String[] lines = tmpTable.split("\\n");
		// skip header
		for (int i = 1; i < lines.length; i++) {
			String aLine = lines[i];
			String[] fields = aLine.split("\\,");
			String chemId = fields[0];
			String name = fields[1];
			String type = fields[2];
			Double weight = Double.parseDouble(fields[3]);
			String formula = fields[4];
			String chebiId = fields[5];
			String isLigand = fields[6];
			String isIonStr = null;
			try {
				isIonStr = fields[8];
			} catch (IndexOutOfBoundsException e) {
				isIonStr = "";
			}
			boolean isIon = false;
			if (isIonStr.equalsIgnoreCase("*")) {
				isIon = true;
			}
			VerifiedLigand vl = new VerifiedLigand(chemId, name, type, weight,
					formula, chebiId, isLigand, isIon);
			rm.add(vl);
		}
		return rm;
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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	private void setName(String name) {
		this.name = name;
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
	 * @return the chebiId
	 */
	public String getChebiId() {
		return chebiId;
	}

	/**
	 * @param chebiId
	 *            the chebiId to set
	 */
	private void setChebiId(String chebiId) {
		this.chebiId = chebiId;
	}

	/**
	 * @return the isLigandCheck
	 */
	public String getIsLigandCheck() {
		return isLigandCheck;
	}

	/**
	 * @param isLigandCheck
	 *            the isLigandCheck to set
	 */
	private void setIsLigandCheck(String isLigandCheck) {
		this.isLigandCheck = isLigandCheck;
	}

	/**
	 * @return the ionFlag
	 */
	public boolean isIonFlag() {
		return ionFlag;
	}

	/**
	 * @param ionFlag
	 *            the ionFlag to set
	 */
	private void setIonFlag(boolean ionFlag) {
		this.ionFlag = ionFlag;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "VerifiedLigand [chemicalId=" + chemicalId + ", name=" + name
				+ ", type=" + type + ", molecularWeight=" + molecularWeight
				+ ", formula=" + formula + ", chebiId=" + chebiId
				+ ", isLigandCheck=" + isLigandCheck + ", ionFlag=" + ionFlag
				+ "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((chebiId == null) ? 0 : chebiId.hashCode());
		result = prime * result
				+ ((chemicalId == null) ? 0 : chemicalId.hashCode());
		result = prime * result + ((formula == null) ? 0 : formula.hashCode());
		result = prime * result + (ionFlag ? 1231 : 1237);
		result = prime * result
				+ ((isLigandCheck == null) ? 0 : isLigandCheck.hashCode());
		result = prime * result
				+ ((molecularWeight == null) ? 0 : molecularWeight.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof VerifiedLigand))
			return false;
		VerifiedLigand other = (VerifiedLigand) obj;
		if (chebiId == null) {
			if (other.chebiId != null)
				return false;
		} else if (!chebiId.equals(other.chebiId))
			return false;
		if (chemicalId == null) {
			if (other.chemicalId != null)
				return false;
		} else if (!chemicalId.equals(other.chemicalId))
			return false;
		if (formula == null) {
			if (other.formula != null)
				return false;
		} else if (!formula.equals(other.formula))
			return false;
		if (ionFlag != other.ionFlag)
			return false;
		if (isLigandCheck == null) {
			if (other.isLigandCheck != null)
				return false;
		} else if (!isLigandCheck.equals(other.isLigandCheck))
			return false;
		if (molecularWeight == null) {
			if (other.molecularWeight != null)
				return false;
		} else if (!molecularWeight.equals(other.molecularWeight))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
