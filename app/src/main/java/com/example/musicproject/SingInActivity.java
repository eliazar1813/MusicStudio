package com.example.musicproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SingInActivity extends AppCompatActivity {

   private Animation rotateAnimation;
   private ImageView imageView;

   private EditText email;
   private EditText passWord;

   private String Email, Password;

   private Button buttonSingIn;
   private ProgressDialog loadingProgress;

   private FirebaseAuth firebaseAuth;
   private FirebaseAuth.AuthStateListener AuthStateListener;

    private String backgroundColor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        backgroundColor = (sharedPreferences.getString("color", "none"));

        switch (backgroundColor) {

            case "Pink":
                setTheme(R.style.AppTheme1);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_sign_in);
                break;

            case "Purple":
                setTheme(R.style.AppTheme2);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_sign_in);
                break;

            case "Yellow":
                setTheme(R.style.AppTheme3);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_sign_in);
                break;

            case "Red":
                setTheme(R.style.AppTheme4);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_sign_in);
                break;

            case "Green":
                setTheme(R.style.AppTheme5);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_sign_in);
                break;


            default:
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_sign_in);
        }

        imageView = findViewById(R.id.circleImageView);
        email = findViewById(R.id.UserNameText);
        passWord = findViewById(R.id.PasswordText);
        buttonSingIn = findViewById(R.id.buttonSignIn);
        rotateAnimation();

        firebaseAuth = FirebaseAuth.getInstance();

        loadingProgress = new ProgressDialog(this);

        //Checking weather the user still online.
        AuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = SingInActivity.this.firebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){
                    Intent SignInHome = new Intent(SingInActivity.this, HomeActivity.class);
                    startActivity(SignInHome);
                }else {
                    displayToast("Please Log In");
                }
            }
        };

        // On Sign_in Click Listener To open Home Activity with Email and Password.
        buttonSingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingProgress.show();
                loadingProgress.setContentView(R.layout.progressloading);
                loadingProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                Email = email.getText().toString().trim();
                Password = passWord.getText().toString().trim();

                if (Email.isEmpty()) {
                    email.setError("Please Enter Email");
                    email.requestFocus();

                }else if(Password.isEmpty()){

                    passWord.setError("Please Enter Password");
                    passWord.requestFocus();

                }else if(!Email.isEmpty() && !Password.isEmpty()){
                    firebaseAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                loadingProgress.dismiss();
                                displayToast("Login Error, Try Again!");

                            }else{
                                loadingProgress.dismiss();
                                Intent SignInHome = new Intent(SingInActivity.this, HomeActivity.class);
                                startActivity(SignInHome);
                                displayToast("Welcome");
                            }

                        }
                    });

                }else{
                    displayToast("Error Occurred!");
                }

            }
        }); // End Of SetOnClick Listener!

    }

    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(AuthStateListener);
    }


     //Rotate Animation method
    private void rotateAnimation() {

        rotateAnimation= AnimationUtils.loadAnimation(this,R.anim.rotate);
        imageView.startAnimation(rotateAnimation);

    }

    /// Toast method
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

}
