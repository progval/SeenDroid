package com.github.progval.SeenDroid.lib;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class User {
	private String id, name, email;
	
	private String getContentFromTag(Element element, String tag) {
		// Shortcut
		return element.getElementsByTagName(tag).item(0).getTextContent();
	}
	
	public User(Element element) {
		this.name = this.getContentFromTag(element, "name"); // Format: "<User name> (@<id>)"
		String uri = this.getContentFromTag(element, "uri"); // Format: http://seenthis.net/people/<id>
		this.id = "@" + uri.substring(uri.lastIndexOf("/") + 1);
		this.email = this.getContentFromTag(element, "email");
	}
	public User(Node node) {
		this((Element) node);
	}
	
	public User(String id, String name, String email) {
		this.id = id;
		this.name = name;
		this.email = email;
	}
	
	public String getId() {
		return this.id;
	}
	public String getName() {
		return this.name;
	}
	public String getEmail() {
		return this.email;
	}
}
