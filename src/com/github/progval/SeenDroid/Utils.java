package com.github.progval.SeenDroid;

import com.github.progval.SeenDroid.lib.Message;

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
	}
}
