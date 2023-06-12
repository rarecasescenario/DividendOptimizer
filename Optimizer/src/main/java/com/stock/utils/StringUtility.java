package com.stock.utils;

import java.math.BigDecimal;

public class StringUtility {

    /** Date format for presentation as 2023-02-15 20:15:56 (24 hour clock) */
    public static String DF_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";

    /** Date format for presentation as 2023-01=28 */
    public static String DF_YYYYMMDD = "yyyy-MM-dd";

    /** Date format for presentation as 0215 */
    public static String DF_MMDD = "MMdd";

    public static String NUMBER_FORMAT_DIGIT_DECIMAL_DIGIT = "#0.00";

    public static BigDecimal string2CurrencyAmount(String amount) {
	if (amount == null || amount.equals("")) {
	    return null;
	}
	return new BigDecimal(amount);
    }

}
