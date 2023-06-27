package com.stock.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.stock.data.CurrentPosition;
import com.stock.data.OutputDesicionData;
import com.stock.data.WatchSymbol;
import com.stock.yahoo.SymbolCurrentState;

public class CalculationService {

  private List<WatchSymbol> watchSymbols;
  private List<CurrentPosition> currentPositions;
  // built localy
  private List<String> symbolWorkList;
  // current yahoo data
  private List<SymbolCurrentState> symbolCurrentState;
  private List<OutputDesicionData> desicionData;

  public CalculationService() {
    super();
  }

  public CalculationService(List<WatchSymbol> watchSymbols, List<CurrentPosition> currentPositions,
      List<String> symbolWorkList, List<SymbolCurrentState> symbolCurrentState,
      List<OutputDesicionData> desicionData) {
    super();
    this.watchSymbols = watchSymbols;
    this.currentPositions = currentPositions;
    this.symbolWorkList = symbolWorkList;
    this.symbolCurrentState = symbolCurrentState;
    this.desicionData = desicionData;
  }

  /**
   * 
   * @return
   */
  public List<OutputDesicionData> processData() {

    List<String> workList = getWorkList();

    for (int i = 0; i < watchSymbols.size(); i++) {

      String symbol = watchSymbols.get(i).getSymbol();
      Optional<SymbolCurrentState> symbolState = symbolCurrentState.stream()
          .filter(t -> t.getSymbol().equalsIgnoreCase(symbol)).findFirst();
    }
    return desicionData;
  }

  /**
   * Getting Symbol work list derived from the watch list
   */
  public List<String> getWorkList() {
    if (watchSymbols != null && !watchSymbols.isEmpty()) {
      return watchSymbols.stream().map(t -> t.getSymbol()).collect(Collectors.toList());
    }
    return null;
  }

  public List<WatchSymbol> getWatchSymbols() {
    return watchSymbols;
  }

  public void setWatchSymbols(List<WatchSymbol> watchSymbols) {
    this.watchSymbols = watchSymbols;
  }

  public List<CurrentPosition> getCurrentPositions() {
    return currentPositions;
  }

  public void setCurrentPositions(List<CurrentPosition> currentPositions) {
    this.currentPositions = currentPositions;
  }

  public List<String> getSymbolWorkList() {
    return symbolWorkList;
  }

  // public void setSymbolWorkList(List<String> symbolWorkList) {
  // this.symbolWorkList = symbolWorkList;
  // }

  public List<SymbolCurrentState> getSymbolCurrentState() {
    return symbolCurrentState;
  }

  public void setSymbolCurrentState(List<SymbolCurrentState> symbolCurrentState) {
    this.symbolCurrentState = symbolCurrentState;
  }

  public List<OutputDesicionData> getDesicionData() {
    return desicionData;
  }

  public void setDesicionData(List<OutputDesicionData> desicionData) {
    this.desicionData = desicionData;
  }
}
