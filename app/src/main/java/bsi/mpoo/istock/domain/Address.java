package bsi.mpoo.istock.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    private long id;
    private String street;
    private int number;
    private String district;
    private String city;
    private String state;
    private int status;

    public Address(){}

    public Address(Parcel parcel){
        this.id = parcel.readLong();
        this.street = parcel.readString();
        this.number = parcel.readInt();
        this.district = parcel.readString();
        this.city = parcel.readString();
        this.state = parcel.readString();
        this.status = parcel.readInt();
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getStreet() {
        return street;
    }
    public void setStreet(String street) {
        this.street = street;
    }
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
    public String getDistrict() {
        return district;
    }
    public void setDistrict(String district) {
        this.district = district;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(street);
        dest.writeInt(number);
        dest.writeString(district);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeInt(status);
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>(){
        @Override
        public Address createFromParcel(Parcel source) {
            return new Address(source);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };
}

