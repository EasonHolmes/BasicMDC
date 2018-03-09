package com.utils.library.http;

import com.google.gson.Gson;

/**
 * Created by cuiyang on 2017/1/17.
 */

public class GsonSingle {

    private static Gson Gsons;


    private GsonSingle() {

        Gsons = new Gson();

    }

    public static Gson getInstance() {
        if (Gsons == null) {
            synchronized (GsonSingle.class) {
                if (Gsons == null) {
                    new GsonSingle();
                }
            }
        }
        return Gsons;
    }
}
