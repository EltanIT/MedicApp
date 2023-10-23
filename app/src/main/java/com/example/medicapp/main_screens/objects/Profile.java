package com.example.medicapp.main_screens.objects;

import android.net.Uri;

import java.io.Serializable;

public class Profile implements Serializable {
    private Uri avatar;
    private String name;
    private String patronymic;
    private String dob;
    private byte gender;

    public Profile( String name, String patronymic, String birthday, String surname, byte sex) {
        this.name = name;
        this.patronymic = patronymic;
        this.dob = birthday;
        this.surname = surname;
        this.gender = sex;
    }

    public void setAvatar(Uri avatar) {
        this.avatar = avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setSex(byte sex) {
        this.gender = sex;
    }

    private String surname;

    public String getDob() {
        return dob;
    }

    public Profile(String name) {
        this.name = name;
    }

    public Profile() {
    }

    public Uri getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getSurname() {
        return surname;
    }

    public byte getSex() {
        return gender;
    }


    public Profile(Uri avatar, String name, String patronymic, String surname, byte sex) {
        this.avatar = avatar;
        this.name = name;
        this.patronymic = patronymic;
        this.surname = surname;
        this.gender = sex;
    }
}
