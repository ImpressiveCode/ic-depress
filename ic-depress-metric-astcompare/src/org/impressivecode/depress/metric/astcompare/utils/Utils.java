package org.impressivecode.depress.metric.astcompare.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeLogger;

/**
 * @author Piotr Mitka
 */
public class Utils {

    public static final String DATE_FROM = "dateFrom";
    public static final String DATE_TO = "dateTo";
    public static final String WEEKS = "weeks";
    public static final String PROJECTS_NAMES = "projectsNames";
    public static final String DEFAULT_VALUE = "";
    private static final NodeLogger logger = NodeLogger.getLogger(Utils.class);
    private static Calendar calendar = Calendar.getInstance();
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public static String getCurrentDate() {
        return dateFormat.format(calendar.getTime());
    }

    public static String getCurrentDayPlus(int field, int amount) {
        Calendar tempcalendar = (Calendar) calendar.clone();
        tempcalendar.add(field, amount);
        return dateFormat.format(tempcalendar.getTime());
    }

    public static String getDateAsString(long timeInMilis) {
        Calendar tempcalendar = (Calendar) calendar.clone();
        tempcalendar.setTimeInMillis(timeInMilis);
        return dateFormat.format(tempcalendar.getTime());
    }

    public static void validateDate(String date) throws InvalidSettingsException {
        Date dateToCheck = null;
        try {
            dateFormat.setLenient(false);
            dateToCheck = dateFormat.parse(date);
        } catch (ParseException e) {
            dateToCheck = null;
        }

        if (dateToCheck == null || !date.matches("\\d{2}-\\d{2}-\\d{4}"))
            throw new InvalidSettingsException("Wrong date format! Please use DD-MM-YYYY");
    }

    public static long getTime(String date) {
        try {
            return dateFormat.parse(date.trim()).getTime();
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    public static long addWeeksToDate(long timeInMilis, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMilis);
        calendar.add(Calendar.WEEK_OF_YEAR, amount);
        return calendar.getTimeInMillis();
    }

    public static int getWeeksAsInteger(String weeks) {
        switch (weeks) {
        case "2":
            return 2;
        case "4":
            return 4;
        case "8":
            return 8;
        default:
            return 1000;
        }
    }

    public static void saveStreamAsFile(InputStream inputStream, File outputFile) throws IOException {
        OutputStream outputStream = new FileOutputStream(outputFile);
        saveFile(inputStream, outputStream);
    }

    private static void saveFile(InputStream is, OutputStream os) throws IOException {
        int read = 0;
        byte[] bytes = new byte[1024];

        while ((read = is.read(bytes)) != -1) {
            os.write(bytes, 0, read);
        }
    }

}
