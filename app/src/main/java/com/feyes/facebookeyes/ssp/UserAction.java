package com.feyes.facebookeyes.ssp;

import java.util.List;

public interface UserAction {
	String getName();
	ActionAttrib[][] getPossibleAttribs();
	boolean attribsAllwaysRequied();

	void action(List<ActionAttrib> attribs);
}
