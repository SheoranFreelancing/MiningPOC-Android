
package com.drill.imagechooser.api;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;

import com.drill.imagechooser.threads.VideoProcessorListener;
import com.drill.imagechooser.threads.VideoProcessorThread;
import com.drill.utils.LTLog;

import java.io.File;
import java.util.Calendar;

public class VideoChooserManager extends BChooser implements
        VideoProcessorListener {
    private final static String TAG = "VideoChooserManager";

    private VideoChooserListener listener;

    public VideoChooserManager(Activity activity, int type) {
        super(activity, type, true);
    }

    public VideoChooserManager(Fragment fragment, int type) {
        super(fragment, type, true);
    }

    public VideoChooserManager(android.app.Fragment fragment, int type) {
        super(fragment, type, true);
    }

    @Deprecated
    public VideoChooserManager(Activity activity, int type, String foldername) {
        super(activity, type, foldername, true);
    }

    @Deprecated
    public VideoChooserManager(Fragment fragment, int type, String foldername) {
        super(fragment, type, foldername, true);
    }

    @Deprecated
    public VideoChooserManager(android.app.Fragment fragment, int type,
                               String foldername) {
        super(fragment, type, foldername, true);
    }

    public VideoChooserManager(Activity activity, int type,
                               boolean shouldCreateThumbnails) {
        super(activity, type, shouldCreateThumbnails);
    }

    public VideoChooserManager(Fragment fragment, int type,
                               boolean shouldCreateThumbnails) {
        super(fragment, type, shouldCreateThumbnails);
    }

    public VideoChooserManager(android.app.Fragment fragment, int type,
                               boolean shouldCreateThumbnails) {
        super(fragment, type, shouldCreateThumbnails);
    }

    @Deprecated
    public VideoChooserManager(Activity activity, int type, String foldername,
                               boolean shouldCreateThumbnails) {
        super(activity, type, foldername, shouldCreateThumbnails);
    }

    @Deprecated
    public VideoChooserManager(Fragment fragment, int type, String foldername,
                               boolean shouldCreateThumbnails) {
        super(fragment, type, foldername, shouldCreateThumbnails);
    }

    @Deprecated
    public VideoChooserManager(android.app.Fragment fragment, int type,
                               String foldername, boolean shouldCreateThumbnails) {
        super(fragment, type, foldername, shouldCreateThumbnails);
    }

    /**
     * Set a listener, to get callbacks when the videos and the thumbnails are
     * processed
     *
     * @param listener
     */
    public void setVideoChooserListener(VideoChooserListener listener) {
        this.listener = listener;
    }

    @Override
    public String choose() throws Exception {
        String path = null;
        if (listener == null) {
            throw new IllegalArgumentException(
                    "ImageChooserListener cannot be null. Forgot to set ImageChooserListener???");
        }
        switch (type) {
            case ChooserType.REQUEST_CAPTURE_VIDEO:
                path = captureVideo();
                break;
            case ChooserType.REQUEST_PICK_VIDEO:
                pickVideo();
                break;
            default:
                throw new IllegalArgumentException(
                        "Cannot choose an image in VideoChooserManager");
        }
        return path;
    }

    private String captureVideo() throws Exception {
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= Build.VERSION_CODES.GINGERBREAD
                && sdk <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            return captureVideoPatchedMethodForGingerbread();
        } else {
            return captureVideoCurrent();
        }
    }

    private String captureVideoCurrent() throws Exception {
        checkDirectory();
        try {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            filePathOriginal = FileUtils.getDirectory(foldername)
                    + File.separator + Calendar.getInstance().getTimeInMillis()
                    + ".mp4";
            intent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(filePathOriginal)));
            if (extras != null) {
                intent.putExtras(extras);
            }
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            throw new Exception("Activity not found");
        }
        return filePathOriginal;
    }

    private String captureVideoPatchedMethodForGingerbread() throws Exception {
        try {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (extras != null) {
                intent.putExtras(extras);
            }
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            throw new Exception("Activity not found");
        }
        return null;
    }

    private void pickVideo() throws Exception {
        checkDirectory();
        try {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            if (extras != null) {
                intent.putExtras(extras);
            }
            intent.setType("video/*");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            throw new Exception("Activity not found");
        }
    }

    @Override
    public void submit(int requestCode, Intent data) {
        switch (type) {
            case ChooserType.REQUEST_PICK_VIDEO:
                processVideoFromGallery(data);
                break;
            case ChooserType.REQUEST_CAPTURE_VIDEO:
                processCameraVideo(data);
                break;
        }
    }

    @SuppressLint("NewApi")
    private void processVideoFromGallery(Intent data) {
        if (data != null && data.getDataString() != null) {
            String uri = data.getData().toString();
            sanitizeURI(uri);
            if (filePathOriginal == null || TextUtils.isEmpty(filePathOriginal)) {
                onError("File path was null");
            } else {

                LTLog.i(TAG, "File: " + filePathOriginal);

                String path = filePathOriginal;
                VideoProcessorThread thread = new VideoProcessorThread(path,
                        foldername, shouldCreateThumbnails);
                thread.setListener(this);
                thread.setContext(getContext());
                thread.start();
            }
        }
    }

    @SuppressLint("NewApi")
    private void processCameraVideo(Intent intent) {
        String path = null;
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= Build.VERSION_CODES.GINGERBREAD
                && sdk <= Build.VERSION_CODES.GINGERBREAD_MR1) {
            path = intent.getDataString();
        } else {
            path = filePathOriginal;
        }
        VideoProcessorThread thread = new VideoProcessorThread(path,
                foldername, shouldCreateThumbnails);
        thread.setListener(this);
        thread.setContext(getContext());
        thread.start();
    }

    @Override
    public void onProcessedVideo(ChosenVideo video) {
        if (listener != null) {
            listener.onVideoChosen(video);
        }
    }

    @Override
    public void onError(String reason) {
        if (listener != null) {
            listener.onError(reason);
        }
    }
}
