package com.github.progval.SeenDroid.lib;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import android.util.Log;

public class MessageFetcher extends Query {
	public MessageFetcher(Connection connection) {
		super(connection);
		
	}
	public Message fetchId(int id) throws ParserException {
		Document document = this.getXmlDocument(String.format("/messages/%d", id));
		return new Message(document.getDocumentElement());
	}
	private ArrayList<Message> multipleFetch(String uri) throws ParserException {
		Document document = this.getXmlDocument(uri);
		ArrayList<Message> messages = new ArrayList<Message>();
		
		Element rootElement = document.getDocumentElement();
		Node node = rootElement.getFirstChild();
		Element element;
		while (node != null) {
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				element = (Element) node;
				if (element.getTagName().equals("entry")) {
					Log.d("SeenDroid", "Parsing entry.");
					messages.add(new Message(element));
				}
				else {
					Log.d("SeenDroid", String.format("Skipping '%s', not an 'entry'.", element.getTagName()));
				}
			}
			node = node.getNextSibling();
		}
		Log.d("SeenDroid", "All entries parsed.");
		
		return messages;
	}
	public ArrayList<Message> fetchHome() throws ParserException {
		return this.multipleFetch("/messages/");
	}
	public ArrayList<Message> fetchUser(String username) throws ParserException {
		return this.multipleFetch(String.format("/people/%s/messages", username));
	}
}
