package com.verizontelematics.indrivemobile.customViews.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;

/**
 * Created by Priyanga on 12/8/2014.
 */
public class CustomProgressDialog extends Dialog {

    public CustomProgressDialog(Context context, String message) {
        super(context);
        getWindow().getDecorView().setBackgroundResource(
                android.R.color.transparent);
        View divider = findViewById(getContext().getResources().getIdentifier("android:id/titleDivider", null, null));
        divider.setBackgroundColor(getContext().getResources().getColor(android.R.color.transparent));
        setContentView(R.layout.custom_progress_fragment);
        TextView messageTV = (TextView) findViewById(R.id.progressBarMessage);
        messageTV.setText(message);
        setCancelable(true);
    }
}
