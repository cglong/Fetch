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
