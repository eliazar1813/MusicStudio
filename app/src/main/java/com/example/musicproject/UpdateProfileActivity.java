package com.example.musicproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {


    private EditText Password, Email, Phone;
    private Button buttonUpdate;
    private FirebaseDatabase firebaseDatabase;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    private String Username;
    private String UserId;

    private String previousEmail;
    private String previousPassword;

    private String email;
    private String password;
    private String phone;

    private TypedArray ResourceImages;
    private CircleImageView profileImage;

    private Random rnd;

    private FirebaseAuth.AuthStateListener AuthStateListener;
    private FirebaseUser newUser;
    private FirebaseUser userCurrent;

    private ProgressDialog loadingProgress;

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
                setContentView(R.layout.activity_update_profile);
                break;

            case "Purple":
                setTheme(R.style.AppTheme2);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_update_profile);
                break;

            case "Yellow":
                setTheme(R.style.AppTheme3);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_update_profile);
                break;

            case "Red":
                setTheme(R.style.AppTheme4);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_update_profile);
                break;

            case "Green":
                setTheme(R.style.AppTheme5);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_update_profile);
                break;


            default:
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_update_profile);
        }

        Password = findViewById(R.id.songTittleText);
        Email = findViewById(R.id.Email);
        Phone = findViewById(R.id.songPicture);
        buttonUpdate = findViewById(R.id.uploadButton);
        profileImage = findViewById(R.id.ProfilePicture);

        rnd = new Random();
        ResourceImages = getResources().obtainTypedArray(R.array.RandomPictures);
        profileImage.setImageDrawable(ResourceImages.getDrawable(rnd.nextInt(9)));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();

        loadingProgress = new ProgressDialog(this);


                AuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                userCurrent = UpdateProfileActivity.this.firebaseAuth.getCurrentUser();
                if(userCurrent != null){
                    newUser = userCurrent;

                }
            }
        };


        final DatabaseReference databaseReference =  firebaseDatabase.getReference("UserAccount").
                child(firebaseUser.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User profile = dataSnapshot.getValue(User.class);
                Password.setText(profile.getPassword());
                Email.setText(profile.getEmail());
                Phone.setText(profile.getPhone());

                Username = profile.getUserName();
                UserId = profile.getUserId();

                previousEmail = profile.getEmail();
                previousPassword = profile.getPassword();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(UpdateProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingProgress.show();
                loadingProgress.setContentView(R.layout.progressloading);
                loadingProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                if(firebaseUser != null) {
                    password = Password.getText().toString();
                    email = Email.getText().toString();
                    phone = Phone.getText().toString();

                    /// Update user newPassword and email to Database and change the authentication of User.

                    newUser.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                displayToast("Error updating Email");
                                email = previousEmail;
                            }
                        }
                    });

                    newUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(!task.isSuccessful()){
                                displayToast("Error updating Email");
                                password = previousPassword;
                            }
                        }
                    });

                    User updateUser = new User(UserId, email, password, Username, phone);
                    databaseReference.setValue(updateUser);
                    displayToast("Profile Updated");
                    Intent profile = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                    startActivity(profile);
                    loadingProgress.dismiss();
                }else{
                    loadingProgress.dismiss();
                    displayToast("Error updating Profile");
                }

            }
        });

    }


    ///// Top Menu Creation
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                firebaseAuth.getInstance().signOut();
                Intent SignHome = new Intent(UpdateProfileActivity.this, MainActivity.class);
                startActivity(SignHome);
                displayToast("See you Later");
                return true;

            case R.id.perfile:
                Intent profile = new Intent(UpdateProfileActivity.this, ProfileActivity.class);
                startActivity(profile);
                return true;

            case R.id.settings:
                Intent settings = new Intent(UpdateProfileActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;

            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);

    }

    public void HomeLibrary(MenuItem item) {

        Intent HomeActivity = new Intent(UpdateProfileActivity.this, HomeActivity.class);
        startActivity(HomeActivity);
        finish();
        displayToast("Home Library");
    }

    public void Import(MenuItem item) {

        Intent ImportActivity = new Intent(UpdateProfileActivity.this, ImportActivity.class);
        startActivity(ImportActivity);
        finish();
        displayToast("Import Your Music!");
    }

    public void Favorite (MenuItem item) {

        Intent FavoriteActivity = new Intent(UpdateProfileActivity.this, FavoriteActivity.class);
        startActivity(FavoriteActivity);
        finish();
        displayToast("Your Favorite Songs!");
    }


    /// Toast method
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }


    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(AuthStateListener);
    }
}
