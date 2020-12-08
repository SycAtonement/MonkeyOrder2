package com.example.monkeyorder;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListPopupWindow;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class IncreaseFood extends AppCompatActivity implements View.OnClickListener, View.OnFocusChangeListener {
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
        type.setOnFocusChangeListener(this);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            showListPopulWindow(); //调用显示PopuWindow 函数
        }
    }

    private void showListPopulWindow() {
        final String[] list = {"素菜", "荤菜", "汤类", "主食", "甜品", "饮料"};//要填充的数据
        final ListPopupWindow listPopupWindow;
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list));//用android内置布局，或设计自己的样式
        listPopupWindow.setAnchorView(type);//以哪个控件为基准，在该处以mEditText为基准
        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {//设置项点击监听
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                type.setText(list[i]);//把选择的选项内容展示在EditText上
                listPopupWindow.dismiss();//如果已经选择了，隐藏起来
            }
        });
        listPopupWindow.show();//把ListPopWindow展示出来
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.increase_confirm:
                String str1 = name.getText().toString().trim();
                String str2 = type.getText().toString();
                String str3 = ingredient.getText().toString();
                String str4 = "";
                if (str1.length() != 0) {
                    orderDB = new OrderDB(this);
                    SQLiteDatabase db = orderDB.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(OrderDB.FOOD_NAME, str1);
                    contentValues.put(OrderDB.FOOD_TYPE, str2);
//                    Log.d(TAG, "onClick: R.drawable.type_vegetable    " + R.drawable.type_vegetable);
//                    Log.d(TAG, "onClick: imageUri   " + imageUri);
//                    Log.d(TAG, "onClick: String.valueOf(imageUri)   " + imageUri);
                    if (imageUri == null) {
                        str4 = "";
                        Toast.makeText(this, "未选择图片", Toast.LENGTH_SHORT).show();
                    } else {
                        str4 = String.valueOf(imageUri);
                    }
                    contentValues.put(OrderDB.FOOD_INGREDIENT, str3);
                    contentValues.put(OrderDB.FOOD_IMAGE, str4);
                    db.insert(OrderDB.FOOD_TABLE_NAME, null, contentValues);


                    //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                    AlertDialog.Builder builder = new AlertDialog.Builder(IncreaseFood.this);
                    //    设置Title的图标
                    builder.setIcon(R.drawable.login_dog);
                    //    设置Title的内容
                    builder.setTitle("添加成功");
                    //    设置Content来显示一个信息
                    builder.setMessage("继续添加吗？");
                    //    设置一个PositiveButton
                    builder.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            name.setText("");
                            type.setText("");
                            ingredient.setText("");
                            Toast.makeText(IncreaseFood.this, "添加成功", Toast.LENGTH_SHORT).show();

                        }
                    });
                    //    设置一个NegativeButton
                    builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(IncreaseFood.this, "添加成功", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(IncreaseFood.this, Pick_Activity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    //    设置一个NeutralButton
                    builder.setNeutralButton("忽略", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(IncreaseFood.this, "添加成功", Toast.LENGTH_SHORT).show();

                        }
                    });
                    //    显示出该对话框
                    builder.show();


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
        File outputImage = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
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
        File outputImage = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
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


