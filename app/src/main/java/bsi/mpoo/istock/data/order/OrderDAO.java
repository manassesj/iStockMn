package bsi.mpoo.istock.data.order;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.provider.BaseColumns;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import bsi.mpoo.istock.data.DbHelper;
import bsi.mpoo.istock.data.item.ContractItem;
import bsi.mpoo.istock.data.product.ContractProduct;
import bsi.mpoo.istock.data.product.ProductDAO;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Item;
import bsi.mpoo.istock.domain.Order;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.services.Exceptions;
import bsi.mpoo.istock.services.client.ClientServices;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.ItemServices;
import bsi.mpoo.istock.services.product.ProductServices;
import bsi.mpoo.istock.services.user.UserServices;

public class OrderDAO {
    private Context context;
    private UserServices userServices;
    private ItemServices itemServices;
    private ClientServices clientServices;

    public OrderDAO(Context context){
        this.context = context;
        this.clientServices = new ClientServices(context);
        this.userServices = new UserServices(context);
        this.itemServices = new ItemServices(context);
    }

    public void insertOrder(Order order) throws Exception {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        itemServices.insertItem(order.getItems());
        values.put(ContractOrder.COLUMN_DATE_CREATION, order.getDateCreation().toString());
        values.put(ContractOrder.COLUMN_ID_CLIENT, order.getClient().getId());
        values.put(ContractOrder.COLUMN_ID_ADM, order.getAdministrator().getUser().getId());
        values.put(ContractOrder.COLUMN_TOTAL, order.getTotal().toString());
        values.put(ContractOrder.COLUMN_DELIVERED, order.getDelivered());
        if (order.getDelivered() == Constants.Order.NOT_DELIVERED && order.getDateDelivery() != null){
            values.put(ContractOrder.COLUMN_DATE_DELIVERY, order.getDateDelivery().toString());
        } else {
            updateProducts(order.getItems());
        }
        values.put(ContractOrder.COLUMN_STATUS, Constants.Status.ACTIVE);
        registerItems(order.getItems());
        values.put(ContractOrder.COLUMN_ITEMS, convertArrayItemToIdString(order.getItems()));
        long newRowID = db.insert(ContractOrder.TABLE_NAME, null, values);
        order.setId(newRowID);
        db.close();
    }

    private void registerItems(ArrayList items) {
        ItemServices itemServices = new ItemServices(context);
        itemServices.insertItem(items);
    }

    private void updateProducts(ArrayList<Item> items) throws Exception{
        ProductServices productServices = new ProductServices(context);
        for (Item item: items){
            item.getProduct().setQuantity(item.getProduct().getQuantity()-item.getQuantity());
            productServices.updateProduct(item.getProduct());

        }
    }

    private String convertArrayItemToIdString(ArrayList<Item> items) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Item item:items) {
            stringBuilder.append(" ").append(item.getId());
        }
        return stringBuilder.toString().trim();
    }

    private ArrayList<Item> arrayItemStringIdToArrayItem(String string){
        String[] itemsId = string.split(" ");
        ArrayList<Item> arrayList = new ArrayList<>();
        for (String id:itemsId) {
            arrayList.add(itemServices.getItemById(Long.parseLong(id)));
        }
        return arrayList;

    }

    public Order getOrderById(long id) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Order searchedOrder = null;
        String[] projection = {
                BaseColumns._ID,
                ContractOrder.COLUMN_DATE_CREATION,
                ContractOrder.COLUMN_ID_CLIENT,
                ContractOrder.COLUMN_ID_ADM,
                ContractOrder.COLUMN_TOTAL,
                ContractOrder.COLUMN_DELIVERED,
                ContractOrder.COLUMN_DATE_DELIVERY,
                ContractOrder.COLUMN_STATUS,
                ContractOrder.COLUMN_ITEMS
        };
        String selection = ContractOrder._ID+" = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(
                ContractOrder.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.getCount()==1){
            cursor.moveToNext();
            searchedOrder = createOrder(cursor);
        }
        cursor.close();
        db.close();
        return searchedOrder;
    }

    public List<Order> getOrdersByAdm(Administrator administrator){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContractOrder.COLUMN_DATE_CREATION,
                ContractOrder.COLUMN_ID_CLIENT,
                ContractOrder.COLUMN_ID_ADM,
                ContractOrder.COLUMN_TOTAL,
                ContractOrder.COLUMN_DELIVERED,
                ContractOrder.COLUMN_DATE_DELIVERY,
                ContractOrder.COLUMN_STATUS,
                ContractOrder.COLUMN_ITEMS
        };
        List<Order> orderList = new ArrayList<>();
        String selection = ContractOrder.COLUMN_ID_ADM + " = ?";
        String[] selectionArgs = {String.valueOf(administrator.getUser().getId())};
        Cursor cursor = db.query(
                ContractOrder.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToNext()){
            do {
                Order order = createOrder(cursor);
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderList;
    }

    public List<Order> getActiveOrdersByAdm(Administrator administrator) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContractOrder.COLUMN_DATE_CREATION,
                ContractOrder.COLUMN_ID_CLIENT,
                ContractOrder.COLUMN_ID_ADM,
                ContractOrder.COLUMN_TOTAL,
                ContractOrder.COLUMN_DELIVERED,
                ContractOrder.COLUMN_DATE_DELIVERY,
                ContractOrder.COLUMN_STATUS,
                ContractOrder.COLUMN_ITEMS
        };
        List<Order> orderList = new ArrayList<>();
        String selection = ContractOrder.COLUMN_ID_ADM + " = ?"+" AND "+
                ContractOrder.COLUMN_STATUS + " = ?";
        String[] selectionArgs = {String.valueOf(administrator.getUser().getId()),
                String.valueOf(Constants.Status.ACTIVE)};
        Cursor cursor = db.query(
                ContractOrder.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.moveToNext()){
            do {
                Order order = createOrder(cursor);
                orderList.add(order);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return orderList;
    }


    public void updateOrder(Order order){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractOrder.COLUMN_DATE_CREATION,order.getDateCreation().toString());
        values.put(ContractOrder.COLUMN_ID_CLIENT,order.getClient().getId());
        values.put(ContractOrder.COLUMN_ID_ADM,order.getAdministrator().getUser().getId());
        values.put(ContractOrder.COLUMN_TOTAL,order.getTotal().toString());
        values.put(ContractOrder.COLUMN_DELIVERED,order.getDelivered());
        values.put(ContractOrder.COLUMN_DATE_DELIVERY,order.getDateDelivery().toString());
        values.put(ContractOrder.COLUMN_STATUS, order.getStatus());
        values.put(ContractOrder.COLUMN_ITEMS, convertArrayItemToIdString(order.getItems()));
        String selection = ContractOrder._ID + " = ?";
        String[] selectionArgs = {String.valueOf(order.getId())};
        db.update(ContractOrder.TABLE_NAME,values,selection,selectionArgs);
    }

    public Order createOrder(Cursor cursor) {
        int idIndex = cursor.getColumnIndexOrThrow(ContractOrder._ID);
        int dateCreationIndex = cursor.getColumnIndexOrThrow(ContractOrder.COLUMN_DATE_CREATION);
        int idClientIndex = cursor.getColumnIndexOrThrow(ContractOrder.COLUMN_ID_CLIENT);
        int idAdmIndex = cursor.getColumnIndexOrThrow(ContractOrder.COLUMN_ID_ADM);
        int totalIndex = cursor.getColumnIndexOrThrow(ContractOrder.COLUMN_TOTAL);
        int deliveredIndex = cursor.getColumnIndexOrThrow(ContractOrder.COLUMN_DELIVERED);
        int dateDeliveryIndex = cursor.getColumnIndexOrThrow(ContractOrder.COLUMN_DATE_DELIVERY);
        int statusIndex = cursor.getColumnIndexOrThrow(ContractOrder.COLUMN_STATUS);
        int itemsIndex = cursor.getColumnIndexOrThrow(ContractOrder.COLUMN_ITEMS);
        long id = cursor.getLong(idIndex);
        String dateCreation = cursor.getString(dateCreationIndex);
        long id_client = cursor.getLong(idClientIndex);
        long id_adm = cursor.getLong(idAdmIndex);
        String total = cursor.getString(totalIndex);
        int delivered = cursor.getInt(deliveredIndex);
        String dateDelivery = cursor.getString(dateDeliveryIndex);
        int status = cursor.getInt(statusIndex);
        String itemsString = cursor.getString(itemsIndex);
        ArrayList<Item> items = arrayItemStringIdToArrayItem(itemsString);
        Order order = new Order();
        order.setId(id);
        order.setDateCreation(stringLocalDateToLocalDate(dateCreation));
        order.setClient(clientServices.getClientById(id_client));
        order.setDelivered(delivered);
        order.setStatus(status);
        order.setItems(items);
        User user = new User();
        user.setId(id_adm);
        User searchedUser = userServices.getUserById(user.getId());
        order.setAdministrator((Administrator) userServices.getUserInDomainType(searchedUser));
        order.setTotal(new BigDecimal(total));
        if (delivered == Constants.Order.NOT_DELIVERED && !dateDelivery.isEmpty()){
            order.setDateDelivery(stringLocalDateToLocalDate(dateDelivery));
        }
        return order;
    }

    public void disableOrder(Order order){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractOrder.COLUMN_DATE_CREATION, order.getDateCreation().toString());
        values.put(ContractOrder.COLUMN_ID_CLIENT, order.getClient().getId());
        values.put(ContractOrder.COLUMN_ID_ADM, order.getAdministrator().getUser().getId());
        values.put(ContractOrder.COLUMN_TOTAL, order.getTotal().toString());
        values.put(ContractOrder.COLUMN_DELIVERED, order.getDelivered());
        values.put(ContractOrder.COLUMN_DATE_DELIVERY, order.getDateDelivery().toString());
        values.put(ContractOrder.COLUMN_STATUS, Constants.Status.INACTIVE);
        String selection = ContractOrder._ID+" = ?";
        String[] selectionArgs = {String.valueOf(order.getId())};
        db.update(ContractOrder.TABLE_NAME, values, selection, selectionArgs);
    }

    private LocalDate stringLocalDateToLocalDate(String text){
        String dateCreationList[] = text.split("-");
        int dateCreationDay = Integer.parseInt(dateCreationList[2]);
        int dateCreationMonth = Integer.parseInt(dateCreationList[1]);
        int dateCreationYear = Integer.parseInt(dateCreationList[0]);
        LocalDate localDate = LocalDate.of(dateCreationYear, dateCreationMonth, dateCreationDay);
        return localDate;
    }
}
