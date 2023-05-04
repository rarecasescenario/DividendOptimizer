package com.stock.data;

public class CurrentPosition {

    public String symbol;
    public int numberOfShares;

    public CurrentPosition() {
	super();
	// TODO Auto-generated constructor stub
    }

    public CurrentPosition(final String symbol, final int numberOfShares) {
	super();
	this.symbol = symbol;
	this.numberOfShares = numberOfShares;
    }

    public String getSymbol() {
	return symbol;
    }

    public void setSymbol(final String symbol) {
	this.symbol = symbol;
    }

    public int getNumberOfShares() {
	return numberOfShares;
    }

    public void setNumberOfShares(final int numberOfShares) {
	this.numberOfShares = numberOfShares;
    }

    @Override
    public String toString() {
	return "CurrentPosition [symbol=" + symbol + ", numberOfShares=" + numberOfShares + "]";
    }
}
