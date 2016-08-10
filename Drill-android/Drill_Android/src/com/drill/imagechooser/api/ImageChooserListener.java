
package com.drill.imagechooser.api;

public interface ImageChooserListener {
    public void onImageChosen(ChosenImage image);
    public void onError(String reason);
}
