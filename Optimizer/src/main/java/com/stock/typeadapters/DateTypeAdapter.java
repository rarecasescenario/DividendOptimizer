package com.stock.typeadapters;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class DateTypeAdapter extends TypeAdapter<Date> {

    private DateFormat dateFormat;

    public DateTypeAdapter() {
    }

    public DateTypeAdapter(DateFormat dateFormat) {
	this.dateFormat = dateFormat;
    }

    public void setFormat(DateFormat dateFormat) {
	this.dateFormat = dateFormat;
    }

    @Override
    public void write(JsonWriter out, Date date) throws IOException {
	if (date == null) {
	    out.nullValue();
	} else {
	    String value;
	    if (dateFormat != null) {
		value = dateFormat.format(date);
	    } else {
		value = ISO8601Utils.format(date, true);
	    }
	    out.beginArray().value(value);
	}

    }

    @Override
    public Date read(JsonReader in) throws IOException {
	switch (in.peek()) {
	case NULL:
	    in.nextNull();
	    return null;
	default:
	    String date = in.nextString();
	    try {
		if (dateFormat != null) {
		    return dateFormat.parse(date);
		}
		return ISO8601Utils.parse(date, new ParsePosition(0));
	    } catch (ParseException e) {
		throw new JsonParseException(e);
	    }
	}
    }
}
