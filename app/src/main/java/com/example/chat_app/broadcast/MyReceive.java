package com.example.chat_app.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.chat_app.activities.SignInActivity;
import com.example.chat_app.utilities.Constants;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
            FirebaseFirestore.getInstance().collection(Constants.KEY_COLLECTION_USERS)
                    .document(SignInActivity.preferenceManager.getString(Constants.KEY_USER_ID))
                    .update(Constants.KEY_STATUS, null);
        }
    }
}
