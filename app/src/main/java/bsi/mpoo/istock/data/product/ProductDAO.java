package bsi.mpoo.istock.data.product;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import bsi.mpoo.istock.data.DbHelper;
import bsi.mpoo.istock.data.user.UserDAO;
import bsi.mpoo.istock.domain.Administrator;
import bsi.mpoo.istock.domain.Product;
import bsi.mpoo.istock.domain.User;
import bsi.mpoo.istock.services.Constants;
import bsi.mpoo.istock.services.user.UserServices;

public class ProductDAO {
    private Context context;
    public ProductDAO(Context context){
        this.context = context;
    }

    public void insertProduct(Product product) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractProduct.COLUMN_NAME, product.getName());
        values.put(ContractProduct.COLUMN_PRICE, product.getPrice().toString());
        values.put(ContractProduct.COLUMN_QUANTITY, product.getQuantity());
        values.put(ContractProduct.COLUMN_MINIMUM_QUANTITY, product.getMinimumQuantity());
        values.put(ContractProduct.COLUMN_ID_ADM, product.getAdministrator().getUser().getId());
        values.put(ContractProduct.COLUMN_STATUS, product.getStatus());
        long newRowID = db.insert(ContractProduct.TABLE_NAME, null, values);
        product.setId(newRowID);
        db.close();
    }

    public Product getProductByName(String name, Administrator administrator) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Product searchedProduct = null;
        String[] projection = {
                BaseColumns._ID,
                ContractProduct.COLUMN_NAME,
                ContractProduct.COLUMN_PRICE,
                ContractProduct.COLUMN_QUANTITY,
                ContractProduct.COLUMN_MINIMUM_QUANTITY,
                ContractProduct.COLUMN_ID_ADM,
                ContractProduct.COLUMN_STATUS
        };
        String selection = ContractProduct.COLUMN_NAME+" = ?"+" AND "+
                ContractProduct.COLUMN_ID_ADM+" =?";
        String[] selectionArgs = { name.trim().toUpperCase(),
                String.valueOf(administrator.getUser().getId()) };
        Cursor cursor = db.query(
                ContractProduct.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.getCount()==1){
            cursor.moveToNext();
            searchedProduct = createProduct(cursor);
        }
        cursor.close();
        db.close();
        return searchedProduct;
    }

    public Product getProductById(long id) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Product searchedProduct = null;
        String[] projection = {
                BaseColumns._ID,
                ContractProduct.COLUMN_NAME,
                ContractProduct.COLUMN_PRICE,
                ContractProduct.COLUMN_QUANTITY,
                ContractProduct.COLUMN_MINIMUM_QUANTITY,
                ContractProduct.COLUMN_ID_ADM,
                ContractProduct.COLUMN_STATUS
        };
        String selection = ContractProduct._ID+" = ?";
        String[] selectionArgs = { String.valueOf(id) };
        Cursor cursor = db.query(
                ContractProduct.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (cursor.getCount()==1 && cursor.moveToNext()){
            searchedProduct = createProduct(cursor);
        }
        cursor.close();
        db.close();
        return searchedProduct;
    }

    public List<Product> getProductsByAdmId(Administrator administrator, String order) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContractProduct.COLUMN_NAME,
                ContractProduct.COLUMN_PRICE,
                ContractProduct.COLUMN_QUANTITY,
                ContractProduct.COLUMN_MINIMUM_QUANTITY,
                ContractProduct.COLUMN_ID_ADM,
                ContractProduct.COLUMN_STATUS
        };
        String sortOrder = ContractProduct.COLUMN_NAME +" "+ order;
        List<Product> productList = new ArrayList<>();
        String selection = ContractProduct.COLUMN_ID_ADM+" = ?";
        String[] selectionArgs = { String.valueOf(administrator.getUser().getId()) };
        Cursor cursor = db.query(
                ContractProduct.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        if (cursor.moveToNext()){
            do {
                Product product = createProduct(cursor);
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public List<Product> getActiveProductsByAdmId(Administrator administrator, String order) {
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String[] projection = {
                BaseColumns._ID,
                ContractProduct.COLUMN_NAME,
                ContractProduct.COLUMN_PRICE,
                ContractProduct.COLUMN_QUANTITY,
                ContractProduct.COLUMN_MINIMUM_QUANTITY,
                ContractProduct.COLUMN_ID_ADM,
                ContractProduct.COLUMN_STATUS
        };
        String sortOrder = ContractProduct.COLUMN_NAME +" "+ order;
        List<Product> productList = new ArrayList<>();
        String selection = ContractProduct.COLUMN_ID_ADM+" = ?"+" AND "+
                ContractProduct.COLUMN_STATUS+" = ?";
        String[] selectionArgs = { String.valueOf(administrator.getUser().getId()),
                String.valueOf(Constants.Status.ACTIVE)};
        Cursor cursor = db.query(
                ContractProduct.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        if (cursor.moveToNext()){
            do {
                Product product = createProduct(cursor);
                productList.add(product);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return productList;
    }

    public void disableProduct(Product product){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractProduct.COLUMN_NAME, product.getName());
        values.put(ContractProduct.COLUMN_PRICE, product.getPrice().toString());
        values.put(ContractProduct.COLUMN_QUANTITY, product.getQuantity());
        values.put(ContractProduct.COLUMN_MINIMUM_QUANTITY, product.getMinimumQuantity());
        values.put(ContractProduct.COLUMN_ID_ADM, product.getAdministrator().getUser().getId());
        values.put(ContractProduct.COLUMN_STATUS, Constants.Status.INACTIVE);
        String selection = ContractProduct._ID+" = ?";
        String[] selectionArgs = {String.valueOf(product.getId())};
        db.update(ContractProduct.TABLE_NAME, values, selection, selectionArgs);
    }

    public void updateProduct(Product product){
        DbHelper mDbHelper = new DbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ContractProduct.COLUMN_NAME, product.getName());
        values.put(ContractProduct.COLUMN_PRICE, product.getPrice().toString());
        values.put(ContractProduct.COLUMN_QUANTITY, product.getQuantity());
        values.put(ContractProduct.COLUMN_MINIMUM_QUANTITY, product.getMinimumQuantity());
        values.put(ContractProduct.COLUMN_ID_ADM, product.getAdministrator().getUser().getId());
        values.put(ContractProduct.COLUMN_STATUS, product.getStatus());
        String selection = ContractProduct._ID+" = ?";
        String[] selectionArgs = {String.valueOf(product.getId())};
        db.update(ContractProduct.TABLE_NAME, values, selection, selectionArgs);
    }


    private Product createProduct(Cursor cursor){

        int idIndex = cursor.getColumnIndexOrThrow(ContractProduct._ID);
        int nameIndex = cursor.getColumnIndexOrThrow(ContractProduct.COLUMN_NAME);
        int priceIndex = cursor.getColumnIndexOrThrow(ContractProduct.COLUMN_PRICE);
        int quantityIndex = cursor.getColumnIndexOrThrow(ContractProduct.COLUMN_QUANTITY);
        int idMinimumIndex = cursor.getColumnIndexOrThrow(ContractProduct.COLUMN_MINIMUM_QUANTITY);
        int admIndex = cursor.getColumnIndexOrThrow(ContractProduct.COLUMN_ID_ADM);
        int statusIndex = cursor.getColumnIndexOrThrow(ContractProduct.COLUMN_STATUS);
        long id = cursor.getLong(idIndex);
        String name = cursor.getString(nameIndex);
        String price = cursor.getString(priceIndex);
        long quantity = cursor.getLong(quantityIndex);
        long minimum = cursor.getLong(idMinimumIndex);
        long idAdm = cursor.getLong(admIndex);
        int status = cursor.getInt(statusIndex);
        Product createdProduct = new Product();
        createdProduct.setId(id);
        createdProduct.setName(name);
        createdProduct.setPrice(new BigDecimal(price));
        createdProduct.setQuantity(quantity);
        createdProduct.setMinimumQuantity(minimum);
        createdProduct.setStatus(status);
        UserDAO userDAO = new UserDAO(context);
        User user = new User();
        user.setId(idAdm);
        User searchedUser = userDAO.getUserById(user.getId());
        UserServices userServices = new UserServices(context);
        createdProduct.setAdministrator((Administrator) userServices.getUserInDomainType(searchedUser));
        return createdProduct;
    }
}

