
package com.drill.imagechooser.threads;

import com.drill.imagechooser.api.ChosenFile;

public interface FileProcessorListener {
    public void onProcessedFile(ChosenFile file);

    public void onError(String reason);
}
