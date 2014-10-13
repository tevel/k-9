package understanding.speech;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.SpeechRecognizer;

import java.util.List;

import understanding.listen.Listen;
import understanding.parse.Parser;

/**
 * Created by Oded on 8/12/14.
 */
public class ListeningDialog extends Dialog {

    private final Context context;
    private final Listen listen;
    SpeechRecognizer speechRecognizer;
    Intent speechRecognitionIntent;
    private AudioManager audioManagerService;
    boolean isBeepMuted = true;

    public ListeningDialog(Listen listen) {
        super(listen.context);
        this.listen = listen;
        this.context = listen.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context, BestSpeechRecognition.getComponent(context));
        speechRecognitionIntent = BestSpeechRecognition.getIntent(context);
        speechRecognizer.setRecognitionListener(new SpeechRecognitionListener(this));
        audioManagerService = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        startListening();

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                stopListening();
                speechRecognizer.destroy();
            }
        });
    }
/*
    @Override
    public void setOnShowListener(OnShowListener listener) {
        super.setOnShowListener(listener);
    }*/

    private void stopListening() {
        if (isBeepMuted) {
            audioManagerService.setStreamMute(AudioManager.STREAM_MUSIC, false);
        }
        speechRecognizer.stopListening();
    }


    public void onSpeechResults(List<String> speechGuesses) {
        setTitle(speechGuesses.get(0));
        new Parser(context, speechGuesses, listen);
    }

    void startListening() {
        if (isBeepMuted) {
            audioManagerService.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
        speechRecognizer.startListening(speechRecognitionIntent);
    }

    public void onPartialSpeechResults(List<String> speechResults) {
        setTitle(speechResults.get(0));
    }
}
