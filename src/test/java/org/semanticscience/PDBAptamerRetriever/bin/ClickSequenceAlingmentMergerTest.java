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

import static org.junit.Assert.*;

import java.io.File;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class ClickSequenceAlingmentMergerTest {
	private static File clickCSV = null;
	private static File pairwiseCSV = null;
	private static File mergedCSV = null;
	private static ClickSequenceAlingmentMerger csam = null;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		clickCSV = new File("/home/jose/Documents/research/aptamers_inPDB/pdb_retriever/Feb202013/click/dna/summary.csv");
		pairwiseCSV = new File("/home/jose/Documents/research/aptamers_inPDB/pdb_retriever/Feb202013/needle/dna/avg_summary.csv");
		mergedCSV = new File("/tmp/merged_pairs.csv");
		csam = new ClickSequenceAlingmentMerger(clickCSV, pairwiseCSV, mergedCSV);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		clickCSV = null;
		mergedCSV= null;
		pairwiseCSV =null;
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
