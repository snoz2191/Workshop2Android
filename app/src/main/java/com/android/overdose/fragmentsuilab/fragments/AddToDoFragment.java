package com.android.overdose.fragmentsuilab.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.overdose.fragmentsuilab.R;
import com.android.overdose.fragmentsuilab.models.ToDoItem;
import com.android.overdose.fragmentsuilab.models.ToDoItem.Priority;
import com.android.overdose.fragmentsuilab.models.ToDoItem.Status;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddToDoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddToDoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddToDoFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    // 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
    private static final int SEVEN_DAYS = 604800000;

    private static final String TAG = "Lab-UserInterface";

    private static String timeString;
    private static String dateString;
    private static TextView dateView;
    private static TextView timeView;

    private Date mDate;
    private static View rootView;
    private RadioGroup mPriorityRadioGroup;
    private RadioGroup mStatusRadioGroup;
    private EditText mTitleText;
    private RadioButton mDefaultStatusButton;
    private RadioButton mDefaultPriorityButton;


    public static AddToDoFragment newInstance(String param1, String param2) {
        AddToDoFragment fragment = new AddToDoFragment();
        return fragment;
    }

    public AddToDoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.add_todo_fragment, container, false);

        mTitleText = (EditText) rootView.findViewById(R.id.title);
        mDefaultStatusButton = (RadioButton) rootView.findViewById(R.id.statusNotDone);
        mDefaultPriorityButton = (RadioButton) rootView.findViewById(R.id.medPriority);
        mPriorityRadioGroup = (RadioGroup) rootView.findViewById(R.id.priorityGroup);
        mStatusRadioGroup = (RadioGroup) rootView.findViewById(R.id.statusGroup);
        dateView = (TextView) rootView.findViewById(R.id.date);
        timeView = (TextView) rootView.findViewById(R.id.time);

        // Set the default date and time

        setDefaultDateTime();

        // OnClickListener for the Date button, calls showDatePickerDialog() to
        // show
        // the Date dialog

        final Button datePickerButton = (Button) rootView.findViewById(R.id.date_picker_button);
        datePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // OnClickListener for the Time button, calls showTimePickerDialog() to
        // show the Time Dialog

        final Button timePickerButton = (Button) rootView.findViewById(R.id.time_picker_button);
        timePickerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });

        // OnClickListener for the Cancel Button,

        final Button cancelButton = (Button) rootView.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(TAG, "Entered cancelButton.OnClickListener.onClick()");

                //TODO:
                //Indicate result and finish
                //Hint: onCanceled method of the listener
            }
        });

        final Button resetButton = (Button) rootView.findViewById(R.id.resetButton);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Entered resetButton.OnClickListener.onClick()");

                //TODO: Clear the mTitleText
                //Hint: use getText() and find a way to clear it

                //Reset data to default values
                mStatusRadioGroup.check(mDefaultStatusButton.getId());
                mPriorityRadioGroup.check(mDefaultPriorityButton.getId());
                setDefaultDateTime();
            }
        });


        final Button submitButton = (Button) rootView.findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Entered submitButton.OnClickListener.onClick()");



                //Get the current Priority
                Priority priority = getPriority();

                //Get the current Status
                Status status = getStatus();

                //Get the current ToDoItem Title
                String titleString = getToDoTitle();

                // Construct the Date string
                String fullDate = dateString + " " + timeString;

                //TODO:
                //Return data Intent and finish
                //Hint: Check onSubmit method on the listener
            }
        });

        return rootView;
    }


    public interface OnFragmentInteractionListener {
        public void onSubmit(String titleString, Priority priority, Status status, String fullDate);
        public void onCanceled();
    }

    private Priority getPriority() {

        switch (mPriorityRadioGroup.getCheckedRadioButtonId()) {
            case R.id.lowPriority: {
                return ToDoItem.Priority.LOW;
            }
            case R.id.highPriority: {
                return ToDoItem.Priority.HIGH;
            }
            default: {
                return ToDoItem.Priority.MED;
            }
        }
    }

    private Status getStatus() {

        switch (mStatusRadioGroup.getCheckedRadioButtonId()) {
            case R.id.statusDone: {
                return ToDoItem.Status.DONE;
            }
            default: {
                return ToDoItem.Status.NOTDONE;
            }
        }
    }

    private String getToDoTitle() {
        return mTitleText.getText().toString();
    }

    // Do not modify below this point.

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // DialogFragment used to pick a ToDoItem deadline date

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current date as the default date in the picker

            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            setDateString(year, monthOfYear, dayOfMonth);

            dateView.setText(dateString);
        }

    }

    // DialogFragment used to pick a ToDoItem deadline time

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return
            return new TimePickerDialog(getActivity(), this, hour, minute, true);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            setTimeString(hourOfDay, minute, 0);

            timeView.setText(timeString);
        }
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }


    private void setDefaultDateTime() {

        // Default is current time + 7 days
        mDate = new Date();
        mDate = new Date(mDate.getTime() + SEVEN_DAYS);

        Calendar c = Calendar.getInstance();
        c.setTime(mDate);

        setDateString(c.get(Calendar.YEAR), c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH));

        dateView.setText(dateString);

        setTimeString(c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE),
                c.get(Calendar.MILLISECOND));

        timeView.setText(timeString);
    }

    private static void setDateString(int year, int monthOfYear, int dayOfMonth) {

        // Increment monthOfYear for Calendar/Date -> Time Format setting
        monthOfYear++;
        String mon = "" + monthOfYear;
        String day = "" + dayOfMonth;

        if (monthOfYear < 10)
            mon = "0" + monthOfYear;
        if (dayOfMonth < 10)
            day = "0" + dayOfMonth;

        dateString = year + "-" + mon + "-" + day;
    }

    private static void setTimeString(int hourOfDay, int minute, int mili) {
        String hour = "" + hourOfDay;
        String min = "" + minute;

        if (hourOfDay < 10)
            hour = "0" + hourOfDay;
        if (minute < 10)
            min = "0" + minute;

        timeString = hour + ":" + min + ":00";
    }
}
