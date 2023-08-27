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
            @SuppressLint("SimpleDateFormat") DateFormat fmtPTBR = new SimpleDateFormat("dd-MM-yyyy");
            assert dateUTC != null;
            return fmtPTBR.format(dateUTC);

        } catch (ParseException e) {
            e.printStackTrace();
            return "**.**.****";
        }
    }
}