package com.dotcomrefugee.connectbotopener;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;

public class Opener extends Activity {
    private static final String LOG_TAG = "ConnectBotOpener";

    private static final String STATE_LAUNCHED = "mLaunched";
    private boolean mLaunched = false;
    private IMEChangeReceiver mListener = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(mLaunched) {
            finish();
            return;
        }
        final Context ctx = getApplicationContext();
        final InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);

        if(!isHackerKeyboard(ctx)) {
            mListener = new IMEChangeReceiver();
            ctx.registerReceiver(mListener, new IntentFilter(Intent.ACTION_INPUT_METHOD_CHANGED));
        }
        imm.showInputMethodPicker();
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        if(mListener != null) {
            getApplicationContext().unregisterReceiver(mListener);
            mListener = null;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        icicle.putBoolean(STATE_LAUNCHED, mLaunched);
    }

    @Override
    public void onRestoreInstanceState(Bundle icicle) {
        super.onRestoreInstanceState(icicle);
        if(icicle.containsKey(STATE_LAUNCHED)) {
            mLaunched = icicle.getBoolean(STATE_LAUNCHED);
        }
    }

    boolean isHackerKeyboard(Context ctx) {
        final String currentIME = Settings.Secure.getString(ctx.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
        Log.d(LOG_TAG, "Current IME: " + currentIME);
        return currentIME.startsWith("org.pocketworkstation.pckeyboard");
    }

    void startConnectBot() {
        mLaunched = true;
        Intent cbIntent = new Intent();
        cbIntent.setClassName("org.connectbot", "org.connectbot.HostListActivity");
        startActivity(cbIntent);
    }

    public class IMEChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(Intent.ACTION_INPUT_METHOD_CHANGED)) {
                Log.d(LOG_TAG, "Got IME change");
                if(!mLaunched && isHackerKeyboard(context)) {
                    startConnectBot();
                }
                finish();
            }
        }
    }
}
