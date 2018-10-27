package com.feyes.facebookeyes.controller;

import com.feyes.facebookeyes.ssp.UserAction;

import java.util.ArrayList;
import java.util.List;

public abstract class StandardAction implements UserAction {

	public static List<UserAction> actions = new ArrayList<>();

	protected final String name;

	protected StandardAction(String name) {
		this.name = name;

		actions.add(this);
	}

	@Override
	public String getName() {
		return name;
	}
}
