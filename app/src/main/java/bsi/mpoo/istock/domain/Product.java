package bsi.mpoo.istock.domain;

import android.os.Parcel;
import android.os.Parcelable;
import java.math.BigDecimal;

public class Product implements Parcelable {
    private long id;
    private String name;
    private BigDecimal price;
    private long quantity;
    private long minimumQuantity;
    private Administrator administrator;
    private int status;

    public Product(){}

    public Product(Parcel parcel){
        this.id = parcel.readLong();
        this.name = parcel.readString();
        this.price = new BigDecimal(parcel.readString());
        this.quantity = parcel.readLong();
        this.minimumQuantity = parcel.readLong();
        this.administrator = (Administrator) parcel.readValue(Administrator.class.getClassLoader());
        this.status = parcel.readInt();
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
    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public long getQuantity() {
        return quantity;
    }
    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }
    public long getMinimumQuantity() {
        return minimumQuantity;
    }
    public void setMinimumQuantity(long minimumQuantity) {
        this.minimumQuantity = minimumQuantity;
    }
    public Administrator getAdministrator() {
        return administrator;
    }
    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
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
        dest.writeString(name);
        dest.writeString(price.toString());
        dest.writeLong(quantity);
        dest.writeLong(minimumQuantity);
        dest.writeValue(administrator);
        dest.writeInt(status);
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>(){
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Product){
            if (getId() == ((Product) obj).getId()){
                return true;
            }
        }
        return false;
    }
}
