package com.stock.data;

import java.math.BigDecimal;

public class WatchSymbol {

    public String symbol;
    public BigDecimal quoterlyDividendAmount;
    public BigDecimal upperYield;
    public BigDecimal lowerYield;
    public String dividendDate1;
    public String dividendDate2;
    public String dividendDate3;
    public String dividendDate4;

    public WatchSymbol() {
	super();
    }

    public String getSymbol() {
	return symbol;
    }

    public void setSymbol(final String symbol) {
	this.symbol = symbol;
    }

    public BigDecimal getQuoterlyDividendAmount() {
	return quoterlyDividendAmount;
    }

    public void setQuoterlyDividendAmount(final BigDecimal quoterlyDividendAmount) {
	this.quoterlyDividendAmount = quoterlyDividendAmount;
    }

    public BigDecimal getUpperYield() {
	return upperYield;
    }

    public void setUpperYield(final BigDecimal upperYield) {
	this.upperYield = upperYield;
    }

    public BigDecimal getLowerYield() {
	return lowerYield;
    }

    public void setLowerYield(final BigDecimal lowerYield) {
	this.lowerYield = lowerYield;
    }

    public String getDividendDate1() {
	return dividendDate1;
    }

    public void setDividendDate1(final String dividendDate1) {
	this.dividendDate1 = dividendDate1;
    }

    public String getDividendDate2() {
	return dividendDate2;
    }

    public void setDividendDate2(final String dividendDate2) {
	this.dividendDate2 = dividendDate2;
    }

    public String getDividendDate3() {
	return dividendDate3;
    }

    public void setDividendDate3(final String dividendDate3) {
	this.dividendDate3 = dividendDate3;
    }

    public String getDividendDate4() {
	return dividendDate4;
    }

    public void setDividendDate4(final String dividendDate4) {
	this.dividendDate4 = dividendDate4;
    }

    @Override
    public String toString() {
	return "WatchSymbol [symbol=" + symbol + ", quoterlyDividendAmount=" + quoterlyDividendAmount + ", upperYield="
		+ upperYield + ", lowerYield=" + lowerYield + ", dividendDate1=" + dividendDate1 + ", dividendDate2="
		+ dividendDate2 + ", dividendDate3=" + dividendDate3 + ", dividendDate4=" + dividendDate4 + "]";
    }

}
