
package com.drill.imagechooser.threads;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.TextUtils;

import com.drill.imagechooser.api.ChosenVideo;
import com.drill.imagechooser.api.FileUtils;
import com.drill.utils.LTLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class VideoProcessorThread extends MediaProcessorThread {
    private final static String TAG = "VideoProcessorThread";

    private VideoProcessorListener listener;

    private String previewImage;

    public VideoProcessorThread(String filePath, String foldername,
                                boolean shouldCreateThumbnails) {
        super(filePath, foldername, shouldCreateThumbnails);
        setMediaExtension("mp4");
    }

    public void setListener(VideoProcessorListener listener) {
        this.listener = listener;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void run() {
        try {
            manageDiretoryCache("mp4");
            processVideo();
        } catch (IOException e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onError(e.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (listener != null) {
                listener.onError(e.getMessage());
            }
        }
    }

    private void processVideo() throws Exception {

        LTLog.i(TAG, "Processing Video file: " + filePath);

        // Picasa on Android >= 3.0
        if (filePath != null && filePath.startsWith("content:")) {
            filePath = getAbsoluteImagePathFromUri(Uri.parse(filePath));
        }
        if (filePath == null || TextUtils.isEmpty(filePath)) {
            if (listener != null) {
                listener.onError("Couldn't process a null file");
            }
        } else if (filePath.startsWith("http")) {
            downloadAndProcess(filePath);
        } else if (filePath
                .startsWith("content://com.google.android.gallery3d")
                || filePath
                .startsWith("content://com.microsoft.skydrive.content.external")) {
            processPicasaMedia(filePath, ".mp4");
        } else if (filePath
                .startsWith("content://com.google.android.apps.photos.content")
                || filePath
                .startsWith("content://com.android.providers.media.documents")
                || filePath
                .startsWith("content://com.google.android.apps.docs.storage")) {
            processGooglePhotosMedia(filePath, ".mp4");
        } else if (filePath.startsWith("content://media/external/video")) {
            processContentProviderMedia(filePath, ".mp4");
        } else {
            process();
        }
    }

    @Override
    protected void process() throws Exception {
        super.process();
        if (shouldCreateThumnails) {
            createPreviewImage();
            String[] thumbnails = createThumbnails(createThumbnailOfVideo());
            processingDone(this.filePath, thumbnails[0], thumbnails[1]);
        } else {
            processingDone(this.filePath, this.filePath, this.filePath);
        }
    }

    private String createPreviewImage() throws IOException {
        previewImage = null;
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath,
                Thumbnails.FULL_SCREEN_KIND);
        if (bitmap != null) {
            previewImage = FileUtils.getDirectory(foldername) + File.separator
                    + Calendar.getInstance().getTimeInMillis() + ".jpg";
            File file = new File(previewImage);
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
        }
        return previewImage;
    }

    private String createThumbnailOfVideo() throws IOException {
        String thumbnailPath = null;
        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath,
                Thumbnails.MINI_KIND);
        if (bitmap != null) {
            thumbnailPath = FileUtils.getDirectory(foldername) + File.separator
                    + Calendar.getInstance().getTimeInMillis() + ".jpg";
            File file = new File(thumbnailPath);
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
        }
        return thumbnailPath;
    }

    @Override
    protected void processingDone(String original, String thumbnail,
                                  String thunbnailSmall) {
        if (listener != null) {
            ChosenVideo video = new ChosenVideo();
            video.setVideoFilePath(original);
            video.setThumbnailPath(thumbnail);
            video.setThumbnailSmallPath(thunbnailSmall);
            video.setVideoPreviewImage(previewImage);
            listener.onProcessedVideo(video);
        }
    }
}
