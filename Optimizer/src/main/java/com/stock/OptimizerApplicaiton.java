package com.stock;

import java.util.List;

import com.stock.data.CurrentPosition;
import com.stock.data.WatchSymbol;
import com.stock.utils.Utility;

public class OptimizerApplicaiton {

    public static void main(final String[] args) {
	final List<WatchSymbol> sl = Utility.readWatchList();
	final List<CurrentPosition> cpl = Utility.getCurrentPositions();

	sl.forEach(System.out::println);
	cpl.forEach(System.out::println);
    }
}
