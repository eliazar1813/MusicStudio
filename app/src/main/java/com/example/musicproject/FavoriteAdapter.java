package com.example.musicproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoriteAdapter extends ArrayAdapter<MusicFile> implements View.OnClickListener, View.OnLongClickListener {

    private Activity context;
    private LinkedList<MusicFile> musicData;
    private ArrayList<User> userList;


    private TextView songTittle;
    private CircleImageView songImage;
    private MusicFile music;

    private ImageView pauseButton;
    private Button stopButton;
    private ImageView playButton;
    private Button favButton;
    private MediaPlayer mediaPlayer;

    private Boolean flag = true;

    private Button songOwner;
    private User user;

    private Button share;

    public FavoriteAdapter(Activity context, LinkedList<MusicFile> musicData, ArrayList<User> user) {
        super(context, R.layout.activity_favorite, musicData);
        this.musicData = musicData;
        this.context = context;
        this.userList = user;


    }


    @SuppressLint("CutPasteId")
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();

        @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.music_content, null, false);


        songTittle = listViewItem.findViewById(R.id.songTittle);
        songImage = listViewItem.findViewById(R.id.songPicture);

        stopButton = listViewItem.findViewById(R.id.stopButton);
        pauseButton = listViewItem.findViewById(R.id.PauseButton);
        playButton = listViewItem.findViewById(R.id.PlayButton);
        favButton = listViewItem.findViewById(R.id.fav_Button);
        songOwner = listViewItem.findViewById(R.id.songOwner);
        share = listViewItem.findViewById(R.id.shareButton);


        listViewItem.setOnClickListener(this);
        listViewItem.setOnLongClickListener(this);

        music = musicData.get(position);


        songTittle.setText(music.getTitle());
        Picasso.get().load(music.getImageID()).into(songImage);

        songOwner.setText("By:"+ getSongOwner(music));




        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!flag){

                    mediaPlayer.stop();
                    mediaPlayer.release();
                    flag = true;
                }

                displayToast("Media Player Release !");

            }
        });

        listViewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(flag) {

                    mediaPlayer = MediaPlayer.create(context , Uri.parse(musicData.get(position).getFilepath()));

                    flag = false;
                }

                if(mediaPlayer.isPlaying()){

                    mediaPlayer.pause();
                    displayToast("Pause");

                }else{

                    mediaPlayer.start();
                    displayToast("Playing " + musicData.get(position).getTitle());
                }



            }
        });

        songOwner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfo(musicData.get(position));

            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                android.content.Intent intent = new android.content.Intent();
                intent.setAction(android.content.Intent.ACTION_SEND);
                intent.putExtra(android.content.Intent.EXTRA_TEXT, "Listen to: " +
                        musicData.get(position).getTitle() + " By: " + getSongOwner(musicData.get(position)) +
                        " Link to song: " + musicData.get(position).getFilepath());
                intent.setType("text/plain");
                android.content.Intent chooser = android.content.Intent.createChooser(intent, "Share with");
                context.startActivity(chooser);

            }
        });



        return listViewItem;
    }



    public int getItemCount() {
        return musicData.size();
    }


    @Override
    public void onClick(View v) {


    }



    @Override
    public boolean onLongClick(View v) {
        return false;
    }

    /// Toast method
    public void displayToast(String message) {
        Toast.makeText(context.getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }


    public String getSongOwner(MusicFile music){

        String userName = "User";
        user = userList.get(0);

        if(user.getUserId().equals(music.getID())){
            userName = user.getUserName();
        }else{
            for(int i = 1; i < userList.size(); i++){
                user = userList.get(i);
                if(user.getUserId().equals(music.getID())){
                    userName = user.getUserName();
                }
            }
        }


        return userName;
    }


    public void userInfo(MusicFile music){

        String email = "Not Email saved";
        user = userList.get(0);

        if(user.getUserId().equals(music.getID())){
            email = user.getEmail();
        }else{
            for(int i = 1; i < userList.size(); i++){
                user = userList.get(i);
                if(user.getUserId().equals(music.getID())){
                    email = user.getEmail();
                }
            }
        }

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater =  LayoutInflater.from(context);
        final View userInfo = inflater.inflate(R.layout.user_info, null);
        dialogBuilder.setView(userInfo);

        final TextView userName = userInfo.findViewById(R.id.userName);
        final TextView userEmail = userInfo.findViewById(R.id.userEmail);

        userName.setText(getSongOwner(music));
        userEmail.setText(email);

        dialogBuilder.setPositiveButton("Ok", new android.content.DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do Nothing

            }
        });

        dialogBuilder.create().show();


    }

}

