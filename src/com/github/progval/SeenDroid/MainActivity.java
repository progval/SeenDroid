package com.github.progval.SeenDroid;

import org.w3c.dom.Document;

import com.github.progval.SeenDroid.lib.Connection;
import com.github.progval.SeenDroid.lib.MessageEmitter;
import com.github.progval.SeenDroid.lib.Query.ParserException;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static final String PREFS_NAME = "Main";
	private static SharedPreferences settings;
	private Connection connection;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.main);
        MainActivity.settings = getSharedPreferences(PREFS_NAME, 0);

        this.bindUi();

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
        Log.d("SeenDroid", String.format("Login as %s", username));
        this.connection = new Connection(username, password);
    }
    
    private class OnUnavailableFeatureClickListener implements Button.OnClickListener {

		@Override
		public void onClick(View v) {
			Toast.makeText(MainActivity.this, R.string.main_unavailablefeature, Toast.LENGTH_LONG).show();
		}
    	
    }

    private void bindUi() {
        // Profile
    	((Button) this.findViewById(R.id.main_button_profile)).setOnClickListener(
    			new Button.OnClickListener() {

					@Override
					public void onClick(View button) {
						Bundle bundle = new Bundle();
						bundle.putString("username", MainActivity.this.connection.getUsername());
						Intent intent = new Intent(MainActivity.this, ShowUserActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
    			}
			);

    	// Show user
    	((Button) this.findViewById(R.id.main_button_show_user)).setOnClickListener(
	    			new Button.OnClickListener() {

						@Override
						public void onClick(View button) {
							EditText editText = (EditText) MainActivity.this.findViewById(R.id.main_edittext_goto);
							Bundle bundle = new Bundle();
							bundle.putString("username", editText.getText().toString());
							Intent intent = new Intent(MainActivity.this, ShowUserActivity.class);
							intent.putExtras(bundle);
							startActivity(intent);
						}
	    				
	    			}
    			);
    	
    	
    	// Post message
    	((Button) this.findViewById(R.id.main_button_send)).setOnClickListener(
	    			new Button.OnClickListener() {

						@Override
						public void onClick(View button) {
							MainActivity.this.post();

						}
	    				
	    			}
    			);
    	
    	// Home feed
    	((Button) this.findViewById(R.id.main_button_home_feed)).setOnClickListener(new OnUnavailableFeatureClickListener());
    	
    	// Search
    	((Button) this.findViewById(R.id.main_button_search)).setOnClickListener(new OnUnavailableFeatureClickListener());

    	// Settings
    	((Button) this.findViewById(R.id.main_button_settings)).setOnClickListener(new OnUnavailableFeatureClickListener());
    }
    
    
    private class PublishMessage extends AsyncTask<Void, Integer, Document> {
    	private Connection connection;
    	private String message;
    	private ProgressDialog dialog;
    	
    	public PublishMessage(Connection connection, String message) {
    		super();
    		this.connection = connection;
    		this.message = message;
    		this.dialog = ProgressDialog.show(MainActivity.this, "", MainActivity.this.getString(R.string.main_sendmessage_sending), true);
    	}
    	
        protected Document doInBackground(Void... arg0) {
        	try {
        		return new MessageEmitter(this.connection).publish(message);
			} catch (ParserException e) {
				e.printStackTrace();
				return null;
			}
        }

        protected void onProgressUpdate(Integer... progress) {
        }
        protected void onPostExecute(Document document) {
            this.dialog.dismiss();
            Toast.makeText(MainActivity.this, R.string.main_sendmessage_success, Toast.LENGTH_LONG).show();
        }
    }
    public void post() {
		String message = ((EditText) this.findViewById(R.id.main_edittext_post)).getText().toString();
	    
		new MainActivity.PublishMessage(this.connection, message).execute();
    }
}