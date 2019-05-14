package com.example.gymstation;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gymstation.Objectos.Ejercicio;
import com.example.gymstation.Objectos.FirebaseReferences;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ListaEntrenamientos extends AppCompatActivity {

    ListView listView;
    DatabaseReference firebaseDatabase;
    ArrayList<String> arrayList;
    ArrayList<String> arrayType;
    ArrayAdapter<String> adapter;
    Ejercicio ejercicio;

    //Ejercicio ejercicio = new Ejercicio();
    //Ejercicio[] valores = new Ejercicio[200];
    //String[] ejercicios = new String[200];




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_entrenamientos);

        listView = findViewById(R.id.listview);

        ejercicio = new Ejercicio();

        Intent intent = getIntent();
        String type = intent.getStringExtra("TYPE");

        switch(type){

            case "pecho":

                firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE).child(FirebaseReferences.PECHO_REFERENCE);
                break;


            case "espalda":
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE).child(FirebaseReferences.ESPALDA_REFERENCE);
                break;


            case "biceps":
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE).child(FirebaseReferences.BICEP_REFERENCE);
                break;


            case "triceps":
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE).child(FirebaseReferences.TRICEPS_REFERENCE);
                break;


            case "pierna":
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE).child(FirebaseReferences.PIERNA_REFERENCE);
                break;


            case "hombro":
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE).child(FirebaseReferences.HOMBRO_REFERENCE);
                break;


            case "abdomen":
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE).child(FirebaseReferences.ABDOMEN_REFERENCE);
                break;
        }



        arrayList=new  ArrayList<String>();
        arrayType=new ArrayList<String>();

        adapter= new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, arrayList);

        firebaseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    ejercicio = ds.getValue(Ejercicio.class);
                    arrayList.add(ejercicio.getNombre());
                    arrayType.add(ejercicio.getTipo());
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tipo = arrayType.get(position);

                String data = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(getApplicationContext(), EntrenamientosDetalle.class);
                intent.putExtra("EJERCICIO", data);
                intent.putExtra("TIPO", tipo);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "DATA:" +data, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "ARRAY:" +, Toast.LENGTH_SHORT).show();


            }
        });




    }

}
