package bsi.mpoo.istock.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    private long id;
    private String name;
    private String email;
    private String password;
    private int type;
    private int status;
    private String company;
    private byte[] image;
    private long administrator;

    public User(){}

    private User(Parcel parcel) {
        id = parcel.readLong();
        name = parcel.readString();
        email = parcel.readString();
        password = parcel.readString();
        type = parcel.readInt();
        status = parcel.readInt();
        company = parcel.readString();
        administrator = parcel.readLong();
        image = (byte[]) parcel.readValue(Byte[].class.getClassLoader());
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeInt(type);
        dest.writeInt(status);
        dest.writeString(company);
        dest.writeLong(administrator);
        dest.writeValue(image);
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public int getType(){
        return type;
    }
    public void setType(int type){
        this.type = type;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getCompany() {
        return company;
    }
    public void setCompany(String company) {
        this.company = company;
    }
    public long getAdministrator() {
        return administrator;
    }
    public void setAdministrator(long administrator) {
        this.administrator = administrator;
    }
    public byte[] getImage() {
        return image;
    }
    public void setImage(byte[] image) {
        this.image = image;
    }
}
