package com.example.musicproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

public class FavoriteActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private LinkedList<MusicFile> Musicdata;
    private FavoriteAdapter favoriteAdapter;



    private ListView favoriteViewMusic;
    private MusicFile music;

    private String backgroundColor;

    private DatabaseReference databaseUserReference;
    private User user;
    private ArrayList<User> userList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        backgroundColor = (sharedPreferences.getString("color", "none"));

        switch (backgroundColor) {

            case "Pink":
                setTheme(R.style.AppTheme1);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_favorite);
                break;

            case "Purple":
                setTheme(R.style.AppTheme2);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_favorite);
                break;

            case "Yellow":
                setTheme(R.style.AppTheme3);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_favorite);
                break;

            case "Red":
                setTheme(R.style.AppTheme4);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_favorite);
                break;

            case "Green":
                setTheme(R.style.AppTheme5);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_favorite);
                break;


            default:
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_favorite);
        }





        favoriteViewMusic = findViewById(R.id.FavoriteViewMusic);
        Musicdata = new LinkedList<>();


        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("FavoriteSongs").child(firebaseAuth.getUid());

        databaseUserReference = firebaseDatabase.getReference("UserAccount");
        userList = new ArrayList<>();


    }


    @Override
    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Musicdata.clear();


                for(DataSnapshot musicSnapShot : dataSnapshot.getChildren() ){

                    music = musicSnapShot.getValue(MusicFile.class);

                    Musicdata.addFirst(music);


                }

                databaseUserReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        userList.clear();

                        for(DataSnapshot userSnapShot : dataSnapshot.getChildren()){
                            user = userSnapShot.getValue(User.class);
                            userList.add(user);
                        }

                        favoriteAdapter = new FavoriteAdapter(FavoriteActivity.this, Musicdata, userList);
                        favoriteViewMusic.setAdapter(favoriteAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        super.onStart();
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
                Intent SignHome = new Intent(FavoriteActivity.this, MainActivity.class);
                startActivity(SignHome);
                displayToast("See you Later");
                return true;

            case R.id.perfile:
                Intent profile = new Intent(FavoriteActivity.this, ProfileActivity.class);
                startActivity(profile);
                return true;

            case R.id.settings:
                Intent settings = new Intent(FavoriteActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;

            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);

    }


    // Bottom Menu options onClick Method!
    public void HomeLibrary(MenuItem item) {

        Intent HomeActivity = new Intent(FavoriteActivity.this, HomeActivity.class);
        startActivity(HomeActivity);
        finish();
        displayToast("Home Library");
    }


    public void Import(MenuItem item) {

        Intent ImportActivity = new Intent(FavoriteActivity.this, ImportActivity.class);
        startActivity(ImportActivity);
        finish();
        displayToast("Import Your Music!");
    }

    public void Favorite (MenuItem item) {

        Intent FavoriteActivity = new Intent(FavoriteActivity.this, FavoriteActivity.class);
        startActivity(FavoriteActivity);
        finish();
        displayToast("Your Favorite Songs!");
    }




}
