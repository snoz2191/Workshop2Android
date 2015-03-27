package com.android.overdose.fragmentsuilab;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import com.android.overdose.fragmentsuilab.fragments.ToDoManagerFragment;
import com.android.overdose.fragmentsuilab.models.ToDoItem;

public class ToDoManagerActivity extends FragmentActivity implements ToDoManagerFragment.OnFragmentInteractionListener {

	private static final int ADD_TODO_ITEM_REQUEST = 0;
	private static final String TAG = "Lab-UserInterface";

	// IDs for menu items
	private static final int MENU_DELETE = Menu.FIRST;
	private static final int MENU_DUMP = Menu.FIRST + 1;

    ToDoManagerFragment mManagerFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.todomanager_activity);

        //TODO: Create a new instance of ToDOManagerFragment and store it on the mManagerFragment
        //variable
        mManagerFragment = null;

        //TODO: Get a Reference to the Fragment Manager using getFragmentManager()

        //TODO: Create a Fragment Transaction using the Fragment Manager's beginTransaction() method

        //TODO: Add to the transaction the Fragment to be added on R.id.manager and commit it


	}

    @Override
    public void onFragmentInteraction() {
        //TODO: Implement Fragment Interaction
        //Create a new Intent and call the startActivityForResult method
        //with the 2nd parameter being ADD_TODO_ITEM_REQUEST
        Intent addToDoActivityIntent = new Intent(this, AddToDoActivity.class);
        startActivityForResult(addToDoActivityIntent, ADD_TODO_ITEM_REQUEST);
    }

	// Do not modify below here

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.i(TAG,"Entered onActivityResult()");

        //Check result code and request code
        // if user submitted a new ToDoItem
        // Create a new ToDoItem from the data Intent
        // and then add it to the adapter trough the Fragment's addItem method
        if ( requestCode == ADD_TODO_ITEM_REQUEST && resultCode == RESULT_OK ) {
            ToDoItem item = new ToDoItem(data);
            mManagerFragment.addItem(item);
        }
    }

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, MENU_DELETE, Menu.NONE, "Delete all");
		menu.add(Menu.NONE, MENU_DUMP, Menu.NONE, "Dump to log");
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case MENU_DELETE:
            mManagerFragment.clearList();
			return true;
		case MENU_DUMP:
			mManagerFragment.dump();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}