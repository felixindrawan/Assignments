package com.example.thestore;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.*;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private EditText userName;
    private EditText password;
    private Button login;
    private TextView failedLogin;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_Launcher);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        variableSetup();

        exit = (Button) findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.exit(0);
            }
        });
    }
    public void authenticateLogin(View view){
        String usernameStr = userName.getText().toString();
        String passwordStr = password.getText().toString();

        if (usernameStr.equals("admin") && passwordStr.equals("a")){
            Intent intentAdmin = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intentAdmin);
        } else if (usernameStr.equals("employee") && passwordStr.equals("e")){
            Intent intentEmpl = new Intent(MainActivity.this, EmployeeActivity.class);
            startActivity(intentEmpl);
        } else if (usernameStr.equals("customer") && passwordStr.equals("c")){
            Intent intentCus = new Intent(MainActivity.this, CustomerActivity.class);
            startActivity(intentCus);
        } else {
            failedLogin.setVisibility(View.VISIBLE);

            Timer timer = new Timer(false);
            timer.schedule(new TimerTask() {
                @Override
                public void run(){
                    runOnUiThread(new Runnable() {
                        public void run(){
                            failedLogin.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }, 2500);
        }
    }

    private void variableSetup(){
        userName = (EditText) findViewById(R.id.username_title);
        password = (EditText) findViewById(R.id.password_title);
        login = (Button) findViewById(R.id.login_btn);
        failedLogin = (TextView) findViewById(R.id.login_status);

    }


}
