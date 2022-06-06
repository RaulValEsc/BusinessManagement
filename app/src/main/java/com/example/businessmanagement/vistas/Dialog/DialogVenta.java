package com.example.businessmanagement.vistas.Dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.businessmanagement.R;

public class DialogVenta extends AppCompatDialogFragment {

    private EditText stockIngresar;
    private DialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_ingresar_stock,null);

        builder.setView(view).setTitle("Ingresar").setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                try {
                    int stock = Integer.parseInt(stockIngresar.getText().toString());
                    listener.venderStock(stock);

                } catch (Exception e){
                    Toast.makeText(getContext(), "Cantidad inválida", Toast.LENGTH_LONG).show();
                }

            }
        });

        stockIngresar = view.findViewById(R.id.etIngresarStock);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }


    }

    public interface DialogListener{

        void venderStock(int stock);

    }

}
