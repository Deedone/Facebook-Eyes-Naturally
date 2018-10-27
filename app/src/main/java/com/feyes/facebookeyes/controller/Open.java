package com.feyes.facebookeyes.controller;

import com.feyes.facebookeyes.MainActivity;

public class Open extends StandardAction {
	protected Open() {
		super("open");
	}

	@Override
	public String getGrammar() {
		return "";
	}

	@Override
	public void action(String st) {
		final String s = MainActivity.popLastCommandValue();

		if(s.isEmpty()) {
			new Thread() {
				@Override
				public void run() {
					MainActivity.speechController.speak("open what");
				}
			}.start();
		} else {
			// TODO OPEN FACEBOOK
		}
	}
}
