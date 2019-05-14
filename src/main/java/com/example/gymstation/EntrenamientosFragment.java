package com.example.gymstation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EntrenamientosFragment extends Fragment {

    GridView gridView;
    private ImageButton ibpecho, ibespalda, ibbiceps, ibtriceps, ibpierna, ibhombro, ibabs;

    String[] values = {

      "Pecho",
      "Espalda",
      "Biceps",
      "Triceps",
      "Pierna",
      "Hombro",
      "Abdomen"

    };

    int[] imagenes = {

      R.drawable.pecho,
            R.drawable.espalda,
              R.drawable.biceps,
              R.drawable.triceps,
              R.drawable.piernas,
              R.drawable.hombro,
              R.drawable.abs


    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {





        View v = inflater.inflate(R.layout.fragment_entrenamientos, container, false);
        gridView = v.findViewById(R.id.gridview);
        GridAdapter gridAdapter = new GridAdapter(this.getContext(), values, imagenes);
        gridView.setAdapter(gridAdapter);



        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(getContext(), position+"", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), ListaEntrenamientos.class);
                switch(position+""){

                    case "0":

                        intent.putExtra("TYPE", "pecho");
                        startActivity(intent);

                        break;

                    case "1":

                        intent.putExtra("TYPE", "espalda");
                        startActivity(intent);

                        break;

                    case "2":

                        intent.putExtra("TYPE", "biceps");
                        startActivity(intent);

                        break;

                    case "3":

                        intent.putExtra("TYPE", "triceps");
                        startActivity(intent);

                        break;

                    case "4":

                        intent.putExtra("TYPE", "pierna");
                        startActivity(intent);

                        break;

                    case "5":

                        intent.putExtra("TYPE", "hombro");
                        startActivity(intent);

                        break;

                    case "6":

                        intent.putExtra("TYPE", "abdomen");
                        startActivity(intent);

                        break;


                }

            }
        });

        return v;
    }


}
