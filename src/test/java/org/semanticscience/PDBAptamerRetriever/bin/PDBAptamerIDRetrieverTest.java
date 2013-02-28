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

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.util.Map;

/**
 * @author  Jose Cruz-Toledo
 *
 */
public class PDBAptamerIDRetrieverTest {
	private static PDBAptamerIDRetriever parir = null;
	private static File fastaOut = null;
	private static String tFASTA = ">DSF:A|lsakd|lksajd\nACACACACACACA\n>ASD:B|lsakdja|laskdjals\nACACACACACACA";
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		parir = new PDBAptamerIDRetriever("RNA", "X-RAY");
		fastaOut = new File(FileUtils.getTempDirectory()+"/fastaOut");
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		parir = null;
		FileUtils.forceDelete(fastaOut);
	}
/*
	@Test
	public void testingRetrieveFasta() {
		boolean b = false;
		try {
			b = parir.retrieveFasta(fastaOut, true);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		assertEquals(true, b);
	}
*/
	@Test
	public void testingSeparateFasta(){
		Map<String,String> r = parir.separateFasta(tFASTA);
		System.out.println(r);
		
	}
}
