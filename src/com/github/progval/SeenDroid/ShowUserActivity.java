package com.github.progval.SeenDroid;

import java.util.ArrayList;
import java.util.List;

import com.github.progval.SeenDroid.lib.Connection;
import com.github.progval.SeenDroid.lib.Message;
import com.github.progval.SeenDroid.lib.MessageFetcher;
import com.github.progval.SeenDroid.lib.Query.ParserException;

import android.app.Activity;
import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;

public class ShowUserActivity extends ListActivity {
	public static final String PREFS_NAME = "Main";
	private static SharedPreferences settings;
	private Connection connection;
	private String showUser;
	
	public ArrayList<Message> listMessages = new ArrayList<Message>();
	public MessageAdapter adapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        ShowUserActivity.settings = getSharedPreferences(PREFS_NAME, 0);
        String username = settings.getString("login.username", "");
        String password = settings.getString("login.password", "");
        this.connection = new Connection(username, password);
        this.setTitle(R.string.homefeed_title);
        ShowUserActivity.settings = getSharedPreferences(PREFS_NAME, 0);
        
        Bundle extras = getIntent().getExtras(); 
        if(extras !=null)
        {
        	this.showUser = extras.getString("username");
        }
        
        
        try {
        	this.listMessages = new MessageFetcher(this.connection).fetchUser(this.showUser);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        this.bindUi();
    }
    
    private void bindUi() {
        this.adapter = new MessageAdapter(this, this.listMessages);
		this.setListAdapter(adapter);
		
		// TODO Bind buttons
    }
}
