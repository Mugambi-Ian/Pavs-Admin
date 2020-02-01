package com.nenecorp.pavsadmin.Utility.Resources;

import com.nenecorp.pavsadmin.Interface.AdminUi.Home;

public class Cache {
    private static Home home;

    public static Home getHome() {
        return home;
    }


    public static void setHome(Home home) {
        Cache.home = home;
    }
}