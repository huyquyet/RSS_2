package framgia.vn.readrss.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import framgia.vn.readrss.stringInterface.StringFormatDate;

public class FormatDate implements StringFormatDate {
    /**
     * Format StringFormatDate to String Format dd/MM/yyyy HH:mm:ss
     *
     * @param date
     * @return String format " dd/MM/yyyy HH:mm:ss "
     */
    public String formatDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_DATE);
        return dateFormat.format(date);
    }

    /**
     * Convert String datetime format "EEE, d MMM yyyy HH:mm:ss Z" to format StringFormatDate
     *
     * @param data
     * @return Datetime format
     * @throws ParseException
     */
    public Date formatDate(String data) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(FORMAT_XML, Locale.ENGLISH);
        Date date = dateFormat.parse(data);
        System.out.println(date);
        return date;
    }
}
