package com.example.monkeyorder;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

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
    public ArrayList<Bitmap> mFoodImages = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_activity);
        Log.d(TAG, "onCreate: initData time begin"+ System.currentTimeMillis());
        try {
            initData();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "onCreate: initData time end"+ System.currentTimeMillis());



        listview_type = findViewById(R.id.listview_type);
        listview_food = findViewById(R.id.listview_food);
        title = findViewById(R.id.title);
        leftTypeAdapet = new LeftTypeAdapet(this, mTypes);
        rightFoodAdapter = new RightFoodAdapter(this, mFoods, mSelectFood, mFoodImages);

        Log.d(TAG, "onCreate:setAdapter time begin"+ System.currentTimeMillis());
        listview_food.setAdapter(rightFoodAdapter);
        Log.d(TAG, "onCreate:setAdapter time end"+ System.currentTimeMillis());

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
                onRestart();
                break;
            case R.id.deleteitem:
                orderDB = new OrderDB(this);
                SQLiteDatabase db = orderDB.getWritableDatabase();
                for (int i = 0; i < mSelectFood.size(); i++) {
                    db.delete(OrderDB.FOOD_TABLE_NAME, "food_name=?", new String[]{mSelectFood.get(i).getmFoodName()});
                }
                db.close();
                onRestart();
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

    private void initData() throws FileNotFoundException {
        FoodType type1 = new FoodType("菜", R.drawable.type_vegetable);
        FoodType type2 = new FoodType("肉", R.drawable.type_meat);
        FoodType type3 = new FoodType("汤", R.drawable.type_soup);
        mTypes.add(type1);
        mTypes.add(type2);
        mTypes.add(type3);

        orderDB = new OrderDB(this);
        SQLiteDatabase db = orderDB.getWritableDatabase();
        Log.d(TAG, "initData: findMaxId(OrderDB.FOOD_TABLE_NAME)" + findMaxId(OrderDB.FOOD_TABLE_NAME));
//        if (findMaxId(OrderDB.FOOD_TABLE_NAME) < 3) {
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(OrderDB.FOOD_NAME, "默认肉");
//            contentValues.put(OrderDB.FOOD_TYPE, "肉");
//            contentValues.put(OrderDB.FOOD_IMAGE, String.valueOf(R.drawable.type_meat));
//            contentValues.put(OrderDB.FOOD_INGREDIENT, "请勿删除");
//            db.insert(OrderDB.FOOD_TABLE_NAME, null, contentValues);
//
//            ContentValues contentValues1 = new ContentValues();
//            contentValues1.put(OrderDB.FOOD_NAME, "默认菜");
//            contentValues1.put(OrderDB.FOOD_TYPE, "菜");
//            contentValues1.put(OrderDB.FOOD_IMAGE,  String.valueOf(R.drawable.type_vegetable));
//            contentValues1.put(OrderDB.FOOD_INGREDIENT, "请勿删除");
//            db.insert(OrderDB.FOOD_TABLE_NAME, null, contentValues1);
//
//            ContentValues contentValues2 = new ContentValues();
//            contentValues2.put(OrderDB.FOOD_NAME, "默认汤");
//            contentValues2.put(OrderDB.FOOD_TYPE, "汤");
//            contentValues2.put(OrderDB.FOOD_IMAGE,  String.valueOf(R.drawable.type_soup));
//            contentValues2.put(OrderDB.FOOD_INGREDIENT, "请勿删除");
//            db.insert(OrderDB.FOOD_TABLE_NAME, null, contentValues2);
//        }

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
                mFoodImages.add(ImageCompressL(mfood.uriToBitmap(this)));
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

    private Bitmap ImageCompressL(Bitmap bitmap) {
        double targetwidth = Math.sqrt(10 * 1000);//约等于100多KB，可自行进行调节
        if (bitmap.getWidth() > targetwidth || bitmap.getHeight() > targetwidth) {
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            double x = Math.max(targetwidth / bitmap.getWidth(), targetwidth
                    / bitmap.getHeight());
            // 缩放图片动作
            matrix.postScale((float) x, (float) x);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
        }
        return bitmap;
    }

}