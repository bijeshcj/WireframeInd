package com.verizontelematics.indrivemobile.customViews.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.verizontelematics.indrivemobile.R;

/**
 * Created by Priyanga on 3/18/2015.
 */
public class HelpPopupDialog extends Dialog implements View.OnClickListener{

    private String info;

    public HelpPopupDialog(Context context) {
        super(context);

    }

    public HelpPopupDialog(Context context, String info) {
        super(context);
        this.info = info;
    }

    public HelpPopupDialog(Context context, int theme) {
        super(context, theme);
    }

    protected HelpPopupDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up_info_text);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

   /* public void setInfo(String str){
        this.txtViewInfo.setText(str);
    }*/

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
