package bsi.mpoo.istock.data.user;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import bsi.mpoo.istock.data.Contract;
import bsi.mpoo.istock.data.DbHelper;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.services.Constants;

public class UserDAO{
    private Context context;
    public UserDAO(Context context){
        this.context = context;
    }

    public void insertUser(User user) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractUser.COLUMN_NAME, user.getName().toUpperCase());
        values.put(ContractUser.COLUMN_EMAIL, user.getEmail().toUpperCase());
        values.put(ContractUser.COLUMN_PASSWORD, user.getPassword());
        values.put(ContractUser.COLUMN_TYPE, user.getType());
        values.put(ContractUser.COLUMN_STATUS, user.getStatus());
        values.put(ContractUser.COLUMN_COMPANY, user.getCompany());
        values.put(ContractUser.COLUMN_ADMINISTRATOR, user.getAdministrator());
        values.put(ContractUser.COLUMN_IMAGE, user.getImage());

        long newRowID = db.insert(ContractUser.TABLE_NAME, null, values);

        user.setId(newRowID);
        db.close();
    }

    public User getUserByEmail(String email) {

        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        User searchedUser = null;
        String[] projection = {
                BaseColumns._ID,
                ContractUser.COLUMN_NAME,
                ContractUser.COLUMN_EMAIL,
                ContractUser.COLUMN_PASSWORD,
                ContractUser.COLUMN_TYPE,
                ContractUser.COLUMN_STATUS,
                ContractUser.COLUMN_COMPANY,
                ContractUser.COLUMN_ADMINISTRATOR,
                ContractUser.COLUMN_IMAGE
        };

        String selection = ContractUser.COLUMN_EMAIL+" = ?";
        String[] selectionArgs = { email.trim().toUpperCase()};

        Cursor cursor = db.query(
                ContractUser.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.getCount()==1){
            cursor.moveToNext();
            searchedUser = createUser(cursor);
        }

        cursor.close();
        db.close();
        return searchedUser;

    }

    public User getUserById(long id) {

        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        User searchedUser = null;
        String[] projection = {
                BaseColumns._ID,
                ContractUser.COLUMN_NAME,
                ContractUser.COLUMN_EMAIL,
                ContractUser.COLUMN_PASSWORD,
                ContractUser.COLUMN_TYPE,
                ContractUser.COLUMN_STATUS,
                ContractUser.COLUMN_COMPANY,
                ContractUser.COLUMN_ADMINISTRATOR,
                ContractUser.COLUMN_IMAGE
        };

        String selection = ContractUser._ID+" = ?";
        String[] selectionArgs = { String.valueOf(id)};

        Cursor cursor = db.query(
                ContractUser.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor.getCount()==1){
            cursor.moveToNext();
            searchedUser = createUser(cursor);
        }

        cursor.close();
        db.close();
        return searchedUser;

    }

    public List<User> getUsersByAdmId(Administrator administrator, String order){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContractUser.COLUMN_NAME,
                ContractUser.COLUMN_EMAIL,
                ContractUser.COLUMN_PASSWORD,
                ContractUser.COLUMN_TYPE,
                ContractUser.COLUMN_STATUS,
                ContractUser.COLUMN_COMPANY,
                ContractUser.COLUMN_ADMINISTRATOR,
                ContractUser.COLUMN_IMAGE
        };
        String sortOrder = ContractUser.COLUMN_NAME +" "+ order;
        List<User> userList = new ArrayList<>();
        String selection = ContractUser.COLUMN_ADMINISTRATOR+" = ?";
        String[] selectionArgs = { String.valueOf(administrator.getUser().getId()) };
        Cursor cursor = db.query(
                ContractUser.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        if (cursor.moveToNext()){
            do {
                User newUser = createUser(cursor);
                userList.add(newUser);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    public List<User> getActiveUsersByAdmId(Administrator administrator, String order){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContractUser.COLUMN_NAME,
                ContractUser.COLUMN_EMAIL,
                ContractUser.COLUMN_PASSWORD,
                ContractUser.COLUMN_TYPE,
                ContractUser.COLUMN_STATUS,
                ContractUser.COLUMN_COMPANY,
                ContractUser.COLUMN_ADMINISTRATOR,
                ContractUser.COLUMN_IMAGE
        };
        String sortOrder = ContractUser.COLUMN_NAME +" "+ order;
        List<User> userList = new ArrayList<>();
        String selection = ContractUser.COLUMN_ADMINISTRATOR+" = ?"+" AND "+
                ContractUser.COLUMN_STATUS+" = ?";
        String[] selectionArgs = { String.valueOf(administrator.getUser().getId()),
                String.valueOf(Constants.Status.ACTIVE)};
        Cursor cursor = db.query(
                ContractUser.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        if (cursor.moveToNext()){
            do {
                User newUser = createUser(cursor);
                userList.add(newUser);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userList;
    }

    private User createUser(Cursor cursor){

        int idIndex = cursor.getColumnIndexOrThrow(ContractUser._ID);
        int nameIndex = cursor.getColumnIndexOrThrow(ContractUser.COLUMN_NAME);
        int emailIndex = cursor.getColumnIndexOrThrow(ContractUser.COLUMN_EMAIL);
        int passwordIndex = cursor.getColumnIndexOrThrow(ContractUser.COLUMN_PASSWORD);
        int typeIndex = cursor.getColumnIndexOrThrow(ContractUser.COLUMN_TYPE);
        int statusIndex = cursor.getColumnIndexOrThrow(ContractUser.COLUMN_STATUS);
        int companyIndex = cursor.getColumnIndexOrThrow(ContractUser.COLUMN_COMPANY);
        int administratorIndex = cursor.getColumnIndexOrThrow(ContractUser.COLUMN_ADMINISTRATOR);
        int imageIndex = cursor.getColumnIndexOrThrow(ContractUser.COLUMN_IMAGE);

        long id = cursor.getInt(idIndex);
        String name = cursor.getString(nameIndex);
        String email = cursor.getString(emailIndex);
        String password = cursor.getString(passwordIndex);
        int type = cursor.getInt(typeIndex);
        int status = cursor.getInt(statusIndex);
        String company = cursor.getString(companyIndex);
        long administrator = cursor.getLong(administratorIndex);
        byte[] image = cursor.getBlob(imageIndex);

        User createdUser = new User();
        createdUser.setId(id);
        createdUser.setName(name);
        createdUser.setEmail(email);
        createdUser.setPassword(password);
        createdUser.setType(type);
        createdUser.setStatus(status);
        createdUser.setCompany(company);
        createdUser.setAdministrator(administrator);
        createdUser.setImage(image);

        return createdUser;

    }

    public void updateUser(User user){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractUser.COLUMN_NAME, user.getName().toUpperCase());
        values.put(ContractUser.COLUMN_EMAIL, user.getEmail().toUpperCase());
        values.put(ContractUser.COLUMN_PASSWORD, user.getPassword());
        values.put(ContractUser.COLUMN_TYPE, user.getType());
        values.put(ContractUser.COLUMN_STATUS, user.getStatus());
        values.put(ContractUser.COLUMN_COMPANY, user.getCompany());
        values.put(ContractUser.COLUMN_ADMINISTRATOR, user.getAdministrator());
        values.put(ContractUser.COLUMN_IMAGE, user.getImage());
        String selection = ContractUser._ID+" = ?";
        String[] selectionArgs = {String.valueOf(user.getId())};
        db.update(ContractUser.TABLE_NAME, values, selection, selectionArgs);
    }

    public void disableUser(User user){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractUser.COLUMN_NAME, user.getName().toUpperCase());
        values.put(ContractUser.COLUMN_EMAIL, user.getEmail().toUpperCase());
        values.put(ContractUser.COLUMN_PASSWORD, user.getPassword());
        values.put(ContractUser.COLUMN_TYPE, user.getType());
        values.put(ContractUser.COLUMN_STATUS, Constants.Status.INACTIVE);
        values.put(ContractUser.COLUMN_COMPANY, user.getCompany());
        values.put(ContractUser.COLUMN_ADMINISTRATOR, user.getAdministrator());
        values.put(ContractUser.COLUMN_IMAGE, user.getImage());
        String selection = ContractUser._ID+" = ?";
        String[] selectionArgs = {String.valueOf(user.getId())};
        db.update(ContractUser.TABLE_NAME, values, selection, selectionArgs);
    }



}
