package com.example.sqllite_basics;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String selected_operation = "";
    ArrayList<user_model> UserArrayList;
    TextView id,username, password;
    RadioButton r_btnadd, r_btndelete, r_btnupdate;
    Button btnconfirm, btnsearch;
    myDBadapter helper;
    user_rv_adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        id = (TextView) findViewById(R.id.text_id);
        btnsearch = (Button) findViewById(R.id.btn_search);
        username = (TextView) findViewById(R.id.text_user);
        password = (TextView) findViewById(R.id.text_password);
        r_btnadd = (RadioButton) findViewById(R.id.btn_add);
        r_btndelete = (RadioButton) findViewById(R.id.btn_delete);
        r_btnupdate = (RadioButton) findViewById(R.id.btn_update);
        btnconfirm = (Button) findViewById(R.id.btn_confirm);
        helper = new myDBadapter(this);

        //RecyclerView Code
        RecyclerView recyclerView = findViewById(R.id.mrecylerview);
        UserArrayList = new ArrayList<>();
        adapter = new user_rv_adapter(this, UserArrayList);
        recyclerView.setAdapter(adapter);

        // Set LinearLayoutManager with horizontal orientation
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // Load initial data
        loadData();

        //Initial visibility of edit fields
        id.setVisibility(View.GONE);
        btnsearch.setVisibility(View.GONE);
        username.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);


        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String snackbarReply = "";
                String user_id = id.getText().toString();
                String user = username.getText().toString();
                String pass = password.getText().toString();
                long ret;

                if (selected_operation.equals("add")) {
                    if (user.isEmpty() || pass.isEmpty()) {
                        snackbarReply = "[ADD] Fill in required credentials.";
                    } else {
                        ret = helper.insertData(user, pass);
                        if (ret <= 0) {
                            snackbarReply = "User not inserted.";
                        } else {
                            snackbarReply = "User inserted successfully.";
                            loadData();
                        }
                        username.setText("");
                        password.setText("");
                    }

                } else if (selected_operation.equals("delete")) {
                    if (user_id.isEmpty()) {
                        snackbarReply = "[DELETE] Fill in required credentials.";
                    } else {
                        ret = helper.deleteData(user_id);
                        if (ret <= 0) {
                            snackbarReply = "User not found.";
                        } else {
                            snackbarReply = "User deleted successfully.";
                            loadData();
                        }
                        id.setText("");
                    }

                } else if (selected_operation.equals("update")) {
                    String updated_name = username.getText().toString();
                    String updated_pass = password.getText().toString();

                    if (user_id.isEmpty()) {
                        snackbarReply = "[UPDATE] Please enter a user ID.";
                    } else if (updated_name.isEmpty() || updated_pass.isEmpty()) {
                        snackbarReply = "[UPDATE] Username and password cannot be empty.";
                    } else {
                        // If both user ID and updated values are valid, proceed with the update
                        ret = helper.updateData(user_id, updated_name, updated_pass);
                        if (ret <= 0) {
                            snackbarReply = "User not found.";
                        } else {
                            snackbarReply = "User updated successfully.";
                            loadData();
                        }

                        // Clear the fields after the update
                        id.setText("");
                        username.setText("");
                        password.setText("");
                        username.setEnabled(false);
                        password.setEnabled(false);
                    }
            }else{
                    snackbarReply = "Please select an operation.";
                }
                Snackbar.make(view, snackbarReply, Snackbar.LENGTH_SHORT).show();
            }
        });



        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userId = id.getText().toString();
                if (userId.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter a user ID.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Call locateUser to find the user based on ID
                user_model returnedUser = helper.locateUser(userId);

                if (returnedUser != null) {
                    username.setText(returnedUser.getUsername());
                    password.setText(returnedUser.getPassword());
                    username.setEnabled(true);
                    password.setEnabled(true);

                } else {
                    Toast.makeText(MainActivity.this, "User not found.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadData() {
        UserArrayList.clear();
        String data = helper.getData();

        // Log data for debugging
        Log.d("Data", data);

        String[] users = data.split("\n");
        for (String user : users) {
            if (!user.isEmpty()) {
                String[] userDetails = user.split(" ");
                if (userDetails.length == 3) { // Ensure proper length
                    UserArrayList.add(new user_model(userDetails[0], userDetails[1], userDetails[2]));
                } else {
                    Log.e("LoadData", "Unexpected userDetails length: " + userDetails.length);
                }
            }
        }

        // Log the contents of UserArrayList
        for (user_model user : UserArrayList) {
            Log.d("UserList", "ID: " + user.getId() + ", Username: " + user.getUsername() + ", Password: " + user.getPassword());
        }

        adapter.notifyDataSetChanged();
    }




    public void onRadioBtnClick(View view) {
        boolean checked = ((RadioButton) view).isPressed();

        if (view.getId() == R.id.btn_add) {
            if (checked) {
                selected_operation = "add";
                id.setVisibility(View.GONE);
                btnsearch.setVisibility(View.GONE);
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
            }
        } else if (view.getId() == R.id.btn_delete) {
            if (checked) {
                selected_operation = "delete";
                id.setVisibility(View.VISIBLE);
                btnsearch.setVisibility(View.GONE);
                username.setVisibility(View.GONE);
                password.setVisibility(View.GONE);
            }
        } else if (view.getId() == R.id.btn_update) {
            if (checked) {
                selected_operation = "update";
                id.setVisibility(View.VISIBLE);
                btnsearch.setVisibility(View.VISIBLE);
                username.setVisibility(View.VISIBLE);
                password.setVisibility(View.VISIBLE);
                username.setEnabled(false);
                password.setEnabled(false);
            }
        }
    }
}