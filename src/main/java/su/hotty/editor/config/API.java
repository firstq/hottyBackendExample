package su.hotty.editor.config;

public class API {

    public static final String APP = "/hotty";

    public static final String PATH = APP + "/api/v1";

    public static final int MAX_NUMBER_OF_RETURNING_RECORDS = 1000;

    public static final long ONE_DAY_MILLIS = 24L * 60L * 60L * 1000L;

    public static final long ONE_YEAR_MILLIS = 366L * ONE_DAY_MILLIS;

    private API() {
    }
}
