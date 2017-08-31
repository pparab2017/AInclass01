package com.amad.ainclass01;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pushparajparab on 8/27/17.
 */

public class User implements Parcelable {

    private String fName,lName,email,password,address,gender,token,status,errorMessage;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User(){}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    protected User(Parcel in){

        fName = in.readString();
        lName = in.readString();
        email = in.readString();
        password = in.readString();
        address = in.readString();
        gender = in.readString();
        status = in.readString();
        errorMessage = in.readString();
        age = in.readInt();
        id = in.readInt();
        weight = in.readInt();
        token = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public String toString() {
        return "User{" +
                "fName='" + fName + '\'' +
                ", lName='" + lName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", address='" + address + '\'' +
                ", gender='" + gender + '\'' +
                ", token='" + token + '\'' +
                ", status='" + status + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", age=" + age +
                ", weight=" + weight +
                ", id=" + id +
                '}';
    }

    public String getfName() {
        return fName;
    }

    public String getUserFullName() {
        return fName + " " + lName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    private int age,weight,id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fName);
        dest.writeString(lName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(address);
        dest.writeString(gender);
        dest.writeString(status);
        dest.writeString(errorMessage);
        dest.writeInt(age);
        dest.writeInt(id);
        dest.writeInt(weight);
        dest.writeString(token);
    }
}
