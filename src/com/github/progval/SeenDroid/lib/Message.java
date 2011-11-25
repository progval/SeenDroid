package com.github.progval.SeenDroid.lib;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import android.util.Log;

public class Message {
	private User author;
	private int id;
	private String title, summary, content, published, updated;
	
	private String getContentFromTag(Element element, String tag) {
		// Shortcut
		try {
			return element.getElementsByTagName(tag).item(0).getTextContent();
		}
		catch (NullPointerException e) {
			Log.w("SeenDroid", String.format("Unable to find tag '%s' in element '%s'", tag, element.getTagName()));
			Log.w("SeenDroid", ((Element) element.getParentNode()).getTagName());
			throw e;
		}
	}
	
	public Message(Element element) {
		this.id = Integer.parseInt(this.getContentFromTag(element, "id") // Format: message:<id>
				.split(":")[1]);
		this.title = this.getContentFromTag(element, "title");
		this.author = new User(element.getElementsByTagName("author").item(0));
		this.summary = this.getContentFromTag(element, "summary");
		this.content = this.getContentFromTag(element, "content");
		this.published = this.getContentFromTag(element, "published");
		this.updated = this.getContentFromTag(element, "updated");
	}

	public Message(User author, String title, String content) {
		this.author = author;
		this.title = title;
		this.content = content;
	}
	
	public int getId() {
		return this.id;
	}
	public String getTitle() {
		return this.title;
	}
	public User getAuthor() {
		return this.author;
	}
	public String getSummary() {
		return this.summary;
	}
	public String getContent() {
		return this.content;
	}
	public String getPlublished() {
		return this.published;
	}
	public String getUpdated() {
		return this.updated;
	}
}
