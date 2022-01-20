package my.edu.utem.ftmk.workshop2.visitor.monitoring.system.v1;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Vector;

public class PrefConfig {

    private static final String TAG = "PQ";
    public static final String LIST_KEY = "list_key";

    public static void writeListInPref(Context context, List<Premise> mPremiseV) {

        Log.d(TAG, "PrefConfig.writeListInPref");

        Log.d(TAG, "PrefConfig.writeListInPref.context: " + context + " mPremiseV: " + mPremiseV);
        Gson gson = new Gson();
        String json_s = gson.toJson(mPremiseV);
        Log.d(TAG, "PrefConfig.writeListInPref.json_s: " + json_s);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putString(LIST_KEY, json_s);
        editor.apply();
    }

    public static Vector<Premise> readListFromPref_mPremiseV(Context context) {
        Log.d(TAG, "PrefConfig.readListFromPref_mPremiseV");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d(TAG, "PrefConfig.readListFromPref_mPremiseV.sharedPrefs: "+ sharedPrefs);
        String json_s = sharedPrefs.getString(LIST_KEY, "");
        Log.d(TAG, "PrefConfig.readListFromPref_mPremiseV.json_s: " + json_s);
        Gson gson = new Gson();
        Type type = new TypeToken<Vector<Premise>>() { }.getType();
        Log.d(TAG, "PrefConfig.readListFromPref_mPremiseV.type: " + type);
        Vector<Premise> mPremiseV = gson.fromJson(json_s, type);
        Log.d(TAG, "PrefConfig.readListFromPref_mPremiseV.mPremiseV: " + mPremiseV);

        return mPremiseV;
    }
}
