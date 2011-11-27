package com.github.progval.SeenDroid;

import java.util.ArrayList;

import com.github.progval.SeenDroid.lib.Connection;
import com.github.progval.SeenDroid.lib.Message;
import com.github.progval.SeenDroid.lib.MessageFetcher;
import com.github.progval.SeenDroid.lib.Query.ParserException;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

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
        
        
        new FetchMessages(this, this.connection, this.showUser).execute();
    }
    
    private void bindUi() {
        this.adapter = new MessageAdapter(this, this.listMessages);
		this.setListAdapter(adapter);
		
		// TODO Bind buttons
    }
    
    public void setMessages(ArrayList<Message> messages) {
    	this.listMessages = messages;
        
        this.bindUi();
    }
    
    private class FetchMessages extends AsyncTask<Void, Integer, ArrayList<Message>> {
    	private ShowUserActivity activity;
    	private Connection connection;
    	private String username;
    	private ProgressDialog dialog;
    	
    	public FetchMessages(ShowUserActivity activity, Connection connection, String username) {
    		super();
    		this.activity = activity;
    		this.connection = connection;
    		this.username = username;
    		this.dialog =  ProgressDialog.show(ShowUserActivity.this, "", ShowUserActivity.this.getString(R.string.showuser_pleasewait), true);
    	}
    	
        protected ArrayList<Message> doInBackground(Void... arg0) {
        	ArrayList<Message> result;
        	Log.d("SeenDroid", this.connection.toString());
        	Log.d("SeenDroid", this.username);
        	try {
				result = new MessageFetcher(this.connection).fetchUser(this.username);
			} catch (ParserException e) {
				result = new ArrayList<Message>();
				e.printStackTrace();
			}
        	return result;
        }

        protected void onProgressUpdate(Integer... progress) {
            
        }

        protected void onPostExecute(ArrayList<Message> result) {
            this.activity.setMessages(result);
            this.dialog.dismiss();
        }
    }
}
