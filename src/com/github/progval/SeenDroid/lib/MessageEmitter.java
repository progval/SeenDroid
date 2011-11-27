package com.github.progval.SeenDroid.lib;

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
