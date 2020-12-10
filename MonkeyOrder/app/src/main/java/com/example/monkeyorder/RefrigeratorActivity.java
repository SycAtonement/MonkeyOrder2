package com.example.monkeyorder;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RefrigeratorActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText;
    Button clear;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refrigerator);

        editText = findViewById(R.id.refrigeratorlist);
        clear = findViewById(R.id.clear);
        save = findViewById(R.id.save);
        clear.setOnClickListener(this);
        save.setOnClickListener(this);
        SharedPreferences sharedPreferences = this.getSharedPreferences("refrigerator", 0);
        String str = sharedPreferences.getString("list", "");
        editText.setText(str);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear:
                editText.setText("");
                Toast.makeText(this, "清空成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.save:
                String list = editText.getText().toString();
                SharedPreferences sharedPreferences = this.getSharedPreferences("refrigerator", 0);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("list", list);
                editor.commit();
                Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}