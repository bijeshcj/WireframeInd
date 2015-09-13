package com.verizontelematics.indrivemobile.utils.ui;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.fragments.BaseUIScreenFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by z688522 on 10/15/14.
 */
public class CustomOptionListFragment extends BaseSubUIListFragment implements HomeActivity.CustomTopBarItemsClickListener {
    List<String> mServiceTypes;
    FragmentManager mFragmentManager;
    private  ArrayList<Integer> lstDisableServiceTypes = new ArrayList<Integer>();

    public interface OnDismissListener {
        public void onDestroy();
        public void onDetach();
    }
    private OnDismissListener mDismissListener;

    public interface OnItemSelected {
        public void onItemSelected(int position, String value);
    }
    OnItemSelected mListener;

    public static CustomOptionListFragment createInstance(List<String> data,ArrayList<Integer> lstDisableServiceTypes, BaseUIScreenFragment homeFragment) {
        CustomOptionListFragment sl = new CustomOptionListFragment();
        sl.setHomeFragment(homeFragment);
        sl.mServiceTypes =  data;
        sl.lstDisableServiceTypes = lstDisableServiceTypes;
        return sl;
    }

    public CustomOptionListFragment() {
        super();
    }

    public void setOnItemSelectedListener(OnItemSelected listener) {
        mListener = listener;
    }

    public void setOnDismissListener(OnDismissListener destroyListener) {
        mDismissListener = destroyListener;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        if (mListener != null) {
            if(hasExistsServiceType(position))
                return;
            mListener.onItemSelected(position, mServiceTypes.get(position));
        }
        dismiss();
    }

    private boolean hasExistsServiceType(int pos){
        boolean retVal = false;
        if(lstDisableServiceTypes != null){
            retVal = lstDisableServiceTypes.contains(pos);
        }

        return retVal;
    }

    public void dismiss() {


//        android.support.v4.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
//        ft.remove(this);
//        ft.commit();
//        if (mFragmentManager ==  null)
//            getFragmentManager().popBackStack();
//        else
//            mFragmentManager.popBackStack();
        getHomeFragment().popFragmentStack();

    }

    public void show(FragmentManager fm, int container) {
        mFragmentManager = fm;
        if (mFragmentManager == null) {
            mFragmentManager = getFragmentManager();
        }
        android.support.v4.app.FragmentTransaction ft = mFragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out, R.anim.slide_in_left, R.anim.slide_out);
        ft.addToBackStack("OptionListSelection");
        ft.replace(container, this,"OptionListSelection");
        ft.commit();
        getHomeFragment().pushFragmentStack("OptionListSelection");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  ((HomeActivity)getActivity()).hideActionToggleButton();
        ((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.service_selection), true,false,false,false);

        //((HomeActivity)getActivity()).setTitle("Service ccSelection");
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, mServiceTypes);
        CustomOptionListAdapter adapter = new CustomOptionListAdapter(inflater.getContext(),R.layout.custom_options_list,mServiceTypes);
        setListAdapter(adapter);

        View v = super.onCreateView(inflater, container, savedInstanceState);
        v.setBackgroundColor(Color.WHITE);
        v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, container.getHeight()));

        ((HomeActivity)getActivity()).addToLeftArrowClickHandlers(this);
        return v;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        ((HomeActivity)getActivity()).removeFromLeftArrowHandlers(this);
        // ((HomeActivity)getActivity()).showActionToggleButton();
        //((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.create_log_entry), true,true,true);
        if (mDismissListener != null) {
            mDismissListener.onDestroy();
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        ((HomeActivity)getActivity()).removeFromLeftArrowHandlers(this);
        //((HomeActivity)getActivity()).showActionToggleButton();
        //((HomeActivity)getActivity()).setCustomActionBarView(getActivity().getResources().getString(R.string.create_log_entry), true,true,true);
        if (mDismissListener != null) {
            mDismissListener.onDetach();
        }

    }


    @Override
    public void onTopBarItemClick(View aView) {
        if(aView.getId()== HomeActivity.LEFT_ARROW_ID)
        {
            this.dismiss();
        }

    }

    private class CustomOptionListAdapter extends ArrayAdapter<String>{
        private final Context context;
        private final List<String> lstServiceTypes;

        public CustomOptionListAdapter(Context context, int resource, List<String> objects) {
            super(context, resource, objects);
            this.context = context;
            this.lstServiceTypes = objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.custom_options_list, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.txtViewPredefinedServiceType);
            textView.setText(lstServiceTypes.get(position));
            if(hasExistsServiceType(position)){
                textView.setEnabled(false);
            }
            return rowView;
        }
    }
}
