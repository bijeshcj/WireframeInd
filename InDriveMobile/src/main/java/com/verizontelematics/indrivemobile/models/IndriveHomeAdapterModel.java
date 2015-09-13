package com.verizontelematics.indrivemobile.models;

/**
 * Created by bijesh on 11/18/2014.
 */
public class IndriveHomeAdapterModel {

    private String header;
    private String description;

    public IndriveHomeAdapterModel(String header,String description){
        this.header = header;
        this.description = description;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
