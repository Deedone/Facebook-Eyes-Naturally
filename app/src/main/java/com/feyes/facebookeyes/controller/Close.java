package com.feyes.facebookeyes.controller;

import com.feyes.facebookeyes.MainActivity;

public class Close extends StandardAction {
	public Close() {
		super("close");
	}

	@Override
	public String getGrammar() {
		return "";
	}

	@Override
	public void action(String st) {
		MainActivity.mainActivity.finish();
		System.exit(-1);

		throw new Error();
	}
}
