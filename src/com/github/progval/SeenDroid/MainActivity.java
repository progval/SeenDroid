package com.github.progval.SeenDroid;

import com.github.progval.SeenDroid.lib.Connection;
import com.github.progval.SeenDroid.lib.MessageFetcher;
import com.github.progval.SeenDroid.lib.Query.ParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	public static final String PREFS_NAME = "Main";
	private static SharedPreferences settings;
	private Connection connection;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        MainActivity.settings = getSharedPreferences(PREFS_NAME, 0);
        
        this.safeConnect();
    }
    
    public void safeConnect() {
        // Check if username and password are available.
        String username = settings.getString("login.username", "");
        String password = settings.getString("login.password", "");
        if (username == "" || password == "") {
        	// Ask login credentials if not available.
        	final Dialog dialog = new Dialog(this);

        	dialog.setContentView(R.layout.loginprompt);
        	dialog.setTitle(R.string.loginprompt_title);
        	
        	Button button = (Button) dialog.findViewById(R.id.loginprompt_button_connect);
        	button.setOnClickListener(
	        			new Button.OnClickListener() {
							@Override
							public void onClick(View button) {
								// User clicked the "Connect" button.
								EditText username = (EditText) dialog.findViewById(R.id.loginprompt_edittext_username);
								EditText password = (EditText) dialog.findViewById(R.id.loginprompt_edittext_password);
								SharedPreferences.Editor editor = MainActivity.settings.edit();
								editor.putString("login.username", username.getText().toString());
								editor.putString("login.password", password.getText().toString());
								editor.commit();
								dialog.dismiss(); // It will run safeConnect().
							}
	        			}
        			);
        	dialog.setOnDismissListener(
	        			new Dialog.OnDismissListener() {
							@Override
							public void onDismiss(DialogInterface dialog) {
								MainActivity.this.safeConnect();
							}
	        			}
        			);
        	dialog.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
        	dialog.show();
        }
        else {
        	// Connect immediatly if credentials are available.
        	this.connect();
        }
    }
    private void connect() {
        String username = settings.getString("login.username", "");
        String password = settings.getString("login.password", "");
        Log.d("SeenDroid", String.format("Login as %s", MainActivity.settings.getString("login.username", "")));
        this.connection = new Connection(MainActivity.settings.getString("login.username", ""),
        		MainActivity.settings.getString("login.password", ""));
        
        MessageFetcher fetcher = new MessageFetcher(this.connection);
        try {
			Log.i("SeenDroid", "-------------------------------------------------------------");
			Log.i("SeenDroid", fetcher.fetchId(20).getContent());
			Log.i("SeenDroid", "-------------------------------------------------------------");
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}