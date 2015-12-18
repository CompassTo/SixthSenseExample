package com.compassto.sixthsense.example.data;import com.compassto.sixthsense.sdk.model.LtedMatchKey;import java.util.ArrayList;import java.util.HashMap;import java.util.List;import java.util.Map;/** * Describes the user of a specific LTED device. */public class UserModel {    private int id;    private String name;    private double lat;    private double longitude;    private Map<Integer, LtedMatchKey> mInterests;    public UserModel(int id, String name) {        this.id = id;        this.name = name;        mInterests = new HashMap<>();    }    public void setKey(LtedMatchKey matchKey) {        if (mInterests == null) {            mInterests = new HashMap<>();        }        mInterests.put(matchKey.getId(), matchKey);    }    public int getId() {        return id;    }    public String getName() {        return name;    }    public List<LtedMatchKey> getInterests() {        if (mInterests == null) {            return null;        }        return new ArrayList<>(mInterests.values());    }    public void setInterests(List<LtedMatchKey> keys) {        if (mInterests == null) {            mInterests = new HashMap<>();        }        if (keys == null) {            mInterests.clear();            return;        }        for (LtedMatchKey key : keys) {            mInterests.put(key.getId(), key);        }    }    public void removeMatchKey(int keyId) {        if (mInterests == null || !mInterests.containsKey(keyId)) {            return;        }        mInterests.remove(keyId);    }    public void updateMatchKey(LtedMatchKey key) {        if (key == null) {            return;        }        mInterests.put(key.getId(), key);    }}