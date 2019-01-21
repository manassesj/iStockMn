package bsi.mpoo.istock.data.address;

import android.provider.BaseColumns;

public class ContractAddress implements BaseColumns {

    private ContractAddress(){}

    public static final String TABLE_NAME = "address";
    public static final String COLUMN_STREET = "street";
    public static final String COLUMN_NUMBER = "number";
    public static final String COLUMN_DISTRICT = "district";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_STATUS = "status";
    public static final String SQL_CREATE_TABLE_ADDRESS =
            "CREATE TABLE "+ContractAddress.TABLE_NAME+" ("+
                    ContractAddress._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    ContractAddress.COLUMN_STREET + " TEXT,"+
                    ContractAddress.COLUMN_NUMBER + " INTEGER,"+
                    ContractAddress.COLUMN_DISTRICT + " TEXT,"+
                    ContractAddress.COLUMN_CITY + " TEXT,"+
                    ContractAddress.COLUMN_STATE + " TEXT," +
                    ContractAddress.COLUMN_STATUS + " INTEGER)";
    public static  final String SQL_DELETE_ADDRESS =
            "DROP TABLE IF EXISTS "+ ContractAddress.TABLE_NAME;


}
