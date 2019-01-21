package bsi.mpoo.istock.data.client;

import android.provider.BaseColumns;

import bsi.mpoo.istock.data.address.ContractAddress;
import bsi.mpoo.istock.data.user.ContractUser;

public class ContractClient implements BaseColumns {

    private ContractClient(){}

    public static final String TABLE_NAME = "clients";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_ID_ADDRESS = "id_address";
    public static final String COLUMN_ID_ADM = "id_administrator";
    public static final String COLUMN_STATUS = "status";
    public static final String SQL_CREATE_TABLE_CLIENT =
            "CREATE TABLE "+ContractClient.TABLE_NAME+" ("+
                    ContractClient._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    ContractClient.COLUMN_NAME + " TEXT,"+
                    ContractClient.COLUMN_PHONE + " TEXT,"+
                    ContractClient.COLUMN_ID_ADDRESS + " INTEGER,"+
                    ContractClient.COLUMN_ID_ADM + " INTEGER,"+
                    ContractClient.COLUMN_STATUS + " INTEGER,"+
                    "FOREIGN KEY("+COLUMN_ID_ADDRESS+") REFERENCES "+
                    ContractAddress.TABLE_NAME+" ("+ContractAddress._ID+"),"+
                    "FOREIGN KEY("+ContractClient.COLUMN_ID_ADM +") REFERENCES "+
                    ContractUser.TABLE_NAME+" ("+ContractUser._ID+")"+
                    ")";
    public static  final String SQL_DELETE_CLIENTS =
            "DROP TABLE IF EXISTS "+ ContractClient.TABLE_NAME;


}
