package com.feyes.facebookeyes.controller;

import com.feyes.facebookeyes.MainActivity;

public class Show extends StandardAction {

	public Show() {
		super("show");
	}

	@Override
	public String getGrammar() {
		return "(news) | (messages) | (notifications)";
	}

	@Override
	public void action(final String st) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MainActivity.speechController.speak(st);
			}
		}).start();
	}
}
