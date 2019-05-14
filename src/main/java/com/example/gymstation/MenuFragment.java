package com.example.gymstation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MenuFragment extends Fragment {


    ImageButton ibEntrenamientos, ibNutricion, ibFavoritos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        ibEntrenamientos = v.findViewById(R.id.iv_menu_entrenamientos);
        ibNutricion = v.findViewById(R.id.iv_menu_nutricion);
        ibFavoritos = v.findViewById(R.id.iv_menu_favoritos);


        ibEntrenamientos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                EntrenamientosFragment newEntrenamientosfragment = new EntrenamientosFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, newEntrenamientosfragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });

        ibNutricion.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                NutricionFragment newNutricionfragment = new NutricionFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, newNutricionfragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });

        ibFavoritos.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                FavoritosFragment newFavoritosfragment = new FavoritosFragment();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, newFavoritosfragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }

        });



        return v;

    }
}
