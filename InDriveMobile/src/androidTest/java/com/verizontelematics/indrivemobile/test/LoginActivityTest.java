package com.verizontelematics.indrivemobile.test;

import android.app.Activity;
import android.app.Application;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.verizontelematics.indrivemobile.R;
import com.verizontelematics.indrivemobile.activity.HomeActivity;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.activity.LoginActivity;
import com.verizontelematics.indrivemobile.activity.TermsConditionsActivity;

import junit.framework.Assert;

import java.util.Date;

@SuppressWarnings("ALL")
public class LoginActivityTest extends ActivityInstrumentationTestCase2 {

    private Solo solo;
    private EditText userNameLogin, password;
    private Button login;
    private Activity activity;




    public LoginActivityTest()
   {
        //put the name of launcher activity to start the application
       super("com.verizontelematics.indrivemobile.activity", LoginActivity.class);
   }

    public void setUp() throws Exception {
        super.setUp();
        solo= new Solo(getInstrumentation(),getActivity());
        activity= getActivity();
    }

    public void tearDown() throws Exception {

        solo.finishOpenedActivities();
    }


    //application not null

    public void testNotNullApplication()
    {
        Application obj= new IndriveApplication();
        assertNotNull(obj);
    }
    //test login details

    public void testLoginDetails()
    {
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password, "mohit");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.assertCurrentActivity("Expected EULA screen",TermsConditionsActivity.class);

    }

    //testing EULA screen
    public void testEULA()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        solo.sleep(1000);

        //clicking don't agree button
        solo.clickOnButton("Cancel");
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected EULA screen",TermsConditionsActivity.class);

        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);

        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(1000);


    }
    //testing tabs
    public void testTab()
    {

        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Location");
        solo.sleep(2000);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(2000);
    }

    //testing AlertHome Screen
    public void testAlertHome()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");
        solo.sleep(1000);

        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);

        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(1000);

        //clicking alerts home fragment
        solo.clickOnText("Alerts Home");
        solo.sleep(1000);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
    }


    //testing Back button functionality
    public void testExitApplication()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");
        solo.sleep(1000);

        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);

        solo.goBack();
        Application obj= new IndriveApplication();
        assertNull(obj);
    }


    //testing AlertHome Screen
    public void testAlertHomeOptions()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");
        solo.sleep(500);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);

        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);

        //clicking alerts home fragment
        solo.clickOnText("Alerts Home");
        solo.clickOnView(solo.getView(R.id.speedAlertsIV));



        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);

        //calling second option from the list
        solo.clickOnText("Geofence Alerts");
        solo.sleep(500);
        //clicking add button to go to geofence screen
        solo.clickLongOnView(solo.getView(R.id.imgBtn_add));
        // solo.clickOnImageButton(1);
        solo.sleep(2000);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //clicking the pencil to show dialog
        solo.clickLongOnView(solo.getView(R.id.more));
        solo.sleep(2000);


    }

    public void testValetAlert()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);

        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking valet alert from list
        solo.clickOnText("Valet Alert");

        solo.sleep(2000);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);

    }
    public void testSpeedAlert()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");

        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);

        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //clicking valet alert from list
        solo.clickOnText("Speed Alert");

        solo.sleep(2000);

    }


    public void testDiagnosticsAlert()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);

        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking valet alert from list
        solo.clickOnText("Diagnostics Alert");

        solo.sleep(2000);

    }

    public void testMaintenanceAlert()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");

        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking valet alert from list
        solo.clickOnText("Maintenance Alert");

        solo.sleep(2000);

    }

    public void testRecallAlert()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");

        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking valet alert from list
        solo.clickOnText("Recall Alert");

        solo.sleep(2000);

    }

    public void testHistoryAlert()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");

        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);

        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking valet alert from list
        solo.clickOnText("Alert History");

        solo.sleep(2000);

    }
    public void testLogout()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking valet alert from list
        solo.clickOnText("Logout");

        solo.sleep(2000);

    }



    //Location Section

    //testing tabs
    public void testLocationLocateVehicle()
    {

        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Location");
        solo.sleep(2000);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(2000);

        //clicking locate vehicle
        solo.clickOnText("Locate Vehicle");

        solo.sleep(2000);
    }



    //test Alerts Label
    public void testAlertsScreenLabel()
    {
        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);

        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(2000);
        Assert.assertTrue("Alerts".equals(solo.getCurrentActivity().getTitle()));


    }

    //testing location history
    public void testLocationLocationHistory() {

        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Location");
        solo.sleep(2000);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(2000);

        //clicking location history
        solo.clickOnText("Location History");
        solo.sleep(2000);
        //id's can be mapped here, instead of text search based click
        solo.clickOnText("S");
        solo.clickOnText("M");
        solo.clickOnText("S");
        solo.clickOnText("M");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);

        solo.clickOnText("S");

        solo.sleep(1000);
        //solo.clickOnView(solo.getView(activity.findViewById(R.id.)));
    }

    //test current activity label name

    public void testLocationScreenLabel()
    {
        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Location");
        solo.sleep(2000);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(2000);
        Assert.assertTrue("Location".equals(solo.getCurrentActivity().getTitle()));

        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);

    }

    //testing geofence
    public void testLocationGeofence() {

        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Location");
        solo.sleep(2000);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(2000);

        //clicking location history
        solo.clickOnText("Geofence");
        solo.sleep(1000);
        //id's can be mapped here, instead of text search based click


    }

    //Driving Data

    public void testDrivingData()
    {
        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Driving Data");
        solo.sleep(2000);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
    }

    //test current activity label name

    public void testDrivingDataScreenLabel()
    {
        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Driving Data");
        solo.sleep(2000);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(2000);
        Assert.assertTrue("Driving Data".equals(solo.getCurrentActivity().getTitle()));

    }



//Driving Data

    public void testDrivingDataMenuOption()
    {
        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Driving Data");
        solo.sleep(2000);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();

        //clicking location history
        solo.clickOnText("Driving Data");


        solo.clickOnButton("Total Miles");
        solo.sleep(300);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.clickOnButton("Max speed");
        solo.sleep(300);
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);

        solo.clickOnButton("Average Trip");
        solo.sleep(300);
        solo.clickOnButton("Cost of fuel");
        solo.sleep(300);
        solo.clickOnButton("City MPG");
        solo.sleep(300);
        solo.clickOnButton("Highway MPG");
        solo.sleep(300);


    }

    public void testDrivingDataMenuButtonClick()
    {
        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Driving Data");
        solo.sendKey(Solo.MENU);
        solo.goBack();
        solo.sleep(2000);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();

        //clicking location history
        solo.clickOnText("Driving Data");


        solo.sendKey(Solo.MENU);


    }

//Diagnostics test

    public void testDiagnostics()
    {
        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Diagnostics");
        solo.sleep(500);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();

        //clicking location history
        solo.clickOnText("Vehicle Health");
        solo.sleep(300);
        solo.clickInList(1);
        solo.clickInList(2);
        solo.clickInList(3);
        //opening Menu option for diagnostics
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking location history
        solo.clickOnText("Maintenance Log");

        solo.sleep(300);

    }


    public void testDiagnosticsMaintenanceLog()
    {
        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Diagnostics");
        solo.sleep(500);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();

        //clicking location history
        solo.clickOnText("Maintenance Log");

        solo.sleep(300);
        solo.clickLongOnView(solo.getView(R.id.btn_create_log));
        solo.sleep(300);
        View vSpinner =solo.getView(Spinner.class,0);
        solo.clickOnView(vSpinner);
        solo.scrollToTop();
        solo.clickOnView(solo.getView(TextView.class,1));
        EditText test=(EditText)activity.findViewById(R.id.alertNameET);
        solo.enterText(test,"test alert name text");
        solo.enterText((EditText)activity.findViewById(R.id.alertDescET),"alert description");

        solo.enterText((EditText)activity.findViewById(R.id.costET),"test alert name text");
        solo.sleep(500);
        solo.goBack();

        solo.clickLongOnView(solo.getView(R.id.btn_create_alert));
        solo.sleep(500);

        solo.goBack();
        //clicking custom switch


        solo.sleep(1000);

    }


    //test recall info in diagnostics
    public void testDiagnosticsRecallInfo()
    {
        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Diagnostics");
        solo.sleep(500);
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();

        //clicking location history
        solo.clickOnText("Recall Info");

        solo.sleep(300);
        solo.clickInList(0);
        solo.clickInList(1);
        solo.clickInList(2);

        solo.sleep(500);

    }


    //test help and preferences screen
    public void testHelpScreen()
    {
        solo.clickOnButton("Log In");
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("current activity should be HomeActivity", HomeActivity.class);
        solo.clickOnText("Diagnostics");
        solo.sleep(500);
        solo.clickOnText("Help and Preferences");


        solo.sleep(300);
        solo.clickInList(0);
        solo.waitForText("Frequently Asked Questions");
        solo.clickInList(1);
        solo.clickInList(2);

        solo.sleep(500);

    }

    public void SwipeLeftRight()
    {
        swipeToRight(10);
        swipeToRight(10);
        swipeToRight(10);
        swipeToRight(10);
        swipeToLeft(10);
        solo.sleep(500);
    }

    private void swipeToLeft(int stepCount) {
        Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        float xStart = width - 10 ;
        float xEnd = 10;
        solo.drag(xStart, xEnd, height / 2, height / 2, stepCount);
    }

    private void swipeToRight(int stepCount) {
        Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        float xStart = 10 ;
        float xEnd = width - 10;
        solo.drag(xStart, xEnd, height / 2, height / 2, stepCount);
    }
    //testing Back button functionality
    public void testHomeButtonPressed()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"mohit");
        solo.enterText(password,"mohit");
        solo.clickOnButton("Log In");
        solo.sleep(1000);

        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(1000);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);

        solo.sendKey(KeyEvent.KEYCODE_HOME);
        Application obj= new IndriveApplication();
        assertNotNull(obj);
    }

    public void testDiagnosticsMaintenanceLogValidationCheck()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking valet alert from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);

        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);

        solo.clickOnImageButton(8);
        solo.clickOnButton("Done");

        boolean actual = solo.searchText("Please select Alert type !!!");
        assertEquals("Service selection validation failed",true, actual);
        solo.sleep(1000);


    }



    public void testDiagnosticsMaintenanceLogInfo()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking valet alert from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);

        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);

        solo.clickOnImageButton(7);
        solo.sleep(500);
        solo.clickOnButton("Cancel");
        solo.sleep(1000);

    }

    public void testDiagnosticsMaintenanceInformationLabelText()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);
        String label = solo.getCurrentActivity().getActionBar().getTitle().toString();
        solo.sleep(1000);
        Assert.assertTrue("Maintenance Information".equals(label));        //solo.assert

    }


    public void testDiagnosticsMaintenanceServiceTypeCancel()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);

        solo.clickOnImageButton(8);
        solo.sleep(1000);

        solo.clickOnText("Type of Service");
        solo.sleep(500);

        solo.clickOnText("Cancel");
        solo.sleep(500);
        assertTrue(solo.searchText("Type of Service"));

    }
    public void testDiagnosticsMaintenanceServiceTypeSelection()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);

        solo.clickOnImageButton(8);
        solo.sleep(1000);

        solo.clickOnText("Type of Service");
        solo.sleep(500);
        solo.clickInList(1);
        solo.sleep(100);
        solo.clickOnText("Done");
        solo.sleep(500);
        assertTrue(solo.searchText("Maintenance Information"));

    }

    public void testDiagnosticsMaintenanceServiceTypeCustomSelection()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);

        solo.clickOnImageButton(8);
        solo.sleep(1000);

        solo.clickOnText("Type of Service");
        solo.sleep(500);
        solo.clickInList(2);
        solo.sleep(100);
        solo.clickOnText("Done");
        solo.sleep(500);
        boolean actual = solo.searchText("Please enter Custom Alert Name !!!");
        assertEquals("Service selection custom name validation failed",true, actual);
        solo.sleep(1000);


    }
    public void testDiagnosticsMaintenanceServiceTypeCustomLogEntry()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);

        solo.clickOnImageButton(8);
        solo.sleep(1000);

        solo.clickOnText("Type of Service");
        solo.sleep(500);
        solo.clickInList(2);
        solo.sleep(100);

        solo.enterText(0,"Custom Alert Name");


        solo.sleep(500);
        solo.clickOnText("Done");


        solo.sleep(1000);


    }

    public void testDiagnosticsMaintenanceServiceTypeCustomLogEntryAsEmptyString()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);

        solo.clickOnImageButton(8);
        solo.sleep(1000);

        solo.clickOnText("Type of Service");
        solo.sleep(500);
        solo.clickInList(2);
        solo.sleep(100);

        solo.enterText(0,"");


        solo.sleep(500);
        solo.clickOnText("Done");

        boolean actual = solo.searchText("Please enter Custom Alert Name !!!");
        assertEquals("Service selection custom name validation failed",true, actual);
        solo.sleep(1000);




    }

    public void testDiagnosticsMaintenanceLogDateSetting()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);

        solo.clickOnImageButton(8);
        solo.sleep(1000);

        solo.clickOnText("Type of Service");
        solo.sleep(500);
        solo.clickInList(0);
        solo.sleep(100);

        Date date= new Date();
        int todayDate= date.getDate();
        solo.clickOnText("Date");
        solo.sleep(500);
        solo.setDatePicker(0,date.getYear(),date.getMonth(),todayDate+1);

        solo.clickOnText("Done");
        solo.sleep(1000);

        solo.clickOnText("Done");
        solo.sleep(1000);
        assertTrue(solo.searchText("Maintenance Information"));

    }

    public void testDiagnosticsMaintenanceLogDateAndNegativeCostSetting()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);

        solo.clickOnImageButton(8);
        solo.sleep(1000);

        solo.clickOnText("Type of Service");
        solo.sleep(500);
        solo.clickInList(0);
        solo.sleep(100);

        Date date= new Date();
        int todayDate= date.getDate();
        solo.clickOnText("Date");
        solo.sleep(500);
        solo.setDatePicker(0,date.getYear(),date.getMonth(),todayDate+1);

        solo.clickOnText("Done");
        solo.sleep(1000);

        solo.clickOnText("Cost");
        solo.enterText(1,"-123");
        solo.sleep(1000);
        solo.clickOnText("Done");
        solo.sleep(1000);
        assertTrue(solo.searchText("Maintenance Information"));

    }


    public void testDiagnosticsMaintenanceLogEditModeLabelCheck()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);
        ListView listview =(ListView)solo.getView(R.id.maintenanceLogLV);
        View listElement =listview.getChildAt(2);
        solo.clickOnView(listElement);
        //solo.clickInList(5);
        solo.sleep(2000);
        assertTrue(solo.searchText("Edit Log Entry"));
        // solo.clickInList()
    }

    public void testDiagnosticsMaintenanceLogEditDeleteEntryCancel()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        solo.setActivityOrientation(Solo.LANDSCAPE);
        solo.setActivityOrientation(Solo.PORTRAIT);
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);
        ListView listview =(ListView)solo.getView(R.id.maintenanceLogLV);
        View listElement =listview.getChildAt(2);
        solo.clickOnView(listElement);
        //solo.clickInList(5);
        solo.sleep(500);
        solo.clickOnButton("Delete Maintenance Log");
        solo.sleep(1000);
        // solo.clickInList()
        solo.clickOnButton("Cancel");

        assertTrue(solo.searchText("Edit Log Entry"));

    }



    public void testDiagnosticsMaintenanceLogEditDeleteOkClick()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);
        ListView listview =(ListView)solo.getView(R.id.maintenanceLogLV);
        View listElement =listview.getChildAt(2);
        solo.clickOnView(listElement);
        //solo.clickInList(5);
        solo.sleep(500);
        solo.clickOnButton("Delete Maintenance Log");
        solo.sleep(1000);
        // solo.clickInList()
        solo.clickOnButton("OK");
        solo.sleep(200);
        assertTrue(solo.searchText("Maintenance Information"));

    }

    public void testDiagnosticsMaintenanceLogEditCheckLeftRightSwipePager()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);
        ListView listview =(ListView)solo.getView(R.id.maintenanceLogLV);
        View listElement =listview.getChildAt(2);
        solo.clickOnView(listElement);
        //solo.clickInList(5);
        solo.sleep(500);
        // solo.clickInList()
        SwipeLeftRight();

        solo.sleep(200);
        assertTrue(solo.searchText("Edit Log Entry"));

    }

    public void testDiagnosticsMaintenanceLogEditDoneButtonClick()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);
        ListView listview =(ListView)solo.getView(R.id.maintenanceLogLV);
        View listElement =listview.getChildAt(1);
        solo.clickOnView(listElement);
        //solo.clickInList(5);
        solo.sleep(500);
        // solo.clickInList()
        solo.clickOnButton("Done");
        solo.sleep(200);
        assertTrue(solo.searchText("Maintenance Information"));
        solo.sleep(100);

    }


    public void testDiagnosticsMaintenanceLogDeleteOption()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);
        ListView listview =(ListView)solo.getView(R.id.maintenanceLogLV);

        int listCount= listview.getCount();
        int countAfterDelete;
        if(listCount>0) {
            View listElement = listview.getChildAt(listCount-1);
            solo.clickLongOnView(listElement);
            solo.sleep(100);

            solo.clickOnButton("OK");
            solo.sleep(200);
            assertTrue(solo.searchText("Maintenance Information"));

        }
        solo.sleep(100);

    }


    public void testDiagnosticsMaintenanceLogDeletedListRefreshed()
    {
        //passing the login details for reaching the execution to EULA screen
        userNameLogin =(EditText)activity.findViewById(R.id.usernameET);
        password =(EditText)activity.findViewById(R.id.passwordET);
        login=(Button)activity.findViewById(R.id.loginBTN);
        solo.enterText(userNameLogin,"test");
        solo.enterText(password,"test");
        solo.clickOnButton("Log In");
        //click agree button
        solo.clickOnButton("I Agree");
        solo.sleep(500);
        solo.assertCurrentActivity("Expected Home Activity",HomeActivity.class);
        solo.clickOnText("Diagnostics");
        //click on Alert Navigation Drawer
        solo.clickOnActionBarHomeButton();
        solo.clickInList(1);
        solo.takeScreenshot();
        solo.sleep(500);
        //clicking maintenance information from list
        solo.clickOnText("Maintenance Information");
        solo.sleep(500);
        ListView listview =(ListView)solo.getView(R.id.maintenanceLogLV);

        int listCount= listview.getCount();
        int countAfterDelete;
        if(listCount>0) {
            View listElement = listview.getChildAt(listCount-1);
            solo.clickOnView(listElement);
            solo.sleep(200);
            solo.clickOnButton("Delete Maintenance Log");
            solo.sleep(100);
            solo.clickOnButton("OK");
            solo.sleep(200);
            assertTrue(solo.searchText("Maintenance Information"));
            countAfterDelete= listview.getCount();

            assertNotSame(listCount,countAfterDelete);

        }




        solo.sleep(100);

    }


}