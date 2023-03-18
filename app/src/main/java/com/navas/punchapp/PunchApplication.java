
package com.navas.punchapp;

import android.app.Application;

public class PunchApplication extends Application {
    public final static String WIDGET_ACTION = "BUTTON_CLICK";
    public static final String PREFS_NAME = "com.navas.punchapp.PunchWidgetProvider";
    public static final String KEY_IS_CHECKED_IN = "isCheckedIn";
    public static final String USER = "Navas";

    public boolean isCheckedIn = false;
}
