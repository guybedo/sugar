package com.akalea.sugar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface Dates {

    public static String isoDateTime(LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static String isoDate(LocalDateTime dt) {
        return dt.format(DateTimeFormatter.ISO_DATE);
    }
}
