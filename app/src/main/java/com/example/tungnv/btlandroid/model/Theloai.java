package com.example.tungnv.btlandroid.model;

/**
 * Created by Tungnv on 3/22/2018.
 */

public class Theloai {
    private String Name;
    private String Image;

    public Theloai() {

    }

    public Theloai(String name, String image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
