package com.github.progval.SeenDroid;

import java.util.ArrayList;

import com.github.progval.SeenDroid.lib.Connection;
import com.github.progval.SeenDroid.lib.Message;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class ThreadReplyActivity extends Activity implements OnMessageSentListener {
	public static final String PREFS_NAME = "Main";
	private static SharedPreferences settings;
	private Connection connection;
	private int originId;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.threadreply);
        ThreadReplyActivity.settings = getSharedPreferences(PREFS_NAME, 0);
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
