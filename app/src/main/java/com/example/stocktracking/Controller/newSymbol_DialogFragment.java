package com.example.stocktracking.Controller;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.example.stocktracking.Model.interface_new_main;
import com.example.stocktracking.R;

@SuppressLint("ValidFragment")
public class newSymbol_DialogFragment extends DialogFragment {
    private Button ok;
    private EditText symbol;

    private interface_new_main Interface;  ////interfaz para comunicarlo con ventas

    public newSymbol_DialogFragment(interface_new_main inter) {
        this.Interface = inter;
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View rootView = inflater.inflate(R.layout.dialog, container);


        ok = rootView.findViewById(R.id.BtnAceptarPago);

        symbol = rootView.findViewById(R.id.ETsymbol);


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Interface.onClick(symbol.getText().toString()); //Validate that SYMBOL exists

                dismiss();

            }
        });


        return rootView;
    }

}