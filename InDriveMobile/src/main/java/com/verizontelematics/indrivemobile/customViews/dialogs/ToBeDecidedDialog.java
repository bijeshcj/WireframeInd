package com.verizontelematics.indrivemobile.customViews.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;

/**
 * Created by bijesh on 10/9/2014.
 */
public class ToBeDecidedDialog extends Dialog implements View.OnClickListener {

    private Button btnAgree, btnCancel;
    private TextView txtViewInfo;
    private String info;
    private boolean isHTMLText = false;

    public ToBeDecidedDialog(Context context) {
        super(context);

    }

    public ToBeDecidedDialog(Context context, String info) {
        super(context);
        this.info = info;
    }

    public ToBeDecidedDialog(Context context, int theme) {
        super(context, theme);
    }

    protected ToBeDecidedDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_info_text);
        Button btnAgree = (Button) findViewById(R.id.agreeBTN);
        txtViewInfo = (TextView) findViewById(R.id.dialog_info);
        //Button btnCancel = (Button)findViewById(R.id.cancelBTN);
        btnAgree.setOnClickListener(this);
        setInfo(info);
        //btnCancel.setOnClickListener(this);
    }

    public void setInfo(String str) {
        if (isHTMLText) {
            this.txtViewInfo.setText(Html.fromHtml(str));
        } else {
            this.txtViewInfo.setText(str);
        }
    }

    public void setFlag(boolean isHTMLText) {
        this.isHTMLText = isHTMLText;
    }

    @Override
    public void onClick(View v) {
        this.dismiss();
    }
}
