package understanding.listen;

import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import understanding.speech.ListeningDialog;

/**
 * A class for all commands that should be used by voice in a specific screen.
 * One line example: new Listen().addListenIntents(new ListenIntent(new Intent(App.getContext(), DeleteEmailIntentService.class)));
 *
 * @author Oded Breiner
 */
public class Listen {

    public Context context;

    public List<ListenIntent> listenIntents;

    public Listen() {
        listenIntents = new ArrayList<ListenIntent>();
    }

    /**
     * This constructor receives one or more ListenIntents
     * that are relevant for the screen the user is looking at.
     * Example: email app "ViewSingleEmail" screen will have 4 intents:
     * 1. reply
     * 2. forward
     * 3. archive
     * 4. delete
     */
    public Listen addListenIntents(Context context, ListenIntent... listenIntentArray) {
        this.context = context;
        listenIntents.addAll(Arrays.asList(listenIntentArray));
        return this;
    }

    public void show() {
        new ListeningDialog(this).show();
    }
}

