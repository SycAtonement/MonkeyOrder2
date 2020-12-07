package com.example.monkeyorder;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class IncreaseFood extends AppCompatActivity implements View.OnClickListener {
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static final int Cut_PHOTO = 1;
    public static final int SHOW_PHOTO = 2;
    public static final int PHOTO_ALBUM = 3;
    public static final int SHOW_PHOTO_ALBUM = 4;
    private static final String TAG = "IncreaseFood";
    private ArrayList<Food> mFoodinIncrease = new ArrayList<>();
    private Cursor mCursor;
    private OrderDB orderDB;
    private EditText name;
    private EditText type;
    private EditText ingredient;
    private ImageButton image;
    private ImageView imagetest;
    private Button confirm;
    private Button quit;
    private Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_increase_food);
        verifyStoragePermissions(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
        name = findViewById(R.id.input_name);
        type = findViewById(R.id.input_type);
        ingredient = findViewById(R.id.input_ingredient);
        image = findViewById(R.id.input_image);
        imagetest = findViewById(R.id.imagetest);
        confirm = findViewById(R.id.increase_confirm);
        quit = findViewById(R.id.increase_quit);

        confirm.setOnClickListener(this);
        quit.setOnClickListener(this);
        image.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.increase_confirm:
                String str1 = name.getText().toString();
                String str2 = type.getText().toString();
                String str3 = ingredient.getText().toString();
                String str4 = "content://media/external/images/media/1618";
                if (str1 != null) {
                    orderDB = new OrderDB(this);
                    SQLiteDatabase db = orderDB.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(OrderDB.FOOD_NAME, str1);
                    contentValues.put(OrderDB.FOOD_TYPE, str2);
                    Log.d(TAG, "onClick: R.drawable.type_vegetable    " + R.drawable.type_vegetable);
                    Log.d(TAG, "onClick: imageUri   " + imageUri);
                    Log.d(TAG, "onClick: String.valueOf(imageUri)   " + String.valueOf(imageUri));
                    if (imageUri == null) {
                        str4 = "content://media/external/images/media/1618";
                        Toast.makeText(this, "使用默认图片", Toast.LENGTH_SHORT).show();

                    } else {
                        str4 = String.valueOf(imageUri);
//                        Uri uri = Uri.parse(String.valueOf(imageUri));
//                        Log.d(TAG, "onClick:string to uri   " + uri);
                    }
                    contentValues.put(OrderDB.FOOD_IMAGE, str4);
                    contentValues.put(OrderDB.FOOD_INGREDIENT, str3);
                    db.insert(OrderDB.FOOD_TABLE_NAME, null, contentValues);
                    Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(IncreaseFood.this, Pick_Activity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "菜名不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.increase_quit:
                finish();
            case R.id.input_image:
                showPopupMenu(image);


        }

    }

    private void showPopupMenu(final View view) {
        final PopupMenu popupMenu = new PopupMenu(this, view);
        //menu 布局
        popupMenu.getMenuInflater().inflate(R.menu.increase, popupMenu.getMenu());
        //点击事件
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.takepic:
                        takePhoto();
                        break;
                    case R.id.choosepic:
                        choosePicture();
                        break;
                    case R.id.exit:
                        popupMenu.dismiss();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        //关闭事件
        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Cut_PHOTO:
                if (resultCode == RESULT_OK) {
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    // 启动裁剪
                    startActivityForResult(intent, SHOW_PHOTO);
                }
                break;
            case PHOTO_ALBUM:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    // 启动裁剪
                    startActivityForResult(intent, SHOW_PHOTO_ALBUM);
                }
                break;
            case SHOW_PHOTO:
            case SHOW_PHOTO_ALBUM:
                if (resultCode == RESULT_OK) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        // 将裁剪后的照片显示出来
                        imagetest.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;

            default:
                break;
        }
    }

    public void takePhoto() {
        File outputImage = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
        //隐式意图启动相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        // 启动相机程序
        startActivityForResult(intent, Cut_PHOTO);
    }

    public void choosePicture() {
        File outputImage = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis()+".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        imageUri = Uri.fromFile(outputImage);
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        //允许裁剪
        intent.putExtra("crop", true);
        //允许缩放
        intent.putExtra("scale", true);
        //图片的输出位置
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, PHOTO_ALBUM);
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


