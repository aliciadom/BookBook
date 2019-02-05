package it.polito.bookbook.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import it.polito.bookbook.R;

public class ShowProfileActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShowProfileActivity.class.getSimpleName();
    private static final String sharedPrefFile = "it.polito.bookbook";
    static final int REQUEST_EDITPROFILE = 0;
    private SharedPreferences mPreferences;
    private TextView
            textView_name,
            textView_email,
            textView_bio;
    private ImageView imageView_profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showprofile);

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        textView_name = findViewById(R.id.textView_name);
        textView_email = findViewById(R.id.textView_email);
        textView_bio =  findViewById(R.id.textView_bio);
        imageView_profilePicture = findViewById(R.id.imageView_profilePicture);

        /*test to check new branch*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_showactivty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(LOG_TAG, "actionBar icon selected!");
        launchEditProfileActivity();
        return true;
    }

    private void launchEditProfileActivity() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivityForResult(intent,REQUEST_EDITPROFILE);
    }

    private void loadPreferences(){

        String name, email, bio, picUriString;
        name = mPreferences.getString("NAME",getResources().getString(R.string.name));
        email = mPreferences.getString("EMAIL",getResources().getString(R.string.email));
        bio = mPreferences.getString("BIO",getResources().getString(R.string.bio));
        picUriString = mPreferences.getString("PICTURE_PROFILE",null);

        textView_name.setText(name);
        textView_email.setText(email);
        textView_bio.setText(bio);

        if(picUriString != null){

            String[] projection = {MediaStore.MediaColumns.DATA};
            Uri profilePictureUri = Uri.parse(picUriString);

            Cursor cursor = getContentResolver().query(profilePictureUri, projection, null, null, null);
            if(cursor!=null){
                imageView_profilePicture.setImageURI(profilePictureUri);
                cursor.close();
            }
            else{
                /*Here we handle the error, clear the PICTURE_PROFILE preference*/
                Log.e(LOG_TAG,picUriString + " invalid Uri");
                SharedPreferences.Editor preferencesEditor = mPreferences.edit();
                preferencesEditor.putString("PICTURE_PROFILE",null);
                preferencesEditor.apply();
            }

        }
    }
}
