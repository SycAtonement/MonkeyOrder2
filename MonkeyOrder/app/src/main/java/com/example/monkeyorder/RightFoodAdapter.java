package com.example.monkeyorder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

class RightFoodAdapter extends BaseAdapter {
    private static final String TAG = "syc";
    public ArrayList<Food> mSelectFood;
    private Context context;
    private ArrayList<Food> mFoodList;
    public  ArrayList<Bitmap> mFoodImages;

    public RightFoodAdapter(Context context, ArrayList<Food> mFoodList, ArrayList<Food> mSelectFood,ArrayList<Bitmap> mFoodImages) {
        this.context = context;
        this.mFoodList = mFoodList;
        this.mSelectFood = mSelectFood;
        this.mFoodImages=mFoodImages;
    }

    @Override
    public int getCount() {
        return mFoodList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFoodList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list_food, null);
            vh = new ViewHold();
            convertView.setTag(vh);
            vh.textViewFoodName = (TextView) convertView.findViewById(R.id.textview_foodname);
            vh.imageViewFood = (ImageView) convertView.findViewById(R.id.imageview_food);
            vh.checkBoxSelected = (CheckBox) convertView.findViewById(R.id.checkbox_select);
            vh.textViewFoodIngredient = (TextView) convertView.findViewById(R.id.textview_foodingredient);

        } else {
            vh = (ViewHold) convertView.getTag();
        }
        vh.textViewFoodName.setText(mFoodList.get(position).getmFoodName());
        try {
            vh.imageViewFood.setImageBitmap(mFoodImages.get(position));
            Log.d(TAG, "getView: mFoodList.get(position).uriToBitmap(context)) " + mFoodList.get(position).uriToBitmap(context).getByteCount());
            Log.d(TAG, "getView:mFoodImages.get(position) " + mFoodImages.get(position).getByteCount());

            vh.imageViewFood.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(context,BigerImage.class);
                    intent1.putExtra("uri",mFoodList.get(position).getmFoodImage());
                    context.startActivity(intent1);
                }
            });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        vh.textViewFoodIngredient.setText(mFoodList.get(position).getmFoodIngredient());
        vh.checkBoxSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mSelectFood.add(mFoodList.get(position));
                } else {
                    mSelectFood.remove(mFoodList.get(position));
                }
            }
        });

        if (position == 0) {//如果是第一个  需要显示标题
            vh.textViewFoodName.setVisibility(View.VISIBLE);
            vh.textViewFoodName.setText(mFoodList.get(position).getmFoodName());
        } else if (!TextUtils.equals(mFoodList.get(position).getmFoodName(),
                mFoodList.get(position - 1).getmFoodName())) {//如果这个标题和上一个不一样   也需要将标题显示出来
            vh.textViewFoodName.setVisibility(View.VISIBLE);
            vh.textViewFoodName.setText(mFoodList.get(position).getmFoodName());
        } else {
            vh.textViewFoodName.setVisibility(View.GONE);
        }

        return convertView;
    }


//    private Bitmap ImageCompressL(Bitmap bitmap) {
//        double targetwidth = Math.sqrt(0.5 * 1000);//约等于100多KB，可自行进行调节
//        if (bitmap.getWidth() > targetwidth || bitmap.getHeight() > targetwidth) {
//            // 创建操作图片用的matrix对象
//            Matrix matrix = new Matrix();
//            // 计算宽高缩放率
//            double x = Math.max(targetwidth / bitmap.getWidth(), targetwidth
//                    / bitmap.getHeight());
//            // 缩放图片动作
//            matrix.postScale((float) x, (float) x);
//            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
//                    bitmap.getHeight(), matrix, true);
//        }
//        return bitmap;
//    }

    public class ViewHold {

        CheckBox checkBoxSelected;

        ImageView imageViewFood;

        TextView textViewFoodName;

        TextView textViewFoodIngredient;
    }
}
