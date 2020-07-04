package com.example.stocktracking.Model;

import android.content.UriMatcher;
import android.graphics.Color;
import android.net.Uri;
import android.provider.BaseColumns;


import java.util.ArrayList;

/**
 * Contract Class entre el provider y las aplicaciones
 */
public class DBvariables {
    /**
     * Autoridad del Content Provider
     */
    public static final String DATABASE_NAME = "stock_tracking.db";

    public static final int DATABASE_VERSION = 1;


        public static final String SYMBOLS = "symbols";


public static class Columns implements BaseColumns {

    private Columns() {
        // Sin instancias
    }
//////////////empleados//////////////////
    public final static String SYMBOL_NAME = "symbol_name";
    public final static String PRICE = "price";
    public final static String HIGH = "high";
    public final static String LOW = "low";

}
}
