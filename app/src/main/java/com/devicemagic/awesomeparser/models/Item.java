package com.devicemagic.awesomeparser.models;

import org.simpleframework.xml.Element;

/**
 * Internal Item contained in Download
 */
public class Item {
    @Element(name = "key")
    private String key;

    @Element(name = "value")
    private String value;

    public Item() {}

    public String getValue() {
        return value;
    }
}