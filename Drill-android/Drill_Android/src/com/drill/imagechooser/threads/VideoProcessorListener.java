
package com.drill.imagechooser.threads;

import com.drill.imagechooser.api.ChosenVideo;

public interface VideoProcessorListener {
    public void onProcessedVideo(ChosenVideo video);

    public void onError(String reason);
}
