package com.stock.yahoo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CurrentYahooData {

  private static ArrayBlockingQueue<String> symbolQueue = null;
  // private final CopyOnWriteArrayList<SymbolCurrentState> csl = new CopyOnWriteArrayList<>();

  // main
  public List<SymbolCurrentState> getData(List<String> symbolList)
      throws InterruptedException, ExecutionException {

    List<SymbolCurrentState> plainSymbolStatus = new ArrayList<>();

    CurrentYahooData.symbolQueue = new ArrayBlockingQueue<>(symbolList.size());
    CurrentYahooData.symbolQueue.addAll(symbolList);
    System.out.println("Symbol Queue size: " + CurrentYahooData.symbolQueue.size());

    CurrentYahooData tc = new CurrentYahooData();
    List<Callable<SymbolCurrentState>> callableTasks = tc.getTasks(symbolList);

    ExecutorService executor = Executors.newFixedThreadPool(10);
    List<Future<SymbolCurrentState>> futureList = null;
    futureList = executor.invokeAll(callableTasks);

    for (int y = 0; y < futureList.size(); y++) {
      Future<SymbolCurrentState> future = futureList.get(y);
      SymbolCurrentState p = future.get();
      plainSymbolStatus.add(p);
      // BigDecimal outstandingShares = p.getMarketCap().divide(p.getPrice(),
      // MathContext.DECIMAL32);
      System.out.printf("%-8S | %6.2f | %5.2f | %6.2f | %n", p.getSymbol(), p.getPrice(),
          p.getChangedPercent(), p.getMarketCap());
    }
    executor.shutdownNow();
    return plainSymbolStatus;
  }

  private List<Callable<SymbolCurrentState>> getTasks(List<String> workList) {

    List<Callable<SymbolCurrentState>> r = new ArrayList<Callable<SymbolCurrentState>>();

    for (int i = 0; i < workList.size(); i++) {

      String symbol = workList.get(i);
      Callable<SymbolCurrentState> task = new Callable<SymbolCurrentState>() {

        @Override
        public SymbolCurrentState call() throws InterruptedException {
          SymbolCurrentState r = null;
          // r = getQuotes(symbol);
          r = getSymbolData(symbol);
          return r;
        }
      };
      r.add(task);
    }
    return r;
  }


  /**
   *
   * Get symbol list
   *
   * @param symbol
   * @return
   */
  private SymbolCurrentState getSymbolData(String symbol) {
    HttpsClientUtil dude = new HttpsClientUtil();
    SymbolCurrentState r = null;

    try {
      InputStream yahooDataPage = dude.getYahooDataPage(symbol);
      try {
        r = dude.getSymbolData(yahooDataPage, symbol);
      } catch (Exception e) {
        e.printStackTrace();
      }

      // System.out.println("Price: " + r.getPrice() + " Prev. Close: " +
      // r.getPreviousClose() + " Percent changed: " + r.getChangedPercent());
      yahooDataPage.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return r;
  }

}
