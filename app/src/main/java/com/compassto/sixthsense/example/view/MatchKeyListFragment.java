package com.compassto.sixthsense.example.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.compassto.sixthsense.sdk.model.LtedMatchKey;
import com.compassto.sixthsense.example.MainActivity;
import com.sixthsense.example.R;
import com.compassto.sixthsense.example.data.DataProvider;

import java.util.ArrayList;
import java.util.List;


public class MatchKeyListFragment extends Fragment {

    private ExpandableListView mListView;
    private MatchKeyListAdapter mListAdapter;
    private TextView mApplyButton;
    private boolean mKeysChanged;

    public MatchKeyListFragment() {
    }

    public void updateButtonState(String state, boolean enable) {
        mApplyButton.setEnabled(enable);
        mApplyButton.setText(state);
        mApplyButton.invalidate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View containerView = inflater.inflate(R.layout.fragment_matchkeys,
                container, false);

        containerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mListView = (ExpandableListView) containerView.findViewById(R.id.listView);

        mListAdapter = new MatchKeyListAdapter(DataProvider.getInstance().globalKeys, getActivity());
        mListView.setAdapter(mListAdapter);

        mApplyButton = (TextView) containerView.findViewById(R.id.buttonApply);
        mApplyButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).onApplyKeys(mListAdapter.getSelectedItems());
            }
        });

        mKeysChanged = false;

        return containerView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //expand all groups
        for (int i = 0; i < mListAdapter.getGroupCount(); i++) {
            mListView.expandGroup(i);
        }
    }

    public void onChangeKey() {
        mKeysChanged = true;
        mApplyButton.setText("UPDATE MATCH KEYS");
        mApplyButton.setEnabled(true);
    }

    //--------------------------------------------------------------------------
    //
    //  Adapter
    //
    //--------------------------------------------------------------------------
    private static class ChildHolder {
        TextView title;
        CheckBox checkbox;

        public ChildHolder(View view) {
            checkbox = (CheckBox) view.findViewById(R.id.checkbox);
            title = (TextView) view.findViewById(R.id.name);
        }
    }


    private static class GroupHolder {
        TextView title;
        LinearLayout container;
    }


    public interface IKeyAdapterInteracitonListener {
        void onChangeKey(String argument, String label, boolean selected);
    }

    private class MatchKeyListAdapter extends BaseExpandableListAdapter {
        private final LayoutInflater mInflater;
        private List<LtedMatchKey> mItems;
        private List<LtedMatchKey> mSelectedItems;
        private Context mContext;
        private IKeyAdapterInteracitonListener mInteractionListener;

        public MatchKeyListAdapter(List<LtedMatchKey> items, Context context) {
            mItems = items;
            mContext = context;
            mInflater = LayoutInflater.from(mContext);
            mSelectedItems = new ArrayList<>();
        }

        public void setIneractionListener(IKeyAdapterInteracitonListener listener) {
            mInteractionListener = listener;
        }

        public List<LtedMatchKey> getSelectedItems() {
            return mSelectedItems;
        }

        @Override
        public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ChildHolder holder;
            final String item = getChild(groupPosition, childPosition);
            final LtedMatchKey matchKey = getGroup(groupPosition);


            if (convertView == null) {

                convertView = mInflater.inflate(R.layout.item_adapter_interest_static, parent, false);
                holder = new ChildHolder(convertView);
                holder.title.setText(item);

                convertView.setClickable(false);


                holder.checkbox.setFocusable(false);
                holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                        boolean keyExists = false;
                        // get related key for label

                        for (LtedMatchKey key : mSelectedItems) {
                            // find label in selected key items
                            if (key.getId() == matchKey.getId()) {
                                keyExists = true;
                                if (checked) {
                                    if (!key.getLabels().contains(item)) {
                                        key.addLabel(item);
                                    }
                                } else {
                                    if (key.getLabels().contains(item)) {
                                        key.removeLabel(item);
                                    }
                                }
                                onChangeKey();
                                break;
                            }
                        }

                        if (!keyExists) {
                            LtedMatchKey newKey = new LtedMatchKey(matchKey.getArgument());
                            newKey.addLabel(item);
                            mSelectedItems.add(newKey);
                            onChangeKey();
                        }
                    }
                });
                convertView.setTag(holder);
            } else {
                holder = (ChildHolder) convertView.getTag();
            }

            return convertView;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
            GroupHolder holder;
            final LtedMatchKey item = getGroup(groupPosition);
            if (convertView == null) {
                holder = new GroupHolder();
                convertView = mInflater.inflate(R.layout.item_adapter_group_interest, parent, false);
                holder.title = (TextView) convertView.findViewById(R.id.textView);
                holder.container = (LinearLayout) convertView.findViewById(R.id.containerView);
                convertView.setClickable(false);
                convertView.setTag(holder);
                convertView.setOnClickListener(null);
            } else {
                holder = (GroupHolder) convertView.getTag();
            }
            holder.title.setText(item.getArgument());

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            if (getGroup(groupPosition).getLabels() == null) {
                return 0;
            }
            return getGroup(groupPosition).getLabels().size();
        }

        @Override
        public int getGroupCount() {
            if (mItems == null) {
                return 0;
            }
            return mItems.size();
        }

        @Override
        public LtedMatchKey getGroup(int groupPosition) {

            return mItems.get(groupPosition);
        }

        @Override
        public String getChild(int groupPosition, int childPosition) {
            if (getGroup(groupPosition).getLabels() == null) {
                return null;
            }
            return getGroup(groupPosition).getLabels().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        public List<LtedMatchKey> getItems() {
            return mItems;
        }

    }
}
