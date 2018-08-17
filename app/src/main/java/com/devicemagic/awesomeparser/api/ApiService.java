package com.devicemagic.awesomeparser.api;

import com.devicemagic.awesomeparser.models.Download;
import com.devicemagic.awesomeparser.models.DownloadsBatch;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiService {
    @GET("/")
    Call<DownloadsBatch> getDownloadsList();

    @GET("/downloads/{id}")
    Call<Download> getDownload(@Path("id") String id);
}
