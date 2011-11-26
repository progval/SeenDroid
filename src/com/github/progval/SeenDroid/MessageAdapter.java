package com.github.progval.SeenDroid;

import java.util.ArrayList;
import java.util.List;

import com.github.progval.SeenDroid.lib.Message;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageAdapter extends BaseAdapter {
	private Context context;
	private List<Message> items = new ArrayList<Message>();

	public MessageAdapter(Context context, List<Message> items) {
		super();
		this.context = context;
		this.items = items;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		RelativeLayout view;
		if (null == convertView) {
			view = (RelativeLayout) RelativeLayout.inflate(this.context, R.layout.message, null);
		} else {
	        view = (RelativeLayout) convertView;
		}

        TextView authorid = (TextView) view.findViewById(R.id.message_authorid);
        authorid.setText(this.items.get(position).getAuthor().getId());
        TextView title = (TextView) view.findViewById(R.id.message_title);
        title.setText(this.items.get(position).getTitle());
		TextView summary = (TextView) view.findViewById(R.id.message_summary);
		summary.setText(this.items.get(position).getSummary());
		
		return view;
	}


	@Override
	public int getCount() {
		return this.items.size();
	}


	@Override
	public Object getItem(int location) {
		return this.items.get(location);
	}


	@Override
	public long getItemId(int arg0) {
		return arg0;
	}


}