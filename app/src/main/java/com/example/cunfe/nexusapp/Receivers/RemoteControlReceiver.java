package com.example.cunfe.nexusapp.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

/**
 * Created by cunfe on 2015-11-25.
 */
public class RemoteControlReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(Intent.ACTION_MEDIA_BUTTON.equals(intent.getAction()))
        {
            KeyEvent event = (KeyEvent)intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
            if (KeyEvent.KEYCODE_MEDIA_PLAY == event.getKeyCode()) {
                // Handle key press.
                int keyCode = event.getKeyCode();
                int keyAction = event.getAction();
                long downtime = event.getDownTime();

                StringBuffer stringBuffer = new StringBuffer();
                if (KeyEvent.KEYCODE_MEDIA_NEXT == keyCode) {
                    stringBuffer.append("KEYCODE_MEDIA_NEXT");
                }
                // 说明：当我们按下MEDIA_BUTTON中间按钮时，实际出发的是 KEYCODE_HEADSETHOOK 而不是
                // KEYCODE_MEDIA_PLAY_PAUSE
                if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == keyCode) {
                    stringBuffer.append("KEYCODE_MEDIA_PLAY_PAUSE");
                }
                if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
                    stringBuffer.append("KEYCODE_HEADSETHOOK");
                }
                if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
                    stringBuffer.append("KEYCODE_MEDIA_PREVIOUS");
                }
                if (KeyEvent.KEYCODE_MEDIA_STOP == keyCode) {
                    stringBuffer.append("KEYCODE_MEDIA_STOP");
                }

            }

        }
    }
}
