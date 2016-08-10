
package com.drill.imagechooser.api;

public class ChosenVideo extends ChosenMedia {

    String videoPreviewImage;

    String videoFilePath;

    private String thumbnailPath;

    private String thumbnailSmallPath;

    public String getVideoFilePath() {
        return videoFilePath;
    }

    public void setVideoFilePath(String videoFilePath) {
        this.videoFilePath = videoFilePath;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getThumbnailSmallPath() {
        return thumbnailSmallPath;
    }

    public void setThumbnailSmallPath(String thumbnailSmallPath) {
        this.thumbnailSmallPath = thumbnailSmallPath;
    }

    public String getVideoPreviewImage() {
        return videoPreviewImage;
    }

    public void setVideoPreviewImage(String videoPreviewImage) {
        this.videoPreviewImage = videoPreviewImage;
    }

    @Override
    public String getMediaHeight() {
        return getHeight(videoPreviewImage);
    }

    @Override
    public String getMediaWidth() {
        return getWidth(videoPreviewImage);
    }

    public String getExtension() {
        return getFileExtension(videoFilePath);
    }
}
