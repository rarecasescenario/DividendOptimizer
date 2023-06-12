package com.dividendyield.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.stock.utils.StringUtility;

public class DividendYieldTests {
    @Tag("slow")
    @Test
    public void testAddMaxInteger() {
	assertEquals(2147483646, Integer.sum(2147183646, 300000));
    }

    @Tag("fast")
    @Test
    public void testDivide() {
	assertThrows(ArithmeticException.class, () -> {
	    Integer.divideUnsigned(42, 0);
	});
    }

    @Test
    @DisplayName("Testing conversion from String into BigDecimal")
    public void testAmountToBigDecimal() {
	String amount = "256.13";
	BigDecimal bd = StringUtility.string2CurrencyAmount(amount);
	assertThat(bd).isEqualByComparingTo(BigDecimal.valueOf(256.13));
    }
}
