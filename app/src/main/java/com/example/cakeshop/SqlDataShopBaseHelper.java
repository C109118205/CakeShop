package com.example.cakeshop;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqlDataShopBaseHelper extends SQLiteOpenHelper {

    private static final String DataBaseName = "DataShop";
    private static final int DataBaseVersion = 1;

    public SqlDataShopBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DataBaseName, factory, DataBaseVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String SqlTable = "CREATE TABLE IF NOT EXISTS Shop_Order (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "product_name TEXT not null," +
                "product_price NUMERIC not null," +
                "product_image BLOB not null"+
                ")";
        sqLiteDatabase.execSQL(SqlTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        final String SQL = "DROP TABLE Shop_Order";
        sqLiteDatabase.execSQL(SQL);
    }
}
