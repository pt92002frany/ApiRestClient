package edu.sv.ues.dam235.apirestclient.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Guarda el token JWT en SharedPreferences para persistir la sesión
 * entre pantallas y entre reinicios de la app.
 */
public class SessionManager {

    private static final String PREF_NAME = "ApiRestSession";
    private static final String KEY_TOKEN = "token";

    private final SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        prefs.edit().putString(KEY_TOKEN, token).apply();
    }

    public String getToken() {
        return prefs.getString(KEY_TOKEN, null);
    }

    public boolean isLoggedIn() {
        return getToken() != null;
    }

    public void clear() {
        prefs.edit().clear().apply();
    }
}