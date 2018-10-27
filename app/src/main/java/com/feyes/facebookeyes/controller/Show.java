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
		return "(news) | (messages)";
	}

	@Override
	public void action(final String st) {
		String url = "";

		if(st.contains("news")) {
			new News().execute();
		} else if(st.contains("messages")) {
			new Msg().execute();
		}
	}

	private static class News extends AsyncTask<Void, Void, Exception> {
		@Override
		protected Exception doInBackground(Void... voids) {
			try {
				String txt = GetUrlRequest.doGet("https://facebook-eyes-naturally.herokuapp.com/feed").trim();

				txt = txt.replace('\n', ';');

				if(txt.isEmpty()) {
					txt = "No messages";
				}

				SpeechControllerSphinx.logger.println(txt);
				MainActivity.speechController.speak(txt);
			} catch (Exception e) {
				e.printStackTrace();
				e.printStackTrace(SpeechControllerSphinx.logger);
			}
			return null;
		}
	}

	private static class Msg extends AsyncTask<Void, Void, Exception> {
		@Override
		protected Exception doInBackground(Void... voids) {
			try {
				String txt = GetUrlRequest.doGet("https://facebook-eyes-naturally.herokuapp.com/msg").trim();

				txt = txt.replace('\n', ';');

				if(txt.isEmpty()) {
					txt = "No messages";
				}

				SpeechControllerSphinx.logger.println(txt);
				MainActivity.speechController.speak(txt);
			} catch (Exception e) {
				e.printStackTrace();
				e.printStackTrace(SpeechControllerSphinx.logger);
			}
			return null;
		}
	}
}
