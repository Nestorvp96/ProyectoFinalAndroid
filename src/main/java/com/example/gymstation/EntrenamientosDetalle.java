package com.example.gymstation;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymstation.Objectos.Dieta;
import com.example.gymstation.Objectos.Ejercicio;
import com.example.gymstation.Objectos.Favoritos;
import com.example.gymstation.Objectos.FirebaseReferences;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

public class EntrenamientosDetalle extends AppCompatActivity {

    DatabaseReference firebaseDatabase, favReference;
    Ejercicio ejercicio = new Ejercicio();
    Favoritos favorito = new Favoritos();
    Favoritos favorites;
    TextView tvNombre, tvDescripcion;
    ImageButton btnVideo;
    String url = "";
    ImageView btn_fav;
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
    String[] correo;
    String idEjercicio;
    Boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_entrenamientos_detalle);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE);
        favReference = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE).child(FirebaseReferences.FAVORITOS_REFERENCE);

        tvNombre = findViewById(R.id.NombreEjercicio);
        tvDescripcion = findViewById(R.id.DescripcionEjercicio);
        btnVideo = findViewById(R.id.btnVideo);
        btn_fav = findViewById(R.id.btnfav);



        Intent intent = getIntent();
        String data = intent.getStringExtra("EJERCICIO");
        String tipo = intent.getStringExtra("TIPO");

        //Toast.makeText(getApplicationContext(), tipo, Toast.LENGTH_SHORT).show();

        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_fav.setImageResource(R.drawable.staron);

                //btn_fav.setEnabled(false);

                if(isFavorite){
                    btn_fav.setImageResource(R.drawable.staroff);
                    deleteFav();
                }
                else {

                    fetchUserInfo();
                    updateUI();
                    isFavorite = true;

                }

            }
        });




        filldata(data, tipo);
        checkFavs(data);


        btnVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), url, Toast.LENGTH_LONG).show();
              showVideo(url);
            }
        });

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

                favorito.setNombre(ejercicio.getNombre());//tvNombre.getText().toString()
                favorito.setTipo(ejercicio.getTipo());

                //firebaseDatabase.child(FirebaseReferences.FAVORITOS_REFERENCE).child(data[0]).push().setValue(favorito);
                String id = favReference.child(data[0]).push().getKey();

                favorito.setId(id);

                favReference.child(data[0]).child(id).setValue(favorito);




                Toast.makeText(getApplicationContext(), "Se agrego a favoritos", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void filldata(String data, String tipo) {

        String tabla = "";

        switch (tipo){


            case "pecho":

                tabla = FirebaseReferences.PECHO_REFERENCE;
                break;

            case "espalda":

                tabla = FirebaseReferences.ESPALDA_REFERENCE;
                break;
            case "biceps":

                tabla = FirebaseReferences.BICEP_REFERENCE;
                break;
            case "triceps":

                tabla = FirebaseReferences.TRICEPS_REFERENCE;
                break;
            case "pierna":

                tabla = FirebaseReferences.PIERNA_REFERENCE;
                break;
            case "hombro":

                tabla = FirebaseReferences.HOMBRO_REFERENCE;
                break;
            case "abdomen":

                tabla = FirebaseReferences.ABDOMEN_REFERENCE;
                break;



        }


        Query query = firebaseDatabase.child(tabla).orderByChild("Nombre").equalTo(data);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        ejercicio = issue.getValue(Ejercicio.class);
                        //Toast.makeText(getContext(), issue.getKey()+"", Toast.LENGTH_LONG).show();
                    }

                    url = ejercicio.getUrl();

                    tvNombre.setText(ejercicio.getNombre());
                    tvDescripcion.setText(ejercicio.getDescripcion());

                    //Toast.makeText(getApplicationContext(), ejercicio.getImagen(), Toast.LENGTH_LONG).show();

                    Uri uri = Uri.parse(ejercicio.getImagen());
                    SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.my_image_view);
                    draweeView.setImageURI(uri);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void showVideo(String url){

        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setPackage("com.google.android.youtube");
        startActivity(intent);
    }

    public void checkFavs(String nombre){


        Query query = favReference.child("pavovelasco").orderByChild("nombre").equalTo(nombre);//.child("pavovelasco")

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    isFavorite = true;
                    //Toast.makeText(getApplicationContext(), "EXISTE", Toast.LENGTH_LONG).show();
                    btn_fav.setImageResource(R.drawable.staron);
                    //btn_fav.setEnabled(false);
                    for(DataSnapshot ds: dataSnapshot.getChildren()) {
                        favorites = ds.getValue(Favoritos.class);
                        idEjercicio = favorites.getId();
                        //Toast.makeText(getApplicationContext(), favorites.getId(), Toast.LENGTH_LONG).show();
                        //firebaseDatabase.child(FirebaseReferences.FAVORITOS_REFERENCE).child("pavovelasco").child()
                    }

                }
                else{
                    isFavorite = false;
                    //Toast.makeText(getApplicationContext(), "NO EXISTE", Toast.LENGTH_LONG).show();
                    btn_fav.setImageResource(R.drawable.staroff);
                    //btn_fav.setEnabled(true);



                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void deleteFav(){

        try{


            favReference.child("pavovelasco").child(idEjercicio).removeValue();
            Toast.makeText(getApplicationContext(),"Se elimino de favoritos", Toast.LENGTH_LONG).show();

        }catch (Exception e){


        }



    }



}
