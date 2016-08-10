
package com.drill.imagechooser.threads;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import com.drill.imagechooser.api.ChosenImage;
import com.drill.utils.LTLog;

import java.io.IOException;

public class ImageProcessorThread extends MediaProcessorThread {

	private final static String TAG = "ImageProcessorThread";

	private com.drill.imagechooser.threads.ImageProcessorListener listener;

	public ImageProcessorThread(String filePath, String foldername,
			boolean shouldCreateThumbnails) {
		super(filePath, foldername, shouldCreateThumbnails);
		setMediaExtension("jpg");
	}

	public void setListener(com.drill.imagechooser.threads.ImageProcessorListener listener) {
		this.listener = listener;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	@Override
	public void run() {
		try {
			manageDiretoryCache("jpg");
			processImage();
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

	private void processImage() throws Exception {
		LTLog.i(TAG, "Processing Image File: " + filePath);

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
						.startsWith("content://com.microsoft.skydrive.content")) {
			processPicasaMedia(filePath, ".jpg");
		} else if (filePath
				.startsWith("content://com.google.android.apps.photos.content")
				|| filePath
						.startsWith("content://com.android.providers.media.documents")
				|| filePath
						.startsWith("content://com.google.android.apps.docs.storage")) {
			processGooglePhotosMedia(filePath, ".jpg");
		} else {
			process();
		}
	}

	@Override
	protected void process() throws Exception {
		super.process();
		if (shouldCreateThumnails) {
			String[] thumbnails = createThumbnails(this.filePath);
			processingDone(this.filePath, thumbnails[0], thumbnails[1]);
		} else {
			processingDone(this.filePath, this.filePath, this.filePath);
		}
	}

	@Override
	protected void processingDone(String original, String thumbnail,
			String thumbnailSmall) {
		if (listener != null) {
			ChosenImage image = new ChosenImage();
			image.setFilePathOriginal(original);
			image.setFileThumbnail(thumbnail);
			image.setFileThumbnailSmall(thumbnailSmall);
			listener.onProcessedImage(image);
		}
	}
}
