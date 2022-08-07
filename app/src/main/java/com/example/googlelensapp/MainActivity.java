package com.example.googlelensapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class MainActivity extends AppCompatActivity {

    private Button signup,login,googlesingnin;
    private EditText email,password;
    private FirebaseAuth auth;
    private GoogleSignInOptions gso;
    private GoogleSignInClient gsc;
    private final String TAG = "MainActivity";
    private final String default_web_client_id = "108344871131-03rh12tg4l2pol2g5odf9l3dl292efe8.apps.googleusercontent.com";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Initialize_views();
        Register_activity();
        google();
        googlesingnin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signin();
            }
        });
        auth = FirebaseAuth.getInstance();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10000)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try
            {
                task.getResult(ApiException.class);
                navigateToSecondActivity();
            }
            catch (ApiException e)
            {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void signin()
    {
        Intent googlesignintent = gsc.getSignInIntent();
        startActivityForResult(googlesignintent,10000);
    }

    protected void google()
    {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        gsc = GoogleSignIn.getClient(this,gso);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if(acct!=null)
        {
            navigateToSecondActivity();
        }
    }
    protected void Initialize_views()
    {
        signup = findViewById(R.id.Sign_button);
        email = findViewById(R.id.editTextEmailAddress);
        password = findViewById(R.id.editTextpassword);
        login = findViewById(R.id.login);
        googlesingnin = findViewById(R.id.sign_in_button);
    }
    protected void Register_activity()
    {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                Log.d(TAG,"Sign_Up Button Clicked");
                if(TextUtils.isEmpty(txt_email)||TextUtils.isEmpty(txt_password))
                    Toast.makeText(MainActivity.this,"Empty fields",Toast.LENGTH_SHORT).show();
                else if(txt_password.length()<=6)
                    Toast.makeText(MainActivity.this,"Password Too Short",Toast.LENGTH_SHORT).show();
                else
                    register_user(txt_email,txt_password);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }

    protected void register_user(String txt_email, String txt_password)
    {
        auth.createUserWithEmailAndPassword(txt_email,txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task)
            {
                if(task.isSuccessful())
                {
                    Toast.makeText(MainActivity.this,"Registration Successful",Toast.LENGTH_SHORT).show();
                    finish();
                    Toast.makeText(MainActivity.this,"Login again",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this,LoginActivity.class));
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthUserCollisionException)
                    Toast.makeText(MainActivity.this,"User Already Registerd",Toast.LENGTH_SHORT).show();
                else if(e instanceof FirebaseAuthInvalidUserException)
                    Toast.makeText(MainActivity.this,"Problem with Email",Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void navigateToSecondActivity()
    {
        finish();
        startActivity(new Intent(this, OCR_google.class));
    }
}