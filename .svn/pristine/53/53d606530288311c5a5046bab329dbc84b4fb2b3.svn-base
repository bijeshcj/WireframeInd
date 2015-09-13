package com.verizontelematics.indrivemobile.utils.ui;

import android.app.Activity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.verizontelematics.indrivemobile.R;

/**
 * Created by z688522 on 1/19/15.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private final Activity mContext;

    public CustomInfoWindowAdapter(Activity ctx) {
        mContext = ctx;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return getInfoContents(marker);
    }

    @Override
    public View getInfoContents(Marker marker) {
        View customInfoView = mContext.getLayoutInflater().inflate(R.layout.map_custom_info_window, null);
        TextView tvAddress = (TextView)customInfoView.findViewById(R.id.txt_address);
        TextView tvSnippet = (TextView)customInfoView.findViewById(R.id.txt_snippet);
        ImageButton imvGetDirections = (ImageButton)customInfoView.findViewById(R.id.btn_get_directions);
        // setup title
        tvAddress.setText(marker.getTitle());
        // setup snippet
        tvSnippet.setText(marker.getSnippet());


        return customInfoView;
    }
}
