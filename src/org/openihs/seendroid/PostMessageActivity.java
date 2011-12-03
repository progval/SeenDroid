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

import java.util.ArrayList;

import org.openihs.seendroid.lib.Connection;
import org.openihs.seendroid.lib.Message;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class PostMessageActivity extends Activity implements OnMessageSentListener {
	private static SharedPreferences settings;
	private Connection connection;
	private int replyTo;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postmessage);
        PostMessageActivity.settings = PreferenceManager.getDefaultSharedPreferences(this);
        String username = settings.getString("login.username", "");
        String password = settings.getString("login.password", "");
        this.connection = new Connection(username, password);
        
        Bundle extras = getIntent().getExtras(); 
        String action = getIntent().getAction();
        
        // if this is from the share menu
        if (Intent.ACTION_SEND.equals(action))
        {
            if (extras.containsKey(Intent.EXTRA_TEXT)) {
            	EditText post = (EditText) this.findViewById(R.id.postmessage_edittext);
            	post.setText(extras.getString(Intent.EXTRA_TEXT));
            	post.requestFocus();
            }
        }
        
        this.replyTo = -1;
    	Log.d("SeenDroid", "test");
        if (extras != null) {
        	Log.d("SeenDroid", "not null");
        	if (extras.containsKey("replyto")) {
            	Log.d("SeenDroid", "contains");
		        this.setTitle(R.string.postmessage_title_reply);
		        this.replyTo = extras.getInt("replyto");
		        Log.d("SeenDroid", String.format("Reply to %d", this.replyTo));
        	}
        }
        if (this.replyTo == -1) {
	        this.setTitle(R.string.postmessage_title_post);
	        Log.d("SeenDroid", "New post.");
        }
        
        this.bindUi();
    }
    
    private void bindUi() {
    	Button post = (Button) this.findViewById(R.id.postmessage_button_post);
    	post.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View button) {
				PostMessageActivity.this.post();
			}
    	});
    	if (this.replyTo != -1) {
    		post.setText(R.string.postmessage_button_reply);
    	}
    	Button dismiss = (Button) this.findViewById(R.id.postmessage_button_dismiss);
    	dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View button) {
				PostMessageActivity.this.finish();
			}
    	});
    }
    public void post() {
		String message = ((EditText) this.findViewById(R.id.postmessage_edittext)).getText().toString();
	    
		AsyncMessageEmitter emitter = new AsyncMessageEmitter(this, this.connection, message, this.replyTo);
		emitter.setOnMessageSentListener(this);
		emitter.execute();
    }
    
    public void onMessageSent() {
    	this.finish();
    }
}
