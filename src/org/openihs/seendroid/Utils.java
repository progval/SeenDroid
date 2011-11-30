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

package org.openihs.seendroid;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openihs.seendroid.lib.Message;

import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Utils {
	static public void populateMessageView(RelativeLayout view, Message message) {
		TextView authorid = (TextView) view.findViewById(R.id.message_authorid);
        authorid.setText(message.getAuthor().getId());
        TextView title = (TextView) view.findViewById(R.id.message_title);
        title.setText(message.getTitle());
		TextView summary = (TextView) view.findViewById(R.id.message_summary);
		summary.setText(message.getSummary());
		Utils.linkify(summary);
	}
	
	public static final Pattern tagPattern = Pattern.compile("#([^ ]+)");
	public static final Uri tagUri = Uri.parse("http://seenthis.net/spip.php?page=recherche&recherche=%23"); // %23 is the # character
	public static final TransformFilter tagFilter = new TransformFilter() {
		@Override
		public String transformUrl(Matcher match, String url) {
            try {
				return URLEncoder.encode(match.group(1), "utf-8");
			} catch (UnsupportedEncodingException e) {
				// UTF8 IZ A SUPORT'D ENCODIN' NOOB
				return null;
			}
		}
    };
	public static final Pattern userPattern = Pattern.compile("@([^ ]+)");
	public static final Uri userUri = Uri.parse("http://seenthis.net/people/");
	public static final TransformFilter userFilter = new TransformFilter() {
		@Override
		public String transformUrl(Matcher match, String url) {
            try {
				return URLEncoder.encode(match.group(1), "utf-8");
			} catch (UnsupportedEncodingException e) {
				return null;
			}
		}
    };
	static public void linkify(TextView view) {
		Linkify.addLinks(view, Linkify.ALL);
		Linkify.addLinks(view, Utils.tagPattern, Utils.tagUri.toString(), null, Utils.tagFilter);
		Linkify.addLinks(view, Utils.userPattern, Utils.userUri.toString(), null, Utils.userFilter);
	}
}
