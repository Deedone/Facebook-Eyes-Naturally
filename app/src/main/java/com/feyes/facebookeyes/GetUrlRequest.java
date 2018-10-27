package com.feyes.facebookeyes;


import com.feyes.facebookeyes.ssp.sphinx.SpeechControllerSphinx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUrlRequest {
	public static String doGet(String url)
			throws Exception {

		int i = 10;

		String inputLine = null;
		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("User-Agent", "Mozilla/5.0" );
//		connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
//		connection.setRequestProperty("Content-Type", "application/json");

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

		do {
			if(i != 10) Thread.sleep(200);
			inputLine = bufferedReader.readLine();
			SpeechControllerSphinx.logger.println("GET NULL");
		} while (i > 0 && inputLine == null);

		StringBuffer response = new StringBuffer();

		while ((inputLine = bufferedReader.readLine()) != null) {
			response.append(inputLine);
		}

		bufferedReader.close();

//      print result
		SpeechControllerSphinx.logger.println("Response string: " + response.toString());

		return response.toString();
	}
}