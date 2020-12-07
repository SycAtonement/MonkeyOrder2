package com.example.monkeyorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Login_Activity extends AppCompatActivity {
    private Button login_button;
    private EditText pwd;
    String TAG = "syc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_button = findViewById(R.id.login_button);
        pwd = findViewById(R.id.editTextTextPassword);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd.getText().toString().trim().equals("monkey")) {
                    Intent intent = new Intent(Login_Activity.this, MainMenuActivity.class);
                    startActivity(intent);
                    Toast.makeText(Login_Activity.this, "登录成功", Toast.LENGTH_SHORT).show();
                    finish();

                } else {
                    Toast.makeText(Login_Activity.this, "你不是小臭猴", Toast.LENGTH_SHORT).show();

                }
            }
        });

    }

}
