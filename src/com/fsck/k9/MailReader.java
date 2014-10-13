package com.fsck.k9;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;

import com.fsck.k9.activity.MessageCompose;
import com.fsck.k9.activity.MessageReference;
import com.fsck.k9.activity.NotificationDeleteConfirmation;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.store.LocalStore;
import com.fsck.k9.service.NotificationActionService;

import java.util.ArrayList;
import java.util.HashMap;

import understanding.listen.Listen;
import understanding.listen.ListenIntent;

/**
 REPLY
 DELETE
 CANCEL
 READ NEXT
 READ PREVIOUS
 COMPOSE NEW MESSAGE
 */
public class MailReader extends UtteranceProgressListener {

    static LocalStore.LocalMessage message = null;
    static Account account = null;
    static Context context = null;
    static Listen listen = null;
    static TextToSpeech tts = null;
    static MailReader self = null;
    static HashMap<String,String> params=new HashMap<String,String> () {
        {
            put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "something");
            //put(TextToSpeech.Engine.KEY_PARAM_STREAM, Integer.toString(VR.TtsAudioStream));
        }
    };

    MailReader() {
        self = this;
    }

    static void init(Context context) {
        self = new MailReader();

        tts = new TextToSpeech(context, null);
        tts.setOnUtteranceProgressListener(self);
    }

    public static void start(Context context, LocalStore.LocalMessage message, Account account) {
        if (self == null)
            init(context);

        self.message = message;
        self.account = account;
        self.context = context;

        try {

            //final CharSequence sender = getMessageSender(context, account, message);
            //final CharSequence subject = getMessageSubject(context, message);
            //CharSequence summary = buildMessageSummary(context, sender, subject);

            String s = message.getSubject();//getTextForDisplay();
            start(context, s);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void start(Context context, String text) {
        if (self == null)
            init(context);

        tts.speak(text, TextToSpeech.QUEUE_ADD, params);
    }

    static void abort() {
        if (tts != null)
            tts.stop();
    }

    @Override
    public void onStart(String utteranceId) {
        listen = new Listen();

        Intent reply1 = MessageCompose.getActionReplyIntent(context, account, message, false, null);
        ListenIntent reply = new ListenIntent(reply1);
        reply.addCommandExamples("reply");

        ArrayList<MessageReference> refs = new ArrayList<MessageReference>();
        refs.add(message.makeMessageReference());
        Intent delete1 = new Intent(context, NotificationDeleteConfirmation.class);
        delete1.putExtra("account", account.getUuid());
        delete1.putExtra("messages", refs);
        delete1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ListenIntent delete = new ListenIntent(delete1);
        delete.addCommandExamples("delete");

        listen.addListenIntents(context, reply, delete);//, cancel, next, previous, compose);
    }

    @Override
    public void onDone(String utteranceId) {
        listen.show();
    }

    @Override
    public void onError(String utteranceId) {

    }
}
