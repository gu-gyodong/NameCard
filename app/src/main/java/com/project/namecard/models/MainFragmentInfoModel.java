package com.project.namecard.models;

public class MainFragmentInfoModel {
    public String success;
    public String ID;
    public String PassWord;
    public String Name;
    public String Birth;
    public String Email;


    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBirth() {
        return Birth;
    }

    public void setBirth(String birth) {
        Birth = birth;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setUserInfo(String ID, String PassWord, String Name, String Birth, String Email){
        this.ID = ID;
        this.PassWord = PassWord;
        this.Name = Name;
        this.Birth = Birth;
        this.Email = Email;
    }
}
