package com.example.ridebike;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    Button mButtonLogin;
    Button mButtonRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mButtonLogin = findViewById(R.id.btnLogin);
        mButtonRegistro = findViewById(R.id.btnRegistro);

        mButtonLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                goToSelectAuth1();
            }
        });

        mButtonRegistro.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                goToSelectAuth2();
            }
        });
    }

    private void goToSelectAuth1() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToSelectAuth2() {
        Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}