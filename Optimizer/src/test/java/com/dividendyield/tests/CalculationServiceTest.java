package com.dividendyield.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.stock.data.WatchSymbol;
import com.stock.service.CalculationService;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CalculationServiceTest {

    CalculationService calculationService;
    List<WatchSymbol> watchSymbols;

    @BeforeAll
    public static void setupYahooData() {
	// System.out.println("Setting Yahoo Data");
    }

    @BeforeEach
    public void setup() {
	calculationService = new CalculationService();
	watchSymbols = new ArrayList<>();

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
    }

    @Test
    // @DisplayName("Testing getting symbol work list from the watch list")
    public void testGetWorkList() {

	List<String> wl = calculationService.getWorkList();

//	Assertions.assertFalse(wl.isEmpty());
	assertEquals(2, wl.size());
//	assertThat(new BigDecimal("4.2")).isEqualByComparingTo(ws2.getLowerYield());
	assertThat(2).isEqualByComparingTo(wl.size());
	assertTrue(calculationService.getWorkList().stream().filter(s -> s.equals("TD.TO")).findAny().isPresent());
    }

    // TODO:
    @ParameterizedTest
    @ValueSource(strings = { "BMO.TO", "TD.TO", "CM.TO" })
    public void testGetSymbolData(String symbol) {
	assertTrue(calculationService.getWorkList().stream().filter(s -> s.equals(symbol)).findAny().isPresent());
    }
}
