package com.example.gymstation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gymstation.Objectos.Ejercicio;
import com.example.gymstation.Objectos.Favoritos;
import com.example.gymstation.Objectos.FirebaseReferences;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class FavoritosFragment extends Fragment {

    ListView listView;
    DatabaseReference firebaseDatabase;
    ArrayList<String> arrayList;
    ArrayList<String> arrayType;
    ArrayAdapter<String> adapter;
    Favoritos ejercicio;
    private JSONObject user;
    String user_email = "";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String EMAIL = "email";
    private static final String FIELDS = "fields";
    private static final String REQUEST_FIELDS =
            TextUtils.join(",", new String[]{ID, NAME, PICTURE, EMAIL});

    String[] data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favoritos, container, false);

        listView = v.findViewById(R.id.listviewFav);

        arrayList=new  ArrayList<String>();
        arrayType=new ArrayList<String>();



        adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_expandable_list_item_1, arrayList);



        fetchUserInfo();
        updateUI();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE).child(FirebaseReferences.FAVORITOS_REFERENCE).child(data[0]);

                firebaseDatabase.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()){

                            ejercicio = ds.getValue(Favoritos.class);
                            arrayList.add(ejercicio.getNombre());
                            arrayType.add(ejercicio.getTipo());
                        }

                        listView.setAdapter(adapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }, 1000);




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String tipo = arrayType.get(position);
                String data = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(getContext(), EntrenamientosDetalle.class);
                intent.putExtra("EJERCICIO", data);
                intent.putExtra("TIPO", tipo);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(), "DATA:" +data, Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "ARRAY:" +, Toast.LENGTH_SHORT).show();


            }
        });

        return v;
    }


    private void fetchUserInfo() {
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken, new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject me, GraphResponse response) {
                            user = me;
                            updateUI();
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString(FIELDS, REQUEST_FIELDS);
            request.setParameters(parameters);
            GraphRequest.executeBatchAsync(request);
        } else {
            user = null;
        }
    }

    private void updateUI() {
        if (AccessToken.getCurrentAccessToken() != null) {
            if (user != null) {
                user_email = user.optString("email");
                data = user_email.split("@", 2);



            }
        }
    }
}
