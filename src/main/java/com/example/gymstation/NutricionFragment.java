package com.example.gymstation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gymstation.Objectos.Dieta;
import com.example.gymstation.Objectos.FirebaseReferences;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

public class NutricionFragment extends Fragment {

    Button btncrear, btnguardar;
    Button btnpadd, btncadd, btnfadd, btnvadd, btnleadd, btnlaadd;
    Button btnpsub, btncsub, btnfsub, btnvsub, btnlesub, btnlasub;
    TextView tvprotTOT, tvcarbTOT, tvfrutTOT, tvverTOT, tvlegTOT, tvlactTOT;
    TextView tvprotNEW, tvcarbNEW, tvfrutNEW, tvverNEW, tvlegNEW, tvlactNEW;
    ProgressBar pgP, pgC, pgF, pgV, pgLeg, pgLact;
    DatabaseReference firebaseDatabase;
    Dieta dietaTOT=new Dieta();
    Dieta dietaNEW=new Dieta();
    String key;
    private JSONObject user;
    String user_email = "";
    private static final String NAME = "name";
    private static final String ID = "id";
    private static final String PICTURE = "picture";
    private static final String EMAIL = "email";
    private static final String FIELDS = "fields";
    private static final String REQUEST_FIELDS =
            TextUtils.join(",", new String[]{ID, NAME, PICTURE, EMAIL});

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_nutricion, container, false);

        btncrear = v.findViewById(R.id.btnnueva);
        btnguardar = v.findViewById(R.id.btnguardar);

        tvprotTOT = v.findViewById(R.id.tv_nutricion_proteinas_total);
        tvcarbTOT= v.findViewById(R.id.tv_nutricion_carbos_totales);
        tvfrutTOT= v.findViewById(R.id.tv_nutricion_frutas_totales);
        tvverTOT= v.findViewById(R.id.tv_nutricion_verduras_totales);
        tvlegTOT= v.findViewById(R.id.tv_nutricion_leguminosas_totales);
        tvlactTOT= v.findViewById(R.id.tv_nutricion_lacteos_totales);

        tvprotNEW = v.findViewById(R.id.tv_nutricion_proteinas_restantes);
        tvcarbNEW= v.findViewById(R.id.tv_nutricion_carbos_restantes);
        tvfrutNEW= v.findViewById(R.id.tv_nutricion_frutas_restantes);
        tvverNEW= v.findViewById(R.id.tv_nutricion_verduras_restantes);
        tvlegNEW= v.findViewById(R.id.tv_nutricion_leguminosas_restantes);
        tvlactNEW= v.findViewById(R.id.tv_nutricion_lacteos_restantes);

        btnpadd= v.findViewById(R.id.btn_mas_proteina);
        btncadd= v.findViewById(R.id.btn_mas_carbos);
        btnfadd= v.findViewById(R.id.btn_mas_frutas);
        btnvadd= v.findViewById(R.id.btn_mas_verduras);
        btnleadd= v.findViewById(R.id.btn_mas_leguminosas);
        btnlaadd= v.findViewById(R.id.btn_mas_lacteos);

        btnpsub= v.findViewById(R.id.btn_menos_proteina);
        btncsub= v.findViewById(R.id.btn_menos_carbos);
        btnfsub= v.findViewById(R.id.btn_menos_frutas);
        btnvsub= v.findViewById(R.id.btn_menos_verduras);
        btnlesub= v.findViewById(R.id.btn_menos_leguminosas);
        btnlasub= v.findViewById(R.id.btn_menos_lacteos);

        pgP = v.findViewById(R.id.pgProte);
        pgC = v.findViewById(R.id.pgCarbos);
        pgF = v.findViewById(R.id.pgFrutas);
        pgV = v.findViewById(R.id.pgVerduras);
        pgLeg = v.findViewById(R.id.pgLeg);
        pgLact = v.findViewById(R.id.pgLact);

        fetchUserInfo();
        updateUI();


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                pgP.setProgress(((Integer.parseInt(tvprotNEW.getText().toString())*100)/ Integer.parseInt(tvprotTOT.getText().toString())));
                pgC.setProgress(((Integer.parseInt(tvcarbNEW.getText().toString())*100)/ Integer.parseInt(tvcarbTOT.getText().toString())));
                pgF.setProgress(((Integer.parseInt(tvfrutNEW.getText().toString())*100)/ Integer.parseInt(tvfrutTOT.getText().toString())));
                pgV.setProgress(((Integer.parseInt(tvverNEW.getText().toString())*100)/ Integer.parseInt(tvverTOT.getText().toString())));
                pgLeg.setProgress(((Integer.parseInt(tvlegNEW.getText().toString())*100)/ Integer.parseInt(tvlegTOT.getText().toString())));
                pgLact.setProgress(((Integer.parseInt(tvlactNEW.getText().toString())*100)/ Integer.parseInt(tvlactTOT.getText().toString())));
            }
        }, 2000);


        firebaseDatabase = FirebaseDatabase.getInstance().getReference(FirebaseReferences.BD_REFERENCE);


        final Handler handler2 = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                filldata();
            }
        }, 1000);


        btncrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), NuevaDieta.class);
                startActivity(intent);

            }
        });

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(), key, Toast.LENGTH_LONG).show();

                dietaNEW.setProteinas(Integer.parseInt(tvprotNEW.getText().toString()));
                dietaNEW.setCarbos(Integer.parseInt(tvcarbNEW.getText().toString()));
                dietaNEW.setFrutas(Integer.parseInt(tvfrutNEW.getText().toString()));
                dietaNEW.setVerduras(Integer.parseInt(tvverNEW.getText().toString()));
                dietaNEW.setLeguminosas(Integer.parseInt(tvlegNEW.getText().toString()));
                dietaNEW.setLacteos(Integer.parseInt(tvlactNEW.getText().toString()));


                firebaseDatabase.child(FirebaseReferences.NUTRICIONACTUAL_REFERENCE).child(key).setValue(dietaNEW);

                Toast.makeText(getContext(), "Guardado con Exito", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);

            }
        });


        /////////////////////////////////BUTTONS SUMAR//////////////////////////////////////////////////////////////





        btnpadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            if(Integer.parseInt(tvprotNEW.getText().toString()) >= Integer.parseInt(tvprotTOT.getText().toString())) btnpadd.setEnabled(false);
            else {
                btnpadd.setEnabled(true);
                tvprotNEW.setText((Integer.parseInt(tvprotNEW.getText().toString())+ 1)+"");
                pgP.setProgress(((Integer.parseInt(tvprotNEW.getText().toString())*100)/ Integer.parseInt(tvprotTOT.getText().toString())));
            }

            if(Integer.parseInt(tvprotNEW.getText().toString()) > 0) btnpsub.setEnabled(true);

            }
        });
        btncadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(tvcarbNEW.getText().toString()) >= Integer.parseInt(tvcarbTOT.getText().toString())) btncadd.setEnabled(false);
                else {
                    btncadd.setEnabled(true);
                    tvcarbNEW.setText((Integer.parseInt(tvcarbNEW.getText().toString())+ 1)+"");
                    pgC.setProgress(((Integer.parseInt(tvcarbNEW.getText().toString())*100)/ Integer.parseInt(tvcarbTOT.getText().toString())));
                }

                if(Integer.parseInt(tvcarbNEW.getText().toString()) > 0) btncsub.setEnabled(true);

            }
        });
        btnfadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(tvfrutNEW.getText().toString()) >= Integer.parseInt(tvfrutTOT.getText().toString())) btnfadd.setEnabled(false);
                else {
                    btnfadd.setEnabled(true);
                    tvfrutNEW.setText((Integer.parseInt(tvfrutNEW.getText().toString())+ 1)+"");
                    pgF.setProgress(((Integer.parseInt(tvfrutNEW.getText().toString())*100)/ Integer.parseInt(tvfrutTOT.getText().toString())));
                }

                if(Integer.parseInt(tvfrutNEW.getText().toString()) > 0) btnfsub.setEnabled(true);

            }
        });
        btnvadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(tvverNEW.getText().toString()) >= Integer.parseInt(tvverTOT.getText().toString())) btnvadd.setEnabled(false);
                else {
                    btnvadd.setEnabled(true);
                    tvverNEW.setText((Integer.parseInt(tvverNEW.getText().toString())+ 1)+"");
                    pgV.setProgress(((Integer.parseInt(tvverNEW.getText().toString())*100)/ Integer.parseInt(tvverTOT.getText().toString())));
                }

                if(Integer.parseInt(tvverNEW.getText().toString()) > 0) btnvsub.setEnabled(true);

            }
        });
        btnleadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(tvlegNEW.getText().toString()) >= Integer.parseInt(tvlegTOT.getText().toString())) btnleadd.setEnabled(false);
                else {
                    btnleadd.setEnabled(true);
                    tvlegNEW.setText((Integer.parseInt(tvlegNEW.getText().toString())+ 1)+"");
                    pgLeg.setProgress(((Integer.parseInt(tvlegNEW.getText().toString())*100)/ Integer.parseInt(tvlegTOT.getText().toString())));
                }

                if(Integer.parseInt(tvlegNEW.getText().toString()) > 0) btnlesub.setEnabled(true);


            }
        });
        btnlaadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(tvlactNEW.getText().toString()) >= Integer.parseInt(tvlactTOT.getText().toString())) btnlaadd.setEnabled(false);
                else {
                    btnlaadd.setEnabled(true);
                    tvlactNEW.setText((Integer.parseInt(tvlactNEW.getText().toString())+ 1)+"");
                    pgLact.setProgress(((Integer.parseInt(tvlactNEW.getText().toString())*100)/ Integer.parseInt(tvlactTOT.getText().toString())));
                }

                if(Integer.parseInt(tvlactNEW.getText().toString()) > 0) btnlasub.setEnabled(true);


            }
        });

        /////////////////////////////////BUTTONS RESTAR//////////////////////////////////////////////////////////////

        btnpsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(tvprotNEW.getText().toString()) <= 0) btnpsub.setEnabled(false);
                else {
                    btnpsub.setEnabled(true);
                    tvprotNEW.setText((Integer.parseInt(tvprotNEW.getText().toString())- 1)+"");
                    pgP.setProgress(((Integer.parseInt(tvprotNEW.getText().toString())*100)/ Integer.parseInt(tvprotTOT.getText().toString())));
                }

                if(Integer.parseInt(tvprotNEW.getText().toString()) < Integer.parseInt(tvprotTOT.getText().toString())) btnpadd.setEnabled(true);

            }
        });
        btncsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(tvcarbNEW.getText().toString()) <= 0) btncsub.setEnabled(false);
                else {
                    btncsub.setEnabled(true);
                    tvcarbNEW.setText((Integer.parseInt(tvcarbNEW.getText().toString())- 1)+"");
                    pgC.setProgress(((Integer.parseInt(tvcarbNEW.getText().toString())*100)/ Integer.parseInt(tvcarbTOT.getText().toString())));
                }

                if(Integer.parseInt(tvcarbNEW.getText().toString()) < Integer.parseInt(tvcarbTOT.getText().toString())) btncadd.setEnabled(true);

            }
        });
        btnfsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(tvfrutNEW.getText().toString()) <= 0) btnfsub.setEnabled(false);
                else {
                    btnfsub.setEnabled(true);
                    tvfrutNEW.setText((Integer.parseInt(tvfrutNEW.getText().toString())- 1)+"");
                    pgF.setProgress(((Integer.parseInt(tvfrutNEW.getText().toString())*100)/ Integer.parseInt(tvfrutTOT.getText().toString())));
                }

                if(Integer.parseInt(tvfrutNEW.getText().toString()) < Integer.parseInt(tvfrutTOT.getText().toString())) btnfadd.setEnabled(true);

            }
        });
        btnvsub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(tvverNEW.getText().toString()) <= 0) btnvsub.setEnabled(false);
                else {
                    btnvsub.setEnabled(true);
                    tvverNEW.setText((Integer.parseInt(tvverNEW.getText().toString())- 1)+"");
                    pgV.setProgress(((Integer.parseInt(tvverNEW.getText().toString())*100)/ Integer.parseInt(tvverTOT.getText().toString())));
                }

                if(Integer.parseInt(tvverNEW.getText().toString()) < Integer.parseInt(tvverTOT.getText().toString())) btnvadd.setEnabled(true);

            }
        });
        btnlesub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Integer.parseInt(tvlegNEW.getText().toString()) <= 0) btnlesub.setEnabled(false);
                else {
                    btnlesub.setEnabled(true);
                    tvlegNEW.setText((Integer.parseInt(tvlegNEW.getText().toString())- 1)+"");
                    pgLeg.setProgress(((Integer.parseInt(tvlegNEW.getText().toString())*100)/ Integer.parseInt(tvlegTOT.getText().toString())));
                }

                if(Integer.parseInt(tvlegNEW.getText().toString()) < Integer.parseInt(tvlegTOT.getText().toString())) btnleadd.setEnabled(true);
            }
        });
        btnlasub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Integer.parseInt(tvlactNEW.getText().toString()) <= 0) btnlasub.setEnabled(false);
                else {
                    btnlasub.setEnabled(true);
                    tvlactNEW.setText((Integer.parseInt(tvlactNEW.getText().toString())- 1)+"");
                    pgLact.setProgress(((Integer.parseInt(tvlactNEW.getText().toString())*100)/ Integer.parseInt(tvlactTOT.getText().toString())));
                }

                if(Integer.parseInt(tvlactNEW.getText().toString()) < Integer.parseInt(tvlactTOT.getText().toString())) btnlaadd.setEnabled(true);

            }
        });





        return v;


    }

    @Override
    public void onResume() {
        super.onResume();

        filldata();
    }

    public void filldata(){

        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        Query query = firebaseDatabase.child(FirebaseReferences.NUTRICION_REFERENCE).orderByChild("email").equalTo(user_email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        dietaTOT = issue.getValue(Dieta.class);

                        //Toast.makeText(getContext(), issue.getKey()+"", Toast.LENGTH_LONG).show();
                    }

                    tvprotTOT.setText(dietaTOT.getProteinas()+"");
                    tvcarbTOT.setText(dietaTOT.getCarbos()+"");
                    tvfrutTOT.setText(dietaTOT.getFrutas()+"");
                    tvverTOT.setText(dietaTOT.getVerduras()+"");
                    tvlegTOT.setText(dietaTOT.getLeguminosas()+"");
                    tvlactTOT.setText(dietaTOT.getLacteos()+"");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        /////////////QUERY FOR NTRIACTUAL//////////////////////////////////////////////////////////////////////////

        Query queryact = firebaseDatabase.child(FirebaseReferences.NUTRICIONACTUAL_REFERENCE).orderByChild("email").equalTo(user_email);
        queryact.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        dietaNEW = issue.getValue(Dieta.class);

                        key = issue.getKey();
                        //Toast.makeText(getContext(), issue.getKey()+"", Toast.LENGTH_LONG).show();
                    }

                    tvprotNEW.setText(dietaNEW.getProteinas()+"");
                    tvcarbNEW.setText(dietaNEW.getCarbos()+"");
                    tvfrutNEW.setText(dietaNEW.getFrutas()+"");
                    tvverNEW.setText(dietaNEW.getVerduras()+"");
                    tvlegNEW.setText(dietaNEW.getLeguminosas()+"");
                    tvlactNEW.setText(dietaNEW.getLacteos()+"");


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }//END FILLDATA


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
 