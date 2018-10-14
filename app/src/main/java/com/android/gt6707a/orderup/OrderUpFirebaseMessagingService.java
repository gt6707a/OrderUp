/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.gt6707a.orderup;

import com.google.firebase.messaging.FirebaseMessagingService;

/** Listens for changes in the InstanceID */
public class OrderUpFirebaseMessagingService extends FirebaseMessagingService {

  private static String token;

  @Override
  public void onNewToken(String s) {
    super.onNewToken(s);
    token = s;
  }
}
