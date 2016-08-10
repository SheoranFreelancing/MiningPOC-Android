
package com.drill.imagechooser.api;


public class ChosenImage extends ChosenMedia {
    private String filePathOriginal;

    private String fileThumbnail;

    private String fileThumbnailSmall;

    public String getFilePathOriginal() {
        return filePathOriginal;
    }

    public void setFilePathOriginal(String filePathOriginal) {
        this.filePathOriginal = filePathOriginal;
    }

    public String getFileThumbnail() {
        return fileThumbnail;
    }

    public void setFileThumbnail(String fileThumbnail) {
        this.fileThumbnail = fileThumbnail;
    }

    public String getFileThumbnailSmall() {
        return fileThumbnailSmall;
    }

    public void setFileThumbnailSmall(String fileThumbnailSmall) {
        this.fileThumbnailSmall = fileThumbnailSmall;
    }

    @Override
    public String getMediaHeight() {
        return getHeight(filePathOriginal);
    }

    @Override
    public String getMediaWidth() {
       return getWidth(filePathOriginal);
    }
    
    public String getExtension(){
        return getFileExtension(filePathOriginal);
    }

}
