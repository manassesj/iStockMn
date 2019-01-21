package bsi.mpoo.istock.data.session;

import android.provider.BaseColumns;

import bsi.mpoo.istock.data.user.ContractUser;

public class ContractSession implements BaseColumns {

    private ContractSession(){}

    public static final String TABLE_NAME = "session";
    public static final String ID_USER = "id_user";
    public static final String COLUMN_REMEMBER = "remember";

    public static final String SQL_CREATE_TABLE_SESSION =
            "CREATE TABLE "+ContractSession.TABLE_NAME+" ("+
                    ContractSession._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    ContractSession.ID_USER + " INTEGER,"+
                    ContractSession.COLUMN_REMEMBER + " INTEGER "+
                    ")";


    public static  final String SQL_DELETE_SESSION =
            "DROP TABLE IF EXISTS "+ ContractSession.TABLE_NAME;
}
