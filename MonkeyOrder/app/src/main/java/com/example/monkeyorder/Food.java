package com.example.monkeyorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;

public class Food {
    private boolean mCheckBoxChecked;
    private String mFoodImage;
    private String mFoodName;
    private String mFoodType;
    private String mFoodIngredient;


    public Food(String mFoodImage, String mFoodName, String mFoodType, String mFoodIngredient) {
        this.mFoodImage = mFoodImage;
        this.mFoodName = mFoodName;
        this.mFoodType = mFoodType;
        this.mFoodIngredient = mFoodIngredient;
    }

    public void setmFoodIngredient(String mFoodIngredient) {
        this.mFoodIngredient = mFoodIngredient;
    }

    public String getmFoodIngredient() {
        return mFoodIngredient;
    }

    public boolean ismCheckBoxChecked() {
        return mCheckBoxChecked;
    }

    public void setmFoodType(String mFoodType) {
        this.mFoodType = mFoodType;
    }

    public String getmFoodType() {
        return mFoodType;
    }

    public boolean ismCheckBoxchecked() {
        return mCheckBoxChecked;
    }

    public String getmFoodImage() {
        return mFoodImage;
    }

    public String getmFoodName() {
        return mFoodName;
    }


    public void setmCheckBoxChecked(boolean mCheckBoxChecked) {
        this.mCheckBoxChecked = mCheckBoxChecked;
    }

    public void setmFoodImage(String mFoodImage) {
        this.mFoodImage = mFoodImage;
    }

    public void setmFoodName(String mFoodName) {
        this.mFoodName = mFoodName;
    }

    public Bitmap uriToBitmap( Context context) throws FileNotFoundException {
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(Uri.parse(mFoodImage)));
    }
}

