package com.drill.ws.parsers;

import android.net.Uri;

import com.drill.sync.RetroHttpManager;

import java.util.Map;

/**
 * Created by Kamesh on 8/13/15.
 */

public class CloudinaryUploadParser extends GenericParser {

    private Uri fileUri;
    private String uploadUrl;

    public CloudinaryUploadParser(Map response, Uri fileUri) {
        this.requestType = RetroHttpManager.REQUEST_CLOUDINARY_UPLOAD;
        this.fileUri = fileUri;
        processResponse(response);
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    @Override
    public boolean processResponse(Object response) {
        super.processResponse(response);
        if (isValid()) {
            uploadUrl = (String) ((Map) response).get("url");
        }
        return isValid();
    }
}
