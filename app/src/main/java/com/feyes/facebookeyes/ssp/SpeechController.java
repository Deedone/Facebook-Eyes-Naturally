package com.feyes.facebookeyes.ssp;

public interface SpeechController {
	void destroy();
	void speak(String text);

	boolean isReady();
}
