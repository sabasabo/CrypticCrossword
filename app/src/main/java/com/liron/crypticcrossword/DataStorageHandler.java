package com.liron.crypticcrossword;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by lir on 16/09/2016.
 */
public class DataStorageHandler {

    public static final String RIGHT_MARGIN_RATIO = "rightMarginRatio";
    public static final String TOP_MARGIN_RATIO = "topMarginRatio";
    public static final String WIDTH_RATIO = "widthRatio";
    public static final String HEIGHT_RATIO = "heightRatio";
    public static final String GRID_TEXT = "gridText";
    public static final String IS_SAVED_LOCATION = "isSavedLocation";
    public static final String NUM_OF_ROWS = "numOfRows";
    public static final String NUM_OF_COLUMNS = "numOfColumns";

    private static Activity activityContext;

    public static Object readData(String dataName) {
        return getTempSharedPreferences().getAll().get(dataName);
    }

    public static Object readData(String dataName, Object defaultValue) {
        Object data = getTempSharedPreferences().getAll().get(dataName);
        if (data == null) {
            return defaultValue;
        }
        return data;
    }

    private static SharedPreferences getTempSharedPreferences() {
        return activityContext.getSharedPreferences(
                activityContext.getString(R.string.tempMemory), Context.MODE_PRIVATE);
    }

    public static void saveData(String dataName, Object data) {
        SharedPreferences.Editor editor = getTempSharedPreferences().edit();
        putDataInEditor(editor, dataName, data);
        editor.commit();
    }

    public static void removeOldData() {
        SharedPreferences.Editor editor = getTempSharedPreferences().edit();
        editor.clear();
        editor.commit();
    }

    private static void putDataInEditor(SharedPreferences.Editor editor, String dataName, Object data) {
        if (data instanceof Boolean) {
            editor.putBoolean(dataName, (Boolean) data);
        } else if (data instanceof Integer) {
            editor.putInt(dataName, (Integer) data);
        } else if (data instanceof String) {
            editor.putString(dataName, (String) data);
        } else if (data instanceof Float) {
            editor.putFloat(dataName, (Float) data);
        }
    }

    public static void init(Activity context) {
        activityContext = context;
    }

}
