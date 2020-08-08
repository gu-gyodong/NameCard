package com.project.namecard.models;

import androidx.lifecycle.MutableLiveData;

public class CardRegisterModel {

    public String success;
    public String CardImage;
    public String ID;
    public String Owner;
    public String Name;
    public String Company;
    public String Depart;
    public String Position;
    public String CompanyNumber;
    public String PhoneNumber;
    public String Email;
    public String EmailAddress;
    public String FaxNumber;
    public String Address;
    public String Detailaddress;
    public String Memo;

    public void setCardRegister(String CardImage, String ID, String Owner, String Name, String Company, String Depart, String Position,
                                  String CompanyNumber, String PhoneNumber, String Email, String EmailAddress, String FaxNumber,
                                  String Address, String DetailAddress, String Memo) {

        this.CardImage = CardImage;
        this.ID = ID;
        this.Owner = Owner;
        this.Name = Name;
        this.Company = Company;
        this.Depart = Depart;
        this.Position = Position;
        this.CompanyNumber = CompanyNumber;
        this.PhoneNumber = PhoneNumber;
        this.Email = Email;
        this.EmailAddress = EmailAddress;
        this.FaxNumber = FaxNumber;
        this.Address = Address;
        this.Detailaddress = DetailAddress;
        this.Memo = Memo;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getCardImage() {
        return CardImage;
    }

    public void setCardImage(String cardImage) {
        CardImage = cardImage;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCompany() {
        return Company;
    }

    public void setCompany(String company) {
        Company = company;
    }

    public String getDepart() {
        return Depart;
    }

    public void setDepart(String depart) {
        Depart = depart;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getCompanyNumber() {
        return CompanyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        CompanyNumber = companyNumber;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getFaxNumber() {
        return FaxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        FaxNumber = faxNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDetailaddress() {
        return Detailaddress;
    }

    public void setDetailaddress(String detailaddress) {
        Detailaddress = detailaddress;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String memo) {
        Memo = memo;
    }
}
