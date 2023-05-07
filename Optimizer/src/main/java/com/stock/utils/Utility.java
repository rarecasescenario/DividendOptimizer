package com.stock.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.stock.data.CurrentPosition;
import com.stock.data.WatchSymbol;

public class Utility {

    /**
     * Getting a watch list
     *
     * @return
     */
    public static List<WatchSymbol> readWatchList() {
	final List<WatchSymbol> r = new ArrayList<>();
	final String fileName = "C:\\AV\\WorkProjects\\DividendOptimizer\\Optimizer\\src\\main\\resources\\WatchList.csv";

	try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

	    String line;
	    int i = 0;
	    while ((line = br.readLine()) != null) {
		// System.out.println(line);
		i++;
		if (i == 1) {
		    continue;
		}

		final WatchSymbol ws = new WatchSymbol();
		final String[] data = line.split(",");

		ws.setSymbol(data[0]);

		// convert quarterly dividend into BigDecimal
		BigDecimal qd = new BigDecimal(0);
		if (data[1].length() > 0) {
		    qd = new BigDecimal(data[1]);
		}
		ws.setQuoterlyDividendAmount(qd);

		// UpperYield
		BigDecimal uy = new BigDecimal(0);
		if (data[2].length() > 0) {
		    uy = new BigDecimal(data[2]);
		}
		ws.setUpperYield(uy);

		// LowerYield
		BigDecimal ly = new BigDecimal(0);
		if (data[3].length() > 0) {
		    ly = new BigDecimal(data[2]);
		}
		ws.setLowerYield(ly);

		// dividend date
		ws.setDividendDate1(data[3]);
		ws.setDividendDate2(data[4]);
		ws.setDividendDate3(data[5]);
		ws.setDividendDate4(data[6]);

		r.add(ws);
	    }

	} catch (final IOException e) {
	    e.printStackTrace();
	}
	return r;
    }

    /**
     * Getting current positions
     *
     * @return
     */
    public static List<CurrentPosition> getCurrentPositions() {
	final List<CurrentPosition> r = new ArrayList<>();
	r.add(new CurrentPosition("TD.TO", 1150));
	r.add(new CurrentPosition("BMO.TO", 419));
	r.add(new CurrentPosition("CM.TO", 300));
	r.add(new CurrentPosition("SU.TO", 300));
	return r;
    }
}
