package com.dividendyield.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.stock.data.WatchSymbol;
import com.stock.service.CalculationService;

public class CalculationServiceTest {

    @Test
    @DisplayName("Testing getting symbol work list from the watch list")
    public void testGetWorkList() {

	CalculationService calculationService = new CalculationService();

	List<WatchSymbol> watchSymbols = new ArrayList<>();
	WatchSymbol ws1 = new WatchSymbol();
	ws1.setSymbol("TD.TO");
	ws1.setQuoterlyDividendAmount(new BigDecimal("0.96"));
	ws1.setUpperYield(new BigDecimal("4.95"));
	ws1.setLowerYield(new BigDecimal("4.6"));
	ws1.setDividendDate1("0107");
	ws1.setDividendDate2("0404");
	ws1.setDividendDate3("0707");
	ws1.setDividendDate4("1007");

	WatchSymbol ws2 = new WatchSymbol();
	ws2.setSymbol("BMO.TO");
	ws2.setQuoterlyDividendAmount(new BigDecimal("1.43"));
	ws2.setUpperYield(new BigDecimal("4.95"));
	ws2.setLowerYield(new BigDecimal("4.2"));
	ws2.setDividendDate1("0131");
	ws2.setDividendDate2("0429");
	ws2.setDividendDate3("0729");
	ws2.setDividendDate4("1029");

	watchSymbols.add(ws1);
	watchSymbols.add(ws2);

	calculationService.setWatchSymbols(watchSymbols);
	List<String> wl = calculationService.getWorkList();

//	Assertions.assertFalse(wl.isEmpty());
	assertEquals(2, wl.size());
	assertThat(new BigDecimal("4.2")).isEqualByComparingTo(ws2.getLowerYield());
	assertThat(2).isEqualByComparingTo(wl.size());
    }
}
