package com.verizontelematics.indrivemobile.activity;

import com.verizontelematics.indrivemobile.fragments.UIScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z688522 on 8/29/14.
 */
public class UIState {
    public ArrayList<UIScreen> mUIScreens = new ArrayList<UIScreen>();
    public void addScreen(int index, UIScreen screen) {

        screen.setIndex(index);
        if (mUIScreens.size() <= index) {
            if(index >= mUIScreens.size())
                mUIScreens.add(screen);
            else
                mUIScreens.add(index, screen);
        }
        else {
            mUIScreens.set(index, screen);
        }

    }

    public List<UIScreen> getScreens() {
        return mUIScreens;
    }
}
