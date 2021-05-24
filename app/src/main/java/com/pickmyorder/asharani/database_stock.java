package com.pickmyorder.asharani;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class database_stock extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 6;

    // Database Name
    private static final String DATABASE_NAME = "stock_items1";


    public database_stock(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /*public database_stock(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }*/

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(ModelStock.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + ModelStock.TABLE_NAME);

        // Create tables again
        onCreate(db);

    }

    public long insertStock(ModelStock modelStock) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ModelStock.COLUMN_PRODCUT_ID, modelStock.pro_id);
        values.put(ModelStock.COLUMN_QUANTITY, modelStock.ordered_qty);
        values.put(ModelStock.COLUMN_STOCK_QUANTITY, modelStock.reorder_qty);


        // insert row
        long id = db.insert(ModelStock.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }


    public List<ModelStock> getAllModelStockMenu() {
        List<ModelStock> modelStockList = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + ModelStock.TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ModelStock modelStock = new ModelStock();
                modelStock.setPro_id(cursor.getString(cursor.getColumnIndex(ModelStock.COLUMN_PRODCUT_ID)));
                modelStock.setOrdered_qty(cursor.getString(cursor.getColumnIndex(ModelStock.COLUMN_QUANTITY)));
                modelStock.setReorder_qty(cursor.getString(cursor.getColumnIndex(ModelStock.COLUMN_STOCK_QUANTITY)));

                modelStockList.add(modelStock);

            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return modelCartMenuArrayList list
        return modelStockList;
    }

    public boolean Exists(String searchItem) {

        String[] columns = { ModelStock.COLUMN_PRODCUT_ID};
        String selection = ModelStock.COLUMN_PRODCUT_ID  + " =?";
        String[] selectionArgs = { searchItem };
        String limit = "1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(ModelStock.TABLE_NAME, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ ModelStock.TABLE_NAME);
        db.close();
    }


    public int updateNote(ModelStock modelStock) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        Log.e("value112",modelStock.getPro_id()+"");
        Log.e("value112",modelStock.getReorder_qty()+"");
        values.put(ModelCartMenu.COLUMN_QUANTITY, modelStock.getReorder_qty());

        // updating row
        return db.update(ModelStock.TABLE_NAME, values, ModelStock.COLUMN_PRODCUT_ID + " = ?" ,
                new String[]{String.valueOf(modelStock.getPro_id())});
    }


}
