package com.devicemagic.awesomeparser.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.devicemagic.awesomeparser.R;
import com.devicemagic.awesomeparser.adapters.DownloadsListAdapter;
import com.devicemagic.awesomeparser.api.ApiHandler;
import com.devicemagic.awesomeparser.models.Download;
import com.devicemagic.awesomeparser.models.DownloadsBatch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Main Class that request a list of downloads and parse each of them, showing
 * detailed information
 */
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.toolbar_progress_bar) ProgressBar progressBar;

    private DownloadsListAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    int remainingItemsToProcess = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

        configureRecyclerView();

        requestDownloadsList();
    }

    /**
     * Configure Recyclerview and prepare it to show data
     */
    private void configureRecyclerView() {

        //Changes in content does not change recyclerview size
        recyclerView.setHasFixedSize(true);

        //Basic layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Use Custom adapter to show data
        adapter = new DownloadsListAdapter(new ArrayList<Download>());
        recyclerView.setAdapter(adapter);
    }

    /**
     * Request Downloads List from webservice
     */
    private void requestDownloadsList() {

        //ShowProgressbar while loading
        progressBar.setVisibility(View.VISIBLE);

        ApiHandler.getInstance().getDownloadsList(new ApiHandler.ApiCallback<DownloadsBatch>() {
            @Override
            public void onSuccess(DownloadsBatch downloadsBatch) {
                //If all went ok, parse the results
                if (downloadsBatch != null && downloadsBatch.getIds() != null) {
                    parseDownloadIds(downloadsBatch.getIds());
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.error_parsing_download_batch), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(String errorDescription) {
                Toast.makeText(MainActivity.this, errorDescription, Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Parse the list of download Ids, requesting detailed information for each of them
     * @param downloadIds ids of downloads to request
     */
    private void parseDownloadIds(List<String> downloadIds) {
        remainingItemsToProcess = downloadIds.size();

        for (String downloadId : downloadIds) {
            requestDownload(downloadId);
        }
    }

    /**
     * Request Download detailed information based on their id
     * @param downloadId download id to request information of
     */
    private void requestDownload(String downloadId) {
        ApiHandler.getInstance().getSingleDownload(downloadId, new ApiHandler.ApiCallback<Download>() {
            @Override
            public void onSuccess(Download downloadItem) {
                //If all went ok, add single item to list
                if (downloadItem != null) {
                    adapter.addItem(downloadItem);
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.error_parsing_download_item), Toast.LENGTH_SHORT).show();
                }
                handleProcessedItem();
            }

            @Override
            public void onFailure(String errorDescription) {
                Toast.makeText(MainActivity.this, errorDescription, Toast.LENGTH_SHORT).show();
                handleProcessedItem();
            }
        });
    }

    /**
     * This function will be in charge of handling the behaviour with the list when all items are processed.
     * It will for example hide progress bar
     */
    private void handleProcessedItem() {
        remainingItemsToProcess--;
        if (remainingItemsToProcess == 0) {
          progressBar.setVisibility(View.GONE);
      }
    }
}
