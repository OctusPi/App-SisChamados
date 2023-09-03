package br.com.dticampossales.appsischamados.controllers;

import android.content.Context;

import br.com.dticampossales.appsischamados.R;

public class LoginController {
    public static void setLoginHash(Context context, String key, String hash) {
        context.getSharedPreferences(context.getString(R.string.preference_key), Context.MODE_PRIVATE)
                .edit()
                .putString(key, hash)
                .apply();
    }

    public static String getLoginHash(Context context, String key) {
        return context.getSharedPreferences(context.getString(R.string.preference_key), Context.MODE_PRIVATE)
                .getString(key, "");
    }
}
