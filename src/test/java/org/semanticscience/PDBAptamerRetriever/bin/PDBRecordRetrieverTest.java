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

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.semanticscience.PDBAptamerRetriever.lib.Ligand;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class PDBRecordRetrieverTest {
	private static PDBRecordRetriever prr;
	private static PDBAptamerIDRetriever par;
	private static String anAptamerType = "RNA";
	private static String anExpMethod = "X-RAY";
	

	@BeforeClass
	public static void setupBeforeClass(){
		par = new PDBAptamerIDRetriever(anAptamerType, anExpMethod);
		prr = new PDBRecordRetriever(par.getPdbids());
	}
	@AfterClass
	public static void afterTest(){
		prr = null;
		par = null;
	}

	@Test
	public void testingGetLigandFreqs() {
		assertNotNull(prr.getLigandFrequenciesCSV());
	}
	
	@Test
	public void testingChoose(){
		Double d = prr.choose(56, 2);
		Double e = 1540.0;
		assertEquals(e, d);
	}

}
