package com.automatica.AXCPT.Servicios;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;
import java.util.Calendar;

import androidx.fragment.app.DialogFragment;

public  class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener
{
    private DatePickerDialog.OnDateSetListener listener;



    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;

    }

    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
       DatePickerFragment fragment = new DatePickerFragment();

        fragment.setListener(listener);
        return fragment;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day)
    {

        // Do something with the date chosen by the user
    }
}