package bsi.mpoo.istock.data.address;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import bsi.mpoo.istock.data.DbHelper;
import bsi.mpoo.istock.domain.Address;
import bsi.mpoo.istock.services.Constants;

public class AddressDAO {
    private Context context;
    public AddressDAO(Context context){
        this.context = context;
    }

    public void insertAddress(Address address) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractAddress.COLUMN_STREET, address.getStreet());
        values.put(ContractAddress.COLUMN_NUMBER, address.getNumber());
        values.put(ContractAddress.COLUMN_DISTRICT, address.getDistrict());
        values.put(ContractAddress.COLUMN_CITY, address.getCity());
        values.put(ContractAddress.COLUMN_STATE, address.getState());
        values.put(ContractAddress.COLUMN_STATUS, address.getStatus());
        long newRowID = db.insert(ContractAddress.TABLE_NAME, null, values);
        address.setId(newRowID);
        db.close();
    }

    public Address getAddressByID(Address address) {

        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Address searchedAddress = null;
        String[] projection = {
                BaseColumns._ID,
                ContractAddress.COLUMN_STREET,
                ContractAddress.COLUMN_NUMBER,
                ContractAddress.COLUMN_DISTRICT,
                ContractAddress.COLUMN_CITY,
                ContractAddress.COLUMN_STATE,
                ContractAddress.COLUMN_STATUS
        };

        String selection = ContractAddress._ID+" = ?";
        String[] selectionArgs = { String.valueOf(address.getId()) };
        Cursor cursor = db.query(
                ContractAddress.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.getCount()==1){
            cursor.moveToNext();
            searchedAddress = createAddress(cursor);
        }
        cursor.close();
        db.close();
        return searchedAddress;
    }

    public void disableAddress(Address address){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractAddress.COLUMN_NUMBER, address.getNumber());
        values.put(ContractAddress.COLUMN_STREET, address.getStreet());
        values.put(ContractAddress.COLUMN_DISTRICT, address.getDistrict());
        values.put(ContractAddress.COLUMN_CITY, address.getCity());
        values.put(ContractAddress.COLUMN_STATE, address.getState());
        values.put(ContractAddress.COLUMN_STATUS,Constants.Status.INACTIVE );
        String selection = ContractAddress._ID+" = ?";
        String[] selectionArgs = {String.valueOf(address.getId())};
        db.update(ContractAddress.TABLE_NAME, values, selection, selectionArgs);
    }

    public void updateAddress(Address address){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractAddress.COLUMN_NUMBER, address.getNumber());
        values.put(ContractAddress.COLUMN_STREET, address.getStreet());
        values.put(ContractAddress.COLUMN_DISTRICT, address.getDistrict());
        values.put(ContractAddress.COLUMN_CITY, address.getCity());
        values.put(ContractAddress.COLUMN_STATE, address.getState());
        values.put(ContractAddress.COLUMN_STATUS, address.getStatus());
        String selection = ContractAddress._ID+" = ?";
        String[] selectionArgs = {String.valueOf(address.getId())};
        db.update(ContractAddress.TABLE_NAME, values, selection, selectionArgs);
    }

    private Address createAddress(Cursor cursor){
        int idIndex = cursor.getColumnIndexOrThrow(ContractAddress._ID);
        int streetIndex = cursor.getColumnIndexOrThrow(ContractAddress.COLUMN_STREET);
        int numberIndex = cursor.getColumnIndexOrThrow(ContractAddress.COLUMN_NUMBER);
        int districtIndex = cursor.getColumnIndexOrThrow(ContractAddress.COLUMN_DISTRICT);
        int cityIndex = cursor.getColumnIndexOrThrow(ContractAddress.COLUMN_CITY);
        int stateIndex = cursor.getColumnIndexOrThrow(ContractAddress.COLUMN_STATE);
        int statusIndex = cursor.getColumnIndexOrThrow(ContractAddress.COLUMN_STATUS);
        long id = cursor.getInt(idIndex);
        String street = cursor.getString(streetIndex);
        int number = cursor.getInt(numberIndex);
        String district = cursor.getString(districtIndex);
        String city = cursor.getString(cityIndex);
        String state = cursor.getString(stateIndex);
        int status = cursor.getInt(statusIndex);
        Address createdAddress = new Address();
        createdAddress.setId(id);
        createdAddress.setStreet(street);
        createdAddress.setNumber(number);
        createdAddress.setDistrict(district);
        createdAddress.setCity(city);
        createdAddress.setState(state);
        createdAddress.setStatus(status);
        return createdAddress;
    }
}
