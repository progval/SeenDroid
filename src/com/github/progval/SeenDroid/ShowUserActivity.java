package com.github.progval.SeenDroid;

import java.util.ArrayList;

import com.github.progval.SeenDroid.lib.Connection;
import com.github.progval.SeenDroid.lib.Message;
import com.github.progval.SeenDroid.lib.MessageFetcher;
import com.github.progval.SeenDroid.lib.Query.ParserException;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
		this.registerForContextMenu(this.getListView());
    }
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo) {
		if (view == this.getListView()) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
			final Message clickedMessage = this.adapter.getItem(info.position);
			menu.setHeaderTitle(clickedMessage.getTitle());
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.showuser_context, menu);
		}
	}
    public boolean onContextItemSelected(MenuItem item) {
	   Log.d("SeenDroid", String.valueOf(item.getItemId()));
	   AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
	   if (item.getItemId() == R.id.showuser_contextmenu_showthread) {
			Toast.makeText(this, R.string.main_unavailablefeature, Toast.LENGTH_LONG).show();
		   return true;
	   }
	   else if (item.getItemId() == R.id.showuser_contextmenu_reply) {
			Bundle bundle = new Bundle();
			bundle.putInt("origin", this.adapter.getItem(info.position).getId());
			Intent intent = new Intent(this, ThreadReplyActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
	   }
	   return super.onContextItemSelected(item);
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
