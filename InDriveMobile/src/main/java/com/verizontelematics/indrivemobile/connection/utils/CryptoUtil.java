package com.verizontelematics.indrivemobile.connection.utils;

import android.content.Context;
import android.util.Log;

import com.verizontelematics.indrivemobile.utils.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by bijesh on 2/25/2015.
 */
public class CryptoUtil {


    private static byte[] generateKey(String password)
    {
        password = getSHA1Password(password);
        SecretKey skey = null;
        try {
            byte[] keyStart = password.getBytes("UTF-8");

            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
            sr.setSeed(keyStart);
            keyGenerator.init(128, sr);
            skey = keyGenerator.generateKey();
        } catch (Exception e){
            e.printStackTrace();
        }
        return skey.getEncoded();
    }

    private static String getSHA1Password(String password){
//        TODO: use sha1 hashing to hash the password: use userId as password
        return password;
    }


    private static byte[] encodeFile(byte[] key, byte[] fileData)
    {
        byte[] encrypted = null;
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            encrypted = cipher.doFinal(fileData);
        }catch (Exception e){
            e.printStackTrace();
        }

        return encrypted;
    }

    public static byte[] decodeFile(byte[] key, byte[] fileData) throws Exception
    {
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

        byte[] decrypted = cipher.doFinal(fileData);

        return decrypted;
    }

    public static void save(Context context,byte[] fileData) throws Exception{
//        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "your_folder_on_sd", "file_name");
//        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        FileOutputStream fos = context.openFileOutput("certificate.txt", Context.MODE_PRIVATE);
        byte[] yourKey = generateKey("password");
        byte[] fileBytes = encodeFile(yourKey, fileData);
        fos.write(fileBytes);
        fos.flush();
        fos.close();
    }

    public static void saveByteArrayToFile(Context context,byte[] fileData,String fileName){
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(fileData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static String getPasswordFromEncryptedFile(String fileName){
        String retString = null;
        BufferedReader input = null;
        try {
            File file = FileUtils.readInternalFile(fileName);
            input = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            StringBuilder buffer = new StringBuilder();
            while ((line = input.readLine()) != null) {
                buffer.append(line);
            }


            retString = buffer.toString();
            Log.d("retString password ", "read from file " + retString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return retString;
    }

}
