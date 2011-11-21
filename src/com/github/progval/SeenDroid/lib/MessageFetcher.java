package com.github.progval.SeenDroid.lib;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import android.util.Log;

public class MessageFetcher extends Query {
	public MessageFetcher(Connection connection) {
		super(connection);
		
	}
	public Message fetchId(int id) throws ParserException {
		Document document = this.getXmlDocument(String.format("/messages/%d", id));
		return new Message(document.getDocumentElement());
	}
}
