package com.stock.yahoo;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * <b>Get the Html source from a https url </b>
 * https://stackoverflow.com/questions/9501237/read-input-stream-twice
 */
public class HttpsClientUtil {

    public static void main(final String[] args) throws Exception {
	final HttpsClientUtil dude = new HttpsClientUtil();
	SymbolCurrentState test = null;
	final String testSymbol = "SLF.TO";

	try {
	    final InputStream yahooDataPage = dude.getYahooDataPage(testSymbol);
	    try {
		test = dude.getSymbolData(yahooDataPage, testSymbol);
	    } catch (final Exception e) {
		e.printStackTrace();
	    }

	    System.out.println("Price: " + test.getPrice() + " Prev. Close: " + test.getPreviousClose()
		    + " Percent changed: " + test.getChangedPercent());
	    yahooDataPage.close();
	} catch (final Exception e) {
	    e.printStackTrace();
	}
    }

    public SymbolCurrentState getSymbolData(final InputStream yahooDataPage, final String symbol) throws Exception {
	final SymbolCurrentState result = new SymbolCurrentState();

	String sPrice = null;
	String sPreviousClose = null;
	String sChangedPercent = null;
	String sMarketCap = null;

	final InputStreamReader isr = new InputStreamReader(yahooDataPage);
	final BufferedReader in = new BufferedReader(isr);
	String inputLine;

	while ((inputLine = in.readLine()) != null) {

	    // Price
	    final String pPrice = "data-symbol=\"" + symbol + "\"";
	    String rawPrice = null;
	    if (inputLine.contains("data-field=\"regularMarketPrice\"") && inputLine.contains(pPrice)) {

		final int startValue = inputLine.indexOf("value=\"");
		final int endValue = inputLine.indexOf("active=\"\">");
		rawPrice = inputLine.substring(startValue, endValue);
		sPrice = makeItDigit(rawPrice);
	    }

	    String rawPreviousClose = null;
	    final String pPreviousClose = "data-test=\"PREV_CLOSE-value\">";
	    if (inputLine.contains(pPreviousClose)) {

		final int startValue = inputLine.indexOf("PREV_CLOSE-value\">");
		final String prevChunk = inputLine.substring(startValue + "PREV_CLOSE-value\">".length());
		final int endValue = prevChunk.indexOf("</td></tr>");
		rawPreviousClose = prevChunk.substring(0, endValue);
		sPreviousClose = makeItDigit(rawPreviousClose);
		// System.out.println("Previous Close: " + sPreviousClose);
	    }

	    String rawPercent = null;
	    if ((inputLine.contains("<span class=\"C($negativeColor)\">(")
		    || inputLine.contains("<span class=\"C($positiveColor)\">"))
		    && inputLine.contains("%)</span></fin-streamer>")) {

		// System.out.println("pStart = " + pStart);
		final int pEnd = inputLine.indexOf("%)</span></fin-streamer>");
		final int pStart = pEnd - 6;
		rawPercent = inputLine.substring(pStart, pEnd);
		// System.out.println("Percent: " + rawPercent);
		sChangedPercent = makeItDigit(rawPercent);
		// System.out.println("PERCENT: " + percent);
	    }

	    String rawMarketCap = null;
	    if (inputLine.contains("data-test=\"MARKET_CAP-value")) {

		final int pStart = inputLine.indexOf("data-test=\"MARKET_CAP-value\">") + 29;
		final int pEnd = pStart + 9;

		rawMarketCap = inputLine.substring(pStart, pEnd);
		sMarketCap = makeItDigit(rawMarketCap);
	    }

	}

	// SymbolCurrentState(String symbol, BigDecimal price, BigDecimal previousClose,
	// BigDecimal changedPercent)
	BigDecimal dPrice = null;
	BigDecimal dPreviousClose = null;
	BigDecimal dChangedPercent = null;
	BigDecimal dMarketCap = null;
	try {
	    dPrice = new BigDecimal(sPrice);
	    dPreviousClose = new BigDecimal(sPreviousClose);
	    dChangedPercent = new BigDecimal(sChangedPercent);
	    dMarketCap = new BigDecimal(sMarketCap);
	    // } catch (NumberFormatException nfe) {
	} catch (final Exception nfe) {
	    System.out.println("ERROR SYMBOL: " + symbol);
	    dPrice = new BigDecimal("0");
	    dPreviousClose = new BigDecimal("0");
	    dChangedPercent = new BigDecimal("0");
	    dMarketCap = new BigDecimal("0");
	}

	result.setSymbol(symbol);
	result.setPrice(dPrice);
	result.setPreviousClose(dPreviousClose);
	result.setChangedPercent(dChangedPercent);
	result.setMarketCap(dMarketCap);

	in.close();
	return result;
    }

    /*
     * Get entire Yahoo Data Page as a stream
     */
    public InputStream getYahooDataPage(final String symbol) throws Exception {
	final String httpsURL = "https://finance.yahoo.com/quote/" + symbol + "?p=" + symbol;

	final URL myurl = new URL(httpsURL);
	final HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
	con.setRequestProperty("User-Agent",
		"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
	return con.getInputStream();
    }

    public String getRawPreviousClose(final InputStream yahooDataPage) throws Exception {
	final String result = "";

	final InputStreamReader isr = new InputStreamReader(yahooDataPage);
	final BufferedReader in = new BufferedReader(isr);
	String inputLine;
	String prevClose = null;
	String rawResult = null;

	while ((inputLine = in.readLine()) != null) {

	    final String p2 = "data-test=\"PREV_CLOSE-value\">";

	    if (inputLine.contains(p2)) {

		// System.out.println(inputLine);
		final int startValue = inputLine.indexOf("PREV_CLOSE-value\">");

		final String prevChunk = inputLine.substring(startValue + "PREV_CLOSE-value\">".length());
		final int endValue = prevChunk.indexOf("</td></tr>");
		rawResult = prevChunk.substring(0, endValue);
		System.out.println("Previous Close Raw Result: " + rawResult);

		prevClose = makeItDigit(rawResult);
		System.out.println("Previous Close: " + prevClose);
	    }
	}
	in.close();
	return result;
    }

    /*
     * String httpsURL = "https://finance.yahoo.com/quote/AAL?p=AAL";
     */
    public String getRawPreviousClose0(final String symbol) throws Exception {
	final String result = "";
	final String httpsURL = "https://finance.yahoo.com/quote/" + symbol + "?p=" + symbol;

	final URL myurl = new URL(httpsURL);
	final HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
	con.setRequestProperty("User-Agent",
		"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
	final InputStream ins = con.getInputStream();
	final InputStreamReader isr = new InputStreamReader(ins);
	final BufferedReader in = new BufferedReader(isr);
	String inputLine;
	String prevClose = null;
	String rawResult = null;

	while ((inputLine = in.readLine()) != null) {

	    final String p2 = "data-test=\"PREV_CLOSE-value\">";

	    if (inputLine.contains(p2)) {

		// System.out.println(inputLine);
		final int startValue = inputLine.indexOf("PREV_CLOSE-value\">");

		final String prevChunk = inputLine.substring(startValue + "PREV_CLOSE-value\">".length());
		final int endValue = prevChunk.indexOf("</td></tr>");
		rawResult = prevChunk.substring(0, endValue);
		System.out.println("Previous Close Raw Result: " + rawResult);

		prevClose = makeItDigit(rawResult);
		System.out.println("Previous Close: " + prevClose);
	    }
	}
	in.close();
	return result;
    }

    /*
     * String httpsURL = "https://finance.yahoo.com/quote/AAL?p=AAL";
     */
    public String getRawPercent(final String symbol) throws Exception {
	String result = "";
	String price = "";
	final String httpsURL = "https://finance.yahoo.com/quote/" + symbol + "?p=" + symbol;

	final URL myurl = new URL(httpsURL);
	final HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
	con.setRequestProperty("User-Agent",
		"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
	final InputStream ins = con.getInputStream();
	final InputStreamReader isr = new InputStreamReader(ins);
	final BufferedReader in = new BufferedReader(isr);
	String inputLine;

	String rawPercent = null;

	while ((inputLine = in.readLine()) != null) {

	    final String p2 = "data-symbol=\"" + symbol + "\"";
	    String rawResult = null;

	    if (inputLine.contains("data-field=\"regularMarketPrice\"") && inputLine.contains(p2)) {

		// System.out.println(inputLine);
		final int startValue = inputLine.indexOf("value=\"");
		final int endValue = inputLine.indexOf("active=\"\">");
		rawResult = inputLine.substring(startValue, endValue);

		price = makeItDigit(rawResult);

		if ((inputLine.contains("<span class=\"C($negativeColor)\">(")
			|| inputLine.contains("<span class=\"C($positiveColor)\">"))
			&& inputLine.contains("%)</span></fin-streamer>")) {

		    // System.out.println("pStart = " + pStart);
		    final int pEnd = inputLine.indexOf("%)</span></fin-streamer>");

		    final int pStart = pEnd - 6;

		    rawPercent = inputLine.substring(pStart, pEnd);
		    // System.out.println("Percent: " + rawPercent);

		    final String percent = makeItDigit(rawPercent);
		    // System.out.println("PERCENT: " + percent);

		    result = price + ",                    " + percent;
		}
	    }
	}
	in.close();
	return result;
    }

    public String getRawPrice(final InputStream yahooDataPage, final String symbol) throws Exception {
	String price = "";

	final InputStreamReader isr = new InputStreamReader(yahooDataPage);
	final BufferedReader in = new BufferedReader(isr);
	String inputLine;

	while ((inputLine = in.readLine()) != null) {

	    final String p2 = "data-symbol=\"" + symbol + "\"";
	    String rawResult = null;

	    if (inputLine.contains("data-field=\"regularMarketPrice\"") && inputLine.contains(p2)) {

		final int startValue = inputLine.indexOf("value=\"");
		final int endValue = inputLine.indexOf("active=\"\">");
		rawResult = inputLine.substring(startValue, endValue);
		price = makeItDigit(rawResult);
	    }
	}
	in.close();
	return price;
    }

    /*
     * String httpsURL = "https://finance.yahoo.com/quote/AAL?p=AAL";
     */
    public String getRawPrice0(final String symbol) throws Exception {
	String price = "";
	final String httpsURL = "https://finance.yahoo.com/quote/" + symbol + "?p=" + symbol;

	final URL myurl = new URL(httpsURL);
	final HttpsURLConnection con = (HttpsURLConnection) myurl.openConnection();
	con.setRequestProperty("User-Agent",
		"Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:63.0) Gecko/20100101 Firefox/63.0");
	final InputStream ins = con.getInputStream();
	final InputStreamReader isr = new InputStreamReader(ins);
	final BufferedReader in = new BufferedReader(isr);
	String inputLine;

	while ((inputLine = in.readLine()) != null) {

	    final String p2 = "data-symbol=\"" + symbol + "\"";
	    String rawResult = null;

	    if (inputLine.contains("data-field=\"regularMarketPrice\"") && inputLine.contains(p2)) {

		final int startValue = inputLine.indexOf("value=\"");
		final int endValue = inputLine.indexOf("active=\"\">");
		rawResult = inputLine.substring(startValue, endValue);

		price = makeItDigit(rawResult);
	    }

	}
	in.close();
	return price;
    }

    /**
     * Scans string and extracts digits in order to build a number representation of
     * the value
     *
     * @param p Piece of raw string containing data
     * @return Digital representation of the value
     */
    public synchronized String makeItDigit(final String p) {
	String result = "";
	boolean isDigitSeen = false;
	int e;
	for (e = 0; e < p.length(); e++) {
	    switch (p.charAt(e)) {
	    case ',':
		break;
	    case '0':
	    case '1':
	    case '2':
	    case '3':
	    case '4':
	    case '5':
	    case '6':
	    case '7':
	    case '8':
	    case '9':
	    case ':':
	    case '+':
	    case '-':
	    case '.':
		isDigitSeen = true;
		result = result + p.charAt(e);
		break;
	    }
	}
	if (!isDigitSeen) {
	    return "0";
	}
	return result;
    }

}