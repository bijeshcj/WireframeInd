package com.verizontelematics.indrivemobile.fragments;

/**
 * Created by z688522 on 8/29/14.
 */
public interface BaseUI {

    public void setTitle(String title);

    com.verizontelematics.indrivemobile.activity.UIState getUIState();

    void refresh(int index);

    String[] getModuleNames();

    public void setBackgroundColor(int colorId);
}
