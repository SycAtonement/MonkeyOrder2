package com.example.monkeyorder;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

class OrderAdapter extends CursorAdapter {


    public OrderAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    public OrderAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_list_order, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        /**
         * 确定需要显示的TextView
         */
        TextView order_time = (TextView) view.findViewById(R.id.order_time);
        TextView order_detail = (TextView) view.findViewById(R.id.order_detail);
        TextView order_state = (TextView) view.findViewById(R.id.order_state);

        /**
         * 找到各个内容的列标签
         */
        int timeColumnIndex = cursor.getColumnIndex(OrderDB.ORDER_TIME);
        int detailColumnIndex = cursor.getColumnIndex(OrderDB.ORDER_DETAIL);
        int stateColumnIndex = cursor.getColumnIndex(OrderDB.ORDER_STATE);

        /**
         *读取对应标签的信息
         */
        String time = cursor.getString(timeColumnIndex);
        String detail = cursor.getString(detailColumnIndex);
        String state = cursor.getString(stateColumnIndex);

        /**
         * 设置显示Cursor中读取的内容
         */
        order_time.setText(time);
        order_detail.setText(detail);
        order_state.setText(state);

    }
}

