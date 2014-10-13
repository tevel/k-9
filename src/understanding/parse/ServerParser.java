package understanding.parse;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by oded on 10/7/14.
 */
public class ServerParser extends AsyncTask {

    private static final String ROBIN_AI_SERVER = "http://default-environment-nwiv4ipcgg.elasticbeanstalk.com/api";
//    private static final String ROBIN_AI_SERVER = "http://requestb.in/1e9yldu1";
    private Context context;
    private List<String> speechGuesses;
    private Map<String, String> commandIntentMap;
    private String intentUriToRun;

    ServerParser(Context context, List<String> speechGuesses, Map<String, String> commandIntentMap) {
        this.context = context;
        this.speechGuesses = speechGuesses;
        this.commandIntentMap = commandIntentMap;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        //todo:actually send to server and use the result to get "intentUriToRun"

        JSONObject jsonObject;
        try {
            jsonObject = convertToJson();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        String response;
        try {
            response = doHttpPost(jsonObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Log.d("server_response", response);

        if (intentUriToRun != null) return null;

        //fall back:
        for (String guess : speechGuesses) {
            intentUriToRun = commandIntentMap.get(guess);
            if (intentUriToRun != null) break;
        }
        return null;
    }

    public static String doHttpPost(JSONObject jsonObject) throws IOException {
        //instantiates httpclient to make request
        DefaultHttpClient httpclient = new DefaultHttpClient();

        //url with the post data
        HttpPost httPost = new HttpPost(ROBIN_AI_SERVER);

        //passes the results to a string builder/entity
        StringEntity se = new StringEntity(jsonObject.toString());

        //sets the post request as the resulting string
        httPost.setEntity(se);
        //sets a request header so the page receiving the request
        //will know what to do with it
        httPost.setHeader("Accept", "application/json");
        httPost.setHeader("Content-type", "application/json");

        //Handles what is returned from the page
        ResponseHandler responseHandler = new BasicResponseHandler();
        return (String) httpclient.execute(httPost, responseHandler);
    }

    private JSONObject convertToJson() throws JSONException {
        JSONObject wrapper = new JSONObject();


        JSONObject guesses = new JSONObject();
        JSONObject commandIntentMapJson = new JSONObject();

        for (String guess : speechGuesses) {
            guesses.put(guess, 0.1f);
        }

        for (Map.Entry<String, String> entry : commandIntentMap.entrySet()) {
            commandIntentMapJson.put(entry.getKey(), entry.getValue());
        }

        wrapper.put("api_version", 1);
        wrapper.put("guesses", guesses);
        wrapper.put("command_uri_map", commandIntentMapJson);

        return wrapper;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        if (intentUriToRun == null) return;

        Intent intent;
        try {
            intent = Intent.parseUri(intentUriToRun, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        context.startService(intent);
    }
}
