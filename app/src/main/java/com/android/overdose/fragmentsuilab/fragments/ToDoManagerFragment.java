package com.android.overdose.fragmentsuilab.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.android.overdose.fragmentsuilab.R;
import com.android.overdose.fragmentsuilab.models.ToDoItem;
import com.android.overdose.fragmentsuilab.adapters.ToDoListAdapter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;

/**
 * A fragment representing a list of Items.
 * Activities containing this fragment MUST implement the {@link ToDoManagerFragment.OnFragmentInteractionListener}
 * interface.
 */
public class ToDoManagerFragment extends ListFragment {

    private OnFragmentInteractionListener mListener;
    private static final String FILE_NAME = "TodoManagerActivityData.txt";
    private static final String TAG = "Lab-UserInterface";

    ToDoListAdapter mAdapter;

    public static ToDoManagerFragment newInstance(String param1, String param2) {
        ToDoManagerFragment fragment = new ToDoManagerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ToDoManagerFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO
        // Create a new TodoListAdapter for this ListActivity's ListView
        // and assign it to the mAdapter variable
        //Hint: use getActivity to get the Context
        mAdapter = new ToDoListAdapter(getActivity());

        //TODO:
        //Attach the adapter to this ListActivity's ListView via the setListAdapter method
        setListAdapter(mAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Put divider between ToDoItems and FooterView
        getListView().setFooterDividersEnabled(true);

        //TODO:
        //Inflate footerView for footer_view.xml file
        //Use getActivity().getLayoutInflater() and the .inflate method
        TextView footerView = null;
        footerView = (TextView) getActivity().getLayoutInflater().inflate(R.layout.footer_view,null);

        //TODO:
        //Add footerView to ListView  (addFooterView method)
        //Hint: Notice how I get the ListView to set the divider in the lines above.
        getListView().addFooterView(footerView);

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Entered footerView.OnClickListener.onClick()");
                //TODO:
                //Implement OnClick(). Call the listener for the transition
                mListener.onFragmentInteraction();
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction();
    }

    // Do not modify below here

    @Override
    public void onResume() {
        super.onResume();
        // Load saved ToDoItems, if necessary
        if (mAdapter.getCount() == 0)
            loadItems();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Save ToDoItems
        saveItems();

    }

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

    public void addItem(ToDoItem item) {
        mAdapter.add(item);
    }

    public void clearList() {
        mAdapter.clear();
    }

    // Load stored ToDoItems
    private void loadItems() {
        BufferedReader reader = null;
        try {
            FileInputStream fis = getActivity().openFileInput(FILE_NAME);
            reader = new BufferedReader(new InputStreamReader(fis));

            String title = null;
            String priority = null;
            String status = null;
            Date date = null;

            while (null != (title = reader.readLine())) {
                priority = reader.readLine();
                status = reader.readLine();
                date = ToDoItem.FORMAT.parse(reader.readLine());
                mAdapter.add(new ToDoItem(title, ToDoItem.Priority.valueOf(priority),
                        ToDoItem.Status.valueOf(status), date));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // Save ToDoItems to file
    private void saveItems() {
        PrintWriter writer = null;
        try {
            FileOutputStream fos = getActivity().openFileOutput(FILE_NAME, getActivity().MODE_PRIVATE);
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                    fos)));

            for (int idx = 0; idx < mAdapter.getCount(); idx++) {

                writer.println(mAdapter.getItem(idx));

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != writer) {
                writer.close();
            }
        }
    }

    public void dump() {

        for (int i = 0; i < mAdapter.getCount(); i++) {
            String data = ((ToDoItem) mAdapter.getItem(i)).toLog();
            Log.i(TAG,	"Item " + i + ": " + data.replace(ToDoItem.ITEM_SEP, ","));
        }

    }
}
