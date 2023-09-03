package Utils;

import static android.content.Context.MODE_PRIVATE;
import android.content.Context;
import android.content.SharedPreferences;
import java.security.NoSuchAlgorithmException;
import br.com.dticampossales.appsischamados.R;

public class Security {

    public static String hashLogin(String email, String password) throws NoSuchAlgorithmException {
        return new StringBuilder(email+"-"+password).reverse().toString();
    }

    public static String getHashLogin(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_key), MODE_PRIVATE);
        return sharedPref.getString(context.getString(R.string.is_hash_login), "");
    }

    public static void setHashLogin(Context context, String hashLogin) {
        context.getSharedPreferences(context.getString(R.string.preference_key), MODE_PRIVATE)
                .edit()
                .putString(context.getString(R.string.is_hash_login), hashLogin)
                .apply();
    }

}
