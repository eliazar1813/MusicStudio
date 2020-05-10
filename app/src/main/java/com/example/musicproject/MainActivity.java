package com.example.musicproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private Animation rotateAnimation;
    private ImageView imageView;

    private EditText username, email, password, phoneNum;
//    private int numPhone;
    private String Email,Phone,Password,UserName;
    private Button registerButton;
    private String id;

    private FirebaseAuth.AuthStateListener AuthStateListener;

    private ProgressDialog loadingProgress;


   // public User user;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser newUser;
    private FirebaseUser current;

    private FloatingActionButton fab;

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
                setContentView(R.layout.activity_main);
                break;

            case "Purple":
                setTheme(R.style.AppTheme2);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                break;

            case "Yellow":
                setTheme(R.style.AppTheme3);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                break;

            case "Red":
                setTheme(R.style.AppTheme4);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                break;

            case "Green":
                setTheme(R.style.AppTheme5);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
                break;


            default:
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);
        }


        username = findViewById(R.id.UserNameText);

        email = findViewById(R.id.EmailText);

        password = findViewById(R.id.PasswordText);

        phoneNum = findViewById(R.id.PhoneText);

        registerButton = findViewById(R.id.buttonSignup);

        firebaseAuth = FirebaseAuth.getInstance();

        loadingProgress = new ProgressDialog(this);



        /// Image Rotation 360 degree///
        imageView= findViewById(R.id.circleImageView);
        rotateAnimation();


        ///Action fab Button///
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent SignInHomeScreen = new Intent(MainActivity.this, SingInActivity.class);
                startActivity(SignInHomeScreen);
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgress.show();
                loadingProgress.setContentView(R.layout.progressloading);
                loadingProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                createUser();
            }
        });


        AuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                current = MainActivity.this.firebaseAuth.getCurrentUser();
                if(current != null){
                    newUser =  current;
                    Intent SignInHome = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(SignInHome);
                }
            }
        };


    }



        // Rotate Animation method
    private void rotateAnimation() {

        rotateAnimation= AnimationUtils.loadAnimation(this,R.anim.rotate);
        imageView.startAnimation(rotateAnimation);

    }

    /// Toast method
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

//============================================== Create User Method =======================================================//
    private void createUser(){

        Email = email.getText().toString().trim();
        Phone = phoneNum.getText().toString().trim();
        Password = password.getText().toString().trim();
        UserName = username.getText().toString().trim();

        if (Email.isEmpty()) {
            email.setError("Please Enter Email");
            email.requestFocus();
            loadingProgress.dismiss();

        } else if (Phone.isEmpty()) {

            phoneNum.setError("Please Enter Phone Number");
            phoneNum.requestFocus();


        } else if (UserName.isEmpty()) {

            username.setError("Please Enter an User Name");
            username.requestFocus();


        }else if (Password.isEmpty()){

            password.setError("Please Enter a Password");
            password.requestFocus();


            //After we collect all the information.
        } else if(!Email.isEmpty()&& !Phone.isEmpty() && !Password.isEmpty() && !UserName.isEmpty()){

            firebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(MainActivity.this,
                    new OnCompleteListener<AuthResult>() {

                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                loadingProgress.dismiss();
                                displayToast("SignUp Unsuccessful, Please Try Again");

                            }else if(task.isSuccessful()){
                                firebaseAuth.signInWithEmailAndPassword(Email,Password);
                                //Data Ready to be insert to the FireBase.
                                FirebaseUser recentUser = FirebaseAuth.getInstance().getCurrentUser();
                                if(recentUser != null) {
                                    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = firebaseDatabase.getReference("UserAccount");
                                    id = recentUser.getUid();
                                    User user = new User(id, Email, Password, UserName, Phone);
                                    myRef.child(recentUser.getUid()).setValue(user);
                                    firebaseAuth.signOut();
                                    loadingProgress.dismiss();
                                    displayToast("Account Created successfully!, Sign In!");
                                    email.getText().clear();
                                    phoneNum.getText().clear();
                                    password.getText().clear();
                                    username.getText().clear();
                                    Intent SignInHomeScreen = new Intent(MainActivity.this, SingInActivity.class);
                                    startActivity(SignInHomeScreen);
                                }else{
                                    loadingProgress.dismiss();
                                    displayToast("Error loading data to Database");
                                }
                            }
                        }
                    });


        }


    }

//===============================================================================================================================//

    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(AuthStateListener);
    }


}