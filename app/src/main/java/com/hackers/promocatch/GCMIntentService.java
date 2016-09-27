/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.hackers.promocatch;

import android.content.Context;
import android.content.Intent;

import com.google.android.gcm.GCMBaseIntentService;
import com.hackers.promocatch.activities.PlayActivity;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {
		super(PlayActivity.SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		PlayActivity.gcmRegID = registrationId;
		if (PlayActivity.activity != null) // Toast.makeText(PlayActivity.activity,
											// "GCM registered!!!",
			// Toast.LENGTH_SHORT).show();
			if (PlayActivity.activity != null)
				PlayActivity.activity.gcmRegisterOnServer();
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		// not implemented
	}

	@Override
	protected void onMessage(Context context, Intent intent) {
		int id = -3;
		try {
			id = Integer.parseInt(intent.getStringExtra("message"));
		} catch (Exception e) {
			return;
		}
		if (PlayActivity.activity != null)
			PlayActivity.activity.gcmPushHandler.sendEmptyMessage(id);
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		// not implemented
	}

}
