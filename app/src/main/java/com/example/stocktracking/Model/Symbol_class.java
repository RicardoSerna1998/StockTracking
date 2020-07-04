package com.example.stocktracking.Model;


public class Symbol_class {
    public String symbol;
    public String price;
    public String high;
    public String low;


    public Symbol_class(String symbol, String price, String high, String low) {
        this.symbol=symbol;
        this.price=price;
        this.high=high;
        this.low=low;

    }
    public String getSymbol() {
        return symbol;
    }
    public String  getPrice() {
        return price;
    }
    public String getHigh() {
        return high;
    }
    public String getLow() {
        return low;
    }

}
