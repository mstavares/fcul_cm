package pdb.cm.fc.ul.pt.pdb.preferences;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import static android.content.Context.MODE_PRIVATE;

public class UserPreferences {

    private static final String USER = "user";
    private static final String EMAIL = "email";


    public static void pushLogin(Context context, String email, String user) {
        pushData(context, EMAIL, email);
        pushData(context, USER, user);

    }

    public static void pushData(Context context, String flag, String data) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(flag, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(flag, data);
        editor.apply();
    }

    public static String getUser(Context context) {
        return getData(context, USER);
    }

    public static String getEmail(Context context) {
        return getData(context, EMAIL);
    }

    public static void clearLogin(Context context) {
        SharedPreferences mySPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = mySPrefs.edit();
        editor.remove(EMAIL);
        editor.remove(USER);
        editor.apply();
    }

    private static String getData(Context context, String flag) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(flag, MODE_PRIVATE);
        return sharedPreferences.getString(flag, "null");
    }

}
