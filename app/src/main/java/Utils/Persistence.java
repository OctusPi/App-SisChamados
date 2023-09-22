package Utils;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import br.com.dticampossales.appsischamados.R;

public class Persistence {
    public static String getStringVal(Context context, String key){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_key), MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static void setStringVal(Context context, String key, String value) {
        context.getSharedPreferences(context.getString(R.string.preference_key), MODE_PRIVATE)
                .edit()
                .putString(key, value)
                .apply();
    }
}
