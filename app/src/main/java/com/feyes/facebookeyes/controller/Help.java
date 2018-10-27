package com.feyes.facebookeyes.controller;

import com.feyes.facebookeyes.MainActivity;

public class Help extends StandardAction {
	protected Help(String name) {
		super("help");
	}

	@Override
	public String getGrammar() {
		return "";
	}

	@Override
	public void action(String st) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				MainActivity.speechController.speak("Hello the Facebook user. Say facebook for " +
						"activation. Say stop for application exit. Say code word open  if you want to open the Facebook application.");
			}
		}).start();
	}
}
