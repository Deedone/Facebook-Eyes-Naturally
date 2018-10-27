package com.feyes.facebookeyes.ssp.sphinx;

import android.app.Activity;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.speech.tts.TextToSpeech;

import com.feyes.facebookeyes.ssp.SpeechController;
import com.feyes.facebookeyes.ssp.UserAction;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;

import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

public class SpeechControllerSphinx implements
		RecognitionListener, SpeechController {

	public static PrintWriter logger;

	/* Named searches allow to quickly reconfigure the decoder */
	private static final String KWS_SEARCH = "hotword";
	private static final String COMMAND_SEARCH = "command";

	/* Keyword we are looking for to activate menu */
	private static final String KEYPHRASE = "facebook";

	private SpeechRecognizer recognizer;

	private TextToSpeech textToSpeech;
	
	private final Queue<String> speechQueue = new LinkedList<String>();

	private final UserAction[] actions;
	private final float keywordThreshold;

	private boolean ready = false;

	private Handler handler = new Handler();
	private Map<String, UserAction> actionsMap = new HashMap<>();

	private final Runnable mStopRecognitionCallback = new Runnable() {
		@Override
		public void run() {
			stopRecognition();
		}
	};

	private final TextToSpeech.OnUtteranceCompletedListener mUtteranceCompletedListener = new TextToSpeech.OnUtteranceCompletedListener() {
		@Override
		public void onUtteranceCompleted(String utteranceId) {
			synchronized (speechQueue) {
				speechQueue.poll();

				if (speechQueue.isEmpty()) {
					recognizer.startListening(KWS_SEARCH);
				}
			}
		}
	};

	public SpeechControllerSphinx(Activity activity, UserAction[] actions, float keywordThreshold) {
		this.actions = actions;

		this.keywordThreshold = keywordThreshold;

		textToSpeech = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
			@Override
			public void onInit(int status) {
				if (status == TextToSpeech.ERROR) return;

				if (textToSpeech.isLanguageAvailable(Locale.getDefault()) == TextToSpeech.LANG_AVAILABLE) {
					textToSpeech.setLanguage(Locale.getDefault());
				}

				textToSpeech.setOnUtteranceCompletedListener(mUtteranceCompletedListener);
			}
		});

		// Recognizer initialization is a time-consuming and it involves IO,
		// so we execute it in async task
		new SetupTask(activity).execute();
	}

	private class SetupTask extends AsyncTask<Void, Void, Exception> {
		WeakReference<Activity> activityReference;

		SetupTask(Activity activity) {
			this.activityReference = new WeakReference<>(activity);
		}

		@Override
		protected Exception doInBackground(Void... params) {
			try {
				setupRecognizer(prepareData(activityReference.get()), activityReference.get());
			} catch (Exception e) {
				return e;
			}

			return null;
		}
		@Override
		protected void onPostExecute(Exception result) {
			if (result != null) {
				result.printStackTrace(logger);
			} else {
				ready = true;
				switchSearch(KWS_SEARCH);
				logger.println("Ready.");
			}
		}
	}

	@Override
	public void destroy() {
		if (recognizer != null) {
			recognizer.cancel();
			recognizer.shutdown();
		}

		if(textToSpeech != null) {
			textToSpeech.shutdown();
		}
	}

	@Override
	public void speak(String text) {
		synchronized (speechQueue) {
			recognizer.stop();
			speechQueue.add(text);
			HashMap<String, String> params = new HashMap<String, String>(2);
			params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UUID.randomUUID().toString());
			params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
			params.put(TextToSpeech.Engine.KEY_FEATURE_NETWORK_SYNTHESIS, "true");
			textToSpeech.speak(text, TextToSpeech.QUEUE_ADD, params);
		}
	}

	/**
	 * In partial result we get quick updates about current hypothesis. In
	 * keyword spotting mode we can react here, in other modes we need to wait
	 * for final result in onResult.
	 */
	@Override
	public void onPartialResult(Hypothesis hypothesis) {
		if (hypothesis == null) {
			return;
		}

		String text = hypothesis.getHypstr();
		logger.println(text);

		if (KWS_SEARCH.equals(recognizer.getSearchName())) {
			startRecognition();
		}
	}

	private synchronized void stopRecognition() {
		if (recognizer == null || KWS_SEARCH.equals(recognizer.getSearchName())) return;
		recognizer.stop();
//		mMicView.setBackgroundResource(R.drawable.background_big_mic);
	}

	private synchronized void startRecognition() {
		if (recognizer == null || COMMAND_SEARCH.equals(recognizer.getSearchName())) return;
		recognizer.cancel();

		new ToneGenerator(AudioManager.STREAM_MUSIC, ToneGenerator.MAX_VOLUME).startTone(ToneGenerator.TONE_CDMA_PIP, 200);
		post(400, new Runnable() {
			@Override
			public void run() {
				recognizer.startListening(COMMAND_SEARCH, 3000);
//				mMicView.setBackgroundResource(R.drawable.background_big_mic_green);
//				Log.d(TAG, "Listen commands");
				post(4000, mStopRecognitionCallback);
			}
		});
	}


	private void post(long delay, Runnable task) {
		handler.postDelayed(task, delay);
	}
	/**
	 * This callback is called when we stop the recognizer.
	 */
	@Override
	public void onResult(Hypothesis hypothesis) {
		handler.removeCallbacks(mStopRecognitionCallback);
		String text = hypothesis != null ? hypothesis.getHypstr() : null;
		logger.println("Result " + text);

		if (text != null) {
//			Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
			String[] data = text.split("\\s+");

			UserAction ua = actionsMap.get(data[0]);

			if(ua != null) {
				ua.action(text.substring(data[0].length()));
			}
		}

		if (COMMAND_SEARCH.equals(recognizer.getSearchName())) {
			recognizer.startListening(KWS_SEARCH);
		}
	}

	@Override
	public void onBeginningOfSpeech() {
	}

	/**
	 * We stop recognizer here to get a final result
	 */
	@Override
	public void onEndOfSpeech() {
		if (!recognizer.getSearchName().equals(KWS_SEARCH)) {
			switchSearch(KWS_SEARCH);
		}
	}

	private static void copyAssetsDir(Activity activity, File baseDir, String directory) throws IOException {
		String[] files = activity.getAssets().list(directory);

		File dir = new File(baseDir.getAbsolutePath() + "/" + directory);

		if(!dir.exists() && !dir.mkdir()) {
			throw new Error();
		}

		StringBuilder sb = new StringBuilder();

		for (String fromFile : files) {
			if(fromFile.equals("en-us-ptm")) continue;

			File toFile = new File(dir, fromFile);

			if(toFile.exists()) {
				continue;
			}

			sb.append(fromFile);
			sb.append("\n");

			InputStream in = activity.getAssets().open(directory + "/" + fromFile);

			FileUtils.copyInputStreamToFile(in, toFile);
		}

		logger.println(sb);
	}

	private void copyAssets(Activity activity, File baseDir) throws IOException {
		copyAssetsDir(activity, baseDir, "en");
		copyAssetsDir(activity, baseDir, "en/en-us-ptm");
	}

	private void switchSearch(String searchName) {
		recognizer.stop();

		// If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
		if (searchName.equals(KWS_SEARCH))
			recognizer.startListening(searchName);
		else
			recognizer.startListening(searchName, 3000);

//		String caption = getResources().getString(captions.get(searchName));
//		((TextView) findViewById(R.id.caption_text)).setText(caption);
	}

	private void setupRecognizer(File datapath, Activity activity) throws IOException {
		// The recognizer can be configured to perform multiple searches
		// of different kind and switch between them

		recognizer = SpeechRecognizerSetup.defaultSetup()
				.setAcousticModel(new File(datapath, "en/en-us-ptm"))
				.setDictionary(new File(datapath, "en/cmudict-en-us.dict"))

				.setRawLogDir(datapath) // To disable logging of raw audio comment out this call (takes a lot of space on the device)
//.setBoolean("-remove_noise", true)
// .setKeywordThreshold(keywordThreshold)
				.getRecognizer();
		recognizer.addListener(this);
		recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

		File grammar = new File(datapath, "thing.gram");

		recognizer.addGrammarSearch(COMMAND_SEARCH, grammar);
	}

	@Override
	public void onError(Exception error) {
//		((TextView) findViewById(R.id.caption_text)).setText(error.getMessage());
		error.printStackTrace(logger);
	}

	@Override
	public void onTimeout() {
		switchSearch(KWS_SEARCH);
	}

	private CharSequence createGrammar() {
		StringBuilder sb = new StringBuilder();
		sb.append("#JSGF V1.0;\ngrammar commands;\n");
		sb.append("public <command> = cancel");

		for (UserAction action: actions) {
			String actionName = action.getName();

			actionsMap.put(actionName, action);

			sb.append(" | ");
			sb.append("<" + actionName + ">");
		}

		sb.append(";\n");

		for (UserAction action: actions) {
			sb.append("<" + action.getName().toLowerCase() + ">");
			sb.append(" = ");

			sb.append(action.getName().toLowerCase());

			String grammar = action.getGrammar();

			if(!grammar.isEmpty()) {

				sb.append(" (");
				sb.append(grammar);
				sb.append(")");

			}
			sb.append(";\n");
		}

		return sb;
	}

	public File prepareData(Activity activity) throws Exception {
		String baseDirAsString = Environment.getExternalStorageDirectory().getAbsolutePath() +
				"/Android/data/" + activity.getPackageName();

		File baseDir = new File(baseDirAsString);

		if(!baseDir.exists()) {
			if(!baseDir.mkdir()) {
				throw new Exception("Can`t create assets data dir.");
			}
		}

		File jsgf = new File(baseDir.getAbsolutePath() + "/" + "thing.gram");

		CharSequence sb = createGrammar();

		FileUtils.write(jsgf, sb, Charset.forName("UTF-8"), false);

		SpeechControllerSphinx.logger.println(sb);

		copyAssets(activity, baseDir);

		return baseDir;
	}

	@Override
	public boolean isReady() {
		return ready;
	}

	@Override
	public void stop() {
		speechQueue.clear();
		textToSpeech.stop();
	}
}