package com.dividendyield.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.stock.utils.Utility;

public class LocalDateTest {

    @BeforeAll
    public static void setupAll() {
	System.out.println("JUnit Tests started...");
    }

    @Test
    @DisplayName("Testing Dividend Date")
    public void testDividendDate() {

	String sDividendDate = "0528";

	assertEquals(1, 2);
    }

    @Test
    @DisplayName("Testing Current Local Date formatting")
    public void testLocalDateFormatting() {
	LocalDateTime ld = LocalDateTime.now();
	Utility util = new Utility();
	String todayIs = util.getDateTimeFormatter().format(ld);
	System.out.println(util.getDateTimeFormatter().format(ld));
	assertEquals("2023-06-11 17:55:10", todayIs);
    }
}
