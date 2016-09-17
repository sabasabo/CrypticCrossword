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
        SharedPreferences sharedPreferences = getTempSharedPreferences();
        return sharedPreferences.getAll().get(dataName);
    }

    public static Object readData(String dataName, Object defaultValue) {
        SharedPreferences sharedPreferences = getTempSharedPreferences();
        Object data = sharedPreferences.getAll().get(dataName);
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
        SharedPreferences sharedPreferences = getTempSharedPreferences();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        putDataInEditor(editor, dataName, data);
        editor.commit();
    }

    public static void removeData(String... dataName) {
        for (String name : dataName) {
            saveData(name, null);
        }
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
