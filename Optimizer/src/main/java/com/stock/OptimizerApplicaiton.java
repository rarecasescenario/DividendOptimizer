package com.stock;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import com.stock.data.CurrentPosition;
import com.stock.data.WatchSymbol;
import com.stock.utils.Utility;
import com.stock.yahoo.SymbolCurrentState;

public class OptimizerApplicaiton {

    private static ArrayBlockingQueue<String> symbolQueue = null;
    private final CopyOnWriteArrayList<SymbolCurrentState> csl = new CopyOnWriteArrayList<>();

    public static void main(final String[] args) {
	final List<WatchSymbol> sl = Utility.readWatchList();
	final List<CurrentPosition> cpl = Utility.getCurrentPositions();

	sl.forEach(System.out::println);
	cpl.forEach(System.out::println);

	final List<String> workList = sl.stream().map(t -> t.getSymbol()).collect(Collectors.toList());
	workList.forEach(System.out::println);

	symbolQueue = new ArrayBlockingQueue<>(workList.size());
	symbolQueue.addAll(workList);

    }
}
