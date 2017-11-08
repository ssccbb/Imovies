package com.sung.imovies.Utils;

import android.net.Uri;
import android.util.Log;
import com.sung.imovies.bean.MoviesItem;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by sung on 2017/11/7.
 */

public class NetWorkUtils {
    public static final String TAG = NetWorkUtils.class.getSimpleName().toString();

    private static final String API_KEY = "your api key";

    private static final String BASE_MOVIES_LIST = "http://api.themoviedb.org/3/movie/top_rated";
    private static final String BASE_MOVIES_COVER = "http://image.tmdb.org/t/p/";
    //构建图片uri屏幕参数
    private static final String screenSize = "w185";

    private static String PARAM_LANGUAGE = "language";
    private static String PARAM_APIKEY = "api_key";

    /**
     * 创建列表请求
     * */
    public static URL buildListUri(){
        Uri buildUri = Uri.parse(BASE_MOVIES_LIST).buildUpon()
                .appendQueryParameter(PARAM_LANGUAGE, "zh")
                .appendQueryParameter(PARAM_APIKEY, API_KEY)
                .build();
        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d(TAG, "buildListUri: "+url.toString() );
        return url;
    }

    /**
     * 创建图片请求
     * */
    public static URL buildImageUri(String paramers){
        Uri uri = Uri.parse(BASE_MOVIES_COVER).buildUpon()
                .appendEncodedPath(screenSize)
                .appendEncodedPath(paramers)
                .build();
        URL url = null;
        try {
            url = new URL(uri.toString());
            Log.d(TAG, "buildImageUri: "+url.toString() );
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * 获取电影列表
     * */
    public static String getResponseWithMoviesList(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasNext = scanner.hasNext();
            if (hasNext) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * 解析列表Json
     * */
    public static List parserMoviesListJSON(String data){
        List<MoviesItem> items = new ArrayList<>();
        String responseInfo = "MoviesDB Response Info:";
        try {
            JSONObject jsonObject = new JSONObject(data);
            String page = jsonObject.optString("page");
            String total_results = jsonObject.optString("total_results");
            String total_pages = jsonObject.optString("total_pages");
            responseInfo = responseInfo.toString()
                    + "\tcurrent page is\t" + page + ", total page is\t"+total_pages+", total movie result is\t"+total_results+"!";
            Log.d(TAG, "get movies successful!\tquery result"+responseInfo );

            JSONArray results = jsonObject.optJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject object = results.getJSONObject(i);
                MoviesItem moviesItem = new MoviesItem(object);
                items.add(i,moviesItem);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "parserMoviesListJSON: "+e.toString() );
        }
        return items;
    }
}
