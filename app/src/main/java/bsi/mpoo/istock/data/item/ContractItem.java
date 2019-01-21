package bsi.mpoo.istock.data.item;

import android.provider.BaseColumns;
import bsi.mpoo.istock.data.order.ContractOrder;
import bsi.mpoo.istock.data.product.ContractProduct;
import bsi.mpoo.istock.data.user.ContractUser;

public class ContractItem implements BaseColumns {
    private ContractItem(){}

    public static final String TABLE_NAME = "items";
    public static final String COLUMN_ID_PRODUCT = "id_product";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_QUANTITY = "quantity";
    public static final String COLUMN_ID_ORDER = "id_order";
    public static final String COLUMN_ID_ADM = "id_administrator";
    public static final String COLUMN_STATUS = "status";
    public static final String SQL_CREATE_TABLE_ITEMS =
            "CREATE TABLE "+ContractItem.TABLE_NAME+" ("+
                    ContractItem._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    ContractItem.COLUMN_ID_PRODUCT + " INTEGER,"+
                    ContractItem.COLUMN_PRICE + " TEXT,"+
                    ContractItem.COLUMN_QUANTITY + " INTEGER,"+
                    ContractItem.COLUMN_ID_ORDER + " INTEGER,"+
                    ContractItem.COLUMN_ID_ADM + " INTEGER,"+
                    ContractItem.COLUMN_STATUS + " INTEGER,"+
                    "FOREIGN KEY("+ContractItem.COLUMN_ID_PRODUCT+") REFERENCES "+
                    ContractProduct.TABLE_NAME+" ("+ContractProduct._ID+"),"+
                    "FOREIGN KEY("+ContractItem.COLUMN_ID_ORDER+") REFERENCES "+
                    ContractOrder.TABLE_NAME+" ("+ContractOrder._ID+"),"+
                    "FOREIGN KEY("+ContractItem.COLUMN_ID_ADM+") REFERENCES "+
                    ContractUser.TABLE_NAME+" ("+ContractUser._ID+")"+
                    ")";
    public static  final String SQL_DELETE_ITEMS =
            "DROP TABLE IF EXISTS "+ ContractItem.TABLE_NAME;
}
