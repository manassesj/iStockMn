package bsi.mpoo.istock.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Administrator implements Parcelable{
    private User user;

    public Administrator(){}

    public Administrator(Parcel parcel){
        this.user = (User)parcel.readValue(User.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(user);
    }

    public static final Parcelable.Creator<Administrator> CREATOR = new Parcelable.Creator<Administrator>(){
        @Override
        public Administrator createFromParcel(Parcel source) {
            return new Administrator(source);
        }

        @Override
        public Administrator[] newArray(int size) {
            return new Administrator[size];
        }
    };

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
