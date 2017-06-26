package su.hotty.editor.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FormatHelper {

    private FormatHelper() {
    }

    synchronized public static DateFormat getDateFormat() {
        return new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
    }

    synchronized public static DateFormat getDateWithTimeFormat() {
        return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
    }

    public static final char MONEY_DECIMAL_SEPARATOR = '.';

    public static synchronized DecimalFormat getMoneyFormat() {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator(MONEY_DECIMAL_SEPARATOR);
        DecimalFormat format = new DecimalFormat("###,###,###,##0.00");
        format.setDecimalFormatSymbols(symbols);
        return format;
    }
}
