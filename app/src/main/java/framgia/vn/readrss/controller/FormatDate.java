package framgia.vn.readrss.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by FRAMGIA\nguyen.huy.quyet on 22/03/2016.
 */
public class FormatDate {

    /**
     * Format Date to String Format dd/MM/yyyy HH:mm:ss
     *
     * @param date
     * @return String format " dd/MM/yyyy HH:mm:ss "
     */
    public String formatDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return dateFormat.format(date);
    }

    /**
     * Convert String datetime format "EEE, d MMM yyyy HH:mm:ss Z" to format Date
     *
     * @param data
     * @return Datetime format
     * @throws ParseException
     */
    public Date formatDate(String data) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        Date date = dateFormat.parse(data);
        System.out.println(date);
        return date;
    }

}
