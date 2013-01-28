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

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class PDBRestQueryerTest {
	private static String q1;
	private static PDBRestQueryer pdbq1;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		q1 = "<orgPdbCompositeQuery version=\"1.0\">"
				+ " <queryRefinement>"
				+ "  <queryRefinementLevel>0</queryRefinementLevel>"
				+ "  <orgPdbQuery>"
				+ "    <queryType>org.pdb.query.simple.SequenceQuery</queryType>"
				+ "    <description><![CDATA[Sequence Search (Structure:Chain = 1HIV:A, Expectation Value = 10.0, Search Tool = blast)]]></description>"
				+ "    <structureId><![CDATA[1HIV]]></structureId>"
				+ "    <chainId><![CDATA[A]]></chainId>"
				+ "    <sequence><![CDATA[PQVTLWQRPLVTIKIGGQLKEALLDTGADDTVLEEMSLPGRWKPKMIGGIGGFIKVRQYDQILIEICGHKAIGTVLVGPTPVNIIGRNLLTQIGCTLNF]]></sequence>"
				+ "    <eCutOff><![CDATA[10.0]]></eCutOff>"
				+ "    <searchTool><![CDATA[blast]]></searchTool>"
				+ "  </orgPdbQuery>"
				+ " </queryRefinement>"
				+ " <queryRefinement>"
				+ "  <queryRefinementLevel>1</queryRefinementLevel>"
				+ "  <conjunctionType>and</conjunctionType>"
				+ "  <orgPdbQuery>"
				+ "    <queryType>org.pdb.query.simple.ExpTypeQuery</queryType>"
				+ "    <description><![CDATA[Experimental Method Search : Experimental Method=X-RAY]]></description>"
				+ "    <runtimeMilliseconds>1389</runtimeMilliseconds>"
				+ "    <mvStructure.expMethod.value><![CDATA[X-RAY]]></mvStructure.expMethod.value>"
				+ "  </orgPdbQuery>" +
				" </queryRefinement>" +
				"</orgPdbCompositeQuery>";
		pdbq1 = new PDBRestQueryer(q1);
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		q1 = null;
		pdbq1 = null;
	}

	@Test
	public void TestQ1() {
		List<String> pdbids = pdbq1.getPdbIds();
		Iterator<String> itr =pdbids.iterator();
		assertTrue(pdbids.size() > 10);
	}

}
