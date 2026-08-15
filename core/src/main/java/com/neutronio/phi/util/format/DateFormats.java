package com.neutronio.phi.util.format;

import com.neutronio.phi.PhiException;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Describes standard formats for time and date.
 */
public enum DateFormats {

    HOUR_MINUTE("HH:mm:ss"),
    DAY_MONTH_YEAR("dd/MM/YYYY"),
    MONTH_YEAR("MM/YYYY"),
    FULL("dd/MM/YYYY HH:mm");

    private SimpleDateFormat formatter;

    DateFormats(String format) {
        this.formatter = new SimpleDateFormat(format);
    }

    public String format(Date date) {
        if( date == null) throw new IllegalArgumentException("Date cannot be null!");
        return this.formatter.format(date);
    }
}
