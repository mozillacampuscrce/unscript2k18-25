package com.example.ht.unscript;

import android.os.Environment;

/**
 * Created by Dell on 25/03/2018.
 */

public class CheckForSDCard {
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
