package com.devicemagic.awesomeparser.api;

import android.content.res.Resources;
import android.text.TextUtils;

import com.devicemagic.awesomeparser.R;
import com.devicemagic.awesomeparser.models.Download;
import com.devicemagic.awesomeparser.models.DownloadsBatch;

import java.io.IOException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class ApiHandler {

    private static final String BASE_URL = "https://glacial-sands-39825.herokuapp.com";

    private static ApiHandler instance;

    ApiService apiService;

    /**
     * This should be the unique entry point to access Api Handler
     * @return running instance
     */
    public static ApiHandler getInstance(){
        if (instance == null) {
            instance = new ApiHandler();
        }

        return instance;
    }

    /**
     * Private constructor (only accesible by getInstance method)
     */
    private ApiHandler() {

        //I observed some api calls are causing timeout, thats why I needed to create a custom http client to increase this default value
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    /**
     * Retrieve Download List Items
     * @param callback callback called when webservice responds with the list of download items
     */
    public void getDownloadsList(final ApiCallback<DownloadsBatch> callback) {
        apiService.getDownloadsList().enqueue(new InternalAPICallback<DownloadsBatch>(callback));
    }

    /**
     * Retrieve Single download item
     * @param downloadId id of the download we want to retrieve
     * @param callback callback called when webservice responds with the download item
     */
    public void getSingleDownload(String downloadId, final ApiCallback<Download> callback) {
        apiService.getDownload(downloadId).enqueue(new InternalAPICallback<Download>(callback));
    }

    /**
     * Interface to be used to notify the caller that the webservice data we requested is ready
     * @param <T>
     */

    public interface ApiCallback<T> {
        void onSuccess(T object);
        void onFailure(String errorDescription);
    }

    /**
     * Some logic added to webservice response. If we have multiple webservice endpoints and all maintain
     * a common structure, we can clean here the raw response and return it clear to the caller
     * @param <T>
     */
    static class InternalAPICallback<T> implements Callback<T> {
        ApiCallback callback;

        InternalAPICallback(ApiCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<T> call, Response<T> response) {

            if(response.isSuccessful()) {
                callback.onSuccess(response.body());
            } else { //Try to return an error message to the caller
                try {
                    callback.onFailure(response.errorBody().string());
                } catch (IOException e) {
                    callback.onFailure(response.message());
                }
            }
        }

        @Override
        public void onFailure(Call<T> call, Throwable t) {
            callback.onFailure(t.getLocalizedMessage());
        }
    }
}
