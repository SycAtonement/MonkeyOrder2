package com.example.monkeyorder;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

class LeftTypeAdapet extends BaseAdapter {

    private Context context;
    ArrayList<FoodType> mFoodTypeList;


    public LeftTypeAdapet(Context context, ArrayList<FoodType> mFoodTypeList) {
        this.context = context;
        this.mFoodTypeList = mFoodTypeList;
    }

    @Override
    public int getCount() {
        return mFoodTypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return mFoodTypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold vh = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_list_type, null);
            vh = new ViewHold();
            convertView.setTag(vh);
            vh.type_name = (TextView) convertView.findViewById(R.id.type_name);
            vh.type_pic = (ImageView) convertView.findViewById(R.id.type_pic);
        } else {
            vh = (ViewHold) convertView.getTag();
        }
        vh.type_name.setText(mFoodTypeList.get(position).getTypename());
        vh.type_pic.setImageResource(mFoodTypeList.get(position).getTypepic());
        vh.type_name.setTag(position);
        return convertView;
    }

    public class ViewHold {
        TextView type_name;
        ImageView type_pic;
    }
}
