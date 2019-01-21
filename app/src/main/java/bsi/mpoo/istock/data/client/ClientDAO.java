package bsi.mpoo.istock.data.client;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import java.util.ArrayList;
import java.util.List;
import bsi.mpoo.istock.data.Contract;
import bsi.mpoo.istock.data.DbHelper;
import bsi.mpoo.istock.data.address.AddressDAO;
import bsi.mpoo.istock.data.user.UserDAO;
import bsi.mpoo.istock.domain.Address;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Client;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.user.UserServices;

public class ClientDAO {
    private Context context;
    public ClientDAO(Context context){
        this.context = context;
    }

    public void insertClient(Client client) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractClient.COLUMN_NAME, client.getName());
        values.put(ContractClient.COLUMN_PHONE, client.getPhone());
        values.put(ContractClient.COLUMN_ID_ADM, client.getAdministrator().getUser().getId());
        values.put(ContractClient.COLUMN_STATUS, client.getStatus());
        AddressDAO addressDAO = new AddressDAO(context);
        addressDAO.insertAddress(client.getAddress());
        values.put(ContractClient.COLUMN_ID_ADDRESS, client.getAddress().getId());
        long newRowID = db.insert(ContractClient.TABLE_NAME, null, values);
        client.setId(newRowID);
        db.close();
    }

    public Client getClientByName(String name, Administrator administrator) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Client searchedClient = null;
        String[] projection = {
                BaseColumns._ID,
                ContractClient.COLUMN_NAME,
                ContractClient.COLUMN_PHONE,
                ContractClient.COLUMN_ID_ADDRESS,
                ContractClient.COLUMN_ID_ADM,
                ContractClient.COLUMN_STATUS,
        };
        String selection = ContractClient.COLUMN_NAME+" = ?"+" AND "+
                ContractClient.COLUMN_ID_ADM+" =?";
        String[] selectionArgs = { name.trim().toUpperCase(),
                String.valueOf(administrator.getUser().getId()) };
        Cursor cursor = db.query(
                ContractClient.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.getCount()==1){
            cursor.moveToNext();
            searchedClient = createClient(cursor);
        }
        cursor.close();
        db.close();
        return searchedClient;
    }

    public Client getClientById(long id) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Client searchedClient = null;
        String[] projection = {
                BaseColumns._ID,
                ContractClient.COLUMN_NAME,
                ContractClient.COLUMN_PHONE,
                ContractClient.COLUMN_ID_ADDRESS,
                ContractClient.COLUMN_ID_ADM,
                ContractClient.COLUMN_STATUS
        };
        String selection = ContractClient._ID+" = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(
                ContractClient.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.getCount()==1){
            cursor.moveToNext();
            searchedClient = createClient(cursor);
        }
        cursor.close();
        db.close();
        return searchedClient;
    }

    public List<Client> getListClientsByAdmId(Administrator administrator, String order) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContractClient.COLUMN_NAME,
                ContractClient.COLUMN_PHONE,
                ContractClient.COLUMN_ID_ADDRESS,
                ContractClient.COLUMN_ID_ADM,
                ContractClient.COLUMN_STATUS
        };
        String sortOrder = ContractClient.COLUMN_NAME +" "+ Contract.ASC;
        List<Client> clientList = new ArrayList<>();
        String selection = ContractClient.COLUMN_ID_ADM+" = ?";
        String[] selectionArgs = { String.valueOf(administrator.getUser().getId()) };
        Cursor cursor = db.query(
                ContractClient.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        if (cursor.moveToNext()){
            do {
                Client client = createClient(cursor);
                clientList.add(client);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return clientList;
    }

    public List<Client> getActiveClientsByAdmId(Administrator administrator, String order) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContractClient.COLUMN_NAME,
                ContractClient.COLUMN_PHONE,
                ContractClient.COLUMN_ID_ADDRESS,
                ContractClient.COLUMN_ID_ADM,
                ContractClient.COLUMN_STATUS
        };
        String sortOrder = ContractClient.COLUMN_NAME +" "+ order;
        List<Client> clientList = new ArrayList<>();
        String selection = ContractClient.COLUMN_ID_ADM+" = ?"+" AND "+
                ContractClient.COLUMN_STATUS+" = ?";
        String[] selectionArgs = { String.valueOf(administrator.getUser().getId()),
                String.valueOf(Constants.Status.ACTIVE)};
        Cursor cursor = db.query(
                ContractClient.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        if (cursor.moveToNext()){
            do {
                Client client = createClient(cursor);
                clientList.add(client);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return clientList;
    }

    public void disableClient(Client client){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        AddressDAO addressDAO = new AddressDAO(context);
        addressDAO.disableAddress(client.getAddress());
        ContentValues values = new ContentValues();
        values.put(ContractClient.COLUMN_NAME, client.getName());
        values.put(ContractClient.COLUMN_PHONE, client.getPhone());
        values.put(ContractClient.COLUMN_ID_ADM, client.getAdministrator().getUser().getId());
        values.put(ContractClient.COLUMN_ID_ADDRESS, client.getAddress().getId());
        values.put(ContractClient.COLUMN_STATUS, Constants.Status.INACTIVE);
        String selection = ContractClient._ID+" = ?";
        String[] selectionArgs = {String.valueOf(client.getId())};
        db.update(ContractClient.TABLE_NAME, values, selection, selectionArgs);
    }

    public void updateClient(Client client){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        AddressDAO addressDAO = new AddressDAO(context);
        addressDAO.updateAddress(client.getAddress());
        ContentValues values = new ContentValues();
        values.put(ContractClient.COLUMN_NAME, client.getName());
        values.put(ContractClient.COLUMN_PHONE, client.getPhone());
        values.put(ContractClient.COLUMN_ID_ADM, client.getAdministrator().getUser().getId());
        values.put(ContractClient.COLUMN_ID_ADDRESS, client.getAddress().getId());
        values.put(ContractClient.COLUMN_STATUS, client.getStatus());
        String selection = ContractClient._ID+" = ?";
        String[] selectionArgs = {String.valueOf(client.getId())};
        db.update(ContractClient.TABLE_NAME, values, selection, selectionArgs);
    }

    private Client createClient(Cursor cursor){
        int idIndex = cursor.getColumnIndexOrThrow(ContractClient._ID);
        int nameIndex = cursor.getColumnIndexOrThrow(ContractClient.COLUMN_NAME);
        int phoneIndex = cursor.getColumnIndexOrThrow(ContractClient.COLUMN_PHONE);
        int idAddressIndex = cursor.getColumnIndexOrThrow(ContractClient.COLUMN_ID_ADDRESS);
        int admIndex = cursor.getColumnIndexOrThrow(ContractClient.COLUMN_ID_ADM);
        int statusIndex = cursor.getColumnIndexOrThrow(ContractClient.COLUMN_STATUS);
        long id = cursor.getLong(idIndex);
        String name = cursor.getString(nameIndex);
        String phone = cursor.getString(phoneIndex);
        int idAddress = cursor.getInt(idAddressIndex);
        long idAdm = cursor.getLong(admIndex);
        int status = cursor.getInt(statusIndex);
        Client createdClient = new Client();
        createdClient.setId(id);
        createdClient.setName(name);
        createdClient.setPhone(phone);
        createdClient.setStatus(status);
        AddressDAO addressDAO = new AddressDAO(context);
        Address address = new Address();
        address.setId(idAddress);
        Address searchedAddress = addressDAO.getAddressByID(address);
        createdClient.setAddress(searchedAddress);
        UserDAO userDAO = new UserDAO(context);
        User user = new User();
        user.setId(idAdm);
        User searchedUser = userDAO.getUserById(user.getId());
        UserServices userServices = new UserServices(context);
        createdClient.setAdministrator((Administrator) userServices.getUserInDomainType(searchedUser));
        return createdClient;

    }
}
