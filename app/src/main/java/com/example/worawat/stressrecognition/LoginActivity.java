package com.example.worawat.stressrecognition;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {
    TextView createAccount;
    EditText email;
    Button signIn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        createAccount=(TextView)findViewById(R.id.signIn_textView_CreateNewAccount);
        email = (EditText)findViewById(R.id.signIn_editText_email);
        signIn = (Button) findViewById(R.id.signIn_imageButton_login);

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i =new Intent(getApplicationContext(),CreateAccountActivity.class);
                startActivity(i);
            }
        });
    }
}
