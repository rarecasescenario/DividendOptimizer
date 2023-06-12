package com.stock;

import java.time.LocalDateTime;

import com.stock.utils.Utility;

public class TestNow {

    public static void main(String[] args) {
	LocalDateTime ld = LocalDateTime.now();
	Utility util = new Utility();
	String todayIs = util.getDateTimeFormatter().format(ld);
	System.out.println(todayIs);
	// System.out.println(util.getDateTimeFormatter().format(ld));
    }

}
