package com.example.moneycontrol;

import java.util.Calendar;

import android.app.Dialog;
import android.os.Bundle;
import android.widget.TimePicker;
//
//public static class TimePickerFragment extends DialogFragment implements
//		TimePickerDialog.OnTimeSetListener {
//
//	@Override
//	public Dialog onCreateDialog(Bundle savedInstanceState) {
//		// Use the current time as the default values for the picker
//		final Calendar c = Calendar.getInstance();
//		int hour = c.get(Calendar.HOUR_OF_DAY);
//		int minute = c.get(Calendar.MINUTE);
//
//		// Create a new instance of TimePickerDialog and return it
//		return new TimePickerDialog(getActivity(), this, hour, minute,
//				DateFormat.is24HourFormat(getActivity()));
//	}
//
//	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//		// Do something with the time chosen by the user
//	}
//}