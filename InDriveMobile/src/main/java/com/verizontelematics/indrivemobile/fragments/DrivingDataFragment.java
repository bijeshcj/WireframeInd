package com.verizontelematics.indrivemobile.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.github.mikephil.charting.utils.XLabels;
import com.github.mikephil.charting.utils.YLabels;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.activity.TimePeriodActivity;
import com.verizontelematics.indrivemobile.constants.UIConstants;
import com.verizontelematics.indrivemobile.controllers.AppController;
import com.verizontelematics.indrivemobile.controllers.DrivingDataController;
import com.verizontelematics.indrivemobile.controllers.UIInterface;
import com.verizontelematics.indrivemobile.customViews.dialogs.ToBeDecidedDialog;
import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.database.tables.DrivingDataTable;
import com.verizontelematics.indrivemobile.models.DrivingDataModel;
import com.verizontelematics.indrivemobile.models.Operation;
import com.verizontelematics.indrivemobile.models.data.DrivingData;
import com.verizontelematics.indrivemobile.models.uimodels.DrivingChartData;
import com.verizontelematics.indrivemobile.userprofile.UserRole;
import com.verizontelematics.indrivemobile.utils.AppConstants;
import com.verizontelematics.indrivemobile.utils.GoogleAnalyticsUtil;
import com.verizontelematics.indrivemobile.utils.ui.CalendarUtil;
import com.verizontelematics.indrivemobile.utils.ui.CalenderBuilder;
import com.verizontelematics.indrivemobile.utils.ui.DateDataFormat;
import com.verizontelematics.indrivemobile.utils.ui.DrivingDataCategory;
import com.verizontelematics.indrivemobile.utils.ui.DrivingDataImpl;
import com.verizontelematics.indrivemobile.utils.ui.Week;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Priyanga on 8/20/2014.
 */
public class DrivingDataFragment extends BaseSubUIFragment implements View.OnClickListener, Observer, UIInterface, HomeActivity.CustomTopBarItemsClickListener {


    private static final String TAG = DrivingDataFragment.class.getCanonicalName();
    private BaseUIScreenFragment mHomeFragment;
    private ImageView mPreviousRecords;
    private ImageView mSubsequentRecords;
    private TextView mTxtViewTotalMilesPeriod, mGraphText;
    private TextView mTxtViewMidHeader;
    private TextView mTxtViewTotalMiles, mTxtViewMaxSpeed, mTxtViewAvgTrip,
            mTxtViewCarbonFootPrint, mTxtViewCityMpg, mTxtViewHighwayMpg,mTxtViewTtMiles,mTxtViewMSpeed,
            mTxtViewATrip,mTxtViewCFP,mTxtViewCFPrint,mTxtViewMPG,mTxtViewHighway;
    private RelativeLayout mTileTotalMilesLayout;
    private RelativeLayout mMaxSpeed;
    private RelativeLayout mAvgTrip;
    private RelativeLayout mCarbonFootPrint;
    private RelativeLayout mCityMPG;
    private RelativeLayout mHighwayMPG;
    private Button mBtnShowDrivingData;

    private long mStartDate, mEndDate;
    private long mEndDateOnChose;
    private String mLocalPrefsTimePeriod;

    private static final int TREND_LINE_COLOR = Color.argb(255, 59, 172, 200);

    private BarChart mChart;
    protected String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
    };
    private String[] mWeekDays = new String[]{
            "S", "M", "T", "W", "T", "F", "S"
    };
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private CalenderBuilder mCalenderWeeksBuilder;
    private Week mWeek;
    private TextView mMonthYearLabel;
//    private static String mKey = "Total Miles";
    private CATEGORY mKey = CATEGORY.TOTAL_MILES;
    private String mTimePeriodOption = "This Week";
    private String defaultViewTest = "---";
    private ArrayList<Boolean> resetDisableXValuesList;
    private String mTimePeriodSelection;
    private long mCustomDateStart,mCustomDateEnd;
    private UserRole mUserRole = UserRole.ACTIVE;


    private enum CATEGORY {
        TOTAL_MILES("Total Miles"),
        MAX_SPEED("Max Speed"),
        AVERAGE_TRIP("Average Trip"),
        CARBON_FOOTPRINT("Carbon Footprint"),
        CITY_MPG("City MPG"),
        HIGHWAY_MPG("Highway MPG");

        CATEGORY(String selection) {
            this.selection = selection;
        }

        private String selection;

        public String getSelection() {
            return selection;
        }

    }

    public DrivingDataFragment() {
        super();
        //mHomeFragment=this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView;
        resetDisableXValuesList = new ArrayList<Boolean>();
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((HomeActivity) getActivity()).hideSlidingMenus();
            rootView = inflater.inflate(R.layout.driving_data_fragment_land, container, false);
        } else {
            ((HomeActivity) getActivity()).showSlidingMenus();
            rootView = inflater.inflate(R.layout.driving_data_fragment, container, false);
            init(rootView,true);

        }

        new GoogleAnalyticsUtil().trackScreens(IndriveApplication.getInstance(),getResources().getString(R.string.screen_driving_data));

        return rootView;

    }


    private void init(View rootView,final boolean refreshForData) {
        resetDisableXValuesList = iniDisableXValues();
        mBtnShowDrivingData = (Button) rootView.findViewById(R.id.txtViewSelectPeriod);

        mTxtViewTotalMilesPeriod = (TextView) rootView.findViewById(R.id.header_current_recalls_date_issued);
        mGraphText = (TextView) rootView.findViewById(R.id.miles_txtView);
        mTxtViewMidHeader = (TextView) rootView.findViewById(R.id.header_current_recalls_description);
        mTileTotalMilesLayout = (RelativeLayout) rootView.findViewById(R.id.tileTotalMiles);
        mMaxSpeed = (RelativeLayout) rootView.findViewById(R.id.tileMaxSpeed);
        mAvgTrip = (RelativeLayout) rootView.findViewById(R.id.tileAvgTrip);
        mCarbonFootPrint = (RelativeLayout) rootView.findViewById(R.id.carbonFootPrintRL);
        mCityMPG = (RelativeLayout) rootView.findViewById(R.id.cityMPGRL);
        mHighwayMPG = (RelativeLayout) rootView.findViewById(R.id.highwayRL);
        mTxtViewTtMiles = (TextView)rootView.findViewById(R.id.total_miles_txtView);
        mTxtViewTotalMiles = (TextView) rootView.findViewById(R.id.buttonTtlMiles);
        mTxtViewMaxSpeed = (TextView) rootView.findViewById(R.id.speedDesc);
        mTxtViewMSpeed = (TextView) rootView.findViewById(R.id.maxSpeed_txtView);
        mTxtViewAvgTrip = (TextView) rootView.findViewById(R.id.avgTripDesc);
        mTxtViewATrip = (TextView) rootView.findViewById(R.id.avgTrip_txtView);
        mTxtViewCarbonFootPrint = (TextView) rootView.findViewById(R.id.FOOTpRINTdESC);
        mTxtViewCFP = (TextView) rootView.findViewById(R.id.FOOTPRINT_txtView);
        mTxtViewCFPrint = (TextView) rootView.findViewById(R.id.lbs_co2_txtView);

        mTxtViewCityMpg = (TextView) rootView.findViewById(R.id.mpgDesc);
        mTxtViewMPG = (TextView) rootView.findViewById(R.id.MPG_txtView);
        mTxtViewHighwayMpg = (TextView) rootView.findViewById(R.id.highwayDesc);
        mTxtViewHighway = (TextView) rootView.findViewById(R.id.Highway_txtView);

//        mUserRole = UserFactory.initUserRole(getArguments());

        ImageView infoDrivingDataIV = (ImageView) rootView.findViewById(R.id.btn_info_driving_data);
        if (infoDrivingDataIV != null) {
            infoDrivingDataIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToBeDecidedDialog dialog = new ToBeDecidedDialog(getActivity(), getActivity().getResources().getString(R.string.info_driving_data));
                    dialog.setTitle(getResources().getString(R.string.about_driving_data));
                    dialog.setFlag(true);
                    dialog.show();

                }
            });
        }
        //
        mMonthYearLabel = (TextView) rootView.findViewById(R.id.txt_month_year_date_label);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            setClickListeners();

            mBtnShowDrivingData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_driving),
                            "DrivingDataEditTime");

                    if(mUserRole != null && mUserRole.toString().equalsIgnoreCase(UserRole.IN_ACTIVE.toString())){
                        Toast.makeText(getActivity(), "Inactive Role", Toast.LENGTH_LONG).show();
                        return;
                    }

                    Intent intent = new Intent(getActivity(), TimePeriodActivity.class);
                    startActivityForResult(intent, 1);
//                    refreshCategorySetToDefault(defaultViewTest);
                }
            });
        }
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.driving_data_list_swipe_refresh);
        if(mSwipeRefreshLayout != null)
         mSwipeRefreshLayout.setColorSchemeResources(R.color.driving_module_code, R.color.sub_header_color, R.color.driving_module_code, R.color.sub_header_color);
        if (mSwipeRefreshLayout != null)
            mSwipeRefreshLayout.setEnabled(false);

        if (mCalenderWeeksBuilder == null) {
            mCalenderWeeksBuilder = new CalenderBuilder(CalenderBuilder.Option.THIS_WEEK.ordinal(), "E/dd");
        }
        //if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && refreshForData)

        if(refreshForData) {
            mWeek = mCalenderWeeksBuilder.getWeek(0);
        }else{
            mWeek = mCalenderWeeksBuilder.getWeek(mCalenderWeeksBuilder.getWeekIndex());
        }
        initBarChart(rootView);
        initRecordViews(rootView);

        updateGraphLabels(mTimePeriodOption, mStartDate, mEndDate,refreshForData);

        DrivingDataController.instance().getDrivingDataModel().addObserver(this);
        DrivingDataController.instance().register(this);

        // work around to smooth the animation.
        // But code need to be removed
        // Actual fix : perform on animation end.
//        refreshUI(refreshForData);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT && refreshForData) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {


                    if (mTimePeriodOption.equals("Custom")) {
                        // update the startDate from saved instance
                        // update the end date from saved instance
                    } else {
                        mStartDate = mWeek.getStartTime();
                        mEndDate = mWeek.getEndTime();
                    }
                    loadDrivingDatabasedOnLocalPreference();
//                    DrivingDataController.instance().getDrivingData(getActivity(), mStartDate, mEndDate);
                    if (mSwipeRefreshLayout != null) {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                }
            }, 700);
        }
        else if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            refreshUI(refreshForData);
        }

        if(!refreshForData){
            StorageTransaction transaction = new StorageTransaction(getActivity());
            refreshCategory(DrivingDataImpl.INS.loadDrivingDataUI(transaction));
        }
       if(refreshForData) {
           initLocalPreference();
       }
    }
    // work around end.

    private void initLocalPreference(){
        String[] prefs = AppController.instance().getAppSettingsDrivingData();
        if(prefs != null){
            mLocalPrefsTimePeriod = prefs[0];
            mKey = getCategory(prefs[1]);
            System.out.println("$$$ local drv prefs time "+mLocalPrefsTimePeriod+" cater "+mKey.toString()+" prefs[1] "+prefs[1]);
        }
    }

    private CATEGORY getCategory(String category){
        if(category.equals(AppConstants.DrivingDataCategory.TOTAL_MILES.toString())){
            return CATEGORY.TOTAL_MILES;
        }else if(category.equals(AppConstants.DrivingDataCategory.MAX_SPEED.toString())){
            return CATEGORY.MAX_SPEED;
        }else if(category.equals(AppConstants.DrivingDataCategory.AVERAGE_TRIP.toString())){
            return CATEGORY.AVERAGE_TRIP;
        }else if(category.equals(AppConstants.DrivingDataCategory.CARBON_FOOTPRINT.toString())){
            return CATEGORY.CARBON_FOOTPRINT;
        }else if(category.equals(AppConstants.DrivingDataCategory.CITY_MPG.toString())){
            return CATEGORY.CITY_MPG;
        }else if(category.equals(AppConstants.DrivingDataCategory.HIGHWAY_MPG.toString())){
            return CATEGORY.HIGHWAY_MPG;
        }
        return CATEGORY.TOTAL_MILES;
    }

    private ArrayList<Boolean> iniDisableXValues(){
        resetDisableXValuesList = new ArrayList<Boolean>();
        for(int i=0;i<7;i++){
            resetDisableXValuesList.add(false);
        }
        return resetDisableXValuesList;
    }



    private void refreshChart(DrivingChartData drivingChartData) {

//        mChart.resetYRange(true);
//        setData(7, 30);
        setDrivingData(drivingChartData, 7);
        mChart.animateY(1000);
    }

    private void setDrivingData(DrivingChartData drivingChartData, int xCount) {
        ArrayList<Double> totalMiles = drivingChartData.getTotalMiles();

        ArrayList<String> xValueList = new ArrayList<String>();
        for (int i = 0; i < xCount; i++) {
            xValueList.add(mWeekDays[i % 7]);
        }

        ArrayList<BarEntry> yValueList = new ArrayList<BarEntry>();

        for (int i = 0; i < xCount; i++) {
//            float multiplier = (range + 1);
//            float val = (float) (Math.random() * multiplier);
            float val = totalMiles.get(i).floatValue();
            yValueList.add(new BarEntry(val, i));
        }

        BarDataSet set1 = new BarDataSet(yValueList, "DataSet");
//        set1.setColor(getResources().getColor(R.color.gray_driving_data_period));
        set1.setColor(getResources().getColor(R.color.gray_separator));
//        set1.setColor(R.drawable.bar_chart_color);
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xValueList, dataSets);

        mChart.setData(data);

        mChart.animateY(1000);


    }

    private void setClickListeners() {
        mTileTotalMilesLayout.setOnClickListener(this);
        mMaxSpeed.setOnClickListener(this);
        mAvgTrip.setOnClickListener(this);
        mCarbonFootPrint.setOnClickListener(this);
        mCityMPG.setOnClickListener(this);
        mHighwayMPG.setOnClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        cleanup();
        ((HomeActivity) getActivity()).setCustomActionBarView("", false, false, false, false);
        ((HomeActivity) getActivity()).showActionToggleButton();
        ((HomeActivity) getActivity()).setTitle("Driving Data");
        ((HomeActivity) getActivity()).showSlidingMenus();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            ((HomeActivity) getActivity()).showSlidingMenus();
            getActivity().getActionBar().show();
            ((HomeActivity) getActivity()).showTabBar(true);
        }
        cleanup();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cleanup();
    }

    private void cleanup() {
        DrivingDataController.instance().unregister(this);
        DrivingDataController.instance().getDrivingDataModel().deleteObserver(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            String timePeriodSelection = data.getStringExtra(UIConstants.DRIVING_DATA_TIME_PERIOD);

            if (timePeriodSelection.equals("Custom")) {
                this.mTimePeriodSelection = "Custom";
                mStartDate = data.getLongExtra("startDate", 0);
                mEndDate = data.getLongExtra("endDate", 0);

            } else {
                this.mTimePeriodSelection = "predefined periods";
            }
            mTimePeriodOption = timePeriodSelection;

            refreshUI(true);
            // make a webservice call.
//            Log.d(TAG,"month start date "+mStartDate+" enddate "+mEndDate);
            DrivingDataController.instance().getDrivingData(getActivity(), mStartDate, mEndDate);
            mChart.setDisabledDates(resetDisableXValuesList);
        }
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }


    private void loadDrivingDatabasedOnLocalPreference(){
        mTimePeriodOption = getTimePeriodOption(mLocalPrefsTimePeriod);

        refreshUI(true);
        // make a webservice call.
        System.out.println("$$$ loadingDrvi sdate "+mStartDate+" eDa "+mEndDate);
        DrivingDataController.instance().getDrivingData(getActivity(), mStartDate, mEndDate);
        mChart.setDisabledDates(resetDisableXValuesList);
    }

    private String getTimePeriodOption(String timePeriod){
        if(timePeriod.equalsIgnoreCase(AppConstants.DrivingDataTimePeriod.THIS_WEEK.toString())){
           return "This Week";
        }else if(timePeriod.equalsIgnoreCase(AppConstants.DrivingDataTimePeriod.LAST_WEEK.toString())){
           return "Last Week";
        }else if(timePeriod.equalsIgnoreCase(AppConstants.DrivingDataTimePeriod.THIS_MONTH.toString())){
           return "This Month";
        }else if(timePeriod.equalsIgnoreCase(AppConstants.DrivingDataTimePeriod.LAST_MONTH.toString())){
           return "Last Month";
        }
        return "This Week";
    }


    private void refreshUI(boolean refreshForData) {
        // update graph labels and navigation controllers.
//        Log.d(TAG,"month refreshUi start date "+mStartDate+" enddate "+mEndDate);
        updateGraphLabels(mTimePeriodOption, mStartDate, mEndDate,refreshForData);
        // update graph with initial '0' values

        updateGraph();
    }

//    private void updateTimePeriodData(String timePeriod, long startDate, long endDate) {
//        Log.d(TAG, "$$ timePeriod " + timePeriod);
//        updateUIForTimePeriod(timePeriod);
//        updateGraphLabels(timePeriod, startDate, endDate);
//
//    }

    private void updateGraphLabels(String timePeriod, long startDate, long endDate,boolean refreshForData) {

        // update user selection details
            updateUIForTimePeriod(timePeriod,startDate,endDate);

        // update calender weeks builder option.

        // we cannot really compare hardcoded strings
        // Better we should get the selected index or enum value from onActivityResult.
        if (mCalenderWeeksBuilder == null) {
            mCalenderWeeksBuilder = new CalenderBuilder(CalenderBuilder.Option.THIS_WEEK.ordinal(), "E/dd");
        }
        if (timePeriod.equals("This Week")) {
            if(refreshForData) {
                mCalenderWeeksBuilder.setOption(CalenderBuilder.Option.THIS_WEEK.ordinal());
                mWeek = mCalenderWeeksBuilder.getWeek(0);
                mStartDate = mWeek.getStartTime();
                mEndDate = mWeek.getEndTime();
            }
        } else if (timePeriod.equals("Last Week")) {
            if(refreshForData) {
                mCalenderWeeksBuilder.setOption(CalenderBuilder.Option.LAST_WEEK.ordinal());
                mWeek = mCalenderWeeksBuilder.getWeek(0);
                mStartDate = mWeek.getStartTime();
                mEndDate = mWeek.getEndTime();
            }
        } else if (timePeriod.equals("This Month")) {
            if(refreshForData) {
                mCalenderWeeksBuilder.setOption(CalenderBuilder.Option.THIS_MONTH.ordinal());
                mWeek = mCalenderWeeksBuilder.getWeek(0);
                mStartDate = mCalenderWeeksBuilder.getMonthBeginDate();
                mEndDate = mCalenderWeeksBuilder.getMonthEndDate();
                mEndDateOnChose = mCalenderWeeksBuilder.getMonthEndDate(mStartDate);
//                Log.d(TAG,"month mEndDateOnChose "+mEndDateOnChose);
            }
        } else if (timePeriod.equals("Last Month")) {
            if(refreshForData) {
                mCalenderWeeksBuilder.setOption(CalenderBuilder.Option.LAST_MONTH.ordinal());
                mStartDate = mCalenderWeeksBuilder.getMonthBeginDate();
                mEndDate = mCalenderWeeksBuilder.getMonthEndDate();
//                Log.d(TAG,"month mStartDate after selecting last mont "+mStartDate);
                mEndDateOnChose = mCalenderWeeksBuilder.getMonthEndDate(mStartDate);
            }

        } else if (timePeriod.equals("Custom")) {
            if(refreshForData) {
                mCalenderWeeksBuilder.setOption(CalenderBuilder.Option.CUSTOM.ordinal(), startDate, endDate);
                mTxtViewTotalMilesPeriod.setText(" (" + DateDataFormat.convertMillisAsDateString(getActivity(), startDate, false) + " - " + DateDataFormat.convertMillisAsDateString(getActivity(), endDate, false) + ")");
                mStartDate = startDate;
                mEndDate = endDate;
            }
        }
        // Stub to test the builder object
        for (int i = 0; i < mCalenderWeeksBuilder.getWeekCount(); i++) {
            if(refreshForData) {
                Week week = mCalenderWeeksBuilder.getWeek(i);
                if (week == null)
                    return;
//                Log.d(TAG, " Week (" + i + ") Start date :" + DateDataFormat.convertMillisAsDateString(getActivity(), week.getStartTime(), true));
//                Log.d(TAG, " Week (" + i + ") End data   : " + DateDataFormat.convertMillisAsDateString(getActivity(), week.getEndTime(), true));
            }
        }

        // Stub ended

        if(refreshForData) {
            // set the default week index
            mCalenderWeeksBuilder.setWeekIndex(0);
            // get the week
            mWeek = mCalenderWeeksBuilder.getWeek();

        }

    }

    private void updateGraph() {
        //setting the label selected by the user for the graph category
        mGraphText.setText(mKey.getSelection());

        List<DrivingData> drivingDataList = (List<DrivingData>) DrivingDataController.instance().getDrivingDataModel().getData();
        if (drivingDataList ==  null || drivingDataList.isEmpty()) {

            mPreviousRecords.setVisibility(View.GONE);
            mSubsequentRecords.setVisibility(View.GONE);
            mChart.clear();
            mChart.setNoDataText(getActivity().getResources().getString(R.string.no_chart_data_text));
            mChart.setNoDataTextDescription("");
//            return;
        }

        // update graph navigation buttons
        int leftNavigationVisibility = View.GONE;
        int rightNavigationVisibility = View.GONE;
        long weekCount = mCalenderWeeksBuilder.getWeekCount();
        int currentIndex = mCalenderWeeksBuilder.getCurrentWeekIndex();

        if (weekCount > 1) {

            leftNavigationVisibility = (currentIndex == 0) ? View.GONE : View.VISIBLE;
            rightNavigationVisibility = (currentIndex >= (weekCount - 1)) ? View.GONE : View.VISIBLE;

            boolean leftArrowInvisible=false,rightArrowInvisible=false;
            if(leftNavigationVisibility == View.GONE){
                leftArrowInvisible = true;
            }
            if(rightNavigationVisibility == View.GONE){
                rightArrowInvisible = true;
            }

            //        run this method only when the right arrow is disable || left arrow is disabled
            if(!mTimePeriodOption.equals("Custom")) {
                if (leftArrowInvisible) {
                    mChart.setDisabledDates(mWeek.getDisabledDaysStart());
                }
                if (rightArrowInvisible) {

                    mChart.setDisabledDates(mWeek.getDisabledDaysEnd1(mEndDateOnChose));
                }
            }else{
                if(leftArrowInvisible){
                    mChart.setDisabledDates(mWeek.getDisabledDaysStartCustom(mStartDate)); // user selected start date
                }
                if(rightArrowInvisible){
                    mChart.setDisabledDates(mWeek.getDisabledDaysEndCustom(mEndDate)); // user selected end date
                }
            }
        }

        mPreviousRecords.setVisibility(leftNavigationVisibility);
        mSubsequentRecords.setVisibility(rightNavigationVisibility);

        // update graph x-axis labels
        mWeekDays = mWeek.getDayLabels(getActivity());
        // update month and year
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("MMMM yyyy");

        mMonthYearLabel.setText(DateDataFormat.getMonth(mWeek.getStartTime(),mWeek.getEndTime()));


        // update graph y-axis data
        BarData barData = generateGraphData(mWeek);

        boolean hasWeekData = hasDataForWeek(weekData);

//        if(hasWeekData) {

            if (barData != null) {
                mChart.setNoChartEntryForWeek(hasWeekData);
                mChart.setNoDataText("");
                mChart.setNoDataTextDescription(getResources().getString(R.string.no_chart_data_text));
                mChart.setData(barData);

                mChart.animateY(1000);
            }
//        }else{
//            mChart.clear();
//            mChart.setNoDataText(getActivity().getResources().getString(R.string.no_chart_data_text));
//        }


    }

    private void setSelectedItem() {
        switch (mKey) {
            case TOTAL_MILES:
                mTileTotalMilesLayout.performClick();
                break;
            case MAX_SPEED:
                mMaxSpeed.performClick();
                break;
            case AVERAGE_TRIP:
                mAvgTrip.performClick();
                break;
            case CARBON_FOOTPRINT:
                mCarbonFootPrint.performClick();
                break;
            case CITY_MPG:
                mCityMPG.performClick();
                break;
            case HIGHWAY_MPG:
                mHighwayMPG.performClick();
                break;
            default:
                break;
        }
    }

    private void updateGraph(Week aWeek, DrivingChartData chartData) {
        // update graph navigation buttons
        int leftNavigationVisibility = View.GONE;
        int rightNavigationVisibility = View.GONE;
        long weekCount = mCalenderWeeksBuilder.getWeekCount();
        int currentIndex = mCalenderWeeksBuilder.getCurrentWeekIndex();
        if (weekCount > 1) {
            leftNavigationVisibility = (currentIndex == 0) ? View.GONE : View.VISIBLE;
            rightNavigationVisibility = (currentIndex >= (weekCount - 1)) ? View.GONE : View.VISIBLE;
        }

        mPreviousRecords.setVisibility(leftNavigationVisibility);
        mSubsequentRecords.setVisibility(rightNavigationVisibility);

        // update graph x-axis labels
        mWeekDays = aWeek.getDayLabels(getActivity());
        // update month and year
        @SuppressLint("SimpleDateFormat") SimpleDateFormat fmt = new SimpleDateFormat("MMMM yyyy");
        mMonthYearLabel.setText(DateDataFormat.convertMillisAsDateString(getActivity(), mWeek.getStartTime(), fmt, false));
        // update graph y-axis data
        BarData barData = generateGraphData(chartData, DrivingDataCategory.TOTAL_MILES);//generateGraphData(aWeek);
        if (barData != null) {
            mChart.setData(barData);

            mChart.animateY(1000);
        }
    }


    private void updateUIForTimePeriod(String timePeriod, long startDate, long endDate) {
        String customDate =  DateDataFormat.convertMillisAsDateString(getActivity(), startDate, false) + " - " + DateDataFormat.convertMillisAsDateString(getActivity(), endDate, false);
        if(!timePeriod.equalsIgnoreCase("Custom"))
            mTxtViewTotalMilesPeriod.setText(" (" + timePeriod + ")");
        else{
            mTxtViewTotalMilesPeriod.setText(" ("+customDate+")");
        }
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            if(!timePeriod.equalsIgnoreCase("Custom"))
               mTxtViewMidHeader.setText(getActivity().getResources().getString(R.string.driving_data_for) + " " + timePeriod);
            else {

                mTxtViewMidHeader.setText(getActivity().getResources().getString(R.string.driving_data_for) + " " + customDate);
            }
        }
    }


    private void initBarChart(View rootView) {
        mChart = (BarChart) rootView.findViewById(R.id.drivingDataGraph);
        mTxtViewTotalMilesPeriod = (TextView) rootView.findViewById(R.id.header_current_recalls_date_issued);
        mGraphText = (TextView) rootView.findViewById(R.id.miles_txtView);
        // enable the drawing of values
        mChart.setDrawYValues(true);

        mChart.setDrawValueAboveBar(false);

        mChart.setDescription("");

        mChart.setInvertYAxisEnabled(true);

        XLabels xLabels = mChart.getXLabels();

        XLabels.XLabelPosition pos = xLabels.getPosition();


        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        mChart.setMaxVisibleValueCount(60);

        // disable 3D
        mChart.set3DEnabled(true);

        setTouchForChart(mChart);

        // draw shadows for each bar that show the maximum value
        // mChart.setDrawBarShadow(true);

        mChart.setUnit("");

        // mChart.setDrawXLabels(false);

        mChart.setDrawGridBackground(false);
        mChart.setDrawHorizontalGrid(false);
        mChart.setDrawVerticalGrid(false);
        mChart.setDrawBarShadow(false);
        mChart.setDrawLegend(false);


        mChart.setDrawYLabels(false);

        // sets the text size of the values inside the chart
        mChart.setValueTextSize(10f);
        mChart.setValueTextColor(getResources().getColor(R.color.white));

        mChart.setDrawBorder(false);
        // mChart.setBorderPositions(new BorderPosition[] {BorderPosition.LEFT,
        // BorderPosition.RIGHT});

//        Typeface tf = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");

        XLabels xl = mChart.getXLabels();
        xl.setPosition(XLabels.XLabelPosition.TOP);
        xl.setCenterXLabelText(true);
        xl.setTextColor(getResources().getColor(R.color.white));
//        xl.setTypeface(tf);

        YLabels yl = mChart.getYLabels();
//        yl.setTypeface(tf);
        yl.setLabelCount(8);
        yl.setPosition(YLabels.YLabelPosition.LEFT);

        // custom value formatter
        mChart.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                if (value == 0)
                    return "";
                DecimalFormat format = new DecimalFormat();
                format.setMaximumFractionDigits(2);
                format.setMinimumIntegerDigits(1);
                if(mKey == CATEGORY.CARBON_FOOTPRINT)
                    format.setMinimumFractionDigits(2);
                else if(mKey == CATEGORY.CITY_MPG)
                    format.setMinimumFractionDigits(1);
                else if(mKey == CATEGORY.HIGHWAY_MPG)
                    format.setMinimumFractionDigits(1);
                return format.format(value);
            }
        });


//        mChart.setValueTypeface(tf);


//        setData(7, 30);
    }

    private void initRecordViews(View view) {

        RelativeLayout mArrowBackRL = (RelativeLayout) view.findViewById(R.id.backArrowRL);
        RelativeLayout mArrowForwardRL = (RelativeLayout) view.findViewById(R.id.forwardArrowRL);
        mPreviousRecords = (ImageView) view.findViewById(R.id.imgPreviousRecords);
        mSubsequentRecords = (ImageView) view.findViewById(R.id.imgSubsequentRecords);
        mArrowBackRL.setOnClickListener(this);
        mArrowForwardRL.setOnClickListener(this);
    }

    private void setTouchForChart(BarChart chart) {
        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);
        chart.setTouchEnabled(false);
    }
    float[] weekData;

    private BarData generateGraphData(Week week) {
        // populate x-axis & y-axis labels for the week
        ArrayList<String> xValues = new ArrayList<String>();
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        // get weekday labels for x-axis.
        String[] weekDayLabels = week.getDayLabels(getActivity());
        // get graph data for y-axis.
        weekData = populateDrivingData(week, mKey);


            for (int i = 0; i < Week.MAXIMUM_DAYS; i++) {
                xValues.add(weekDayLabels[i]);
                yValues.add(new BarEntry(weekData[i], i));
            }


        BarDataSet barDataSet = new BarDataSet(yValues, "DataSet");
        barDataSet.setColor(getResources().getColor(R.color.gray_separator));
//        barDataSet.setColor(R.drawable.bar_chart_color);


        barDataSet.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(barDataSet);

        BarData barData = new BarData(xValues, dataSets);

        return barData;
    }

    private boolean hasDataForWeek(float[] data){
        boolean retFlag = false;
        for(float f:data){
            if(f > 0)
                retFlag = true;
        }
        return retFlag;
    }

    private BarData generateGraphData(DrivingChartData chartData, DrivingDataCategory drivingDataCategory) {
        // populate x-axis & y-axis labels for the week
        ArrayList<String> xValues = new ArrayList<String>();
        ArrayList<BarEntry> yValues = new ArrayList<BarEntry>();
        // get weekday labels for x-axis.
        String[] weekDayLabels = mWeek.getDayLabels(getActivity());
        // get graph data for y-axis.
        ArrayList<Double> data = getDrivingData(chartData, drivingDataCategory);//populateDrivingData(week, mKey);
        for (int i = 0; i < Week.MAXIMUM_DAYS; i++) {
            xValues.add(weekDayLabels[i]);
            yValues.add(new BarEntry(data.get(i).floatValue(), i));
        }

        BarDataSet barDataSet = new BarDataSet(yValues, "DataSet");
        barDataSet.setColor(getResources().getColor(R.color.gray_separator));
//        barDataSet.setColor(R.drawable.bar_chart_gradient);
        barDataSet.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(barDataSet);

        BarData barData = new BarData(xValues, dataSets);

        return barData;
    }

    private ArrayList<Double> getDrivingData(DrivingChartData drivingChartData, DrivingDataCategory drivingDataCategory) {
        ArrayList<Double> returnList = null;
        switch (drivingDataCategory) {
            case TOTAL_MILES:
                returnList = drivingChartData.getTotalMiles();
                break;
            case MAX_SPEED:
                returnList = drivingChartData.getMaxSpeeds();
                break;
            case AVERAGE_TRIP:
                returnList = drivingChartData.getAverageTrip();
                break;
            case CARBON_FOOTPRINT:
                returnList = drivingChartData.getCarbonFootPrints();
                break;
            case CITY_MPG:
                returnList = drivingChartData.getCityMPG();
                break;
            case HIGHWAY_MPG:
                returnList = drivingChartData.getHighwayMPG();
                break;
        }
        return returnList;
    }

    private float[] populateDrivingData(Week week, CATEGORY key) {
        float[] data = new float[Week.MAXIMUM_DAYS];
        // initialize with zero
        for (int i = 0; i < Week.MAXIMUM_DAYS; i++)
            data[i] = 0;
        // get driving data list
        List<DrivingData> drivingDataList = (List<DrivingData>) DrivingDataController.instance().getDrivingDataModel().getData();
        if (drivingDataList == null) {
            return data;
        }
        // copy to mList for logic.
        ArrayList<DrivingData> mList = new ArrayList<DrivingData>(drivingDataList);
        long startGMTTime = CalendarUtil.getGMTTime(week.getStartTime());

        if (drivingDataList != null) {
            // get the point where the date matches.
            while (!mList.isEmpty() && mList.get(0) != null) {
                DrivingData dd = mList.get(0);
                if (dd == null)
                    break;
                if (dd.getDrivingDate() >= startGMTTime) {
                    break;
                }
                mList.remove(0);
            }


            // populate the graph data for 7 days.
            for (int i = 0; i < Week.MAXIMUM_DAYS; i++) {
                // if list is empty
                if (mList.isEmpty()) {
                    return data;
                }

                DrivingData dd = mList.get(0);

//                Log.d(TAG, " week day " + DateDataFormat.convertMillisAsDateString(getActivity(), week.getDay(i), false)
//                        + " - " + DateDataFormat.convertMillisAsDateString(getActivity(), dd.getDrivingDate(), false));

                // both day are matches
                int diffDays = CalendarUtil.compareDates(week.getDay(i),dd.getDrivingDate());
//                Log.d(TAG,"$$ diffDays "+diffDays);
                if (diffDays == 0) {
                    // depends on user key interest
                    // populate y-axis values.
                    String str = "0";
                    // below hardcoded logic need to refactored.
                     if (key== CATEGORY.CARBON_FOOTPRINT) {
                        str = dd.getCarbonFootprintLbs();
                    }
                    try {
//                        data[i] = Float.parseFloat(str);
                        DecimalFormat format2decimals = new DecimalFormat();
                        format2decimals.setMinimumFractionDigits(2);
                        format2decimals.setMaximumFractionDigits(2);
                        format2decimals.setMinimumIntegerDigits(1);
                        data[i] = Float.parseFloat(format2decimals.format(Double.valueOf(str)));
//                        Log.v("DATA[i]inCarbonFP", ""+Float.valueOf(format2decimals.format(Double.valueOf(str))));
                    } catch (NumberFormatException ne) {
                        data[i] = 0.00f;
                    }
                    if (key==CATEGORY.MAX_SPEED) {
                        data[i] = Math.round(dd.getMaxSpeed());
                    }
                    if (key==CATEGORY.AVERAGE_TRIP) {
                        String cityMilesAvg= dd.getCityMiles();
                        String highwayMiles= dd.getHighwayMiles();
                        String totalTrips= dd.getTrips();
                        Double cityMilesDouble= Double.parseDouble(cityMilesAvg);
                        Double highwayMilesDouble= Double.parseDouble(highwayMiles);
                        Double totalTripsPerDayDouble= Double.parseDouble(totalTrips);
                        try {

                            if(totalTripsPerDayDouble > 0)
                               data[i] = Math.round((cityMilesDouble+highwayMilesDouble) /totalTripsPerDayDouble);
                            else
                               data[i] = 0;

                        }
                        catch (NumberFormatException ne)
                        {
                            data[i]=0;
                        }

                    }
                    if (key==CATEGORY.CITY_MPG) {

                        String cityMiles = dd.getCityMiles();
                        String cityGallons = dd.getCityGallons();
                        Float cityMilesTemp = Float.parseFloat(cityMiles);
                        Float cityGallonsTemp = Float.parseFloat(cityGallons);
                        try {
                            if(!Double.isInfinite(cityMilesTemp / cityGallonsTemp) && cityGallonsTemp >0)

                            {
                                DecimalFormat format1decimal = new DecimalFormat();
                                format1decimal.setMaximumFractionDigits(1);
                                format1decimal.setMinimumFractionDigits(1);
                                format1decimal.setMinimumIntegerDigits(1);
                                data[i] = Float.valueOf(format1decimal.format(cityMilesTemp / cityGallonsTemp));
                            }
                            else
                            {
                                data[i]=0.0f;
                            }
                        }
                        catch (NumberFormatException ne)
                        {
                            data[i]=0.0f;
                        }
                        //str = dd.getCityMiles();
                    }
                    if (key==CATEGORY.HIGHWAY_MPG) {
                        str = dd.getHighwayMiles();

                        String highwayMiles = dd.getHighwayMiles();
                        String highwayGallons = dd.getHighwayGallons();
                        Float highwayMilesTemp = Float.parseFloat(highwayMiles);
                        Float highwayGallonsTemp = Float.parseFloat(highwayGallons);
                        try {
                            if(!Float.isInfinite(highwayMilesTemp / highwayGallonsTemp) && highwayGallonsTemp >0) {
                                DecimalFormat format1decimal = new DecimalFormat();
                                format1decimal.setMaximumFractionDigits(1);
                                format1decimal.setMinimumFractionDigits(1);
                                format1decimal.setMinimumIntegerDigits(1);
                                data[i] = Float.valueOf(format1decimal.format(highwayMilesTemp / highwayGallonsTemp));
                            }
                            else
                            {
                                data[i]=0.0f;
                            }
                        }
                        catch (NumberFormatException ne)
                        {
                            data[i]=0.0f;
                        }
                    }

                    if (key==CATEGORY.TOTAL_MILES) {
                        String strCityMiles = dd.getCityMiles();
                        String strHighwayMiles = dd.getHighwayMiles();
                        try {
                            long cityMiles = Math.round(Float.parseFloat(strCityMiles));
                            data[i] = cityMiles + Math.round(Float.parseFloat(strHighwayMiles));
                        } catch (NumberFormatException ne) {
                            data[i] = 0;
                        }
                    }
//                    Log.d(TAG, " graph y values " + i + " : " + data[i]);

                    mList.remove(0);
                }else{
//                    Log.d(TAG, "diff greater than day");
                }
            }
        }
        return data;
    }







    @Override
    public void onResume() {
        super.onResume();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
//        Log.d(TAG, "onResume");
        if (mChart != null) {
            //mChart.animateY(2000);
//            Log.d(TAG, DateDataFormat.convertMillisAsDateString(getActivity(),
//                    DrivingDataUtil.getWeekLastDay(Calendar.getInstance().getTimeInMillis()), true));
        }
        //      ((HomeActivity)getActivity()).hideCustomActionBar("Driving Data");

    }

    @Override
    public void onPause() {
        super.onPause();


//         DrivingDataController.instance().getDrivingDataModel().clear();
    }




    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.backArrowRL) {
            // what the purpose of this code here
//            below line is for testing purpose once tested it will be removed
            //DBUtils.pullDbFromLocalStorageToSDCard();
            ////////////////////////////////////////
            mWeek = mCalenderWeeksBuilder.getPreviousWeek();
            mChart.setDisabledDates(resetDisableXValuesList);
//            updateGraphXAxis(mWeek);
//            setChartData();



        } else if (v.getId() == R.id.forwardArrowRL) {
            mWeek = mCalenderWeeksBuilder.getNextWeek();
            mChart.setDisabledDates(resetDisableXValuesList);


//            updateGraphXAxis(mWeek);
//            setChartData();


        } else if (v.getId() == R.id.tileTotalMiles) {

            new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_driving),
                    "DrivingDataMiles");

            mKey = CATEGORY.TOTAL_MILES;
            mGraphText.setText(mKey.getSelection());
            mTileTotalMilesLayout.requestFocusFromTouch();
            TextView[] selectedViews = {mTxtViewTtMiles,mTxtViewTotalMiles};
            TextView[] unsSelectedViews = {mTxtViewMaxSpeed,mTxtViewMSpeed,mTxtViewATrip, mTxtViewAvgTrip,
                    mTxtViewCarbonFootPrint,mTxtViewCFP,mTxtViewCFPrint,mTxtViewCityMpg,mTxtViewMPG,mTxtViewHighwayMpg,mTxtViewHighway};
            setComponentsColor(getResources().getColor(R.color.white),getResources().getColor(R.color.gray),selectedViews,unsSelectedViews);


        } else if (v.getId() == R.id.tileMaxSpeed) {

            new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_driving),
                    "DrivingDataSpeed");
            mKey = CATEGORY.MAX_SPEED;
            mGraphText.setText(mKey.getSelection());
            mMaxSpeed.requestFocusFromTouch();
            TextView[] selectedViews = {mTxtViewMaxSpeed,mTxtViewMSpeed};
            TextView[] unsSelectedViews = {mTxtViewTtMiles,mTxtViewTotalMiles,mTxtViewATrip, mTxtViewAvgTrip,
                    mTxtViewCarbonFootPrint,mTxtViewCFP,mTxtViewCFPrint,mTxtViewCityMpg,mTxtViewMPG,mTxtViewHighwayMpg,mTxtViewHighway};
            setComponentsColor(getResources().getColor(R.color.white),getResources().getColor(R.color.gray),selectedViews,unsSelectedViews);


        } else if (v.getId() == R.id.tileAvgTrip) {
            new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_driving),
                    "DrivingDataTrip");
            mKey = CATEGORY.AVERAGE_TRIP;
            mGraphText.setText(mKey.getSelection());
            mAvgTrip.requestFocusFromTouch();
            TextView[] selectedViews = {mTxtViewATrip, mTxtViewAvgTrip};
            TextView[] unsSelectedViews = {mTxtViewMaxSpeed,mTxtViewMSpeed,mTxtViewTtMiles,mTxtViewTotalMiles,
                    mTxtViewCarbonFootPrint,mTxtViewCFP,mTxtViewCFPrint,mTxtViewCityMpg,mTxtViewMPG,mTxtViewHighwayMpg,mTxtViewHighway};
            setComponentsColor(getResources().getColor(R.color.white),getResources().getColor(R.color.gray),selectedViews,unsSelectedViews);


        } else if (v.getId() == R.id.carbonFootPrintRL) {

            new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_driving),
                    "DrivingDataFootPrint");
            mKey = CATEGORY.CARBON_FOOTPRINT;
            mGraphText.setText(mKey.getSelection());
            mCarbonFootPrint.requestFocusFromTouch();
            TextView[] selectedViews = {mTxtViewCarbonFootPrint,mTxtViewCFP,mTxtViewCFPrint};
            TextView[] unsSelectedViews = {mTxtViewMaxSpeed,mTxtViewMSpeed,mTxtViewATrip, mTxtViewAvgTrip,
                    mTxtViewTtMiles,mTxtViewTotalMiles,mTxtViewCityMpg,mTxtViewMPG,mTxtViewHighwayMpg,mTxtViewHighway};
            setComponentsColor(getResources().getColor(R.color.white),getResources().getColor(R.color.gray),selectedViews,unsSelectedViews);


        } else if (v.getId() == R.id.cityMPGRL) {

            new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_driving),
                    "DrivingDataCity");
            mKey = CATEGORY.CITY_MPG;
            mGraphText.setText(mKey.getSelection());
            mCityMPG.requestFocusFromTouch();
            TextView[] selectedViews = {mTxtViewCityMpg,mTxtViewMPG};
            TextView[] unsSelectedViews = {mTxtViewMaxSpeed,mTxtViewMSpeed,mTxtViewATrip, mTxtViewAvgTrip,
                    mTxtViewCarbonFootPrint,mTxtViewCFP,mTxtViewCFPrint,mTxtViewTtMiles,mTxtViewTotalMiles,mTxtViewHighwayMpg,mTxtViewHighway};
            setComponentsColor(getResources().getColor(R.color.white),getResources().getColor(R.color.gray),selectedViews,unsSelectedViews);


        } else if (v.getId() == R.id.highwayRL) {
            new GoogleAnalyticsUtil().trackEvents(IndriveApplication.getInstance(),getResources().getString(R.string.category_driving),
                    "DrivingDataHighway");
            mKey = CATEGORY.HIGHWAY_MPG;
            mGraphText.setText(mKey.getSelection());
            mHighwayMPG.requestFocusFromTouch();
            TextView[] selectedViews = {mTxtViewHighwayMpg,mTxtViewHighway};
            TextView[] unsSelectedViews = {mTxtViewMaxSpeed,mTxtViewMSpeed,mTxtViewATrip, mTxtViewAvgTrip,
                    mTxtViewCarbonFootPrint,mTxtViewCFP,mTxtViewCFPrint,mTxtViewCityMpg,mTxtViewMPG,mTxtViewTtMiles,mTxtViewTotalMiles};
            setComponentsColor(getResources().getColor(R.color.white),getResources().getColor(R.color.gray),selectedViews,unsSelectedViews);


        }
        updateGraph();
    }

    private void setComponentsColor(int selectedColor,int unSelectedColor,TextView[] selectedComponents,TextView[] unSelectedComponents){
            for (TextView tv : selectedComponents) {
                tv.setTextColor(selectedColor);
            }
            for (TextView tv : unSelectedComponents) {
                tv.setTextColor(unSelectedColor);
            }
    }

//    private void setChartData(){
//        mChart.resetYRange(true);
//        setData(7, 30);
//        mChart.animateY(1000);
//    }

    @Override
    public void update(final Observable observable, final Object object) {
//        Log.d(TAG, "$$$ update received");

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (DrivingDataModel.class.isInstance(observable) ) {
//                    Log.d(TAG, "$$ this is new driving data");
                    StorageTransaction transaction = new StorageTransaction(getActivity());
                    transaction.deleteAllDataForTable(DrivingDataTable.TABLE_NAME_DRIVING_DATA);
//                    Log.d(TAG, "$$$ clearing all the driving data");
                    if (object != null) {
                        List<DrivingData> drivingData = (List<DrivingData>) object;
//                        Log.d(TAG, "$$$ after casting driving data " + drivingData+" size "+drivingData.size());
                        transaction.insertDrivingData(drivingData);
                        mCustomDateStart = drivingData.get(0).getDrivingDate();
                        mCustomDateEnd = drivingData.get(drivingData.size()-1).getDrivingDate();

                    }
//                    Log.d(TAG, "$$ getting maxSpeed");
//                    DrivingDataScreenData screenData = DrivingDataImpl.INS.loadDrivingDataUI(transaction, DrivingDataCategory.TOTAL_MILES, mStartDate, mEndDate);
                    refreshCategory(DrivingDataImpl.INS.loadDrivingDataUI(transaction));

//                    updateGraphLabels("This Week", mStartDate, mEndDate);
//                    transaction.getCategoryDrivingData("",mWeek.getStartTime(),DrivingDataCategory.TOTAL_MILES,30);
//                    DBUtils.pullDbFromLocalStorageToSDCard();

                    updateGraph();
                    if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                        setSelectedItem();
                    }

                }
            }
        });
    }

    //Resetting UI to default values
    private void refreshCategorySetToDefault(String defaultText) {

        mTxtViewTotalMiles.setText(defaultText);
        mTxtViewMaxSpeed.setText(defaultText);
        mTxtViewAvgTrip.setText(defaultText);
        mTxtViewCarbonFootPrint.setText(defaultText);
        mTxtViewCityMpg.setText(defaultText);
        mTxtViewHighwayMpg.setText(defaultText);

    }


    //Setting UI values as per the data
    private void refreshCategory(double[] categoryData) {
        if (mTxtViewTotalMiles == null
                || mTxtViewMaxSpeed ==  null
                || mTxtViewAvgTrip ==  null
                || mTxtViewCarbonFootPrint == null
                || mTxtViewCityMpg == null
                || mTxtViewHighwayMpg == null)
            return;

        if (categoryData != null &&   (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)) {
            String defaultZero = "0";
            mTxtViewTotalMiles.setText(categoryData[0] == 0 || Double.isNaN(categoryData[0]) ? defaultZero : Math.round(categoryData[0]) + "");
            mTxtViewMaxSpeed.setText(categoryData[1] == 0 || Double.isNaN(categoryData[1]) ? defaultZero : Math.round(categoryData[1]) + "");
            mTxtViewAvgTrip.setText(categoryData[2] == 0 || Double.isNaN(categoryData[2]) ? defaultZero : Math.round(categoryData[2]) + "");

            DecimalFormat format2decimals = new DecimalFormat();
            format2decimals.setMinimumFractionDigits(2);
            format2decimals.setMaximumFractionDigits(2);
            format2decimals.setMinimumIntegerDigits(1);
            String defaultZeroWithDecimals = "0.00";
            mTxtViewCarbonFootPrint.setText(categoryData[3] == 0 || Double.isNaN(categoryData[3]) ? defaultZeroWithDecimals : format2decimals.format(categoryData[3]) + "");

            DecimalFormat format1decimal = new DecimalFormat();
            format1decimal.setMaximumFractionDigits(1);
            format1decimal.setMinimumFractionDigits(1);
            format1decimal.setMinimumIntegerDigits(1);
            String defaultZeroWithDecimal = "0.0";
            mTxtViewCityMpg.setText(categoryData[4] == 0 || Double.isNaN(categoryData[4]) ? defaultZeroWithDecimal : format1decimal.format(categoryData[4]) + "");

            mTxtViewHighwayMpg.setText(categoryData[5] == 0 || Double.isNaN(categoryData[5]) ? defaultZeroWithDecimal : format1decimal.format(categoryData[5]) + "");

            setSelectedItem();
        }
    }

    public String removeDecimal(double value) {
        String stringValue = Double.toString(value);
        return stringValue.substring(0, stringValue.length() - 2);
    }

    @Override
    public void onProgress(Operation opr) {

        if (opr == null || mSwipeRefreshLayout == null)
            return;
        if (opr.getId() == Operation.OperationCode.GET_DRIVING_DATA.ordinal()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!mSwipeRefreshLayout.isRefreshing()) {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                }
            });

        }
    }

    @Override
    public void onError(Operation opr) {
        if (opr == null)
            return;
        if (opr.getId() == Operation.OperationCode.GET_DRIVING_DATA.ordinal()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
                        mSwipeRefreshLayout.setRefreshing(false);
                }
            });

        }
    }

    @Override
    public void onSuccess(Operation opr) {
        if (opr == null)
            return;
        if (opr.getId() == Operation.OperationCode.GET_DRIVING_DATA.ordinal()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing())
                        mSwipeRefreshLayout.setRefreshing(false);
                }
            });

        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);

//        Log.v("onConfigurationChanged", "Driving Data Screen");

        if (getActivity() == null)
            return;
        //Work around for all onConfigurationChanged being called.

        LayoutInflater inflater = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater == null)
            return;

        View newView = null;

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((HomeActivity) getActivity()).hideSlidingMenus();
            getActivity().getActionBar().hide();
            newView = inflater.inflate(R.layout.driving_data_fragment_land, null);
        } else {
            ((HomeActivity) getActivity()).showSlidingMenus();
            getActivity().getActionBar().show();
            ((HomeActivity) getActivity()).showTabBar(true);
            newView = inflater.inflate(R.layout.driving_data_fragment, null);
        }



        if (newView == null)
            return;
        newView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        init(newView,false);



        ViewGroup rootView = (ViewGroup) getView();
        rootView.removeAllViews();
        rootView.addView(newView);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).setCustomActionBarView(getActivity().getResources().getStringArray(R.array.driving_data_option_list_array)[0],
                true, false, false,false);

    }

    @Override
    public void onTopBarItemClick(View aView) {
        if (aView.getId() == HomeActivity.LEFT_ARROW_ID) {
            removeFragment();
        }
    }

    private void removeFragment() {
        getHomeFragment().popFragmentStack();
    }



}
