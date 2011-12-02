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

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.util.Log;

public class MessageFetcher extends Query {
	public class UserDoesNotExist extends Exception {
		
	}
	
	public MessageFetcher(Connection connection) {
		super(connection);
		
	}
	public Message fetchId(int id) throws ParserException {
		Document document = this.getXmlDocument(String.format("/messages/%d", id));
		return new Message(document.getDocumentElement());
	}
	private ArrayList<Message> multipleFetch(String uri) throws ParserException, UserDoesNotExist {
		Log.d("SeenDroid", "Downloading.");
		Document document = this.getXmlDocument(uri);
		if (document.getDocumentElement().getNodeName().equals("html")) {
			throw new MessageFetcher.UserDoesNotExist();
		}
		Log.d("SeenDroid", "Starting parse.");
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
	public ArrayList<Message> fetchHome() throws ParserException, UserDoesNotExist {
		return this.multipleFetch("/messages/");
	}
	public ArrayList<Message> fetchUser(String username) throws ParserException, UserDoesNotExist {
		return this.multipleFetch(String.format("/people/%s/messages", username));
	}
}
