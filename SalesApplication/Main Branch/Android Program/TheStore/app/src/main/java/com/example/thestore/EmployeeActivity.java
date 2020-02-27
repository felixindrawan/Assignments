package com.example.thestore;

import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class EmployeeActivity extends AppCompatActivity {
  private Button logout;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_employee);

    logout = (Button) findViewById(R.id.logout);
    logout.setOnClickListener(new View.OnClickListener(){
      @Override
      public void onClick(View view){
        System.exit(0);
      }
    });

  }
}
