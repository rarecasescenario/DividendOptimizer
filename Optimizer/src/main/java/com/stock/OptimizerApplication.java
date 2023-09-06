package com.stock;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import com.stock.data.CurrentPosition;
import com.stock.data.WatchSymbol;
import com.stock.utils.Utility;
import com.stock.yahoo.CurrentYahooData;
import com.stock.yahoo.SymbolCurrentState;

public class OptimizerApplication {

  public static void main(final String[] args) throws InterruptedException, ExecutionException {

    final CurrentYahooData currentYahooData = new CurrentYahooData();

    final List<WatchSymbol> sl = Utility.readWatchList();
    final List<CurrentPosition> cpl = Utility.getCurrentPositions();

    // sl.forEach(System.out::println);
    // cpl.forEach(System.out::println);

    final List<String> workList = sl.stream().map(t -> t.getSymbol()).collect(Collectors.toList());
    // workList.forEach(System.out::println);

    final List<SymbolCurrentState> symbolCurrentState = currentYahooData.getData(workList);
    System.out.println("symbolCurrentState size = " + symbolCurrentState.size());

    // Calculate current assets
    final BigDecimal cash = Utility.getAvailableCash();
    BigDecimal investedAmount = Utility.getInvestedAmount();
    BigDecimal totalDividends = new BigDecimal(0);
    BigDecimal totalAssets = new BigDecimal(0);
    BigDecimal totalAccount = new BigDecimal(0);
    String action = "";
    int res;
    int res2;
    int resHold;
    int resTotalAssets;
    int resDiv;
    int resAction;

    System.out.println("\n" + new Utility().getDateTimeFormatter().format(LocalDateTime.now()));

    System.out.println(
        "=========================================================================================");
    System.out.println(
        "| Symbol  |Shares| Price  | Position,$| QDiv,$ | QDivAmt |UpperY| MidY | CYield | YDiff |");
    System.out.println(
        "=========================================================================================");

    for (int i = 0; i < sl.size(); i++) {
      final String symbol = sl.get(i).getSymbol();
      // System.out.println("symbol = " + symbol);

      final Optional<SymbolCurrentState> scs = symbolCurrentState.stream()
          .filter(t -> t.getSymbol().equalsIgnoreCase(symbol)).findFirst();

      final Optional<WatchSymbol> ws =
          sl.stream().filter(x -> x.getSymbol().equalsIgnoreCase(symbol)).findFirst();

      BigDecimal symbolQuaterlyDividends = new BigDecimal(0);
      BigDecimal upperYield = new BigDecimal(0);
      BigDecimal lowerYield = new BigDecimal(0);
      BigDecimal yield = new BigDecimal(0);
      BigDecimal middleOfYieldRange = new BigDecimal(0);
      BigDecimal yieldDiff = new BigDecimal(0);
      int numberOfShares = 0;

      final Optional<CurrentPosition> cplR =
          cpl.stream().filter(r -> r.getSymbol().equalsIgnoreCase(symbol)).findFirst();

      if (cplR != null && !cplR.isEmpty()) {
        numberOfShares = cplR.get().getNumberOfShares();

        totalAssets = BigDecimal.valueOf(numberOfShares).multiply(scs.get().getPrice());

        symbolQuaterlyDividends =
            BigDecimal.valueOf(numberOfShares).multiply(ws.get().getQuoterlyDividendAmount());
        totalDividends = totalDividends.add(symbolQuaterlyDividends);
        totalAccount = totalAccount.add(totalAssets);
      } else {
        totalAssets = BigDecimal.valueOf(0.0);
      }

      upperYield = ws.get().getUpperYield();
      lowerYield = ws.get().getLowerYield();

      if (scs.get().getPrice() == null) {
        continue;
      }

      yield = ws.get().quoterlyDividendAmount.multiply(BigDecimal.valueOf(400))
          .divide(scs.get().getPrice(), RoundingMode.HALF_EVEN);
      middleOfYieldRange =
          (upperYield.add(lowerYield)).divide(BigDecimal.valueOf(2), 3, RoundingMode.HALF_EVEN);

      // orig yieldDiff = yield.subtract(middleOfYieldRange);
      yieldDiff = upperYield.subtract(yield);

      // Action = "Strong Buy" if CYield is greater than UpYield
      res = yield.compareTo(ws.get().getUpperYield());
      res2 = ws.get().getUpperYield().compareTo(BigDecimal.valueOf(0.0));
      resHold = yield.compareTo(middleOfYieldRange);

      if (res == 0 || res == 1 && res2 != 0) {
        action = "Buy";
      } else {
        action = "";
      }

      if (res2 == 0) {
        action = "";
      }

      if (resHold == 1 && res == -1) {
        action = "Hold";
      }
      if (resHold == -1) {
        action = "Sell";
      }
      // res = yield.compareTo(ws.get().lowerYield);
      // if (res == -1) {
      // action = "Sell Now";
      // }

      String strShares = (numberOfShares != 0) ? String.format("%3d", numberOfShares) : "   ";
      resTotalAssets = totalAssets.compareTo(BigDecimal.valueOf(0.0));
      String strPosition = (resTotalAssets == 1) ? String.format("%,9.2f", totalAssets) : "   ";

      resDiv = symbolQuaterlyDividends.compareTo(BigDecimal.valueOf(0.0));
      String strSymbolQDiv =
          (resDiv == 1) ? String.format("%6.2f", symbolQuaterlyDividends) : "   ";

      // action = (numberOfShares == 0 && !action.equalsIgnoreCase("sell")) ? action :
      // " ";
      if (numberOfShares == 0 && action.equalsIgnoreCase("buy")) {
        // do nothing
      } else if (numberOfShares == 0 && action.equalsIgnoreCase("sell")) {
        action = "   ";
      } else if (numberOfShares == 0 && action.equalsIgnoreCase("hold")) {
        action = "   ";
      }

      System.out.printf(
          "| %-7S | %4s | %6.2f | %9s | %6.2f | %7s | %5.2f | %4.2f | %5.2f | % 5.3f | %-12s %n",
          symbol, strShares, scs.get().getPrice(), strPosition,
          ws.get().getQuoterlyDividendAmount(), strSymbolQDiv, upperYield, middleOfYieldRange,
          yield, yieldDiff, action);
    }
    totalAccount = totalAccount.add(cash);
    BigDecimal pl = totalAccount.subtract(investedAmount);
    System.out.println(
        "=========================================================================================");
    System.out.printf("  Quaterly Dividends,$ %,8.2f  Account Total,$: %,10.2f %n", totalDividends,
        totalAccount);
    System.out.printf("  Available Cash: $%,10.2f %n", cash);
    System.out.printf("  Invested Amount: $%,10.2f    P/L: $%,10.2f  %n", investedAmount, pl);
  }
}
