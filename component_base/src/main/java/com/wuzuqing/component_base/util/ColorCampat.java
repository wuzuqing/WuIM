package com.wuzuqing.component_base.util;


import com.wuzuqing.component_base.constants.BaseApplication;

public class ColorCampat {

    public static int getColor( int color){
        return BaseApplication.getAppContext().getResources().getColor(color);
    }
}
