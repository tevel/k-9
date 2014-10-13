package understanding.parse;

import android.content.Context;
import android.content.Intent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import understanding.listen.Listen;
import understanding.listen.ListenIntent;

/**
 * Created by oded on 10/7/14.
 */
public class Parser {

    public Parser(Context context, List<String> speechGuesses, Listen listen) {

        Map<String, String> commandIntentMap = new HashMap<String, String>();

        for (ListenIntent listenIntent : listen.listenIntents) {
            for (String keyword : listenIntent.getCommandExampleList())
                commandIntentMap.put(keyword, listenIntent.intent.toUri(Intent.URI_INTENT_SCHEME));
        }
        new ServerParser(context, speechGuesses, commandIntentMap).execute();
    }

}
