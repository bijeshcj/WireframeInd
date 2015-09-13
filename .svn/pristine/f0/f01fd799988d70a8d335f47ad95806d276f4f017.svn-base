package com.verizontelematics.indrivemobile.utils.ui;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.verizontelematics.indrivemobile.constants.SQLiteConstants;
import com.verizontelematics.indrivemobile.database.StorageTransaction;
import com.verizontelematics.indrivemobile.database.tables.AccountInfoTable;
import com.verizontelematics.indrivemobile.database.tables.DiagnosticInfoTable;
import com.verizontelematics.indrivemobile.database.tables.NotificationPreferenceInfoTable;
import com.verizontelematics.indrivemobile.database.tables.VehicleHealthStatusTable;
import com.verizontelematics.indrivemobile.database.tables.VehicleTable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Created by bijesh on 10/13/2014.
 */
public class DBUtils implements SQLiteConstants{

    private static final String TAG = DBUtils.class.getCanonicalName();

    public static String generateSortQuery(String query,boolean[] isAscDesc,String... columns){
        StringBuilder builder = new StringBuilder(query);
        if(isAscDesc.length != columns.length){
            System.err.println("Incorrect parameter: number of columns must match sorting parameters");
            return null;
        }
        for(int i=0;i<columns.length;i++){
            String ascDesc,ascDecWC;
            if(isAscDesc[i]){
                ascDesc = " ASC ";
                ascDecWC = " ASC, ";
            }else{
                ascDesc = " DESC ";
                ascDecWC = " DESC, ";
            }
            if(i == 0) {
                if(columns.length == 1)
                    builder.append(" ORDER BY  ").append(columns[i]).append(ascDesc);
                else
                    builder.append(" ORDER BY  ").append(columns[i]).append(ascDecWC);
            }else if(i == (columns.length-1)) {
                builder.append(" ").append(columns[i]).append(ascDesc);
            }
            else {
                builder.append(" ").append(columns[i]).append(ascDecWC);
            }
        }
        Log.d(TAG,"$$$ sort query "+builder.toString());
        return builder.toString();
    }

    public static void pullDbFromLocalStorageToSDCard(){
        Log.d(TAG, "pullDbFromLocalStorageToSDCard");
        File outputFile = null,inputFile=null;
        FileOutputStream fos = null;
        InputStream ips = null;
        try{
//			outputFile = new File(Environment.getExternalStorageDirectory()+"/sample3.db");
            outputFile = new File(Environment.getExternalStorageDirectory()+"/"+DATABASE_NAME);

            inputFile = new File(Environment.getDataDirectory()+"/data/com.verizontelematics.indrivemobile/databases/"+DATABASE_NAME);
            if(!outputFile.exists()){

                Log.d(TAG,"creating new outputFile");
                outputFile.createNewFile();
            }else{
                Log.d(TAG,"deleting and creating new outputFile");
                outputFile.delete();
                outputFile.createNewFile();
            }
            fos = new FileOutputStream(outputFile);
            ips = new FileInputStream(inputFile);
            byte[] buffer = new byte[1024];
            int length;
            while((length = ips.read(buffer)) > 0){
                fos.write(buffer,0,length);
            }
            fos.flush();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(fos != null)
                    fos.close();
                if(ips != null)
                    ips.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void clearLoginModuleDBData(Context context){
        StorageTransaction transaction = new StorageTransaction(context);
        transaction.deleteAllDataForTable(AccountInfoTable.TABLE_NAME_ACCOUNTINFO);
        transaction.deleteAllDataForTable(VehicleTable.TABLE_NAME_VEHICLE);
        transaction.deleteAllDataForTable(NotificationPreferenceInfoTable.TABLE_NAME_NOTIFICATIONPREFERENCEINFO);
        Log.d(TAG,"$$$ cleared previous Login module data");
    }

    public static void clearVehicleHealthData(Context context){
        StorageTransaction transaction = new StorageTransaction(context);
        transaction.deleteAllDataForTable(VehicleHealthStatusTable.TABLE_NAME_HEALTH_STATUS);
        transaction.deleteAllDataForTable(DiagnosticInfoTable.TABLE_NAME_DIAGNOSTIC_INFO);
        Log.d(TAG,"$$$ cleared previous Vehicle Health module data");
    }

}
