package com.example.musicproject;

import android.net.Uri;

public class MusicFile {

     String Title;
     String Filepath;
     String ImageID;
     String ID;

    public MusicFile(){


    }


    public MusicFile(String id, String tittle, String SongFilePath, String imageId){

        this.ID = id;
        this.Title = tittle;
        this.Filepath = SongFilePath;
        this.ImageID = imageId;


    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getFilepath() {
        return Filepath;
    }

    public void setFilepath(String filepath) {
        Filepath = filepath;
    }

    public String getImageID() {
        return ImageID;
    }

    public void setImageID(String imageID) {
        ImageID = imageID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}

