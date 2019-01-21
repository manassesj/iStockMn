package bsi.mpoo.istock.data.product;

import android.provider.BaseColumns;

import bsi.mpoo.istock.data.user.ContractUser;

public class ContractProduct  implements BaseColumns{

    private ContractProduct(){}

    public static final String TABLE_NAME = "products";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_MINIMUM_QUANTITY = "minimum_quantity";
    public static final String COLUMN_ID_ADM = "id_administrator";
    public static final String COLUMN_STATUS = "status";
    public static final String SQL_CREATE_TABLE_PRODUCT =
            "CREATE TABLE "+ContractProduct.TABLE_NAME+" ("+
            ContractProduct._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
            ContractProduct.COLUMN_NAME + " TEXT,"+
            ContractProduct.COLUMN_PRICE + " TEXT,"+
            ContractProduct.COLUMN_QUANTITY + " INTEGER,"+
            ContractProduct.COLUMN_MINIMUM_QUANTITY + " INTEGER,"+
            ContractProduct.COLUMN_ID_ADM + " INTEGER,"+
            ContractProduct.COLUMN_STATUS + " INTEGER,"+
            "FOREIGN KEY("+COLUMN_ID_ADM+") REFERENCES "+
            ContractUser.TABLE_NAME+" ("+ContractUser._ID+")"+
            ")";
    public static  final String SQL_DELETE_PRODUCTS =
            "DROP TABLE IF EXISTS "+ ContractProduct.TABLE_NAME;


}
