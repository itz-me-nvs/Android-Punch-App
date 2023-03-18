package com.navas.punchapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.AdapterView;
import android.content.SharedPreferences;

public class HelloWorldActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		PunchApplication PunchApp = ((PunchApplication) getApplication());

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_hello_world);

		Spinner spinner = findViewById(R.id.my_spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.my_spinner_options,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		// dropdown list handler
		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				// Handle item selection
				String selectedOption = parent.getItemAtPosition(position).toString();

				// Setting user to preferences
				SharedPreferences preferences = getSharedPreferences(PunchApp.PREFS_NAME, MODE_PRIVATE);
				SharedPreferences.Editor editor = preferences.edit();

				editor.putString(PunchApp.USER, selectedOption);

				// Apply the changes to SharedPreferences
				editor.apply();

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// Do something when the selection disappears from the dropdown
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_hello_world, menu);
		return true;
	}
}