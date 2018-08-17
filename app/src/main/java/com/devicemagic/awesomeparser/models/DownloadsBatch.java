package com.devicemagic.awesomeparser.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "downloads")
public class DownloadsBatch {
    @ElementList(entry = "item", inline=true)
    private List<String> ids;

    public DownloadsBatch() {}

    public List<String> getIds() {
        return ids;
    }
}