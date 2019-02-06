package it.polito.bookbook.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import it.polito.bookbook.R;

public class EditProfileActivity extends AppCompatActivity{

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_IMAGE_GALLERY = 2;
    private static final String LOG_TAG = EditProfileActivity.class.getSimpleName();
    private static final String sharedPrefFile = "it.polito.bookbook";
    private Uri profilePictureUri = null;
    private SharedPreferences mPreferences;

    @BindView(R.id.editText_name) private EditText editText_name;
    @BindView(R.id.editText_name) private EditText editText_email;
    @BindView(R.id.editText_bio) private EditText editText_bio;
    @BindView(R.id.imageView_profilePicture) private ImageView imageView_profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        String name, email, bio, picUriString;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editprofile);
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);

        name = mPreferences.getString("NAME",getResources().getString(R.string.name));
        email = mPreferences.getString("EMAIL",getResources().getString(R.string.email));
        bio = mPreferences.getString("BIO",getResources().getString(R.string.bio));
        picUriString = mPreferences.getString("PICTURE_PROFILE",null);

        editText_name.setText(name);
        editText_email.setText(email);
        editText_bio.setText(bio);

        if(picUriString!=null){
            String[] projection = {MediaStore.MediaColumns.DATA};
            profilePictureUri = Uri.parse(picUriString);
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

    private void savePreferences(String name, String email, String bio, Uri uri){
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putString("NAME",name);
        preferencesEditor.putString("EMAIL",email);
        preferencesEditor.putString("BIO",bio);
        if(uri!=null)
            preferencesEditor.putString("PICTURE_PROFILE",uri.toString());
        preferencesEditor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageView_profilePicture.setImageURI(profilePictureUri);
        }else if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK && data != null){
            profilePictureUri = data.getData();
            imageView_profilePicture.setImageURI(profilePictureUri);
        }
    }

    private void returnShowProfileActivity() {

        savePreferences(
        editText_name.getText().toString(),
        editText_email.getText().toString(),
        editText_bio.getText().toString(),
                profilePictureUri);

        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    public void takePictureFromCamera(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                String error_message = getResources().getString(R.string.camera_error_message);
                Toast.makeText(getApplicationContext(), error_message, Toast.LENGTH_SHORT).show();
                return;
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,"it.polito.bookbook.android.fileprovider",photoFile);
                profilePictureUri = photoURI;
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
    }

    public void selectPictureFromGallery(View view){
        Intent selectPictureIntent = new Intent(Intent.ACTION_PICK);
        selectPictureIntent.setType("image/*");
        startActivityForResult(selectPictureIntent, REQUEST_IMAGE_GALLERY);
    }

    private File createImageFile() throws IOException {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",java.util.Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }


}
