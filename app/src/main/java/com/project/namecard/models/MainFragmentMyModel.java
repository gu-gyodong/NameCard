package com.project.namecard.models;

public class MainFragmentMyModel {
    public String CardID;
    public String ID;
    public String Owner;
    public String CardImage;
    public String Name;
    public String Company;
    public String Depart;
    public String Position;
    public String CompanyNumber;
    public String PhoneNumber;
    public String Email;
    public String FaxNumber;
    public String Address;
    public String Memo;

    public void setCard(String CardID, String ID, String Owner, String CardImage,
                        String Name, String Company, String Depart, String Position, String CompanyNumber,
                        String PhoneNumber, String Email, String FaxNumber, String Address, String Memo){
        this.CardID = CardID;
        this.ID = ID;
        this.Owner = Owner;
        this.CardImage = CardImage;
        this.Name = Name;
        this.Company = Company;
        this.Depart = Depart;
        this.Position = Position;
        this.CompanyNumber = CompanyNumber;
        this.PhoneNumber = PhoneNumber;
        this.Email = Email;
        this.FaxNumber = FaxNumber;
        this.Address = Address;
        this.Memo = Memo;

    }
    public String getCardID() {
        return CardID;
    }
    public void setCardID(String cardID) {
        CardID = cardID;
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
    public String getCardImage() {
        return CardImage;
    }
    public void setCardImage(String cardImage) {
        CardImage = cardImage;
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
    public String getMemo() {
        return Memo;
    }
    public void setMemo(String memo) {
        Memo = memo;
    }


}
