package com.fsck.k9;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.store.LocalStore;

import java.util.HashMap;

/**
 * Created by user on 13/10/2014.
 */
public class MailReader extends UtteranceProgressListener {

    static TextToSpeech tts = null;
    static MailReader self = null;

    MailReader() {
        self = this;
    }

    public static void start(Context context, LocalStore.LocalMessage message) {
        try {

            String s = message.getSubject();//getTextForDisplay();
            start(context, s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void start(Context context, String text) {
        if (self == null)
            self = new MailReader();

        if (tts == null) {
            tts = new TextToSpeech(context, null);
            tts.setOnUtteranceProgressListener(self);
        }

        tts.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    static void abort() {
        if (tts != null)
            tts.stop();
    }

    @Override
    public void onStart(String utteranceId) {

    }

    @Override
    public void onDone(String utteranceId) {

    }

    @Override
    public void onError(String utteranceId) {

    }
}
