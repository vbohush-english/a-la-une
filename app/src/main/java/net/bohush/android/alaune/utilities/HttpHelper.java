package net.bohush.android.alaune.utilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {

    private static final String LOG_TAG = "HttpHelper";

    public static String loadData(String stringUrl) {
        Log.i(LOG_TAG, "Starting downloading \"" + stringUrl + "\"");
        long startingTime = System.currentTimeMillis();

        if ((stringUrl == null) || (stringUrl.equals(""))) {
            return null;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String result = null;
        try {
            URL url = new URL(stringUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            if (inputStream == null) {
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }
            if (buffer.length() == 0) {
                return null;
            }
            result = buffer.toString();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            return null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        String seconds = String.format("%.3f", (System.currentTimeMillis() - startingTime) * 0.001);
        Log.i(LOG_TAG, "Finished downloading \"" + stringUrl + "\" in " + seconds + " seconds");
        return result;
    }

}
