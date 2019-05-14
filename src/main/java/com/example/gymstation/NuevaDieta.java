package com.example.gymstation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymstation.Objectos.Dieta;
import com.example.gymstation.Objectos.FirebaseReferences;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.widget.ProfilePictureView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

public class NuevaDieta extends AppCompatActivity {

    EditText etproteina, etcarbos, etfrutas, etverduras, etleguminosas, etlacteos;
    Button btncrear;
    DatabaseReference firebaseDatabase;
    Dieta dieta=new Dieta();

    ////////////////////////////////FACEBOOK PROFILE////////////////////

    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String EMAIL = "email";
    private static final String FIELDS = "fields";
    private static final String REQUEST_FIELDS =
            TextUtils.join(",", new String[]{ID, NAME, PICTURE, EMAIL});
    private AccessTokenTracker accessTokenTracker;
    private CallbackManager callbackManager;
    private JSONObject user;
    private ProfilePictureView profilePicture;

    String user_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nueva_dieta);

        etproteina = findViewById(R.id.etproteinas);
        etcarbos = findViewById(R.id.etcarbos);
        etfrutas = findViewById(R.id.etfrutas);
        etverduras = findViewById(R.id.etverduras);
        etleguminosas = findViewById(R.id.etleguminosas);
        etlacteos = findViewById(R.id.etlacteos);
        btncrear = findViewById(R.id.n_btncrear);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE);



        //filldata();

        btncrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try{


                    fetchUserInfo();
                    updateUI();

                    Log.d("USER_MAIL:", user_email);
                    /////////////////////////////////TBL NUTRI//////////////////////////////////////////////
                    Dieta dietacreate = new Dieta(user_email, Integer.parseInt(etproteina.getText().toString()), Integer.parseInt(etcarbos.getText().toString()),
                     Integer.parseInt(etfrutas.getText().toString()), Integer.parseInt(etverduras.getText().toString()), Integer.parseInt(etleguminosas.getText().toString()), Integer.parseInt(etlacteos.getText().toString()));

                    firebaseDatabase.child(FirebaseReferences.NUTRICION_REFERENCE).push().setValue(dietacreate);

                    /////////////////////////////////TBL NUTRIACTUAL//////////////////////////////////////////////
//////se crea duplicado de tabla para los valores actuales restantes

                    firebaseDatabase.child(FirebaseReferences.NUTRICIONACTUAL_REFERENCE).push().setValue(dietacreate);


                    finish();
                }catch(Exception e){

                    Log.e("FIREBASE tblNutri:", e.getMessage());
                }





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
            }
        }
    }


}
