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
package org.semanticscience.PDBAptamerRetriever.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

/**
 * @author Jose Cruz-Toledo
 * 
 */
public class URLReader {
	private String contents;
	private CookieStore cookieStore;
	private HttpContext localContext;

	public URLReader(String scheme, String host, String path, String query) {
		cookieStore = new BasicCookieStore();
		localContext = new BasicHttpContext();
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
		contents = this.getStringFromURLGET(scheme, host, path, query);

	}

	private String getStringFromURLGET(String scheme, String host, String path,
			String query) {
		String returnMe;
		try {
			URI uri = new URI(scheme, host, path, query, null);
			DefaultHttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(uri);
			try {
				HttpResponse response = client.execute(get, localContext);
				HttpEntity entity = response.getEntity();
				if (entity != null) {
					InputStream is = entity.getContent();
					String s = convertinputStreamToString(is);
					returnMe = s;
					// Do not need the rest
					get.abort();
					return returnMe;
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String convertinputStreamToString(InputStream ists)
			throws IOException {
		if (ists != null) {
			StringBuilder sb = new StringBuilder();
			String line;
			try {
				BufferedReader r1 = new BufferedReader(new InputStreamReader(
						ists, "UTF-8"));
				while ((line = r1.readLine()) != null) {
					sb.append(line).append("\n");
				}
			} finally {
				ists.close();
			}
			return sb.toString();
		} else {
			return "";
		}
	}

	/**
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	private void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * @return the cookieStore
	 */
	public CookieStore getCookieStore() {
		return cookieStore;
	}

	/**
	 * @param cookieStore the cookieStore to set
	 */
	private void setCookieStore(CookieStore cookieStore) {
		this.cookieStore = cookieStore;
	}

	/**
	 * @return the localContext
	 */
	public HttpContext getLocalContext() {
		return localContext;
	}

	/**
	 * @param localContext the localContext to set
	 */
	private void setLocalContext(HttpContext localContext) {
		this.localContext = localContext;
	}
	

}
