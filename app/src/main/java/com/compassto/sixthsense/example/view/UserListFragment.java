package com.compassto.sixthsense.example.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sixthsense.example.R;
import com.compassto.sixthsense.example.data.UserModel;

/**
 * @author Cathleen Scharfe
 */
public class UserListFragment extends Fragment implements ListView.OnItemClickListener {

    private UserListAdapter mAdapter;
    private ListView mListView;

    public UserListFragment() {
    }

    public interface OnFragmentInteractionListener {
        void onUserSelected(UserModel user);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_list, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        mListView.setOnItemClickListener(this);
        // set list adapter
        mAdapter = new UserListAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        return view;
    }


    public void updateView() {

        //  mAdapter = new UserListAdapter();
        mAdapter.updateData();
        // mListView.setAdapter(mAdapter);
        //mListView.invalidate();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}