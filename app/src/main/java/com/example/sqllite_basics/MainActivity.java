package com.example.sqllite_basics;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    TextView username, password;
    Button btnadd, btnview;
    myDBadapter helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        username = (TextView) findViewById(R.id.text_user);
        password = (TextView) findViewById(R.id.text_password);
        btnadd = (Button) findViewById(R.id.btn_add);
        btnview = (Button) findViewById(R.id.btn_view);
        helper = new myDBadapter(this);

        btnview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = helper.getData();
                Snackbar.make(view, data, Snackbar.LENGTH_SHORT).show();
            }
        });


        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();

                if (user.isEmpty() || pass.isEmpty()) {
                    Snackbar.make(view, "Fill in required credentials.", Snackbar.LENGTH_SHORT).show();

                } else {
                    long id = helper.insertData(user, pass);
                    if (id <= 0) {
                        Snackbar.make(view, "User not inserted.", Snackbar.LENGTH_SHORT).show();

                        username.setText("");
                        password.setText("");
                    } else {
                        Snackbar.make(view, "User inserted successfully.", Snackbar.LENGTH_SHORT).show();

                        username.setText("");
                        password.setText("");
                    }
                }
            }
        });


    }
}