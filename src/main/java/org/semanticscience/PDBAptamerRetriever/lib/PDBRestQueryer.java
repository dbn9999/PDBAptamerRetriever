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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class PDBRestQueryer {
	private final String SERVICELOCATION = "http://www.rcsb.org/pdb/rest/search";
	private String xmlQuery = "";
	private List<String> pdbIds = null;

	public PDBRestQueryer() {
		pdbIds = new ArrayList<String>();
	}

	public PDBRestQueryer(String anXMLQuery) {
		xmlQuery = anXMLQuery;
		pdbIds = postQuery(xmlQuery);
	}

	private List<String> postQuery(String anXMLQuery) {
		List<String> rm = null;
		try {
			URL u = new URL(SERVICELOCATION);
			String encodedXML = URLEncoder.encode(anXMLQuery, "UTF-8");
			InputStream in = doPOST(u, encodedXML);
			rm = new ArrayList<String>();
			BufferedReader rd  = new BufferedReader(new InputStreamReader(in));
			String l;
			while((l = rd.readLine())!= null){
				rm.add(l);
			}
			rd.close();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rm;
	}
	
	private InputStream doPOST(URL u, String d){
		try {
			URLConnection conn = u.openConnection();
			conn.setDoOutput(true);
			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(d);
			wr.flush();
			return conn.getInputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	/**
	 * @return the xmlQuery
	 */
	public String getXmlQuery() {
		return xmlQuery;
	}

	/**
	 * @param xmlQuery the xmlQuery to set
	 */
	private void setXmlQuery(String xmlQuery) {
		this.xmlQuery = xmlQuery;
	}

	/**
	 * @return the pdbIds
	 */
	public List<String> getPdbIds() {
		return pdbIds;
	}

	/**
	 * @param pdbIds the pdbIds to set
	 */
	private void setPdbIds(List<String> pdbIds) {
		this.pdbIds = pdbIds;
	}

	
}
