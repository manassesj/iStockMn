package bsi.mpoo.istock.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import bsi.mpoo.istock.data.address.ContractAddress;
import bsi.mpoo.istock.data.client.ContractClient;
import bsi.mpoo.istock.data.item.ContractItem;
import bsi.mpoo.istock.data.order.ContractOrder;
import bsi.mpoo.istock.data.product.ContractProduct;
import bsi.mpoo.istock.data.session.ContractSession;
import bsi.mpoo.istock.data.user.ContractUser;

public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, Contract.DATABASE_NAME, null, Contract.DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ContractUser.SQL_CREATE_TABLE_USER);
        db.execSQL(ContractClient.SQL_CREATE_TABLE_CLIENT);
        db.execSQL(ContractAddress.SQL_CREATE_TABLE_ADDRESS);
        db.execSQL(ContractProduct.SQL_CREATE_TABLE_PRODUCT);
        db.execSQL(ContractSession.SQL_CREATE_TABLE_SESSION);
        db.execSQL(ContractOrder.SQL_CREATE_TABLE_ORDER);
        db.execSQL(ContractItem.SQL_CREATE_TABLE_ITEMS);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(ContractUser.SQL_DELETE_USERS);
        db.execSQL(ContractClient.SQL_DELETE_CLIENTS);
        db.execSQL(ContractAddress.SQL_DELETE_ADDRESS);
        db.execSQL(ContractProduct.SQL_DELETE_PRODUCTS);
        db.execSQL(ContractSession.SQL_DELETE_SESSION);
        db.execSQL(ContractOrder.SQL_DELETE_ORDERS);
        db.execSQL(ContractItem.SQL_DELETE_ITEMS);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

}
