package com.github.cglong.fetch;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This activity sends a user-provided message to a server and displays the response to the user.
 * 
 * @author Chris Long
 */
public class MainActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	/**
	 * Asynchronously sends the string in the {@link android.widget.EditText EditText} to the server
	 * and sets the contents of the {@link android.widget.TextView TextView} to its response.
	 * 
	 * @param view The {@link android.widget.EditText EditText} containing the string to send
	 */
	public void sendMessage(View view) {
		EditText editMessage = (EditText) findViewById(R.id.edit_message);
		CharSequence message = editMessage.getText();
		new RetrieveResponseTask().execute(message);
	}
	
	private class RetrieveResponseTask extends AsyncTask<CharSequence, Void, String> {
		protected String doInBackground(CharSequence... messages) {
			String uri = getString(R.string.string_uri);
			
			try {
				Socket socket = new Socket(uri, 8080);
				PrintWriter outgoing = new PrintWriter(socket.getOutputStream(), true);
				outgoing.println(messages[0]);

				BufferedReader incoming = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String response = incoming.readLine();
				socket.close();
				return response;
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		
		protected void onPostExecute(String response) {
			TextView textResponse = (TextView) findViewById(R.id.text_response);
			textResponse.setText(response);
		}
	}
}
