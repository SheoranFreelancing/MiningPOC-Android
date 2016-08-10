package com.drill.cloudinary;

import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.drill.ui.LT_BaseActivity;
import com.drill.utils.LTLog;
import com.drill.ws.parsers.CloudinaryUploadParser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kamesh on 8/12/15.
 */

public class CloudinaryManager {
    public static String DEFUALT_CLOUDINARY_TRANSFORMATION = "w_150,h_150";
    private static CloudinaryManager instance;
    private Cloudinary cloudinary;

    private CloudinaryManager() {
        Map<String, String> config = new HashMap<String, String>();
        config.put("cloud_name", "groupon-tigers");
        config.put("api_key", "683845122943794");
        config.put("api_secret", "weisQLMEG9LbwSsV53TbNewIeSc");
        cloudinary = new Cloudinary(config);
    }

    public synchronized static CloudinaryManager getInstance() {
        if (instance == null) {
            instance = new CloudinaryManager();
        }
        return instance;
    }

    public void uploadPhoto(Uri uri, LT_BaseActivity responseListener) {
        new UploadImageTask(responseListener, uri).execute();
    }

    private class UploadImageTask extends AsyncTask<Void, Void, Map> {
        private LT_BaseActivity responseListener;
        private Uri uri;

        public UploadImageTask(LT_BaseActivity responseListener, Uri uri) {
            this.responseListener = responseListener;
            this.uri = uri;
        }
        protected Map doInBackground(Void... voids) {
            Map response = null;
            try {
                InputStream is = responseListener.getContentResolver().openInputStream(uri);
                Map params = ObjectUtils.asMap("use_filename", true);//Cloudinary.asMap("public_id", "sample_remote")
                response = cloudinary.uploader().upload(is, params);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        protected void onProgressUpdate(Void... v) {
        }

        protected void onPostExecute(Map result) {
            LTLog.i("CloudinaryManager", "onPostExecute:" + result);
            CloudinaryUploadParser parser = new CloudinaryUploadParser(result, uri);
            responseListener.onSuccessResponse(null,parser,parser.getRequestType(),null);
        }
    }

    public static String getDefaultCloudinaryTransformedUrl(String url) {
        if(TextUtils.isEmpty(url))
            return url;
        String parts[] = url.split("/");
        StringBuffer transformedUrl = new StringBuffer(parts[0]);
        if(parts.length > 2)
            parts[parts.length-2] = DEFUALT_CLOUDINARY_TRANSFORMATION;
        for(int i = 1; i<parts.length; i++) {
            transformedUrl.append("/").append(parts[i]);
        }
        return transformedUrl.toString();
    }
}
