package com.android.overdose.fragmentsuilab;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.overdose.fragmentsuilab.fragments.AddToDoFragment;
import com.android.overdose.fragmentsuilab.fragments.ToDoManagerFragment;
import com.android.overdose.fragmentsuilab.models.ToDoItem;
import com.android.overdose.fragmentsuilab.models.ToDoItem.Priority;
import com.android.overdose.fragmentsuilab.models.ToDoItem.Status;

public class AddToDoActivity extends Activity implements AddToDoFragment.OnFragmentInteractionListener{

	// 7 days in milliseconds - 7 * 24 * 60 * 60 * 1000
	private static final int SEVEN_DAYS = 604800000;

	private static final String TAG = "Lab-UserInterface";
    private static AddToDoFragment maddToDoFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_todo_activity);

        //TODO: Create a new instance of AddToDoFragment and store it on the mManagerFragment
        //variable
        maddToDoFragment = new AddToDoFragment();

        //TODO: Get a Reference to the Fragment Manager using getFragmentManager()
        FragmentManager manager = getFragmentManager();
        //TODO: Create a Fragment Transaction using the Fragment Manager's beginTransaction() method
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        //TODO: Add to the transaction the Fragment to be added on R.id.manager and commit it
        fragmentTransaction.add(R.id.add_todo, maddToDoFragment);
        fragmentTransaction.commit();
	}

    @Override
    public void onSubmit(String titleString, Priority priority, Status status, String fullDate) {
        // Package ToDoItem data into an Intent
        Intent data = new Intent();
        ToDoItem.packageIntent(data, titleString, priority, status,
                fullDate);

        //Return data Intent and finish
        setResult(RESULT_OK,data);
        finish();
    }

    @Override
    public void onCanceled() {
        finish();
    }


}
