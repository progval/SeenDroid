/*
 * Copyright (C) 2011, Valentin Lorentz
 *
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

package org.openihs.seendroid.lib;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MessageEmitter extends Query {

	public MessageEmitter(Connection connection) {
		super(connection);
	}
	
	public Document publish(String message) throws ParserException {
		return this.publish(message, -1);
	}

	public Document publish(String message, int replyTo) throws ParserException {
		Document document;
		try {
			document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		} catch (ParserConfigurationException e) {
			throw new MessageEmitter.ParserException();
		}
		
		Element entry = document.createElement("entry");
		document.appendChild(entry);
		
		Element id = document.createElement("id");
		id.setTextContent("$id_me");
		entry.appendChild(id);
		
		Element summary = document.createElement("summary");
		summary.setTextContent(message);
		entry.appendChild(summary);
		
		Element replyto = document.createElement("thr:in-reply-to");
		if (replyTo == -1) {
			replyto.setAttribute("ref", "$inreplyto");
		}
		else {
			replyto.setAttribute("ref", String.format("message:%d", replyTo));
		}
		entry.appendChild(replyto);
		
		document.getDocumentElement().normalize();
		
		// Very long code to say "I want the XML for the document":
		String xml;
		try {
			TransformerFactory transfac = TransformerFactory.newInstance();
			Transformer trans;
			trans = transfac.newTransformer();
			StringWriter sw = new StringWriter();
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(document);
			trans.transform(source, result);
			xml = sw.toString();
		} catch (Exception e) {
			throw new MessageEmitter.ParserException();
		}

		return this.getXmlDocument(this.connection.getHttpPost("/messages", xml));
 
	}
}
