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
	BigDecimal totalDividends = new BigDecimal(0);
	System.out.println("Sumbol Price CurrentTotal QuoterlyDividends QuoterlyDivAmount CurrentYield UpperYield");
	for (int i = 0; i < cpl.size(); i++) {
	    final String symbol = cpl.get(i).getSymbol();
	    System.out.println("symbol = " + symbol);

	    final Optional<SymbolCurrentState> scs = symbolCurrentState.stream()
		    .filter(t -> t.getSymbol().equalsIgnoreCase(symbol)).findFirst();
	    final Optional<WatchSymbol> ws = sl.stream().filter(t -> t.getSymbol().equalsIgnoreCase(symbol))
		    .findFirst();

	    final BigDecimal totalAssets = BigDecimal.valueOf(cpl.get(i).getNumberOfShares())
		    .multiply(scs.get().getPrice());

	    final BigDecimal symbolQuaterlyDividends = BigDecimal.valueOf(cpl.get(i).getNumberOfShares())
		    .multiply(ws.get().getQuoterlyDividendAmount());

	    final BigDecimal yield = ws.get().quoterlyDividendAmount.multiply(BigDecimal.valueOf(400))
		    .divide(scs.get().getPrice(), RoundingMode.HALF_EVEN);

	    totalDividends = totalDividends.add(symbolQuaterlyDividends);
	    System.out.println(symbol + " | " + scs.get().getPrice() + " | " + totalAssets + " | "
		    + ws.get().getQuoterlyDividendAmount() + " | " + symbolQuaterlyDividends + " | " + yield + " | "
		    + ws.get().getUpperYield());
	}
	System.out.println("TOTAL QUOTERLY DIVIDEND AMOUNT, $: " + totalDividends);

    }
}
