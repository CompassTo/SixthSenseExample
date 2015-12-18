/**
 * Copyright (C) 2014 Hugleberry Corp.
 * All rights reserved.
 *
 * @author Cathleen Scharfe
 * @author Jens Franke
 */
package com.compassto.sixthsense.example.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.compassto.sixthsense.sdk.model.LtedMatchKey;
import com.sixthsense.example.R;
import com.compassto.sixthsense.example.data.DataProvider;
import com.compassto.sixthsense.example.data.UserModel;

import java.util.ArrayList;
import java.util.List;


public class UserListAdapter extends BaseAdapter {

    private List<UserModel> mListData;
    private Context mContext;

    public UserListAdapter(Context context) {
        mListData = new ArrayList<>();
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_adapter_user, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.userName = (TextView) convertView.findViewById(R.id.textViewName);
            viewHolder.interests = (TextView) convertView.findViewById(R.id.textViewInterests);
            viewHolder.id = (TextView) convertView.findViewById(R.id.textViewImei);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final UserModel user = getItem(position);

        if (user == null) {
            return convertView;
        }

        String ltedUserName = user.getName();
        if (ltedUserName == null) {
            ltedUserName = "Android User";
        }

        if (user.getId() == DataProvider.getInstance().appUser.getId()) {
            viewHolder.userName.setText(ltedUserName + " (own match)");
        } else {
            viewHolder.userName.setText(ltedUserName);
        }

        viewHolder.id.setText("Device: " + String.valueOf(user.getId()));

        // display matching keys
        List<LtedMatchKey> ownInterests = DataProvider.getInstance().appUser.getInterests();
        List<LtedMatchKey> userInterests = user.getInterests();


        List<String> matchLabels = getMatchLabels(ownInterests, userInterests);

        if (matchLabels != null) {
            String keysString = "";
            for (String label : matchLabels) {
                keysString += "#" + label + " ";
            }
            viewHolder.interests.setText("Matched keys: " + keysString);
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView userName;
        TextView interests;
        TextView id;
    }


    public List<String> getMatchLabels(List<LtedMatchKey> interestsA, List<LtedMatchKey> interestsB) {

        if (interestsA == null || interestsB == null) {
            return null;
        }

        List<String> matchLabels = new ArrayList<>();


        for (LtedMatchKey interestA : interestsA) {

            for (LtedMatchKey interestB : interestsB) {

                if (interestA.getId() == interestB.getId()) {
                    for (String labelA : interestA.getLabels()) {
                        for (String labelB : interestB.getLabels()) {
                            if (labelA.equals(labelB)) {
                                matchLabels.add(labelA);
                            }
                        }
                    }
                }
            }
        }

        return matchLabels;
    }

    public void updateData() {
        mListData = DataProvider.getInstance().getUserList();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (mListData == null) {
            return 0;
        }

        return mListData.size();
    }

    @Override
    public UserModel getItem(int i) {
        return mListData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


}