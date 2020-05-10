package com.example.musicproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class HomeActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private LinkedList<MusicFile> Musicdata;
    private MusicAdapter musicAdapter;


    private ListView listViewMusic;
    private MusicFile music;

    private String backgroundColor;

    private DatabaseReference databaseUserReference;
    private User user;
    private ArrayList<User> userList;


    protected void onCreate(Bundle savedInstanceState) {

        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);

        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        backgroundColor = (sharedPreferences.getString("color", "none"));



        switch (backgroundColor) {

            case "Pink":
                setTheme(R.style.AppTheme1);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);
                break;

            case "Purple":
                setTheme(R.style.AppTheme2);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);
                break;

            case "Yellow":
                setTheme(R.style.AppTheme3);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);
                break;

            case "Red":
                setTheme(R.style.AppTheme4);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);
                break;

            case "Green":
                setTheme(R.style.AppTheme5);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);
                break;


            default:
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_home);
        }



        listViewMusic = findViewById(R.id.listViewMusic);
        Musicdata = new LinkedList<>();


        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("SongInformation");

        databaseUserReference = firebaseDatabase.getReference("UserAccount");
        userList = new ArrayList<>();


    }

    @Override
    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Musicdata.clear();


                for (DataSnapshot musicSnapShot : dataSnapshot.getChildren()) {
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

                        musicAdapter = new MusicAdapter(HomeActivity.this, Musicdata,userList);
                        listViewMusic.setAdapter(musicAdapter);

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
        getMenuInflater().inflate(R.menu.menu_top, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                firebaseAuth.getInstance().signOut();
                Intent SignHome = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(SignHome);
                displayToast("See you Later");
                return true;

            case R.id.perfile:
                Intent profile = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(profile);
                return true;

            case R.id.settings:
                Intent settings = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;

            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);

    }


    // Bottom Menu options onClick Method!
    public void HomeLibrary(MenuItem item) {

        Intent HomeActivity = new Intent(HomeActivity.this, HomeActivity.class);
        startActivity(HomeActivity);
        finish();
        displayToast("Home Library");
    }


    public void Import(MenuItem item) {

        Intent ImportActivity = new Intent(HomeActivity.this, ImportActivity.class);
        startActivity(ImportActivity);
        finish();
        displayToast("Import Your Music!");
    }

    public void Favorite(MenuItem item) {

        Intent FavoriteActivity = new Intent(HomeActivity.this, FavoriteActivity.class);
        startActivity(FavoriteActivity);
        finish();
        displayToast("Your Favorite Songs!");
    }


}
