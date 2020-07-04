package com.example.stocktracking.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.stocktracking.R;
import com.example.stocktracking.Model.Symbol_class;

import java.util.ArrayList;


public class Symbols_Adapter extends RecyclerView.Adapter <Symbols_Adapter.Productos_ventasViewHolder>{  ///adaptador para el Fragmet Ventas
    private ArrayList<Symbol_class> itemsSymbols;

    public Symbols_Adapter(ArrayList<Symbol_class> itemsSymbols) {
        this.itemsSymbols=itemsSymbols;

    }
    public static class Productos_ventasViewHolder extends RecyclerView.ViewHolder{    ////cardview elements
        // Campos respectivos de un item
        public TextView symbol, price, high, low;
        public Productos_ventasViewHolder(final View v) {   ////lo que se programe aqui es para cuando se le de clic a un item del recycler
            super(v);
            symbol = v.findViewById(R.id.TVsymbol);
            price = v.findViewById(R.id.TVprice);
            high = v.findViewById(R.id.TVhigh);
            low = v.findViewById(R.id.TVlow);
        }
    }

    @Override
    public int getItemCount() {
        return itemsSymbols.size();
    }

    @Override
    public Productos_ventasViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.symbol_card, viewGroup, false);
        return new Productos_ventasViewHolder(v);
    }

    @SuppressLint("ResourceType")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(final Productos_ventasViewHolder holder, final int position) {

        holder.symbol.setText(String.valueOf(itemsSymbols.get(position).getSymbol()));
        holder.price.setText(String.valueOf(itemsSymbols.get(position).getPrice()));
        holder.high.setText(String.valueOf(itemsSymbols.get(position).getHigh()));
        holder.low.setText(String.valueOf(itemsSymbols.get(position).getLow()));

    }

    }