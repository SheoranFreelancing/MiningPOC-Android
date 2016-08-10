
package com.drill.imagechooser.api;

public interface FileChooserListener {

    public void onFileChosen(ChosenFile file);

    public void onError(String reason);
}
