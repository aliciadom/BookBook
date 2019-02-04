package it.polito.bookbook.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import it.polito.bookbook.R;

public class EditProfileActivity extends AppCompatActivity{

    private static final String LOG_TAG = EditProfileActivity.class.getSimpleName();
    private static final String sharedPrefFile = "it.polito.bookbook";
    private SharedPreferences mPreferences;
    private EditText
            editText_name,
            editText_email,
            editText_bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String name, email, bio;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        editText_name = findViewById(R.id.editText_name);
        editText_email = findViewById(R.id.editText_email);
        editText_bio = findViewById(R.id.editText_bio);

        name = mPreferences.getString("NAME",getResources().getString(R.string.name));
        email = mPreferences.getString("EMAIL",getResources().getString(R.string.email));
        bio = mPreferences.getString("BIO",getResources().getString(R.string.bio));

        editText_name.setText(name);
        editText_email.setText(email);
        editText_bio.setText(bio);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_editactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "actionBar icon selected!");
        returnShowProfileActivity();
        return true;
    }

    private void savePreferences(String name, String email, String bio){
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("NAME",name);
        preferencesEditor.putString("EMAIL",email);
        preferencesEditor.putString("BIO",bio);
        preferencesEditor.apply();
    }

    private void returnShowProfileActivity() {

        savePreferences(
        editText_name.getText().toString(),
        editText_email.getText().toString(),
        editText_bio.getText().toString());

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

}
