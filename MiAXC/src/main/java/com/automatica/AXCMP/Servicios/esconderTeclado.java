package com.automatica.AXCMP.Servicios;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class esconderTeclado
{
    public esconderTeclado(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
