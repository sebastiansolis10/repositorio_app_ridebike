package com.example.ridebike;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ridebike.providers.UsuarioProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import dmax.dialog.SpotsDialog;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class LoginActivity extends AppCompatActivity {

    TextInputEditText mTextInputEmail;
    TextInputEditText mTextInputPassword;
    Button mButtonLogin;
    Toolbar mToolbar;
    UsuarioProvider mUsuarioProvider;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    private  int contador=0;
    AlertDialog mDialog;
    int status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mTextInputEmail = findViewById(R.id.TextInputEmail);
        mTextInputPassword = findViewById(R.id.textInputPassword);
        mButtonLogin = findViewById(R.id.btnLogin);
        mUsuarioProvider = new UsuarioProvider();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mDialog = new SpotsDialog.Builder().setContext(LoginActivity.this).setMessage("Espere un momento").build();

        mButtonLogin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
        getused();
        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

private void login(){
        String email = mTextInputEmail.getText().toString();
        String password = mTextInputPassword.getText().toString();

        if(!email.isEmpty() && !password.isEmpty()) {
            if (password.length()>= 6){
                mDialog.show();
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            if (status==1){
                                makeText(LoginActivity.this, "El login se realizo exitosamente", LENGTH_SHORT).show();
                                Intent intent=new Intent(LoginActivity.this,Maps_usuario_Activity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(LoginActivity.this, " Su cuenta a sido bloqueada tendra que restablecer contraseña.", LENGTH_SHORT).show();
                                mDialog.dismiss();
                                Log.d("bloqueado","se bloqueo su cuenta.");
                            }
                        }

                        else{
                            contador++;
                            if (contador<= 2){
                                Toast.makeText(LoginActivity.this, " Tercer intento  fallido se bloqueara su cuenta", LENGTH_SHORT).show();

                            }
                            else if (contador==3){
                               mUsuarioProvider.Bloqueo(mAuth.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                   @Override
                                   public void onComplete(@NonNull Task<Void> status) {
                                     if (status.isSuccessful()){
                                         Toast.makeText(LoginActivity.this, " Su cuenta a sido bloqueada tendra que restablecer contraseña.", LENGTH_SHORT).show();
                                         mDialog.dismiss();
                                     }
                                     else{
                                         makeText(LoginActivity.this, status.getException().getMessage(), LENGTH_SHORT).show();
                                     }
                                   }
                               });
                            }
                            Log.d("Usuariobloqueado", String.valueOf(contador));
                            makeText(LoginActivity.this, "La contraseña o el password no son correctos", LENGTH_SHORT).show();
                        }
                        mDialog.dismiss();
                    }

                });


            }
        }
}
    private void getused(){
        mUsuarioProvider.getused(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    status = Integer.parseInt(snapshot.child("status").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}