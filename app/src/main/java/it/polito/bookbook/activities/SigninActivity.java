package it.polito.bookbook.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.appcompat.app.AppCompatActivity;
import it.polito.bookbook.R;

public class SigninActivity extends AppCompatActivity {

    private static final String LOG_TAG = SigninActivity.class.getSimpleName();
    private EditText editText_name;
    private EditText editText_email;
    private EditText editText_password;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        firebaseAuth = FirebaseAuth.getInstance();

        editText_name = findViewById(R.id.editText_name);
        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if(currentUser!=null){
            updateUI(currentUser);
        }
    }

    public void signin(View view){
        String email = editText_email.getText().toString();
        String password = editText_password.getText().toString();
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        updateUI(firebaseAuth.getCurrentUser());
                    }else{
                        Log.w(LOG_TAG, "createUserWithEmail:failure", task.getException());
                        Toast.makeText(SigninActivity.this, getString(R.string.signinerror), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }else{
            Toast.makeText(SigninActivity.this, getString(R.string.emptyfields), Toast.LENGTH_SHORT).show();
        }
    }
    public void login(View view){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void updateUI(FirebaseUser currentUser) {
        Intent intent = new Intent(this,ShowProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }



}
