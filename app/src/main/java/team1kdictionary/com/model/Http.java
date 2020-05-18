package team1kdictionary.com.model;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Http {
    private static final String BASE_URL = "https://translation.googleapis.com/language/translate/v2?";
    private static final String KEY = "YOUR_KEY_HERE";


    private static AsyncHttpClient client = new AsyncHttpClient();


    public static void post(String transText,String sourceLang, String destLang, AsyncHttpResponseHandler responseHandler) throws UnsupportedEncodingException {
        client.get(getAbsoluteUrl(transText, sourceLang, destLang), responseHandler);
    }

    private static String makeKeyChunk(String key) {
        return "key=" + KEY;
    }

    private static String makeTransChunk(String transText) throws UnsupportedEncodingException {
        String encodedText = URLEncoder.encode(transText, "UTF-8");
        return "&amp;q=" + encodedText;
    }

    private static String langSource(String langSource) {
        return "&amp;source=" + langSource;
    }

    private static String langDest(String langDest) {
        return "&amp;target=" + langDest;

    }

    private static String getAbsoluteUrl(String transText, String sourceLang, String destLang) throws UnsupportedEncodingException {
        String apiUrl = BASE_URL + makeKeyChunk(KEY) + makeTransChunk(transText) + langSource(sourceLang) + langDest(destLang);
        return apiUrl;
    }
}
