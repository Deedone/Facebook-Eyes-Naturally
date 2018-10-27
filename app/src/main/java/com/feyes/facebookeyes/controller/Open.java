package com.feyes.facebookeyes.controller;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;

import com.feyes.facebookeyes.MainActivity;

public class Open extends StandardAction {
	public Open() {
		super("open");
	}

	@Override
	public String getGrammar() {
		return "";
	}

	public static String FACEBOOK_URL = "https://www.facebook.com/YourPageName";
	public static String FACEBOOK_PAGE_ID = "100017306468751";

	//method to get the right URL to use in the intent
	public String getFacebookPageURL(Context context) {
		PackageManager packageManager = context.getPackageManager();
		try {
			int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
			return "fb://page/" + FACEBOOK_PAGE_ID;
		} catch (PackageManager.NameNotFoundException e) {
			return FACEBOOK_URL; //normal web url
		}
	}

	@Override
	public void action(String st) {
		// TODO OPEN FACEBOOK
		Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
		String facebookUrl = getFacebookPageURL(MainActivity.mainActivity);
		facebookIntent.setData(Uri.parse(facebookUrl));
		MainActivity.mainActivity.startActivity(facebookIntent);
	}
}
