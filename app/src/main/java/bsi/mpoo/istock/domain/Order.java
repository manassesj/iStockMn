package bsi.mpoo.istock.domain;
import android.os.Parcel;
import android.os.Parcelable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;


public class Order implements Parcelable{

    private long id;
    private LocalDate dateCreation;
    private Client client;
    private Administrator administrator;
    private BigDecimal total;
    private int delivered;
    private LocalDate dateDelivery;
    private int status;
    private ArrayList items;

    public Order() {}

    public Order(Parcel parcel) {
        this.id = parcel.readLong();
        this.dateCreation = (LocalDate) parcel.readValue(LocalDate.class.getClassLoader());
        this.client = parcel.readParcelable(Client.class.getClassLoader());
        this.administrator = parcel.readParcelable(Administrator.class.getClassLoader());
        this.total = (BigDecimal)parcel.readValue(BigDecimal.class.getClassLoader());
        this.delivered = parcel.readInt();
        this.dateDelivery = (LocalDate) parcel.readValue(LocalDate.class.getClassLoader());
        this.status = parcel.readInt();
        this.items = parcel.readArrayList(Item.class.getClassLoader());
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public LocalDate getDateCreation() {
        return dateCreation;
    }
    public void setDateCreation(LocalDate dateCreation) {
        this.dateCreation = dateCreation;
    }
    public Client getClient() {
        return client;
    }
    public void setClient(Client client) {
        this.client = client;
    }
    public Administrator getAdministrator() {
        return administrator;
    }
    public void setAdministrator(Administrator administrator) {
        this.administrator = administrator;
    }
    public BigDecimal getTotal() {
        return total;
    }
    public void setTotal(BigDecimal total) {
        this.total = total;
    }
    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }
    public int getDelivered() {
        return delivered;
    }
    public LocalDate getDateDelivery() {
        return dateDelivery;
    }
    public void setDateDelivery(LocalDate dateDelivery) {
        this.dateDelivery = dateDelivery;
    }
    public int getStatus(){
        return this.status;
    }
    public void setStatus(int status){
        this.status = status;
    }
    public ArrayList getItems() {
        return items;
    }
    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(id);
        parcel.writeValue(dateCreation);
        parcel.writeValue(client);
        parcel.writeValue(administrator);
        parcel.writeString(total.toString());
        parcel.writeInt(delivered);
        parcel.writeValue(dateDelivery);
        parcel.writeInt(status);
        parcel.writeArray(new ArrayList[]{items});
    }
}
