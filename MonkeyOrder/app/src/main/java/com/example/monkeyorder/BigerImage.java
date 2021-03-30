package com.example.monkeyorder;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BigerImage extends AppCompatActivity {
    private static final String TAG = "syc";
    public static final int Cut_PHOTO = 1;
    public static final int SHOW_PHOTO = 2;
    public static final int PHOTO_ALBUM = 3;
    public static final int SHOW_PHOTO_ALBUM = 4;
    ImageView biggerImage;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_biger_image);
        biggerImage = findViewById(R.id.biggerImage);
        String str = getIntent().getStringExtra("uri");

        try {
            biggerImage.setImageBitmap(BitmapFactory.decodeStream(getContentResolver().openInputStream(Uri.parse(str))));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public int[] twoSum(int[] nums, int target) {
        int[] arr = new int[2];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(i, target - nums[i]);
        }

        for (int i = 0; i < map.size(); i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (map.get(j) + map.get(i) == target) {
                    arr[0] = i;
                    arr[1] = j;

                }
            }
        }
        return arr;
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_pic:
                showPopupMenu(biggerImage);
                break;
        }
        return super.onOptionsItemSelected(item);
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
}