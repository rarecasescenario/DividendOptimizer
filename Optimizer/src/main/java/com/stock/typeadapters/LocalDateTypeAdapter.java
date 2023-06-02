package com.stock.typeadapters;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

    private DateTimeFormatter formatter;

    public LocalDateTypeAdapter() {
	this(DateTimeFormatter.BASIC_ISO_DATE);
    }

    public LocalDateTypeAdapter(DateTimeFormatter formatter) {
	this.formatter = formatter;
    }

    public void setFormat(DateTimeFormatter dateFormat) {
	formatter = dateFormat;
    }

    @Override
    public void write(JsonWriter out, LocalDate date) throws IOException {
	if (date == null) {
	    out.nullValue();
	} else {
	    out.value(formatter.format(date));
	}
    }

    @Override
    public LocalDate read(final JsonReader in) throws IOException {
	switch (in.peek()) {
	case NULL:
	    in.nextNull();
	    return null;
	default:
	    String date = in.nextString();
	    if (date == null || date.trim().isEmpty()) {
		return null;
	    }
	    return LocalDate.parse(date, formatter);
	}
    }
}
