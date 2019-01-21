package bsi.mpoo.istock.data.user;

import android.provider.BaseColumns;

public class ContractUser  implements BaseColumns {

    private ContractUser(){}

    public static final String TABLE_NAME = "users";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_STATUS = "status";
    public static final String COLUMN_COMPANY = "company";
    public static final String COLUMN_ADMINISTRATOR = "administrator";
    public static final String COLUMN_IMAGE = "image";

    public static final String SQL_CREATE_TABLE_USER =
            "CREATE TABLE "+ContractUser.TABLE_NAME+" ("+
                    ContractUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    ContractUser.COLUMN_NAME + " TEXT,"+
                    ContractUser.COLUMN_EMAIL + " TEXT,"+
                    ContractUser.COLUMN_PASSWORD + " TEXT,"+
                    ContractUser.COLUMN_TYPE + " INTEGER,"+
                    ContractUser.COLUMN_STATUS + " INTEGER,"+
                    ContractUser.COLUMN_COMPANY + " TEXT,"+
                    ContractUser.COLUMN_ADMINISTRATOR + " INTEGER," +
                    ContractUser.COLUMN_IMAGE + " BLOB"+
                    ")";


    public static  final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS "+ ContractUser.TABLE_NAME;


}
