package com.github.progval.SeenDroid;

import org.w3c.dom.Document;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.github.progval.SeenDroid.lib.Connection;
import com.github.progval.SeenDroid.lib.MessageEmitter;
import com.github.progval.SeenDroid.lib.Query.ParserException;

public class AsyncMessageEmitter extends AsyncTask<Void, Integer, Document> {
	private Activity activity;
	private Connection connection;
	private String message;
	private ProgressDialog dialog;
	private int replyTo;
	private OnMessageSentListener onMessageSentListener;
	
	public AsyncMessageEmitter(Activity activity, Connection connection, String message) {
		this(activity, connection, message, -1);
	}
	

	public AsyncMessageEmitter(Activity activity, Connection connection, String message, int replyTo) {
		super();
		this.connection = connection;
		this.message = message;
		this.activity = activity;
		this.replyTo = replyTo;
		this.dialog = ProgressDialog.show(this.activity, "", this.activity.getString(R.string.main_sendmessage_sending), true);
	}

	protected Document doInBackground(Void... arg0) {
    	try {
    		MessageEmitter emitter = new MessageEmitter(this.connection);
    		if (this.replyTo == -1) {
    			Log.d("SeenDroid", "reply");
    			return emitter.publish(this.message);
    		}
    		else {
    			Log.d("SeenDroid", "post");
    			return emitter.publish(this.message, this.replyTo);
    		}
    		
		} catch (ParserException e) {
			e.printStackTrace();
			return null;
		}
    }

    protected void onProgressUpdate(Integer... progress) {
    }
    protected void onPostExecute(Document document) {
        this.dialog.dismiss();
        Toast.makeText(this.activity, R.string.main_sendmessage_success, Toast.LENGTH_LONG).show();
        if (this.onMessageSentListener != null) {
        	this.onMessageSentListener.onMessageSent();
        }
    }
    public void setOnMessageSentListener(OnMessageSentListener listener) {
    	this.onMessageSentListener = listener;
    }
}