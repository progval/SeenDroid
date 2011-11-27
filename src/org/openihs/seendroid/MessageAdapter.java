package org.openihs.seendroid;

import java.util.ArrayList;
import java.util.List;

import org.openihs.seendroid.lib.Message;

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

		Utils.populateMessageView(view, this.items.get(position));

		
		return view;
	}


	@Override
	public int getCount() {
		return this.items.size();
	}


	@Override
	public Message getItem(int location) {
		return this.items.get(location);
	}


	@Override
	public long getItemId(int arg0) {
		return arg0;
	}


}