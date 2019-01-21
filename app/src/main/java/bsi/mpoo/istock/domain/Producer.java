package bsi.mpoo.istock.domain;

import android.os.Parcel;
import android.os.Parcelable;

public class Producer implements Parcelable{
    private User user;

    public Producer(){}

    public Producer(Parcel parcel){
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

    public static final Parcelable.Creator<Producer> CREATOR = new Parcelable.Creator<Producer>(){
        @Override
        public Producer createFromParcel(Parcel source) {
            return new Producer(source);
        }

        @Override
        public Producer[] newArray(int size) {
            return new Producer[size];
        }
    };

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
