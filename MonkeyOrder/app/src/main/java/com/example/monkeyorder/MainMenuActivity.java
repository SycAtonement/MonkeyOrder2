package com.example.monkeyorder;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private ImageButton eat;
    private ImageButton order;
    private ImageButton refrigerator;
    private ImageButton expendmenu;
    private TextView show_datacount;
    //private DatePicker datePicker;
    private static final String DATA = "20191227";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        verifyStoragePermissions(this);
        eat = findViewById(R.id.imageButton2);
        order = findViewById(R.id.imageButton3);
        refrigerator = findViewById(R.id.imageButton4);
        expendmenu = findViewById(R.id.imageButton5);
        show_datacount = findViewById(R.id.show_datecount);
//        datePicker = findViewById(R.id.dp);
//        datePicker.updateDate(2019, 11, 27);

        double datecount = caculateDays(DATA);


        show_datacount.setText("憨憨和小猴已经在一起" + (int) datecount + "天  ❤");

        eat.setOnClickListener(this);
        order.setOnClickListener(this);
        refrigerator.setOnClickListener(this);
        expendmenu.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton2:
                Intent intent = new Intent(this, Pick_Activity.class);
                startActivity(intent);
                break;
            case R.id.imageButton3:
                Intent intent1 = new Intent(this, OrderManager.class);
                startActivity(intent1);
                break;

            case R.id.imageButton4:
                Intent intent2 = new Intent(this, RefrigeratorActivity.class);
                startActivity(intent2);
                break;

            case R.id.imageButton5:
                Intent intent3 = new Intent(this, IncreaseFood.class);
                startActivity(intent3);
                break;

        }
    }

    public double caculateDays(String _beginTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");// 输入日期的格式
        String str1 = _beginTime;
        Date date1 = null;
        try {
            date1 = simpleDateFormat.parse(str1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Date date2 = new Date(System.currentTimeMillis());

        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        cal1.setTime(date1);
        cal2.setTime(date2);
        double dayCount = (cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 3600 * 24);
        return dayCount;
    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
}