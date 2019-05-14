package com.example.gymstation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridAdapter extends BaseAdapter {

    Context context;
    private final String [] values;
    private final int [] imagenes;
    View view;
    LayoutInflater layoutInflater;

    public GridAdapter(Context context, String[] values, int[] imagenes) {
        this.context = context;
        this.values = values;
        this.imagenes = imagenes;
    }

    @Override
    public int getCount() {
        return values.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(convertView == null){
            view = new View(context);
            view = layoutInflater.inflate(R.layout.single_item, null);
            ImageView imageView = view.findViewById(R.id.imageview);
            TextView textView = view.findViewById(R.id.textview);

            imageView.setImageResource(imagenes[position]);
            textView.setText(values[position]);

        }

        return view;
    }
}
