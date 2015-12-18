package com.compassto.sixthsense.example;

import android.content.Context;

import com.compassto.sixthsense.sdk.internal.handler.ILtedManagerCallbackHandler;
import com.compassto.sixthsense.sdk.internal.job.ILtedExpressionJob;
import com.compassto.sixthsense.sdk.internal.job.ILtedPublicManagedExpressionJob;
import com.compassto.sixthsense.sdk.internal.task.LtedPublicManagedMatchListener;
import com.compassto.sixthsense.sdk.manager.LtedManager;
import com.compassto.sixthsense.sdk.model.LtedDeviceMetaData;
import com.compassto.sixthsense.sdk.model.LtedMatchKey;
import com.compassto.sixthsense.sdk.model.LtedPublicManagedConfigModel;
import com.compassto.sixthsense.sdk.model.LtedTaskResult;
import com.compassto.sixthsense.sdk.task.ILtedPublicManagedTask;
import com.compassto.sixthsense.sdk.task.ILtedTask;
import com.compassto.sixthsense.sdk.task.ILtedTaskStateChangeListener;
import com.compassto.sixthsense.sdk.task.LtedPublicManagedPublishTask;
import com.compassto.sixthsense.sdk.task.LtedPublicManagedSubscribeTask;
import com.compassto.sixthsense.example.data.DataProvider;
import com.compassto.sixthsense.example.data.UserModel;
import com.qualcomm.qdiscoverysdk.api.FailureReason;

import java.util.List;

/**
 * Inits LTED to discover and get discovered by LTED devices within proximity.
 */
public class DiscoveryController implements LtedPublicManagedMatchListener {

    LtedPublicManagedPublishTask mPublishTask; // to get detected by other LTE Direct Devices
    LtedPublicManagedSubscribeTask mSubscribeTask; // to detect other LTE Direct Devices

    private Context mContext;
    private OnDiscoveryUpdateListener mCallbackHandler;

    public DiscoveryController(final OnDiscoveryUpdateListener listener, Context context) {
        mCallbackHandler = listener;
        mContext = context;
    }

    /**
     * Get specific data about the device using LTED
     *
     * @return LtedDeviceMetaData data model
     */
    public LtedDeviceMetaData getLtedDeviceData() {
        if (LtedManager.getInstance().isInitialized()) {
            return LtedManager.getLtedDeviceMetaData();
        }
        return null;
    }

    /**
     * Init Discovery Service and LTED tasks to execute LTED expressions needed for subscribe/publish
     */
    public void initLted() {

        try {
            LtedPublicManagedConfigModel config = new LtedPublicManagedConfigModel(LtedApplicationConfig.LTED_APP_ID, LtedApplicationConfig.getEnsProjectAddress(), LtedApplicationConfig.API_KEY);

            LtedManager.initialize(config, mContext, DataProvider.getInstance().getAppUser().getId(), new ILtedManagerCallbackHandler() {
                @Override
                public void onInitSuccess() {
                    mCallbackHandler.onLtedInitSuccess();
                    initLtedTasks();
                }

                @Override
                public void onInitFailure() {
                    mCallbackHandler.onLtedInitFailure();
                }

                @Override
                public void onTerminated() {
                    mCallbackHandler.onLtedTerminated();
                }
            });

        } catch (LtedPublicManagedConfigModel.LtedConfigException e) {
            e.printStackTrace();
        } catch (LtedManager.LtedInitException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates susbcribe task to discover other devices with matching keys.
     * Creates publish task to publish own keys and meta data to other LTE direct devices.
     */
    private void initLtedTasks() {

        mSubscribeTask = new LtedPublicManagedSubscribeTask(LtedApplicationConfig.LTED_DISCOVERY_TASK);
        mSubscribeTask.setOnTaskStateChangeListener(new LtedPublicManagedTaskStateListener(mSubscribeTask, mCallbackHandler));
        mSubscribeTask.registerMatchListener(this);

        mPublishTask = new LtedPublicManagedPublishTask(LtedApplicationConfig.LTED_DISCOVERY_TASK);
        mPublishTask.setOnTaskStateChangeListener(new LtedPublicManagedTaskStateListener(mPublishTask, mCallbackHandler));
    }

    public void start() {
        mSubscribeTask.start();
        mPublishTask.start();
    }

    public void stop() {
        if (mSubscribeTask != null) {
            mSubscribeTask.stop();
        }
        if (mPublishTask != null) {
            mPublishTask.stop();
        }

        LtedManager.getInstance().terminate();
    }

    public void udpateMatchKey(LtedMatchKey key) {
        // remove key if no labels are selected
        if (key.getLabels() == null || key.getLabels().isEmpty()) {
            mPublishTask.removeMatchKey(key.getId());
            mSubscribeTask.removeMatchKey(key.getId());
        } else {
            // update key
            mPublishTask.setMatchKey(key);
            mSubscribeTask.setMatchKey(key);
        }
    }


   /* -------------------------
      LTE DIRECT DISCOVERY MATCH CALLBACKS
     ------------------------- */

    @Override
    public void onNewMatchingDevice(LtedDeviceMetaData device, List<LtedMatchKey> matchKeys) {

        // create user model for detected device
        UserModel user = new UserModel(device.getId(), "User " + String.valueOf(device.getId()));
        user.setInterests(matchKeys);

        mCallbackHandler.onNewUser(user);
    }

    @Override
    public void onUpdateDeviceKey(LtedDeviceMetaData device, LtedMatchKey matchKey) {
        UserModel user = DataProvider.getInstance().getUserForId(device.getId());
        if (user != null) {
            user.setKey(matchKey);
            mCallbackHandler.onUpdateUser(user);
        }
    }

    @Override
    public void onRemoveMatchingDevice(int deviceId) {
        mCallbackHandler.onRemoveUser(deviceId);
    }


    @Override
    public void onRemoveKeyForDevice(LtedDeviceMetaData device, LtedMatchKey matchKey) {
        if (matchKey == null) {
            return;
        }
        UserModel user = DataProvider.getInstance().getUserForId(device.getId());
        if (user != null) {
            user.removeMatchKey(matchKey.getId());
            mCallbackHandler.onUpdateUser(user);
        }
    }


    @Override
    public void onNewMatchData(int deviceId, String classType, Object object) {
    }

    @Override
    public void onUpdateMatchData(int deviceId, String classType, Object object) {
    }

    @Override
    public void onRemoveMatchData(int deviceId, String classType, Object object) {
    }


    public class LtedPublicManagedTaskStateListener implements ILtedTaskStateChangeListener {

        private OnDiscoveryUpdateListener mCallbackHandler;
        private ILtedPublicManagedTask mTask;

        public LtedPublicManagedTaskStateListener(ILtedPublicManagedTask task, OnDiscoveryUpdateListener handler) {
            mTask = task;
            mCallbackHandler = handler;
        }

        @Override
        public void onUpdate(ILtedExpressionJob job, FailureReason reason) {
            mCallbackHandler.onUpdateTask(mTask, ((ILtedPublicManagedExpressionJob) job).getMatchKey(), reason);
        }

        @Override
        public void onComplete(LtedTaskResult result) {
            mCallbackHandler.onCompleteTask(mTask, result);
        }
    }

    public interface OnDiscoveryUpdateListener {

        void onLtedInitFailure();

        void onLtedInitSuccess();

        void onLtedTerminated();

        void onUpdateUser(UserModel user);

        void onNewUser(UserModel user);

        void onRemoveUser(int userId);

        void onCompleteTask(ILtedTask ltedTask, LtedTaskResult result);

        void onUpdateTask(ILtedTask ltedTask, LtedMatchKey key, FailureReason reason);
    }
}
