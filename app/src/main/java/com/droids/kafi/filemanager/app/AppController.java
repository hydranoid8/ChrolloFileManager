package com.droids.kafi.filemanager.app;

import android.app.Application;

import com.droids.kafi.filemanager.activity.MainActivity;
import com.droids.kafi.filemanager.helper.AnalyticsTrackers;
import com.droids.kafi.filemanager.model.MediaFileListModel;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

public class AppController extends Application {
    private static AppController mInstance;
    private ArrayList<MediaFileListModel>  mediaFileListModelArrayList;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }


    public void setButtonBackPressed(MainActivity.ButtonBackPressListener listener) {
        MainActivity.buttonBackPressListener=listener;
    }

    public void setMediaFileListArrayList(ArrayList<MediaFileListModel> mediaFileListArrayList) {
        this.mediaFileListModelArrayList = mediaFileListArrayList;
    }

    public ArrayList<MediaFileListModel> getMediaFileListModeLArray() {
        return mediaFileListModelArrayList;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }


    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();

        // Set screen name.
        t.setScreenName(screenName);

        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());

        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }


    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();

            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(
                            new StandardExceptionParser(this, null)
                                    .getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }
}
