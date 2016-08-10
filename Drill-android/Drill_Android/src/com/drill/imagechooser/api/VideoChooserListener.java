
package com.drill.imagechooser.api;

public interface VideoChooserListener {

	public void onVideoChosen(ChosenVideo video);

	public void onError(String reason);
}
