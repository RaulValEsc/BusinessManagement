package com.example.businessmanagement.controladores;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.businessmanagement.modelos.Proveedor;

public class SpinnerProveedoresAdapter extends ArrayAdapter<Proveedor> {

    private Context context;
    private Proveedor[] proveedores;

    public SpinnerProveedoresAdapter(Context context, int textViewResourceId, Proveedor[] proveedores) {
        super(context, textViewResourceId, proveedores);
        this.context = context;
        this.proveedores = proveedores;
    }

    @Override
    public int getCount(){
        return proveedores.length;
    }

    @Override
    public Proveedor getItem(int position){
        return proveedores[position];
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.WHITE);


        label.setText(proveedores[position].getNombre());

        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(proveedores[position].getNombre());

        return label;
    }
}
