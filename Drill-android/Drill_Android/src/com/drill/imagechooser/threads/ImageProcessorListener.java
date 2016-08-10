
package com.drill.imagechooser.threads;

import com.drill.imagechooser.api.ChosenImage;

public interface ImageProcessorListener {
    public void onProcessedImage(ChosenImage image);

    public void onError(String reason);
}
