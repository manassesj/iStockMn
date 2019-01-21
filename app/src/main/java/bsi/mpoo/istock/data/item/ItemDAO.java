package bsi.mpoo.istock.data.item;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import bsi.mpoo.istock.data.DbHelper;
import bsi.mpoo.istock.domain.Item;
import bsi.mpoo.istock.domain.Order;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.product.ProductServices;

public class ItemDAO {

    private Context context;
    public ItemDAO(Context context){
        this.context = context;
    }

    public void insertItem(ArrayList<Item> items){
        for (Item item:items) {
            insertItem(item);
        }
    }

    public void insertItem(Item item) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractItem.COLUMN_ID_PRODUCT, item.getProduct().getId());
        values.put(ContractItem.COLUMN_PRICE, item.getPrice().toString());
        values.put(ContractItem.COLUMN_QUANTITY, item.getQuantity());
        values.put(ContractItem.COLUMN_ID_ORDER, item.getIdOrder());
        values.put(ContractItem.COLUMN_ID_ADM, item.getIdAdministrator());
        values.put(ContractItem.COLUMN_STATUS, Constants.Status.ACTIVE);
        long newRowID = db.insert(ContractItem.TABLE_NAME, null, values);
        item.setId(newRowID);
        db.close();
    }

    public Item getItemById(long id) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Item searchedItem = null;
        String[] projection = {
                BaseColumns._ID,
                ContractItem.COLUMN_ID_PRODUCT,
                ContractItem.COLUMN_PRICE,
                ContractItem.COLUMN_QUANTITY,
                ContractItem.COLUMN_ID_ORDER,
                ContractItem.COLUMN_ID_ADM,
                ContractItem.COLUMN_STATUS
        };
        String selection = ContractItem._ID+" = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(
                ContractItem.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.getCount()==1 && cursor.moveToNext()){
            searchedItem = createItem(cursor);
        }
        cursor.close();
        db.close();
        return searchedItem;
    }

    public List<Item> getItemsOrderById(Order order, String orderASCorDESC) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContractItem.COLUMN_ID_PRODUCT,
                ContractItem.COLUMN_PRICE,
                ContractItem.COLUMN_QUANTITY,
                ContractItem.COLUMN_ID_ORDER,
                ContractItem.COLUMN_ID_ADM,
                ContractItem.COLUMN_STATUS
        };
        String sortOrder = ContractItem.COLUMN_PRICE +" "+ orderASCorDESC;
        List<Item> itemList = new ArrayList<>();
        String selection = ContractItem.COLUMN_ID_ORDER+" = ?";
        String[] selectionArgs = { String.valueOf(order.getId()) };
        Cursor cursor = db.query(
                ContractItem.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        if (cursor.moveToNext()){
            do {
                Item item = createItem(cursor);
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    public List<Item> getActiveItemsByOrderId(Order order, String orderASCorDESC) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContractItem.COLUMN_ID_PRODUCT,
                ContractItem.COLUMN_PRICE,
                ContractItem.COLUMN_QUANTITY,
                ContractItem.COLUMN_ID_ORDER,
                ContractItem.COLUMN_ID_ADM,
                ContractItem.COLUMN_STATUS
        };
        String sortOrder = ContractItem.COLUMN_PRICE +" "+ orderASCorDESC;
        List<Item> itemList = new ArrayList<>();
        String selection = ContractItem.COLUMN_ID_ORDER+" = ?"+" AND "+
                ContractItem.COLUMN_STATUS+" = ?";
        String[] selectionArgs = { String.valueOf(order.getId()),
                String.valueOf(Constants.Status.ACTIVE)};
        Cursor cursor = db.query(
                ContractItem.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        if (cursor.moveToFirst()){
            do {
                Item item = createItem(cursor);
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    public void disableItem(Item item){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractItem.COLUMN_ID_PRODUCT, item.getProduct().getId());
        values.put(ContractItem.COLUMN_PRICE, item.getPrice().toString());
        values.put(ContractItem.COLUMN_QUANTITY, item.getQuantity());
        values.put(ContractItem.COLUMN_ID_ORDER, item.getIdOrder());
        values.put(ContractItem.COLUMN_ID_ADM, item.getIdAdministrator());
        values.put(ContractItem.COLUMN_STATUS, Constants.Status.INACTIVE);
        String selection = ContractItem._ID+" = ?";
        String[] selectionArgs = {String.valueOf(item.getId())};
        db.update(ContractItem.TABLE_NAME, values, selection, selectionArgs);
    }

    public void updateItem(Item item){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractItem.COLUMN_ID_PRODUCT, item.getProduct().getId());
        values.put(ContractItem.COLUMN_PRICE, item.getPrice().toString());
        values.put(ContractItem.COLUMN_QUANTITY, item.getQuantity());
        values.put(ContractItem.COLUMN_ID_ORDER, item.getIdOrder());
        values.put(ContractItem.COLUMN_ID_ADM, item.getIdAdministrator());
        values.put(ContractItem.COLUMN_STATUS, item.getStatus());
        String selection = ContractItem._ID+" = ?";
        String[] selectionArgs = {String.valueOf(item.getId())};
        db.update(ContractItem.TABLE_NAME, values, selection, selectionArgs);
    }


    private Item createItem(Cursor cursor){

        int idIndex = cursor.getColumnIndexOrThrow(ContractItem._ID);
        int productIndex = cursor.getColumnIndexOrThrow(ContractItem.COLUMN_ID_PRODUCT);
        int priceIndex = cursor.getColumnIndexOrThrow(ContractItem.COLUMN_PRICE);
        int quantityIndex = cursor.getColumnIndexOrThrow(ContractItem.COLUMN_QUANTITY);
        int orderIndex = cursor.getColumnIndexOrThrow(ContractItem.COLUMN_ID_ORDER);
        int admIndex = cursor.getColumnIndexOrThrow(ContractItem.COLUMN_ID_ADM);
        int statusIndex = cursor.getColumnIndexOrThrow(ContractItem.COLUMN_STATUS);
        long id = cursor.getLong(idIndex);
        long idProduct = cursor.getLong(productIndex);
        String price = cursor.getString(priceIndex);
        long quantity = cursor.getLong(quantityIndex);
        long idOrder = cursor.getLong(orderIndex);
        long idAdm = cursor.getLong(admIndex);
        int status = cursor.getInt(statusIndex);
        Item item = new Item();
        item.setId(id);
        item.setPrice(new BigDecimal(price));
        item.setQuantity(quantity);
        item.setIdOrder(idOrder);
        item.setStatus(status);
        item.setIdAdministrator(idAdm);
        ProductServices productServices = new ProductServices(context);
        item.setProduct(productServices.getProductById(idProduct));
        return item;
    }
}
