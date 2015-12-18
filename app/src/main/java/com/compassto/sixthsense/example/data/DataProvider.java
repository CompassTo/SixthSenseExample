package com.compassto.sixthsense.example.data;


import com.compassto.sixthsense.sdk.model.LtedMatchKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataProvider {

    private static DataProvider singleton;

    public UserModel getAppUser() {
        return appUser;
    }

    public UserModel appUser;
    public Map<Integer, UserModel> userMap = new HashMap<>();
    public List<LtedMatchKey> globalKeys;

    public List<UserModel> getUserList() {
        return new ArrayList<>(userMap.values());
    }

    private DataProvider() {
        globalKeys = new ArrayList<>();

        List<String> musicLabels = new ArrayList<>();
        musicLabels.add("rock");
        musicLabels.add("pop");
        musicLabels.add("electro");

        List<String> filmLabels = new ArrayList<>();
        filmLabels.add("horror");
        filmLabels.add("drama");
        filmLabels.add("comedy");

        // arguments are static and need to be defined at Qualcomm developer platform dynamic managed project
        // labels can be dynamic
        globalKeys.add(new LtedMatchKey("Music", musicLabels));
        globalKeys.add(new LtedMatchKey("Film", filmLabels));

        userMap = new HashMap<>();
    }

    public static DataProvider getInstance() {
        if (singleton == null) {
            singleton = new DataProvider();
        }
        return singleton;
    }

    public void removeUser(int userId) {
        if (userMap == null) {
            return;
        }
        userMap.remove(userId);
    }

    public void putUser(UserModel user) {
        userMap.put(user.getId(), user);
    }

    public UserModel getUserForId(int id) {
        if (userMap == null || !userMap.containsKey(Integer.valueOf(id))) {
            return null;
        }

        return userMap.get(id);
    }
}
