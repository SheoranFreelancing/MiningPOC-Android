
package com.drill.imagechooser.api;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;

import java.io.File;

public abstract class BChooser {
    protected Activity activity;

    protected Fragment fragment;

    protected android.app.Fragment appFragment;

    protected int type;

    protected String foldername;

    protected boolean shouldCreateThumbnails;

    protected String filePathOriginal;

    protected Bundle extras;

    @Deprecated
    public BChooser(Activity activity, int type, String folderName,
                    boolean shouldCreateThumbnails) {
        this.activity = activity;
        this.type = type;
        this.foldername = folderName;
        this.shouldCreateThumbnails = shouldCreateThumbnails;
    }

    @Deprecated
    public BChooser(Fragment fragment, int type, String foldername,
                    boolean shouldCreateThumbnails) {
        this.fragment = fragment;
        this.type = type;
        this.foldername = foldername;
        this.shouldCreateThumbnails = shouldCreateThumbnails;
    }

    @Deprecated
    public BChooser(android.app.Fragment fragment, int type, String foldername,
                    boolean shouldCreateThumbnails) {
        this.appFragment = fragment;
        this.type = type;
        this.foldername = foldername;
        this.shouldCreateThumbnails = shouldCreateThumbnails;
    }

    public BChooser(Activity activity, int type,
                    boolean shouldCreateThumbnails) {
        this.activity = activity;
        this.type = type;
        this.shouldCreateThumbnails = shouldCreateThumbnails;
        initDirector(activity.getApplicationContext());
    }

    public BChooser(Fragment fragment, int type,
                    boolean shouldCreateThumbnails) {
        this.fragment = fragment;
        this.type = type;
        this.shouldCreateThumbnails = shouldCreateThumbnails;
        initDirector(fragment.getActivity().getApplicationContext());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public BChooser(android.app.Fragment fragment, int type,
                    boolean shouldCreateThumbnails) {
        this.appFragment = fragment;
        this.type = type;
        this.shouldCreateThumbnails = shouldCreateThumbnails;
        initDirector(fragment.getActivity().getApplicationContext());
    }

    /**
     * Call this method, to start the chooser, i.e, The camera app or the
     * gallery depending upon the type.
     * <p>
     * Returns the path, in case, a capture is requested. You will need to save
     * this path, so that, in case, the ChooserManager is destoryed due to
     * activity lifecycle, you will use this information to create the
     * ChooserManager instance again
     * </p>
     * <p>
     * In case of picking a video or image, null would be returned.
     * </p>
     *
     * @throws IllegalArgumentException
     * @throws Exception
     */
    public abstract String choose() throws IllegalArgumentException, Exception;

    /**
     * Call this method to process the result from within your onActivityResult
     * method. You don't need to do any processing at all. Just pass in the
     * request code and the data, and everything else will be taken care of.
     *
     * @param requestCode
     * @param data
     */
    public abstract void submit(int requestCode, Intent data);

    protected void checkDirectory() {
        File directory = null;
        directory = new File(FileUtils.getDirectory(foldername));
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    @SuppressLint("NewApi")
    protected void startActivity(Intent intent) {
        if (activity != null) {
            activity.startActivityForResult(intent, type);
        } else if (fragment != null) {
            fragment.startActivityForResult(intent, type);
        } else if (appFragment != null) {
            appFragment.startActivityForResult(intent, type);
        }
    }

    /**
     * This method should be used to re-initialize the ChooserManagers in case your activity of
     * fragments are destroyed for some reason, and you need to recreate this object in onActivityResult
     *
     * @param path
     */
    public void reinitialize(String path) {
        filePathOriginal = path;
    }

    // Change the URI only when the returned string contains "file:/" prefix.
    // For all the other situations the URI doesn't need to be changed
    protected void sanitizeURI(String uri) {
        filePathOriginal = uri;
        // Picasa on Android < 3.0
        if (uri.matches("https?://\\w+\\.googleusercontent\\.com/.+")) {
            filePathOriginal = uri;
        }
        // Local storage
        if (uri.startsWith("file://")) {
            filePathOriginal = uri.substring(7);
        }
    }

    @SuppressLint("NewApi")
    protected Context getContext() {
        if (activity != null) {
            return activity.getApplicationContext();
        } else if (fragment != null) {
            return fragment.getActivity().getApplicationContext();
        } else if (appFragment != null) {
            return appFragment.getActivity().getApplicationContext();
        }
        return null;
    }

    protected boolean wasVideoSelected(Intent data) {
        if (data == null) {
            return false;
        }

        if (data.getType() != null && data.getType().startsWith("video")) {
            return true;
        }

        ContentResolver cR = getContext().getContentResolver();
        String type = cR.getType(data.getData());
        if (type != null && type.startsWith("video")) {
            return true;
        }

        return false;
    }

    public void setExtras(Bundle extras) {
        this.extras = extras;
    }

    /**
     * Utility method which quickly looks up the file size. Use this, if you want to set a limit to
     * the media chosen, and which your application can safely handle.
     * <p/>
     * For example, you might not want a video of 1 GB to be imported into your app.
     *
     * @param uri
     * @param context
     * @return
     */
    public long queryProbableFileSize(Uri uri, Context context) {
        try {
            if (uri.toString().startsWith("file")) {
                File file = new File(uri.getPath());
                return file.length();
            } else if (uri.toString().startsWith("content")) {
                Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
                cursor.moveToFirst();
                long length = cursor.getLong(cursor.getColumnIndex(OpenableColumns.SIZE));
                cursor.close();
                return length;
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private void initDirector(Context context){
        BChooserPreferences preferences = new BChooserPreferences(context);
        foldername = preferences.getFolderName();
    }
}
