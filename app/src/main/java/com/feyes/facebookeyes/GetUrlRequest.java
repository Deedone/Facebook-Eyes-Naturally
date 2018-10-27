package com.feyes.facebookeyes;


import com.feyes.facebookeyes.ssp.sphinx.SpeechControllerSphinx;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUrlRequest {
	public static String doGet(String url)
			throws Exception {

		URL obj = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

		//add reuqest header
		connection.setRequestMethod("GET");
//		connection.setRequestProperty("Data", "Sahsa Dodik" );
		//    connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		//   connection.setRequestProperty("Content-Type", "application/json");

		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String inputLine;

		StringBuilder response = new StringBuilder();
		if ((inputLine = bufferedReader.readLine()) == null) {
			SpeechControllerSphinx.logger.println(url + ": " + inputLine);
			return doGet(url);
		}

		while ((inputLine = bufferedReader.readLine()) != null) {
			response.append(inputLine);
		}
		bufferedReader.close();

//      print result
		//Log.d(TAG,"Response string: " + response.toString());


		return response.toString();
	}
}