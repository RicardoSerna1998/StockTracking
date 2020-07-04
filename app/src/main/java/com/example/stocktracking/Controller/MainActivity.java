package com.example.stocktracking.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.stocktracking.Model.DBvariables;
import com.example.stocktracking.Model.DatabaseHelper;
import com.example.stocktracking.R;
import com.example.stocktracking.Model.Symbol_class;
import com.example.stocktracking.Model.VolleySingleton;
import com.example.stocktracking.Model.interface_new_main;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {
    public static ProgressDialog progressDialog;
    private static Cursor sym, rep;
    private static SQLiteDatabase db;
    private static ContentValues values;

    public static RecyclerView recyclerNumeros;
    public static RecyclerView.Adapter adapter;
    public static RecyclerView.LayoutManager lManager;
    public static FragmentManager fm;

    private static ArrayList<Symbol_class> itemsSymbols= new ArrayList<Symbol_class>();
    //private static ArrayList<String> symbols= new ArrayList<>();


    private Timer t;  //declaramos el timer
    private RequestQueue queue;

    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar);

        queue = Volley.newRequestQueue(this);
        recyclerNumeros = findViewById(R.id.RVnumeros);
        DatabaseHelper admin=new DatabaseHelper(getApplicationContext(), DBvariables.DATABASE_NAME, null, DBvariables.DATABASE_VERSION);
        db=admin.getWritableDatabase();


        final Handler handler = new Handler();
        new Runnable() {
            @Override
            public void run() {
                execute();
                handler.postDelayed(this, 2 * 60 * 1000); // every 2 minutes
            }
        }.run();

    }
    public void execute(){
        if (isOnlineNet()){  //there is INTERNET and data to pull information
            sym= db.rawQuery("select symbol_name from symbols", null);

            if (sym.moveToFirst()) {///si hay un elemento
                itemsSymbols.clear();
                request(sym.getString(0)); //call the API
                while (sym.moveToNext()) {
                    request(sym.getString(0)); //call the API
                }
            }
        }
        else{   //display actual data
           sym= db.rawQuery("select symbol_name, price, high, low from symbols", null);

            if (sym.moveToFirst()) {///si hay un elemento
                itemsSymbols.clear();
                itemsSymbols.add(new Symbol_class(sym.getString(0), sym.getString(1), sym.getString(2), sym.getString(3)));  //call to the view too
                while (sym.moveToNext()) {
                    itemsSymbols.add(new Symbol_class(sym.getString(0), sym.getString(1), sym.getString(2), sym.getString(3)));  //call to the view too
                }
            }
            fill(MainActivity.this, itemsSymbols);

        }
    }

    public void  request(String symbol){

        final String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol="+symbol+"&apikey=VJ856XHXT0I8GR7N";
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(
                new JsonObjectRequest(Request.Method.GET, url,  ////POSIBLE ERROR
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Log.i("Connection", "Correct" );
                                    updateLocalData(response);

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {  //// Si el ip es incorrecto
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("Connection", "Correct" );
                                //progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Check your Internet Connection", Toast.LENGTH_LONG).show();
                            }
                        }
                )
        );

    }
    public Boolean isOnlineNet() {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.es");

            int val           = p.waitFor();
            boolean reachable = (val == 0);
            return reachable;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    public void updateLocalData(JSONObject response) throws JSONException {

        JSONObject stock = null;
        try {
            stock = response.getJSONObject("Global Quote");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (stock == null) {   // symbol was not found

            Toast.makeText(getApplicationContext(), "Stock symbol not found", Toast.LENGTH_LONG).show();

        } else {   //symbol found

            String symbol = stock.getString("01. symbol");
            String price = String.format("%.2f", stock.getDouble("05. price"));
            String high = String.format("%.2f", stock.getDouble("03. high"));
            String low = String.format("%.2f", stock.getDouble("04. low"));


            ///when new is added
            if (!repeated(symbol,1)){ //its not repeated
                if (!repeated(symbol,2)){ //its not repeated

                    values = new ContentValues();
                    values.put("symbol_name", symbol);
                    values.put("price", price);
                    values.put("high", high);
                    values.put("low", low);
                    db.insertOrThrow("symbols", null, values);

                    itemsSymbols.add(new Symbol_class(symbol, price, high, low));  //call to the view too

                }
                }
            //when is just called
            else{
                if (!repeated(symbol,2)){ //its not repeated because itemsSymbols was empty
                    itemsSymbols.add(new Symbol_class(symbol, price, high, low));  //call to the view too

                }
            }

            fill(MainActivity.this, itemsSymbols);
        }
    }

        public static void fill(Context context, ArrayList<Symbol_class> item){
            adapter = new Symbols_Adapter(item);///llamamos al adaptador y le enviamos el array como parametro
            lManager = new LinearLayoutManager(context);  //9 filas de nueve columnas
            recyclerNumeros.setLayoutManager(lManager);
            recyclerNumeros.setAdapter(adapter);
        }

        public Boolean repeated (String symbol, int index) {   //when inserts
            boolean repeated = false;
            if(index==1){
                rep= db.rawQuery("select symbol_name from symbols where symbol_name ='" + symbol + "'", null);

                if (rep.moveToFirst()) {///si hay un elemento
                    repeated = true;
                }

            }
            else if(index==2){
                //  Check if value exists into array
                if (itemsSymbols.size() != 0) {  //Empty array

                    for (Symbol_class anArray : itemsSymbols) {

                        if (anArray.getSymbol().equals(symbol)) {
                            repeated = true;

                        }
                    }
                }
            }

            return  repeated;
        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.newStock) {
            new newSymbol_DialogFragment(new interface_new_main() {
                @Override
                public void onClick(String symbol) {

                       request(symbol);

                }
            }).show(getSupportFragmentManager(), "Symbol_Fragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

