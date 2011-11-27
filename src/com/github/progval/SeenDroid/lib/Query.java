package com.github.progval.SeenDroid.lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;
import org.w3c.dom.Document;

public abstract class Query {
	public class ParserException extends Exception {
		private static final long serialVersionUID = 1681739776018219945L;
	}
	
	protected Connection connection;
	
	public Query(Connection connection) {
		this.connection = connection;
	}
	public String convertStreamToString(InputStream is)
            throws IOException {
        /*
         * To convert the InputStream to String we use the
         * Reader.read(char[] buffer) method. We iterate until the
         * Reader return -1 which means there's no more data to
         * read. We use the StringWriter class to produce the string.
         */
        if (is != null) {
            Writer writer = new StringWriter();

            char[] buffer = new char[1024];
            try {
                Reader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF-8"));
                int n;
                while ((n = reader.read(buffer)) != -1) {
                    writer.write(buffer, 0, n);
                }
            } finally {
                is.close();
            }
            return writer.toString();
        } else {        
            return "";
        }
    }
	
	protected Document getXmlDocument(String uri) throws ParserException {
	    return this.getXmlDocument(this.connection.getHttpGet(uri));
	}
	protected Document getXmlDocument(HttpRequestBase request) throws ParserException {
		HttpResponse response;
		try {
			response = this.connection.query(request);
	        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			return builder.parse(response.getEntity().getContent());
		} catch (Exception e) {
			e.printStackTrace();
			throw new Query.ParserException();
		}
	}
}
