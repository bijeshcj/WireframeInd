package com.verizontelematics.indrivemobile.userprofile.utils;

/**
 * Created by Bijesh on 12-03-2015.
 */
public class UpSellData {

    private String header;
    private String message;
    private int imageId;

    public UpSellData(){

    }

    public UpSellData(String header,String message,int imageId){
        this.header = header;
        this.message = message;
        this.imageId = imageId;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
