package com.aunthtoo.timetableforcupyay.validatemonth;

import android.content.Context;

import com.aunthtoo.timetableforcupyay.prefmanager.PrefManager;

/**
 * Created by Aunt Htoo on 11/29/2017.
 */

public class Validate {

    PrefManager prefManager;
    Context context;

    public Validate(Context context) {
        this.context = context;
        prefManager = new PrefManager(context);
    }

    public boolean isValidateMonth(String month) {

        return prefManager.getVALID_MONTH().contains(month);
    }
}
