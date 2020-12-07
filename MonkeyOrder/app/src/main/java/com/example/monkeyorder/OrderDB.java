package com.example.monkeyorder;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


class OrderDB extends SQLiteOpenHelper {
    public static final String ID = "_id";
    public static final String ORDER_TIME = "order_time";
    public static final String ORDER_DETAIL = "order_detail";
    public static final String ORDER_STATE = "order_state";
    public static final String ORDER_TABLE_NAME = "allorders";
    public static final String FOOD_TABLE_NAME = "allfoods";
    public static final String DATABASE_NAME = "monkeyorder";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_CREATE_ORDER =
            "CREATE TABLE " + ORDER_TABLE_NAME + " (" +
                    ID + " integer PRIMARY KEY autoincrement," +
                    ORDER_TIME + " varchar(50)," +
                    ORDER_DETAIL + " varchar(50)," +
                    ORDER_STATE + " varchar(50))";

    public static final String FOOD_NAME = "food_name";
    public static final String FOOD_TYPE = "food_type";
    public static final String FOOD_IMAGE = "food_image";
    public static final String FOOD_INGREDIENT = "food_ingredient";

    private static final String DATABASE_CREATE_FOOD =
            "CREATE TABLE " + FOOD_TABLE_NAME + " (" +
                    ID + " integer PRIMARY KEY autoincrement," +
                    FOOD_NAME + " varchar(50)," +
                    FOOD_TYPE + " varchar(50)," +
                    FOOD_IMAGE + " varchar(50)," +
                    FOOD_INGREDIENT + " varchar(50))";


    public OrderDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(DATABASE_CREATE_ORDER);
        sqLiteDatabase.execSQL(DATABASE_CREATE_FOOD);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
