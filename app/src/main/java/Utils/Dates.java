package Utils;

import android.annotation.SuppressLint;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Dates {

    public static String fmtLocal(String utcDate){

        try {
            @SuppressLint("SimpleDateFormat") DateFormat fmtUTC  = new SimpleDateFormat("yyyy-MM-dd");
            Date dateUTC = fmtUTC.parse(utcDate);
            @SuppressLint("SimpleDateFormat") DateFormat fmtPTBR = new SimpleDateFormat("dd/MM/yyyy");
            assert dateUTC != null;
            return fmtPTBR.format(dateUTC);

        } catch (ParseException e) {
            e.printStackTrace();
            return "**.**.****";
        }
    }

    public static String fmtLocalTime(String utcDateTime){

        try {
            @SuppressLint("SimpleDateFormat") DateFormat fmtUTC  = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date dateUTC = fmtUTC.parse(utcDateTime);
            @SuppressLint("SimpleDateFormat") DateFormat fmtPTBR = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            assert dateUTC != null;
            return fmtPTBR.format(dateUTC);

        } catch (ParseException e) {
            e.printStackTrace();
            return "**.**.****";
        }
    }
}