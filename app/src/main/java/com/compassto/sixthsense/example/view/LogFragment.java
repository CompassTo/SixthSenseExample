package com.compassto.sixthsense.example.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.compassto.sixthsense.sdk.helper.LtedLogger;
import com.compassto.sixthsense.example.MainActivity;
import com.sixthsense.example.R;


/**
 * Displays log text entries to retrace program flow
 */
public class LogFragment extends Fragment {

    private TextView tv;
    private View containerView;
    private MainActivity mActivity;

    public static LogFragment newInstance(MainActivity activity) {
        LogFragment fragment = new LogFragment();
        fragment.setCallbackActivity(activity);
        return fragment;
    }

    public void setCallbackActivity(MainActivity activity) {
        mActivity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        containerView = inflater.inflate(R.layout.fragment_log,
                container, false);

        initUIElements();
        updateLog();
        return containerView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        updateLog();
    }


    private void initUIElements() {

        // containerView = inflater.inflate(R.layout.fragment_log,
        // fragmentContainer, false);
        tv = (TextView) containerView.findViewById(R.id.idLogTextView);

        // auto scroll to bottom
        final ScrollView scrollVw = (ScrollView) containerView
                .findViewById(R.id.logScrollView);
        if (scrollVw != null) {
            scrollVw.post(new Runnable() {
                @Override
                public void run() {
                    scrollVw.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }

    public void updateLog() {
        LtedLogger.getInstance().getLogs();
        if (tv != null) {
            tv.setText(LtedLogger.getInstance().getLogs());
        }
    }
}
