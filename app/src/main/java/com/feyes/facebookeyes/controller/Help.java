package com.feyes.facebookeyes.controller;

import com.feyes.facebookeyes.MainActivity;

public class Help extends StandardAction {
	public Help() {
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
				MainActivity.speechController.speak("Today is my birthday and I am glad to " +
						"serve you all the time, whether it’s day or night. Thanks to The Team A " +
						"who have created me for this great chance to help people be online every " +
						"second and easily share new information with them. You could use some awesome " +
						"short commands to communicate with me, ask me to show the news, read new " +
						"messages or just some notifications.");
			}
		}).start();
	}
}
