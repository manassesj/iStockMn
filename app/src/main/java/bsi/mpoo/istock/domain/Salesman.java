package bsi.mpoo.istock.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Salesman implements Parcelable{
    private User user;

    public Salesman(){}

    public Salesman(Parcel parcel){
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

    public static final Parcelable.Creator<Salesman> CREATOR = new Parcelable.Creator<Salesman>(){
        @Override
        public Salesman createFromParcel(Parcel source) {
            return new Salesman(source);
        }

        @Override
        public Salesman[] newArray(int size) {
            return new Salesman[size];
        }
    };

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
