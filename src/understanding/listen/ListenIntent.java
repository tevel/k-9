package understanding.listen;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A class to define a specific voice command and its corresponding android intent
 *
 * @author Oded Breiner
 */
public class ListenIntent {

    public final Intent intent;
    private List<String> commandExampleList;

    /**
     * Construct a ListenIntent with a standard android intent
     * Example: ListenIntent(new Intent(context, DeleteEmailIntentService.class));
     */
    public ListenIntent(Intent intent) {
        this.intent = intent;
        commandExampleList = new ArrayList<String>();
    }

    /**
     * Add voice commandExamples to the specific android intent that will be called
     * Example: addCommandExamples(String[]{"delete","remove","trash"})
     */
    public ListenIntent addCommandExamples(String... commandExamples) {
        commandExampleList.addAll(Arrays.asList(commandExamples));
//        intent.putStringArrayListExtra(LISTEN_INTENT_KEYWORDS, keywordArrayList);
        return this;
    }

    public List<String> getCommandExampleList() {
        return commandExampleList;
    }

}
