package com.example.musicproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImportActivity extends AppCompatActivity {

    private FirebaseAuth.AuthStateListener AuthStateListener;
    private FirebaseUser newUser;
    private FirebaseUser userCurrent;


    private TypedArray ResourceImages;
    private CircleImageView fileImage;

    private static int PICK_AUDIO = 1813;
    private static int PICK_IMAGE = 1318;

    private Uri songImagePath;
    private ImageView songImage;

    private Uri songFilePath;
    private String strFileName1;
    private String strFileName2;

    private String stringImagePath;
    private String stringSongPath;

    private Random rnd;
    private Button upLoadData;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private StorageReference  storageReference;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    private EditText songName;
    private TextView songPathView;

    private String songTittle;
    private String SongPATH;
    private String SongImagePATH;

    private static final String fileName = "songTittle.txt";
    private FileOutputStream fos;


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
                setContentView(R.layout.activity_import);
                break;

            case "Purple":
                setTheme(R.style.AppTheme2);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_import);
                break;

            case "Yellow":
                setTheme(R.style.AppTheme3);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_import);
                break;

            case "Red":
                setTheme(R.style.AppTheme4);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_import);
                break;

            case "Green":
                setTheme(R.style.AppTheme5);
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_import);
                break;


            default:
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_import);
        }


        fileImage = findViewById(R.id.filePicture);
        upLoadData = findViewById(R.id.uploadButton);
        songImage = findViewById(R.id.songCover);
        songName = findViewById(R.id.songTittleText);

        songPathView = findViewById(R.id.filepath);

        rnd = new Random();
        ResourceImages = getResources().obtainTypedArray(R.array.FileImage);
        fileImage.setImageDrawable(ResourceImages.getDrawable(rnd.nextInt(3)));

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        firebaseDatabase = FirebaseDatabase.getInstance();

        firebaseStorage = FirebaseStorage.getInstance();

        storageReference = firebaseStorage.getReference("SongInfo").child(firebaseAuth.getUid());


        loadingProgress = new ProgressDialog(this);





        AuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                userCurrent = ImportActivity.this.firebaseAuth.getCurrentUser();
                if(userCurrent != null){
                    newUser = userCurrent;

                }
            }
        };

        upLoadData.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                loadingProgress.show();
                loadingProgress.setContentView(R.layout.progressloading);
                loadingProgress.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

//-------------------------------------------------Image PAth-----------------------------------------------------------------//
                if(songImagePath != null) {
                    final StorageReference imageRef =  storageReference.child("Images").child(strFileName2+"."+fileExtension(songImagePath));
                    imageRef.putFile(songImagePath)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            DatabaseReference databaseReference = firebaseDatabase.getReference("UserAccount").child(firebaseAuth.getUid()).child("SongInfo");
                                            songImagePath = uri;
                                            SongImagePATH = String.valueOf(uri);

                                        }
                                    });

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle unsuccessful uploads
                                    displayToast("Error Uploading Image");
                                }
                            });


                }
///------------------------------------------Song Tittle Name File-------------------------------------------------------------------------//
                if(songName != null){
                    songTittle = songName.getText().toString();
                     fos = null;
                    try {
                        fos = openFileOutput(fileName, MODE_PRIVATE);
                        fos.write(songTittle.getBytes());

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if(fos!= null){
                            try {
                                StorageReference fileSongTittle =  storageReference.child("SongTittle").child(fileName);
                                fileSongTittle.putFile(Uri.fromFile(new File(getFilesDir() + "/" + fileName))).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        displayToast("Error Loading Song Title");
                                    }
                                });
                                System.out.println("File Directory ----->>>>>>>>>>> " + getFilesDir());
                                System.out.println("File Path For Data ----->>>>>>>>>>> " + getFilesDir() + "/" + fileName);
                                fos.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }


                }else if(songName == null){
                    songName.setError("Please Enter a Song Title");
                    songName.requestFocus();
                }
//---------------------------------------------------------------Song FilePath---------------------------------------------------------
                if(songFilePath != null){
                    final StorageReference songFileRef =  storageReference.child("Songs").child(strFileName1+"."+fileExtension(songFilePath));
                    songFileRef.putFile(songFilePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            songFileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    songFilePath = uri;
                                    SongPATH = String.valueOf(uri);

                                    DatabaseReference databaseReference = firebaseDatabase.getReference("SongInformation");
                                    String idUser = firebaseAuth.getUid();
                                    String id = databaseReference.push().getKey();
                                    MusicFile music = new MusicFile(idUser,songTittle,SongPATH,SongImagePATH);
                                    databaseReference.child(id).setValue(music).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(!task.isSuccessful()){
                                                displayToast("Error uploading files");
                                            }else {
                                                loadingProgress.dismiss();
                                                displayToast("Files have been uploaded!");
                                            }
                                        }

                                    });


                                }
                            });
                        }
                    });


                }

            }
        });

    }

    //-------------------------------------------------------- Access Phone Only Photo Files -------------------------------------------------------------//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null){
            songImagePath = data.getData();
            File file = new File(songImagePath.toString());
            strFileName2 = file.getName();
            System.out.println("Here ---------->>>>>>>>" + "Path: "+ songImagePath );
            System.out.println("Here ---------->>>>>>>>" + "Name: "+strFileName2+"."+fileExtension(songImagePath) );
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), songImagePath);
                songImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//-------------------------------------------------------- Access Audio Only Photo Files -------------------------------------------------------------//
        if(requestCode == PICK_AUDIO && resultCode == RESULT_OK && data.getData() != null){
            songFilePath = data.getData();
            File file = new File(songFilePath.toString());
            strFileName1 = file.getName();
            System.out.println("Here ---------->>>>>>>>" + "Path: "+songFilePath );
            System.out.println("Here ---------->>>>>>>>" + "Name: "+strFileName1 );
            songPathView.setText(strFileName1+"."+fileExtension(songFilePath));

        }



        super.onActivityResult(requestCode, resultCode, data);
    }


    /////Gets File Extension
    public String fileExtension(Uri uri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }


    //-------------------------------------------------------- Access Audio Button Method -------------------------------------------------------------//
    public void searchFileSong(View view) {

        Intent search = new Intent();
        search.setType("audio/*");
        search.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(search.createChooser(search,"Select File" ), PICK_AUDIO);

    }


    //-------------------------------------------------------- Access Phone Button Method  -------------------------------------------------------------//

    public void searchPictureSong(View view) {

        Intent search = new Intent();
        search.setType("image/*");
        search.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(search.createChooser(search,"Select Image" ), PICK_IMAGE);

    }

    /// Toast method//
    public void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

    ///// Top Menu Creation//
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_top,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logOut:
                firebaseAuth.getInstance().signOut();
                Intent SignHome = new Intent(ImportActivity.this, MainActivity.class);
                startActivity(SignHome);
                displayToast("See you Later");
                return true;

            case R.id.perfile:
                Intent profile = new Intent(ImportActivity.this, ProfileActivity.class);
                startActivity(profile);
                return true;

            case R.id.settings:
                Intent settings = new Intent(ImportActivity.this, SettingsActivity.class);
                startActivity(settings);
                return true;

            default:
                // Do nothing
        }
        return super.onOptionsItemSelected(item);

    }


    // Bottom Menu options onClick Method!
    public void HomeLibrary(MenuItem item) {

        Intent HomeActivity = new Intent(ImportActivity.this, HomeActivity.class);
        startActivity(HomeActivity);
        finish();
        displayToast("Home Library");
    }


    public void Import(MenuItem item) {

        Intent ImportActivity = new Intent(ImportActivity.this, ImportActivity.class);
        startActivity(ImportActivity);
        finish();
        displayToast("Import Your Music!");
    }

    public void Favorite (MenuItem item) {

        Intent FavoriteActivity = new Intent(ImportActivity.this, FavoriteActivity.class);
        startActivity(FavoriteActivity);
        finish();
        displayToast("Your Favorite Songs!");
    }

    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(AuthStateListener);
    }
}
