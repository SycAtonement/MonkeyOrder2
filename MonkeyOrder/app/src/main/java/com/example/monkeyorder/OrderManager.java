package com.example.monkeyorder;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class OrderManager extends AppCompatActivity {
    private OrderDB orderDB;
    private OrderAdapter orderAdapter;
    private Cursor mCursor;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager);
        orderDB = new OrderDB(this);
        SQLiteDatabase db = orderDB.getWritableDatabase();
        mCursor = db.query(OrderDB.ORDER_TABLE_NAME, new String[]{OrderDB.ID, OrderDB.ORDER_TIME, OrderDB.ORDER_DETAIL, OrderDB.ORDER_STATE},
                null, null, null, null, null);

        orderAdapter = new OrderAdapter(this, mCursor, false);
        listView = findViewById(R.id.list_order);
        listView.setAdapter(orderAdapter);
    }

}