package com.devicemagic.awesomeparser.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "download")
public class Download {
    @Element(name = "item")
    private Item item;

    public Download() {}

    public Item getItem() {
        return item;
    }
}