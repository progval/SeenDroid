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

import org.w3c.dom.Element;

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
		this.summary = this.getContentFromTag(element, "summary")
				.replaceAll(((Character) Character.toChars(0x275D)[0]).toString(), "« ") // ❝ to «
				.replaceAll(((Character) Character.toChars(0x275E)[0]).toString(), " »"); // ❞ to »
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
