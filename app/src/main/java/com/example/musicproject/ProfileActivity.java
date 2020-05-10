package com.example.musicproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;

    private FirebaseAuth.AuthStateListener AuthStateListener;
    private FirebaseUser newUser;
    private FirebaseUser current;

    private TextView userView;
    private TextView emailView;
    private TextView phoneView;


    private TypedArray ResourceImages;
    private CircleImageView profileImage;

    private Random rnd;


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
                setContentView(R.layout.activity_profile);
                break;

            case "Purple":
                setTheme(R.style.AppTheme2);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_profile);
                break;

            case "Yellow":
                setTheme(R.style.AppTheme3);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_profile);
                break;

            case "Red":
                setTheme(R.style.AppTheme4);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_profile);
                break;

            case "Green":
                setTheme(R.style.AppTheme5);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_profile);
                break;


            default:
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_profile);
        }


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        databaseReference = firebaseDatabase.getReference("UserAccount").child(firebaseAuth.getUid());

        userView = findViewById(R.id.UserNameView);
        emailView = findViewById(R.id.emailView);
        phoneView = findViewById(R.id.phoneView);
        profileImage = findViewById(R.id.ProfilePicture);

        rnd = new Random();
        ResourceImages = getResources().obtainTypedArray(R.array.RandomPictures);
        profileImage.setImageDrawable(ResourceImages.getDrawable(rnd.nextInt(9)));


        AuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                current = ProfileActivity.this.firebaseAuth.getCurrentUser();
                if(current != null){
                    newUser =  current;
                }
            }
        };


        ///Getting User information to display in profile Activity.
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User profile = dataSnapshot.getValue(User.class);
                userView.setText(profile.getUserName());
                emailView.setText(profile.getEmail());
                phoneView.setText(profile.getPhone());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ProfileActivity.this, databaseError.getCode(), Toast.LENGTH_SHORT).show();

            }
        });




    }

    /// Toast method
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
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
                Intent SignHome = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(SignHome);
                displayToast("See you Later");
                return true;

            case R.id.perfile:
                Intent profile = new Intent(ProfileActivity.this, ProfileActivity.class);
                startActivity(profile);
                return true;

            case R.id.settings:
                Intent settings = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;

            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);

    }


    // Bottom Menu options onClick Method!
    public void HomeLibrary(MenuItem item) {

        Intent HomeActivity = new Intent(ProfileActivity.this, HomeActivity.class);
        startActivity(HomeActivity);
        finish();
        displayToast("Home Library");
    }

    public void Import(MenuItem item) {

        Intent ImportActivity = new Intent(ProfileActivity.this, ImportActivity.class);
        startActivity(ImportActivity);
        finish();
        displayToast("Import Your Music!");
    }

    public void editProfile(View view) {


        Intent editProfile = new Intent(ProfileActivity.this, UpdateProfileActivity.class);
        startActivity(editProfile);
        finish();
    }

    public void Favorite (MenuItem item) {

        Intent FavoriteActivity = new Intent(ProfileActivity.this, FavoriteActivity.class);
        startActivity(FavoriteActivity);
        finish();
        displayToast("Your Favorite Songs!");
    }

    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(AuthStateListener);
    }

}
