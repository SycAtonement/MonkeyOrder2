package com.example.monkeyorder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class Pick_Activity extends AppCompatActivity {
    private ListView listview_type;
    private ListView listview_food;
    private LeftTypeAdapet leftTypeAdapet;
    private RightFoodAdapter rightFoodAdapter;
    private TextView title;
    public ArrayList<Food> mFoods = new ArrayList<>();
    private ArrayList<FoodType> mTypes = new ArrayList<>();
    private ArrayList<Food> mSelectFood = new ArrayList<>();
    public static final String TAG = "syc";
    public ArrayList<String> showTitle = new ArrayList<>();
    private OrderDB orderDB;
    private int lastPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_activity);
        Log.d(TAG, "onCreate: initData time begin" + System.currentTimeMillis());

        initData();

        Log.d(TAG, "onCreate: initData time end" + System.currentTimeMillis());


        listview_type = findViewById(R.id.listview_type);
        listview_food = findViewById(R.id.listview_food);
        title = findViewById(R.id.title);
        leftTypeAdapet = new LeftTypeAdapet(this, mTypes);
        rightFoodAdapter = new RightFoodAdapter(this, mFoods, mSelectFood);

        Log.d(TAG, "onCreate:setAdapter time begin" + System.currentTimeMillis());
        listview_food.setAdapter(rightFoodAdapter);
        Log.d(TAG, "onCreate:setAdapter time end" + System.currentTimeMillis());

        listview_type.setAdapter(leftTypeAdapet);


        title.setText(mTypes.get(0).getTypename());


        listview_food.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int currentPosition = showTitle.indexOf(firstVisibleItem + "");
                updateLeftListview(firstVisibleItem, currentPosition);
            }
        });


        listview_type.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int firstVisibleItem = listview_type.getFirstVisiblePosition();
                updateLeftListview(firstVisibleItem, position);
                listview_food.setSelection(Integer.parseInt(showTitle.get(position)));
            }
        });


    }

    private void updateLeftListview(int firstVisibleItem, int currentPosition) {
        Log.d(TAG, "updateLeftListview: lastPosition " + lastPosition);
        Log.d(TAG, "updateLeftListview: currentPosition " + currentPosition);
        if (showTitle.contains(firstVisibleItem + "")) {
            title.setText(mFoods.get(firstVisibleItem).getmFoodType());
            TextView lastTV = listview_type.findViewWithTag(lastPosition);
            if (lastTV != null) {
                lastTV.setTextColor(Color.GRAY);
            }
            TextView currentTV = listview_type.findViewWithTag(currentPosition);
            if (currentTV != null) {
                currentTV.setTextColor(Color.BLUE);
            }
            lastPosition = currentPosition;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sendtowechat:
                Toast.makeText(this, "下单成功", Toast.LENGTH_SHORT).show();
                saveToDataBase(mSelectFoodToString());
                Intent share_intent = new Intent();
                share_intent.setAction(Intent.ACTION_SEND);
                share_intent.setType("text/plain");
                share_intent.putExtra(Intent.EXTRA_TEXT, mSelectFoodToString());//添加分享内容;
                share_intent = Intent.createChooser(share_intent, "分享");
                startActivity(share_intent);
                break;
            case R.id.increasefoodlist:
                Intent intent = new Intent(this, IncreaseFood.class);
                startActivity(intent);
                finish();
                break;
            case R.id.sendtosms:
                saveToDataBase(mSelectFoodToString());
                Uri uri = Uri.parse("smsto:17889885755"); //要发送短信的电话号码
                Intent intent1 = new Intent(Intent.ACTION_SENDTO, uri);
                intent1.putExtra("sms_body", "小猴想吃" + " " + mSelectFoodToString());
                startActivity(intent1);
                break;

            case R.id.refresh:


                break;
            case R.id.deleteitem:
                orderDB = new OrderDB(this);
                SQLiteDatabase db = orderDB.getWritableDatabase();
                for (int i = 0; i < mSelectFood.size(); i++) {
                    db.delete(OrderDB.FOOD_TABLE_NAME, "food_name=?", new String[]{mSelectFood.get(i).getmFoodName()});
                }
                db.close();
                Intent intent2 = new Intent(Pick_Activity.this, Pick_Activity.class);
                startActivity(intent2);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String mSelectFoodToString() {
        String foodstoString = "";
        for (int i = 0; i < mSelectFood.size(); i++) {
            foodstoString = foodstoString + mSelectFood.get(i).getmFoodName() + " ";
        }
        return foodstoString;
    }

    private void saveToDataBase(String foodstoString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        ContentValues contentValues = new ContentValues();
        contentValues.put(OrderDB.ORDER_TIME, simpleDateFormat.format(date));
        contentValues.put(OrderDB.ORDER_DETAIL, foodstoString);
        contentValues.put(OrderDB.ORDER_STATE, "已完成");
        SQLiteDatabase db = orderDB.getWritableDatabase();
        db.insert(OrderDB.ORDER_TABLE_NAME, null, contentValues);
        db.close();
    }

    private void initData() {
        FoodType type1 = new FoodType("素菜", R.drawable.xianggu);
        FoodType type2 = new FoodType("荤菜", R.drawable.jitui);
        FoodType type3 = new FoodType("汤类", R.drawable.tang);
        FoodType type4 = new FoodType("主食", R.drawable.zhushilei);
        FoodType type5 = new FoodType("甜品", R.drawable.dangao);
        FoodType type6 = new FoodType("饮料", R.drawable.guozhi);
        mTypes.add(type1);
        mTypes.add(type2);
        mTypes.add(type3);
        mTypes.add(type4);
        mTypes.add(type5);
        mTypes.add(type6);

        orderDB = new OrderDB(this);
        SQLiteDatabase db = orderDB.getWritableDatabase();
        Log.d(TAG, "initData: findMaxId(OrderDB.FOOD_TABLE_NAME)" + findMaxId(OrderDB.FOOD_TABLE_NAME));

        Cursor cursor = db.query(OrderDB.FOOD_TABLE_NAME, new String[]{OrderDB.ID, OrderDB.FOOD_NAME, OrderDB.FOOD_TYPE, OrderDB.FOOD_IMAGE, OrderDB.FOOD_INGREDIENT},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex(OrderDB.FOOD_NAME));
                Log.d(TAG, "initData: name" + name);
                String type = cursor.getString(cursor.getColumnIndex(OrderDB.FOOD_TYPE));

                String image = cursor.getString(cursor.getColumnIndex(OrderDB.FOOD_IMAGE));

                String ingredient = cursor.getString(cursor.getColumnIndex(OrderDB.FOOD_INGREDIENT));
                Food mfood = new Food(image, name, type, ingredient);
                mFoods.add(mfood);

                Log.d(TAG, "initData: mFoods.size() " + mFoods.size());
            } while (cursor.moveToNext());

            cursor.close();
            //排序
            Collections.sort(mFoods, new Comparator<Food>() {
                @Override
                public int compare(Food o1, Food o2) {
                    return o2.getmFoodType().compareTo(o1.getmFoodType());
                }
            });


            for (int i = 0; i < mFoods.size(); i++) {
                if (i == 0) {
                    showTitle.add(i + "");
                } else if (!TextUtils.equals(mFoods.get(i).getmFoodType(),
                        mFoods.get(i - 1).getmFoodType())) {
                    showTitle.add(i + "");
                }
            }

            Log.d(TAG, "initData: showTitle size " + showTitle.size() + showTitle.toString());


        }
    }

    public long findMaxId(String table) {
        // TODO Auto-generated method stub
        SQLiteDatabase database = orderDB.getWritableDatabase();
//Cursor cursor = database.query(table, null, null, null, null, null, " _id DESC");
        Cursor cursor = database.rawQuery("select count(2) from " + table, null);
        // cursor.getCount();
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        Log.d(TAG, "findMaxId: count" + count);
        cursor.close();
        return count;
    }


}