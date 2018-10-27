package com.feyes.facebookeyes.ssp;

import java.util.List;

public interface UserAction {
	String getName();
	String getGrammar();

	void action(String st);
}
