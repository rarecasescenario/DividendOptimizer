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
	List<WatchSymbol> r = new ArrayList<>();
	String fileName = "C:\\AV\\WorkProjects\\DividendOptimizer\\Optimizer\\src\\main\\resources\\WatchList.csv";

	try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {

	    String line;
	    int i = 0;
	    while ((line = br.readLine()) != null) {
		// System.out.println(line);
		i++;
		if (i == 1) {
		    continue;
		}

		WatchSymbol ws = new WatchSymbol();
		String[] data = line.split(",");

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
		    ly = new BigDecimal(data[3]);
		}
		ws.setLowerYield(ly);

		// dividend date
		ws.setDividendDate1(data[3]);
		ws.setDividendDate2(data[4]);
		ws.setDividendDate3(data[5]);
		ws.setDividendDate4(data[6]);

		r.add(ws);
	    }

	} catch (IOException e) {
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
	List<CurrentPosition> r = new ArrayList<>();
//	r.add(new CurrentPosition("CM.TO", 530));
//	r.add(new CurrentPosition("BMO.TO", 300));
//	r.add(new CurrentPosition("TD.TO", 280));
	r.add(new CurrentPosition("SU.TO", 600));
	r.add(new CurrentPosition("ENB.TO", 650));
//	r.add(new CurrentPosition("SHOP.TO", 172));
//	r.add(new CurrentPosition("IAG.TO", 160));
	return r;
    }

    public static BigDecimal getAvailableCash() {
	return BigDecimal.valueOf(15054);
    }
}
