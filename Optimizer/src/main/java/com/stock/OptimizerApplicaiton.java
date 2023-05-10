package com.stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.stock.data.CurrentPosition;
import com.stock.data.WatchSymbol;
import com.stock.utils.Utility;
import com.stock.yahoo.CurrentYahooData;
import com.stock.yahoo.SymbolCurrentState;

public class OptimizerApplicaiton {

    public static void main(final String[] args) throws InterruptedException, ExecutionException {

	final CurrentYahooData cyd = new CurrentYahooData();

	final List<WatchSymbol> sl = Utility.readWatchList();
	final List<CurrentPosition> cpl = Utility.getCurrentPositions();

//	sl.forEach(System.out::println);
//	cpl.forEach(System.out::println);

	final List<String> workList = sl.stream().map(t -> t.getSymbol()).collect(Collectors.toList());
	// workList.forEach(System.out::println);

	final List<SymbolCurrentState> symbolCurrentState = cyd.getData(workList);
	System.out.println("symbolCurrentState size = " + symbolCurrentState.size());

	// Calculate current assets
	final BigDecimal cash = Utility.getAvailableCash();
	BigDecimal totalDividends = new BigDecimal(0);
	BigDecimal totalAssets = new BigDecimal(0);
	BigDecimal totalAccount = new BigDecimal(0);
	String action = "";
	int res;
	int res2;
	System.out.println("========================================================================");
	System.out.println("| Symbol  |Shares| Price  | Position,$| QDiv,$ | QDivAmt|CYield|UpYield|");
	System.out.println("========================================================================");

	for (int i = 0; i < sl.size(); i++) {
	    final String symbol = sl.get(i).getSymbol();
	    // System.out.println("symbol = " + symbol);

	    final Optional<SymbolCurrentState> scs = symbolCurrentState.stream()
		    .filter(t -> t.getSymbol().equalsIgnoreCase(symbol)).findFirst();

	    final Optional<WatchSymbol> ws = sl.stream().filter(x -> x.getSymbol().equalsIgnoreCase(symbol))
		    .findFirst();

	    BigDecimal symbolQuaterlyDividends = new BigDecimal(0);
	    BigDecimal yield = new BigDecimal(0);
	    int numberOfShares = 0;

	    final Optional<CurrentPosition> cplR = cpl.stream().filter(r -> r.getSymbol().equalsIgnoreCase(symbol))
		    .findFirst();

	    if (!cplR.isEmpty()) {
		numberOfShares = cplR.get().getNumberOfShares();

		totalAssets = BigDecimal.valueOf(numberOfShares).multiply(scs.get().getPrice());

		symbolQuaterlyDividends = BigDecimal.valueOf(numberOfShares)
			.multiply(ws.get().getQuoterlyDividendAmount());
		totalDividends = totalDividends.add(symbolQuaterlyDividends);
		totalAccount = totalAccount.add(totalAssets);
	    } else {
		totalAssets = BigDecimal.valueOf(0.0);
	    }

	    yield = ws.get().quoterlyDividendAmount.multiply(BigDecimal.valueOf(400)).divide(scs.get().getPrice(),
		    RoundingMode.HALF_EVEN);

//	    System.out.println(symbol + " | " + scs.get().getPrice() + " | " + totalAssets + " | " + numberOfShares
//		    + " | " + ws.get().getQuoterlyDividendAmount() + " | " + symbolQuaterlyDividends + " | " + yield
//		    + " | " + ws.get().getUpperYield());

	    // Action = "Strong Buy" if CYield is greater than UpYield
	    res = yield.compareTo(ws.get().getUpperYield());
	    res2 = ws.get().getUpperYield().compareTo(BigDecimal.valueOf(0.0));

	    if (res == 0 || res == 1 && res2 != 0) {
		action = "Strong Buy";
	    } else {
		action = "";
	    }

	    if (res2 == 0) {
		action = "";
	    }

//	    res = yield.compareTo(ws.get().lowerYield);
//	    if (res == -1) {
//		action = "Sell Now";
//	    }

	    System.out.printf("| %-7S | %4d | %6.2f | %,9.2f | %6.2f | %6.2f | %4.2f | %5.2f | %-12S %n", symbol,
		    numberOfShares, scs.get().getPrice(), totalAssets, ws.get().getQuoterlyDividendAmount(),
		    symbolQuaterlyDividends, yield, ws.get().getUpperYield(), action);
	}
	totalAccount = totalAccount.add(cash);
	System.out.println("========================================================================");
	System.out.printf("  Quaterly Dividends,$ %,8.2f  Account Total,$: %,10.2f %n", totalDividends, totalAccount);
	System.out.printf("  Available Cash,$ %,10.2f %n", cash);
    }
}
