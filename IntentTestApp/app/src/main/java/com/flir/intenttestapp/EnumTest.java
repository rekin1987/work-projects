package com.flir.intenttestapp;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by psuszek on 2017-01-19.
 */
public abstract class EnumTest {

    @Retention(SOURCE)
    @IntDef({NAVIGATION_MODE_STANDARD, NAVIGATION_MODE_LIST, NAVIGATION_MODE_TABS})
    public @interface NavigationMode {
    }

    public static final int NAVIGATION_MODE_STANDARD = 0;
    public static final int NAVIGATION_MODE_LIST = 1;
    public static final int NAVIGATION_MODE_TABS = 2;

    public void setNavigationMode(@NavigationMode int mode) {
    }

    @NavigationMode
    public int getNavigationMode() {
        //        return 1;
        return NAVIGATION_MODE_LIST;
    }

    public void testMethod() {
        setNavigationMode(NAVIGATION_MODE_LIST);
        int a = getNavigationMode();
        //NavigationMode b = getNavigationMode();
    }

}
