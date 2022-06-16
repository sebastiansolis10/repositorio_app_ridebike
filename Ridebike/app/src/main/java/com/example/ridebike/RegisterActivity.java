package com.example.ridebike;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ridebike.modelos.Usuario;
import com.example.ridebike.providers.AuthProvider;
import com.example.ridebike.providers.UsuarioProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.widget.Toast.makeText;


public class RegisterActivity extends AppCompatActivity {

    Toolbar mToolbar;
    SharedPreferences mPref;

    AuthProvider mAuthProvider;
    UsuarioProvider mUsuarioProvider;

    //VIEWS
    Button mButtonRegister;

    TextInputEditText mTextInputName;
    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuthProvider = new AuthProvider();
        mUsuarioProvider = new UsuarioProvider();

        mPref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);

        mButtonRegister = findViewById(R.id.btnRegistro);
        mTextInputEmail = findViewById(R.id.TextInputEmail);
        mTextInputName = findViewById(R.id.TextInputName);
        mTextInputPassword = findViewById(R.id.TextInputPassword);

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickRegister();
            }
        });


        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Registrarse");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    void clickRegister(){
        final String name = mTextInputName.getText().toString();
        final String email = mTextInputEmail.getText().toString();
        final String password = mTextInputPassword.getText().toString();


        if (!name.isEmpty() && !email.isEmpty() && !password.isEmpty()){
            if(password.length() >= 6){
               register(name, email, password);
            }
            else {
                makeText(this,"La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            makeText(this,"Ingrese todos los campos" , Toast.LENGTH_SHORT).show();
        }
    }

    void register(final String name, final String email, String password){
        mAuthProvider.register(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Usuario usuario = new Usuario(id, name, email,1);
                    create(usuario);
                }
                else{
                    Toast.makeText(RegisterActivity.this,"No se pudo registrar el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    void create(Usuario usuario){
        mUsuarioProvider.create(usuario).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"El registro se realizo correctamente", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(RegisterActivity.this,"No se pudo crear el usuario",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /*
    void saveUser(String id,String name, String email) {
        String selectedUser = mPref.getString("user","");
        User user = new User();
        user.setEmail(email);
        user.setName(name);

        if (selectedUser.equals("Usuario")) {
            mDatabase.child("Users").child("Usuarios").child(id).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>(){
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Fallo el Registro", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    */

}