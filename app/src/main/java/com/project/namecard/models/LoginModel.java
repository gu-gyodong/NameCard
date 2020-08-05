package com.project.namecard.models;

public class LoginModel {
    public String success;
    public String ID;
    public String PassWord;

    public String getSuccess() {
        return success;
    }
    public String getId() {
        return ID;
    }
    public String getPassword() {
        return PassWord;
    }
    public void setSuccess(String success) {
        this.success = success;
    }
    public void setId(String ID) {
        this.ID = ID;
    }
    public void setPassword(String PassWord) {
        this.PassWord = PassWord;
    }
    public void setLogin(String success, String ID, String PassWord){
        this.success = success;
        this.ID = ID;
        this.PassWord = PassWord;
    }
}
