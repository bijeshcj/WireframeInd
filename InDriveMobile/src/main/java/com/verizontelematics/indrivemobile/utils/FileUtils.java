package com.verizontelematics.indrivemobile.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

//import com.google.common.io.ByteStreams;
//import com.google.common.io.ByteStreams;
import com.verizontelematics.indrivemobile.IndriveApplication;
import com.verizontelematics.indrivemobile.connection.utils.Base64Util;
import com.verizontelematics.indrivemobile.cryptography.CryptoManager;
import com.verizontelematics.indrivemobile.utils.security.PKCS12Attributes;


import org.bouncycastle.openssl.PEMWriter;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;
import org.bouncycastle.util.io.pem.PemObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.security.auth.x500.X500Principal;

/**
 * Created by bijesh on 4/13/2015.
 */
public class FileUtils {

   private static final String TAG = FileUtils.class.getCanonicalName();
   private static String fileName = "key.txt";
   private static String certFileName = "cert.txt";

    public static void saveFileToAppInternal(Context context,String key){
        FileOutputStream out = null;

        try {
                out = context.openFileOutput(fileName , Context.MODE_PRIVATE);
                out.write(key.getBytes());
                Log.d(TAG, "key stored successfully "+context.getFilesDir().getAbsolutePath());
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null)
                    out.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void saveCert(Context context,byte[] cert){
        FileOutputStream out = null;

        try {
//            out = context.openFileOutput(fileName , Context.MODE_PRIVATE);
            File file = new File(context.getFilesDir()+File.separator+certFileName);
            if(file == null){
                Log.d(TAG,"layer7 making file for saving cert "+file.createNewFile());
            }else {
                Log.d(TAG,"cert file deletion else part");
                file.delete();
                Log.d(TAG,"file deleted");
                file = new File(context.getFilesDir()+File.separator+certFileName);
                Log.d(TAG,"created new file");
            }
            out = new FileOutputStream(file);
            out.write(cert);
            Log.d(TAG, "cert stored successfully "+context.getFilesDir().getAbsolutePath());
//            File file = context.getFilesDir();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(out != null)
                    out.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

    }

    public static String readFileContentFromAppInternal(Context context){
        String retVal = "";

        BufferedReader input = null;
        File file = null;
        try {
            file = new File(context.getFilesDir(), fileName); // Pass getFilesDir() and "MyFile" to read file   // getCacheDir()

            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }


            retVal = buffer.toString();
            Log.d(TAG, "read from file "+retVal);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retVal;
    }


    public static String readCertFromAppInternal(Context context){
        String retVal = null;

        BufferedReader input = null;
        File file = null;
        try {
            file = new File(context.getFilesDir(), certFileName); // Pass getFilesDir() and "MyFile" to read file   // getCacheDir()

            byte[] bytesFromFile = getBytes(new FileInputStream(file));
//            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//            String line;
//            StringBuffer buffer = new StringBuffer();
//            while ((line = input.readLine()) != null) {
//                buffer.append(line);
//            }

//            com.google.common.io.Files files = null;

//            byte[] encryptedByteArray = ByteStreams.toByteArray(new FileInputStream(file));//IOUtils.toByteArray(new FileInputStream(file));
//            Log.d(TAG,"layer7 decrypted from IOUtil "+CryptoManager.decrypt(encryptedByteArray));

//            retVal = buffer.toString();
//            retVal = Base64.encodeToString(bytesFromFile,Base64.DEFAULT);
//            Log.d(TAG, "read from cert file "+retVal);//data.getBytes("UTF-8")
            retVal = CryptoManager.decrypt(bytesFromFile);
            Log.d(TAG,"after decrypted from file "+retVal);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public static byte[] getBytes(InputStream is) throws IOException {

        int len;
        int size = 1024;
        byte[] buf;


            size = is.available();
            buf = new byte[size];
            len = is.read(buf, 0, size);

        return buf;
    }

    public static byte[] readCertAsBytesFromAppInternal(Context context){
        byte[] retVal = null;

        BufferedReader input = null;
        File file = null;
        try {
            file = new File(context.getFilesDir(), certFileName); // Pass getFilesDir() and "MyFile" to read file   // getCacheDir()

            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }


            retVal = buffer.toString().getBytes();
//            Log.d(TAG, "read from cert file "+retVal);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retVal;
    }



    public static byte[] readKeyContentFromAppInternal(Context context){
        byte[] retVal = null;

        BufferedReader input = null;
        File file = null;
        try {
            file = new File(context.getFilesDir(), fileName); // Pass getFilesDir() and "MyFile" to read file   // getCacheDir()

            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuffer buffer = new StringBuffer();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }


            retVal = buffer.toString().getBytes();
            Log.d(TAG, "read from file bytes length "+retVal.length);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retVal;
    }

    public static File readInternalFile(String filename){
        File file = null;
        try{
            file = new File(IndriveApplication.getInstance().getFilesDir(),filename);
        }catch (Exception e){
            e.printStackTrace();
        }
        return file;
    }


    public static FileOutputStream getAppInternalFileOutputStream(String fileName){
        FileOutputStream out = null;

        try {
            out = IndriveApplication.getInstance().openFileOutput(fileName , Context.MODE_PRIVATE);
        }catch (IOException e){
            e.printStackTrace();
        }
        return out;
    }

    public static FileOutputStream getExternalFileOutputStream(String fileName){
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(new File(Environment.getExternalStorageDirectory()+ File.separator + fileName));
        }catch (IOException e){
            e.printStackTrace();
        }
        return out;
    }

    public static void printInternalFileDir(){
        File[] internalFiles = IndriveApplication.getInstance().getFilesDir().listFiles();
        for(File file:internalFiles){
            Log.d(TAG,"layer7 internal file "+file.getAbsolutePath());

        }

    }





}
