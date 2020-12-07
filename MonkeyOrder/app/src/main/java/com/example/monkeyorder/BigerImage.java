package com.example.monkeyorder;

import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.FileNotFoundException;

public class BigerImage extends AppCompatActivity {
    private static final String TAG = "syc";
    ImageView biggerImage;

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


}