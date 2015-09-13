package com.verizontelematics.indrivemobile.models.data;

/**
 * Created by z688522 on 3/25/15.
 */
public class PhoneType {
    private String PhoneNumberMask;
    private String PhoneToken;

    public String getPhoneNumberMask() {
        return PhoneNumberMask;
    }

    public void setPhoneNumberMask(String phoneNumberMask) {
        this.PhoneNumberMask = phoneNumberMask;
    }

    public String getPhoneToken() {
        return PhoneToken;
    }

    public void setPhoneToken(String phoneToken) {
        this.PhoneToken = phoneToken;
    }
}
