package com.example.ridebike.providers;

import com.example.ridebike.modelos.Usuario;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class UsuarioProvider {

    DatabaseReference mDatabase;

    public UsuarioProvider(){
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Usuarios");
    }

    public Task<Void> create(Usuario usuario){
        return mDatabase.child(usuario.getId()).setValue(usuario);
    }
    public Task<Void>Bloqueo(String id){
        Map<String,Object>mMAP=new HashMap<>();
        mMAP.put("status",0);
        return mDatabase.child(id).updateChildren(mMAP);

    }
    public DatabaseReference getused(String idusuario){
        return mDatabase.child(idusuario);
    }

}
