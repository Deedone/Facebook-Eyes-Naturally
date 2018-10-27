package com.feyes.facebookeyes.controller;

import android.os.AsyncTask;

import com.feyes.facebookeyes.GetUrlRequest;
import com.feyes.facebookeyes.MainActivity;
import com.feyes.facebookeyes.ssp.sphinx.SpeechControllerSphinx;

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
		MainActivity.mainActivity.lastCommands.push(st);

		String url = "";

		if(st.contains("news")) {
			url = "https://facebook-eyes-naturally.herokuapp.com/feed";
		} else if(st.contains("messages")) {
			url = "https://facebook-eyes-naturally.herokuapp.com/feed";
		} else if(st.contains("notifications")) {
			url = "https://facebook-eyes-naturally.herokuapp.com/feed";
		}

		if(url.isEmpty()) {
			return;
		}

		final String urlC = url;

		new AsyncTask<Void, Void, Exception>() {

			@Override
			protected Exception doInBackground(Void... voids) {
				try {
					String txt = GetUrlRequest.doGet(urlC);
					SpeechControllerSphinx.logger.println(txt);
					MainActivity.speechController.speak(txt);
				} catch (Exception e) {
					e.printStackTrace();
					e.printStackTrace(SpeechControllerSphinx.logger);
				}
				return null;
			}
		}.execute();
	}
}
