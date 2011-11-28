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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ThreadReplyActivity extends Activity implements OnMessageSentListener {
	private static SharedPreferences settings;
	private Connection connection;
	private int originId;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threadreply);
        ThreadReplyActivity.settings = PreferenceManager.getDefaultSharedPreferences(this);
        String username = settings.getString("login.username", "");
        String password = settings.getString("login.password", "");
        this.connection = new Connection(username, password);
        this.setTitle(R.string.threadreply_title);
        
        Bundle extras = getIntent().getExtras(); 
        this.originId = extras.getInt("origin");
        Log.d("SeenDroid", String.format("Reply to %d", this.originId));
        
        this.bindUi();
    }
    
    private void bindUi() {
    	Button reply = (Button) this.findViewById(R.id.threadreply_button_reply);
    	reply.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View button) {
				ThreadReplyActivity.this.post();
			}
    	});
    	Button dismiss = (Button) this.findViewById(R.id.threadreply_button_dismiss);
    	dismiss.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View button) {
				ThreadReplyActivity.this.finish();
			}
    	});
    }
    public void post() {
		String message = ((EditText) this.findViewById(R.id.threadreply_edittext)).getText().toString();
	    
		AsyncMessageEmitter emitter = new AsyncMessageEmitter(this, this.connection, message, this.originId);
		emitter.setOnMessageSentListener(this);
		emitter.execute();
    }
    
    public void onMessageSent() {
    	this.finish();
    }
}
